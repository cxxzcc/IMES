package com.itl.iap.attachment.client.service;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.attachment.client.config.FallBackConfig;
import com.itl.iap.attachment.client.service.impl.UserServiceImpl;
import com.itl.iap.system.api.entity.IapSysUserT;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务远程调用
 *
 * @author 汤俊
 * @date 2020-6-30 10:48
 * @since jdk 1.8
 */
@FeignClient(value = "iap-system-provider", fallbackFactory = UserServiceImpl.class, configuration = FallBackConfig.class)
public interface UserService {

    @GetMapping("/iapSysUserT/queryByName/{userName}")
    @ApiOperation(value = "用户查询", notes = "根据名称查询用户信息")
    ResponseData<IapSysUserTDto> queryByName(@PathVariable("userName") String userName);

    @GetMapping("/iapSysUserT/getUser")
    @ApiOperation(value = "用户查询", notes = "根据名称查询用户信息")
    ResponseData<IapSysUserT> getUser(@RequestParam String userName);

    @PostMapping("/iapSysUserT/getUserList")
    @ApiOperation(value = "根据用户名查询用户信息", notes = "根据用户名查询用户信息")
    ResponseData<List<IapSysUserT>> getUserList(@RequestBody List<String> userNameList);

    @PostMapping("/iapSysUserT/userList")
    @ApiOperation(value = "获取所有用户列表",notes = "获取所有用户列表")
    ResponseData<List<IapSysUserTDto>> getUserList();

    @PostMapping("/iapSysUserT/userListByIds")
    @ApiOperation(value = "根据ids获取用户列表",notes = "根据ids获取用户列表")
    ResponseData<List<IapSysUserTDto>> getUserListByIds(@RequestBody List<String> ids);

    @PostMapping("/iapSysUserT/getUserInfByName/{username}")
    @ApiOperation(value = "根据用户名称获取所有用户列表",notes = "获取所有用户列表")
    ResponseData<IapSysUserTDto> getUserInfByName(@PathVariable("username") String username);

    @PostMapping("/iapSysUserT/getUserByGroupId/{groupId}")
    @ApiOperation(value = "通过聊天群ID查找群成员",notes = "通过聊天群ID查找群成员")
    ResponseData<List<IapSysUserTDto>> getUserByGroupId(@PathVariable("groupId") String groupId);

    @GetMapping("/iapSysUserT/queryUserInfoByUserName")
    @ApiOperation(value = "根据用户名称模糊查询用户信息",notes = "根据用户名称模糊查询用户信息")
    ResponseData<List<IapSysUserTDto>> queryUserInfoByUserName(@RequestParam(value = "username",required = false) String username);

    @PostMapping("/iapSysUserT/preciseQueryUserInformation/{username}")
    @ApiOperation(value = "根据用户名称精确查询用户信息",notes = "根据用户名称精确查询用户信息")
    ResponseData<IapSysUserTDto> preciseQueryUserInformation(@PathVariable("username") String username);
}
