package com.itl.mom.label.provider.impl.label;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mom.label.api.entity.label.SnTransRecord;
import com.itl.mom.label.api.service.label.SnTransRecordService;
import com.itl.mom.label.provider.mapper.label.SnTransRecordMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 条码转移记录服务实现
 * @author dengou
 * @date 2021/11/1
 */
@Service
public class SnTransRecordServiceImpl extends ServiceImpl<SnTransRecordMapper, SnTransRecord> implements SnTransRecordService {


    @Override
    public Boolean add(SnTransRecord snTransRecord) {
        Assert.valid(snTransRecord == null, "转移记录参数不能为空");
        Assert.valid(StrUtil.isBlank(snTransRecord.getSnBo()), "条码id不能为空");

        if(StrUtil.isBlank(snTransRecord.getRealName())) {
            snTransRecord.setRealName(UserUtils.getCurrentUser().getRealName());
        }
        if(StrUtil.isBlank(snTransRecord.getUserName())) {
            snTransRecord.setUserName(UserUtils.getCurrentUser().getUserName());
        }
        if(snTransRecord.getTransDate() == null) {
            snTransRecord.setTransDate(new Date());
        }

        return save(snTransRecord);
    }

    @Override
    public Page<SnTransRecord> page(Map<String, Object> params) {
        params.put("site", UserUtils.getSite());
        Page<SnTransRecord> queryPage = new QueryPage<>(params);
        List<SnTransRecord> list = baseMapper.page(queryPage, params);
        queryPage.setRecords(list);
        return queryPage;
    }
}
