package com.itl.mes.me.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.NcCodeHandleBO;
import com.itl.mes.me.api.entity.MeProductInspectionItemsNcCode;
import com.itl.mes.me.api.service.MeProductInspectionItemsNcCodeService;
import com.itl.mes.me.api.vo.MeProductInspectionItemsNcCodeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/productNcCodes")
@Api(tags = "产品检验项目不合格代码表" )
public class MeProductInspectionItemsNcCodeController {
    private final Logger logger = LoggerFactory.getLogger(MeProductInspectionItemsNcCodeController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public MeProductInspectionItemsNcCodeService meProductInspectionItemsNcCodeService;


    /**
     * 列表-产品检验项不良代码
     */
    @PostMapping("/listItemNcCodes")
    @ApiOperation(value = "产品检验项不良代码列表查询")
    public ResponseData<List<MeProductInspectionItemsNcCode>> listItemNcCodes(@RequestBody MeProductInspectionItemsNcCode meProductInspectionItemsNcCode) {
        final LambdaQueryWrapper<MeProductInspectionItemsNcCode> lambda = new QueryWrapper<MeProductInspectionItemsNcCode>().lambda();
        if (StrUtil.isNotBlank(meProductInspectionItemsNcCode.getBo())) {
            lambda.eq(MeProductInspectionItemsNcCode::getBo, meProductInspectionItemsNcCode.getBo());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsNcCode.getNcBo())) {
            lambda.eq(MeProductInspectionItemsNcCode::getNcBo, meProductInspectionItemsNcCode.getNcBo());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsNcCode.getNcCode())) {
            lambda.eq(MeProductInspectionItemsNcCode::getNcCode, meProductInspectionItemsNcCode.getNcCode());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsNcCode.getNcName())) {
            lambda.eq(MeProductInspectionItemsNcCode::getNcName, meProductInspectionItemsNcCode.getNcName());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsNcCode.getNcDesc())) {
            lambda.eq(MeProductInspectionItemsNcCode::getNcDesc, meProductInspectionItemsNcCode.getNcDesc());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsNcCode.getNcType())) {
            lambda.eq(MeProductInspectionItemsNcCode::getNcType, meProductInspectionItemsNcCode.getNcType());
        }
        if (meProductInspectionItemsNcCode.getSerialNum() != null && !"".equals(meProductInspectionItemsNcCode.getSerialNum())) {
            lambda.eq(MeProductInspectionItemsNcCode::getSerialNum, meProductInspectionItemsNcCode.getSerialNum());
        }
        if (meProductInspectionItemsNcCode.getInspectionItemId() != null && !"".equals(meProductInspectionItemsNcCode.getInspectionItemId())) {
            lambda.eq(MeProductInspectionItemsNcCode::getInspectionItemId, meProductInspectionItemsNcCode.getInspectionItemId());
        }
        if(meProductInspectionItemsNcCode.getItemType() == null || "".equals(meProductInspectionItemsNcCode.getItemType())){
            return ResponseData.error("产品检验项目类型(itemType)不能为空！");
        }
        lambda.eq(MeProductInspectionItemsNcCode::getItemType, meProductInspectionItemsNcCode.getItemType());
