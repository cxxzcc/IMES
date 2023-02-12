package com.itl.mom.label.provider.mapper.label;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mom.label.api.entity.label.SnTransRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 条码转移记录mapper
 * @author dengou
 * @date 2021/11/1
 */
@Mapper
public interface SnTransRecordMapper extends BaseMapper<SnTransRecord> {

    /**
     * 分页查询
     * */
    List<SnTransRecord> page(IPage<SnTransRecord> queryPage,@Param("params") Map<String, Object> params);
}
