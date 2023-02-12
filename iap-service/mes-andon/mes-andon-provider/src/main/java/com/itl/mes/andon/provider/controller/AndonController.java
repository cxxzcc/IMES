package com.itl.mes.andon.provider.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.andon.api.dto.AndonQueryDTO;
import com.itl.mes.andon.api.dto.AndonSaveDTO;
import com.itl.mes.andon.api.entity.Record;
import com.itl.mes.andon.api.service.AndonService;
import com.itl.mes.andon.api.service.RecordService;
import com.itl.mes.andon.api.vo.AndonVo;
import com.itl.mes.andon.provider.mapper.AndonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/andon")
@Api(tags = "安灯表")
public class AndonController {

    @Autowired
    AndonService andonService;

    @Autowired
    RecordService recordService;
    @Autowired
    AndonMapper andonMapper;

    @PutMapping("/save")
    @ApiOperation(value = "保存安灯数据")
    public ResponseData save(@RequestBody AndonSaveDTO andonSaveDTO) {
        andonService.save(andonSaveDTO);
        return ResponseData.success(true);
    }


    @DeleteMapping("/delete")
    @ApiOperation(value = "删除安灯数据")
    public ResponseData delete(@RequestBody List<String> ids) {
        andonService.deleteByIds(ids);
        return ResponseData.success(true);
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询安灯数据列表")
    public ResponseData<IPage<AndonVo>> findList(@RequestBody AndonQueryDTO andonQueryDTO) {
        andonQueryDTO.setSite(UserUtils.getSite());
        return ResponseData.success(andonService.findList(andonQueryDTO));
    }


    @GetMapping("/findById")
    @ApiOperation(value = "根据ID查询安灯数据")
    public ResponseData<AndonVo> findById(String id) {

        return ResponseData.success(andonService.findById(id));

    }

    @GetMapping("/findByLine")
    @ApiOperation(value = "根据产线查询所有的异常")
    public List<Record> findByLine(@RequestParam("line") String line) {

        return andonService.findByLine(line);

    }

    /**
     * 安灯触发记录查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "findRecord")
    public ResponseData<IPage<Record>> findRecord(@RequestBody Record record) {
        record.setSite(UserUtils.getSite());
        IPage<Record> page = andonMapper.findPage(record.getPage() == null ? new Page<>(0, 10) : record.getPage(), record);
        List<Record> list = page.getRecords();
        if (null != list) {
            list.forEach(x -> x.setAndonType(x.getAndonType().split(",")[1]));

            List<Record> records = list.stream().filter(x -> (x.getTriggerTime() != null && x.getReceiveTime() != null) || (x.getTriggerTime() != null && x.getRepairTime() != null)).collect(Collectors.toList());

            records.forEach(x -> {
                Date triggerTime = x.getTriggerTime();// 触发时刻
                Date receiveTime = x.getReceiveTime(); // 受理时刻
                Date repairTime = x.getRepairTime(); // 维修时刻

                if (triggerTime != null && receiveTime != null) {
                    x.setReceivePeriodTime(DateUtil.formatBetween(triggerTime, receiveTime));
                }

                if (triggerTime != null && repairTime != null) {
                    x.setCompletePeriodTime(DateUtil.formatBetween(triggerTime, repairTime));
                }
            });
        }
        return ResponseData.success(page);
    }

    @PostMapping("/listForUse")
    @ApiOperation(value = "查询可用的安灯数据列表")
    public ResponseData<IPage<AndonVo>> findListForUse(@RequestBody AndonQueryDTO andonQueryDTO) {

        return ResponseData.success(andonService.findListForUse(andonQueryDTO));

    }
}
