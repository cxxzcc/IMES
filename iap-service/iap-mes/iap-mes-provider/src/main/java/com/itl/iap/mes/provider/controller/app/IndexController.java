package com.itl.iap.mes.provider.controller.app;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.IndexStatisticsDTO;
import com.itl.iap.mes.provider.service.impl.IndexStatisticsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app首页controller
 * @author dengou
 * @date 2021/9/30
 */
@RestController
@RequestMapping("/m/index")
@Api(tags = " 首页统计" )
public class IndexController {

    @Autowired
    private IndexStatisticsServiceImpl indexStatisticsService;

    /**
     * 角标数量统计
     * */
    @GetMapping("/indexStatistics")
    @ApiOperation(value="角标数量统计")
    public ResponseData<IndexStatisticsDTO> getIndexStatistics() {
        return ResponseData.success(indexStatisticsService.getStatisticsByUserAndSite(UserUtils.getUserId(), UserUtils.getUserName(), UserUtils.getSite()));
    }


}
