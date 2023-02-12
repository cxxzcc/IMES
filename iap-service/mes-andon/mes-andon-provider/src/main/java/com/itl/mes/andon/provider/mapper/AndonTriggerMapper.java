package com.itl.mes.andon.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.andon.api.vo.AndonTriggerAndonVo;
import com.itl.mes.andon.api.vo.AndonTriggerPushUserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @auth liuchenghao
 * @date 2020/12/22
 */
@Repository
public interface AndonTriggerMapper extends BaseMapper {

    /**
     * @param andonBoxBos   灯箱Bo
     * @param stationBo     工位Bo
     * @param productLineBo 产线Bo
     * @param workShopBo    车间Bo
     * @param deviceBo      设备Bo
     * @return 安灯信息
     */
    Set<AndonTriggerAndonVo> findList(@Param("site") String site, @Param("idList") Collection<String> andonBoxBos, @Param("stationBo") String stationBo, @Param("productLineBo") String productLineBo, @Param("workShopBo") String workShopBo, @Param("deviceBo") String deviceBo);

    List<Map<String, String>> getStationList(String userId, String site);

    String getProductLineBo(String stationBo);

    List<String> getLineBo(@Param("stationBos") Set<String> stationBos);

    String getWorkShopBo(String productLineBo);

    List<String> getWorkShopBoList(@Param("productLineBoList") List<String> productLineBoList);

    List<Map<String, String>> getDeviceList(String stationBo);


    String getItem(String itemBo);

    /**
     * 获取需要推送的用户ID
     *
     * @param andonBo
     * @return
     */
    Set<AndonTriggerPushUserVo> getPushUser(String andonBo);


}
