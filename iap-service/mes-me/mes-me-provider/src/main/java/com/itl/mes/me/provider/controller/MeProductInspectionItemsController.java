package com.itl.mes.me.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.OperationHandleBO;
import com.itl.mes.core.api.bo.UomHandleBO;
import com.itl.mes.me.api.dto.MeProductInspectionItemsDto;
import com.itl.mes.me.api.entity.MeProductInspectionItemsEntity;
import com.itl.mes.me.api.service.MeProductInspectionItemsService;
import com.itl.mes.me.provider.mapper.SnMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;


/**
 * 产品检验项目
 *
 * @author cch
 * @email ${email}
 * @date 2021-10-18 16:14:54
 */
@RestController
@RequestMapping("/product")
@Api(tags = "产品检验项目")
public class MeProductInspectionItemsController {
    @Autowired
    private MeProductInspectionItemsService meProductInspectionItemsService;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    SnMapper snMapper;

    /**
     * 列表
     */
    @PostMapping("/list")
    public ResponseData list(@RequestBody MeProductInspectionItemsDto meProductInspectionItemsDto) {
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
            final Date effectiveDateEnd = meProductInspectionItemsDto.getEffectiveDateEnd();
            effectiveDateEnd.setHours(23);
            effectiveDateEnd.setMinutes(59);
            effectiveDateEnd.setSeconds(59);
            lambda.between(MeProductInspectionItemsEntity::getEffectiveDate, meProductInspectionItemsDto.getEffectiveDateStart(), effectiveDateEnd);
        }
        if (meProductInspectionItemsDto.getExpiryDateStart() != null && meProductInspectionItemsDto.getExpiryDateEnd() != null) {
            final Date expiryDateEnd = meProductInspectionItemsDto.getExpiryDateEnd();
            expiryDateEnd.setHours(23);
            expiryDateEnd.setMinutes(59);
            expiryDateEnd.setSeconds(59);
            lambda.between(MeProductInspectionItemsEntity::getExpiryDate, meProductInspectionItemsDto.getExpiryDateStart(), expiryDateEnd);
        }

        final IPage<MeProductInspectionItemsEntity> page = meProductInspectionItemsService.page(meProductInspectionItemsDto.getPage() == null ? new Page<>(0, 10) : meProductInspectionItemsDto.getPage(), lambda.orderByDesc(MeProductInspectionItemsEntity::getCreateDate));
        page.getRecords().forEach(x -> {
            change(x);
            final ItemHandleBO itemHandleBO = new ItemHandleBO(x.getItemBo());
            x.setItemName(snMapper.getById(x.getItemBo()));
        });
        return ResponseData.success(page);
    }

    public void change(MeProductInspectionItemsEntity x) {
        final String itemBo = x.getItemBo();
        if (itemBo.startsWith("ITEM:")) {
            x.setItem(new ItemHandleBO(itemBo).getItem());
        }
        final String operationBo = x.getOperationBo();
        if (operationBo.startsWith("OP:")) {
            x.setOperation(new OperationHandleBO(operationBo).getOperation());
        }
        final String unitBo = x.getUnitBo();
        if (unitBo.startsWith("UOM:")) {
            x.setUnit(new UomHandleBO(unitBo).getUom());
        }
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public ResponseData info(@PathVariable("id") Integer id) {
        MeProductInspectionItemsEntity x = meProductInspectionItemsService.getById(id);
        change(x);
        return ResponseData.success(x);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public ResponseData save(@RequestBody MeProductInspectionItemsEntity meProductInspectionItems) {
        meProductInspectionItems.setCreateDate(new Date());
        meProductInspectionItems.setCreateUser(userUtil.getUser().getUserName());
        meProductInspectionItems.setSite(UserUtils.getSite());
        meProductInspectionItemsService.save(meProductInspectionItems);
        return ResponseData.success();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public ResponseData update(@RequestBody MeProductInspectionItemsEntity meProductInspectionItems) {
        meProductInspectionItemsService.updateById(meProductInspectionItems);

        return ResponseData.success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody Integer[] ids) {
        meProductInspectionItemsService.removeByIds(Arrays.asList(ids));

        return ResponseData.success();
    }

}
