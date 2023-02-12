package com.itl.mes.me.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.entity.MeProductInspectionItemsOrderNcCode;
import com.itl.mes.core.client.service.MeProductInspectionItemsOrderNcCodeService;
import com.itl.mes.me.api.dto.MeProductGroupInspectionItemsDto;
import com.itl.mes.me.api.dto.MeProductInspectionItemsDto;
import com.itl.mes.me.api.dto.MeProductInspectionItemsOrderDto;
import com.itl.mes.me.api.entity.MeProductGroupInspectionItemsEntity;
import com.itl.mes.me.api.entity.MeProductInspectionItemsEntity;
import com.itl.mes.me.api.entity.MeProductInspectionItemsOrderEntity;
import com.itl.mes.me.api.service.MeProductGroupInspectionItemsService;
import com.itl.mes.me.api.service.MeProductInspectionItemsOrderService;
import com.itl.mes.me.api.service.MeProductInspectionItemsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * 产品检验项目-工单副本
 *
 * @author chenjx1
 * @date 2021-10-20
 */
@RestController
@RequestMapping("/productOrder")
@Api(tags = "产品检验项目-工单副本")
public class MeProductInspectionItemsOrderController {
    @Autowired
    private MeProductInspectionItemsService meProductInspectionItemsService;

    @Autowired
    private MeProductGroupInspectionItemsService meProductGroupInspectionItemsService;

    @Autowired
    private MeProductInspectionItemsOrderService meProductInspectionItemsOrderService;

    @Autowired
    private MeProductInspectionItemsOrderNcCodeService meProductInspectionItemsOrderNcCodeService;

