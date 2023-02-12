package com.itl.mes.me.provider.controller;


import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.service.MeSnCrossStationLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 条码过站记录 前端控制器
 * </p>
 *
 * @author dengou
 * @since 2021-12-07
 */
@RestController
@RequestMapping("/snCrossStationLog")
public class MeSnCrossStationLogController {

    @Autowired
    private MeSnCrossStationLogService meSnCrossStationLogService;


    /**
     * 获取过站次数
     * */
    @GetMapping("/countBySn")
    @ApiOperation(value = "获取过站次数, feign接口")
    public ResponseData<Integer> getCountBySn(String sn, String site, String operationBo) {
        return ResponseData.success(meSnCrossStationLogService.getCountBySn(sn, site, operationBo));
    }

}

