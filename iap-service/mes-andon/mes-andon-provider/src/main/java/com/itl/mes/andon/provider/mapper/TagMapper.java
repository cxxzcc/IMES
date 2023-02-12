package com.itl.mes.andon.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.andon.api.entity.Tag;
import com.itl.mes.andon.api.vo.TagVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    IPage<TagVo> findList(Page page, @Param("tagVo")TagVo tagVo);
}
