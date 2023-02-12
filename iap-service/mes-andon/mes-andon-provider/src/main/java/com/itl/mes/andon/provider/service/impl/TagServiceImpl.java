package com.itl.mes.andon.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.andon.api.bo.AndonHandleBo;
import com.itl.mes.andon.api.bo.PushHandleBO;
import com.itl.mes.andon.api.bo.TagHandleBO;
import com.itl.mes.andon.api.entity.Tag;
import com.itl.mes.andon.api.entity.Type;
import com.itl.mes.andon.api.service.TagService;
import com.itl.mes.andon.api.vo.TagVo;
import com.itl.mes.andon.api.vo.TypeVo;
import com.itl.mes.andon.provider.mapper.TagMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service("tagService")
@Transactional
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private  TagMapper tagMapper;

    @Override
    public void saveByTagVo(TagVo tagVo) throws CommonException {
        TagHandleBO tagHandleBO = new TagHandleBO(UserUtils.getSite(),tagVo.getAndonTypeTag());
        String bo = tagHandleBO.getBo();
        Tag tag_db = tagMapper.selectById(bo);
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagVo,tag);
        if(tag_db == null){
            tag.setBo(bo);
            tag.setSite(UserUtils.getSite());
            tag.setCreateDate(new Date());
            tag.setCreateUser(UserUtils.getUserName());
            tagMapper.insert(tag);
        }else{
            tag.setModifyDate(new Date());
            tag.setModifyUser(UserUtils.getUserName());
            tagMapper.updateById(tag);
        }

    }

    @Override
    public IPage<TagVo> queryPage(TagVo tagVo) throws CommonException {
        if (ObjectUtil.isEmpty(tagVo.getPage())) {
            tagVo.setPage(new Page(0, 10));
        }
        tagVo.setSite(UserUtils.getSite());
        return tagMapper.findList(tagVo.getPage(), tagVo);


    }

}
