package com.itl.mes.core.provider.listener;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.itl.mes.core.api.dto.InstrumentTypeDataDTO;
import com.itl.mes.core.api.dto.ProjectActualDataDTO;
import com.itl.mes.core.api.entity.TInstrumentType;
import com.itl.mes.core.api.entity.TProject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Author cjq
 * @Date 2021/11/15 3:44 下午
 * @Description TODO
 */
@Slf4j
public class InstrumentTypeDataListener implements ReadListener<InstrumentTypeDataDTO> {
    /**
     * 缓存的数据
     */
    @Getter
    private List<InstrumentTypeDataDTO> cachedDataList = ListUtils.newArrayList();

    @Getter
    private List errorList = new ArrayList<>();

    @Setter
    private Map<String, TInstrumentType> projectMap;


    @Override
    public void invoke(InstrumentTypeDataDTO data, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSONUtil.toJsonStr(data));
        String name = data.getName();
        StringBuilder msg = new StringBuilder();
        if (StrUtil.isBlank(name) || projectMap.containsKey(name)) {
            msg.append("名称为空或已存在 ");
        }
        if (msg.length() > 0) {
            String sheetName = analysisContext.readSheetHolder().getReadSheet().getSheetName();
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            Map m = new HashMap();
            m.put("sheet", sheetName);
            m.put("row", rowIndex + 1);
            m.put("msg", msg);
            errorList.add(m);
        }
        projectMap.put(name, new TInstrumentType());
        cachedDataList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("所有数据解析完成！");
    }


}
