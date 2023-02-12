package com.itl.mom.label.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.StringUtils;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.constant.SnTypeEnum;
import com.itl.mes.core.client.service.ShopOrderService;
import com.itl.mom.label.api.bo.SnHandleBO;
import com.itl.mom.label.api.dto.label.LabelTransferRequestDTO;
import com.itl.mom.label.api.dto.label.SnChangeDto;
import com.itl.mom.label.api.entity.label.LabelPrint;
import com.itl.mom.label.api.entity.label.LabelPrintLog;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.api.entity.label.SnTransRecord;
import com.itl.mom.label.api.enums.LabelPrintLogStateEnum;
import com.itl.mom.label.api.service.label.LabelPrintLogService;
import com.itl.mom.label.api.service.label.LabelPrintService;
import com.itl.mom.label.api.service.label.SnService;
import com.itl.mom.label.api.service.label.SnTransRecordService;
import com.itl.mom.label.api.vo.LabelTransVo;
import com.itl.mom.label.provider.mapper.label.LabelPrintMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author space
 * @since 2019-07-25
 */
@RestController
@RequestMapping("/monopy/sns")
@Api(tags = " 条码信息表")
public class SnController {
    @Autowired
    public SnService snService;
    @Autowired
    private SnTransRecordService snTransRecordService;
    @Autowired
    private LabelPrintLogService labelPrintLogService;
    @Autowired
    private LabelPrintService labelPrintService;
    @Autowired
    private LabelPrintMapper labelPrintMapper;
    @Autowired
    private ShopOrderService shopOrderService;


    /**
     * NEW新建；标签条码刚生成
     * LOADED装载；标签已装箱或已有实物
     * EMPTY空置；标签未装箱或无实物
     * END结束；标签已发出或失效
     * WIP在制；标签生产中
     * RESERVED保留；标签预留，占不可用
     * SCRAPPED报废，标签不可以已报废
     * UP  上架
     * DOWN  下架
     *
     * @param sn
     * @param type
     * @return
     */
    @PostMapping("/check/{sn}/{type}")
    @ApiOperation(value = "校验条码type-当前操作允许的状态")
    public ResponseData check(@PathVariable("sn") String sn, @PathVariable("type") String type) {
        final Sn byId = snService.getById(new SnHandleBO(UserUtils.getSite(), sn).getBo());
        if(byId == null) {
            return ResponseData.error("标签不存在");
        }
        if(!StrUtil.contains(type, byId.getState())) {
            return ResponseData.error("该标签不可用");
        }

        return ResponseData.success(true);
    }

    @PostMapping("/update/{type}")
    @ApiOperation(value = "变更条码状态")
    public ResponseData save(@RequestBody List<SnChangeDto> sns, @PathVariable("type") String type) {
        final Sn sn1 = new Sn();
        sn1.setState(type);
        snService.update(sn1, new QueryWrapper<Sn>().lambda().in(Sn::getSn, sns.stream().map(SnChangeDto::getSn).collect(Collectors.toList())));
        sns.forEach(x -> {
            final Sn sn = snService.getById(new SnHandleBO(UserUtils.getSite(), x.getSn()).getBo());

            if (!"SCRAPPED".equals(sn.getState())){
                final List<LabelPrint> list = labelPrintService.list(new QueryWrapper<LabelPrint>().lambda()
                        .eq(LabelPrint::getElementBo, new ShopOrderHandleBO(UserUtils.getSite(), x.getShopOrder()).getBo()));
                for (LabelPrint one : list) {
                    one.setBarCodeAmount(one.getBarCodeAmount() + 1);
                    labelPrintService.updateById(one);
                }
            }

            if(StrUtil.equals(type, SnTypeEnum.SCRAPPED.getCode())) {

                //更新工单 条码数量  -1
                shopOrderService.updateShopOrderLabelQtyByBO(new ShopOrderHandleBO(UserUtils.getSite(), x.getShopOrder()).getBo(), new BigDecimal("-1"));

                LabelPrint labelPrint = labelPrintMapper.selectById(sn.getLabelPrintBo());
                LabelPrintLog labelPrintLog = new LabelPrintLog();
                labelPrintLog.setLabelPrintBo(labelPrint.getBo());
                labelPrintLog.setLabelPrintDetailBo(sn.getBo());
                labelPrintLog.setPrintDate(new Date());
                labelPrintLog.setPrintUser(UserUtils.getCurrentUser().getUserName());
                labelPrintLog.setState(LabelPrintLogStateEnum.SCRAPPED.getCode());
                labelPrintLogService.save(labelPrintLog);
            }
        });

        return ResponseData.success();
    }

    @PostMapping("/updateBySns/{type}")
    @ApiOperation(value = "变更条码状态")
    public ResponseData saveBySns(@RequestBody List<String> sns, @PathVariable("type") String type) {
        final Sn sn1 = new Sn();
        sn1.setState(type);
        snService.update(sn1, new QueryWrapper<Sn>().lambda().in(Sn::getSn, sns));
        sns.forEach(x -> {
            final Sn sn = snService.getById(new SnHandleBO(UserUtils.getSite(), x).getBo());

            String shopOrderBySn = labelPrintService.queryShopOrderBySn(x);
            if (!"SCRAPPED".equals(sn.getState())){
                final List<LabelPrint> list = labelPrintService.list(new QueryWrapper<LabelPrint>().lambda()
                        .eq(LabelPrint::getElementBo, new ShopOrderHandleBO(UserUtils.getSite(), shopOrderBySn).getBo()));
                for (LabelPrint one : list) {
                    one.setBarCodeAmount(one.getBarCodeAmount() + 1);
                    labelPrintService.updateById(one);
                }
            }

            if(StrUtil.equals(type, SnTypeEnum.SCRAPPED.getCode())) {

                //更新工单 条码数量  -1
                shopOrderService.updateShopOrderLabelQtyByBO(new ShopOrderHandleBO(UserUtils.getSite(), shopOrderBySn).getBo(), new BigDecimal("-1"));

                LabelPrint labelPrint = labelPrintMapper.selectById(sn.getLabelPrintBo());
                LabelPrintLog labelPrintLog = new LabelPrintLog();
                labelPrintLog.setLabelPrintBo(labelPrint.getBo());
                labelPrintLog.setLabelPrintDetailBo(sn.getBo());
                labelPrintLog.setPrintDate(new Date());
                labelPrintLog.setPrintUser(UserUtils.getCurrentUser().getUserName());
                labelPrintLog.setState(LabelPrintLogStateEnum.SCRAPPED.getCode());
                labelPrintLogService.save(labelPrintLog);
            }
        });

        return ResponseData.success();
    }

