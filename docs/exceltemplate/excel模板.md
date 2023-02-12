### excel模板说明

excel导入导出如果需要模板可以放到指定目录下
> ${file.path} + /excel/{文件名}

demo: /home/excel/deviceImportTemplate.xls

获取下载url： /api/mes/excel/template/deviceImport

需要在枚举中定义

```java
com.itl.iap.common.base.constants.ExcelTemplateEnum
```


此文件夹下的 excel 文件夹 用于备份excel模板，如模板有修改需要及时更新。