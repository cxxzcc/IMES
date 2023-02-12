package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.serviceImpl.ImomServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.RouterHandleBO;
import com.itl.mes.core.api.constant.BOPrefixEnum;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.constant.RouterProcessEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.dto.RouterDataDTO;
import com.itl.mes.core.api.dto.RouterProcessDataDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.service.RouterProcessService;
import com.itl.mes.core.api.service.RouterService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataValVo;
import com.itl.mes.core.api.vo.RouterAsSaveVo;
import com.itl.mes.core.api.vo.RouterVo;
import com.itl.mes.core.provider.mapper.ItemGroupMapper;
import com.itl.mes.core.provider.mapper.OperationMapper;
import com.itl.mes.core.provider.mapper.RouterMapper;
import com.itl.mes.core.provider.mapper.RouterProcessMapper;
import com.itl.mes.core.provider.utils.CommonCode;
import com.itl.mes.core.provider.utils.ExcelReader;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 工艺路线
 * </p>
 *
 * @author linjl
 * @since 2021-01-28
 */
@Service
@Transactional
public class RouterServiceImpl extends ImomServiceImpl<RouterMapper, Router> implements RouterService {

    @Autowired
    RouterProcessService routerProcessService;

    @Autowired
    ItemService itemService;

    @Autowired
    CustomDataValService customDataValService;

    @Resource
    RouterMapper routerMapper;

    @Resource
    RouterProcessMapper routerProcessMapper;

    @Resource
    OperationMapper operationMapper;

    @Resource
    ItemGroupMapper itemGroupMapper;

    /**
     * 获取工艺路线信息
     */
    @Override
    public Router getRouter(String bo) throws CommonException {
        Router router = getById(bo);
        if (null == router) {
            return null;
        }

        Item item = itemService.getById(router.getItemBo());
        router.setItem(item);
        RouterProcess routerProcess = routerProcessService.getById(bo);
        router.setRouterProcess(routerProcess);

        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(router.getSite(),
                router.getBo(), CustomDataTypeEnum.ROUTER.getDataType());
        router.setCustomDataAndValVoList(customDataAndValVos);
        return router;
    }

    /**
     * 获取工艺路线信息
     */
    @Override
    public Router getRouterCode(String routerCode) throws CommonException {
        QueryWrapper<Router> routerQueryWrapper = new QueryWrapper<>();
        routerQueryWrapper.eq("router", routerCode);
        // Router router = getOne(routerQueryWrapper);
        Router router = routerMapper.selectOne(routerQueryWrapper);
        if (null == router) {
            return null;
        }

        Item item = itemService.getById(router.getItemBo());
        router.setItem(item);
        RouterProcess routerProcess = routerProcessService.getById(router.getBo());
        router.setRouterProcess(routerProcess);

        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(router.getSite(),
                router.getBo(), CustomDataTypeEnum.ROUTER.getDataType());
        router.setCustomDataAndValVoList(customDataAndValVos);
        return router;
    }

