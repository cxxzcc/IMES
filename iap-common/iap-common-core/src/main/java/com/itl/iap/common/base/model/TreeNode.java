package com.itl.iap.common.base.model;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.itl.iap.common.base.constants.CommonConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 树节点
 * @author dengou
 * @date 2021/11/3
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class TreeNode<T extends TreeNode> {

    /**
     * 子节点
     * */
    @TableField(exist = false)
    @ApiModelProperty("子节点")
    private List<T> childList;
    /**
     * 是否叶子节点
     * */
    @TableField(exist = false)
    @ApiModelProperty("是否叶子节点")
    private String isLeafFlag;
    /**
     * 当前节点id （唯一值）
     * */
    public abstract String getId();

    /**
     * 父节点id
     * */
    public abstract String getParentId();

    public String getIsLeafFlag() {
        if(CollUtil.isEmpty(childList)) {
            return CommonConstants.FLAG_Y;
        }
        return CommonConstants.FLAG_N;
    }
}
