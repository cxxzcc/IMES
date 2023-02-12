package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yx
 * @date 2021/8/17
 * @since 1.8
 */
@TableName("m_repair_check_file")
@Data
@ApiModel("设备点检附件")
public class CheckFile {

    @ApiModelProperty("设备点检附件id")
    @TableId(value = "ID", type = IdType.UUID)
    private String id;

    @ApiModelProperty("设备点检执行id")
    @TableField("CHECK_EXECUTE_ID")
    private String checkExecuteId;

    @ApiModelProperty("文件id")
    @TableField("FILE_ID")
    private String fileId;


}
