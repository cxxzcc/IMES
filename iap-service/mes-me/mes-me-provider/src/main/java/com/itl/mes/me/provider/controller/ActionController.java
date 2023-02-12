package com.itl.mes.me.provider.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.me.api.dto.ActionDto;
import com.itl.mes.me.api.entity.Action;
import com.itl.mes.me.api.entity.ActionOperation;
import com.itl.mes.me.api.service.ActionService;
import com.itl.mes.me.provider.mapper.ActionOperationMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * 过站动作
 *
 * @author cch
 * @date 2021-05-31
 */
@RestController
@Api(tags = "过站动作")
@RequestMapping("/action")
public class ActionController {
    @Autowired
    private ActionService actionService;
    @Autowired
    private ActionOperationMapper actionOperationMapper;

    @ApiOperation(value = "分页查询")
    @PostMapping("/list")
    public ResponseData list(@RequestBody ActionDto actionDto) {
        LambdaQueryWrapper<Action> lambda = new QueryWrapper<Action>().lambda();
        if (StrUtil.isNotBlank(actionDto.getAction())) {
            lambda.like(Action::getAction, actionDto.getAction());
        }
        if (StrUtil.isNotBlank(actionDto.getDesc())) {
            lambda.like(Action::getActionDesc, actionDto.getDesc());
        }
        lambda.eq(Action::getSite, UserUtils.getSite());
        IPage page = actionService.page(actionDto.getPage() == null ? new Page<>(1, 10) :
                actionDto.getPage(), lambda);
        return ResponseData.success(page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public ResponseData info(@PathVariable("id") String id) {
        List<HashMap<String,Object>> actionOperations = actionService.getDetails(id);
        return ResponseData.success(actionOperations);
    }

    @GetMapping("/infoByOperationID/{id}")
    public ResponseData infoByOperationID(@PathVariable("id") String id) {
        List<HashMap<String,Object>> actionOperations = actionService.getDetailsByOperationID(id);
        return ResponseData.success(actionOperations);
    }

    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData save(@RequestBody Action action) {
        action.setCreateDate(new Date());
        action.setCreateUser(UserUtils.getUser());
        action.setId(IdUtil.fastSimpleUUID());
        action.setSite(UserUtils.getSite());
        actionService.save(action);
        action.getList().forEach(x ->
                actionOperationMapper.insert(x.setActionId(action.getId())));
        return ResponseData.success();
    }

    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData update(@RequestBody Action action) {
        action.setModifyDate(new Date());
        action.setModifyUser(UserUtils.getUser());
        actionService.updateById(action);
        actionOperationMapper.delete(new QueryWrapper<ActionOperation>().lambda().eq(ActionOperation::getActionId, action.getId()));
        action.getList().forEach(x ->
                actionOperationMapper.insert(x.setActionId(action.getId())));
        return ResponseData.success();
    }


    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData delete(@RequestBody String[] ids) {
        actionService.removeByIds(Arrays.asList(ids));
        actionOperationMapper.delete(new QueryWrapper<ActionOperation>().lambda().in(ActionOperation::getActionId, ids));
        return ResponseData.success();
    }

    @PostMapping("/getById/{actionId}")
    @ApiOperation("查一条for feign")
    public Action getById(@PathVariable("actionId") String actionId) {
        return actionService.getById(actionId);
    }
}
