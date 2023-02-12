package com.itl.mes.pp.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.pp.api.dto.scheduleplan.ClassSystemFrequencyMergeDTO;
import com.itl.mes.pp.api.entity.scheduleplan.ClassFrequencyEntity;
import org.apache.ibatis.annotations.Param;

public interface ClassFrequencyMapper extends BaseMapper<ClassFrequencyEntity> {

    IPage<ClassSystemFrequencyMergeDTO> findMergeList(Page page, @Param("dto") ClassSystemFrequencyMergeDTO classSystemFrequencyMergeDTO);

}
