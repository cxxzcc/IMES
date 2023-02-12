package com.itl.mes.core.api.vo;

import com.itl.mes.core.api.entity.BaseModelHasCus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 复制保存新工艺路线
 * </p>
 *
 * @author chenjx1
 * @since 2021-11-03
 */
@Data
@ApiModel(value="RouterAsSaveVo",description="复制保存新工艺路线")
public class RouterAsSaveVo extends BaseModelHasCus<RouterAsSaveVo>  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="SO:SITE,Router【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="新工艺路线编号【UK】")
    private String router;

    @ApiModelProperty(value="新工艺路线名称")
    private String routerName;

    @ApiModelProperty(value="新工艺路线描述")
    private String routerDesc;

    @ApiModelProperty(value="新版本")
    private String version;

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }
}
