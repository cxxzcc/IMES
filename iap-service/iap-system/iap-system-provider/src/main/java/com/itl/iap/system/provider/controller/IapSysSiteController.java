package com.itl.iap.system.provider.controller;


import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.provider.service.impl.IapSysSiteServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


@RestController
@RequestMapping("/site")
public class IapSysSiteController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IapSysSiteServiceImpl iapSysSiteService;

    @PostMapping("/changeSite")
    @ApiOperation(value = "切换工厂", notes = "切换工厂")
    public ResponseData changeSite(HttpSession session, HttpServletResponse response, @RequestParam(value = "siteId") String siteId) {
        iapSysSiteService.changeSite(session, response, siteId);
        return ResponseData.success(true);
    }

    @PostMapping("/changeStation")
    @ApiOperation(value = "切换工位", notes = "切换工位")
    public ResponseData changeStation(HttpSession session, HttpServletResponse response, @RequestParam(value = "stationId") String stationId) {
        if (null == stationId) {
            throw new RuntimeException("工位id为空");
        }
        Map s = iapSysSiteService.changeStation(session, response, stationId);
        return ResponseData.success(s);
    }

    @PostMapping("/confirmSchedule")
    @ApiOperation(value = "确认选择排程", notes = "确认选择排程")
    public ResponseData confirmSchedule(HttpSession session, HttpServletResponse response, @RequestParam(value = "scheduleNo") String scheduleNo) {
        iapSysSiteService.confirmSchedule(session, response, scheduleNo);
        return ResponseData.success(true);
    }
}
