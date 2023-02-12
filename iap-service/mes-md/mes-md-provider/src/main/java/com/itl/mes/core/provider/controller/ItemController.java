package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.dto.ItemQueryDto;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.ItemGroupService;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.vo.ItemAndCustomDataValVo;
import com.itl.mes.core.api.vo.ItemFullVo;
import com.itl.mes.core.provider.mapper.ItemMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author space
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/items")
@Api(tags = " 物料表")
public class ItemController {
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public ItemService itemService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    public ItemGroupService itemGroupService;

    @Autowired
    public CustomDataValService customDataValService;

    @GetMapping("/selectItemGroup")
    @ApiOperation(value = "页面打开时触发，显示物料组数据")
    public ResponseData<List<String>> selectItemGroupList() throws CommonException {
        return ResponseData.success(itemGroupService.selectItemGroupListBySite());
    }

    /**
     * 根据item查询
     *
     * @param item 物料
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "根据物料和版本查询物料数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "item", value = "物料", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "版本", dataType = "string", paramType = "query")
    })
    public ResponseData<ItemFullVo> getItemByItemAndVersion(String item, String version) throws CommonException {
        if (StrUtil.isBlank(item) || StrUtil.isBlank(version)) {
            throw new CommonException("物料和版本不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        ItemFullVo itemFullVo = itemService.getItemFullVoByItemAndVersion(item, version);
        return ResponseData.success(itemFullVo);
    }

    /**
     * forFeign 查询全部
     *
     * @return
     */
    @GetMapping("/getAll")
    public List<Item> getAll() {
        return itemService.list(new QueryWrapper<Item>().lambda().isNotNull(Item::getBo));
    }

    /**
     * author: liKun
     * for feign
     * 根据主键集合查询Item
     */
    @PostMapping("/getByIds")
    public Map<String, String> getByIds(@RequestBody Set<String> bos) {
        List<Item> items = itemMapper.selectBatchIds(bos);
        if (null != items) {
            return items.stream().collect(Collectors.toMap(Item::getBo, Item::getItemName));
        }
        return new HashMap<String, String>();
    }

    /**
     * liKun
     * 根据Bom清单查询Item
     */
    @ApiOperation(value = "根据Bom清单查询数据")
    @PostMapping("/getByBom")
    public List<Item> getByIds(@RequestBody String bom) {
        return itemMapper.selectList(new QueryWrapper<Item>().eq("BOM_BO", bom));
    }

    /**
     * liKun
     * 根据itemBO获取ITEM_UNIT(单位BO)
     */
    @ApiOperation(value = "根据itemBO获取ITEM_UNIT(单位BO)")
    @PostMapping("/getItemUnit")
    public Map<String, String> getItemUnit(@RequestBody Set<String> bos) {
        List<Item> items = itemMapper.selectBatchIds(bos);
        Map<String, String> collect = items.stream().filter(item -> item.getItemUnit() != null).collect(Collectors.toMap(Item::getBo, Item::getItemUnit));
        return collect;
    }

    /**
     * author: zhongfei
     * for feign
     * 根据主键集合查询Item
     */
    @PostMapping("/getItemList")
    public List<Item> getItemList(@RequestBody Set<String> bos) {
        List<Item> items = itemMapper.selectBatchIds(bos);
        return items;
    }

    /**
     * 保存物料
     *
     * @param itemFullVo 前端物料相关数据
     * @return RestResult<ItemFullVo>
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存物料数据")
    public ResponseData<ItemFullVo> saveItem(@RequestBody ItemFullVo itemFullVo) throws CommonException {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(itemFullVo); //验证数据是否合规
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        itemService.saveItem(itemFullVo);
        itemFullVo = itemService.getItemFullVoByItemAndVersion(itemFullVo.getItem(), itemFullVo.getVersion());
        return ResponseData.success(itemFullVo);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除物料数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "item", value = "物料", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "版本", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "modifyDate", value = "修改时间", dataType = "string", paramType = "query")
    })
    public ResponseData<String> deleteItem(String item, String version, String modifyDate) throws CommonException {
        if (StrUtil.isBlank(item) || StrUtil.isBlank(version)) {
            throw new CommonException("物料和版本不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (StrUtil.isBlank(modifyDate)) {
            throw new CommonException("修改时间不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        itemService.deleteItem(item, version, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success("success");
    }


    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportItem(String site, HttpServletResponse response) throws CommonException {
        itemService.exportItem(site, response);
    }


    @RequestMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("FileName", file.getOriginalFilename());
        //POIUtils.checkFile(file);
        List<ItemFullVo> itemFullVoList = ExcelUtils.importExcel(file, 1, 1, ItemFullVo.class);
        for (ItemFullVo itemFullVo : itemFullVoList) {
            itemService.saveItem(itemFullVo);
        }
        return ResponseData.success("success");
    }


    @PostMapping("/getItemByTerm")
    @ApiOperation(value = "根据条件查询物料")
    public List<Item> getItemByTerm(@RequestBody ItemQueryDto itemQueryDto) {

        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site", itemQueryDto.getSite());
        if (StrUtil.isNotBlank(itemQueryDto.getItem())) {
            queryWrapper.like("item", itemQueryDto.getItem());
        }
        if (StrUtil.isNotBlank(itemQueryDto.getItemName())) {
            queryWrapper.like("item_name", itemQueryDto.getItemName());
        }
        return itemMapper.selectList(queryWrapper);
    }


    @PostMapping("/getItemAndCustomDataVal")
    @ApiOperation(value = "获取物料的字段值和自定义字段值")
    public List<ItemAndCustomDataValVo> getItemAndCustomDataVal(@RequestBody Set<String> bos) {

        return itemService.getItemAndCustomDataVal(bos);

    }


}
