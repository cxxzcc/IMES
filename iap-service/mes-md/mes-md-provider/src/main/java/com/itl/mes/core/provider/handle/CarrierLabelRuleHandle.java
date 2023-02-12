package com.itl.mes.core.provider.handle;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.api.service.CarrierService;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 崔翀赫
 * @date 2021/3/22$
 * @since JDK1.8
 */
@Component
public class CarrierLabelRuleHandle implements LabelRuleHandle {

    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private CarrierService carrierService;

    @Override
    public Map<String, Object> handle(ItemForParamQueryDto queryDto) {
        Map<String, Object> ret = new HashMap<>();
        List<String> columns = queryDto.getColumns();
        String elementBo = queryDto.getElementBo();
        //主字段
        Map<String, Object> map = BeanUtil.beanToMap(carrierService.getById(elementBo));
        columns.forEach(column -> {
            Object o = map.get(lineToHump(column.toLowerCase()));
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
        List<CustomDataAndValVo> customs = customDataValService.selectCustomDataAndValByBoAndDataType(site, elementBo, CustomDataTypeEnum.CARRIER.getDataType());
        customs.forEach(custom -> {
            if (columns.contains(custom.getAttribute())) {
                ret.put(custom.getAttribute(), custom.getVals());
            }
        });
        return ret;
    }
}
