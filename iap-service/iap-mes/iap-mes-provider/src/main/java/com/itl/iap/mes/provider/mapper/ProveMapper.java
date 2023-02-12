package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.prove.StationProveQueryDTO;
import com.itl.iap.mes.api.dto.prove.UserProveQueryDTO;
import com.itl.iap.mes.api.entity.prove.ProveEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/3 17:44
 */
@Repository
public interface ProveMapper extends BaseMapper<ProveEntity> {



    IPage<ProveEntity> findUserProveList(Page page,@Param("userProve") UserProveQueryDTO userProveQueryDTO);

    IPage<ProveEntity> findNotUserProveList(Page page,@Param("userProve")UserProveQueryDTO userProveQueryDTO);

    IPage<ProveEntity> findStationProveList(Page page,@Param("stationProve")StationProveQueryDTO stationProveQueryDTO);

    IPage<ProveEntity> findNotStationProveList(Page page,@Param("stationProve")StationProveQueryDTO stationProveQueryDTO);

    /**
     * 根据用户id查询
     * @param userId 用户id
     * @return 结果集
     * */
    List<ProveEntity> getByUserId(@Param("userId") String userId, @Param("site") String site);
}
