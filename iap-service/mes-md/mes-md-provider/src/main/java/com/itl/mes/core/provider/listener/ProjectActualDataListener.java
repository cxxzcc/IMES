package com.itl.mes.core.provider.listener;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.itl.iap.common.base.utils.LocalDateTimeUtils;
import com.itl.mes.core.api.dto.ProjectActualDataDTO;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.entity.Router;
import com.itl.mes.core.api.entity.TProject;
import com.itl.mes.core.api.entity.TProjectActual;
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
public class ProjectActualDataListener implements ReadListener<ProjectActualDataDTO> {
    /**
     * 缓存的数据
     */
    @Getter
    private List<ProjectActualDataDTO> cachedDataList = ListUtils.newArrayList();

    @Getter
    private List errorList = new ArrayList<>();

    @Setter
    private Map<String, TProject> projectMap;

    @Setter
    private Map<String, Device> deviceMap;


    @Override
    public void invoke(ProjectActualDataDTO data, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSONUtil.toJsonStr(data));
        String device = data.getDevice();
        String projectCode = data.getProjectCode();
        Date useDate = data.getUseDate();
        String standard = data.getStandard();
        StringBuilder msg = new StringBuilder();
        if (StrUtil.isBlank(device) || !deviceMap.containsKey(device)) {
            msg.append("仪器编码为空或者不存在 ");
        }
        if (StrUtil.isBlank(projectCode) || !projectMap.containsKey(projectCode)) {
            msg.append("项目编码为空或者不存在 ");
        }
        if (useDate == null) {
            msg.append("使用日期为空");
        }
        if (StrUtil.isBlank(standard) || standard.length() > 8) {
            msg.append("标准值为空或总长度超过7 ");
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
        cachedDataList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("所有数据解析完成！");
    }


}
