package com.itl.mes.core.provider.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CollectionRecordCommonTempDTO;
import com.itl.mes.core.api.entity.TemporaryData;
import com.itl.mes.core.api.service.ITemporaryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
@RestController
@RequestMapping("/temporaryData")
public class TemporaryDataController {

    @Autowired
    private ITemporaryDataService temporaryDataService;

    /**
     * 删除临时数据
     * @param sn 条码
     * @param station 工位
     * @return 是否成功
     * */
    @DeleteMapping("/remove")
    public ResponseData<Boolean> remove(@RequestParam("sn") String sn, @RequestParam("station") String station, @RequestParam("type") String type) {
        return ResponseData.success(temporaryDataService.removeBySns(Arrays.asList(sn), station, type));
    }
    /**
     * 删除临时数据
     * @param sn 条码
     * @param station 工位
     * @return 是否成功
     * */
    @DeleteMapping("/removeList")
    public ResponseData<Boolean> removeList(@RequestParam("sn") String sn, @RequestParam("station") String station, @RequestParam("type") String type) {
        return ResponseData.success(temporaryDataService.removeListBySns(Arrays.asList(sn), station, type));
    }


    /**
     * 保存临时数据， 根据sn和station判断是否存在， 存在则更新，不存在则新增
     * */
    @PostMapping("/repair/saveTemp")
    public ResponseData<Boolean> addOrUpdate(@RequestBody TemporaryData temporaryData) {
        return ResponseData.success(temporaryDataService.addOrUpdate(temporaryData));
    }

    /**
     * 保存临时数据， 根据sn和station判断是否存在， 存在则更新，不存在则新增
     * */
    @PostMapping("/common/getCommonData")
    public ResponseData<List<CollectionRecordCommonTempDTO>> getCommons(String sn, String station, String types) {
        List<TemporaryData> list = temporaryDataService.getBySnAndStation(sn, station, StrUtil.splitTrim(types, StrUtil.COMMA));
        if(CollUtil.isEmpty(list)) {
            return ResponseData.success(Collections.emptyList());
        }
        List<CollectionRecordCommonTempDTO> result = new ArrayList<>();
        for (TemporaryData temporaryData : list) {
            String content = temporaryData.getContent();
            if(StrUtil.isBlank(content)) {
                continue;
            }
            CollectionRecordCommonTempDTO collectionRecordCommonTempDTO = JSONUtil.parse(content).toBean(CollectionRecordCommonTempDTO.class);
            if(StrUtil.isBlank(collectionRecordCommonTempDTO.getSn())) {
                collectionRecordCommonTempDTO.setSn(temporaryData.getSn());
            }
            result.add(collectionRecordCommonTempDTO);
        }
        return ResponseData.success(result);

    }

    /**
     * 根据id列表查询
     * */
    @PostMapping("/getByIds")
    public ResponseData<List<TemporaryData>> getByIds(@RequestBody List<String> ids) {
        return ResponseData.success(new ArrayList<>(temporaryDataService.listByIds(ids)));
    }

}
