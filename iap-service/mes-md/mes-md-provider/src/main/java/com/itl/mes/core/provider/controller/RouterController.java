package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.dto.RouterDataDTO;
import com.itl.mes.core.api.dto.RouterProcessDataDTO;
import com.itl.mes.core.api.entity.OrderRouter;
import com.itl.mes.core.api.entity.Router;
import com.itl.mes.core.api.entity.RouterProcess;
import com.itl.mes.core.api.service.OrderRouterService;
import com.itl.mes.core.api.service.RouterService;
import com.itl.mes.core.api.service.ShopOrderService;
import com.itl.mes.core.api.service.SnService;
import com.itl.mes.core.api.vo.RouterAsSaveVo;
import com.itl.mes.core.api.vo.RouterVo;
import com.itl.mes.core.provider.listener.RouterDataListener;
import com.itl.mes.core.provider.listener.RouterProcessDataListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 工艺路线
 *
 * @author linjl
 * @since 2021-01-28
 */
@RestController
@RequestMapping("/router")
@Api(tags = " 工艺路线")
public class RouterController {
    private final Logger logger = LoggerFactory.getLogger(RouterController.class);

    @Autowired
    private SnService snService;

    @Autowired
    public RouterService routerService;

    @Resource
    private ShopOrderService shopOrderService;
    @Autowired
    public OrderRouterService orderRouterService;

    /**
     * 根据router查询
     *
     * @param router
     * @return RestResult<Router>
     */
    @GetMapping("/query")
    @ApiOperation(value = "查找工艺路线")
    @ApiImplicitParam(name = "router", value = "工艺路线", dataType = "string", paramType = "query")
    public ResponseData<Router> queryRouter(@RequestParam String router) throws CommonException {
        return ResponseData.success(routerService.getRouter(router));
    }

    @GetMapping("/get")
    @ApiOperation(value = "工单查找工艺路线")
    @ApiImplicitParam(name = "shopOrder", value = "工单", dataType = "string", paramType = "query")
    public ResponseData getRouter(@RequestParam String shopOrder, @RequestParam String sn) throws CommonException {
        final String bo = new ShopOrderHandleBO(UserUtils.getSite(), shopOrder).getBo();
        final String bo1 = new SnHandleBO(UserUtils.getSite(), sn).getBo();
        final OrderRouter orderRouter = orderRouterService.getOrderRouter(bo);
        if (orderRouter!=null) {
            final Router router = new Router();
            final RouterProcess routerProcess = new RouterProcess();
            routerProcess.setProcessInfo(orderRouter.getOrderRouterProcess().getProcessInfo());
            router.setRouterProcess(routerProcess);
            return ResponseData.success(router);
        }

        final String routerBo = shopOrderService.getById(bo).getRouterBo();
        final Router router = routerService.getRouter(routerBo);
        if (router==null) {
            throw new RuntimeException("没有工艺路线数据");
        }
        router.setBo("");
        return ResponseData.success(router);
    }


    /**
     * 保存工艺路线
     *
     * @param routerVo
     * @return RestResult<Router>
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存工艺路线")
    public ResponseData<Router> saveRouter(@RequestBody RouterVo routerVo) throws Exception {
        routerService.saveRouter(routerVo);
        return ResponseData.success(routerService.getRouter(routerVo.getBo()));
    }

    /**
     * 复制保存新工艺路线
     *
     * @param routerAsSaveVo
     * @return RestResult<Router>
     */
    @PostMapping("/saveAsRouter")
    @ApiOperation(value = "复制保存新工艺路线")
    public ResponseData<Router> saveAsRouter(@RequestBody RouterAsSaveVo routerAsSaveVo) {
        try {
            Router router = routerService.saveAsRouter(routerAsSaveVo);
            return ResponseData.success(router);
        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    }

    /**
     * 删除工艺路线
     *
     * @param bo 工艺路线
     * @return RestResult<String>
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除工艺路线")
    @ApiImplicitParam(name = "bo", value = "工艺路线bo", dataType = "string", paramType = "query")
    public ResponseData<String> deleteRouter(String bo) throws CommonException {
        if (StrUtil.isBlank(bo)) {
            throw new CommonException("工艺路线BO不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        Router router = new Router();
        router.setBo(bo);
        routerService.deleteRouter(router);
        return ResponseData.success("success");
    }

    @ApiOperation(value = "importFile", notes = "导入")
    @PostMapping("/importFile")
    public ResponseData importFile(@RequestParam("file") MultipartFile file) throws Exception {
        // 读取部分sheet
        ExcelReader excelReader = null;
        try {
            List<Router> list = routerService.list();
            Map<String, Router> routerMap = list.stream().collect(Collectors.toMap(Router::getBo, Function.identity()));
            excelReader = EasyExcel.read(file.getInputStream()).build();
            RouterDataListener routerDataListener = new RouterDataListener();
            routerDataListener.setRouterMap(routerMap);
            RouterProcessDataListener routerProcessDataListener = new RouterProcessDataListener();
            ReadSheet readSheet1 = EasyExcel.readSheet(0).head(RouterDataDTO.class)
                    .registerReadListener(routerDataListener).build();
            ReadSheet readSheet2 = EasyExcel.readSheet(1).head(RouterProcessDataDTO.class)
                    .registerReadListener(routerProcessDataListener).build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1, readSheet2);
            List<Map> errorList = routerDataListener.getErrorList();
            List<Map> errorList1 = routerProcessDataListener.getErrorList();
            errorList.addAll(errorList1);
            if (errorList.size() > 0) {
                return ResponseData.success(errorList);
            } else {
                routerService.importFile(routerDataListener.getCachedDataList(),
                        routerProcessDataListener.getCachedDataList());
            }
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
        return ResponseData.success();
    }


    @ApiOperation(value = "download", notes = "下载模板", httpMethod = "GET")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response) {
        try (
                InputStream in = (new ClassPathResource("templates/router_v1.xls")).getInputStream();
                ServletOutputStream out = response.getOutputStream();
        ) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=router.xls");
            response.setContentLength(in.available());
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
