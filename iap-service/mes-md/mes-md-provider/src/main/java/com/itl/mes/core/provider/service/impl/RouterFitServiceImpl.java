package com.itl.mes.core.provider.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.dto.RouterFitDto;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.vo.CustomDataValVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.provider.mapper.RouterFitMapper;
import com.itl.mes.core.api.entity.RouterFit;
import com.itl.mes.core.api.service.RouterFitService;
import javax.annotation.Resource;
import java.util.List;


/**
 *
 * @author xtz
 * @date 2021-05-25
 */
@Service("routerFitService")
public class RouterFitServiceImpl extends ServiceImpl<RouterFitMapper, RouterFit> implements RouterFitService {

    @Resource
    private RouterFitMapper routerFitMapper;

    @Autowired
    CustomDataValService customDataValService;

    @Autowired
    private RouterFitService routerFitService;


    @Override
    public IPage<RouterFitDto> queryByItem(RouterFitDto routerFitDto) {
        if(ObjectUtil.isEmpty(routerFitDto.getPage())){
            routerFitDto.setPage(new Page(0, 10));
        }
        routerFitDto.setSite(UserUtils.getSite());
        return routerFitMapper.queryByItem(routerFitDto.getPage(),routerFitDto);
    }

    @Override
    public IPage<RouterFitDto> queryByItemGroup(RouterFitDto routerFitDto) {
        if(ObjectUtil.isEmpty(routerFitDto.getPage())){
            routerFitDto.setPage(new Page(0, 10));
        }
        routerFitDto.setSite(UserUtils.getSite());
        return routerFitMapper.queryByItemGroup(routerFitDto.getPage(),routerFitDto);
    }

    @Override
    public IPage<RouterFitDto> queryByProductLine(RouterFitDto routerFitDto) {
        if(ObjectUtil.isEmpty(routerFitDto.getPage())){
            routerFitDto.setPage(new Page(0, 10));
        }
        routerFitDto.setSite(UserUtils.getSite());
        return routerFitMapper.queryByProductLine(routerFitDto.getPage(),routerFitDto);
    }

    @Override
    public boolean add(RouterFit routerFit) throws CommonException {
        routerFit.setBo(UUID.uuid32());
        routerFit.setSite(UserUtils.getSite());
        saveCustom(routerFit.getCustomDataValVoList(),routerFit.getBo());
        routerFitMapper.insert(routerFit);
        return true;
    }

    @Override
    public boolean update(RouterFit routerFit) throws CommonException {
        saveCustom(routerFit.getCustomDataValVoList(),routerFit.getBo());
        routerFitMapper.updateById(routerFit);
        return true;
    }

    @Override
    public void delete(RouterFit routerFit) {
    customDataValService.deleteCustomDataValByBoAndType(UserUtils.getSite(), routerFit.getBo(), CustomDataTypeEnum.ROUTER_FIT);
    routerFitMapper.deleteById(routerFit);
    }

    @Override
    public RouterFitDto getRouterAndBom(String orderType, String itemBo, String productBo) {
        if(orderType !=null && orderType !="" && itemBo!=null && itemBo!= "" ){
            RouterFitDto first = new RouterFitDto();
            first.setSite(UserUtils.getSite());
            first.setShopOrderType(orderType);
            first.setElementBo(itemBo);
            IPage<RouterFitDto> queryByItem = routerFitService.queryByItem(first);
            if(queryByItem.getRecords().size()!=0){
                String routerBo1 = queryByItem.getRecords().get(0).getRouterBo();
                String bomBo1 = queryByItem.getRecords().get(0).getBomBo();
                if (routerBo1==null && bomBo1==null){
                    String itemGroupBo = routerFitMapper.getItemGroup(itemBo).get(0);
                    RouterFitDto second = new RouterFitDto();
                    second.setSite(UserUtils.getSite());
                    second.setShopOrderType(orderType);
                    second.setElementBo(itemGroupBo);
                    IPage<RouterFitDto> queryByItemGroup = routerFitService.queryByItemGroup(second);
                    if (queryByItemGroup.getRecords().size()!=0){
                        String routerBo2 = queryByItemGroup.getRecords().get(0).getRouterBo();
                        String bomBo2 = queryByItemGroup.getRecords().get(0).getBomBo();
                        if (routerBo2==null && bomBo2==null){
                            RouterFitDto third = new RouterFitDto();
                            third.setSite(UserUtils.getSite());
                            third.setShopOrderType(orderType);
                            third.setElementBo(productBo);
                            IPage<RouterFitDto> queryByProductLine = routerFitService.queryByProductLine(third);
                            return queryByProductLine.getRecords().get(0);
                        }else {
                            return queryByItemGroup.getRecords().get(0);
                        }
                    }
                }else {
                    return queryByItem.getRecords().get(0);
                }
            }
        }
        return null;
    }


    /**
     * 保存自定义数据
     * @param list
     * @param bo
     */
    private void saveCustom(List<CustomDataValVo> list, String bo) throws CommonException {
        if( list!=null && !list.isEmpty() ){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(bo );
            customDataValRequest.setSite( UserUtils.getSite() );
            customDataValRequest.setCustomDataType( CustomDataTypeEnum.ROUTER_FIT.getDataType() );
            customDataValRequest.setCustomDataValVoList( list );
            customDataValService.saveCustomDataVal( customDataValRequest );
        }
    }
}