    /**
     * 保存工艺路线
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public boolean saveRouter(RouterVo routerVo) throws Exception {
        try {
            Router router = new Router(routerVo.getSite(), routerVo.getRouter(), routerVo.getVersion());

            router.setSite(routerVo.getSite())
                    .setRouter(routerVo.getRouter())
                    .setRouterType(routerVo.getRouterType())
                    .setRouterName(routerVo.getRouterName())
                    .setRouterDesc(routerVo.getRouterDesc())
                    .setState(routerVo.getState())
                    .setVersion(routerVo.getVersion())
                    .setItemBo(routerVo.getItemBo());

            if (null != routerVo.getProcessInfo()) {
                RouterProcess routerProcess = new RouterProcess(router.getBo());
                routerProcess.setProcessInfo(routerVo.getProcessInfo());
                routerProcess.setSite(routerVo.getSite());
                router.setRouterProcess(routerProcess);
            }
            router.setCustomDataValVoList(routerVo.getCustomDataValVoList());

            boolean isUpdate = false; //是否更新操作
            // 校验组合唯一：router + site + version
            /*LambdaQueryWrapper<Router> query = new QueryWrapper<Router>().lambda()
                    .and(i -> i.eq(Router::getRouter, router.getRouter())
                            .eq(Router::getSite, router.getSite())
                            .eq(Router::getVersion, router.getVersion()));*/
            // 校验组合唯一：router + site
            LambdaQueryWrapper<Router> query = new QueryWrapper<Router>().lambda()
                    .and(i -> i.eq(Router::getRouter, router.getRouter())
                            .eq(Router::getSite, router.getSite()));
            List<Router> queryRouter = new ArrayList<>();
            if (StrUtil.isBlank(routerVo.getBo())) {
                queryRouter = routerMapper.selectList(query);
                if (queryRouter != null && queryRouter.size() > 0) {
                    throw new Exception(String.format("工厂中已存在该工艺路线编号，请用新的编号", router.getSite(), router.getRouter()));
                }
                router.setCreateDate(DateTime.now());
                router.setCreateUser(UserUtils.getUser());
                routerMapper.insert(router);
            } else {
                query.ne(Router::getBo, routerVo.getBo());
                queryRouter = routerMapper.selectList(query);
                if (queryRouter != null && queryRouter.size() > 0) {
                    throw new Exception(String.format("工厂中已存在该工艺路线编号，请用新的编号！", router.getSite(), router.getRouter()));
                }
                router.setModifyDate(DateTime.now());
                router.setModifyUser(UserUtils.getUser());
                routerMapper.updateById(router);
                isUpdate = true;
            }

            RouterProcess routerProcess = router.getRouterProcess();
            if (null != routerProcess) {
                routerProcess.setSite(router.getSite());
                routerProcess.setRouterBo(router.getBo());
                if (isUpdate) {
                    routerProcessService.updateById(routerProcess);
                } else {
                    routerProcessService.save(routerProcess);
                }
            }

