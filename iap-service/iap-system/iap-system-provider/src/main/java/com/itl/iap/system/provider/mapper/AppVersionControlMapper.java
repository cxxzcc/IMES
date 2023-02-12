package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.system.api.entity.AppVersionControl;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yezi
 * @date 2019/5/29
 */
public interface AppVersionControlMapper extends BaseMapper<AppVersionControl> {
    /**
     * 设置所有记录不为最新版本
     */
    void setAllRecordNotUpToDate(@RequestParam(value = "systemType") String systemType);
}
