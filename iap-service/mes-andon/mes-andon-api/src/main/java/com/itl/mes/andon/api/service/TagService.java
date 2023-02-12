package com.itl.mes.andon.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.andon.api.entity.Tag;
import com.itl.mes.andon.api.vo.TagVo;
import com.itl.mes.andon.api.vo.TypeVo;

public interface TagService extends IService<Tag> {
    /**
     * 保存安灯类型标识数据
     *
     * @param tagVo 参数
     * @throws CommonException 异常
     */
    void saveByTagVo(TagVo tagVo) throws CommonException;


    IPage<TagVo> queryPage(TagVo tagVo) throws CommonException;
}


