package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.RecordOfProductTest;
import com.itl.mes.core.api.service.IRecordOfProductTestService;
import com.itl.mes.core.provider.mapper.RecordOfProductTestMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  产品检验记录服务实现类
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
@Service
public class RecordOfProductTestServiceImpl extends ServiceImpl<RecordOfProductTestMapper, RecordOfProductTest> implements IRecordOfProductTestService {

    @Override
    public List<RecordOfProductTest> getListByCollectionRecordId(String id) {
        return lambdaQuery().eq(RecordOfProductTest::getCollectionRecordId, id).orderByDesc(RecordOfProductTest::getCreateTime).list();
    }

}
