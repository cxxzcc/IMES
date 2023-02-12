package com.itl.iap.system.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.system.api.dto.AdvancedQueryDto;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.system.provider.utils.QueryConditionUtil;
import com.itl.iap.system.api.service.AdvanceService;
import com.itl.iap.system.provider.mapper.AdvanceMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 崔翀赫
 * @date 2021/3/5$
 * @since JDK1.8
 */
@Service
@Slf4j
public class AdvanceServiceImpl implements AdvanceService {
    @Value("${mybatis-plus.configuration.database-id}")
    private String databaseId;
    @Autowired
    private AdvanceMapper advanceMapper;

    @SneakyThrows
    @Override
    public IPage<Map> queryAdvance(AdvancedQueryDto advancedQueryDtos) throws CommonException {
        if (advancedQueryDtos.getPage() == null) {
            advancedQueryDtos.setPage(new Page(0, 10));
        }
        Map<String, String> advance = null;
        if (databaseId.equals("oracle")) {
            Map<String, Clob> advanceOracle = advanceMapper.getAdvanceOracle(advancedQueryDtos.getId());
            advance = new HashMap<>();
            advance.put("FROM", ClobToString(advanceOracle.get("FROM")));
            advance.put("COLUMNS_DEFAULT", ClobToString(advanceOracle.get("COLUMNS_DEFAULT")));
        } else {
            advance = advanceMapper.getAdvance(advancedQueryDtos.getId());
        }
        String whereSql = QueryConditionUtil.getWhereSql(advancedQueryDtos);
        StringBuilder sb = new StringBuilder(whereSql);
        if (StrUtil.isNotBlank(advance.get("MAIN_TABLE"))) {
            sb.append(" AND ")
                    .append(advance.get("MAIN_TABLE"))
                    .append("='")
                    .append(UserUtils.getSite())
                    .append("'");
        }
        if (StrUtil.isNotBlank(advance.get("MUST_CONDITION"))) {
            sb.append(" AND ")
                    .append(advance.get("MUST_CONDITION"));
        }
        try {
            return advanceMapper.advanceQuery(advancedQueryDtos.getPage(), sb.toString(),
                    advance.get("FROM"), advance.get("COLUMNS_DEFAULT"));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new CommonException("条件构造异常,请联系yx!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    public String ClobToString(Clob clob) throws SQLException, IOException {
        String reString = "";
        Reader is = clob.getCharacterStream();// 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        return reString;
    }


    @Override
    public List<Map<String, String>> getColumn(String pageId) throws CommonException {
        final String tables = advanceMapper.getTables(pageId);

        if (StrUtil.isBlank(tables)) {
            throw new CommonException("数据库未配置需要查询的表", 500);
        }
        final List<String> strings = Arrays.asList(tables.split(","));
        List<Map<String, String>> list = advanceMapper.getColumn(strings);
        String columnLabel = "columnLabel";
        String columnName = "columnName";
        list.forEach(x -> {
            if (".".equals(x.get(columnLabel))) {
                x.put("columnLabel", x.get(columnName));
            } else {
                String[] split = x.get(columnLabel).split("\\.", -1);
                String[] splitName = x.get(columnName).split("\\.", -1);
                if ("".equals(split[0])) {
                    x.put("columnLabel", splitName[0] + "." + split[1]);
                } else if ("".equals(split[1])) {
                    x.put("columnLabel", split[0] + "." + splitName[1]);
                }
            }
        });

        return list;
    }

}
