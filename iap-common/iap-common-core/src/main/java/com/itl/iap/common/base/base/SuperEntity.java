package com.itl.iap.common.base.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用实体父类， uuid主键，带site（工厂）字段
 * @author dengou
 * @date 2021/11/22
 */
@Data
public class SuperEntity<T extends SuperEntity> extends Model<T> {

    /**
     * id
     * */
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "主键id")
    private String id;

    /**
     * 工厂
     * */
    @TableField("site")
    @ApiModelProperty(value = "工厂id")
    private String site;


}
