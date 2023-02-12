package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.ShopOrderTypeEnum;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.IProcessRouteFitProductGroupService;
import com.itl.mes.core.api.service.IProcessRouteFitProductService;
import com.itl.mes.core.api.service.IProcessRouteFitProductionLineService;
import com.itl.mes.core.provider.service.impl.ItemGroupServiceImpl;
import com.itl.mes.core.provider.service.impl.ItemServiceImpl;
import com.itl.mes.core.provider.service.impl.ProductLineServiceImpl;
import com.itl.mes.core.provider.service.impl.RouterServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Api(tags = "工艺路线设置新版")
@RestController
@RequestMapping("/processRouteFit")
public class RouterFitNewController {

    /**
     * -------------------------------------------工艺路线设置 新方式-------------------------------------------------------
     */
    @Autowired
    private IProcessRouteFitProductService productService; //产品工艺路线设置
    @Autowired
    private IProcessRouteFitProductGroupService productGroupService; //产品组工艺路线设置
    @Autowired
    private IProcessRouteFitProductionLineService productionLineService; //产线工艺路线设置
    @Autowired
    private RouterServiceImpl routerService; //工艺路线
    @Autowired
    private ItemServiceImpl itemService; //物料
    @Autowired
    private ItemGroupServiceImpl groupItemService;// 物料组
    @Autowired
    private ProductLineServiceImpl lineService; // 产线

    // ------------------------产品工艺路线设置--------------------------
    @PostMapping("/product/get")
    @ApiOperation("产品工艺路线设置-查询")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "routeBo", value = "工艺路线ID", dataType = "string"),
            @ApiImplicitParam(name = "routeCode", value = "工艺路线编码", dataType = "string"),
            @ApiImplicitParam(name = "routerName", value = "工艺路线名称", dataType = "string"),
