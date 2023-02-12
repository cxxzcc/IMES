package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.constants.CommonConstants;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.constant.SnTypeEnum;
import com.itl.mes.core.api.dto.ProductStateUpdateDto;
import com.itl.mes.core.api.dto.UpdateNextOperationDto;
import com.itl.mes.core.client.service.CollectionRecordService;
import com.itl.mes.me.api.dto.UpdateBatchSnProcessDto;
import com.itl.mes.me.api.dto.UpdateSnRouteDto;
import com.itl.mes.me.api.entity.MeSnRouter;
import com.itl.mes.me.api.service.MeSnRouterService;
import com.itl.mes.me.provider.mapper.MeSnRouterMapper;
import com.itl.mom.label.api.dto.label.UpdateNextProcessDTO;
import com.itl.mom.label.api.dto.label.UpdateSnHoldDTO;
import com.itl.mom.label.api.entity.MeProductStatus;
import com.itl.mom.label.client.service.MeProductStatusService;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 条码-工艺路线表 服务实现类
 * </p>
 *
 * @author dengou
 * @since 2021-11-26
 */
@Service
public class MeSnRouterServiceImpl extends ServiceImpl<MeSnRouterMapper, MeSnRouter> implements MeSnRouterService {


    @Autowired
    private MeProductStatusService meProductStatusService;
    @Autowired
    private CollectionRecordService collectionRecordService;

    @Override
    public MeSnRouter getSnRouteBySnList(List<String> snList) {
        if(CollUtil.isEmpty(snList)) {
            return null;
        }
        List<MeSnRouter> meSnRouters = lambdaQuery().eq(MeSnRouter::getSite, UserUtils.getSite())
                .in(MeSnRouter::getSn, snList)
                .list();
        if(CollUtil.isEmpty(meSnRouters)) {
            return null;
        }
        //过滤出修改过工艺路线的条码
        List<String> hasUpdateSnList = meSnRouters.stream().filter(e -> StrUtil.isNotBlank(e.getVersion())).map(MeSnRouter::getSn).collect(Collectors.toList());
        //如果都没修改过，直接返回
        if(hasUpdateSnList.size() == 0) {
            return meSnRouters.get(0);
        }
        //如果都修改过，且都是同一个版本，直接返回
        if(hasUpdateSnList.size() == meSnRouters.size()) {
            //查询是不是同一个版本
            long versionCount = meSnRouters.stream().map(MeSnRouter::getVersion).distinct().count();
            if(versionCount == 1) {
                return meSnRouters.get(0);
            }
        }
        //部分修改， 抛出异常，返回修改过的部分的条码
        Assert.valid(CollUtil.isEmpty(hasUpdateSnList), "条码："+ CollUtil.join(hasUpdateSnList, StrUtil.COMMA) +"与工单工艺路线版本不一致，请单独处理；");
        return null;
    }

