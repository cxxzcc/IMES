package com.itl.iap.common.base.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itl.iap.common.base.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author cjq
 * @Date 2021/10/18 11:10 上午
 * @Description TODO
 */
@Slf4j
public class CommonMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        Object createBy = this.getFieldValByName("createBy", metaObject);
        if (createBy == null) {
            this.setFieldValByName("createBy", UserUtils.getUserName(), metaObject);
        }
        this.setFieldValByName("createTime", now, metaObject);
        Object updateBy = this.getFieldValByName("updateBy", metaObject);
        if (updateBy == null) {
            this.setFieldValByName("updateBy", UserUtils.getUserName(), metaObject);
        }
        this.setFieldValByName("updateTime", now, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateBy = this.getFieldValByName("updateBy", metaObject);
        if (updateBy == null) {
            this.setFieldValByName("updateBy", UserUtils.getUserName(), metaObject);
        }
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
