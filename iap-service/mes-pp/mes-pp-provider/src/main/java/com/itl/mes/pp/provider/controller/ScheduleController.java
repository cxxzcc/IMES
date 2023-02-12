package com.itl.mes.pp.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.StringUtils;
import com.itl.mes.core.api.dto.ConfigItemDto;
import com.itl.mes.core.api.vo.ConfigItemVo;
import com.itl.mes.core.client.service.ConfigItemService;
import com.itl.mes.pp.api.dto.schedule.*;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import com.itl.mes.pp.api.service.ScheduleService;
import com.itl.mes.pp.provider.mapper.ScheduleMapper;
import feign.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author liuchenghao
 * @date 2020/11/12 9:49
 */
@Api("排程管理控制层")
@RestController
@RequestMapping("/p/schedule")
@Slf4j
public class ScheduleController {

    @Autowired
    ScheduleMapper scheduleMapper;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    ConfigItemService configItemService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData<IPage<ScheduleRespDTO>> findList(@RequestBody ScheduleQueryDTO scheduleQueryDTO) {
        return ResponseData.success(scheduleService.findList(scheduleQueryDTO));
    }

    @PostMapping("/queryTwo")
    @ApiOperation(value = "分页查询信息Two", notes = "分页查询信息(Two)")
    public ResponseData<IPage<ScheduleRespTwoDTO>> findTwoList(@RequestBody ScheduleQueryTwoDTO scheduleQueryTwoDTO) {
        // 默认的处理搜索框模糊搜索，将用户输入的  ”*searchText*“ 替换成 ”%searchText%“
        scheduleQueryTwoDTO.setScheduleNo(StringUtils.replaceMatchText(scheduleQueryTwoDTO.getScheduleNo())); // 排程号
        scheduleQueryTwoDTO.setOrderNo(StringUtils.replaceMatchText(scheduleQueryTwoDTO.getOrderNo())); // 订单号
        scheduleQueryTwoDTO.setItemCode(StringUtils.replaceMatchText(scheduleQueryTwoDTO.getItemCode())); // 物料号
        scheduleQueryTwoDTO.setItemName(StringUtils.replaceMatchText(scheduleQueryTwoDTO.getItemName())); // 物料名称
        scheduleQueryTwoDTO.setWorkShop(StringUtils.replaceMatchText(scheduleQueryTwoDTO.getWorkShop())); // 车间编号
        scheduleQueryTwoDTO.setProductionLineCode(StringUtils.replaceMatchText(scheduleQueryTwoDTO.getProductionLineCode())); // 产线号
        scheduleQueryTwoDTO.setProductionLineDesc(StringUtils.replaceMatchText(scheduleQueryTwoDTO.getProductionLineDesc())); // 产线描述
        scheduleQueryTwoDTO.setShopOrder(StringUtils.replaceMatchText(scheduleQueryTwoDTO.getShopOrder())); // 工单号
        return ResponseData.success(scheduleService.findTwoList(scheduleQueryTwoDTO));
    }


