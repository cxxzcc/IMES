package com.itl.mes.core.provider.listener;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.BOPrefixEnum;
import com.itl.mes.core.api.dto.RouterDataDTO;
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
public class RouterDataListener implements ReadListener<RouterDataDTO> {
    /**
     * 缓存的数据
     */
    @Getter
    private List<RouterDataDTO> cachedDataList = ListUtils.newArrayList();

    @Getter
    private List errorList = new ArrayList<>();

    private String site = UserUtils.getSite();

    @Setter
    private Map<String, Router> routerMap;

    @Override
    public void invoke(RouterDataDTO data, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSONUtil.toJsonStr(data));
        StringBuilder msg = new StringBuilder();
        if (StrUtil.isBlank(data.getRouter())) {
            msg.append("工艺路线编码不能为空 ");
        }
        if (StrUtil.isBlank(data.getRouterName())) {
            msg.append("工艺路线名称不能为空 ");
        }
        if (StrUtil.isBlank(data.getRouterType())) {
            msg.append("路线类型不能为空 ");
        }
        if (StrUtil.isBlank(data.getState())) {
            msg.append("路线状态不能为空 ");
        }
        if (StrUtil.isBlank(data.getVersion())) {
            msg.append("版本不能为空 ");
        }
        String routerBo = BOPrefixEnum.ROUTER.getPrefix() + ":" + site + "," + data.getRouter() + "," + data.getVersion();
        if(routerMap.containsKey(routerBo)){
            msg.append("工艺路线不能重复 ");
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
