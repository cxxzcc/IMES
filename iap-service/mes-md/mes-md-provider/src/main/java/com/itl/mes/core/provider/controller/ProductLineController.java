package com.itl.mes.core.provider.controller;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.mes.client.service.FileUploadService;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.dto.ProductLineDto;
import com.itl.mes.core.api.entity.ProductLine;
import com.itl.mes.core.api.service.ProductLineService;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.api.vo.ProductLineQueryVo;
import com.itl.mes.core.api.vo.ProductLineVo;
import com.itl.mes.core.provider.utils.excel.ProducLineVerifyHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author space
 * @since 2019-05-24
 */
@RestController
@RequestMapping("/productLines")
@Api(tags = " 产线表" )
public class ProductLineController {
    private final Logger logger = LoggerFactory.getLogger(ProductLineController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public ProductLineService productLineService;

    @Autowired
    private ProducLineVerifyHandler productLineVerifyHandler;

    @Autowired
    private FileUploadService fileUploadService;

    /**
    * 根据productLine查询
    *
    * @param productLine 产线
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value="通过产线查询数据")
    @ApiImplicitParam(name="productLine",value="产线",dataType="string", paramType = "query")
    public ResponseData<ProductLineVo> getProductLineByProductLine(String productLine ) throws CommonException {
        ProductLineVo productLineVo = productLineService.getProductLineByProductLine( productLine );
        return ResponseData.success(productLineVo);
    }


    /**
     * 保存产线数据
     *
     * @param productLineVo
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value="保存产线数据")
    public ResponseData<ProductLineVo> saveProductLine( @RequestBody ProductLineVo productLineVo ) throws CommonException {

        if( StrUtil.isBlank( productLineVo.getProductLine() ) ){
            throw new CommonException( "产线参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        productLineService.saveProductLine( productLineVo );
        productLineVo = productLineService.getProductLineByProductLine( productLineVo.getProductLine() );
        return ResponseData.success(productLineVo);
    }


    @GetMapping( "/delete" )
    @ApiOperation(value="删除产线数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="productLine",value="产线",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="modifyDate",value="修改时间",dataType="string", paramType = "query")
    })
    public ResponseData<String> deleteProductLineByProductLine(String productLine, String modifyDate ) throws CommonException {
        if( StrUtil.isBlank( productLine ) ){
            throw new CommonException( "产线不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION  );
        }
        if( StrUtil.isBlank( modifyDate ) ){
            throw new CommonException( "修改时间不能为空",CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        productLineService.deleteProductLineByProductLine( productLine, DateUtil.parse( modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS ) );
        return  ResponseData.success( "success" );
    }

    @PostMapping("/listByWorkShop")
    @ApiOperation(value = "根据车间查询车间下的产线")
    public ResponseData<List<ProductLine>> listByWorkShop(@RequestBody ProductLineQueryVo queryVo) {
        return ResponseData.success(productLineService.listByWorkShop(queryVo));
    }



    @ApiOperation(value = "export", notes = "导出模板", httpMethod = "GET")
    @GetMapping(value = "/export")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        productLineService.export(request, response);
    }

    @Value("${file.path}")
    private String filePath;
    @ApiOperation(value = "importFile", notes = "导入")
    @PostMapping("/importFile")
    public ResponseData importFile(@RequestParam("file") MultipartFile file) throws IOException {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setTitleRows(0);
        params.setVerifyHandler(productLineVerifyHandler);
        Map<String, Object> map = ExcelUtils.importExcel(
                file.getInputStream(),
                ProductLineVo.class,
                params,
                filePath,
                productLineService::saveByImport,
                fileUploadService::uploadSingle
        );
        return ResponseData.success(map);
    }


    @ApiOperation(value = "download", notes = "下载模板", httpMethod = "GET")
    @GetMapping(value = "/download")
    public String download(HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            in = (new ClassPathResource("templates/productLine.xls")).getInputStream();
            out = response.getOutputStream();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename=产线维护导入模板.xls");
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

}
