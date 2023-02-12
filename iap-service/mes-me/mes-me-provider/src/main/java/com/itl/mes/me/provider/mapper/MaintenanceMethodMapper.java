package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.me.api.dto.MaintenanceMethodDto;
import com.itl.mes.me.api.entity.MaintenanceMethod;
import com.itl.mes.me.api.vo.CorrectiveMaintenanceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 维修方法mapper
 * @author dengou
 * @date 2021/11/4
 */
@Mapper
public interface MaintenanceMethodMapper extends BaseMapper<MaintenanceMethod> {

    /**
     * 分页列表
     * @param params 查询参数
     * @return 分页查询参数
     * */
    List<MaintenanceMethodDto> getPage(Page<MaintenanceMethodDto> page, @Param("params") Map<String, Object> params);

    /**
     * 详情
     * @param id 维修方法id
     * @return 维修方法详情
     * */
    MaintenanceMethodDto getDetailById(String id);

    List<CorrectiveMaintenanceVo> queryList(@Param("site") String site);

    List<CorrectiveMaintenanceVo> queryListByIds(@Param("site")String site, @Param("list")List<String> list);
}
