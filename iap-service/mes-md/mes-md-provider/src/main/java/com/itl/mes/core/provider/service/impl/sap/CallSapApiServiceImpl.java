package com.itl.mes.core.provider.service.impl.sap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itl.iap.common.base.aop.JdbcConfig;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.HttpClient;
import com.itl.mes.core.api.dto.CustomerDTO;
import com.itl.mes.core.api.service.sap.CallSapApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhec
 * @create 2021/9/10
 * @description 调用sap接口实现类
 **/
@Slf4j
@Service
public class CallSapApiServiceImpl implements CallSapApiService {

    @Resource
    JdbcConfig jdbcConfig;

    //@Value("${sap.url}")
    private String url = "http://61.143.33.102:38080/stage-api/md";
    //private String url = "http://localhost:19000/api/md";

    //@Value("${sap.authorized.username}")
    private String username = "admin";

    //@Value("${sap.authorized.password}")
    private String password = "147258";

    public static final String PRODUCTION_STORAGE_ROUTING = "/customer/add";

    public static final String SALE_CODE = "SaleSilentPeriodSet";

    public static final String PRODUCE_CODE = "SilentPeriodSet";


    //@Autowired
    //private IapOpsLogTService iapOpsLogTService;


    @Override
    public ResponseData customerSet(CustomerDTO customerDTO) {
        log.info("params:{}", JSON.toJSONString(customerDTO));
        JSONObject rseObject = null;
        try {
//            if (querySilentPeriodSet(JSON.toJSONString(customerDTO), url + PRODUCTION_STORAGE_ROUTING, "1", PRODUCE_CODE)) {
//                return ResponseData.success("静默期开启延缓执行!");
//            }
            String resStr = HttpClient.doPost(url + PRODUCTION_STORAGE_ROUTING, username, password, JSON.toJSONString(customerDTO),jdbcConfig);
            rseObject = JSONObject.parseObject(resStr);
            if (!"200".equals(rseObject.getString("code"))) {
                return ResponseData.error(rseObject.getString("msg"));
            }
        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
        return ResponseData.success(rseObject.getString("msg"));

    }

//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public ResponseData call(List<String> ids) {
//        List<IapOpsLogT> iapOpsLogTList = iapOpsLogTService.getByIds(ids);
//        if (CollectionUtil.isNotEmpty(iapOpsLogTList)) {
//            for (IapOpsLogT logT : iapOpsLogTList) {
//                try {
//                    Thread.sleep(5000);
//                    if (logT.getRequestMethod().contains(CallSapApiServiceImpl.PRODUCTION_STORAGE_ROUTING)) {
//                        //生产入库
//                        CustomerDTO dto = JSON.parseObject(logT.getRequestParams(), CustomerDTO.class);
//                        customerSet(dto);
//                    }
//                    iapOpsLogTService.delete(logT);
//                } catch (Exception e) {
//                    return ResponseData.error(e.getMessage());
//                }
//            }
//
//        }
//        return ResponseData.success("success");
//    }


    /**
     * 查询静默期如果已开启则不执行
     *
     * @param params
     * @param url
     * @param methodDesc
     */
//    public boolean querySilentPeriodSet(String params, String url, String methodDesc, String code) {
//        IapDictT dict = warehouseMapper.querySilentPeriodSet(code);
//        if (null != dict && dict.getEnabled() == 0) {
//            logUtil.log("SAP", params, url, "静默期开启延缓执行!", "0", methodDesc, "1");
//            return true;
//        }
//        return false;
//    }


}
