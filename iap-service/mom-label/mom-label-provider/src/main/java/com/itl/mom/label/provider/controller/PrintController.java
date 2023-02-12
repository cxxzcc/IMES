package com.itl.mom.label.provider.controller;


import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.provider.impl.print.PrintServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "打印机管理")
@RestController
@RequestMapping("/m/print")
public class PrintController extends BaseController {

    @Autowired
    private PrintServiceImpl printService;



    @ApiOperation(value = "list", notes = "查询所有打印机", httpMethod = "POST")
    @PostMapping(value = "/list")
    public ResponseData queryPage(@RequestParam(value = "printerName",required = false) String printerName, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return ResponseData.success(printService.findList(printerName,page,pageSize));
    }


}