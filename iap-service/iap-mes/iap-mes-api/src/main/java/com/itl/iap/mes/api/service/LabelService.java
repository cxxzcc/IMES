package com.itl.iap.mes.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.mes.api.dto.label.LabelQueryDTO;
import com.itl.iap.mes.api.dto.label.LabelSaveDto;
import com.itl.iap.mes.api.entity.label.LabelEntity;
import com.itl.iap.mes.api.vo.LabelVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @auth liuchenghao
 * @date 2021/3/12
 */
public interface LabelService {

    IPage<LabelEntity> findList(LabelQueryDTO labelQueryDTO);


    IPage<LabelEntity> findListByState(LabelQueryDTO labelQueryDTO);


    void save(LabelSaveDto labelSaveDto);


    void delete(List<String> ids);

    LabelVo findById(String id);


    String preview(Map<String, Object> params, HttpServletResponse response);


    String exportFile(Map params, HttpServletResponse response);


    void batchPrint(List<Map<String, Object>> list, String labelId);


    List<String> batchCreatePdf(List<Map<String, Object>> list, String labelId);
}