    @Override
    public Boolean saveSnRoute(UpdateSnRouteDto updateSnRouteDto) {
        //校验传参
        Assert.valid(updateSnRouteDto == null, "请求参数不能为空");
        List<String> snList = updateSnRouteDto.getSnList();
        String processInfo = updateSnRouteDto.getProcessInfo();
        String site = UserUtils.getSite();
        Assert.valid(CollUtil.isEmpty(snList), "条码列表不能为空");
        Assert.valid(StrUtil.isBlank(processInfo), "工艺路线信息不能为空");

        //查询产品状态列表
        List<String> snBoList = new ArrayList<>();
        for (String sn : snList) {
            snBoList.add(new SnHandleBO(site, sn).getBo());
        }
        ResponseData<List<MeProductStatus>> getBySnBoListResult = meProductStatusService.getBySnBoList(snBoList);
        Assert.valid(!getBySnBoListResult.isSuccess(), getBySnBoListResult.getMsg());
        List<MeProductStatus> meProductStatusList = getBySnBoListResult.getData();
        Assert.valid(CollUtil.isEmpty(meProductStatusList), "未找到"+ CollUtil.join(snList, StrUtil.COMMA) +"条码数据");
        //对比数量
        if(snList.size() != meProductStatusList.size()) {
            List<String> productStatusSnList = meProductStatusList.stream().map(e->new SnHandleBO(e.getSnBo()).getSn()).collect(Collectors.toList());
            Collection<String> subtract = CollUtil.subtract(snList, productStatusSnList);
            Assert.valid(CollUtil.isNotEmpty(subtract), "未找到条码数据:" + CollUtil.join(subtract, StrUtil.COMMA));
        }

        //校验条码是否挂起
        String notHoldSnListStr = meProductStatusList.stream().filter(e -> e.getHold() == 0).map(e -> new SnHandleBO(e.getSnBo()).getSn()).collect(Collectors.joining(StrUtil.COMMA));
        Assert.valid(StrUtil.isNotBlank(notHoldSnListStr), "请先挂起条码:" + notHoldSnListStr);

        //校验条码是否属于相同工单
        long shopOrderCount = meProductStatusList.stream().map(MeProductStatus::getShopOrder).distinct().count();
        Assert.valid(shopOrderCount > 1, "同工单的条码才可批量修改");

        //校验条码是否报废
        String scrappedSnStr = meProductStatusList.stream().filter(e -> StrUtil.equals(e.getSnState(), SnTypeEnum.SCRAPPED.getCode())).map(e -> new SnHandleBO(e.getSnBo()).getSn()).collect(Collectors.joining(StrUtil.COMMA));
        Assert.valid(StrUtil.isNotBlank(scrappedSnStr), "报废条码不可修改工艺路线:" + scrappedSnStr);

        //校验条码是否完工
        String isDoneSnStr = meProductStatusList.stream().filter(e -> CommonConstants.NUM_ONE.equals(e.getDone())).map(e -> new SnHandleBO(e.getSnBo()).getSn()).collect(Collectors.joining(StrUtil.COMMA));
        Assert.valid(StrUtil.isNotBlank(isDoneSnStr), "已完工条码不可修改工艺路线:" + scrappedSnStr);

        List<MeSnRouter> routers = lambdaQuery().eq(MeSnRouter::getSite, UserUtils.getSite())
                .in(MeSnRouter::getSn, snList)
                .list();
        //过滤出修改过工艺路线的条码
        List<String> updateRouteSnList = routers.stream().filter(e -> StrUtil.isNotBlank(e.getVersion())).map(MeSnRouter::getSn).collect(Collectors.toList());
        //有修改过工艺路线
        if(CollUtil.isNotEmpty(updateRouteSnList)) {
            long versionCount = routers.stream().map(MeSnRouter::getVersion).distinct().count();
            //部分修改， 抛出异常，返回修改过的部分的条码
            Assert.valid(versionCount > 1, "条码："+ CollUtil.join(updateRouteSnList, StrUtil.COMMA) +"与工单工艺路线版本不一致，请单独处理；");
        }

        //校验工艺路线是否合法
        List<String> nodeList = JsonPath.read(processInfo, "$.lineList[?(@.from nin $.lineList[*].to)].from");
        Assert.valid(nodeList.size() != 1, "无法确定工艺步骤中的`开始`!");

        List<String> nodeList2 = JsonPath.read(processInfo, String.format("$.lineList[?(@.from=~/%s/)].to", nodeList.get(0)));
        Assert.valid(nodeList2.size() != 1, "`开始`之后有多条分支!");
        // 获取第一个执行的节点
        String nodeId = nodeList2.get(0);

        //修改条码工艺路线
        String versionStr = IdUtil.fastSimpleUUID();
        Date now = new Date();
        String userName = UserUtils.getUserName();
        List<MeSnRouter> meSnRouters = new ArrayList<>();
        for (String sn : snList) {
            MeSnRouter meSnRouter = new MeSnRouter();
            meSnRouter.setSn(sn);
            meSnRouter.setJson(processInfo);
            meSnRouter.setUpdateTime(now);
            meSnRouter.setUpdateUser(userName);
            meSnRouter.setVersion(versionStr);
            meSnRouter.setCurrent(nodeId);
            meSnRouters.add(meSnRouter);
        }
        updateBatchById(meSnRouters);

        List<Map<String, String>> operations = JsonPath.read(processInfo, String.format("$.nodeList[?(@.id == '%s')]", nodeId));
        Map<String, String> operationMap = operations.get(0);
        String operationBo = operationMap.get("operation");
        String operationName = operationMap.get("name");

        //更新产品状态管理  下工序
        updateProductStatusNextProcess(snBoList, operationBo, operationName);

        //更新采集记录下工序
        updateCollectionRecordNextProcess(snBoList, operationBo, operationName);

        return true;
    }


