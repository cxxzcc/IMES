package com.itl.plugins.report.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.constants.DateScopeEnum;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.LocalDateTimeUtils;
import com.itl.iap.mes.api.dto.CheckExecuteAndPlanDTO;
import com.itl.iap.mes.api.dto.CheckExecuteAndPlanQueryDTO;
import com.itl.iap.mes.api.entity.CheckExecute;
import com.itl.iap.mes.client.service.MesReportFeign;
import com.itl.mes.core.api.dto.ProductCheckoutDTO;
import com.itl.mes.core.api.dto.ProductCheckoutDetailDTO;
import com.itl.mes.core.api.dto.TProjectActualQueryDTO;
import com.itl.mes.core.api.vo.ProductCheckoutDetailVO;
import com.itl.mes.core.api.vo.ProductCheckoutVO;
import com.itl.mes.core.api.vo.TProjectActualVO;
import com.itl.mes.core.client.service.MdReportFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author cjq
 * @Date 2021/11/29 10:48 上午
 * @Description api数据集
 */
@Component
@RequiredArgsConstructor
public class DataApi {

    private final MesReportFeign mesReportFeign;
    private final MdReportFeign mdReportFeign;

    /**
     * 基地对标-交叉日常验证
     *
     * @param dsName
     * @param datasetName
     * @param parameters
     * @return
     */
    public List<TProjectActualVO> getDeviceActual(String dsName, String datasetName, Map<String, Object> parameters) {
        TProjectActualQueryDTO projectActualQueryDTO = BeanUtil.fillBeanWithMap(parameters, new TProjectActualQueryDTO(), false);
        ResponseData<List<TProjectActualVO>> productCheckoutList = mdReportFeign.getDeviceActual(projectActualQueryDTO);
        Assert.valid(productCheckoutList == null, "数据集调用失败");
        List<TProjectActualVO> data = productCheckoutList.getData();
        BigDecimal bd0 = new BigDecimal("0");
        for (TProjectActualVO datum : data) {
            Integer digital = datum.getDigital() == null ? 2 : datum.getDigital();
            String standardDeviation = datum.getStandardDeviation();
            String replace = standardDeviation.replaceAll("[^0-9]", "");
            datum.setStandardDeviation(replace);
            datum.setActual(datum.getActual() == null ? bd0 : datum.getActual().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setStandard(datum.getStandard() == null ? bd0 : datum.getStandard().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setRange(datum.getRange() == null ? bd0 : datum.getRange().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setActual1(datum.getActual1() == null ? bd0 : datum.getActual1().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setActual2(datum.getActual2() == null ? bd0 : datum.getActual2().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setActual3(datum.getActual3() == null ? bd0 : datum.getActual3().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setActual4(datum.getActual4() == null ? bd0 : datum.getActual4().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setActual5(datum.getActual5() == null ? bd0 : datum.getActual5().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setActual6(datum.getActual6() == null ? bd0 : datum.getActual6().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setActual7(datum.getActual7() == null ? bd0 : datum.getActual7().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setActual8(datum.getActual8() == null ? bd0 : datum.getActual8().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setActual9(datum.getActual9() == null ? bd0 : datum.getActual9().setScale(digital, BigDecimal.ROUND_HALF_UP));
            datum.setActual10(datum.getActual10() == null ? bd0 : datum.getActual10().setScale(digital, BigDecimal.ROUND_HALF_UP));
        }
        return data;
    }


    /**
     * 检验项目统计数据集
     *
     * @param dsName
     * @param datasetName
     * @param parameters
     * @return
     */
    public List<ProductCheckoutVO> getProductCheckoutList(String dsName, String datasetName, Map<String, Object> parameters) {
        String site = (String) parameters.get("site");
        Assert.valid(StrUtil.isBlank(site), "工厂不能为空");
        ProductCheckoutDTO productCheckoutDTO = BeanUtil.fillBeanWithMap(parameters, new ProductCheckoutDTO(), false);
        productCheckoutDTO.setSite(site);
        ResponseData<List<ProductCheckoutVO>> productCheckoutList = mdReportFeign.getProductCheckoutList(productCheckoutDTO);
        Assert.valid(productCheckoutList == null, "数据集调用失败");
        return productCheckoutList.getData();
    }

    /**
     * 检验项目详情数据集
     *
     * @param dsName
     * @param datasetName
     * @param parameters
     * @return
     */
    public List<ProductCheckoutDetailVO> getProductCheckoutDetailList(String dsName, String datasetName, Map<String, Object> parameters) {
        String site = (String) parameters.get("site");
        Assert.valid(StrUtil.isBlank(site), "工厂不能为空");
        ProductCheckoutDetailDTO productCheckoutDetailDTO = BeanUtil.fillBeanWithMap(parameters, new ProductCheckoutDetailDTO(), false);
        productCheckoutDetailDTO.setSite(site);
        ResponseData<List<ProductCheckoutDetailVO>> productCheckoutDetailList = mdReportFeign.getProductCheckoutDetailList(productCheckoutDetailDTO);
        Assert.valid(productCheckoutDetailList == null, "数据集调用失败");
        return productCheckoutDetailList.getData();
    }

    /**
     * 点检执行报表数据集
     *
     * @param dsName
     * @param datasetName
     * @param parameters
     * @return
     */
    public List<CheckExecuteAndPlanDTO> getCheckExecuteList(String dsName, String datasetName, Map<String, Object> parameters) {
        CheckExecuteAndPlanQueryDTO checkExecuteAndPlanQueryDTO = new CheckExecuteAndPlanQueryDTO();
        String operaStartTime = (String) parameters.get("operaStartTime");
        String operaEndTime = (String) parameters.get("operaEndTime");
        String site = (String) parameters.get("site");
        Assert.valid(StrUtil.isBlank(site), "工厂不能为空");
        if (StrUtil.isBlank(operaStartTime) && StrUtil.isBlank(operaEndTime)) {
            LocalDateTimeUtils.DateTimeScope dateTimeScope = LocalDateTimeUtils.getDateTimeScope(DateScopeEnum.WEEK.getCode());
            if (dateTimeScope != null) {
                operaStartTime = LocalDateTimeUtil.format(dateTimeScope.getStartTime(), DatePattern.NORM_DATETIME_PATTERN);
                operaEndTime = LocalDateTimeUtil.format(dateTimeScope.getEndTime(), DatePattern.NORM_DATETIME_PATTERN);
            }
        }
        checkExecuteAndPlanQueryDTO.setOperaStartTime(operaStartTime);
        checkExecuteAndPlanQueryDTO.setOperaEndTime(operaEndTime);
        checkExecuteAndPlanQueryDTO.setSite(site);
        ResponseData<List<CheckExecuteAndPlanDTO>> checkExecuteList = mesReportFeign.getCheckExecuteList(checkExecuteAndPlanQueryDTO);
        Assert.valid(checkExecuteList == null || CollectionUtil.isEmpty(checkExecuteList.getData()), "数据集调用失败");
        return checkExecuteList.getData();
    }


}
