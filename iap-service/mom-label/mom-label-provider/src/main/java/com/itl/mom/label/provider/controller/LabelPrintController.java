package com.itl.mom.label.provider.controller;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.StringUtils;
import com.itl.iap.mes.client.service.FileUploadService;
import com.itl.mom.label.api.dto.label.*;
import com.itl.mom.label.api.dto.ruleLabel.LabelPrintBarCodeDto;
import com.itl.mom.label.api.entity.label.LabelPrint;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.api.enums.LabelPrintLogStateEnum;
import com.itl.mom.label.api.service.label.LabelPrintLogService;
import com.itl.mom.label.api.service.label.LabelPrintService;
import com.itl.mom.label.api.service.label.SnService;
import com.itl.mom.label.api.vo.*;
import com.itl.mom.label.provider.constant.ElementConstant;
import com.itl.mom.label.provider.mapper.label.LabelPrintMapper;
import com.itl.mom.label.provider.utils.excel.ShopOrderSnVerifyHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author liuchenghao
 * @date 2021/1/26 15:34
 */
@Api(tags = "标签打印控制层")
@RestController
@RequestMapping("/labelPrintRange")
public class LabelPrintController {

    @Autowired
    LabelPrintService labelPrintService;

    @Autowired
    LabelPrintMapper labelPrintMapper;

    @Autowired
    SnService snService;

    @Autowired
    LabelPrintLogService labelPrintLogService;

    @Value("${file.path}")
    private String filePath;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private ShopOrderSnVerifyHandler shopOrderSnVerifyHandler;


