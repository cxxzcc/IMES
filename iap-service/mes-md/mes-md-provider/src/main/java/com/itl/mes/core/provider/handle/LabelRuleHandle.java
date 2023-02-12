package com.itl.mes.core.provider.handle;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yx
 * @date 2021/3/21
 * @since JDK1.8
 */
public interface LabelRuleHandle {
    Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰,正常输出
     *
     * @param str
     * @return
     */
    default String lineToHump(String str) {
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    Map<String, Object> handle(ItemForParamQueryDto queryDto) throws CommonException;

    Map<String, Object> handleCustom(ItemForParamQueryDto queryDto) throws CommonException;
}
