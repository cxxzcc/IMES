package com.itl.iap.common.base.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author cjq
 * @Description mapper封装 暂未测试
 * @Date 2021/9/12 9:52
 */
public interface MesBaseMapper<T> extends BaseMapper<T> {

    /**
     * 默认批次提交数量
     */
    int DEFAULT_BATCH_SIZE = 1000;

    /**
     * 更新数据 （支持TableId 复合主键）
     *
     * @param entity
     * @return
     */
    @Override
    default int updateById(T entity) {
        return this.update(entity, getQueryWrapper(entity));
    }

    default List<T> selectAll() {
        return this.selectList(Wrappers.emptyWrapper());
    }

    default int insertBatch(Collection<T> entityList) {
        return insertBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量插入
     *
     * @param entityList 数据集合
     * @param batchSize  批量大小
     * @return
     */
    default int insertBatch(Collection<T> entityList, int batchSize) {
        String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
        int size = entityList.size();
        return executeBatch(sqlSession -> {
            int insertCount = 0;
            for (T entity : entityList) {
                sqlSession.insert(sqlStatement, entity);
                insertCount++;
                if ((insertCount % batchSize == 0) || insertCount == size) {
                    sqlSession.flushStatements();
                }
            }
            return insertCount;
        });
    }

    /**
     * 存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return
     */
    default int insertOrUpdate(T entity) {
        if (null != entity) {
            QueryWrapper queryWrapper = getQueryWrapper(entity);
            return Objects.isNull(selectOne(queryWrapper)) ? insert(entity) : updateById(entity);
        }
        return 0;
    }

    default int insertOrUpdateBatch(Collection<T> entityList) {
        return insertOrUpdateBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量操作（支持多主键 TableID）
     *
     * @param entityList 数据集合
     * @param batchSize  数据大小
     * @return
     */
    default int insertOrUpdateBatch(Collection<T> entityList, int batchSize) {
        Assert.notEmpty(entityList, "error: entityList must not be empty");
        int size = entityList.size();
        return executeBatch(sqlSession -> {
            int insertOrUpdateCount = 0;
            for (T entity : entityList) {
                MapperMethod.ParamMap p = new MapperMethod.ParamMap<>();
                p.put(Constants.WRAPPER, getQueryWrapper(entity));
                Object o = sqlSession.selectOne(sqlStatement(SqlMethod.SELECT_ONE), p);
                if (Objects.isNull(o)) {
                    sqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), entity);
                    insertOrUpdateCount++;
                } else {
                    MapperMethod.ParamMap param = new MapperMethod.ParamMap<>();
                    param.put(Constants.ENTITY, entity);
                    param.put(Constants.WRAPPER, getQueryWrapper(entity));
                    sqlSession.update(sqlStatement(SqlMethod.UPDATE), param);
                    insertOrUpdateCount++;
                }
                if ((insertOrUpdateCount % batchSize == 0) || insertOrUpdateCount == size) {
                    sqlSession.flushStatements();
                }
            }
            return insertOrUpdateCount;
        });
    }


    default int updateBatchById(Collection<T> entityList) {
        return updateBatchById(entityList, DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量更新操作（支持多主键 TableID）
     *
     * @param entityList 数据集合
     * @param batchSize  数据大小
     * @return
     */
    default int updateBatchById(Collection<T> entityList, int batchSize) {
        Assert.notEmpty(entityList, "error: entityList must not be empty");
        String sqlStatement = sqlStatement(SqlMethod.UPDATE);
        int size = entityList.size();
        return executeBatch(sqlSession -> {
            int updateCount = 0;
            for (T entity : entityList) {
                MapperMethod.ParamMap param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, entity);
                param.put(Constants.WRAPPER, getQueryWrapper(entity));
                sqlSession.update(sqlStatement, param);
                updateCount++;
                if ((updateCount % batchSize == 0) || updateCount == size) {
                    sqlSession.flushStatements();
                }
            }
            return updateCount;
        });
    }


    /**
     * 获取 SqlStatement
     *
     * @param sqlMethod ignore
     * @return ignore
     */
    default String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }

    /**
     * 建议重写该方法，返回T——数据库表实体类的Class类型
     *
     * @return T.class
     */
    default Class<T> currentModelClass() {
        try {
            String baseMapperClassTypeName = this.getClass().getGenericInterfaces()[0].getTypeName();
            Class<? extends BaseMapper> baseMapperClass = (Class<? extends BaseMapper>) Class.forName(baseMapperClassTypeName);
            ParameterizedType parameterizedType = (ParameterizedType) baseMapperClass.getGenericInterfaces()[0];
            String entityClassName = parameterizedType.getActualTypeArguments()[0].getTypeName();
            return (Class<T>) Class.forName(entityClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("请重写该方法，返回T的Class类型", e);
        }
//        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

    /**
     * 执行批量操作
     *
     * @param fun 操作
     * @param <R> 泛型
     * @return
     */
    default <R> R executeBatch(Function<SqlSession, R> fun) {
        Class<T> tClass = currentModelClass();
//        SqlHelper.clearCache(tClass);
//        SqlSessionFactory sqlSessionFactory = SqlHelper.sqlSessionFactory(tClass);
        SqlSessionFactory sqlSessionFactory = GlobalConfigUtils.currentSessionFactory(tClass);
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            R apply = fun.apply(sqlSession);
            sqlSession.commit();
            return apply;
        } catch (Throwable t) {
            sqlSession.rollback();
            Throwable unwrapped = ExceptionUtil.unwrapThrowable(t);
            if (unwrapped instanceof RuntimeException) {
                MyBatisExceptionTranslator myBatisExceptionTranslator
                        = new MyBatisExceptionTranslator(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), true);
                throw Objects.requireNonNull(myBatisExceptionTranslator.translateExceptionIfPossible((RuntimeException) unwrapped));
            }
            throw ExceptionUtils.mpe(unwrapped);
        } finally {
            sqlSession.close();
        }
    }


    /**
     * 重写where 主键逻辑
     *
     * @param t
     * @return
     */
    default QueryWrapper getQueryWrapper(T t) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Class<T> tClass = currentModelClass();
        for (Field field : ReflectionKit.getFieldList(tClass)) {
            if (field.getAnnotation(TableId.class) != null) {
                field.setAccessible(true);
                String name = field.getName();
                try {
                    String tableFieldName = field.getAnnotation(TableId.class).value();
                    queryWrapper.eq(StrUtil.isNotBlank(tableFieldName) ? tableFieldName : name, field.get(t));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return queryWrapper;
    }


}
