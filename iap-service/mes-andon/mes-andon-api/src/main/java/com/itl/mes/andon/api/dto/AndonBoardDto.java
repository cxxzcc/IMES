package com.itl.mes.andon.api.dto;

import com.itl.mes.andon.api.entity.Type;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2021/12/28
 * @since 1.8
 */
@Data
public class AndonBoardDto {

    private List<Type> types;

    private List<Map<String, Object>> tableDatas;
}