    @Override
    public List<Map<String, Object>> getNexProcessListBySn(String sn) {
        Assert.valid(StrUtil.isBlank(sn), "条码不能为空");
        MeSnRouter meSnRouter = getById(sn);
        Assert.valid(meSnRouter == null, "未找到条码工艺路线信息");

        String processInfo = meSnRouter.getJson();
        Assert.valid(StrUtil.isBlank(processInfo), "条码工艺路线信息为空");

        //将me_sn_route.json转成processInfo  JSON obj
        JSONObject processInfoObj = JSONUtil.parseObj(processInfo);
        //获取工序列表
        JSONArray nodeList = processInfoObj.getJSONArray("nodeList");
        if(CollUtil.isEmpty(nodeList)) {
            return Collections.emptyList();
        }

        //返回结果对象
        List<Map<String, Object>> result = new ArrayList<>();
        for (JSONObject jsonObject : nodeList.jsonIter()) {
            String type = jsonObject.getStr("type");
            //过滤掉开始节点和结束节点
            if(StrUtil.equalsAny(type, "timer", "end")) {
                continue;
            }
            Map<String, Object> resultItem = MapUtil.builder(new HashMap<String, Object>())
                    .put("id", jsonObject.get("id"))
                    .put("name", jsonObject.get("name"))
                    .put("operation", jsonObject.get("operation"))
                    .build();
            result.add(resultItem);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateNexProcessBySn(String sn, String processId) {
        //获取可选工序节点
        List<Map<String, Object>> nexProcessListBySn = getNexProcessListBySn(sn);
        //是否包含
        Optional<Map<String, Object>> optional = nexProcessListBySn.stream().filter(e -> StrUtil.equals((String) e.get("id"), processId)).findFirst();
        Assert.valid(optional.isPresent(), "未找到所选工序,条码:" + sn);
        Map<String, Object> map = optional.get();
        //工序bo
        String operationBo = (String)map.get("operation");
        String operationName = (String)map.get("name");
        //执行修改me_sn_router
        MeSnRouter meSnRouter = new MeSnRouter();
        meSnRouter.setSn(sn);
        meSnRouter.setCurrent(processId);
        updateById(meSnRouter);
        //: 修改采集记录,产品状态管理的下工序
        //产品状态更新
        ProductStateUpdateDto productStateUpdateDto = new ProductStateUpdateDto();
        productStateUpdateDto.setSnBo(new SnHandleBO(UserUtils.getSite(), sn).getBo());
        productStateUpdateDto.setNextOperationBo(operationBo);
        productStateUpdateDto.setNextOperationName(operationName);
        ResponseData<Boolean> updateNextProcessResult = meProductStatusService.updateNextProcess(productStateUpdateDto);
        Assert.valid(!updateNextProcessResult.isSuccess(), updateNextProcessResult.getMsg());
        //更新采集记录
        UpdateNextOperationDto updateNextOperationDto = new UpdateNextOperationDto();
        updateNextOperationDto.setOperationBo(operationBo);
        updateNextOperationDto.setOperationName(operationName);
        updateNextOperationDto.setSnBoList(Arrays.asList(new SnHandleBO(UserUtils.getSite(), sn).getBo()));
        ResponseData<Boolean> updateNextOperationResult = collectionRecordService.updateNextOperation(updateNextOperationDto);
        Assert.valid(!updateNextOperationResult.isSuccess(), updateNextOperationResult.getMsg());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateNexProcessBySnBatch(UpdateBatchSnProcessDto updateBatchSnProcessDto) {
        //查询产品状态列表
        List<String> snList = updateBatchSnProcessDto.getSnList();
        Assert.valid(CollUtil.isEmpty(snList), "条码列表不能为空");
        String operationBo = updateBatchSnProcessDto.getOperationBo();
        Assert.valid(StrUtil.isBlank(operationBo), "工序不能为空");
        String site = UserUtils.getSite();
        List<String> snBoList = new ArrayList<>();
        for (String sn : snList) {
            snBoList.add(new SnHandleBO(site, sn).getBo());
        }
        ResponseData<List<MeProductStatus>> getBySnBoListResult = meProductStatusService.getBySnBoList(snBoList);
        Assert.valid(!getBySnBoListResult.isSuccess(), getBySnBoListResult.getMsg());
        List<MeProductStatus> meProductStatusList = getBySnBoListResult.getData();
        //对比数量
        if(snList.size() != meProductStatusList.size()) {
            List<String> productStatusSnList = meProductStatusList.stream().map(e->new SnHandleBO(e.getSnBo()).getSn()).collect(Collectors.toList());
            Collection<String> subtract = CollUtil.subtract(snList, productStatusSnList);
            Assert.valid(CollUtil.isNotEmpty(subtract), "未找到条码数据:" + CollUtil.join(subtract, StrUtil.COMMA));
        }

        //校验条码是否挂起
        String notHoldSnListStr = meProductStatusList.stream().filter(e -> e.getHold() == 0).map(e -> new SnHandleBO(e.getSnBo()).getSn()).collect(Collectors.joining(StrUtil.COMMA));
        Assert.valid(StrUtil.isNotBlank(notHoldSnListStr), "请先挂起条码:" + notHoldSnListStr);

        //校验条码是否报废
        String scrappedSnStr = meProductStatusList.stream().filter(e -> StrUtil.equals(e.getSnState(), SnTypeEnum.SCRAPPED.getCode())).map(e -> new SnHandleBO(e.getSnBo()).getSn()).collect(Collectors.joining(StrUtil.COMMA));
        Assert.valid(StrUtil.isNotBlank(scrappedSnStr), "报废条码不可修改工序:" + scrappedSnStr);

        //校验条码是否完工
        String isDoneSnStr = meProductStatusList.stream().filter(e -> CommonConstants.NUM_ONE.equals(e.getDone())).map(e -> new SnHandleBO(e.getSnBo()).getSn()).collect(Collectors.joining(StrUtil.COMMA));
        Assert.valid(StrUtil.isNotBlank(isDoneSnStr), "已完工条码不可修改工序:" + scrappedSnStr);

        //根据条码查询工艺路线
        List<MeSnRouter> meSnRouters = lambdaQuery().in(MeSnRouter::getSn, snList).eq(MeSnRouter::getSite, UserUtils.getSite()).list();
        Assert.valid(CollUtil.isEmpty(meSnRouters), "未找到条码工艺路线");
        //工艺路线是否包含指定工序
        List<String> containsOperationBoRouters = meSnRouters.stream().filter(e -> StrUtil.contains(e.getJson(), operationBo)).map(MeSnRouter::getSn).collect(Collectors.toList());
        if(containsOperationBoRouters.size() != meSnRouters.size()) {
            Collection<String> subtract = CollUtil.subtract(snList, containsOperationBoRouters);
            Assert.valid(true, "条码"+ CollUtil.join(subtract, StrUtil.COMMA) + "工艺路线中不包含指定工序");
        }
        //更新 下工序
        String operationName = null;
        List<MeSnRouter> meSnRouterUpdateList = new ArrayList<>();
        for (MeSnRouter e : meSnRouters) {
            String processInfo = e.getJson();
            List<String> operationIds = JsonPath.read(processInfo, String.format("$.nodeList[?(@.operation == '%s')].id", operationBo));
            Assert.valid(CollUtil.isEmpty(operationIds), "未找到"+ operationBo +"对应的工序id");
            String operationId = operationIds.get(0);

            List<String> operationNames = JsonPath.read(processInfo, String.format("$.nodeList[?(@.id == '%s')].name", operationId));
            if(StrUtil.isBlank(operationName)) {
                operationName = operationNames.get(0);
            }

            MeSnRouter meSnRouter = new MeSnRouter();
            meSnRouter.setSn(e.getSn());
            meSnRouter.setCurrent(operationId);
            meSnRouterUpdateList.add(meSnRouter);
        }
        updateBatchById(meSnRouterUpdateList);
        //更新产品状态管理  下工序
        updateProductStatusNextProcess(snBoList, operationBo, operationName);

        //更新采集记录下工序
        updateCollectionRecordNextProcess(snBoList, operationBo, operationName);

        //取消挂起
        UpdateSnHoldDTO updateSnHoldDTO = new UpdateSnHoldDTO();
        updateSnHoldDTO.setHold(CommonConstants.NUM_ZERO);
        updateSnHoldDTO.setSnBoList(snBoList);
        meProductStatusService.updateIsHold(updateSnHoldDTO);

        return true;
    }

    /**
     * 更新产品状态管理  下工序
     * */
    private void updateProductStatusNextProcess(List<String> snBoList, String operationBo, String operationName) {
        UpdateNextProcessDTO updateNextProcessDTO = new UpdateNextProcessDTO();
        updateNextProcessDTO.setSnBoList(snBoList);
        updateNextProcessDTO.setNextProcessId(operationBo);
        updateNextProcessDTO.setNextProcessName(operationName);
        ResponseData<Boolean> responseData = meProductStatusService.updateNextProcessBatch(updateNextProcessDTO);
        Assert.valid(!responseData.isSuccess(), responseData.getMsg());
    }

    /**
     * 更新采集记录下工序
     * */
    private void updateCollectionRecordNextProcess(List<String> snBoList, String operationBo, String operationName) {
        UpdateNextOperationDto updateNextOperationDto = new UpdateNextOperationDto();
        updateNextOperationDto.setOperationBo(operationBo);
        updateNextOperationDto.setOperationName(operationName);
        updateNextOperationDto.setSnBoList(snBoList);
        ResponseData<Boolean> updateNextOperationResult = collectionRecordService.updateNextOperation(updateNextOperationDto);
        Assert.valid(!updateNextOperationResult.isSuccess(), updateNextOperationResult.getMsg());
    }

    @Override
    public Boolean addSnRoute(UpdateSnRouteDto updateSnRouteDto) {
        String processInfo = updateSnRouteDto.getProcessInfo();
        List<String> snList = updateSnRouteDto.getSnList();
        Assert.valid(StrUtil.isEmpty(processInfo), "工艺路线信息不能为空");
        Assert.valid(CollUtil.isEmpty(snList), "条码列表不能为空");

        List<String> nodeList = JsonPath.read(processInfo, "$.lineList[?(@.from nin $.lineList[*].to)].from");
        Assert.valid(nodeList.size() != 1, "无法确定工艺步骤中的`开始`!");

        List<String> nodeList2 = JsonPath.read(processInfo, String.format("$.lineList[?(@.from=~/%s/)].to", nodeList.get(0)));
        Assert.valid(nodeList2.size() != 1, "`开始`之后有多条分支!");
        // 获取第一个执行的节点
        String nodeId = nodeList2.get(0);

        List<MeSnRouter> meSnRouters = new ArrayList<>();
        for (String sn : snList) {
            MeSnRouter meSnRouter = new MeSnRouter();
            meSnRouter.setSn(sn);
            meSnRouter.setSite(updateSnRouteDto.getSite());
            meSnRouter.setJson(processInfo);
            meSnRouter.setCurrent(nodeId);
            meSnRouters.add(meSnRouter);
        }
        return saveOrUpdateBatch(meSnRouters);
    }

    @Override
    public MeSnRouter getBySn(String sn, String site) {
        if(StrUtil.isBlank(sn) || StrUtil.isBlank(site)) {
            return null;
        }
        return lambdaQuery().eq(MeSnRouter::getSn, sn).eq(MeSnRouter::getSite, site).one();
    }
}