    /**
     * 列表-产品检验项-工单副本
     */
    @PostMapping("/listItemsOrder")
    public ResponseData<IPage<MeProductInspectionItemsOrderEntity>> listItemsOrder(@RequestBody MeProductInspectionItemsOrderDto meProductInspectionItemsOrderDto) {
        final LambdaQueryWrapper<MeProductInspectionItemsOrderEntity> lambda = new QueryWrapper<MeProductInspectionItemsOrderEntity>().lambda();
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderDto.getOrderBo())) {
            lambda.eq(MeProductInspectionItemsOrderEntity::getOrderBo, meProductInspectionItemsOrderDto.getOrderBo());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderDto.getItemType())) {
            lambda.eq(MeProductInspectionItemsOrderEntity::getItemType, meProductInspectionItemsOrderDto.getItemType());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderDto.getItemBo())) {
            lambda.eq(MeProductInspectionItemsOrderEntity::getItemBo, meProductInspectionItemsOrderDto.getItemBo());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderDto.getItemGroupBo())) {
            lambda.eq(MeProductInspectionItemsOrderEntity::getItemGroupBo, meProductInspectionItemsOrderDto.getItemGroupBo());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderDto.getOperationBo())) {
            lambda.eq(MeProductInspectionItemsOrderEntity::getOperationBo, meProductInspectionItemsOrderDto.getOperationBo());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderDto.getCheckMark())) {
            lambda.likeLeft(MeProductInspectionItemsOrderEntity::getCheckMark, meProductInspectionItemsOrderDto.getCheckMark());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderDto.getCheckType())) {
            lambda.likeLeft(MeProductInspectionItemsOrderEntity::getCheckType, meProductInspectionItemsOrderDto.getCheckType());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsOrderDto.getCheckProject())) {
            lambda.likeLeft(MeProductInspectionItemsOrderEntity::getCheckProject, meProductInspectionItemsOrderDto.getCheckProject());
        }
        if (meProductInspectionItemsOrderDto.getEffectiveDateStart() != null && meProductInspectionItemsOrderDto.getEffectiveDateEnd() != null) {
            lambda.between(MeProductInspectionItemsOrderEntity::getEffectiveDate, meProductInspectionItemsOrderDto.getEffectiveDateStart(), meProductInspectionItemsOrderDto.getEffectiveDateEnd());
        }
        if (meProductInspectionItemsOrderDto.getExpiryDateStart() != null && meProductInspectionItemsOrderDto.getExpiryDateEnd() != null) {
            lambda.between(MeProductInspectionItemsOrderEntity::getExpiryDate, meProductInspectionItemsOrderDto.getExpiryDateStart(), meProductInspectionItemsOrderDto.getExpiryDateEnd());
        }

        final IPage<MeProductInspectionItemsOrderEntity> page = meProductInspectionItemsOrderService.page(meProductInspectionItemsOrderDto.getPage() == null ? new Page<>(0, 10) : meProductInspectionItemsOrderDto.getPage(), lambda);

        // 查询工单产品检验项目的不良代码集合
        List<MeProductInspectionItemsOrderEntity> meProductInspectionItemsOrderEntityList = page.getRecords();
        if(meProductInspectionItemsOrderEntityList != null && !"".equals(meProductInspectionItemsOrderEntityList)){
            for(MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity : meProductInspectionItemsOrderEntityList){
                List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList = new ArrayList<>();
                MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = new MeProductInspectionItemsOrderNcCode();
                String orderBo = meProductInspectionItemsOrderEntity.getOrderBo();
                Integer inspectionItemId = meProductInspectionItemsOrderEntity.getId();
                meProductInspectionItemsOrderNcCode.setOrderBo(orderBo);
                meProductInspectionItemsOrderNcCode.setInspectionItemId(inspectionItemId);
                ResponseData<List<MeProductInspectionItemsOrderNcCode>> listResponseData = meProductInspectionItemsOrderNcCodeService.listItemNcCodes(meProductInspectionItemsOrderNcCode);
                Assert.valid(!listResponseData.isSuccess(), listResponseData.getMsg());
                meProductInspectionItemsOrderNcCodeList = listResponseData.getData();
                meProductInspectionItemsOrderEntity.setMeProductInspectionItemsOrderNcCodeList(meProductInspectionItemsOrderNcCodeList);
            }
        }

        return ResponseData.success(page);
    }

    /**
     * 列表-产品检验项
     */
    @PostMapping("/listItems")
    public List<MeProductInspectionItemsEntity> listItems(@RequestBody MeProductInspectionItemsDto meProductInspectionItemsDto) {
        final LambdaQueryWrapper<MeProductInspectionItemsEntity> lambda = new QueryWrapper<MeProductInspectionItemsEntity>().lambda();
        lambda.eq(MeProductInspectionItemsEntity::getSite, UserUtils.getSite());
        if (StrUtil.isNotBlank(meProductInspectionItemsDto.getItemBo())) {
            lambda.eq(MeProductInspectionItemsEntity::getItemBo, meProductInspectionItemsDto.getItemBo());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsDto.getOperationBo())) {
            lambda.eq(MeProductInspectionItemsEntity::getOperationBo, meProductInspectionItemsDto.getOperationBo());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsDto.getCheckMark())) {
            lambda.likeLeft(MeProductInspectionItemsEntity::getCheckMark, meProductInspectionItemsDto.getCheckMark());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsDto.getCheckType())) {
            lambda.likeLeft(MeProductInspectionItemsEntity::getCheckType, meProductInspectionItemsDto.getCheckType());
        }
        if (StrUtil.isNotBlank(meProductInspectionItemsDto.getCheckProject())) {
            lambda.likeLeft(MeProductInspectionItemsEntity::getCheckProject, meProductInspectionItemsDto.getCheckProject());
        }
        if (meProductInspectionItemsDto.getEffectiveDateStart() != null && meProductInspectionItemsDto.getEffectiveDateEnd() != null) {
            lambda.between(MeProductInspectionItemsEntity::getEffectiveDate, meProductInspectionItemsDto.getEffectiveDateStart(), meProductInspectionItemsDto.getEffectiveDateEnd());
        }
        if (meProductInspectionItemsDto.getExpiryDateStart() != null && meProductInspectionItemsDto.getExpiryDateEnd() != null) {
            lambda.between(MeProductInspectionItemsEntity::getExpiryDate, meProductInspectionItemsDto.getExpiryDateStart(), meProductInspectionItemsDto.getExpiryDateEnd());
        }

        List<MeProductInspectionItemsEntity> meProductInspectionItemsEntityList = meProductInspectionItemsService.list(lambda);
        return meProductInspectionItemsEntityList;
    }

    /**
     * 列表-产品组检验项
     */
    @PostMapping("/listGroupItems")
    public List<MeProductGroupInspectionItemsEntity> listGroupItems(@RequestBody MeProductGroupInspectionItemsDto meProductGroupInspectionItemsDto) {
        final LambdaQueryWrapper<MeProductGroupInspectionItemsEntity> lambda = new QueryWrapper<MeProductGroupInspectionItemsEntity>().lambda();
        lambda.eq(MeProductGroupInspectionItemsEntity::getSite, UserUtils.getSite());
        if (StrUtil.isNotBlank(meProductGroupInspectionItemsDto.getItemGroupBo())) {
            lambda.eq(MeProductGroupInspectionItemsEntity::getItemGroupBo, meProductGroupInspectionItemsDto.getItemGroupBo());
        }
        if (StrUtil.isNotBlank(meProductGroupInspectionItemsDto.getOperationBo())) {
            lambda.eq(MeProductGroupInspectionItemsEntity::getOperationBo, meProductGroupInspectionItemsDto.getOperationBo());
        }
        if (StrUtil.isNotBlank(meProductGroupInspectionItemsDto.getCheckMark())) {
            lambda.likeLeft(MeProductGroupInspectionItemsEntity::getCheckMark, meProductGroupInspectionItemsDto.getCheckMark());
        }
        if (StrUtil.isNotBlank(meProductGroupInspectionItemsDto.getCheckType())) {
            lambda.likeLeft(MeProductGroupInspectionItemsEntity::getCheckType, meProductGroupInspectionItemsDto.getCheckType());
        }
        if (StrUtil.isNotBlank(meProductGroupInspectionItemsDto.getCheckProject())) {
            lambda.likeLeft(MeProductGroupInspectionItemsEntity::getCheckProject, meProductGroupInspectionItemsDto.getCheckProject());
        }
        if (meProductGroupInspectionItemsDto.getEffectiveDateStart() != null && meProductGroupInspectionItemsDto.getEffectiveDateEnd() != null) {
            lambda.between(MeProductGroupInspectionItemsEntity::getEffectiveDate, meProductGroupInspectionItemsDto.getEffectiveDateStart(), meProductGroupInspectionItemsDto.getEffectiveDateEnd());
        }
        if (meProductGroupInspectionItemsDto.getExpiryDateStart() != null && meProductGroupInspectionItemsDto.getExpiryDateEnd() != null) {
            lambda.between(MeProductGroupInspectionItemsEntity::getExpiryDate, meProductGroupInspectionItemsDto.getExpiryDateStart(), meProductGroupInspectionItemsDto.getExpiryDateEnd());
        }

        List<MeProductGroupInspectionItemsEntity> meProductGroupInspectionItemsEntityList = meProductGroupInspectionItemsService.list(lambda);
        return meProductGroupInspectionItemsEntityList;
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public ResponseData info(@PathVariable("id") Integer id) {
        MeProductInspectionItemsOrderEntity meProductInspectionItems = meProductInspectionItemsOrderService.getById(id);
        return ResponseData.success(meProductInspectionItems);
    }

    /**
     * 根据ids查询列表
     */
    @PostMapping("/listByIds")
    public ResponseData<Collection<MeProductInspectionItemsOrderEntity>> listByIds(@RequestBody List<Integer> ids) {
        return ResponseData.success(meProductInspectionItemsOrderService.listByIds(ids));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public ResponseData save(@RequestBody MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity) throws CommonException {
        // 保存工单检验项副本
        Boolean bl = meProductInspectionItemsOrderService.save(meProductInspectionItemsOrderEntity);
//        if(!bl){
//            return ResponseData.error("保存失败！");
//        }
        return ResponseData.success(bl);
    }

    /**
     * 保存集合
     */
    @PostMapping("/saveList")
    public ResponseData saveList(@RequestBody List<MeProductInspectionItemsOrderEntity> meProductInspectionItemsOrderEntityList) throws CommonException {
        Boolean bl = meProductInspectionItemsOrderService.saveList(meProductInspectionItemsOrderEntityList);
//        if(!bl){
//            return ResponseData.error("保存失败！");
//        }
        return ResponseData.success(bl);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public ResponseData update(@RequestBody MeProductInspectionItemsOrderEntity meProductInspectionItems) {
        Boolean bl = meProductInspectionItemsOrderService.updateById(meProductInspectionItems);
        return ResponseData.success(bl);
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody Integer[] ids) {
        Boolean bl = meProductInspectionItemsOrderService.removeByIds(Arrays.asList(ids));
        return ResponseData.success(bl);
    }

    /**
     * 删除工单检验项目副本
     */
    @PostMapping("/deleteOrderItems")
    public ResponseData deleteOrderItems(@RequestParam String orderBo) {
        int i = meProductInspectionItemsOrderService.deleteOrderItems(orderBo);
        return ResponseData.success(i);
    }


}
