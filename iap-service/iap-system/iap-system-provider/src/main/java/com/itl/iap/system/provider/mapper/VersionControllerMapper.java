//package com.itl.iap.system.provider.mapper;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.itl.iap.system.api.entity.VersionController;
//import org.apache.ibatis.annotations.Param;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * @author yezi
// * @date 2019/5/29
// */
//public interface VersionControllerMapper extends BaseMapper<VersionController> {
//    /**
//     * 设置所有记录不为最新版本
//     */
//    void setAllRecordNotUpToDate();
//
//    List<Map<String, Object>> selectPageVersionForTable(IPage<Map<String, Object>> page, @Param("params") Map<String, Object> params);
//
//}
//
