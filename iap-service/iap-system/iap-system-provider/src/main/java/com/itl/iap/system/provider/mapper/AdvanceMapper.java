package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.sql.Clob;
import java.util.List;
import java.util.Map;

/**
 * @author 崔翀赫
 * @date 2021/3/5$
 * @since JDK1.8
 */
public interface AdvanceMapper {

    IPage<Map> advanceQuery(Page page, @Param("queryConditions") String queryConditions, @Param("tables") String tables, @Param("columns") String columns);

    Map<String, String> getAdvance(@Param("id") String id);

    Map<String, Clob> getAdvanceOracle(@Param("id") String id);

    String getTables(@Param("id") String id);

    List<Map<String, String>> getColumn(@Param("list") List list);
}
