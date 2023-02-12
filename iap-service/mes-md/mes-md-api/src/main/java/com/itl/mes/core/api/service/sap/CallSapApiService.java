package com.itl.mes.core.api.service.sap;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CustomerDTO;


/**
 * @author zhec
 * @create 2021/9/10
 * @description 调用交互接口
 **/
public interface CallSapApiService {


    /**
     * 客户信息
     *
     * @param customerDTO
     * @return
     */
    ResponseData customerSet(CustomerDTO customerDTO);


    /**
     * sap接口推送
     * @param ids
     * @return
     */
    //ResponseData call(List<String> ids);

}
