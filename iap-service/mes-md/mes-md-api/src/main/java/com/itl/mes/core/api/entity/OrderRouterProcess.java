package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 工单工艺路线路线图
 * </p>
 *
 * @author chenjx1
 * @since 2021-10-26
 */
@TableName("m_order_router_process")
@Data
@ApiModel(value="OrderRouterProcess",description="工艺路线路线图")
public class OrderRouterProcess extends Model<OrderRouterProcess> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="SO:SITE,Router【PK】")
    @Length( max = 100 )
    @NotBlank
    @TableId(value = "ROUTER_BO", type = IdType.INPUT)
    private String routerBo;

    @ApiModelProperty(value="工厂【UK】")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="流程信息")
    @Length( max = 2048 )
    @NotBlank
    @TableField("PROCESS_INFO")
    private String processInfo;

    @Override
    protected Serializable pkVal() {
        return this.routerBo;
    }

    public OrderRouterProcess(){

    }

    public OrderRouterProcess(String routerBo){
        this.setRouterBo(routerBo);
    }
}
