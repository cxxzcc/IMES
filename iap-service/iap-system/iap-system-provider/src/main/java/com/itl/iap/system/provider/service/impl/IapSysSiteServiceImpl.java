package com.itl.iap.system.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.system.provider.mapper.StationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IapSysSiteServiceImpl {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private StationMapper stationMapper;

    public void changeSite(HttpSession session, HttpServletResponse response, String siteId) {
        String uuid = UUID.randomUUID().toString();
        Cookie userCookie = new Cookie("onlySite", uuid);
        userCookie.setMaxAge(1 * 24 * 60 * 60);
        userCookie.setPath("/");
        response.addCookie(userCookie);
        redisTemplate.opsForValue().set("site-" + userUtil.getUser().getUserName() + ":" + uuid, siteId, 24, TimeUnit.HOURS);
    }

    public Map<String, Object> changeStation(HttpSession session, HttpServletResponse response, String stationId) {
        Map<String, Object> byStation = stationMapper.getByStation(stationId, UserUtils.getSite());
        String workShop = null;
        String productLine = null;
        if (byStation != null) {
            workShop = StrUtil.toString(byStation.get("WORK_SHOP"));
            productLine = StrUtil.toString(byStation.get("PRODUCT_LINE"));
            byStation.put("OPERATION", byStation.get("OPERATION"));
        }
        String uuid = UUID.randomUUID().toString();
        Cookie userCookie = new Cookie("onlyStation", uuid);
        userCookie.setMaxAge(1 * 24 * 60 * 60);
        userCookie.setPath("/");
        response.addCookie(userCookie);
        redisTemplate.opsForValue().set("station-" + userUtil.getUser().getUserName() + ":" + uuid, stationId, 24, TimeUnit.HOURS);
        redisTemplate.opsForValue().set("workShop-" + userUtil.getUser().getUserName() + ":" + uuid, workShop, 24, TimeUnit.HOURS);
        redisTemplate.opsForValue().set("productLine-" + userUtil.getUser().getUserName() + ":" + uuid, productLine, 24, TimeUnit.HOURS);
        return byStation;
    }

    public void confirmSchedule(HttpSession session, HttpServletResponse response, String scheduleNo) {
        String uuid = UUID.randomUUID().toString();
        Cookie userCookie = new Cookie("onlyScheduleNo", uuid);
        userCookie.setMaxAge(1 * 24 * 60 * 60);
        userCookie.setPath("/");
        response.addCookie(userCookie);
        redisTemplate.opsForValue().set("schedultNo-" + userUtil.getUser().getUserName() + ":" + uuid, scheduleNo, 24, TimeUnit.HOURS);
    }

}
