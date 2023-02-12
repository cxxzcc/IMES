package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.dto.MaintenanceMethodDto;
import com.itl.mes.me.api.entity.MaintenanceMethod;
import com.itl.mes.me.api.vo.CorrectiveMaintenanceVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 维修方法服务接口
 * @author dengou
 * @date 2021/11/4
 */
public interface MaintenanceMethodService extends IService<MaintenanceMethod> {

    /**
     * 分页列表
     * @param params 查询参数
     * @return 分页查询参数
     * */
    Page<MaintenanceMethodDto> getPage(Map<String, Object> params);

    /**
     * 详情
     * @param id 维修方法id
     * @return 维修方法详情
     * */
    MaintenanceMethodDto getDetailById(String id);

    /**
     * 新增
     * @param maintenanceMethod 维修方法详情
     * @return 是否成功
     * */
    Boolean add(MaintenanceMethod maintenanceMethod);

    /**
     * 编辑
     * @param maintenanceMethod 维修方法详情
     * @return 是否成功
     * */
    Boolean updateMaintenance(MaintenanceMethod maintenanceMethod);

    /**
     * 删除
     * @param ids id列表
     * @return 是否删除成功
     * */
    Boolean deleteBatchIds(List<String> ids);

    /**
     * 导入的数据保存
     * @param ts ts
      */
     void saveByImport(List<CorrectiveMaintenanceVo> ts);

    /**
     * 数据导出
     * @param ids dto
     * @param response response
     */
    void export(String ids, HttpServletResponse response);


}
