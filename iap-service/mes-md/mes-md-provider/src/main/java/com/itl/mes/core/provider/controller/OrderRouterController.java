package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.constant.BOPrefixEnum;
import com.itl.mes.core.api.entity.OrderRouter;
import com.itl.mes.core.api.service.OrderRouterService;
import com.itl.mes.core.api.vo.OrderRouterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  工艺路线-工单副本
 * @author chenjx1
 * @since 2021-10-26
 */
@RestController
@RequestMapping("/orderRouter")
@Api(tags = "工艺路线-工单副本" )
public class OrderRouterController {
    private final Logger logger = LoggerFactory.getLogger(OrderRouterController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public OrderRouterService orderRouterService;


    /**
     * 根据router查询
     *
     * @param orderRouter
     * @return RestResult<Router>
     */
    @PostMapping("/queryList")
    @ApiOperation(value="查找工单工艺路线集合")
    @ApiImplicitParam(name="OrderRouter",value="工艺路线对象",dataType="OrderRouter", paramType = "query")
    public ResponseData<List<OrderRouter>> queryRouterList(@RequestBody OrderRouter orderRouter) throws CommonException {
        return ResponseData.success( orderRouterService.getOrderRouterList(orderRouter) );
    }

    /**
     * 根据shopOrderBo查询
     *
     * @param shopOrderBo
     * @return RestResult<Router>
     */
    @GetMapping("/query")
    @ApiOperation(value="查找工单工艺路线对象")
    @ApiImplicitParam(name="shopOrderBo",value="工单BO",dataType="string", paramType = "query")
    public ResponseData<OrderRouter> queryRouter(@RequestParam String shopOrderBo) throws CommonException {
        return ResponseData.success( orderRouterService.getOrderRouter(shopOrderBo) );
    }

    /**
     * 根据router查询
     * @param routerBo
     * @return
     * @throws CommonException
     */
    @GetMapping("/queryById")
    @ApiOperation(value="根据工单工艺路线副本ID查找工单工艺路线")
    @ApiImplicitParam(name="routerBo",value="工艺路线BO",dataType="string", paramType = "query")
    public ResponseData<OrderRouter> queryRouterById(@RequestParam String routerBo) throws CommonException {
        return ResponseData.success( orderRouterService.getOrderRouterById(routerBo) );
    }


    /**
     * 保存工单工艺路线
     *
     * @param orderRouterVo
     * @return RestResult<OrderRouter>
     */
    @PostMapping("/save")
    @ApiOperation(value="保存工艺路线")
    public ResponseData<OrderRouter> saveRouter(@RequestBody OrderRouterVo orderRouterVo) throws CommonException {
        if (!orderRouterVo.getShopOrderBo().startsWith(BOPrefixEnum.SO.getPrefix())) {
            orderRouterVo.setShopOrderBo(new ShopOrderHandleBO(UserUtils.getSite(),orderRouterVo.getShopOrderBo()).getBo());
        }
        if(orderRouterVo.getSnBo() != null){
            orderRouterVo.getSnBo().forEach(x->{
                if (!x.contains(BOPrefixEnum.SN.getPrefix())) {
                    new SnHandleBO(UserUtils.getSite(),x).getBo();
                }
            });
        }
        orderRouterService.saveOrderRouter(orderRouterVo);
        return ResponseData.success(orderRouterService.getOrderRouterById(orderRouterVo.getBo()));
    }

    /**
     * 删除工单工艺路线
     *
     * @param bo 工单工艺路线
     * @return RestResult<String>
     */
    @GetMapping("/delete")
    @ApiOperation(value="删除工单工艺路线")
    @ApiImplicitParam(name="bo",value="工单工艺路线bo",dataType="string", paramType = "query")
    public ResponseData<String> deleteOrderRouter( String bo) throws CommonException {
        if( StrUtil.isBlank( bo ) ){
            throw new CommonException( "工单工艺路线BO不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }

        OrderRouter orderRouter = new OrderRouter();
        orderRouter.setBo(bo);
        orderRouterService.deleteOrderRouter(orderRouter);
        return ResponseData.success( "success" );
    }

}
