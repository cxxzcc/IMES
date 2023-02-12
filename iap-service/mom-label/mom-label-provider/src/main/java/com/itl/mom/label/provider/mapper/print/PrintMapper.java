package com.itl.mom.label.provider.mapper.print;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mom.label.api.entity.print.Printer;
import org.apache.ibatis.annotations.Param;

public interface PrintMapper extends BaseMapper<Printer> {

    IPage<Printer> findList(Page page, @Param("printerName") String printerName);
}
