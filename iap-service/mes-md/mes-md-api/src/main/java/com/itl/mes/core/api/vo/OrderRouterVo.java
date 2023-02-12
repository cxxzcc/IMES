package com.itl.mes.core.api.vo;

import com.itl.mes.core.api.entity.BaseModelHasCus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工艺路线-工单副本
 * </p>
 *
 * @author chenjx1
 * @since 2021-10-26
 */
@Data
@ApiModel(value="OrderRouterVo",description="工单工艺路线")
public class OrderRouterVo extends BaseModelHasCus<OrderRouterVo>  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="SO:SITE,Router【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="工艺路线编号【UK】")
    private String router;

    @ApiModelProperty(value="工艺路线类型")
    private String routerType;

    @ApiModelProperty(value="工艺路线名称")
    private String routerName;

    @ApiModelProperty(value="工艺路线描述")
    private String routerDesc;

    @ApiModelProperty(value="状态")
    private String state;

    @ApiModelProperty(value="版本")
    private String version;

    @ApiModelProperty(value="物料BO")
    private String itemBo;

    @ApiModelProperty(value="工单BO")
    private String shopOrderBo;

    @ApiModelProperty(value="流程信息")
    private String processInfo;

    @ApiModelProperty(value="snBo")
    private List<String> snBo;

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }
}
