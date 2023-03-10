package com.itl.mes.core.provider.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.constants.CommonConstants;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.iap.mes.client.service.ProveService;
import com.itl.mes.core.api.bo.OperationHandleBO;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.dto.ProveDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.OperationParam;
import com.itl.mes.core.api.vo.OperationVo;
import com.itl.mes.core.provider.mapper.OperationMapper;
import com.itl.mes.me.client.service.ActionService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * <p>
 * ????????? ???????????????
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */
@Service
@Transactional
public class OperationServiceImpl extends ServiceImpl<OperationMapper, Operation> implements OperationService {


    @Autowired
    private OperationMapper operationMapper;
    @Resource
    private ProductLineService productLineService;
    @Resource
    private RouterProcessService routerProcessService;
    @Autowired
    private NcGroupService ncGroupService;
    @Autowired
    private NcCodeService ncCodeService;
    @Autowired
    private StationService stationService;
    @Autowired
    private StationTypeService stationTypeService;
    @Autowired
    private CustomDataValService customDataValService;
    @Resource
    private UserUtil userUtil;

    @Autowired
    private ActionService actionService;
    @Autowired
    private OperationProveService operationProveService;
    @Autowired
    private ProveService proveService;

    @Override
    public List<Operation> selectList() {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
        //getEntityWrapper(entityWrapper, operation);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveByOperationVo(OperationVo operationVo) throws CommonException {
        String lineBo = null;
        Objects.requireNonNull(operationVo.getActionId(), "????????????????????????!");
        if (!StrUtil.isBlank(operationVo.getProductionLine())) {
            ProductLine productLine = productLineService.getExistProductLineByHandleBO(new ProductLineHandleBO(UserUtils.getSite(), operationVo.getProductionLine()));
            lineBo = productLine.getBo();
        }

//        else {
//            throw new CommonException("productionLine ???????????????UK??? ????????????", Integer.parseInt(ResultResponseEnum.WEAK_SUCCESS.getCode()));
//        }

        String ncGroupBo = null;
        if (!StrUtil.isBlank(operationVo.getNcGroup())) {
            NcGroup ncGroup = ncGroupService.selectByNcGroup(operationVo.getNcGroup());
            ncGroupBo = ncGroup.getBo();
        }

        String ncCodeBo = null;
        if (!StrUtil.isBlank(operationVo.getDefaultNcCode())) {
            NcCode ncCode = ncCodeService.selectByNcCode(operationVo.getDefaultNcCode());
            ncCodeBo = ncCode.getBo();
        }

        String stationBO = null;
        if (!StrUtil.isBlank(operationVo.getDefaultStation())) {
            Station station = stationService.selectByStation(operationVo.getDefaultStation());
            stationBO = station.getBo();
        }

        String stationTypeBO = null;
        if (!StrUtil.isBlank(operationVo.getStationType())) {
            StationType stationType = stationTypeService.selectByStationType(operationVo.getStationType());
            stationTypeBO = stationType.getBo();
        }
        OperationHandleBO operationHandleBO = new OperationHandleBO(UserUtils.getSite(), operationVo.getOperation(), operationVo.getVersion());
        String bo = operationHandleBO.getBo();
        Operation entityOperation = operationMapper.selectById(bo);
        Operation operation = new Operation();
        //??????????????????
        List<OperationParam> operationParams = operationVo.getOperationParams();
        operation.setOperationParamStr("");
        if(CollUtil.isNotEmpty(operationParams)) {
            operation.setOperationParamStr(JSONUtil.toJsonStr(operationParams));
        }
        //????????????????????????????????????
        operation.setIsCheckProveFlag(operationVo.getIsCheckProveFlag());

        if (entityOperation == null) {
            operation.setBo(bo);
            operation.setSite(UserUtils.getSite());
            operation.setOperation(operationVo.getOperation());
            operation.setOperationName(operationVo.getOperationName());
            operation.setOperationDesc(operationVo.getOperationDesc());
            operation.setOperationType(operationVo.getOperationType());
            operation.setVersion(operationVo.getVersion());
            operation.setProductionLineBo(lineBo);
            operation.setDefaultStationBo(stationBO);
            operation.setStationTypeBo(stationTypeBO);
            operation.setIsCurrentVersion(operationVo.getIsCurrentVersion());
            operation.setState(operationVo.getState());
            operation.setDefaultNcCodeBo(ncCodeBo);
            operation.setMaxTimes(operationVo.getMaxTimes());
            operation.setRepeatTestTimes(operationVo.getRepeatTestTimes());
            operation.setNcGroupBo(ncGroupBo);
            operation.setActionId(operationVo.getActionId());
            Date newDate = new Date();
            operation.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), newDate);
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(operation);
            if (validResult.hasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if (operation.getIsCurrentVersion().equals("Y")) {
                updateOperationVersionType(operation.getOperation());
            }
            operationMapper.insert(operation);
            operationVo.setModifyDate(newDate);
        } else {
            Date frontModifyDate = operationVo.getModifyDate();
            Date modifyDate = entityOperation.getModifyDate();
            CommonUtil.compareDateSame(frontModifyDate, modifyDate);

            operation.setBo(entityOperation.getBo());
            operation.setSite(UserUtils.getSite());
            operation.setOperation(operationVo.getOperation());
            operation.setOperationName(operationVo.getOperationName());
            operation.setOperationDesc(operationVo.getOperationDesc());
            operation.setOperationType(operationVo.getOperationType());
            operation.setVersion(operationVo.getVersion());
            operation.setProductionLineBo(lineBo);
            operation.setDefaultStationBo(stationBO);
            operation.setStationTypeBo(stationTypeBO);
            operation.setIsCurrentVersion(operationVo.getIsCurrentVersion());
            operation.setState(operationVo.getState());
            operation.setDefaultNcCodeBo(ncCodeBo);
            operation.setMaxTimes(operationVo.getMaxTimes());
            operation.setRepeatTestTimes(operationVo.getRepeatTestTimes());
            operation.setNcGroupBo(ncGroupBo);
            Date newDate = new Date();
            operation.setCreateDate(entityOperation.getCreateDate());
            operation.setCreateUser(entityOperation.getCreateUser());
            operation.setModifyUser(userUtil.getUser().getUserName());
            operation.setModifyDate(newDate);
            operation.setActionId(operationVo.getActionId());
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(operation);
            if (validResult.hasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if (operation.getIsCurrentVersion().equals("Y")) {
                updateOperationVersionType(operation.getOperation());
            }
            Integer update = operationMapper.updateById(operation);
            if (update == 0) {
                throw new CommonException("??????????????????????????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            operationVo.setModifyDate(newDate);
        }

        //?????????????????????
        if (operationVo.getCustomDataValVoList() != null && operationVo.getCustomDataValVoList().size() > 0) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(bo);
            customDataValRequest.setSite(UserUtils.getSite());
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.OPERATION.getDataType());
            customDataValRequest.setCustomDataValVoList(operationVo.getCustomDataValVoList());
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
        //????????????-????????????????????????
        List<ProveDto> proveDtoList = operationVo.getProveDtoList();
        List<OperationProve> operationProves = new ArrayList<>();
        if(CollUtil.isNotEmpty(proveDtoList)) {
            for (ProveDto prove : proveDtoList) {
                OperationProve item = new OperationProve();
                item.setProveId(prove.getProveId());
                item.setOperationBo(operation.getBo());
                operationProves.add(item);
            }
        }
        operationProveService.save(operation.getBo(), operationProves);

    }

    @Override
    public Operation selectByOperation(String operation, String version) throws CommonException {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
        entityWrapper.eq(Operation.OPERATION, operation);
        entityWrapper.eq(Operation.VERSION, version);
        entityWrapper.eq(Operation.SITE, UserUtils.getSite());
        List<Operation> operations = operationMapper.selectList(entityWrapper);
        if (operations.isEmpty()) {
            throw new CommonException("??????:" + operation + "," + version + "?????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        } else {
            return operations.get(0);
        }

    }

    /**
     * ????????????????????????
     *
     * @param site      ??????
     * @param operation ??????
     * @return Operation
     */
    @Override
    public Operation getBasicCurrentOperation(String site, String operation) {

        QueryWrapper<Operation> wrapper = new QueryWrapper<>();
        wrapper.eq(Operation.SITE, site).eq(Operation.OPERATION, operation).eq(Operation.IS_CURRENT_VERSION, "Y");
        List<Operation> operationList = operationMapper.selectList(wrapper);
        if (operationList.size() > 0) {
            return operationList.get(0);
        }
        return null;
    }

    @Override
    public OperationVo getOperationVoByOperationAndVersion(String operation, String version) throws CommonException {
        Operation operationEntity = selectByOperation(operation, version);
        ProductLine productLine = null;
//        if (!StringUtils.isEmpty(operationEntity.getProductionLineBo())) {
//            productLine = productLineService.getById(operationEntity.getProductionLineBo());
//        }
        Station station = null;
//        if (!StringUtils.isEmpty(operationEntity.getProductionLineBo())) {
        station = stationService.getById(operationEntity.getDefaultStationBo());
//        }
        StationType stationType = stationTypeService.getById(operationEntity.getStationTypeBo());
        NcCode ncCode = null;
//        if (!StringUtils.isEmpty(operationEntity.getProductionLineBo())) {
        ncCode = ncCodeService.getById(operationEntity.getDefaultNcCodeBo());
//        }
        NcGroup ncGroup = null;
        if (!StringUtils.isEmpty(operationEntity.getNcGroupBo())) {
            ncGroup = ncGroupService.getById(operationEntity.getNcGroupBo());
        }
        OperationVo operationVo = new OperationVo();
        if(StrUtil.isNotEmpty(operationEntity.getActionId())) {
            Optional.ofNullable(actionService.getById(operationEntity.getActionId())).ifPresent(x -> operationVo.setAction(x.getAction()));
        }
        BeanUtils.copyProperties(operationEntity, operationVo);
        //??????????????????
        if(StrUtil.isNotBlank(operationEntity.getOperationParamStr())) {
            try {
                List<OperationParam> operationParams = JSONUtil.toList(JSONUtil.parseArray(operationEntity.getOperationParamStr()), OperationParam.class);
                operationVo.setOperationParams(operationParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (productLine != null) {
            operationVo.setProductionLine(productLine.getProductLine());
        }
        if (stationType != null) {
            operationVo.setStationType(stationType.getStationType());
        }
        if (station != null) {
            operationVo.setDefaultStation(station.getStation());
        }
        if (ncCode != null) {
            operationVo.setDefaultNcCode(ncCode.getNcCode());
        }
        if (ncGroup != null) {
            operationVo.setNcGroup(ncGroup.getNcGroup());
        }
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), operationEntity.getBo(), CustomDataTypeEnum.OPERATION.getDataType());
        operationVo.setCustomDataAndValVoList(customDataAndValVos);
        if(StrUtil.isBlank(operationVo.getIsCheckProveFlag())) {
            operationVo.setIsCheckProveFlag(CommonConstants.FLAG_N);
        }
        //??????????????????
        List<ProveDto> proveDtos = operationProveService.getProveDtoListByOperationBo(operationEntity.getBo());
        operationVo.setProveDtoList(proveDtos);
        return operationVo;
    }

    @Override
    public List<Operation> selectOperation(String operation, String operationName, String operationDesc, String operationType, String productionLine, String version) {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
        entityWrapper.eq(Operation.SITE, UserUtils.getSite());
        entityWrapper.like(Operation.OPERATION, operation);
        entityWrapper.like(Operation.OPERATION_NAME, operationName);
        entityWrapper.like(Operation.OPERATION_DESC, operationDesc);
        entityWrapper.like(Operation.OPERATION_TYPE, operationType);
        entityWrapper.like(Operation.PRODUCTION_LINE_BO, productionLine);
        entityWrapper.like(Operation.VERSION, version);
        List<Operation> operations = operationMapper.selectList(entityWrapper);
        return operations;
    }

    @Override
    public List<Operation> selectByOperation(String operation) {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
        entityWrapper.eq(Operation.SITE, UserUtils.getSite());
        entityWrapper.like(Operation.OPERATION, operation);
        List<Operation> operations = operationMapper.selectList(entityWrapper);
        return operations;
    }

    @Override
    public List<Operation> selectByOperation(Map<String, String> map) {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
        String operation = map.get("operation");
        String operationName = map.get("operationName");
        String operationDesc = map.get("operationDesc");
        String version = map.get("version");
        entityWrapper.eq(Operation.SITE, UserUtils.getSite());
        entityWrapper.like(Operation.OPERATION, operation);
        entityWrapper.like(Operation.OPERATION_NAME, operationName);
        entityWrapper.like(Operation.OPERATION_DESC, operationDesc);
        entityWrapper.like(Operation.VERSION, version);
        List<Operation> operations = operationMapper.selectList(entityWrapper);
        return operations;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int delete(String operation, String version, String modifyDate) throws CommonException {
        OperationHandleBO operationHandleBO = new OperationHandleBO(UserUtils.getSite(), operation, version);
        Operation operationEntity = operationMapper.selectById(operationHandleBO.getBo());
        final List<RouterProcess> list = routerProcessService.list(new QueryWrapper<RouterProcess>().lambda()
                .eq(RouterProcess::getSite, UserUtils.getSite())
                .like(true, RouterProcess::getProcessInfo, operationHandleBO.getBo()));
        if (CollUtil.isNotEmpty(list)) {
            throw new CommonException("?????????:" + operation + "??????:" + version + "??????????????????, ????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (operationEntity == null) {
            throw new CommonException("?????????:" + operation + "??????:" + version + "?????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        CommonUtil.compareDateSame(DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS), operationEntity.getModifyDate());
        Integer deleteInt = operationMapper.deleteById(operationEntity.getBo());
        return deleteInt;
    }


    @Override
    public List<NcCode> selectByNcCode(String ncCode, String ncName, String ncDesc) throws CommonException {
        List<NcCode> ncCodes = ncCodeService.selectNcCode(ncCode, ncName, ncDesc);
        return ncCodes;
    }

    @Override
    public List<NcGroup> selectByNcGroup(String ncGroup, String ncGroupName, String ncGroupDesc) throws CommonException {
        List<NcGroup> ncGroups = ncGroupService.selectNcGroup(ncGroup, ncGroupName, ncGroupDesc);
        return ncGroups;
    }

    @Override
    public List<Station> selectStation(String station, String stationName, String stationDesc) throws CommonException {
        List<Station> stations = stationService.selectStation(station, stationName, stationDesc);
        return stations;
    }

    @Override
    public List<StationType> selectStationType(String stationType, String stationTypeName, String stationTypeDesc) throws CommonException {
        List<StationType> stationTypes = stationTypeService.selectStationType(stationType, stationTypeName, stationTypeDesc);
        return stationTypes;
    }

    @Override
    public void updateOperationVersionType(String operation) throws CommonException {
        Integer integer = operationMapper.updateOperationVersionType(UserUtils.getSite(), operation);
    }

    /**
     * ????????????????????????????????????????????????OPERATION????????????OPERATION_DESC???????????????
     *
     * @param site ??????
     * @return List<Map < String, Object>>
     */
    @Override
    public List<Map<String, Object>> selectCurrentVersionOperations(String site) {

        return operationMapper.selectCurrentVersionOperations(site);
    }

    @Override
    public List<Operation> getOperationByStationTypeBO(String stationTypeBO) throws CommonException {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
        entityWrapper.eq(Operation.STATION_TYPE_BO, stationTypeBO);
        List<Operation> operations = operationMapper.selectList(entityWrapper);
        return operations;
    }


    @Override
    public void exportOperation(String site, HttpServletResponse response) {
        Workbook workBook = null;
        try {
            //?????????????????????
            QueryWrapper<Operation> operationEntityWrapper = new QueryWrapper<>();
            operationEntityWrapper.eq(Item.SITE, site);
            List<Operation> operations = operationMapper.selectList(operationEntityWrapper);
            // ?????????????????????????????????excel???sheet?????????????????????
            ExportParams operationExportParams = new ExportParams();
            // ??????sheet?????????
            operationExportParams.setSheetName("??????");
            // ??????sheet1?????????map
            Map<String, Object> operationExportMap = new HashMap<>();
            // title????????????ExportParams????????????????????????ExportParams????????????sheetName
            operationExportMap.put("title", operationExportParams);
            // ?????????????????????????????????
            operationExportMap.put("entity", Operation.class);
            // sheet?????????????????????
            operationExportMap.put("data", operations);

            //??????customDataVal?????????
            List<CustomDataVal> customDataVals = customDataValService.selectList();
            ExportParams customDataValExportParams = new ExportParams();
            customDataValExportParams.setSheetName("?????????????????????");
            // ??????sheet4?????????map
            Map<String, Object> customDataValExportMap = new HashMap<>();
            customDataValExportMap.put("title", customDataValExportParams);
            customDataValExportMap.put("entity", CustomDataVal.class);
            customDataValExportMap.put("data", customDataVals);

            // ???sheet1???sheet2???sheet3?????????map????????????
            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(operationExportMap);
            sheetsList.add(customDataValExportMap);

            // ????????????
            workBook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            //String fileName = URLEncoder.encode("?????????.xls", "UTF-8");
            String fileName = "?????????.xls";
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workBook.write(response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<OperationParam> getOperationParamsById(String id) {
        List<OperationParam> operationParams = new ArrayList<>();
        if(StrUtil.isBlank(id)) {
            return operationParams;
        }
        Operation operation = lambdaQuery().select(Operation::getOperationParamStr).eq(Operation::getBo, id).one();
        if(operation != null && StrUtil.isNotBlank(operation.getOperationParamStr())) {
            try {
                operationParams = JSONUtil.toList(JSONUtil.parseArray(operation.getOperationParamStr()), OperationParam.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return operationParams;
    }
}