    @PostMapping("/updateOrderBo")
    @ApiOperation(value = "工单拆单-变更条码工单")
    public ResponseData updateOrderBo(@RequestParam List<String> snBoList, @RequestParam String newOrderBo) {
        // 查询工单未上线的产品编码
        if (snBoList.size() < 1 || snBoList == null) {
            return ResponseData.error("没有找到未上线的产品BO!");
        }
        return snService.updateOrderBo(snBoList, newOrderBo);
    }

    @PostMapping("/queryOrderBoList")
    @ApiOperation(value = "拆单使用：查询拆当前工单产品SN，未上线状态的条码BO")
    public ResponseData<List<String>> queryOrderBoList(@RequestParam String orderBo, @RequestParam int onLine) {
        return ResponseData.success(snService.queryOrderBoList(orderBo, onLine));
    }

    @GetMapping("/getItemInfoAndSnStateBySn/{sn}")
    @ApiOperation(value = "getItemInfoAndSnStateBySn for feign")
    public Map<String, String> getItemInfoAndSnStateBySn(@PathVariable("sn") String sn) {
        return snService.getItemInfoAndSnStateBySn(sn);
    }

    @GetMapping("/getSnInfo/{sn}")
    @ApiOperation(value = "getSnInfo for feign")
    public Sn getSnInfo(@PathVariable("sn") String sn) throws CommonException {
        List<Sn> ret = snService.lambdaQuery().eq(Sn::getSn, sn).list();
        if (ret == null || ret.size() == 0) {
            return null;
        }
        if (ret != null && ret.size() != 1) {
            throw new CommonException("无法确定SN的信息!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return ret.get(0);
    }

    @PostMapping("/collarSn/{sn}/{workShopBo}/{productLineBo}")
    @ApiOperation(value = "领用Sn for feign")
    public void collarSn(@PathVariable("sn") String sn,
                         @PathVariable("workShopBo") String workShopBo,
                         @PathVariable("productLineBo") String productLineBo) {
        snService.collar(sn, workShopBo, productLineBo);
    }

    @PostMapping("/changeSnStateByMap")
    @ApiOperation(value = "根据map<sn,state>信息改变对应条码状态 for feign")
    public Boolean changeSnStateByMap(Map<String, String> map) {
        return snService.changeSnStateByMap(map);
    }


    /**
     * 条码列表
     *
     * @param params 分页查询参数
     */
    @ApiOperation(value = "条码转移-条码列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页面，默认为1"),
            @ApiImplicitParam(name = "limit", value = "分页大小，默认20，可不填"),
            @ApiImplicitParam(name = "detailCode", value = "条码code, 模糊查询"),
            @ApiImplicitParam(name = "elementBo", value = "工单编号"),
            @ApiImplicitParam(name = "state", value = "条码状态")
    })
    @GetMapping(value = "/transfer/list")
    public ResponseData<Page<LabelTransVo>> labelList(@RequestParam Map<String, Object> params) {
        StringUtils.replaceMatchTextByMap(params);
        String state = (String) params.get("state");
        if(StrUtil.isNotBlank(state)) {
            params.put("state", StrUtil.splitTrim(state, ","));
        }
        if(StrUtil.isBlank((String)params.get("limit"))) {
            params.put("limit", 10);
        }
        return ResponseData.success(snService.labelTransList(params));
    }


    /**
     * 条码转移
     *
     * @param labelTransferRequestDTO 条码转移参数
     */
    @PostMapping("/transfer")
    @ApiOperation(value = "条码转移")
    public ResponseData<Boolean> transferLabels(@RequestBody @Valid LabelTransferRequestDTO labelTransferRequestDTO) {
        return ResponseData.success(snService.transferLabels(labelTransferRequestDTO));
    }

    /**
     * 工单拆单时，条码转移
     *
     * @param labelTransferRequestDTO 工单拆单时，条码转移参数
     */
    @PostMapping("/transferAsOrder")
    @ApiOperation(value = "工单拆单时，条码转移")
    public ResponseData<Boolean> transferLabelsAsOrder(@RequestBody @Valid LabelTransferRequestDTO labelTransferRequestDTO) {
        return ResponseData.success(snService.transferLabelsAsOrder(labelTransferRequestDTO));
    }

    /**
     * 条码转移记录
     */
    @GetMapping("/transfer/record")
    @ApiOperation(value = "条码转移记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页面，默认为1"),
            @ApiImplicitParam(name = "limit", value = "分页大小，默认20，可不填"),
            @ApiImplicitParam(name = "sn", value = "条码")
    })
    public ResponseData<Page<SnTransRecord>> selectTransRecordPage(@RequestParam Map<String, Object> params) {
        StringUtils.replaceMatchTextByMap(params);
        return ResponseData.success(snTransRecordService.page(params));
    }

}


