package com.itl.mes.core.provider.handle;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.constant.BOPrefixEnum;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.api.entity.Packing;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.PackLevelService;
import com.itl.mes.core.api.service.PackingService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author yx
 * @date 2021/3/22
 * @since JDK1.8
 */
@Component
public class PackLabelRuleHandle implements LabelRuleHandle {

    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private PackingService packingService;

    @Autowired
    private PackLevelService packLevelService;

    @Override
    public Map<String, Object> handle(ItemForParamQueryDto queryDto) throws CommonException {
        Map<String, Object> ret = new HashMap<>();
        List<String> columns = queryDto.getColumns();
        String elementBo = queryDto.getElementBo();
        // 主字段
        Packing packing = packingService.getById(elementBo);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = BeanUtil.beanToMap(packing);

        StringJoiner seq = new StringJoiner(",");
        StringJoiner packLevel = new StringJoiner(",");
        StringJoiner minQty = new StringJoiner(",");
        StringJoiner maxQty = new StringJoiner(",");

        packLevelService.getPackLevels(elementBo).forEach(p -> {
            seq.add(p.getSeq());
            packLevel.add(p.getPackLevel());
            minQty.add(p.getMinQty().toString());
            maxQty.add(p.getMaxQty().toString());
        });

        String pkTable = BOPrefixEnum.PK.getTable() + ".";
        String pklTable = BOPrefixEnum.PKL.getTable() + ".";
        map1.keySet().stream().forEach(k -> map.put(pkTable + k, map1.get(k)));
        map.put(pklTable + "seq", seq);
        map.put(pklTable + "packLevel", packLevel);
        map.put(pklTable + "minQty", minQty);
        map.put(pklTable + "maxQty", maxQty);

        columns.forEach(column -> {
            Object o = map.get(pkTable + lineToHump(column.split("\\.")[1].toLowerCase()));
            if (ObjectUtil.isNotNull(o)) {
                ret.put(column, o);
            }
        });
        return ret;
    }

    @Override
    public Map<String, Object> handleCustom(ItemForParamQueryDto queryDto) throws CommonException {
        String site = queryDto.getSite();
        Map<String, Object> ret = new HashMap<>();
        List<String> columns = queryDto.getColumns();
        String elementBo = queryDto.getElementBo();

        //自定义字段
        List<CustomDataAndValVo> customs = customDataValService.selectCustomDataAndValByBoAndDataType(site, elementBo, CustomDataTypeEnum.PACKING.getDataType());
        customs.forEach(custom -> {
            if (columns.contains(custom.getAttribute())) {
                ret.put(custom.getAttribute(), custom.getVals());
            }
        });
        return ret;
    }
}