    /*@PostMapping("/queryTwoPage")
    @ApiOperation(value = "分页查询信息Two", notes = "分页查询信息(Two)")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="orderNo", value = "订单编号，支持模糊查询。" ),
            @ApiImplicitParam( name="itemNo", value = "物料编号，支持模糊查询。" ),
            @ApiImplicitParam( name="itemName", value = "物料名称，支持模糊查询。" ),
            @ApiImplicitParam( name="status", value = "订单状态,字典:PRO_ORD_STATUS" ),
            @ApiImplicitParam( name="type", value = "订单类型,字典:PRO_ORD_TYPE" ),
            @ApiImplicitParam( name="sellOrderNo", value = "销售订单编号，支持模糊查询。" ),
            @ApiImplicitParam( name="sellOrderLine", value = "销售订单行，支持模糊查询。" ),
            @ApiImplicitParam( name="source", value = "来源，MES=mes自建" )
    })
    public ResponseData<IPage<ScheduleRespTwoDTO>> findTwoPage(@RequestParam Map<String, Object> params) {
        params = StringUtils.replaceMatchTextByMap(params);
        return ResponseData.success(scheduleService.findTwoPage(params));
    }*/

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody ScheduleSaveDTO scheduleSaveDTO) {
        try {
            scheduleService.save(scheduleSaveDTO);
            return ResponseData.success(true);
        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }

    }

    @ApiOperation(value = "saveTwo", notes = "新增/修改(Two)", httpMethod = "PUT")
    @PutMapping(value = "/saveTwo")
    public ResponseData saveTwo(@RequestBody List<ScheduleSaveTwoDTO> scheduleSaveTwoDTOList) {
        try {
            scheduleService.saveTwo(scheduleSaveTwoDTOList);
            return ResponseData.success(true);
        } catch (CommonException e) {
            return ResponseData.error(String.valueOf(e.getCode()),e.getMessage());
        }

    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        try {
            scheduleService.delete(ids);
            return ResponseData.success(true);
        } catch (CommonException e) {
            e.printStackTrace();
            return ResponseData.error(String.valueOf(e.getCode()),e.getMessage());
        }

    }

    @ApiOperation(value = "deletePline", notes = "删除排程产线", httpMethod = "DELETE")
    @DeleteMapping(value = "/deletePline")
    public ResponseData deletePline(@RequestBody List<String> ids) {
        try {
            scheduleService.deletePline(ids);
            return ResponseData.success(true);
        } catch (CommonException e) {
            e.printStackTrace();
            return ResponseData.error(String.valueOf(e.getCode()),e.getMessage());
        }
    }


    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData<ScheduleDetailRespDTO> getById(@PathVariable String id) {
        return ResponseData.success(scheduleService.findById(id));
    }

    @ApiOperation(value = "getTwoById", notes = "查看一条(Two)", httpMethod = "GET")
    @GetMapping(value = "/getTwoById/{schedulePlineBo}")
    public ResponseData<ScheduleSaveTwoDTO> getTwoById(@PathVariable String schedulePlineBo) {
        return ResponseData.success(scheduleService.findTwoById(schedulePlineBo));
    }

    @ApiOperation(value = "getByIdWithCount", notes = "查看一条WithCount", httpMethod = "GET")
    @GetMapping(value = "/getByIdWithCount/{id}")
    public ResponseData<ScheduleDetailRespDTO> getByIdWithCount(@PathVariable String id) throws CommonException {
        return ResponseData.success(scheduleService.findByIdWithCount(id));
    }


    @PostMapping("/release")
    @ApiOperation(value = "排程下达", notes = "排程下达")
    public ResponseData release(String scheduleBo) {
        scheduleService.updateState(scheduleBo);
        return ResponseData.success(true);
    }

    @PostMapping("/releaseSchedule")
    @ApiOperation(value = "排程下达Two", notes = "排程下达Two")
    public ResponseData releaseSchedule(@RequestParam String schedulePlineBo) {
        scheduleService.updateScheduleState(schedulePlineBo);
        return ResponseData.success(true);
    }

    @PostMapping("/batchReleaseSchedule")
    @ApiOperation(value = "排程批量下达Two", notes = "排程批量下达Two")
    public ResponseData batchReleaseSchedule(@RequestBody List<String> schedulePlineBos) {
        try {
            scheduleService.batchUpdateScheduleState(schedulePlineBos);
            return ResponseData.success(true);
        }catch (CommonException commonException){
            return ResponseData.error(String.valueOf(commonException.getCode()),commonException.getMessage());
        }
    }


    @PostMapping("/batchRelease")
    @ApiOperation(value = "批量排程下达", notes = "批量排程下达")
    public ResponseData batchRelease(@RequestBody List<String> scheduleBos) throws CommonException {
        scheduleService.batchRelease(scheduleBos);
        return ResponseData.success(true);
    }

    @PostMapping("/initScheduleProductionLineList")
    @ApiOperation(value = "查询初始化的排程产线，未保存数据查询显示", notes = "查询初始化的排程产线，未保存数据查询显示")
    public ResponseData<IPage<ProductionLineResDTO>> initScheduleProductionLineList(@RequestBody ScheduleProductionLineQueryDTO scheduleProductionLineQueryDTO) {

        return ResponseData.success(scheduleService.getInitScheduleProductionLineList(scheduleProductionLineQueryDTO));

    }

    @PostMapping("/getStationList")
    @ApiOperation(value = "查询产线工位", notes = "查询产线工位")
    public ResponseData<List<StationResDTO>> getStationList(String productionLineBo) {

        return ResponseData.success(scheduleService.getStationList(productionLineBo));

    }


    @PostMapping("/scheduleProductionLineList")
    @ApiOperation(value = "查询排程产线，保存数据后查询显示", notes = "查询排程产线，保存数据后查询显示")
    public ResponseData<IPage<ScheduleProductionLineRespDTO>> scheduleProductionLineList(@RequestBody ScheduleProductionLineQueryDTO scheduleProductionLineQueryDTO) {

        return ResponseData.success(scheduleService.getScheduleProductionLine(scheduleProductionLineQueryDTO));

    }

    @PostMapping("/getByProductionLine")
    public ResponseData<IPage<Map<String, Object>>> getByProductLine(@RequestBody ScheduleShowDto scheduleShowDto) {
        scheduleShowDto.setProductLine(
                Optional.ofNullable(scheduleShowDto.getProductLine())
                        .orElseGet(() -> UserUtils.getProductLine())
        );
        return ResponseData.success(scheduleService.getByProductLine(scheduleShowDto));
    }


    /**
     * liKun
     * /p/schedule/getByc/process
     */
    @GetMapping("/getByc/process")
    @ApiOperation(value = "根据排程BO获取排程信息", notes = "仅供工序模块调用")
    public ResponseData<ScheduleEntity> getByScheduleBoProcess(@Param("scheduleBo") String scheduleBo) {
        ScheduleEntity scheduleEntity = scheduleMapper.selectById(scheduleBo);
        return ResponseData.success(scheduleEntity);
    }

    /**
     * @param shopOrderBO
     * @return
     */
    @GetMapping("/getByc/shopOrder")
    @ApiOperation(value = "根据工单号获取排程信息")
    public ResponseData<List<ScheduleEntity>> getByScheduleShopOrder(@Param("shopOrderBO") String shopOrderBO) {
        List<ScheduleEntity> scheduleEntityList = scheduleService.getByScheduleShopOrder(shopOrderBO);
        return ResponseData.success(scheduleEntityList);
    }

    /**
     * 新增/修改配置项信息
     * @param configItemVo
     * @return
     */
    @ApiOperation(value = "configItemSave", notes = "新增/修改配置项信息", httpMethod = "PUT")
    @PostMapping(value = "/configItem/save")
    public ResponseData save(@RequestBody ConfigItemVo configItemVo) {
        try {
            configItemVo.setItemCode("SCHEDULE_CONFIG_ITEM");
            configItemVo.setConfigItemKey("codeRuleType");
            configItemService.save(configItemVo);
            return ResponseData.success(true);
        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    }

    /**
     * 查询配置项信息(codeRuleType)
     * @param configItemDto
     * @return
     * @throws CommonException
     */
    @PostMapping("/configItem/queryPage")
    @ApiOperation(value = "查询配置项信息")
    public ResponseData<IPage<ConfigItemDto>> queryPage(@RequestBody ConfigItemDto configItemDto) {
        try {
            configItemDto.setSite(UserUtils.getSite());
            configItemDto.setItemCode("SCHEDULE_CONFIG_ITEM");
            configItemDto.setConfigItemKey("codeRuleType");
            return configItemService.queryPage(configItemDto);
        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    };


}
