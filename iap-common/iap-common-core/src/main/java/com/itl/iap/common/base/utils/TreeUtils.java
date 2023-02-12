package com.itl.iap.common.base.utils;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.model.TreeNode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 树 工具类
 * @author dengou
 * @date 2021/11/3
 */
public class TreeUtils {


    /**
     * 将平铺list转成树结构list， 顶级节点的parentId必须为空
     * @param list 平铺结构
     * @return 树结构
     * */
    public static <T extends TreeNode> List<T> buildTree(List<T> list) {
        if(CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        //将parentId不为空的节点 按 parentId 分组
        Map<String, List<T>> mapByParentId = list.stream().filter(e -> StrUtil.isNotBlank(e.getParentId())).collect(Collectors.groupingBy(e -> e.getParentId()));
        list.forEach(e -> {
            String id = e.getId();
            if(StrUtil.isNotBlank(id) && mapByParentId.containsKey(id)) {
                e.setChildList(mapByParentId.get(id));
            }
        });
        return list.stream().filter(e -> StrUtil.isBlank(e.getParentId())).collect(Collectors.toList());
    }

}
