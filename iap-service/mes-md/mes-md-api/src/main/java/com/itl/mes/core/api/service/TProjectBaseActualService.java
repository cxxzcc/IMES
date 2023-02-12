package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.dto.TProjectActualQueryDTO;
import com.itl.mes.core.api.dto.TProjectBaseActualQueryDTO;
import com.itl.mes.core.api.entity.TProjectActual;
import com.itl.mes.core.api.entity.TProjectBaseActual;
import com.itl.mes.core.api.vo.TProjectActualVO;
import com.itl.mes.core.api.vo.TProjectBaseActualVO;

import java.util.List;

public interface TProjectBaseActualService extends IService<TProjectBaseActual> {

    IPage<TProjectBaseActualVO> pageList(TProjectBaseActualQueryDTO tProjectBaseActualQueryDTO);

    List<TProjectBaseActualVO> allList(TProjectBaseActualQueryDTO tProjectBaseActualQueryDTO);
}