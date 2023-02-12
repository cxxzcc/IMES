package com.itl.iap.notice.provider.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.notice.api.entity.PushTargetPerson;
import com.itl.iap.notice.api.service.IPushTargetPersonService;
import com.itl.iap.notice.provider.mapper.PushTargetPersonMapper;
import org.springframework.stereotype.Service;

/**
 *
 * @author lK
 * @since 2021-08-23
 */
@Service
public class PushTargetPersonServiceImpl extends ServiceImpl<PushTargetPersonMapper, PushTargetPerson> implements IPushTargetPersonService {

}
