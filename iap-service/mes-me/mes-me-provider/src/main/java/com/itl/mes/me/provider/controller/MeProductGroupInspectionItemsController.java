package com.itl.mes.me.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ItemGroupHandleBO;
import com.itl.mes.core.api.bo.OperationHandleBO;
import com.itl.mes.core.api.bo.UomHandleBO;
import com.itl.mes.me.api.dto.MeProductGroupInspectionItemsDto;
import com.itl.mes.me.api.entity.MeProductGroupInspectionItemsEntity;
import com.itl.mes.me.api.service.MeProductGroupInspectionItemsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;


/**
 * 产品组检验项目
 *
 * @author cch
 * @email ${email}
 * @date 2021-10-18 16:14:54
 */
@RestController
@RequestMapping("/productGroup")
@Api(tags = "产品组检验项目")
public class MeProductGroupInspectionItemsController {
    @Autowired
    private MeProductGroupInspectionItemsService meProductGroupInspectionItemsService;
    @Autowired
    private UserUtil userUtil;

    /**
     * 列表
     */
    @PostMapping("/list")
    public ResponseData list(@RequestBody MeProductGroupInspectionItemsDto meProductGroupInspectionItemsDto) {
        final LambdaQueryWrapper<MeProductGroupInspectionItemsEntity> lambda = new QueryWrapper<MeProductGroupInspectionItemsEntity>().lambda();
        lambda.eq(MeProductGroupInspectionItemsEntity::getSite, UserUtils.getSite());
        if (StrUtil.isNotBlank(meProductGroupInspectionItemsDto.getOperationBo())) {
            lambda.eq(MeProductGroupInspectionItemsEntity::getOperationBo, meProductGroupInspectionItemsDto.getOperationBo());
        }
        if (StrUtil.isNotBlank(meProductGroupInspectionItemsDto.getItemGroupBo())) {
            lambda.eq(MeProductGroupInspectionItemsEntity::getItemGroupBo, meProductGroupInspectionItemsDto.getItemGroupBo());
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
            final Date effectiveDateEnd = meProductGroupInspectionItemsDto.getEffectiveDateEnd();
            effectiveDateEnd.setHours(23);
            effectiveDateEnd.setMinutes(59);
            effectiveDateEnd.setSeconds(59);
            lambda.between(MeProductGroupInspectionItemsEntity::getEffectiveDate, meProductGroupInspectionItemsDto.getEffectiveDateStart(), effectiveDateEnd);
        }
        if (meProductGroupInspectionItemsDto.getExpiryDateStart() != null && meProductGroupInspectionItemsDto.getExpiryDateEnd() != null) {
            final Date expiryDateEnd = meProductGroupInspectionItemsDto.getExpiryDateEnd();
            expiryDateEnd.setHours(23);
            expiryDateEnd.setMinutes(59);
            expiryDateEnd.setSeconds(59);
            lambda.between(MeProductGroupInspectionItemsEntity::getExpiryDate, meProductGroupInspectionItemsDto.getExpiryDateStart(), expiryDateEnd);
        }

        final IPage<MeProductGroupInspectionItemsEntity> page = meProductGroupInspectionItemsService.page(meProductGroupInspectionItemsDto.getPage() == null ? new Page<>(0, 10) : meProductGroupInspectionItemsDto.getPage(), lambda);
        page.getRecords().forEach(x->{
            change(x);
        });
        return ResponseData.success(page);
    }

    public void change(MeProductGroupInspectionItemsEntity x) {
        final String itemGroupBo = x.getItemGroupBo();
        if (itemGroupBo.startsWith("IG:")) {
            x.setItemGroup(new ItemGroupHandleBO(itemGroupBo).getItemGroup());
        }
        final String operationBo = x.getOperationBo();
        if (operationBo.startsWith("OP:")) {
            x.setOperation(new OperationHandleBO(operationBo).getOperation());
        }
        final String unitBo = x.getUnitBo();
        if (unitBo.startsWith("UOM:")){
            x.setUnit(new UomHandleBO(unitBo).getUom());
        }
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public ResponseData info(@PathVariable("id") Integer id) {
        MeProductGroupInspectionItemsEntity x = meProductGroupInspectionItemsService.getById(id);
        change(x);
        return ResponseData.success(x);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public ResponseData save(@RequestBody MeProductGroupInspectionItemsEntity meProductGroupInspectionItems) {
        meProductGroupInspectionItems.setCreateDate(new Date());
        meProductGroupInspectionItems.setCreateUser(userUtil.getUser().getUserName());
        meProductGroupInspectionItems.setSite(UserUtils.getSite());
        meProductGroupInspectionItemsService.save(meProductGroupInspectionItems);

        return ResponseData.success();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public ResponseData update(@RequestBody MeProductGroupInspectionItemsEntity meProductGroupInspectionItems) {
        meProductGroupInspectionItemsService.updateById(meProductGroupInspectionItems);
        return ResponseData.success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody Integer[] ids) {
        meProductGroupInspectionItemsService.removeByIds(Arrays.asList(ids));
        return ResponseData.success();
    }

}
