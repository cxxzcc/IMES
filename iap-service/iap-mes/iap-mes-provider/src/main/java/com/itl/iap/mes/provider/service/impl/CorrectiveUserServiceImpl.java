package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.mes.api.entity.CorrectiveUser;
import com.itl.iap.mes.api.service.CorrectiveUserService;
import com.itl.iap.mes.provider.mapper.CorrectiveUserMapper;
import org.springframework.stereotype.Service;

/**
 * @author yx
 * @date 2021/8/16
 * @since 1.8
 */
@Service
public class CorrectiveUserServiceImpl extends ServiceImpl<CorrectiveUserMapper, CorrectiveUser> implements CorrectiveUserService {
}
