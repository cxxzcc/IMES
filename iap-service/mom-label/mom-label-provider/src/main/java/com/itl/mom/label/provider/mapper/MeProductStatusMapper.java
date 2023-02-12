package com.itl.mom.label.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mom.label.api.entity.MeProductStatus;
import com.itl.mom.label.api.vo.MeProductStatusQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * product_status(MeProductStatus)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-22 11:09:34
 */
public interface MeProductStatusMapper extends BaseMapper<MeProductStatus> {


    List<MeProductStatusQueryVo> findProductStatusBySnAndStatus(@Param("sn")  String sn, @Param("state")  int state);

    int updateProductState(@Param("meProductStatus") MeProductStatus meProductStatus);

    int updateProductStatusDoneByBo(@Param("productStateBo")  String productStateBo, @Param("done") Integer done);

    /**
     * 根据snBo列表查询产品状态集合
     * */
    List<MeProductStatus> getBySnBos(@Param("snBos") List<String> snBos);

    /**
     * 根据snBo列表查询产品状态集合, 工单编号和sn
     * */
    List<MeProductStatus> getShopOrderBySnBoList(@Param("snBos") List<String> snBoList);
}
