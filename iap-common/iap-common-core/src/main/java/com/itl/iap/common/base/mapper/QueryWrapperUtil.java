package com.itl.iap.common.base.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author cjq
 * @Date 2021/10/15 9:38 上午
 * @Description TODO
 */
public class QueryWrapperUtil {

    public static QueryWrapper convertQuery(Object obj) {
        return convertQuery(null, obj);
    }

    /**
     * 拼接查询条件
     *
     * @param queryWrapper 条件对象
     * @param obj          数据实体
     * @return void 返回参数说明
     * @exception/throws
     */
    public static QueryWrapper convertQuery(QueryWrapper queryWrapper, Object obj) {
        if (queryWrapper == null) {
            queryWrapper = new QueryWrapper();
        }
        queryWrapper.apply(" 1=1 ");
        Class clazz = obj.getClass();
        try {
            // 反射遍历属性
            for (Field field : clazz.getDeclaredFields()) {
                // 获取属性名
                String fieldName = field.getName();
                // 抑制Java对修饰符的检查
                field.setAccessible(true);
                // 获取属性值
                Object fieldValue = field.get(obj);
                if (fieldValue == null) {
                    continue;
                }
                // 查询注解
                QueryWapper queryWapperAnnotation = AnnotationUtils.getAnnotation(field, QueryWapper.class);
                if (ObjectUtils.isEmpty(queryWapperAnnotation)) {
                    continue;
                }
                String annotationField = queryWapperAnnotation.value();
                if (StrUtil.isNotBlank(annotationField)) {
                    fieldName = annotationField;
                }
                // 获取枚举
                QueryWapperEnum queryWapperEnum = queryWapperAnnotation.queryWapperEnum();
                // 拼接查询条件
                switch (queryWapperEnum) {
                    case EQ:
                        queryWrapper.eq(!ObjectUtils.isEmpty(fieldValue), fieldName, fieldValue);
                        break;
                    case LIKE:
                        queryWrapper.like(!ObjectUtils.isEmpty(fieldValue), fieldName, fieldValue);
                        break;
                    case MATCH:
                        if (fieldValue instanceof Number || fieldValue instanceof String) {
                            String value = fieldValue + "";
                            if (value.contains(",")) {
                                String[] split = StrUtil.split(value, ",");
                                List<String> collect = Arrays.stream(split).filter(v -> StrUtil.isNotBlank(v)).collect(Collectors.toList());
                                queryWrapper.in(!ObjectUtils.isEmpty(collect), fieldName, collect);
                            } else if (value.contains("*")) {
                                value = value.replace("*", "%");
                                queryWrapper.apply(fieldName + " like {0}", value);
                            } else {
                                queryWrapper.eq(!ObjectUtils.isEmpty(fieldValue), fieldName, fieldValue);
                            }
                        }
                        break;
                    case IN:
                        if (fieldValue instanceof List) {
                            List fields = (List) fieldValue;
                            if (CollectionUtil.isNotEmpty(fields)) {
                                queryWrapper.in(CollectionUtil.isNotEmpty(fields), fieldName, fields);
                            }
                        }
                        break;
                    case APPLY:
                        String sql = queryWapperAnnotation.whereSql();
                        if (StrUtil.isNotBlank(sql) && !ObjectUtils.isEmpty(fieldValue)) {
                            queryWrapper.apply(sql, fieldValue);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return queryWrapper;
    }

    public static void getMatch(QueryWrapper queryWrapper, boolean bool, String fieldName, Object fieldValue) {
        if (bool) {
            if (fieldValue instanceof Number || fieldValue instanceof String) {
                String value = fieldValue + "";
                if (value.contains(",")) {
                    String[] split = StrUtil.split(value, ",");
                    List<String> collect = Arrays.stream(split).filter(v -> StrUtil.isNotBlank(v)).collect(Collectors.toList());
                    queryWrapper.in(!ObjectUtils.isEmpty(collect), fieldName, collect);
                } else if (value.contains("*")) {
                    value = value.replace("*", "%");
                    queryWrapper.apply(fieldName + " like {0}", value);
                } else {
                    queryWrapper.eq(!ObjectUtils.isEmpty(fieldValue), fieldName, fieldValue);
                }
            }
        }
    }
}
