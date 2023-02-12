package com.itl.mes.core.provider.listener;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.BOPrefixEnum;
import com.itl.mes.core.api.constant.RouterProcessEnum;
import com.itl.mes.core.api.dto.RouterDataDTO;
import com.itl.mes.core.api.dto.RouterProcessDataDTO;
import com.itl.mes.core.api.entity.Router;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author cjq
 * @Date 2021/11/15 3:44 下午
 * @Description TODO
 */
@Slf4j
public class RouterProcessDataListener implements ReadListener<RouterProcessDataDTO> {
    /**
     * 缓存的数据
     */
    @Getter
    private List<RouterProcessDataDTO> cachedDataList = ListUtils.newArrayList();

    @Getter
    private List errorList = new ArrayList<>();


    @Override
    public void invoke(RouterProcessDataDTO data, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSONUtil.toJsonStr(data));
        StringBuilder msg = new StringBuilder();
        if (StrUtil.isBlank(data.getRouter())) {
            msg.append("工艺路线编码不能为空 ");
        }
        if (StrUtil.isBlank(data.getId())) {
            msg.append("工序顺序码不能为空 ");
        }
        if (StrUtil.isBlank(data.getOperation())) {
            msg.append("工序编码,版本号不能为空 ");
        }
        if (!RouterProcessEnum.END.getCode().equals(data.getOperation())) {
            if (StrUtil.isBlank(data.getNextId())) {
                msg.append("下工序顺序码不能为空 ");
            }
        }
        if (!RouterProcessEnum.START.getCode().equals(data.getOperation())
                && !RouterProcessEnum.END.getCode().equals(data.getOperation())) {
            if (data.getIsRepeat() == null) {
                msg.append("可否重复过站不能为空 ");
            }
            if (data.getIsSkip() == null) {
                msg.append("可否跳站不能为空 ");
            }
            if (data.getIsCreateSKU() == null) {
                msg.append("是否创建SKU不能为空 ");
            }
        }
        if (msg.length() > 0) {
            String sheetName = analysisContext.readSheetHolder().getReadSheet().getSheetName();
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            Map m = new HashMap();
            m.put("sheet", sheetName);
            m.put("row", rowIndex+1);
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