    @PostMapping("/addLabelPrint")
    @ApiOperation(value = "添加标签打印任务")
    public ResponseData<Integer> addLabelPrint(@RequestBody LabelPrintSaveDto labelPrintSaveDto) throws CommonException {
        try {
            return ResponseData.success(labelPrintService.addLabelPrint(labelPrintSaveDto).size());
        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询标签打印集合")
    public ResponseData<IPage<LabelPrintVo>> findList(@RequestBody LabelPrintQueryDto labelPrintQueryDto) throws CommonException {

        return ResponseData.success(labelPrintService.findList(labelPrintQueryDto));
    }


    @PostMapping("/barCodePrint")
    @ApiOperation(value = "条码打印")
    public ResponseData barCodePrint(@RequestBody LabelPrintBarCodeDto barCodeDto) {

        try {
            return ResponseData.success(labelPrintService.barCodePrint(barCodeDto));
        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    }

    @PostMapping("/detailList")
    @ApiOperation(value = "查询标签详细打印集合")
    public ResponseData<IPage<SnVo>> findDetailList(@RequestBody SnQueryDto labelPrintRangeDetailQueryDto) throws CommonException {

        return ResponseData.success(snService.findList(labelPrintRangeDetailQueryDto));
    }

    @PostMapping("/logList")
    @ApiOperation(value = "查询标签详细log集合")
    public ResponseData<IPage<LabelPrintLogVo>> findLogList(@RequestBody SnQueryDto snQueryDto) throws CommonException {
        return ResponseData.success(snService.findLog(snQueryDto));
    }

    @PostMapping("/findProductStatus")
    @ApiOperation(value = "查询ProductStatus集合")
    public ResponseData<IPage<MeProductStatusVo>> findProductStatus(@RequestBody SnQueryDto snQueryDto) throws CommonException {
        snQueryDto.setSite(UserUtils.getSite());
        JSONObject jsonObject = JSONUtil.parseObj(snQueryDto);
        StringUtils.replaceMatchTextByMap(jsonObject);
        return ResponseData.success(snService.findProductStatus(JSONUtil.toBean(jsonObject, SnQueryDto.class)));
    }


    @PostMapping("/barCodeDetailPrint")
    @ApiOperation(value = "条码详细打印")
    public ResponseData barCodeDetailPrint(@RequestBody LabelPrintBarCodeDto barCodeDto) {

        return ResponseData.success(snService.barCodeDetailPrint(barCodeDto.getBo(), barCodeDto.getPrintCount(), barCodeDto.getPrinter()));
    }


    @PostMapping("/labelPrintDetailLogList")
    @ApiOperation(value = "条码详细打印日志信息")
    public ResponseData<List<LabelPrintLogVo>> labelPrintDetailLogList(@RequestBody String labelPrintDetailBo) {

        return ResponseData.success(labelPrintLogService.findDetailList(labelPrintDetailBo));
    }


    @GetMapping("/scanReturn")
    @ApiOperation(value = "扫描条码带出信息，返回对应的BO和物料BO，和数量")
    public ResponseData<ScanReturnVo> scanReturn(@RequestParam("barCode") String barCode, @RequestParam("elementType") String elementType) {
        return labelPrintService.scanReturn(barCode, elementType);
    }

    @PostMapping("/updateLabelPrintDetail")
    @ApiOperation(value = "更新标签数量")
    public ResponseData updateLabelPrintDetail(@RequestBody Sn sn) {
        snService.updateById(sn);
        return ResponseData.success();
    }

    @GetMapping("/getSn")
    @ApiOperation(value = "通过snBo查询sn条码")
    public ResponseData getSn(@RequestParam("bo") String bo) {
        labelPrintMapper.getSn(bo);
        return ResponseData.success();
    }


    @GetMapping("/getLabelPrintBo")
    ResponseData<LabelPrint> getLabelPrintBo(@RequestParam("labelPrintBo") String labelPrintBo) {
        try {
            return labelPrintService.getLabelPrintBo(labelPrintBo);

        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    }

    @GetMapping("/queryCodeByItem")
    ResponseData<List<String>> queryCodeByItem(@RequestParam("itemCode") List<String> itemCode) {
        try {
            return labelPrintService.queryCodeByItem(itemCode);
        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    }

    @GetMapping("/queryItemBySn")
    ResponseData<String> queryItemBySn(@RequestParam("sn") String sn) {
        try {
            return labelPrintService.queryItemBySn(sn);
        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    }

    /**
     * SN条码导入
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "importFile", notes = "导入")
    @PostMapping("/sn/import")
    public ResponseData<Map<String, Object>> importShopOrderAndSn(MultipartFile file) throws IOException {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setTitleRows(0);
        params.setVerifyHandler(shopOrderSnVerifyHandler);
        Map<String, Object> map = ExcelUtils.importExcel(
                file.getInputStream(),
                ShopOrderSnImportDto.class,
                params,
                filePath,
                labelPrintService::saveByImport,
                fileUploadService::uploadSingle
        );
        return ResponseData.success(map);
    }

    @ApiOperation(value = "download", notes = "下载模板", httpMethod = "GET")
    @GetMapping(value = "/sn/download")
    public String download(HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            in = (new ClassPathResource("templates/orderSnCode.xls")).getInputStream();
            out = response.getOutputStream();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename=工单条码导入模板.xls");
            response.setContentLength(in.available());
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var15) {
                    ;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException var14) {
                }
            }
        }


        return "";
    }


    /**
     * 在线打印
     * */
    @ApiOperation(value = "在线打印")
    @PostMapping(value = "/productLine/print")
    public ResponseData<LabelPrintResponseDTO> inProductionLinePrint(@RequestBody LabelInProductLinePrintDTO labelInProductLinePrintDTO) {
        return ResponseData.success(labelPrintService.inProductionLinePrint(labelInProductLinePrintDTO));
    }

    /**
     * 补打
     * */
    @ApiOperation(value = "补打")
    @PostMapping(value = "/productLine/additional/print")
    public ResponseData<List<LabelPrintResponseDTO>> additionalPrint(@RequestBody LabelInProductLinePrintDTO labelInProductLinePrintDTO) {
        Map<String, Object> map = labelPrintService.additionalPrint(labelInProductLinePrintDTO);
        ResponseData<List<LabelPrintResponseDTO>> responseData = ResponseData.success((List<LabelPrintResponseDTO>) map.get("result"));
        responseData.setMsg((String) map.get("msg"));
        return responseData;
    }

    /**
     * 工位打印记录
     * */
    @ApiOperation(value = "工位打印记录")
    @GetMapping(value = "/station/printLog")
    public ResponseData<IPage<LabelPrintLogVo>> getPrintLogByStation() {
        SnQueryDto snQueryDto = new SnQueryDto();
        snQueryDto.setStation(UserUtils.getStation());
        snQueryDto.setElementType(ElementConstant.SHOP_ORDER);
        Page page = new Page<>(1, 20);
        snQueryDto.setPage(page);
        List<String> typeList = Arrays.asList(LabelPrintLogStateEnum.ADDITIONAL.getCode(), LabelPrintLogStateEnum.ON_PRODUCT_LINE.getCode());
        snQueryDto.setTypeList(typeList);
        return ResponseData.success(snService.findLog(snQueryDto));
    }

}
