package com.itl.iap.mes.api.entity.sparepart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 备件库存
 * @author dengou
 * @date 2021/9/17
 */
@TableName("m_spare_part_inventory")
@Data
public class SparePartInventory {

    @TableId(value = "ID", type = IdType.UUID)
    @ApiModelProperty(value="主键")
    private String id;
    /**
     * 备件id
     * */
    @TableField("SPARE_PART_ID")
    @ApiModelProperty(value="备件id")
    private String sparePartId;
    /**
     * 仓库id
     * */
    @TableField("WARE_HOUSE_ID")
    @ApiModelProperty(value="仓库id")
    private String wareHouseId;
    /**
     * 仓库名称
     * */
    @TableField("WARE_HOUSE_NAME")
    @ApiModelProperty(value="仓库名称")
    private String wareHouseName;
    /**
     * 库存
     * */
    @TableField("INVENTORY")
    @ApiModelProperty(value="库存")
    private Integer inventory;

}
