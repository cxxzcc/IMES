package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.constants.CommonConstants;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.StringUtils;
import com.itl.mes.me.api.dto.MaintenanceMethodDto;
import com.itl.mes.me.api.entity.ErrorType;
import com.itl.mes.me.api.entity.MaintenanceMethod;
import com.itl.mes.me.api.service.MaintenanceMethodService;
import com.itl.mes.me.api.vo.CorrectiveMaintenanceVo;
import com.itl.mes.me.provider.mapper.ErrorTypeMapper;
import com.itl.mes.me.provider.mapper.MaintenanceMethodMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 维修方法服务实现类
 *
 * @author dengou
 * @date 2021/11/4
 */
@Service
@Slf4j
public class MaintenanceMethodServiceImpl extends ServiceImpl<MaintenanceMethodMapper, MaintenanceMethod> implements MaintenanceMethodService {

    @Autowired
    private ErrorTypeMapper errorTypeMapper;

    @Override
    public Page<MaintenanceMethodDto> getPage(Map<String, Object> params) {
        Page<MaintenanceMethodDto> page = new QueryPage<>(params);
        params.put("site", UserUtils.getSite());
        List<MaintenanceMethodDto> list = baseMapper.getPage(page, params);
        page.setRecords(list);
        return page;
    }

    @Override
    public MaintenanceMethodDto getDetailById(String id) {
        return baseMapper.getDetailById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(MaintenanceMethod maintenanceMethod) {
        maintenanceMethod.setId(null);

        //校验code是否已存在
        Assert.valid(checkCodeExists(maintenanceMethod), "维修措施编码已存在");

        maintenanceMethod.setCreateUser(UserUtils.getUserName());
        maintenanceMethod.setCreateTime(new Date());
        maintenanceMethod.setUpdateUser(null);
        maintenanceMethod.setUpdateTime(null);
        if (StrUtil.isBlank(maintenanceMethod.getIsDisableFlag())) {
            maintenanceMethod.setIsDisableFlag(CommonConstants.FLAG_Y);
        }
        if (StrUtil.isBlank(maintenanceMethod.getSite())) {
            maintenanceMethod.setSite(UserUtils.getSite());
        }
        return save(maintenanceMethod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMaintenance(MaintenanceMethod maintenanceMethod) {

        String site = UserUtils.getSite();
        //校验维修方法是否存在
        Assert.valid(!checkExistsByIdAndSite(maintenanceMethod.getId(), site), "未找到异常类型");
        //校验code是否已存在
        Assert.valid(checkCodeExists(maintenanceMethod), "维修措施编码已存在");
        maintenanceMethod.setCreateUser(null);
        maintenanceMethod.setCreateTime(null);
        maintenanceMethod.setUpdateUser(UserUtils.getUserName());
        maintenanceMethod.setUpdateTime(new Date());
        maintenanceMethod.setSite(site);
        if (StrUtil.isBlank(maintenanceMethod.getIsDisableFlag())) {
            maintenanceMethod.setIsDisableFlag(CommonConstants.FLAG_Y);
        }
        return updateById(maintenanceMethod);
    }

    /**
     * 校验code是否存在
     *
     * @return 是否存在
     */
    private Boolean checkCodeExists(MaintenanceMethod maintenanceMethod) {
        LambdaQueryChainWrapper<MaintenanceMethod> wrapper = lambdaQuery().eq(MaintenanceMethod::getSite, UserUtils.getSite())
                .eq(MaintenanceMethod::getCode, maintenanceMethod.getCode());
        if (StrUtil.isNotBlank(maintenanceMethod.getId())) {
            wrapper.ne(MaintenanceMethod::getId, maintenanceMethod.getId());
        }
        return wrapper.count() > 0;
    }

    /**
     * 根据id和site查询维修方法是否存在
     *
     * @param id   id
     * @param site 工厂id
     * @return 是否存在
     */
    private Boolean checkExistsByIdAndSite(String id, String site) {
        Integer count = lambdaQuery().eq(MaintenanceMethod::getSite, site).eq(MaintenanceMethod::getId, id).count();
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBatchIds(List<String> ids) {
        Assert.valid(CollUtil.isEmpty(ids), "id列表不能为空");

        Integer count = lambdaQuery().eq(MaintenanceMethod::getSite, UserUtils.getSite())
                .in(MaintenanceMethod::getId, ids).count();

        Assert.valid(count == 0 || count < ids.size(), "未找到指定维修方法");
        return removeByIds(ids);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public void saveByImport(List<CorrectiveMaintenanceVo> ts) {
        String userName = UserUtils.getUserName();
        // String userName = "111";
        Date date = new Date();


        //异常类型id 要根据异常code和工厂拿到
        //不为空判断是否已经存在   /errortype
        ts = ts.stream().filter(distinctByKey(CorrectiveMaintenanceVo::getCode)).collect(Collectors.toList());
        ts.forEach(x -> {
            String site = UserUtils.getSite();
            //String site = "1040";

            String errorTypeCode = x.getErrorTypeCode();
            ErrorType errorType = getErrorType(site, errorTypeCode);
            MaintenanceMethod maintenanceMethod = new MaintenanceMethod();
            maintenanceMethod.setSite(site);
            maintenanceMethod.setCode(x.getCode());
            maintenanceMethod.setTitle(x.getTitle());
            maintenanceMethod.setDescription(x.getDescription());
            maintenanceMethod.setErrorTypeId(errorType.getId());
            String s = reverseString(site, errorTypeCode);
            maintenanceMethod.setErrorTypeFullIds(s);
            maintenanceMethod.setCreateUser(userName);
            maintenanceMethod.setCreateTime(date);
            maintenanceMethod.setPlace(x.getPlace());
            maintenanceMethod.setMethod(x.getMethod());
            maintenanceMethod.setRemark(x.getRemark());

           // Assert.valid(checkCodeExists(maintenanceMethod), "维修措施编码已存在");
            baseMapper.insert(maintenanceMethod);
        });
    }

    private Predicate<? super CorrectiveMaintenanceVo> distinctByKey(Function<? super CorrectiveMaintenanceVo, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


    /**
     * 导出数据
     *
     * @param ids      ids
     * @param response response
     */
    @Override
    public void export(String ids, HttpServletResponse response) {

      String site = UserUtils.getSite();
        //  String site = "1040";

       // String  site = org.apache.commons.lang3.StringUtils.isEmpty(site) ? site : "1040";
        log.info("site ----->{}", site);
        List<CorrectiveMaintenanceVo> maintenanceMethodDos;
        if (StringUtils.isEmpty(ids)) {
            //获取所有的数据导出
            maintenanceMethodDos = baseMapper.queryList(site);
        } else {
            String[] split = ids.split(",");
            List<String> list = Arrays.asList(split);
            maintenanceMethodDos = baseMapper.queryListByIds(site, list);

        }


        ExcelUtils.exportExcel(maintenanceMethodDos, "维修措施", "维修措施列表", CorrectiveMaintenanceVo.class, "维修措施列表.xls", true, response);

    }



    /**
     * 获取异常分类
     *
     * @param site          site
     * @param errorTypeCode errorTypeCode
     * @return ErrorType.class
     */
    public ErrorType getErrorType(String site, String errorTypeCode) {

        LambdaQueryWrapper<ErrorType> queryWrapper = new QueryWrapper<ErrorType>().lambda()
                .eq(ErrorType::getErrorCode, errorTypeCode)
                .eq(ErrorType::getSite, site);
        ErrorType errorType = errorTypeMapper.selectOne(queryWrapper);
        if (null == errorType) {
            throw new CommonException("异常分类数据为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return errorType;
    }

    /**
     * 获取异常类型id路线
     *
     * @param site          site
     * @param errorTypeCode errorTypeCode
     * @return String.class
     */
    public String getErrorTypeFullIds(String site, String errorTypeCode) {
        StringBuffer stringBuffer = new StringBuffer();
        LambdaQueryWrapper<ErrorType> queryWrapper = new QueryWrapper<ErrorType>().lambda()
                .eq(ErrorType::getErrorCode, errorTypeCode)
                .eq(ErrorType::getSite, site);
        ErrorType errorType = errorTypeMapper.selectOne(queryWrapper);
        if (null == errorType) {
            throw new CommonException("异常分类数据为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        String parentId = errorType.getParentId();
        if (StringUtils.isEmpty(parentId)) {
            stringBuffer.append(errorType.getId());
            return stringBuffer.toString();
        } else {
            stringBuffer.append(errorType.getId());
            return getString(stringBuffer, parentId);
        }
    }

    /**
     * 循环遍历获取到父子数据
     *
     * @param stringBuffer stringBuffer
     * @param parentId     parentId
     * @return String.class
     */
    public String getString(StringBuffer stringBuffer, String parentId) {
        ErrorType errorType1 = errorTypeMapper.selectById(parentId);
        String parentId1 = errorType1.getParentId();
        stringBuffer.append("-");
        if (StringUtils.isEmpty(parentId1)) {

            stringBuffer.append(errorType1.getId());
            return stringBuffer.toString();
        } else {
            stringBuffer.append(errorType1.getId());
            return getString(stringBuffer, parentId1);
        }
    }

    /**
     * 反转字符串
     *
     * @param site          site
     * @param errorTypeCode errorTypeFullIds
     * @return String.class
     */
    public String reverseString(String site, String errorTypeCode) {
        String errorTypeFullIds = this.getErrorTypeFullIds(site, errorTypeCode);
        StringBuilder stringBuffer = new StringBuilder();
        if (StringUtils.isNotEmpty(errorTypeFullIds)) {
             String[] split = errorTypeFullIds.split("-");

            for (int i = split.length - 1; i >= 0; i--) {
                stringBuffer.append(split[i]);
                if (i > 0) {
                    stringBuffer.append("-");
                }



            }
        }
        return stringBuffer.toString();
    }

}
