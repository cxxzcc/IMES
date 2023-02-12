package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.AuthClientDto;
import com.itl.iap.system.api.dto.IapSysResourceTDto;
import com.itl.iap.system.api.entity.IapSysResourceHelp;
import com.itl.iap.system.api.entity.IapSysResourceT;
import com.itl.mes.core.api.entity.CollectionRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单mapper
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
public interface IapSysResourceHelpMapper extends BaseMapper<IapSysResourceHelp> {


    List<IapSysResourceHelp> findList();

 //   IapSysResourceHelp selectByUrl(String url);


    List<IapSysResourceHelp> selectByUrl(Page<IapSysResourceHelp> queryPage, @Param("params") Map<String, Object> params);

}
