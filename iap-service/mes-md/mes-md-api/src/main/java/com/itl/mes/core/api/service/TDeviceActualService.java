package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.dto.TDeviceActualQueryDTO;
import com.itl.mes.core.api.dto.TProjectActualQueryDTO;
import com.itl.mes.core.api.entity.TDeviceActual;
import com.itl.mes.core.api.entity.TProjectActual;
import com.itl.mes.core.api.vo.TDeviceActualVO;
import com.itl.mes.core.api.vo.TProjectActualVO;

public interface TDeviceActualService extends IService<TDeviceActual> {

    IPage<TDeviceActualVO> pageList(TDeviceActualQueryDTO tDeviceActualQueryDTO);
}