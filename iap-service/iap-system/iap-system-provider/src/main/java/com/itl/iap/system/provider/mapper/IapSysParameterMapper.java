package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapDictTDto;
import com.itl.iap.system.api.entity.IapDictT;
import com.itl.iap.system.api.entity.IapSysParameter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典表主表Mapper
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
public interface IapSysParameterMapper extends BaseMapper<IapSysParameter> {


    IPage<IapSysParameter> pageList(Page page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
