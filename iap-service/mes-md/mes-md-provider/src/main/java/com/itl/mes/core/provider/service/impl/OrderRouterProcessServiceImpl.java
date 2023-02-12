package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.OrderRouterProcess;
import com.itl.mes.core.api.service.OrderRouterProcessService;
import com.itl.mes.core.provider.mapper.OrderRouterProcessMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderRouterProcessServiceImpl extends ServiceImpl<OrderRouterProcessMapper, OrderRouterProcess> implements OrderRouterProcessService {

}
