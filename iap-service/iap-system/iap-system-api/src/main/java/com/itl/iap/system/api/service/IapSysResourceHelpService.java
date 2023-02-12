
package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapSysResourceTDto;
import com.itl.iap.system.api.entity.IapSysResourceHelp;
import com.itl.iap.system.api.entity.IapSysResourceT;

import java.util.List;
import java.util.Map;

/**
 * 菜单Controller
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
public interface IapSysResourceHelpService extends IService<IapSysResourceHelp> {

    Page<IapSysResourceHelp> getByUrl(Map<String, Object> params);

    //IapSysResourceHelp getByUrl(String url);
}
