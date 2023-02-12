package com.itl.iap.common.util;

import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 字符串工具类
 * @author dengou
 * @date 2021/10/13
 */
public class StringUtils extends org.apache.commons.lang.StringUtils{

    public static final String BASE_MATCH_CHAR = "*";
    public static final String BASE_REPLACE_CHAR = "%";

    /**
     * 默认的处理搜索框模糊搜索，将用户输入的  ”*searchText*“ 替换成 ”%searchText%“
     * @param str 需要处理的字符串
     * @return 替换成模糊搜索的字符串
     * */
    public static String replaceMatchText(String str) {
        if(isEmpty(str)) {
            return str;
        }
        if(!contains(str, BASE_MATCH_CHAR)) {
            return str;
        }
        int i = 2;
        if(startsWith(str, BASE_MATCH_CHAR)) {
            if(str.length() < i) {
                return str;
            }
            str = replaceOnce(str, BASE_MATCH_CHAR, BASE_REPLACE_CHAR);
            i = 3;
        }
        if(endsWith(str, BASE_MATCH_CHAR)) {
            if(str.length() < i) {
                return str;
            }
            str = str.substring(0, str.length()-1) + BASE_REPLACE_CHAR;
        }
        return  str;
    }

    /**
     * 循环处理map的value.只处理String类型的
     * 默认的处理搜索框模糊搜索，将用户输入的  ”*searchText*“ 替换成 ”%searchText%“
     *
     * @param param 需要处理的map
     * @return
     * */
    public static Map<String, Object> replaceMatchTextByMap(Map<String, Object> param) {
        if(CollectionUtils.isEmpty(param)) {
            return param;
        }
        param.forEach((k,v) -> {
            if(v instanceof String) {
                String vStr = replaceMatchText((String) v);
                param.put(k, vStr);
            }
        });
        return param;
    }

}
