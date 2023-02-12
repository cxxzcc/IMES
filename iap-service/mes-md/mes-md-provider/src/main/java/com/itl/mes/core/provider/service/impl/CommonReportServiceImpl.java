package com.itl.mes.core.provider.service.impl;


import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.service.CommonReportService;
import com.itl.mes.core.provider.mapper.CommonReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

/**
 * 报表通用入口
 */
@Service
public class CommonReportServiceImpl implements CommonReportService {

    @Autowired
    private CommonReportMapper commonReportMapper;



    @Override
    public List<Map<String, Object>> selectManualSql( String sql, Map<String,Object> params ) {
        if( params!=null && !params.containsKey( "site" ) ){
            params.put( "site", UserUtils.getSite() );
        }
        return commonReportMapper.selectManualSql( sql, params );
    }

}
