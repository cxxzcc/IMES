package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.system.api.dto.AuthClientDto;
import com.itl.iap.system.api.dto.IapSysResourceTDto;
import com.itl.iap.system.api.entity.IapSysResourceT;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 菜单mapper
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
public interface IapSysResourceTMapper extends BaseMapper<IapSysResourceT> {

    /**
     * 获取所有菜单
     *
     * @param clientType
     * @return List<IapSysResourceTDto>
     */
    List<IapSysResourceTDto> queryList(@Param("clientType") String clientType);

    /**
     * 获取客户端列表
     *
     * @param authClientDto 客户端对象
     * @return List<AuthClientDto>
     */
    List<AuthClientDto> getAllClient(@Param("authClientDto") AuthClientDto authClientDto);

    /**
     * 获取用户菜单
     *
     * @param userName 用户名
     * @return
     */
    Set<IapSysResourceTDto> getAllMenu(@Param("userName") String userName, @Param("clientType") String clientType);

    /**
     * 只要菜单
     * @param userName
     * @param clientType
     * @return
     */
    Set<IapSysResourceTDto> getAllMenuTwo(@Param("userName") String userName,@Param("clientType") String clientType);
}
