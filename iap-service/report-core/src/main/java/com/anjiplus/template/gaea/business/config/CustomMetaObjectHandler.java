package com.anjiplus.template.gaea.business.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itl.iap.common.base.utils.UserUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author yx
 * @date 2021/8/5
 * @since 1.8
 */
public class CustomMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
//        String username = UserContentHolder.getContext().getUsername();
        String username = UserUtils.getCurrentUser().getUserName();

        Object createBy = this.getFieldValByName("createBy", metaObject);
        if (createBy == null) {
            this.setFieldValByName("createBy", username,metaObject);
        }

        this.setFieldValByName("createTime", new Date(),metaObject);

        Object updateBy = this.getFieldValByName("updateBy", metaObject);
        if (updateBy == null) {
            this.setFieldValByName("updateBy", username,metaObject);
        }

        this.setFieldValByName("updateTime", new Date(),metaObject);


        this.setFieldValByName("version", Integer.valueOf(1),metaObject);

        this.setFieldValByName("id", System.currentTimeMillis(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        String username = UserContentHolder.getContext().getUsername();
        String username = UserUtils.getCurrentUser().getUserName();

        this.setFieldValByName("updateBy", username,metaObject);
        this.setFieldValByName("updateTime", new Date(),metaObject);
        this.setFieldValByName("version", this.getFieldValByName("version",metaObject),metaObject);
    }
}
