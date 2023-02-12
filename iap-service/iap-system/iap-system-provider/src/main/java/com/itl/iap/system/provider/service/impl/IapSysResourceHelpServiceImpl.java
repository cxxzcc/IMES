package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.system.api.dto.AuthClientDto;
import com.itl.iap.system.api.dto.IapSysResourceTDto;
import com.itl.iap.system.api.entity.IapSysResourceHelp;
import com.itl.iap.system.api.entity.IapSysResourceT;
import com.itl.iap.system.api.service.IapSysResourceHelpService;
import com.itl.iap.system.api.service.IapSysResourceTService;
import com.itl.iap.system.provider.mapper.IapSysResourceHelpMapper;
import com.itl.iap.system.provider.mapper.IapSysResourceTMapper;
import com.itl.mes.core.api.entity.CollectionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 菜单实现类
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
@Service
public class IapSysResourceHelpServiceImpl extends ServiceImpl<IapSysResourceHelpMapper, IapSysResourceHelp> implements IapSysResourceHelpService {
    @Autowired
    private IapSysResourceHelpMapper iapSysResourceHelpMapper;
    @Autowired
    private IapSysResourceTService iapSysResourceTService;

 /*   @Override
    public IapSysResourceHelp getByUrl(String url) {
        IapSysResourceHelp iapSysResourceHelp = iapSysResourceHelpMapper.selectByUrl(url);
        if(iapSysResourceHelp != null){
            IapSysResourceT iapSysResourceT = iapSysResourceTService.getById(iapSysResourceHelp.getResourceId());
            iapSysResourceHelp.setResourcesName(iapSysResourceT.getResourcesName());
            iapSysResourceHelp.setParentId(iapSysResourceT.getParentId());
        }
        return iapSysResourceHelp;
    }*/

    @Override
    public Page<IapSysResourceHelp> getByUrl(Map<String, Object> params) {
        Page<IapSysResourceHelp> queryPage = new QueryPage<>(params);
        List<IapSysResourceHelp> list = iapSysResourceHelpMapper.selectByUrl(queryPage, params);
        if(list.size()>0) {
            IapSysResourceHelp iapSysResourceHelp = list.get(0);
            IapSysResourceT iapSysResourceT = iapSysResourceTService.getById(iapSysResourceHelp.getResourceId());
            if (iapSysResourceT != null) {
                iapSysResourceHelp.setResourcesName(iapSysResourceT.getResourcesName());
                iapSysResourceHelp.setParentId(iapSysResourceT.getParentId());
            }
        }
        queryPage.setRecords(list);
        return queryPage;
    }
}
