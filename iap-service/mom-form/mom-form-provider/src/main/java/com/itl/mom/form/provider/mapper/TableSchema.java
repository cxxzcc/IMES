package com.itl.mom.form.provider.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 表结构
 * @author linjl
 * @since 2021-03-09
 * */
@Mapper
public interface TableSchema {

    @Select("select name from sysobjects where xtype='u' and name like'frm_%' and status>=0 ") //sqlserver
//    @Select("select * from information_schema.TABLES where TABLE_SCHEMA=(select database())") //mysql
    List<Map> listTable();

//    @Select("select * from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME=#{tableName}")  //mysql
    List<Map> listTableColumn(String tableName);
}

