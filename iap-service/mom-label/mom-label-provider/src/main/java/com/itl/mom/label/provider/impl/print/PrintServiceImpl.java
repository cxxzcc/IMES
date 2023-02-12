package com.itl.mom.label.provider.impl.print;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mom.label.api.entity.print.Printer;
import com.itl.mom.label.provider.mapper.print.PrintMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PrintServiceImpl {

    @Autowired
    private PrintMapper printMapper;


    public IPage<Printer> findList(String printerName, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum,pageSize);
        return printMapper.findList(page, printerName);
    }


}
