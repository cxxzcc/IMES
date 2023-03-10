package com.itl.mes.andon.api.service;

import com.itl.mes.andon.api.dto.AndonBoardDto;
import com.itl.mes.andon.api.dto.AndonLookPlateQueryDTO;
import com.itl.mes.andon.api.dto.RecordUpdateDTO;
import com.itl.mes.andon.api.vo.AndonLookPlateVo;
import com.itl.mes.andon.api.vo.RecordVo;

import java.util.Collection;
import java.util.List;

/**
 * @auth liuchenghao
 * @date 2020/12/25
 */
public interface AndonLookPlateService {

    AndonBoardDto getAndonLookPlate(AndonLookPlateQueryDTO andonLookPlateQueryDTO);


    RecordVo getRecordById(String pid);


    void updateRecord(RecordUpdateDTO recordUpdateDTO);


}
