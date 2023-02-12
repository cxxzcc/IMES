package com.itl.mom.label.api.entity.label;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.mom.label.api.entity.file.MesFiles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


/**
 * @author liuchenghao
 * @date 2020/10/29 17:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("label_label")
public class LabelEntity {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField( "LABEL_TYPE_ID")
    private String labelTypeId;


    @TableField( "SITE")
    private String site;

    @TableField( "USE_DATA_SOURCE")
    private Integer useDataSource;

    @TableField( "LABEL")
    private String label;


    @TableField( "LABEL_DESCRIPTION")
    private String labelDescription;


    @TableField( "STATE")
    private String state;
    @TableField( "CREATION_DATE")
    private Date creationDate;
    @TableField( "LAST_UPDATE_DATE")
    private Date lastUpdateDate;

    @TableField("TEMPLATE_TYPE")
    private String templateType;
    @TableField("LODOP_TEXT")
    private String lodopText;

    @TableField(exist = false)
    private MesFiles jasperFile;

    @TableField(exist = false)
    private List<LabelEntityParams> labelEntityParamsList;

}
