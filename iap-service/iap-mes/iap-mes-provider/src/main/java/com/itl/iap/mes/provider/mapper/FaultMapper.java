package com.itl.iap.mes.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.FaultDto;
import com.itl.iap.mes.api.entity.Fault;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FaultMapper extends BaseMapper<Fault> {

    IPage<Fault> pageQuery(Page page, @Param("faultDto") FaultDto faultDto);
    IPage<Fault> pageQueryByState(Page page, @Param("faultDto") FaultDto faultDto);

    List<Map<String, String>> queryCountMapByErrorTypeId(@Param("ids") List<String> ids, @Param("site") String site);
}
