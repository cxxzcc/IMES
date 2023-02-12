package com.itl.plugins.report.config;

import com.bstek.ureport.definition.datasource.BuildinDatasource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author cjq
 * @Date 2021/11/28 9:18 下午
 * @Description TODO
 */
@Slf4j
@Component
public class UreportDataSource implements BuildinDatasource {
    private static final String NAME = "内置数据源";

    @Resource
    private DataSource dataSource;

    /**
     * 数据源名称
     **/
    @Override
    public String name() {
        return NAME;
    }

    /**
     * 获取连接
     **/
    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("Ureport 数据源 获取连接失败！");
            e.printStackTrace();
        }
        return null;
    }

}
