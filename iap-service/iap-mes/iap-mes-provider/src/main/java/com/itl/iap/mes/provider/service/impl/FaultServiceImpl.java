package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResultCode;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.ErrorTypeDto;
import com.itl.iap.mes.api.dto.FaultDto;
import com.itl.iap.mes.api.entity.ErrorType;
import com.itl.iap.mes.api.entity.Fault;
import com.itl.iap.mes.api.service.ErrorTypeService;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.FaultMapper;
import com.itl.iap.mes.provider.mapper.LovEntryRepository;
import com.itl.iap.mes.provider.utils.ExcelReader;
import com.itl.iap.mes.provider.utils.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class FaultServiceImpl extends ServiceImpl<FaultMapper, Fault> {

    @Autowired
    private FaultMapper faultMapper;
    @Autowired
    LovEntryRepository lovEntryRepository;
    @Autowired
    private ErrorTypeService errorTypeService;

    public IPage<Fault> pageQuery(FaultDto faultDto) {
        faultDto.setSiteId(UserUtils.getSite());
        if (null == faultDto.getPage()) {
            faultDto.setPage(new Page(0, 10));
        }
        return faultMapper.pageQuery(faultDto.getPage(), faultDto);
    }
    public IPage<Fault> pageQueryByState(FaultDto faultDto) {
        faultDto.setSiteId(UserUtils.getSite());
        if (null == faultDto.getPage()) {
            faultDto.setPage(new Page(0, 10));
        }
        return faultMapper.pageQueryByState(faultDto.getPage(), faultDto);
    }

    public void saveFault(Fault fault) {
        String site = UserUtils.getSite();
        fault.setSiteId(site);
        String faultCode = fault.getFaultCode();
        if(StringUtils.isBlank(faultCode)){
            throw new CustomException(CommonCode.CODE_EMPTY);
        }
        QueryWrapper<Fault> faultQueryWrapper = new QueryWrapper<Fault>().eq("faultCode", faultCode);
        if(fault.getId() != null) faultQueryWrapper.ne("id", fault.getId());
        Fault result = faultMapper.selectOne(faultQueryWrapper);
        if(result != null){
            throw new CustomException(CommonCode.CODE_REPEAT);
        }

        // 异常分类type为空时，通过errorTypeID查询
        String errorCode = fault.getType();
        if(fault.getType() != null && !"".equals(fault.getType())){
            ErrorTypeDto errorTypeDto = new ErrorTypeDto();
            errorTypeDto.setSite(site);
            errorTypeDto.setErrorCode(errorCode);
            List<ErrorTypeDto> errorTypeDtoList = errorTypeService.listTree(errorTypeDto);
            if(errorTypeDtoList == null || "".equals(errorTypeDtoList)){
                throw new CustomException(CommonCode.ERROR_TYPE_CODE_EMPTY);
            }else if(errorTypeDtoList.size() < 1){
                throw new CustomException(CommonCode.ERROR_TYPE_CODE_EMPTY);
            }else if(errorTypeDtoList.size() > 1){
                throw new CustomException(CommonCode.ERROR_TYPE_CODE_UNIQUE);
            }
            String errorTypeIds = errorTypeDtoList.get(0).getId();
            String parentId = errorTypeDtoList.get(0).getParentId();
            String errorName = errorTypeDtoList.get(0).getErrorName();
            // 关联父级ID不为空时
            while (parentId != null && !"".equals(parentId)){
                errorTypeIds = parentId+"-"+ errorTypeIds;
                // 通过关联的父类ID获取父类信息
                ErrorType errorTypeObj = errorTypeService.getById(parentId);
                if(errorTypeObj != null && !"".equals(errorTypeObj)){
                    parentId = errorTypeObj.getParentId();
                }
            }
            fault.setErrorTypeId(errorTypeIds);
            fault.setErrorName(errorName);
        }
        /*if(fault.getErrorTypeId() != null && !"".equals(fault.getErrorTypeId())){
            ErrorType errorType = errorTypeService.getById(fault.getErrorTypeId());
            if(errorType == null || "".equals(errorType)){
                throw new CustomException(CommonCode.ERROR_TYPE_CODE_EMPTY);
            }
            fault.setErrorTypeId(errorType.getId());
            fault.setType(errorType.getErrorCode());
            fault.setErrorName(errorType.getErrorName());
        }else if(fault.getType() != null && !"".equals(fault.getType())){
            ErrorTypeDto errorTypeDto = new ErrorTypeDto();
            errorTypeDto.setSite(UserUtils.getSite());
            errorTypeDto.setErrorCode(fault.getType());
            List<ErrorTypeDto> errorTypeDtoList = errorTypeService.listTree(errorTypeDto);
            if(errorTypeDtoList == null || "".equals(errorTypeDtoList)){
                throw new CustomException(CommonCode.ERROR_TYPE_CODE_EMPTY);
                //throw new CommonException("未找到("+errorCode+")异常分类代码!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }else if(errorTypeDtoList.size() < 1){
                throw new CustomException(CommonCode.ERROR_TYPE_CODE_EMPTY);
            }else if(errorTypeDtoList.size() > 1){
                throw new CustomException(CommonCode.ERROR_TYPE_CODE_UNIQUE);
            }
            fault.setErrorTypeId(errorTypeDtoList.get(0).getId()); // 异常分类代码ID
            fault.setErrorName(errorTypeDtoList.get(0).getErrorName()); // 异常分类代码名称
        }*/

        if(fault.getId() != null){
            faultMapper.updateById(fault);
        }else{
            fault.setCreateTime(new Date());
            faultMapper.insert(fault);
        }
    }

    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id ->{
            faultMapper.deleteById(id);
        });
    }

    public Object findById(String code) {
        Fault fault = faultMapper.selectOne(new QueryWrapper<Fault>().eq("faultCode", code));
        return fault;
    }

    @Value("${file.path}")
    private String filePath;


    public void export(HttpServletRequest request, HttpServletResponse response) {
        String[] headers = {"工厂编码", "异常代码", "异常名称", "异常描述", "异常类别", "维修方法", "状态（0启用1关闭）"};
        QueryWrapper<Fault> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("siteId",UserUtils.getSite()).orderByDesc("createTime");
        List<Fault> faults = faultMapper.selectList(queryWrapper);

        List<Map<String, Object>> tableList = new ArrayList<>();
        faults.forEach(fault -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("planCode", UserUtils.getSite()); // 工厂编号
            map.put("faultCode", fault.getFaultCode()); // 异常代码
            map.put("faultName", fault.getFaultName()); // 异常名称
            map.put("remark", fault.getRemark()); // 异常描述
            map.put("type", fault.getType()); // 异常类别
            map.put("repairMethod",fault.getRepairMethod()); // 维修方法
            map.put("state", Constant.EnabledEnum.getItemName(fault.getState())); // 状态

            tableList.add(map);
        });
        String uuid = UUID.randomUUID().toString();
        FileUtils.createExcel(headers, tableList, filePath + uuid + ".xls");
        FileUtils.downExcel(response, filePath + uuid + ".xls");

    }

    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    @Transactional
    public void importFile(MultipartFile file) throws CommonException {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String site = UserUtils.getSite();

        Map<String, CommonCode> commonCodeMap = new HashMap<String, CommonCode>();
        // List<Map<String, CommonCode>> commonCodeList = new ArrayList<>();
        List<Map<String, Object>> readResult = ExcelReader.readExcel2Map(file.getOriginalFilename(),inputStream);
        readResult.forEach(stringObjectMap -> {
            String faultCode = stringObjectMap.get("faultCode") == null?"":stringObjectMap.get("faultCode").toString();
            if(StringUtils.isNotBlank(faultCode)){
                Fault result = faultMapper.selectOne(new QueryWrapper<Fault>().eq("faultCode", faultCode));

                if(result != null){
                    commonCodeMap.put(faultCode, CommonCode.CODE_REPEAT);
                    //throw new CustomException(CommonCode.CODE_REPEAT);
                    return;
                }

                String errorCode = stringObjectMap.get("type")==null?"":stringObjectMap.get("type").toString();

                Fault fault = new Fault();
                fault.setFaultCode(stringObjectMap.get("faultCode")==null?"":stringObjectMap.get("faultCode").toString());
                fault.setFaultName(stringObjectMap.get("faultName")==null?"":stringObjectMap.get("faultName").toString());
                fault.setRemark(stringObjectMap.get("remark")==null?"":stringObjectMap.get("remark").toString());
                fault.setType(errorCode); // 异常分类代码
                if(!"".equals(errorCode)){
                    ErrorTypeDto errorTypeDto = new ErrorTypeDto();
                    errorTypeDto.setSite(site);
                    errorTypeDto.setErrorCode(errorCode);
                    List<ErrorTypeDto> errorTypeDtoList = errorTypeService.listTree(errorTypeDto);
                    if(errorTypeDtoList == null || "".equals(errorTypeDtoList)){
                        commonCodeMap.put(faultCode, CommonCode.ERROR_TYPE_CODE_EMPTY);
                        //throw new CommonException(CommonCode.INVALID_PARAM);
                        //throw new CommonException("未找到("+errorCode+")异常分类代码!", CommonExceptionDefinition.BASIC_EXCEPTION);
                        return;
                    }else if(errorTypeDtoList.size() < 1){
                        commonCodeMap.put(faultCode, CommonCode.ERROR_TYPE_CODE_EMPTY);
                        //throw new CommonException("未找到("+errorCode+")异常分类代码!", CommonExceptionDefinition.BASIC_EXCEPTION);
                        return;
                    }else if(errorTypeDtoList.size() > 1){
                        commonCodeMap.put(faultCode, CommonCode.ERROR_TYPE_CODE_UNIQUE);
                        //throw new CommonException(errorCode+"：异常分类代码存在多条结果，请确保异常分类代码唯一!", CommonExceptionDefinition.BASIC_EXCEPTION);
                        return;
                    }
                    String errorTypeIds = errorTypeDtoList.get(0).getId();
                    String parentId = errorTypeDtoList.get(0).getParentId();

                    // 关联父级ID不为空时
                    while (parentId != null && !"".equals(parentId)){
                        errorTypeIds = parentId+"-"+ errorTypeIds;
                        // 通过关联的父类ID获取父类信息
                        ErrorType errorTypeObj = errorTypeService.getById(parentId);
                        if(errorTypeObj != null && !"".equals(errorTypeObj)){
                            parentId = errorTypeObj.getParentId();
                        }
                    }
                    // fault.setErrorTypeId(errorTypeDtoList.get(0).getId()); // 异常分类代码ID
                    fault.setErrorTypeId(errorTypeIds); // 异常分类代码IDS
                    fault.setErrorName(errorTypeDtoList.get(0).getErrorName()); // 异常分类代码名称
                }
                fault.setRepairMethod(stringObjectMap.get("repairMethod")==null?"":stringObjectMap.get("repairMethod").toString());
                if(stringObjectMap.get("state")!=null){
                    if(!isInteger(stringObjectMap.get("state").toString())){
                        commonCodeMap.put(faultCode, CommonCode.INVALID_PARAM);
                        //throw new CustomException(CommonCode.IS_NOT_NUM);
                        //throw new CommonException("state参数不合法！", CommonExceptionDefinition.BASIC_EXCEPTION);
                        return;
                    }
                    fault.setState(Integer.valueOf(stringObjectMap.get("state").toString()));
                }
                fault.setSiteId(UserUtils.getSite());
                fault.setCreateTime(new Date());
                faultMapper.insert(fault);
            }
        });
        if(!commonCodeMap.isEmpty()){
//            ResultCode resultCode = new ResultCode() {
//                @Override
//                public boolean success() {
//                    return false;
//                }
//
//                @Override
//                public int code() {
//                    return 999;
//                }
//
//                @Override
//                public String message() {
//                    return commonCodeMap.toString();
//                }
//            };
//            throw new CustomException(resultCode);
            throw new CommonException(commonCodeMap.toString(),999);
        }
    }

    public Map<String, Integer> countByErrorTypeIds(List<String> ids) {
        if(CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<Map<String, String>> list = baseMapper.queryCountMapByErrorTypeId(ids, UserUtils.getSite());
        if(CollUtil.isNotEmpty(list)) {
            return list.stream().collect(Collectors.toMap(e->e.get("id"), e->Integer.parseInt(e.get("count"))));
        }
        return Collections.emptyMap();
    }
}
