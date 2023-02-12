package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.DeviceChangeStateRequestVO;
import com.itl.mes.core.api.dto.DeviceConditionDto;
import com.itl.mes.core.api.dto.DeviceCountStatisticsDTO;
import com.itl.mes.core.api.dto.DeviceDto;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.entity.DeviceType;
import com.itl.mes.core.api.vo.DevicePlusVo;
import com.itl.mes.core.api.vo.DeviceVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
public interface DeviceService extends IService<Device> {

    List<Device> selectList();

    void updateState(String code,String state) throws CommonException;

    void saveDevice(DeviceVo deviceVo) throws CommonException;

    DeviceVo getDeviceVoByDevice(String device) throws CommonException;

    void deleteDevice(String device, Date modifyDate) throws CommonException;

    IPage<DeviceType> getDeviceTypeVoList(Page page);
    IPage<Device> selectDeviceWorkshop(DeviceDto deviceDto);

    IPage getScrewByLine(Map<String, Object> params);

    Integer getDeviceCountBySite(String site);

    /**
     * 更改设备状态
     * @param deviceChangeStateRequestVO
     * */
    Boolean changeState(DeviceChangeStateRequestVO deviceChangeStateRequestVO) throws CommonException;

    /**
     * 根据设备bos查询设备名称列表
     * */
    List<DeviceVo> queryNameByBos(List<String> bos);

    List<Device> selectByDevices(List<String> devices);

    /**
     * 统计各个状态设备的数量
     * @param site 工厂id
     * @return
     * */
    List<DeviceCountStatisticsDTO> queryDeviceCountStatisticsByState(String site);

    void export(HttpServletRequest request, HttpServletResponse response);

    List<DevicePlusVo> getDataByCondition(DeviceConditionDto deviceLikeDto);



    void saveByImport(List<DeviceVo> list);
}