package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.ErrorTypeDto;
import com.itl.iap.mes.api.entity.ErrorType;
import com.itl.iap.mes.api.service.ErrorTypeService;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.ErrorTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yx
 * @date 2021/8/19
 * @since 1.8
 */
@Service
public class ErrorTypeServiceImpl extends ServiceImpl<ErrorTypeMapper, ErrorType> implements ErrorTypeService {

    private ErrorTypeMapper errorTypeMapper;

    /**
     * 异常代码
     */
    @Autowired
    private FaultServiceImpl faultService;

    @Resource
    public void setErrorTypeMapper(ErrorTypeMapper errorTypeMapper) {
        this.errorTypeMapper = errorTypeMapper;
    }

    @Override
    public List<ErrorTypeDto> listTree(ErrorTypeDto errorTypeDto) {
        errorTypeDto.setSite(UserUtils.getSite());
        List<ErrorTypeDto> ret = errorTypeMapper.pageList(errorTypeDto);
        if (ret != null && ret.size() > 0) {
            List<ErrorType> list = this.list(new QueryWrapper<ErrorType>().lambda().eq(ErrorType::getSite, errorTypeDto.getSite()).isNotNull(ErrorType::getParentId));
            if (list != null && list.size() > 0) {
                Map<String, List<ErrorType>> groupByIds = list.stream().collect(Collectors.groupingBy(ErrorType::getParentId));
                Set<String> idSet = this.recursiveList(ret, groupByIds);
                // 树形最外层去重
                return ret.stream().filter(x -> !idSet.contains(x.getId())).collect(Collectors.toList());
            }
        }
        return ret;
    }

    @Override
    public void delete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        Map<String, Integer> map = faultService.countByErrorTypeIds(ids);
        for(String id : ids){
            Integer count = map.get(id);
            if(count != null && count > 0){
                throw new CustomException(CommonCode.FAIL);
            }
        }

        List<ErrorType> list = this.list(new QueryWrapper<ErrorType>().lambda().in(ErrorType::getParentId, ids));
        if (list != null && list.size() > 0) {
            this.delete(list.stream().map(ErrorType::getId).collect(Collectors.toList()));
        }
        this.removeByIds(ids);
    }

    /**
     * 递归, 返回idSet供树形结构一层去重去重
     * @param list
     * @param groupByIds
     * @return
     */
    private Set<String> recursiveList(List<ErrorTypeDto> list, Map<String, List<ErrorType>> groupByIds) {
        Set<String> idSet = new HashSet<>();
        for (ErrorTypeDto dto : list) {
            List<ErrorType> types = groupByIds.get(dto.getId());
            if (types != null && types.size() > 0) {
                dto.setChildren(types.stream().map(x -> {
                    ErrorTypeDto child = new ErrorTypeDto();
                    child.setId(x.getId());
                    idSet.add(x.getId());
                    child.setErrorName(x.getErrorName());
                    child.setErrorCode(x.getErrorCode());
                    child.setErrorDesc(x.getErrorDesc());
                    child.setParentId(x.getParentId());
                    child.setSite(x.getSite());
                    return child;
                }).collect(Collectors.toList()));
                idSet.addAll(this.recursiveList(dto.getChildren(), groupByIds));
            }
        }
        return idSet;
    }
}
