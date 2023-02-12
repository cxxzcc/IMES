package com.itl.iap.mes.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.mes.api.dto.sparepart.SparePartDTO;
import com.itl.iap.mes.api.entity.sparepart.SparePart;
import com.itl.mes.core.api.vo.DeviceVo;

import java.util.List;
import java.util.Map;

/**
 * 备件业务接口
 * @author dengou
 * @date 2021/9/17
 */
public interface SparePartService extends IService<SparePart> {

    /**
     * 分页查询
     *
     * @param params 分页参数，查询参数
     * @return 分页结果集
     */
    Page<SparePartDTO> page(Map<String, Object> params);

    /**
     * 分页查询坤村列表
     *
     * @param params 分页参数，查询参数
     * @return 分页结果集
     */
    Page<SparePartDTO> pageByInventory(Map<String, Object> params);

    /**
     * 备件新增
     *
     * @param sparePart 备件信息
     * @return 是否成功
     */
    Boolean addSparePart(SparePart sparePart) throws CommonException;

    /**
     * 备件修改
     *
     * @param sparePart 备件信息
     * @return 是否成功
     */
    Boolean updateSparePart(SparePart sparePart);

    /**
     * 备件详情
     *
     * @param id 备件id
     * @return 备件详情信息
     */
    SparePart detail(String id);

    /**
     * 删除备件
     *
     * @param id 备件id
     * @return 是否成功
     */
    Boolean deleteSparePart(String id);

    /**
     * 批量删除备件
     *
     * @param ids 备件id
     * @return 是否成功
     */
    Boolean deleteSparePartBatch(List<String> ids);


    void saveByImport(List<SparePart> list);

    Boolean checkExistsBySparePartNo(SparePart sparePart, String site);
}
