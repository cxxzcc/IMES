package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.dto.RouterDataDTO;
import com.itl.mes.core.api.dto.RouterProcessDataDTO;
import com.itl.mes.core.api.entity.Router;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.vo.RouterAsSaveVo;
import com.itl.mes.core.api.vo.RouterVo;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import org.springframework.util.RouteMatcher;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工艺路线
 * </p>
 *
 * @author linjl
 * @since 2020-01-28
 */
public interface RouterService extends IService<Router> {

    /**
     * 获取工艺路线信息(带路线图)
     *
     * @return
     */
    Router getRouter(String bo) throws CommonException;

    /**
     * 根据routerCode获取工艺路线信息(带路线图)
     *
     * @return
     */
    Router getRouterCode(String routerCode) throws CommonException;

    /**
     * 保存工艺路线
     * */
    boolean saveRouter(RouterVo routerVo) throws Exception;

    /**
     * 复制另存工艺路线
     * */
    Router saveAsRouter(RouterAsSaveVo routerAsSaveVo) throws Exception;

    /**
     * 删除工艺路线
     *
     * @param router 工艺路线
     * @throws CommonException
     */
    void deleteRouter(Router router) throws CommonException;

    /**
     * 导入工艺路线
     * @param file
     */
    void importFile(MultipartFile file) throws CommonException;

    void importFile(List<RouterDataDTO> cachedDataList, List<RouterProcessDataDTO> cachedDataList1);
}
