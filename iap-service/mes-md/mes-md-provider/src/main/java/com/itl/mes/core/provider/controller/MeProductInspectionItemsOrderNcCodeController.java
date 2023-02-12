package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.entity.MeProductInspectionItemsOrderNcCode;
import com.itl.mes.core.api.service.MeProductInspectionItemsOrderNcCodeService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author chenjx1
 * @since 2021-12-10
 */
@RestController
@RequestMapping("/productOrderNcCodes")
@Api(tags = "产品检验项目不合格代码表-工单副本" )
public class MeProductInspectionItemsOrderNcCodeController {
    private final Logger logger = LoggerFactory.getLogger(MeProductInspectionItemsOrderNcCodeController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public MeProductInspectionItemsOrderNcCodeService meProductInspectionItemsOrderNcCodeService;


    /**
     * 列表-产品检验项不良代码
     */
    @PostMapping("/listItemNcCodes")
    public ResponseData<List<MeProductInspectionItemsOrderNcCode>> listItemNcCodes(@RequestBody MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) {
        final LambdaQueryWrapper<MeProductInspectionItemsOrderNcCode> lambda = new QueryWrapper<MeProductInspectionItemsOrderNcCode>().lambda();
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderNcCode.getBo())) {
            lambda.eq(MeProductInspectionItemsOrderNcCode::getBo, meProductInspectionItemsOrderNcCode.getBo());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderNcCode.getNcBo())) {
            lambda.eq(MeProductInspectionItemsOrderNcCode::getNcBo, meProductInspectionItemsOrderNcCode.getNcBo());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderNcCode.getNcCode())) {
            lambda.eq(MeProductInspectionItemsOrderNcCode::getNcCode, meProductInspectionItemsOrderNcCode.getNcCode());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderNcCode.getNcName())) {
            lambda.eq(MeProductInspectionItemsOrderNcCode::getNcName, meProductInspectionItemsOrderNcCode.getNcName());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderNcCode.getNcDesc())) {
            lambda.eq(MeProductInspectionItemsOrderNcCode::getNcDesc, meProductInspectionItemsOrderNcCode.getNcDesc());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderNcCode.getNcType())) {
            lambda.eq(MeProductInspectionItemsOrderNcCode::getNcType, meProductInspectionItemsOrderNcCode.getNcType());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderNcCode.getState())) {
            lambda.eq(MeProductInspectionItemsOrderNcCode::getState, meProductInspectionItemsOrderNcCode.getState());
        }
        if (meProductInspectionItemsOrderNcCode.getSerialNum() != null && !"".equals(meProductInspectionItemsOrderNcCode.getSerialNum())) {
            lambda.eq(MeProductInspectionItemsOrderNcCode::getSerialNum, meProductInspectionItemsOrderNcCode.getSerialNum());
        }
        if (meProductInspectionItemsOrderNcCode.getOrderBo() != null && !"".equals(meProductInspectionItemsOrderNcCode.getOrderBo())) {
            lambda.eq(MeProductInspectionItemsOrderNcCode::getOrderBo, meProductInspectionItemsOrderNcCode.getOrderBo());
        }
        if (meProductInspectionItemsOrderNcCode.getInspectionItemId() != null && !"".equals(meProductInspectionItemsOrderNcCode.getInspectionItemId())) {
            lambda.eq(MeProductInspectionItemsOrderNcCode::getInspectionItemId, meProductInspectionItemsOrderNcCode.getInspectionItemId());
        }
        if(meProductInspectionItemsOrderNcCode.getItemType() != null && !"".equals(meProductInspectionItemsOrderNcCode.getItemType())){
            lambda.eq(MeProductInspectionItemsOrderNcCode::getItemType, meProductInspectionItemsOrderNcCode.getItemType());
        }
        lambda.eq(MeProductInspectionItemsOrderNcCode::getSite, UserUtils.getSite());
        // 升排序（从小到大）
        lambda.orderByAsc(MeProductInspectionItemsOrderNcCode::getOrderBo, MeProductInspectionItemsOrderNcCode::getInspectionItemId, MeProductInspectionItemsOrderNcCode::getSerialNum);
        final List<MeProductInspectionItemsOrderNcCode> list = meProductInspectionItemsOrderNcCodeService.list(lambda);
        return ResponseData.success(list);
    }

    /**
     * 根据BO查询
     */
    @GetMapping("/info/{bo}")
    public ResponseData info(@PathVariable("bo") String bo) {
        MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = meProductInspectionItemsOrderNcCodeService.getById(bo);
        return ResponseData.success(meProductInspectionItemsOrderNcCode);
    }

    /**
     * 根据bos查询列表
     */
    @PostMapping("/listByBos")
    public ResponseData<Collection<MeProductInspectionItemsOrderNcCode>> listByBos(@RequestBody List<String> bos) {
        return ResponseData.success(meProductInspectionItemsOrderNcCodeService.listByIds(bos));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public ResponseData save(@RequestBody MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) throws CommonException {
        meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
        return ResponseData.success(true);
    }

    /**
     * 保存集合
     */
    @PostMapping("/saveList")
    public ResponseData saveList(@RequestBody List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList) throws CommonException {
        // 保存检验项目不良代码
        meProductInspectionItemsOrderNcCodeService.saveList(meProductInspectionItemsOrderNcCodeList);
//        // 先删除检验项不良代码
//        for(MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode : meProductInspectionItemsNcCodeList){
//            Integer inspectionItemId = meProductInspectionItemsOrderNcCode.getInspectionItemId();
//            if(inspectionItemId != null && !"".equals(inspectionItemId)){
//                deleteByInspectionItemId(inspectionItemId);
//            }
//        }
//        for(MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode : meProductInspectionItemsNcCodeList){
//            Boolean checkBl = meProductInspectionItemsOrderNcCodeService.checkItemsNcCode(meProductInspectionItemsOrderNcCode);
//            if(!checkBl){
//                return ResponseData.error("产品检验项目不良代码校验失败！");
//            }
//            // String bo = meProductInspectionItemsOrderNcCode.getBo();
//            meProductInspectionItemsOrderNcCode.setBo(null);
//            Boolean bl = false;
//            bl = meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
//            if(!bl){
//                throw new CommonException("保存失败", CommonExceptionDefinition.BASIC_EXCEPTION);
//                // return ResponseData.error("保存失败！");
//            }
//        }
        return ResponseData.success(true);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public ResponseData update(@RequestBody MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) throws CommonException {
        meProductInspectionItemsOrderNcCodeService.updateById(meProductInspectionItemsOrderNcCode);
        return ResponseData.success();
    }

    /**
     * 根据BO删除
     */
    @PostMapping("/deleteByBos")
    public ResponseData deleteByBos(@RequestBody String[] bos) {
        meProductInspectionItemsOrderNcCodeService.removeByIds(Arrays.asList(bos));
        return ResponseData.success();
    }

    /**
     * 根据工单BO删除不良代码
     */
    @PostMapping("/deleteNcCodesByOrderBo")
    public ResponseData deleteNcCodesByOrderBo(@RequestParam String orderBo) throws CommonException {
        int i = meProductInspectionItemsOrderNcCodeService.deleteNcCodesByOrderBo(orderBo);
        return ResponseData.success(i);
    }

    /**
     * 根据工单BO+产品检验项目ID删除不良代码
     */
    @PostMapping("/deleteByInspectionItemId")
    public ResponseData deleteByInspectionItemId(@RequestParam String orderBo, @RequestParam Integer inspectionItemId) throws CommonException {
        int i = meProductInspectionItemsOrderNcCodeService.deleteByInspectionItemId(orderBo, inspectionItemId);
        return ResponseData.success(i);
    }

    /**
     * 根据工单BO+产品检验项目ID+产品类型删除不良代码
     */
    @PostMapping("/deleteByInspectionItemIdItemType")
    public ResponseData deleteByInspectionItemIdItemType(@RequestParam String orderBo, @RequestParam Integer inspectionItemId, @RequestParam String itemType) throws CommonException {
        int i = meProductInspectionItemsOrderNcCodeService.deleteByInspectionItemIdItemType(orderBo, inspectionItemId, itemType);
        return ResponseData.success(i);
    }


}
