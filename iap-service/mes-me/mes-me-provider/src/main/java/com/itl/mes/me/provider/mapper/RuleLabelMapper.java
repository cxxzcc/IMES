package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.label.LabelEntityParams;
import com.itl.mes.me.api.dto.ruleLabel.ItemColumns;
import com.itl.mes.me.api.dto.ruleLabel.RuleLabelQueryDto;
import com.itl.mes.me.api.dto.ruleLabel.RuleLabelShowDto;
import com.itl.mes.me.api.entity.RuleLabel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 规则模板
 *
 * @author yx
 * @date 2021-01-21
 */
@Mapper
public interface RuleLabelMapper extends BaseMapper<RuleLabel> {

    /**
     * 获取自定义字段名称
     *
     * @param site
     * @param customDataTypes
     * @return
     */
    List<String> getCustoms(@Param("site") String site, @Param("list") List<String> customDataTypes);

    /**
     * 分页查询
     *
     * @param page
     * @param queryDto
     * @return
     */
    IPage<RuleLabelShowDto> queryList(Page page, @Param("queryDto") RuleLabelQueryDto queryDto);

    /**
     * 获取表字段及自定义字段
     *
     * @param site
     * @param tableName
     * @param customDataType
     * @return
     */
    List<ItemColumns> getColumns(@Param("site") String site, @Param("tableName") String tableName, @Param("customDataType") String customDataType);
    List<ItemColumns> getColumnsP(@Param("site") String site, @Param("customDataType") String customDataType);

    /**
     * 获取标签模板变量
     *
     * @return
     */
    List<LabelEntityParams> getLabelEntityParams(@Param("id") String id);

    /**
     * 根据Bo获取规则模板明细
     *
     * @param bo
     * @return
     */
    RuleLabelShowDto getByBo(@Param("bo") String bo);


}
