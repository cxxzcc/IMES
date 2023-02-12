package com.itl.mom.label.provider.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.dto.ProductStateUpdateDto;
import com.itl.mes.core.api.dto.UpdateDoneDto;
import com.itl.mom.label.api.dto.label.UpdateNextProcessDTO;
import com.itl.mom.label.api.dto.label.UpdateSnHoldDTO;
import com.itl.mom.label.api.entity.MeProductStatus;
import com.itl.mom.label.api.service.MeProductStatusService;
import com.itl.mom.label.api.vo.MeProductStatusQueryVo;
import com.itl.mom.label.provider.mapper.label.SnMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * product_status(MeProductStatus)表控制层
 *
 * @author makejava
 * @since 2021-10-22 11:09:56
 */
@RestController
@Api(tags = "产品状态")
@RequestMapping("meProductStatus")
public class MeProductStatusController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private MeProductStatusService meProductStatusService;

    @Resource
    private SnMapper snMapper;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseData<MeProductStatus> selectOne(@PathVariable Serializable id) {
        return ResponseData.success(this.meProductStatusService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param meProductStatus 实体对象
     * @return 新增结果
     */
    @PostMapping
    public ResponseData<Boolean> insert(@RequestBody MeProductStatus meProductStatus) {
        return ResponseData.success(this.meProductStatusService.save(meProductStatus));
    }

    /**
     * 修改数据
     *
     * @param meProductStatus 实体对象
     * @return 修改结果
     */
    @PutMapping
    public ResponseData<Boolean> update(@RequestBody List<MeProductStatus> meProductStatus) {
        for (MeProductStatus s : meProductStatus) {
            final String nextOperation = s.getNextOperation();
            final String shopOrder = s.getShopOrder();
            if (StrUtil.isNotBlank(nextOperation) && StrUtil.isNotBlank(shopOrder)) {
                final String byId = snMapper.getById(new ShopOrderHandleBO(UserUtils.getSite(), shopOrder).getBo());
                if (StrUtil.isBlank(byId)) {
                    throw new CommonException("工艺路线不存在", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                if (!byId.contains(nextOperation)) {
                    throw new CommonException("该工序不在工艺路线中,请选择其他工序", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
            }
        }
        return ResponseData.success(this.meProductStatusService.updateBatchById(meProductStatus));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseData delete(@RequestParam("idList") List<Long> idList) {
        return ResponseData.success(this.meProductStatusService.removeByIds(idList));
    }


    /**
     * 根据sn查询
     * */
    @GetMapping("/getBySn")
    public ResponseData<MeProductStatus> getBySn(@RequestParam("sn") String sn, @RequestParam("site")String site) {
        return ResponseData.success(meProductStatusService.getBySn(sn, site));
    }

    /**
     * 根据snbo列表查询
     * @param snBoList snbo列表
     * */
    @PostMapping("/getBySnBoList")
    public ResponseData<List<MeProductStatus>> getBySn(@RequestBody List<String> snBoList) {
        return ResponseData.success(meProductStatusService.getBySnBos(snBoList));
    }

    /**
     * 根据snbo列表查询  工单和sn
     * @param snBoList snbo列表
     * */
    @PostMapping("/getShopOrderBySnBoList")
    public ResponseData<List<MeProductStatus>> getShopOrderBySnBoList(@RequestBody List<String> snBoList) {
        return ResponseData.success(meProductStatusService.getShopOrderBySnBoList(snBoList));
    }


    @GetMapping("/findProductStatus")
    ResponseData<List<MeProductStatusQueryVo>> findProductStatusBySnAndStatus(@RequestParam("sn") String sn, @RequestParam("status")int status){
        try{
            return meProductStatusService.findProductStatusBySnAndStatus(sn,status);
        }catch (Exception e){
            return ResponseData.error(e.getMessage());
        }
    }

    @PostMapping("/productStateUpdate")
    ResponseData<Boolean> productStateUpdate(@RequestBody List<ProductStateUpdateDto> productStateUpdateDto){
        try{
            return meProductStatusService.productStateUpdate(productStateUpdateDto);
        }catch (Exception e){
            return ResponseData.error(e.getMessage());
        }
    }

    @PostMapping("/updateProductStatusDoneByBo")
    ResponseData updateProductStatusDoneByBo(@RequestBody  List<UpdateDoneDto> updateDoneDtos){
        try{
            return meProductStatusService.updateProductStatusDoneByBo(updateDoneDtos);
        }catch (Exception e){
            return ResponseData.error(e.getMessage());
        }
    }

    /**
     * 更新下一工序
     * */
    @PostMapping("/updateNextProcess")
    public ResponseData<Boolean> updateNextProcess(@RequestBody  ProductStateUpdateDto productStateUpdateDto){
        return ResponseData.success(meProductStatusService.updateNextProcess(productStateUpdateDto));
    }

    /**
     * 更新下一工序
     * */
    @PostMapping("/updateNextProcessBatch")
    public ResponseData<Boolean> updateNextProcessBatch(@RequestBody UpdateNextProcessDTO param){
        return ResponseData.success(meProductStatusService.updateNextProcessBatch(param.getSnBoList(), param.getNextProcessId(), param.getNextProcessName()));
    }

    /**
     * 根据ids更新是否挂起
     * @param ids 产品状态id集合
     * @param isHold 是否挂起
     * @return 是否成功
     * */
    @PostMapping("/updateIsHoldByIds")
    @ApiOperation(value = "挂起/取消挂起")
    public ResponseData<Boolean> updateIsHoldByIds(@RequestBody UpdateSnHoldDTO updateSnHoldDTO) {
        List<String> idList = updateSnHoldDTO.getIdList();
        if(CollUtil.isNotEmpty(idList)) {
            return ResponseData.success(meProductStatusService.updateIsHoldByIds(idList, updateSnHoldDTO.getHold(), true));
        }
        return ResponseData.success(meProductStatusService.updateIsHoldBySnList(updateSnHoldDTO.getSnBoList(), updateSnHoldDTO.getHold()));
    }
}
