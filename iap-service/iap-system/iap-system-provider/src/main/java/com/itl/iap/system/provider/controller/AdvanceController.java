package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.system.api.dto.AdvancedQueryDto;
import com.itl.iap.system.api.dto.UserCustomColumnDto;
import com.itl.iap.system.api.service.AdvanceService;
import com.itl.iap.system.provider.service.impl.AdvanceServiceCustomColumnImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 崔翀赫
 * @date 2021/3/5$
 * @since JDK1.8
 */
@Api(tags = "高级查询")
@RestController
@RequestMapping("/advance")
public class AdvanceController {


    @Autowired
    private AdvanceService advanceService;

    @Autowired
    private AdvanceServiceCustomColumnImpl advanceCustomColumn;


    @PostMapping("/queryAdvance")
    @ApiOperation(value = "高级查询", notes = "高级查询")
    public ResponseData<IPage<Map>> queryAdvance(@RequestBody AdvancedQueryDto advancedQueryDtos) throws CommonException {
        return ResponseData.success(advanceService.queryAdvance(advancedQueryDtos));
    }

    @GetMapping("/getColumn")
    @ApiOperation(value = "getColumn", notes = "getColumn")
    public ResponseData<List<Map<String, String>>> getColumn(@RequestParam String pageId) throws CommonException {
        return ResponseData.success(advanceService.getColumn(pageId));
    }

    /**
     * user_custom_column_config表
     */
    @PostMapping("/saveUserColumnConfig")
    @ApiOperation("用户选择要展示的字段然后保存此配置 update或save")
    public Boolean saveOrUpdateColumn(@RequestBody UserCustomColumnDto customColumnDto) {
        return advanceCustomColumn.saveOrUpdateById(customColumnDto);
    }

    /**
     * 查询出前端要显示的字段,user_custom_column_config表
     * String pageID 页面标识
     */
    @GetMapping("/queryUserShowColumn")
    @ApiOperation("查询要显示的字段")
    public ResponseData<String> queryShowColumns(@RequestParam String pageID) {
        return ResponseData.success(advanceCustomColumn.queryShowColumns(pageID));
    }
    /**
     * 删除用户配置
     * @param pageID 页面id
     */
    @DeleteMapping("/userShowColumn")
    @ApiOperation("删除用户配置")
    public ResponseData<Boolean> deleteUserShowColumn(@RequestParam String pageID) {
        return ResponseData.success(advanceCustomColumn.deleteUserShowColumn(UserUtils.getUserId(), pageID));
    }

}
