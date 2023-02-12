/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.itl.iap.common.base.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.exception.BasicCommonExceptionDefinition;
import com.itl.iap.common.base.exception.CommonException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;

/**
 * @author lengleng
 * @date 2017/11/20
 * 用户相关工具类
 */

@Component
public class UserUtils {
    private static final ThreadLocal<String> THREAD_LOCAL_USER = new TransmittableThreadLocal<>();
    private static final ThreadLocal<String> THREAD_LOCAL_SITE = new TransmittableThreadLocal<>();
    private static final String KEY_USER = "onlySite";
    private static final String KEY_USER_STATION = "onlyStation";
    private static final String SITE = "1000";
    private static final String HEADER_TITLE = "Bearer ";

    private static RedisTemplate redisTemplate;

    @Resource
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        UserUtils.redisTemplate = redisTemplate;
    }

    public static UserTDto getCurrentUser() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(authorization)) {
            String token = authorization.substring(HEADER_TITLE.length(), authorization.length());

            UserTDto username = JWTUtil.getUsername(token);
            Object o = redisTemplate.opsForValue().get(username.getUserName() + "-user");
            //   Object o = redisTemplate.opsForValue().get("user-"+username.getUserName());
            if (o == null) {
                return null;
            }
            return JSONObject.parseObject(o.toString(), UserTDto.class);
        }

        return null;
    }

    /**
     * 根据请求heard中的token获取用户角色
     *
     * @return 角色名
     */
    public static List<String> getRole(String token) {
        String key = Base64.getEncoder().encodeToString("IAP".getBytes());
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        List<String> roleNames = (List<String>) claims.get("authorities");
        return roleNames;
    }

    /**
     * 根据header中的token获取用户ID
     *
     * @return 用户ID
     */
    public static String getUserId() {

        UserTDto dto = getCurrentUser();
        if (dto != null) {
            return dto.getId();
        }
        return null;
//        String key = Base64.getEncoder().encodeToString("IAP".getBytes());
//        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
//        Integer userId = (Integer) claims.get("userId");
//        return userId;
    }

    /**
     * 根据HttpServletRequest中header中的token获取用户名
     *
     * @return 用户名
     */
    public static String getUserName() {
        UserTDto dto = getCurrentUser();
        if (dto != null) {
            return dto.getUserName();
        }
        return null;

//        String token = httpServletRequest.getHeader( "token" );
//        String key = Base64.getEncoder().encodeToString( "IAP".getBytes() );
//        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws( token ).getBody();
//        return (String) claims.get("user_name");
    }

    /**
     * 获取当前用户姓名
     *
     * @return 用户姓名
     */
    public static String getRealName() {
        UserTDto currentUser = getCurrentUser();
        return currentUser == null ? null : currentUser.getRealName();
    }

    /**
     * 获取请求中token
     *
     * @param httpServletRequest request
     * @return token
     */
   /* public static String getToken(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        return StringUtils.substringAfter(authorization,"Bearer");
    }*/

    /**
     * 设置用户信息
     *
     * @param username 用户名
     */
    public static void setUser(String username) {
        THREAD_LOCAL_USER.set(username);
    }

    /**
     * 设置用户当前站点
     *
     * @param site
     */
    public static void setSite(String site) {
        THREAD_LOCAL_SITE.set(site);
    }

    /**
     * 从threadlocal 获取用户名
     *
     * @return 用户名
     */

    public static String getUser() {
        return THREAD_LOCAL_USER.get();

    }

    /**
     * 获取用户站点
     *
     * @return
     */
    public static String getSite() {
        UserTDto currentUser = getCurrentUser();
        if (null == currentUser) {
            throw new RuntimeException("获取当前用户信息失败");
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Cookie[] cookies = request.getCookies();
        Assert.valid((cookies == null || cookies.length == 0), BasicCommonExceptionDefinition.NON_COOKIE);

        String uuid = null;
        for (Cookie cookie : cookies) {
            if (cookie != null && StrUtil.equals(cookie.getName(), KEY_USER)) {
                uuid = cookie.getValue();
            }
        }

        Assert.valid(StringUtils.isBlank(uuid), BasicCommonExceptionDefinition.NON_COOKIE);

        Object o = redisTemplate.opsForValue().get("site-" + currentUser.getUserName() + ":" + uuid);
        if (o == null) {
            throw new CommonException(BasicCommonExceptionDefinition.SITE_EXPIRY);
        }
        return o.toString();

//        return "1040";
    }

    public static String getStation() {
        UserTDto currentUser = getCurrentUser();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Cookie[] cookies = request.getCookies();
        String uuid = null;
        Assert.valid((cookies == null || cookies.length == 0), BasicCommonExceptionDefinition.NON_COOKIE);
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(KEY_USER_STATION)) {
                uuid = cookie.getValue();
            }
        }
        Assert.valid(StringUtils.isBlank(uuid), BasicCommonExceptionDefinition.NON_COOKIE);
        Object o = redisTemplate.opsForValue().get("station-" + currentUser.getUserName() + ":" + uuid);
        if (o == null) {
            throw new CommonException(BasicCommonExceptionDefinition.STATION_EXPIRY);
        }
        return o.toString();
    }

    public static String getWorkShop() {
        UserTDto currentUser = getCurrentUser();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Cookie[] cookies = request.getCookies();
        String uuid = null;
        Assert.valid((cookies == null || cookies.length == 0), BasicCommonExceptionDefinition.NON_COOKIE);
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(KEY_USER_STATION)) {
                uuid = cookie.getValue();
            }
        }
        Assert.valid(StringUtils.isBlank(uuid), BasicCommonExceptionDefinition.NON_COOKIE);
        Object o = redisTemplate.opsForValue().get("workShop-" + currentUser.getUserName() + ":" + uuid);
        if (o == null) {
            throw new CommonException(BasicCommonExceptionDefinition.WORK_SHOP_EXPIRY);
        }
        return o.toString();
    }

    public static String getProductLine() {
        UserTDto currentUser = getCurrentUser();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Cookie[] cookies = request.getCookies();
        String uuid = null;
        Assert.valid((cookies == null || cookies.length == 0), BasicCommonExceptionDefinition.NON_COOKIE);
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(KEY_USER_STATION)) {
                uuid = cookie.getValue();
            }
        }
        Assert.valid(StringUtils.isBlank(uuid), BasicCommonExceptionDefinition.NON_COOKIE);
        Object o = redisTemplate.opsForValue().get("productLine-" + currentUser.getUserName() + ":" + uuid);
        if (o == null) {
            throw new CommonException(BasicCommonExceptionDefinition.PRODUCT_LINE_EXPIRY);
        }
        return o.toString();
    }

    public static void clearAllUserInfo() {
        THREAD_LOCAL_USER.remove();
    }

    /**
     * 清除工厂信息
     */
    public static void clearSiteInfo() {
        THREAD_LOCAL_SITE.remove();
    }
}
