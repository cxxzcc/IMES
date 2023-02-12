package com.itl.mom.label.api.dto.label;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "物料标签查询对象")
public class ItemLabelQueryDTO {

    //分页对象
    private Page page;

    @ApiModelProperty(value = "物料标签编码")
    @QueryWapper(value = "b.sn", queryWapperEnum = QueryWapperEnum.MATCH)
    private String sn;

    @ApiModelProperty(value = "物料编码")
    @QueryWapper(value = "c.item", queryWapperEnum = QueryWapperEnum.MATCH)
    private String item;

    @ApiModelProperty(value = "物料名称")
    @QueryWapper(value = "c.item_name", queryWapperEnum = QueryWapperEnum.MATCH)
    private String itemName;

    @ApiModelProperty(value = "追溯方式")
    @QueryWapper("c.zs_type")
    private String zsType;

    @ApiModelProperty(value = "标签状态")
    @QueryWapper("b.state")
    private String state;

    @ApiModelProperty(value = "工单编码")
    @QueryWapper(value = "a.shop_order_bo", queryWapperEnum = QueryWapperEnum.MATCH)
    private String shopOrderBo;

    @ApiModelProperty(value = "批次")
    @QueryWapper(value = "a.pc", queryWapperEnum = QueryWapperEnum.MATCH)
    private String pc;

    @ApiModelProperty(value = "是否挂起")
    @QueryWapper(value = "a.sfgq")
    private String sfgq;

    @ApiModelProperty(value = "工厂编号")
    @QueryWapper(value = "a.site")
    private String site;

}
