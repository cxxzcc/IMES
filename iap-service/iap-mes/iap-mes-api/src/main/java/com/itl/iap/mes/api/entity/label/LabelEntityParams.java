package com.itl.iap.mes.api.entity.label;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("label_params")
public class LabelEntityParams {
    @TableId(type = IdType.UUID)
    private String id;

    @TableField( "CODE")
    private String code;

    @TableField( "NAME")
    private String name;

    @TableField( "IS_FILE")
    private Integer isFile;

    @TableField( "LABEL_ID")
    private String  labelId;
}
