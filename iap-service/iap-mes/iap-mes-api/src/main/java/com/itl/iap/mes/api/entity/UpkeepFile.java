package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author yx
 * @date 2021/8/17
 * @since 1.8
 */
@TableName("m_repair_upkeep_file")
@Data
@ApiModel("设备保养附件")
public class UpkeepFile {

    @ApiModelProperty("设备保养附件id")
    @TableId(value = "ID", type = IdType.UUID)
    private String id;

    @ApiModelProperty("设备保养执行id")
    @TableField("UPKEEP_EXECUTE_ID")
    private String upkeepExecuteId;

    @ApiModelProperty("文件id")
    @TableField("FILE_ID")
    private String fileId;


}
