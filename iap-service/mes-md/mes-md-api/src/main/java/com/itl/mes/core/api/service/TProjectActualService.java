package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.dto.TProjectActualQueryDTO;
import com.itl.mes.core.api.dto.TProjectQueryDTO;
import com.itl.mes.core.api.entity.TProject;
import com.itl.mes.core.api.entity.TProjectActual;
import com.itl.mes.core.api.vo.TProjectActualVO;

import java.util.List;

public interface TProjectActualService extends IService<TProjectActual> {

    IPage<TProjectActualVO> pageList(TProjectActualQueryDTO tProjectActualQueryDTO);

    List<TProjectActualVO> allList(TProjectActualQueryDTO tProjectActualQueryDTO);
}