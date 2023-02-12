package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.mes.api.entity.label.LabelEntityParams;
import com.itl.mes.me.api.dto.ruleLabel.ItemColumns;
import com.itl.mes.me.api.dto.ruleLabel.RuleLabelQueryDto;
import com.itl.mes.me.api.dto.ruleLabel.RuleLabelSaveDto;
import com.itl.mes.me.api.dto.ruleLabel.RuleLabelShowDto;
import com.itl.mes.me.api.entity.RuleLabel;
import com.itl.mes.me.api.enums.FLAG;

import java.util.List;
import java.util.Map;


/**
 * 规则模板
 *
 * @author yx
 * @date 2021-01-21
 */
public interface RuleLabelService extends IService<RuleLabel> {

    /**
     * 保存
     * @param saveDto
     * @throws CommonException
     */
    void saveAndUpdate(RuleLabelSaveDto saveDto) throws CommonException;

    /**
     * 分页查询
     * @param queryDto
     * @return
     * @throws CommonException
     */
    IPage<RuleLabelShowDto> queryPage(RuleLabelQueryDto queryDto) throws CommonException;

    /**
     * 删除
     * @param bos
     * @throws CommonException
     */
    void delete(String[] bos) throws CommonException;

    /**
     * 根据flag获取字段及自定义字段
     * @param flag
     * @return
     */
    List<ItemColumns> getColumns(FLAG flag);
    List<ItemColumns> getColumns(FLAG flag, FLAG flag2);
    /**
     * 获取标签模板变量
     * @return
     */
    List<LabelEntityParams> getLabelEntityParams(String id);

    /**
     * 生成该规则对应编码
     * @param bo
     * @param elementBo
     * @param number
     * @return
     */
    List<String> generatorCode(String bo, String elementBo, Integer number, Map<String,Object> paramsMap) throws CommonException;

    /**
     * 根据Bo获取规则模板明细
     * @param bo
     * @return
     */
    RuleLabelShowDto getByBo(String bo);

}

