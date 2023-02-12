package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author yx
 * @date 2021/8/18
 * @since 1.8
 */
@Data
public class TeamUserQueryDto {
    private Page page;
    private String userName;
    private String realName;
    private String team;
    private String teamBo;
}
