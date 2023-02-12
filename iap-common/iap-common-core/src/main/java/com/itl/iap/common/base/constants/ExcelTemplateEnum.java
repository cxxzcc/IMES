package com.itl.iap.common.base.constants;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * excel模板枚举定义
 * @author dengou
 * @date 2021/11/3
 */
@Getter
@AllArgsConstructor
public enum ExcelTemplateEnum {

    /**
     * 设备导入模板
     * */
    DEVICE_IMPORT_TEMPLATE("deviceImport", "excel/deviceImport.xls"),
    /**
     * 备件导入模板
     * */
    SPART_PART_IMPORT_TEMPLATE("sparePartImportTemplate", "excel/sparePartImportTemplate.xls"),

    ;
    /**
     * 名称，用于寻找下载地址
     * */
    private String name;
    /**
     * 文件地址
     * */
    private String filePath;


    /**
     * 根据code转换， 没找到对应code会返回null,需要注意判空
     * */
    public static ExcelTemplateEnum parseByCode(String code) {
        return Arrays.stream(ExcelTemplateEnum.values())
                .filter(e-> StrUtil.equals(e.getName(), code)).findFirst().orElse(null);
    }

    /**
     * 根据name获取filePath
     * */
    public static String getPathByCode(String code) {
        Optional<ExcelTemplateEnum> first = Arrays.stream(ExcelTemplateEnum.values())
                .filter(e -> StrUtil.equals(e.getName(), code)).findFirst();
        if(first.isPresent()) {
            return first.get().getFilePath();
        }
        return null;
    }


}
