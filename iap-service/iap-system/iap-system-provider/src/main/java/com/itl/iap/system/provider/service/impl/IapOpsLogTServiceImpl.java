package com.itl.iap.system.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.system.api.dto.IapOpsLogTDto;
import com.itl.iap.system.api.entity.IapOpsLogT;
import com.itl.iap.system.api.service.IapOpsLogTService;
import com.itl.iap.system.provider.mapper.IapOpsLogTMapper;
import com.itl.mes.core.api.dto.CustomerDTO;
import com.itl.mes.core.client.service.CallSapApiService;
import com.itl.mes.core.client.service.impl.CallSapApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 操作日志实现类
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
@Service
public class IapOpsLogTServiceImpl extends ServiceImpl<IapOpsLogTMapper, IapOpsLogT> implements IapOpsLogTService {

    @Resource
    private IapOpsLogTMapper iapOpsLogMapper;

    @Autowired
    private CallSapApiService callSapApiService;

    /**
     * 分页查询全部
     *
     * @param
     * @return
     */
    @Override
    public IPage<IapOpsLogTDto> pageQuery(IapOpsLogTDto iapOpsLogDto) {
        if (null == iapOpsLogDto.getPage()) {
            iapOpsLogDto.setPage(new Page(0, 10));
        }
        return iapOpsLogMapper.pageQuery(iapOpsLogDto.getPage(), iapOpsLogDto);
    }

    /**
     * 系统接口日志，分页查询
     * methodType = (short) 0
     * @param
     * @return
     */
    @Override
    public IPage<IapOpsLogTDto> pageQueryTypeInterface(IapOpsLogTDto iapOpsLogDto) {
        if (null == iapOpsLogDto.getPage()) {
            iapOpsLogDto.setPage(new Page(0, 10));
        }
        // 方法类型 (0:接口日志,1:异常日志,3:交互接口日志)
        iapOpsLogDto.setMethodType((short) 0);
        return iapOpsLogMapper.pageQuery(iapOpsLogDto.getPage(), iapOpsLogDto);
    }

    /**
     * 异常日志，分页查询
     * methodType = (short) 1
     * @param
     * @return
     */
    @Override
    public IPage<IapOpsLogTDto> pageQueryException(IapOpsLogTDto iapOpsLogDto) {
        if (null == iapOpsLogDto.getPage()) {
            iapOpsLogDto.setPage(new Page(0, 10));
        }
        // 方法类型 (0:接口日志,1:异常日志,3:交互接口日志)
        iapOpsLogDto.setMethodType((short) 1);
        return iapOpsLogMapper.pageQuery(iapOpsLogDto.getPage(), iapOpsLogDto);
    }

    /**
     * 交互接口日志，分页查询
     *  methodType = (short) 3
     * @param
     * @return
     */
    @Override
    public IPage<IapOpsLogTDto> pageQueryInteractive(IapOpsLogTDto iapOpsLogDto) {
        if (null == iapOpsLogDto.getPage()) {
            iapOpsLogDto.setPage(new Page(0, 10));
        }
        // 方法类型 (0:接口日志,1:异常日志,3:交互接口日志)
        iapOpsLogDto.setMethodType((short) 3);
        return iapOpsLogMapper.pageQuery(iapOpsLogDto.getPage(), iapOpsLogDto);
    }

    /**
     * 交互接口日志
     *  methodType = (short) 3
     * @param
     * @return
     */
    @Override
    public List<IapOpsLogT> listQueryInteractive(IapOpsLogT iapOpsLogT) {
        // 方法类型 (0:接口日志,1:异常日志,3:交互接口日志)
        iapOpsLogT.setMethodType((short) 3);
        QueryWrapper<IapOpsLogT> wrapper = new QueryWrapper<IapOpsLogT>();
        wrapper.setEntity(iapOpsLogT);
        return iapOpsLogMapper.selectList(wrapper);
    }

    /**
     * 交互接口日志
     *  methodType = (short) 3
     * @param
     * @return
     */
    @Override
    public List<IapOpsLogT> getByIds(List<String> ids) {
        return iapOpsLogMapper.selectBatchIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData call(List<String> ids) {
        List<IapOpsLogT> iapOpsLogTList = iapOpsLogMapper.selectBatchIds(ids);
        if (CollectionUtil.isNotEmpty(iapOpsLogTList)) {
            for (IapOpsLogT logT : iapOpsLogTList) {
                try {
                    Thread.sleep(5000);
                    if (logT.getRequestMethod().contains(CallSapApiServiceImpl.PRODUCTION_STORAGE_ROUTING)) {
                        //生产入库
                        CustomerDTO dto = JSON.parseObject(logT.getRequestParams(), CustomerDTO.class);
                        ResponseData responseData = callSapApiService.customerSet(dto);
                        iapOpsLogMapper.deleteById(logT.getId());
                        Assert.valid(!responseData.isSuccess(), responseData.getMsg());
                    }
                    //iapOpsLogMapper.deleteById(logT.getId());
//                    if (logT.getRequestMethod().contains(CallSapApiServiceImpl.PRODUCTION_STORAGE_ROUTING)) {
//                        //生产入库
//                        ProductionStorageDto dto = JSON.parseObject(logT.getRequestParams(), ProductionStorageDto.class);
//                        dto.getIS_BFLUSHDATAGEN().setPOSTDATE(DateUtil.dateToYyyyMmDd(new Date()));
//                        productionStorage(dto);
//                    }
//                    iapOpsLogMapper.deleteById(logT.getId());
                } catch (Exception e) {
                    return ResponseData.error(e.getMessage());
                }
            }

        }
        return ResponseData.success("success");
    }

}
