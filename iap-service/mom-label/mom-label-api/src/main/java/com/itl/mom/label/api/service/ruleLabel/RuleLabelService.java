package com.itl.mom.label.api.service.ruleLabel;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mom.label.api.dto.ruleLabel.ItemColumns;
import com.itl.mom.label.api.dto.ruleLabel.RuleLabelQueryDto;
import com.itl.mom.label.api.dto.ruleLabel.RuleLabelSaveDto;
import com.itl.mom.label.api.dto.ruleLabel.RuleLabelShowDto;
import com.itl.mom.label.api.entity.label.LabelEntityParams;
import com.itl.mom.label.api.entity.ruleLabel.RuleLabel;
import com.itl.mom.label.api.entity.ruleLabel.RuleLabelDetail;
import com.itl.mom.label.api.enums.FLAG;

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
    @SqlParser(filter = true)
    List<ItemColumns> getColumns(FLAG flag);
    @SqlParser(filter = true)
    List<ItemColumns> getColumns(FLAG flag, FLAG flag2);
    /**
     * 获取标签模板变量
     * @return
     */
    List<LabelEntityParams> getLabelEntityParams(String id);

    /**
     * 生成该规则对应编码
     * @param ruleLabel
     * @param list
     * @param elementBo
     * @param number
     * @return
     */
    List<String> generatorCode(RuleLabel ruleLabel, List<RuleLabelDetail> list, String elementBo, Integer number, Map<String,Object> paramsMap) throws CommonException;

    /**
     * 根据Bo获取规则模板明细
     * @param bo
     * @return
     */
    RuleLabelShowDto getByBo(String bo);

    /**
     * 根据code获取规则模板明细
     * @param ruleLabel
     * @param elementType
     * @return
     */
    RuleLabelShowDto getByCode(String ruleLabel, String elementType);

    Map<String, Object> getLabelParams(RuleLabel ruleLabel, List<RuleLabelDetail> list, String site, String elementBo);
}