            //保存自定义数据
            List<CustomDataValVo> customDataValVoList = router.getCustomDataValVoList();
            if (CollUtil.isNotEmpty(customDataValVoList)) {
                CustomDataValRequest customDataValRequest = new CustomDataValRequest();
                customDataValRequest.setBo(router.getBo());
                customDataValRequest.setSite(router.getSite());
                customDataValRequest.setCustomDataType(CustomDataTypeEnum.ROUTER.getDataType());
                customDataValRequest.setCustomDataValVoList(customDataValVoList);
                customDataValService.saveCustomDataVal(customDataValRequest);
            }
        } catch (SQLServerException e) {
            e.printStackTrace();
            throw new RuntimeException("同一工厂工艺路线编号不可重复！");
        }

        return true;
    }

    /**
     * 复制保存新工艺路线
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Router saveAsRouter(RouterAsSaveVo routerAsSaveVo) throws Exception {
        Router router = null;
        try {
            // 工线路线BO（必填）
            String routerBo = routerAsSaveVo.getBo();
            if (routerBo == null || "".equals(routerBo)) {
                throw new Exception(String.format("工艺路线BO不能为空！", UserUtils.getSite(), routerAsSaveVo.getBo()));
            }

            router = getRouter(routerBo);
            if (router == null) {
                throw new Exception(String.format("根据BO未找到源工艺路线信息！", UserUtils.getSite(), routerAsSaveVo.getBo()));
            }

            // 新工艺路线编号（必填，要求唯一）
            String routerCode = routerAsSaveVo.getRouter();
            if (routerCode == null || "".equals(routerCode)) {
                throw new Exception(String.format("新增的工艺路线编号不能为空！", UserUtils.getSite(), routerAsSaveVo.getRouter()));
            }

            /* 判断编号是否已存在（方式1）
             *         LambdaQueryWrapper<Router> query = new QueryWrapper<Router>().lambda()
             *                 .and(i -> i.eq(Router::getRouter, routerVo.getRouter())
             *                         .eq(Router::getSite, routerVo.getSite())
             *                         .eq(Router::getVersion, routerVo.getVersion()));
             *         List<Router> queryRouter = routerMapper.selectList(query);
             *         if (queryRouter != null && queryRouter.size() > 0) {
             *             throw new Exception(String.format("工厂%s中已存在工艺路线%s", router.getSite(), router.getRouter()));
             *         }
             */

            // 判断编号是否已存在（方式2）
            String routerVersion = routerAsSaveVo.getVersion();
            if (routerVersion == null || "".equals(routerVersion)) {
                routerVersion = router.getVersion();
            }
            RouterHandleBO routerHandleBO = new RouterHandleBO(UserUtils.getSite(), routerCode, routerVersion);
            /*Router routerUK = getById(routerHandleBO.getBo());
            if (routerUK != null) {
                if (null != routerUK.getRouter() && !"".equals(routerUK.getRouter())) {
                    throw new Exception(String.format("工厂中已存在该工艺路线编号，请用新的编号", UserUtils.getSite(), routerAsSaveVo.getRouter()));
                }
            }*/

            // 校验组合唯一：router + site
            LambdaQueryWrapper<Router> query = new QueryWrapper<Router>().lambda()
                    .and(i -> i.eq(Router::getRouter, routerCode)
                            .eq(Router::getSite, UserUtils.getSite()));
            List<Router> queryRouter = routerMapper.selectList(query);
            if (queryRouter != null && queryRouter.size() > 0) {
                throw new Exception(String.format("工厂中已存在该工艺路线编号，请用新的编号", router.getSite(), router.getRouter()));
            }

            // 新工艺路线名称（必填）
            String routerName = routerAsSaveVo.getRouterName();
            if (routerName == null || "".equals(routerName)) {
                throw new Exception(String.format("新增的工艺路线名称不能为空！", UserUtils.getSite(), routerAsSaveVo.getRouterName()));
            }

            String routerDesc = routerAsSaveVo.getRouterDesc();
            if (routerDesc == null || "".equals(routerDesc)) {
                routerDesc = router.getRouterDesc();
            }

            String site = UserUtils.getSite();
            if (site == null || "".equals(site)) {
                site = router.getSite();
            }

            // 新工艺路线赋值，并保存
            router.setBo(routerHandleBO.getBo()); // 新工艺路线BO
            router.setRouter(routerCode); // 新工艺路线编号
            router.setVersion(routerVersion); // 新工艺路线版本号
            router.setRouterName(routerName); // 新工艺路线名称
            router.setRouterDesc(routerDesc); // 新工艺路线描述
            router.setSite(site); // 新工艺路线工厂
            router.setCreateDate(DateTime.now()); // 创建时间
            router.setCreateUser(UserUtils.getUser()); // 创建用户
            routerMapper.insert(router);

            // 保存工艺路线路线图
            RouterProcess routerProcess = router.getRouterProcess();
            if (null != routerProcess) {
                routerProcess.setSite(router.getSite());
                routerProcess.setRouterBo(router.getBo());
                routerProcessService.save(routerProcess);
            }

            // 保存自定义数据
            List<CustomDataValVo> customDataValVoList = router.getCustomDataValVoList();
            if (CollUtil.isNotEmpty(customDataValVoList)) {
                CustomDataValRequest customDataValRequest = new CustomDataValRequest();
                customDataValRequest.setBo(router.getBo());
                customDataValRequest.setSite(router.getSite());
                customDataValRequest.setCustomDataType(CustomDataTypeEnum.ROUTER.getDataType());
                customDataValRequest.setCustomDataValVoList(customDataValVoList);
                customDataValService.saveCustomDataVal(customDataValRequest);
            }

        } catch (SQLServerException e) {
            e.printStackTrace();
            throw new RuntimeException("同一工厂编码不可重复");
        }

        return router;
    }

    /**
     * 删除工艺路线
     *
     * @param router 工艺路线
     * @throws CommonException 异常
     */
    @Override
    public void deleteRouter(Router router) throws CommonException {
        LambdaQueryWrapper<Router> query = new QueryWrapper<Router>().lambda()
                .eq(Router::getBo, router.getBo());

        //删除工艺路线
        Integer integer = routerMapper.delete(query);

        LambdaQueryWrapper<RouterProcess> queryWrapper = new QueryWrapper<RouterProcess>().lambda()
                .eq(RouterProcess::getRouterBo, router.getBo())
                .eq(RouterProcess::getSite, UserUtils.getSite());
        //删除工艺路线路线图
        routerProcessService.remove(queryWrapper);

        //删除自定义数据
        customDataValService.deleteCustomDataValByBoAndType(UserUtils.getSite(), router.getBo(), CustomDataTypeEnum.ROUTER);

    }


    @Override
    @Transactional
    public void importFile(List<RouterDataDTO> cachedDataList, List<RouterProcessDataDTO> cachedDataList1) {
        String site = UserUtils.getSite();
        List<Router> routerList = new ArrayList<>();
        List<RouterProcess> routerProcessList = new ArrayList<>();
        //分组
        Map<String, List<RouterProcessDataDTO>> routerProcessMap = cachedDataList1.stream().collect(Collectors.groupingBy(RouterProcessDataDTO::getRouter));
        //工序
        List<String> opreaBos = cachedDataList1.stream()
                .filter(v -> !v.getOperation().equals(RouterProcessEnum.START.getCode())
                        && !v.getOperation().equals(RouterProcessEnum.END.getCode()))
                .map(v -> BOPrefixEnum.OP.getPrefix() + ":" + site + "," + v.getOperation())
                .collect(Collectors.toList());
        List<Operation> operations = operationMapper.selectBatchIds(opreaBos);
        Map<String, Operation> operationMap = operations.stream().collect(Collectors.toMap(Operation::getBo, Function.identity()));
        //物料组
        List<ItemGroup> itemGroups = itemGroupMapper.selectList(Wrappers.emptyWrapper());
        Map<String, ItemGroup> itemGroupMap = itemGroups.stream().collect(Collectors.toMap(ItemGroup::getBo, Function.identity()));
        for (RouterDataDTO routerDataDTO : cachedDataList) {
            String routerNo = routerDataDTO.getRouter();
            Router router = new Router(site, routerNo, routerDataDTO.getVersion());
            BeanUtil.copyProperties(routerDataDTO, router);
            routerList.add(router);
            if (routerProcessMap.containsKey(routerNo)) {
                List<RouterProcessDataDTO> routerProcessDataDTOS = routerProcessMap.get(routerNo);
                RouterProcess routerProcess = new RouterProcess();
                routerProcess.setRouterBo(router.getBo());
                routerProcess.setSite(site);
                Map process = new HashMap();
                List<Map> nodeList = new ArrayList<>();
                List<String> nodeIds = new ArrayList<>();
                List<Map> lineList = new ArrayList<>();
                for (int i = 0; i < routerProcessDataDTOS.size(); i++) {
                    RouterProcessDataDTO routerProcessDataDTO = routerProcessDataDTOS.get(i);
                    Map node = new HashMap();
                    Map line = new HashMap();
                    String id = routerProcessDataDTO.getId();
                    String nextId = routerProcessDataDTO.getNextId();
                    String operation = routerProcessDataDTO.getOperation();
                    if (!RouterProcessEnum.END.getCode().equals(operation)) {
                        line.put("from", id);
                        line.put("to", nextId);
                        line.put("label", routerProcessDataDTO.getParam());
                        lineList.add(line);
                    }
                    node.put("id", id);
                    node.put("state", "success");
                    node.put("left", "190px");
                    if (RouterProcessEnum.START.getCode().equals(operation)) {//开始节点
                        node.put("name", "开始流程");
                        node.put("type", "timer");
                        node.put("ico", "craftRouteStart");
                        node.put("top", "20px");
                    } else if (RouterProcessEnum.END.getCode().equals(operation)) {//结束节点
                        node.put("name", "流程结束");
                        node.put("type", "end");
                        node.put("ico", "craftRouteEnd");
                        node.put("top", (i + 1) * 100 + "px");
                    } else {
                        String opBo = BOPrefixEnum.OP.getPrefix() + ":" + site + "," + routerProcessDataDTO.getOperation();
                        if (operationMap.containsKey(opBo)) {
                            node.put("name", operationMap.get(opBo).getOperationName());
                        }
                        node.put("operation", opBo);
                        node.put("type", opBo);
                        node.put("ico", "craftRouteList");
                        node.put("top", (i + 1) * 100 + "px");
                        node.put("isRepeat", routerProcessDataDTO.getIsRepeat());
                        node.put("isSkip", routerProcessDataDTO.getIsSkip());
                        node.put("isCreateSKU", routerProcessDataDTO.getIsCreateSKU());
                        node.put("repeatCount", routerProcessDataDTO.getRepeatCount());
                        String bom = routerProcessDataDTO.getBom();
                        if (StrUtil.isNotBlank(bom)) {
                            List<Map> bomList = new ArrayList<>();
                            String[] split = bom.split("\\|");
                            for (String s : split) {
                                String bo = BOPrefixEnum.IG.getPrefix() + ":" + site + "," + s;
                                if (itemGroupMap.containsKey(bo)) {
                                    ItemGroup itemGroup = itemGroupMap.get(bo);
                                    Map map = new HashMap();
                                    map.put("BO", bo);
                                    map.put("ITEM_GROUP", s);
                                    map.put("GROUP_NAME", itemGroup.getGroupName());
                                    bomList.add(map);
                                }
                            }
                            node.put("BOM", bomList);
                        }
                    }
                    if (!nodeIds.contains(id)) {
                        nodeIds.add(id);
                        nodeList.add(node);
                    }
                }
                process.put("nodeList", nodeList);
                process.put("lineList", lineList);
                routerProcess.setProcessInfo(JSONUtil.toJsonStr(process));
                routerProcessList.add(routerProcess);
            }
        }
        routerMapper.insertOrUpdateBatch(routerList);
        routerProcessMapper.insertBatch(routerProcessList);
    }

    @Transactional
    public void importFile(MultipartFile file) throws CommonException {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, CommonCode> commonCodeMap = new HashMap<String, CommonCode>();
        // List<Map<String, CommonCode>> commonCodeList = new ArrayList<>();
        List<Map<String, Object>> readResult = ExcelReader.readExcel2Map(file.getOriginalFilename(), inputStream);
        /*readResult.forEach(stringObjectMap -> {
            String faultCode = stringObjectMap.get("faultCode") == null?"":stringObjectMap.get("faultCode").toString();
            if(StringUtils.isNotBlank(faultCode)){
                Fault result = faultMapper.selectOne(new QueryWrapper<Fault>().eq("faultCode", faultCode));

                if(result != null){
                    commonCodeMap.put(faultCode, CommonCode.CODE_REPEAT);
                    // throw new CustomException(CommonCode.CODE_REPEAT);
                }
                Fault fault = new Fault();
                fault.setFaultCode(stringObjectMap.get("faultCode")==null?"":stringObjectMap.get("faultCode").toString());
                fault.setFaultName(stringObjectMap.get("faultName")==null?"":stringObjectMap.get("faultName").toString());
                fault.setRemark(stringObjectMap.get("remark")==null?"":stringObjectMap.get("remark").toString());
                fault.setType(stringObjectMap.get("type")==null?"":stringObjectMap.get("type").toString());
                fault.setRepairMethod(stringObjectMap.get("repairMethod")==null?"":stringObjectMap.get("repairMethod").toString());
                if(stringObjectMap.get("state")!=null){
                    if(!isInteger(stringObjectMap.get("state").toString())){
                        commonCodeMap.put(faultCode, CommonCode.IS_NOT_NUM);
                        // throw new CustomException(CommonCode.IS_NOT_NUM);
                    }
                    fault.setState(Integer.valueOf(stringObjectMap.get("state").toString()));
                }
                fault.setSiteId(UserUtils.getSite());
                faultMapper.insert(fault);
            }
        });
        if(!commonCodeMap.isEmpty()){
            ResultCode resultCode = new ResultCode() {
                @Override
                public boolean success() {
                    return false;
                }

                @Override
                public int code() {
                    return 999;
                }

                @Override
                public String message() {
                    return commonCodeMap.toString();
                }
            };
            throw new CustomException(resultCode);
        }*/
    }

}
