com.alibaba.excel.exception.ExcelDataConvertException: Can not find 'Converter' support class Timestamp.

```shell
EasyExcel有些不会自动处理的类型,常见于：某些日期类型,以及像BigInteger这种'模拟型'数值类型。需要查询时用sql转,或者使用EasyExcel内置的Converter转。
以m_item表为例日期转为字符串：
select
	bo,
	site,
	version,
	is_current_version,
	item_name,
	item_desc,
	item_unit,
	item_state_bo,
	item_type,
	router_bo,
	bom_bo,
	CONVERT ( VARCHAR ( 20 ), create_date, 120 ) create_date,
	CONVERT ( VARCHAR ( 20 ), modify_date, 120 ) modify_date,
	modify_user,
	zs_type 
FROM
	m_item