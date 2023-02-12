package com.itl.iap.common.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DBUtils {

    private static final String PREFIX_LOG = "【自定义DB工具】";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public List<Map<String, Object>> excuteQuerySql(String sql) {
        log.info(PREFIX_LOG + "执行查询sql:" + sql);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        PreparedStatement pst = null;
        SqlSession session = getSqlSession();
        ResultSet result = null;
        try {
            pst = session.getConnection().prepareStatement(sql);
            result = pst.executeQuery();
            //获得结果集结构信息,元数据
            ResultSetMetaData md = result.getMetaData();
            //获得列数
            int columnCount = md.getColumnCount();
            while (result.next()) {
                Map<String, Object> rowData = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), result.getObject(i));
                }
                list.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeSqlSession(session);
        }
        return list;
    }

    /**
     * 获取sqlSession
     *
     * @return
     */
    public SqlSession getSqlSession() {
        SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
        return SqlSessionUtils.getSqlSession(sqlSessionFactory,
                sqlSessionTemplate.getExecutorType(), sqlSessionTemplate.getPersistenceExceptionTranslator());
    }

    /**
     * 关闭sqlSession
     *
     * @param session
     */
    public void closeSqlSession(SqlSession session) {
        SqlSessionUtils.closeSqlSession(session, sqlSessionTemplate.getSqlSessionFactory());
    }
}