//            @ApiImplicitParam(name = "itemBo", value = "物料ID", dataType = "string"),
            @ApiImplicitParam(name = "itemCode", value = "物料编码", dataType = "string"),
            @ApiImplicitParam(name = "itemName", value = "物料名称", dataType = "string"),
            @ApiImplicitParam(name = "orderType", value = "工单类型", dataType = "string"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "lang", required = true),
            @ApiImplicitParam(name = "size", value = "每页大小", dataType = "lang", required = true),

            @ApiImplicitParam(name = "routeCode", value = "返回字段: 工艺路线编码"),
            @ApiImplicitParam(name = "routerName", value = "返回字段: 工艺路线名称"),
            @ApiImplicitParam(name = "itemCode", value = "返回字段: 物料编码"),
            @ApiImplicitParam(name = "itemName", value = "返回字段: 物料名称"),
            @ApiImplicitParam(name = "id", value = "返回字段: 主键"),
            @ApiImplicitParam(name = "orderType", value = "返回字段: 工单类型"),
            @ApiImplicitParam(name = "createBy", value = "返回字段: 创建人"),
            @ApiImplicitParam(name = "createDate", value = "返回字段: 创建时间")
    })
    public ResponseData<IPage<Object>> getProductRoute(@RequestBody Map<String, Object> map) {

        return ResponseData.success(productService.getProductRoute(map));
    }

    @ApiOperation("产品工艺路线设置-保存")
    @PostMapping("/product/save")
    public ResponseData<Boolean> saveProductRoute(@RequestBody ProcessRouteFitProduct entity) {
        if (entity == null || StringUtils.isEmpty(entity.getRouteCode()) || StringUtils.isEmpty(entity.getItemCode()) || StringUtils.isEmpty(entity.getOrderType())) {
            return ResponseData.success(false, "参数缺失");
        }

        LambdaQueryWrapper<ProcessRouteFitProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRouteFitProduct::getItemCode, entity.getItemCode())
                .eq(ProcessRouteFitProduct::getOrderType, entity.getOrderType());

        List<ProcessRouteFitProduct> list = productService.list(wrapper);
        if (list != null && list.size() != 0) {
            return ResponseData.success(false, "数据已存在不能重复存储");
        }

        entity.setCreateDate(new Date());
        entity.setCreateBy(UserUtils.getUserName());
        entity.setSite(UserUtils.getSite());
        return ResponseData.success(productService.save(entity));
    }

    @ApiOperation("产品工艺路线设置-更新")
    @PutMapping("/product/update")
    public ResponseData<Boolean> updateProductRoute(@RequestBody ProcessRouteFitProduct entity) {
        boolean b = productService.updateById(entity);
        return ResponseData.success(b);
    }

    @ApiOperation("产品工艺路线设置-删除")
    @DeleteMapping("/product/delete")
    public ResponseData<Boolean> deleteProductRoute(@RequestBody List<String> idList) {
        return ResponseData.success(productService.removeByIds(idList));
    }

    @ApiOperation("产品工艺路线设置-excel导入")
    @PostMapping("product/import/excel")
    public ResponseData<Map<String, Object>> importByExcel(@RequestBody MultipartFile file) throws CommonException {
        List<ProcessRouteFitProduct> excelList = ExcelUtils.importExcel(file, 0, 1, ProcessRouteFitProduct.class);
        if (null == excelList || excelList.size() == 0) {
            throw new RuntimeException("excel中无数据");
        }

        String site = UserUtils.getSite();

        //router表的编码
        QueryWrapper<Router> lambdaWrapper = new QueryWrapper<>();
        lambdaWrapper.select("Distinct ROUTER as router").eq("SITE", site);
        List<Router> dbListRouter = routerService.list(lambdaWrapper);
//        Stream<Router> routerStream = dbListRouter.stream();

        //item表的编码
        QueryWrapper<Item> itemWrapper = new QueryWrapper<>();
        itemWrapper.select("Distinct ITEM as item").eq("SITE", site);
        List<Item> itemList = itemService.list(itemWrapper);
//        Stream<Item> itemStream = itemList.stream();

        ArrayList<ProcessRouteFitProduct> badData = new ArrayList<>();
        ArrayList<ProcessRouteFitProduct> yesData = new ArrayList<>();

        // 判断excel表格里的数据是否合法
        excelList.forEach(x -> {
            String excelRouteCode = x.getRouteCode();
            String itemCode = x.getItemCode();

            // 判断工艺路线编码是否在router表中
            boolean rb = dbListRouter.stream().anyMatch(router -> {
                return router.getRouter().equals(excelRouteCode);
            });

            // 判断物料编码是否在item表中
            boolean ib = itemList.stream().anyMatch(item -> {
                return item.getItem().equals(itemCode);
            });

            if (rb && ib) {
                yesData.add(x);
            } else {
                badData.add(x);
            }
        });

        // 判断process_route_fit_product表是否存在如果存在则覆盖。 天鲁了,好麻烦。
//        LambdaQueryWrapper<ProcessRouteFitProduct> wrapper = new LambdaQueryWrapper<>();
//        wrapper.select(ProcessRouteFitProduct::getRouteCode, ProcessRouteFitProduct::getItemCode, ProcessRouteFitProduct::getSite);
//        List<ProcessRouteFitProduct> list = productService.list(wrapper);
//        if (yesData.size() != 0) {
//        }


        String userName = UserUtils.getUserName();
        Date date = new Date();

        yesData.forEach(t -> {
            t.setCreateBy(userName);
            t.setCreateDate(date);
            t.setSite(site);
            if (t.getOrderType().equals(ShopOrderTypeEnum.NORMAL.getDesc())) {
                // 量产
                t.setOrderType(ShopOrderTypeEnum.NORMAL.getCode());
            } else if (t.getOrderType().equals(ShopOrderTypeEnum.TRIAL.getDesc())) {
                // 试产
                t.setOrderType(ShopOrderTypeEnum.TRIAL.getCode());
            } else if (t.getOrderType().equals(ShopOrderTypeEnum.REWORK.getDesc())) {
                // 返工
                t.setOrderType(ShopOrderTypeEnum.REWORK.getCode());
            }
        });
        productService.saveBatch(yesData);

        HashMap<String, Object> map = new HashMap<>();
        map.put("badData", badData.size());
        map.put("yesData", yesData.size());
        map.put("badDataItem", badData);
        return ResponseData.success(map, "错误数据");
    }

    // ------------------------产品组工艺路线设置--------------------------
    @PostMapping("/group/get")
    @ApiOperation("产品组工艺路线设置-查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeCode", value = "工艺路线编码", dataType = "string"),
            @ApiImplicitParam(name = "routerName", value = "工艺路线名称", dataType = "string"),
            @ApiImplicitParam(name = "itemGroup", value = "物料组编码", dataType = "string"),
            @ApiImplicitParam(name = "groupName", value = "物料组名", dataType = "string"),
            @ApiImplicitParam(name = "orderType", value = "工单类型", dataType = "string"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "lang", required = true),
            @ApiImplicitParam(name = "size", value = "每页大小", dataType = "lang", required = true),

            @ApiImplicitParam(name = "routeCode", value = "返回字段: 工艺路线编码"),
            @ApiImplicitParam(name = "routerName", value = "返回字段: 工艺路线名称"),
            @ApiImplicitParam(name = "itemGroup", value = "返回字段: 物料组编码"),
            @ApiImplicitParam(name = "groupName", value = "返回字段: 物料组名称"),
            @ApiImplicitParam(name = "id", value = "返回字段: 中间表主键"),
            @ApiImplicitParam(name = "orderType", value = "返回字段: 工单类型"),
            @ApiImplicitParam(name = "createBy", value = "返回字段: 创建人"),
            @ApiImplicitParam(name = "createDate", value = "返回字段: 创建时间")
    })
    public ResponseData<IPage<Object>> getGroupRoute(@RequestBody Map<String, Object> map) {
//        String itemGroup = (String) map.get("itemGroup");
//        if (!StringUtils.isEmpty(itemGroup)) {
//            ItemGroupHandleBO bo = new ItemGroupHandleBO(itemGroup);
//            map.put("itemGroup", bo.getItemGroup());
//        }

        return ResponseData.success(productGroupService.getGroupRoute(map));
    }

    @ApiOperation("产品组工艺路线设置-保存")
    @PostMapping("/group/save")
    public ResponseData<Boolean> saveGroupRoute(@RequestBody ProcessRouteFitProductGroup entity) {
        if (entity == null || StringUtils.isEmpty(entity.getRouteCode()) || StringUtils.isEmpty(entity.getItemGroup()) || StringUtils.isEmpty(entity.getOrderType())) {
            return ResponseData.success(false, "参数缺失");
        }

        LambdaQueryWrapper<ProcessRouteFitProductGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRouteFitProductGroup::getItemGroup, entity.getItemGroup())
                .eq(ProcessRouteFitProductGroup::getOrderType, entity.getOrderType());

        List<ProcessRouteFitProductGroup> list = productGroupService.list(wrapper);
        if (list != null && list.size() != 0) {
            return ResponseData.success(false, "数据已存在不能重复存储");
        }

        entity.setCreateDate(new Date());
        entity.setCreateBy(UserUtils.getUserName());
        entity.setSite(UserUtils.getSite());
        return ResponseData.success(productGroupService.save(entity));
    }

    @ApiOperation("产品组工艺路线设置-更新")
    @PutMapping("/group/update")
    public ResponseData<Boolean> updateProductGroupRoute(@RequestBody ProcessRouteFitProductGroup entity) {
        return ResponseData.success(productGroupService.updateById(entity));
    }

    @ApiOperation("产品组工艺路线设置-删除")
    @DeleteMapping("/group/delete")
    public ResponseData<Boolean> deleteProductGroupRoute(@RequestBody List<String> idList) {
        return ResponseData.success(productGroupService.removeByIds(idList));
    }

    @ApiOperation("产品组工艺路线设置-excel导入")
    @PostMapping("/group/import/excel")
    public ResponseData<Map<String, Object>> groupImport(@RequestBody MultipartFile file) throws CommonException {
        List<ProcessRouteFitProductGroup> excelList = ExcelUtils.importExcel(file, 0, 1, ProcessRouteFitProductGroup.class);
        if (null == excelList) {
            throw new RuntimeException("excel中无数据");
        }
        String site = UserUtils.getSite();

        //router表的编码
        QueryWrapper<Router> lambdaWrapper = new QueryWrapper<>();
        lambdaWrapper.select("Distinct ROUTER as router").eq("SITE", site);
        List<Router> dbListRouter = routerService.list(lambdaWrapper);
//        Stream<Router> routerStream = dbListRouter.stream();

        //ItemGroup表的编码
        QueryWrapper<ItemGroup> wrapper = new QueryWrapper<>();
        lambdaWrapper.select("Distinct ITEM_GROUP as itemGroup").eq("SITE", site);
        List<ItemGroup> dbGroupItem = groupItemService.list(wrapper);
//        Stream<ItemGroup> groupStream = dbGroupItem.stream();

        ArrayList<ProcessRouteFitProductGroup> badData = new ArrayList<>();
        ArrayList<ProcessRouteFitProductGroup> yesData = new ArrayList<>();

        // 判断excel表格里的数据是否合法
        excelList.forEach(x -> {
            String excelRouteCode = x.getRouteCode();
            String groupItem = x.getItemGroup();

            // 判断工艺路线编码是否在router表中
            boolean rb = dbListRouter.stream().anyMatch(router -> {
                return router.getRouter().equals(excelRouteCode);
            });

            // 判断物料编码是否在item表中
            boolean ib = dbGroupItem.stream().anyMatch(group -> {
                return group.getItemGroup().equals(groupItem);
            });

            if (rb && ib) {
                yesData.add(x);
            } else {
                badData.add(x);
            }
        });

        String userName = UserUtils.getUserName();
        Date date = new Date();
        yesData.forEach(t -> {
            t.setCreateBy(userName);
            t.setCreateDate(date);
            t.setSite(site);
            if (t.getOrderType().equals(ShopOrderTypeEnum.NORMAL.getDesc())) {
                // 量产
                t.setOrderType(ShopOrderTypeEnum.NORMAL.getCode());
            } else if (t.getOrderType().equals(ShopOrderTypeEnum.TRIAL.getDesc())) {
                // 试产
                t.setOrderType(ShopOrderTypeEnum.TRIAL.getCode());
            } else if (t.getOrderType().equals(ShopOrderTypeEnum.REWORK.getDesc())) {
                // 返工
                t.setOrderType(ShopOrderTypeEnum.REWORK.getCode());
            }
        });
        productGroupService.saveBatch(yesData);
        HashMap<String, Object> map = new HashMap<>();
        map.put("badData", badData.size());
        map.put("yesData", yesData.size());
        map.put("badDataItem", badData);

        return ResponseData.success(map, "错误数据");
    }


    // -------------------------产线工艺路线设置---------------------------
    @PostMapping("/productLine/get")
    @ApiOperation("产线工艺路线设置-查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeCode", value = "工艺路线编码", dataType = "string"),
            @ApiImplicitParam(name = "routerName", value = "工艺路线名称", dataType = "string"),
            @ApiImplicitParam(name = "productLine", value = "产线", dataType = "string"),
            @ApiImplicitParam(name = "productLineDesc", value = "产线描述", dataType = "string"),
            @ApiImplicitParam(name = "orderType", value = "工单类型", dataType = "string"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "lang", required = true),
            @ApiImplicitParam(name = "size", value = "每页大小", dataType = "lang", required = true),

            @ApiImplicitParam(name = "routeCode", value = "返回字段: 工艺路线编码"),
            @ApiImplicitParam(name = "routerName", value = "返回字段: 工艺路线名称"),
            @ApiImplicitParam(name = "productLine", value = "返回字段: 产线"),
            @ApiImplicitParam(name = "productLineDesc", value = "返回字段: 产线描述"),
            @ApiImplicitParam(name = "id", value = "返回字段: 中间表主键"),
            @ApiImplicitParam(name = "orderType", value = "返回字段: 工单类型"),
            @ApiImplicitParam(name = "createBy", value = "返回字段: 创建人"),
            @ApiImplicitParam(name = "createDate", value = "返回字段: 创建时间")
    })
    public ResponseData<IPage<Object>> getLineRoute(@RequestBody Map<String, Object> map) {
//        String productLine = (String) map.get("productLine");
//        if (!StringUtils.isEmpty(productLine)) {
//            ProductLineHandleBO bo = new ProductLineHandleBO(productLine);
//            map.put("productLine", bo.getProductLine());
//        }

        return ResponseData.success(productionLineService.getProductLineRoute(map));
    }

    @ApiOperation("产线工艺路线设置-保存")
    @PostMapping("/productLine/save")
    public ResponseData<Boolean> saveLineRoute(@RequestBody ProcessRouteFitProductionLine entity) {
        if (entity == null || StringUtils.isEmpty(entity.getRouteCode()) || StringUtils.isEmpty(entity.getProductLine()) || StringUtils.isEmpty(entity.getOrderType())) {
            return ResponseData.success(false, "参数缺失");
        }

        LambdaQueryWrapper<ProcessRouteFitProductionLine> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRouteFitProductionLine::getProductLine, entity.getProductLine())
                .eq(ProcessRouteFitProductionLine::getOrderType, entity.getOrderType());

        List<ProcessRouteFitProductionLine> list = productionLineService.list(wrapper);
        if (list != null && list.size() != 0) {
            return ResponseData.success(false, "数据已存在不能重复存储");
        }

        entity.setCreateDate(new Date());
        entity.setCreateBy(UserUtils.getUserName());
        entity.setSite(UserUtils.getSite());
        return ResponseData.success(productionLineService.save(entity));
    }

    @ApiOperation("产线工艺路线设置-更新")
    @PutMapping("/productLine/update")
    public ResponseData<Boolean> updateLineRoute(@RequestBody ProcessRouteFitProductionLine entity) {
        return ResponseData.success(productionLineService.updateById(entity));
    }

    @ApiOperation("产线工艺路线设置-删除")
    @DeleteMapping("/productLine/delete")
    public ResponseData<Boolean> deleteLineRoute(@RequestBody List<String> idList) {
        return ResponseData.success(productionLineService.removeByIds(idList));
    }

    @ApiOperation("产线工艺路线设置-excel导入")
    @PostMapping("/productLine/import/excel")
    public ResponseData<Map<String, Object>> productLineImport(@RequestBody MultipartFile file) throws CommonException {
        List<ProcessRouteFitProductionLine> excelList = ExcelUtils.importExcel(file, 0, 1, ProcessRouteFitProductionLine.class);
        if (null == excelList) {
            throw new RuntimeException("excel中无数据");
        }
        String site = UserUtils.getSite();

        //router表的编码
        QueryWrapper<Router> lambdaWrapper = new QueryWrapper<>();
        lambdaWrapper.select("Distinct ROUTER as router").eq("SITE", site);
        List<Router> dbListRouter = routerService.list(lambdaWrapper);

        // 产线
        QueryWrapper<ProductLine> lineWrapper = new QueryWrapper<>();
        lambdaWrapper.select("Distinct PRODUCT_LINE as productLine").eq("SITE", site);
        List<ProductLine> line = lineService.list(lineWrapper);

        ArrayList<ProcessRouteFitProductionLine> badData = new ArrayList<>();
        ArrayList<ProcessRouteFitProductionLine> yesData = new ArrayList<>();

        String userName = UserUtils.getUserName();
        Date date = new Date();
        // 判断excel表格里的数据是否合法
        excelList.forEach(x -> {
            String excelRouteCode = x.getRouteCode();
            String productLine = x.getProductLine();

            // 判断工艺路线编码是否在router表中
            boolean rb = dbListRouter.stream().anyMatch(router -> {
                return router.getRouter().equals(excelRouteCode);
            });

            // 判断物料编码是否在item表中
            boolean ib = line.stream().anyMatch(l -> {
                return l.getProductLine().equals(productLine);
            });

            if (rb && ib) {
                yesData.add(x);
            } else {
                badData.add(x);
            }
        });


        yesData.forEach(t -> {
            t.setCreateBy(userName);
            t.setCreateDate(date);
            t.setSite(site);
            if (t.getOrderType().equals(ShopOrderTypeEnum.NORMAL.getDesc())) {
                // 量产
                t.setOrderType(ShopOrderTypeEnum.NORMAL.getCode());
            } else if (t.getOrderType().equals(ShopOrderTypeEnum.TRIAL.getDesc())) {
                // 试产
                t.setOrderType(ShopOrderTypeEnum.TRIAL.getCode());
            } else if (t.getOrderType().equals(ShopOrderTypeEnum.REWORK.getDesc())) {
                // 返工
                t.setOrderType(ShopOrderTypeEnum.REWORK.getCode());
            }
        });
        productionLineService.saveBatch(yesData);
        HashMap<String, Object> map = new HashMap<>();
        map.put("badData", badData.size());
        map.put("yesData", yesData.size());
        map.put("badDataItem", badData);

        return ResponseData.success(map, "错误数据");
    }


}