//        if (StrUtil.isNotBlank(meProductInspectionItemsNcCode.getItemType())) {
//            lambda.eq(MeProductInspectionItemsNcCode::getItemType, meProductInspectionItemsNcCode.getItemType());
//        }
        // 升排序（从小到大）
        lambda.orderByAsc(MeProductInspectionItemsNcCode::getInspectionItemId,MeProductInspectionItemsNcCode::getSerialNum);
        final List<MeProductInspectionItemsNcCode> list = meProductInspectionItemsNcCodeService.list(lambda);
        return ResponseData.success(list);
    }

    /**
     * 产品检验项不良代码关联列表查询Two
     */
    @PostMapping("/listItemNcCodesTwo")
    @ApiOperation(value = "产品检验项不良代码关联列表查询Two")
    public ResponseData<List<MeProductInspectionItemsNcCodeVo>> listItemNcCodesTwo(@RequestBody MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo) {
        if(meProductInspectionItemsNcCodeVo.getItemType() == null || "".equals(meProductInspectionItemsNcCodeVo.getItemType())){
            return ResponseData.error("产品检验项目类型(itemType)不能为空！");
        }
        meProductInspectionItemsNcCodeVo.setSite(UserUtils.getSite());
        List<MeProductInspectionItemsNcCodeVo> list = meProductInspectionItemsNcCodeService.listItemNcCodesTwo(meProductInspectionItemsNcCodeVo);
        return ResponseData.success(list);
    }

    /**
     * 根据BO查询
     */
    @GetMapping("/info/{bo}")
    public ResponseData info(@PathVariable("bo") String bo) {
        MeProductInspectionItemsNcCode meProductInspectionItemsNcCode = meProductInspectionItemsNcCodeService.getById(bo);
        return ResponseData.success(meProductInspectionItemsNcCode);
    }

    /**
     * 根据bos查询列表
     */
    @PostMapping("/listByBos")
    public ResponseData<Collection<MeProductInspectionItemsNcCode>> listByBos(@RequestBody List<String> bos) {
        return ResponseData.success(meProductInspectionItemsNcCodeService.listByIds(bos));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public ResponseData save(@RequestBody MeProductInspectionItemsNcCode meProductInspectionItemsNcCode) throws CommonException {
        // 先删除工单检验项副本
        /*String orderBo = meProductInspectionItemsOrderEntity.getOrderBo();
        if(orderBo != null || !"".equals(orderBo)){
            deleteOrderItems(orderBo);
        }*/
        Boolean checkBl = meProductInspectionItemsNcCodeService.checkItemsNcCode(meProductInspectionItemsNcCode);
        if(!checkBl){
            return ResponseData.error("产品检验项目不良代码校验失败！");
        }

        String site = UserUtils.getSite();
        String ncCode = meProductInspectionItemsNcCode.getNcCode();
        NcCodeHandleBO ncCodeHandleBO = new NcCodeHandleBO(site,ncCode);
        meProductInspectionItemsNcCode.setNcBo(ncCodeHandleBO.getBo());
        meProductInspectionItemsNcCode.setSite(site);

        // 保存产品检验项不良代码
        String bo = meProductInspectionItemsNcCode.getBo();
        Boolean bl = false;
        if(bo == null || "".equals(bo)){
            bl = meProductInspectionItemsNcCodeService.save(meProductInspectionItemsNcCode);
            if(!bl){
                return ResponseData.error("产品检验项目不良代码新增保存失败！");
            }
        }else{
            bl = meProductInspectionItemsNcCodeService.updateById(meProductInspectionItemsNcCode);
            if(!bl){
                return ResponseData.error("产品检验项目不良代码修改保存失败！");
            }
        }
        return ResponseData.success(true);
    }

    /**
     * 保存集合
     */
    @PostMapping("/saveList")
    public ResponseData saveList(@RequestBody List<MeProductInspectionItemsNcCode> meProductInspectionItemsNcCodeList) throws CommonException {
        // 保存检验项目不良代码
        meProductInspectionItemsNcCodeService.saveList(meProductInspectionItemsNcCodeList);
//        // 先删除检验项不良代码
//        for(MeProductInspectionItemsNcCode meProductInspectionItemsNcCode : meProductInspectionItemsNcCodeList){
//            Integer inspectionItemId = meProductInspectionItemsNcCode.getInspectionItemId();
//            if(inspectionItemId != null && !"".equals(inspectionItemId)){
//                deleteByInspectionItemId(inspectionItemId);
//            }
//        }
//        for(MeProductInspectionItemsNcCode meProductInspectionItemsNcCode : meProductInspectionItemsNcCodeList){
//            Boolean checkBl = meProductInspectionItemsNcCodeService.checkItemsNcCode(meProductInspectionItemsNcCode);
//            if(!checkBl){
//                return ResponseData.error("产品检验项目不良代码校验失败！");
//            }
//            // String bo = meProductInspectionItemsNcCode.getBo();
//            meProductInspectionItemsNcCode.setBo(null);
//            Boolean bl = false;
//            bl = meProductInspectionItemsNcCodeService.save(meProductInspectionItemsNcCode);
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
    public ResponseData update(@RequestBody MeProductInspectionItemsNcCode meProductInspectionItemsNcCode) {
        meProductInspectionItemsNcCodeService.updateById(meProductInspectionItemsNcCode);
        return ResponseData.success();
    }

    /**
     * 根据BO删除
     */
    @PostMapping("/deleteByBos")
    public ResponseData deleteByBos(@RequestBody String[] bos) {
        meProductInspectionItemsNcCodeService.removeByIds(Arrays.asList(bos));
        return ResponseData.success();
    }

    /**
     * 根据产品检验项目ID删除不良代码
     */
    @PostMapping("/deleteByInspectionItemId")
    public ResponseData deleteByInspectionItemId(@RequestParam Integer inspectionItemId) {
        LambdaQueryWrapper<MeProductInspectionItemsNcCode> query = new QueryWrapper<MeProductInspectionItemsNcCode>().lambda()
                .eq(MeProductInspectionItemsNcCode::getInspectionItemId, inspectionItemId);
        meProductInspectionItemsNcCodeService.remove(query);
        return ResponseData.success();
    }


}
