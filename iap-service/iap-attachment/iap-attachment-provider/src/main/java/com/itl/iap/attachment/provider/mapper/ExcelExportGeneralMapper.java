package com.itl.iap.attachment.provider.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.attachment.api.entity.ExcelExportGeneral;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * description:
 * author: lK
 * time: 2021/7/6 10:56
 */
@Repository
@SqlParser(filter = true)
public interface ExcelExportGeneralMapper extends BaseMapper<ExcelExportGeneral> {

    /**
     * @param s 整条sql
     * @return 要出的数据
     */
//    List<Map<String, Object>> exportExcelGeneral(@Param("sql") String s);

}
