package com.itl.mes.core.provider.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.*;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.Attached;
import com.itl.mes.core.api.entity.CustomDataVal;
import com.itl.mes.core.api.entity.QualityPlan;
import com.itl.mes.core.api.entity.QualityPlanParameter;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.QualityPlanAtParameterVo;
import com.itl.mes.core.provider.mapper.QualityPlanMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lsl
 * @since 2019-08-29
 */
@Service
@Transactional
public class QualityPlanServiceImpl extends ServiceImpl<QualityPlanMapper, QualityPlan> implements QualityPlanService {

    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private QualityPlanMapper qualityPlanMapper;

    @Autowired
    private QualityPlanParameterService qualityPlanParameterService;

    @Autowired
    private AttachedService attachedService;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private StationService stationService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemGroupService itemGroupService;

    @Autowired
    private CodeRuleService codeRuleService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private WorkShopService workShopService;

    @Resource
    private UserUtil userUtil;

    @Override
    public String getQPnumber() throws CommonException {
        String QPnumber = codeRuleService.generatorNextNumber(new CodeRuleHandleBO(UserUtils.getSite(), "QUALITYPLAN").getBo());
        return QPnumber;
    }

    /**
     * 保存或新增控制质量数据
     *
     * @param qpapVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public QualityPlan saveInUpdate(QualityPlanAtParameterVo qpapVO) throws CommonException {
        List<QualityPlanParameter> parameterList = qpapVO.getQpParameterList();

        if (parameterList == null || parameterList.size() == 0) {
            throw new CommonException("质量控制明细列表不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        String QualityPlan = qpapVO.getQualityPlan();
        String version = qpapVO.getVersion();
        String site = UserUtils.getSite();
        //控制明细表操作
        //判断请求内容是否重复
//        List<String> boList = new ArrayList<String>(); //存储明细表数据对应的BO
        String boParameter = new QualityPlanParameterBO(site, QualityPlan, version, parameterList.get(0).getParameterName()).getBo();
        int seq = parameterList.get(0).getSeq();
//        boList.add(boParameter);
        for (int i = 1; i < parameterList.size(); i++) {
            String bos = new QualityPlanParameterBO(site, QualityPlan, version, parameterList.get(i).getParameterName()).getBo();
            if (boParameter.equals(bos)) {
                throw new CommonException("质量控制明细列表第" + i + "行数据出现检验编号重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            int circulationSeq = parameterList.get(i).getSeq();
            if (seq == circulationSeq) {
                throw new CommonException("质量控制明细列表第" + i + "行数据出现序号重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            seq = circulationSeq;
            boParameter = bos;
            bos = null;
//            boList.add(boParameter);
        }
        //附加项操作
        List<Attached> attacheds = qpapVO.getAttachedList();
        if (attacheds != null) {
            List arr = attacheds.stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Attached::getContextBo))), ArrayList::new));
            //判断是否有重复的FieldValue
            if (attacheds.size() != arr.size()) {
                throw new CommonException("附加对象值不能重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        String bo = new QualityPlanHandleBO(site, QualityPlan, version).getBo();
        QualityPlan qualityPlanEntity = qualityPlanMapper.selectById(bo);
        QualityPlan newQualityPlan = new QualityPlan();
        //质量控制表操作,找不到则插入
        if (qualityPlanEntity == null) {
            newQualityPlan.setBo(bo);
            newQualityPlan.setVersion(version);
            newQualityPlan.setIsCurrentVersion(qpapVO.getIsCurrentVersion());
            newQualityPlan.setQualityPlan(QualityPlan);
            newQualityPlan.setQualityPlanDesc(qpapVO.getQualityPlanDesc());
            newQualityPlan.setSite(site);
            newQualityPlan.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(newQualityPlan);
            if (validResult.isHasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            QualityPlan qualityPlan = new QualityPlan();
            qualityPlan.setIsCurrentVersion("N");
            qualityPlanMapper.update(qualityPlan,
                    new QueryWrapper<QualityPlan>().eq("QUALITY_PLAN", newQualityPlan.getQualityPlan()));

            qualityPlanMapper.insert(newQualityPlan);
        } else {
            //否则更新数据库中对应的数据
            CommonUtil.compareDateSame(qpapVO.getModifyDate(), qualityPlanEntity.getModifyDate());
            newQualityPlan.setBo(bo);
            newQualityPlan.setVersion(version);
            newQualityPlan.setIsCurrentVersion(qpapVO.getIsCurrentVersion());
            newQualityPlan.setQualityPlan(QualityPlan);
            newQualityPlan.setQualityPlanDesc(qpapVO.getQualityPlanDesc());
            newQualityPlan.setSite(site);

            newQualityPlan.setCreateUser(qualityPlanEntity.getCreateUser());
            newQualityPlan.setCreateDate(qualityPlanEntity.getCreateDate());
            Date newDate = new Date();
            newQualityPlan.setModifyDate(newDate);
            newQualityPlan.setModifyUser(userUtil.getUser().getUserName());
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(newQualityPlan);
            if (validResult.isHasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            QualityPlan qualityPlan = new QualityPlan();
            qualityPlan.setIsCurrentVersion("N");
            qualityPlanMapper.update(qualityPlan,
                    new QueryWrapper<QualityPlan>().eq("QUALITY_PLAN", newQualityPlan.getQualityPlan()));

            qualityPlanMapper.updateById(newQualityPlan);
            qpapVO.setModifyDate(newDate);
        }
        Date date = new Date();
        for (int i = 0; i < parameterList.size(); i++) {
            QueryWrapper<QualityPlanParameter> QueryWrapper = new QueryWrapper<>();
            QueryWrapper.eq(QualityPlanParameter.SITE, site);
            QueryWrapper.eq(QualityPlanParameter.QUALITY_PLAN_BO, bo);
            qualityPlanParameterService.remove(QueryWrapper);
        }
        //for循环保存计划明细表数据
        for (int i = 0; i < parameterList.size(); i++) {
            String bos = new QualityPlanParameterBO(site, QualityPlan, version, parameterList.get(i).getParameterName()).getBo();
            QualityPlanParameter qpEntity = new QualityPlanParameter();
            QualityPlanParameter qp = parameterList.get(i);
            //新增数据
            qpEntity.setBo(bos);
            qpEntity.setSite(site);
            qpEntity.setQualityPlanBo(bo);
            qpEntity.setSeq(qp.getSeq());
            qpEntity.setParameterName(qp.getParameterName());
            qpEntity.setParameterDesc(qp.getParameterDesc());
            qpEntity.setAimVal(qp.getAimVal());
            qpEntity.setUpperLimit(qp.getUpperLimit());
            qpEntity.setLowerLimit(qp.getLowerLimit());
            qpEntity.setInspectType(qp.getInspectType());
            qpEntity.setInspectMethod(qp.getInspectMethod());
            qpEntity.setParameterType(qp.getParameterType());
            qpEntity.setInspectQty(qp.getInspectQty());
            qpEntity.setEnabled(qp.getEnabled());
            qpEntity.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), date);
//            qpEntity.setCreateDate(seletqpp.getCreateDate());
//            qpEntity.setCreateUser(seletqpp.getCreateUser());
            qpEntity.setModifyDate(date);
            qpEntity.setModifyUser(userUtil.getUser().getUserName());
            qualityPlanParameterService.save(qpEntity);
//            qualityPlanParameterService.updateById(qpEntity);
        }

        QueryWrapper<Attached> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq(Attached.SITE, site);
        QueryWrapper.eq(Attached.ATTACHED_FROM_BO, bo);
        attachedService.remove(QueryWrapper);

        //附加表数据
        try {
            if (attacheds.size() != 0) {
                int index = 0;
                for (Attached attached : attacheds) {
                    String contextBo = attached.getContextBo();
                    if (!StrUtil.isBlank(contextBo)) {
                        String[] singleStr = contextBo.split(",");
                        int count = singleStr.length;
                        for (int i = 0; i < count; i++) {
                            String[] dataTable = singleStr[i].split(":");
                            if (!"".equals(dataTable[1]) || !"/".equals(dataTable[1])) {
                                splitData(bo, dataTable[1], i, index, count, dataTable[0]);
                            }
                        }
                    }
                    ++index;
                }
            }
        } catch (NullPointerException e) {
        }
        //自定义数据
        if (qpapVO.getCustomDataValVoList() != null && qpapVO.getCustomDataValVoList().size() > 0) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(bo);
            customDataValRequest.setSite(site);
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.QUALITY_PLAN.getDataType());
            customDataValRequest.setCustomDataValVoList(qpapVO.getCustomDataValVoList());
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
        return qualityPlanMapper.selectById(bo);
    }

    /**
     * 附加项分割数据验证
     *
     * @param bo    控制质量计划BO ->ATTACHED_FROM_BO
     * @param right 编号 ->CONTEXT_BO
     * @param indx  判断对应哪个表数据 ->ATTACHED_KEY
     * @param index 同一条数据的序号 ->SEQ
     * @param count 附加总个数 ->COUNT_TOTAL
     */
    private void splitData(String bo, String right, int indx, int index, int count, String type) throws CommonException {
        String childBO = "";
        if ("PL".equals(type)) {
            ProductLineHandleBO pBo = new ProductLineHandleBO(UserUtils.getSite(), right);
            productLineService.getExistProductLineByHandleBO(pBo);
            childBO = pBo.getBo();
        }
        if ("OP".equals(type)) {
            String[] itemOper = right.split("/");
            if (itemOper.length == 1) {
                String version = operationService.getBasicCurrentOperation(UserUtils.getSite(), itemOper[0]).getVersion();
                childBO = new OperationHandleBO(UserUtils.getSite(), itemOper[0], version).getBo();
            } else if (itemOper.length == 2) {
                operationService.selectByOperation(itemOper[0], itemOper[1]);
                childBO = new OperationHandleBO(UserUtils.getSite(), itemOper[0], itemOper[1]).getBo();
            }
        }
        if ("ITEM".equals(type)) {
            String[] items = right.split("/");
            if (items.length == 1) {
                String version = itemService.selectByItemAndSite(items[0], UserUtils.getSite()).getVersion();
                childBO = new ItemHandleBO(UserUtils.getSite(), items[0], version).getBo();
            } else if (items.length == 2) {
                itemService.getExitsItemByItemHandleBO(new ItemHandleBO(UserUtils.getSite(), items[0], items[1]));
                childBO = new ItemHandleBO(UserUtils.getSite(), items[0], items[1]).getBo();
            }
        }
        if ("IG".equals(type)) {
            ItemGroupHandleBO itemGroupHandleBO = new ItemGroupHandleBO(UserUtils.getSite(), right);
            itemGroupService.getItemGroupByItemGroupBO(itemGroupHandleBO);
            childBO = itemGroupHandleBO.getBo();
        }
        if ("WS".equals(type)) {
            WorkShopHandleBO workShopHandleBO = new WorkShopHandleBO(UserUtils.getSite(), right);
            workShopService.getById(workShopHandleBO.getBo());
            childBO = workShopHandleBO.getBo();
        }
        Attached attached = new Attached();
        String newsbo = new AttachedBO(UserUtils.getSite(), bo, index + 1, indx).getBo();
        attached.setCountTotal(count);
        attached.setAttachedType("Q");
        attached.setContextBo(childBO);
        attached.setSeq(index + 1);
        attached.setAttachedKey(indx + 1);
        attached.setBo(newsbo);
        attached.setAttachedFromBo(bo);
        attached.setSite(UserUtils.getSite());
        attachedService.save(attached);
    }


    /**
     * 删除数据
     *
     * @param qualityPlan
     * @param version
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteQuality(String qualityPlan, String version, Date modifyDate) throws CommonException {
        QualityPlan qualityPlanEntity = selectByInspectType(qualityPlan, version);
        CommonUtil.compareDateSame(modifyDate, qualityPlanEntity.getModifyDate()); //验证时间
        qualityPlanMapper.deleteById(qualityPlanEntity.getBo());

        QueryWrapper<QualityPlanParameter> QueryWrapper = new QueryWrapper<QualityPlanParameter>();
        QueryWrapper.eq(QualityPlanParameter.SITE, UserUtils.getSite());
        QueryWrapper.eq(QualityPlanParameter.QUALITY_PLAN_BO, qualityPlanEntity.getBo());
        qualityPlanParameterService.remove(QueryWrapper);

        QueryWrapper<Attached> attachedQueryWrapper = new QueryWrapper<Attached>();
        attachedQueryWrapper.eq(Attached.SITE, UserUtils.getSite());
        attachedQueryWrapper.eq(Attached.ATTACHED_FROM_BO, qualityPlanEntity.getBo());
        attachedService.remove(attachedQueryWrapper);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map> selectQualityPlanPage(IPage<Map> page, Map<String, Object> params) {

        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map> qualityPlanPageList = qualityPlanMapper.selectQualityPlanPage(page, params);
        page.setRecords(qualityPlanPageList);
        return page;
    }

    /**
     * 查询检验类型数据Entity
     *
     * @param qualityPlan
     * @param version
     * @return
     * @throws CommonException
     */
    @Override
    public QualityPlan selectByInspectType(String qualityPlan, String version) throws CommonException {

        QueryWrapper<QualityPlan> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq(QualityPlan.QUALITY_PLAN, qualityPlan);
        QueryWrapper.eq(QualityPlan.VERSION, version);
        QueryWrapper.eq(QualityPlan.SITE, UserUtils.getSite());
        List<QualityPlan> list = qualityPlanMapper.selectList(QueryWrapper);
        if (list.isEmpty()) {
            throw new CommonException("检验类型编号:" + qualityPlan + "未维护或不是当前版本", CommonExceptionDefinition.BASIC_EXCEPTION);
        } else {
            return list.get(0);
        }
    }

    /**
     * 获取明细列表数据
     *
     * @param qualityPlan
     * @return
     */
    @Override
    public List<QualityPlanParameter> getParameterList(QualityPlan qualityPlan) throws CommonException {
        QueryWrapper<QualityPlanParameter> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq(QualityPlanParameter.SITE, UserUtils.getSite());
        QueryWrapper.eq(QualityPlanParameter.QUALITY_PLAN_BO, qualityPlan.getBo());
        List<QualityPlanParameter> list = qualityPlanParameterService.list(QueryWrapper);
        if (list.isEmpty()) {
//            throw new CommonException("检验类型编号:"+qualityPlan+"未维护或不是当前版本");
        }
        return list;
    }

    /**
     * 精确查询
     *
     * @param qualityPlan
     * @param versionn
     * @return
     * @throws CommonException
     */
    @Override
    public QualityPlanAtParameterVo getQpapVoByQualityPlan(String qualityPlan, String versionn) throws CommonException {
        QualityPlan qualityPlanEntity = selectByInspectType(qualityPlan, versionn);
        List<QualityPlanParameter> paraMeterlist = getParameterList(qualityPlanEntity);
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), qualityPlanEntity.getBo(), CustomDataTypeEnum.QUALITY_PLAN.getDataType());
        QualityPlanAtParameterVo qualityPlanAtParameterVo = new QualityPlanAtParameterVo();
        //拷贝属性
        BeanUtils.copyProperties(qualityPlanEntity, qualityPlanAtParameterVo);
        qualityPlanAtParameterVo.setQpParameterList(paraMeterlist);
        qualityPlanAtParameterVo.setCustomDataAndValVoList(customDataAndValVos);
        //附加项设置
        List<Attached> attacheds = getAttachList(qualityPlanEntity.getBo());
        qualityPlanAtParameterVo.setAttachedList(attacheds);
        return qualityPlanAtParameterVo;
    }

    /**
     * 获取已附加对象
     *
     * @return
     */
    public List<Attached> getAttachList(String bo) {
        QueryWrapper<Attached> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq(Attached.SITE, UserUtils.getSite());
        QueryWrapper.eq(Attached.ATTACHED_FROM_BO, bo);
        return attachedService.list(QueryWrapper);
    }

    /**
     * 导出文件
     *
     * @param site
     * @param response
     * @throws CommonException
     */
    @Override
    public void exportQplan(String site, HttpServletResponse response) throws CommonException {
        QueryWrapper<QualityPlan> qpQueryWrapper = new QueryWrapper<QualityPlan>();
        qpQueryWrapper.eq(QualityPlan.SITE, site);
        List<QualityPlan> qPlanList = qualityPlanMapper.selectList(qpQueryWrapper);

        // 创建参数对象（用来设定excel得sheet得内容等信息）
        ExportParams QualityPlanExport = new ExportParams();
        // 设置sheet得名称
        QualityPlanExport.setSheetName("质量控制计划表");
        // 创建sheet1使用得map
        Map<String, Object> qpExportMap = new HashMap<>();
        // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
        qpExportMap.put("title", QualityPlanExport);
        // 模版导出对应得实体类型
        qpExportMap.put("entity", QualityPlan.class);
        // sheet中要填充得数据
        qpExportMap.put("data", qPlanList);

        // 创建sheet2
        QueryWrapper<QualityPlanParameter> qppQueryWrapper = new QueryWrapper<QualityPlanParameter>();
        qppQueryWrapper.eq(QualityPlanParameter.SITE, site);
        List<QualityPlanParameter> qualityPlanParameterList = qualityPlanParameterService.list(qppQueryWrapper);

        ExportParams QualityPlanParameterExport = new ExportParams();
        QualityPlanParameterExport.setSheetName("计划明细表");

        Map<String, Object> qppExportMap = new HashMap<>();
        qppExportMap.put("title", QualityPlanParameterExport);
        qppExportMap.put("entity", QualityPlanParameter.class);
        qppExportMap.put("data", qualityPlanParameterList);

        //创建sheet3
        QueryWrapper<Attached> attachWrapper = new QueryWrapper<Attached>();
        qppQueryWrapper.eq(Attached.SITE, site);
        List<Attached> attachedList = attachedService.list(attachWrapper);

        ExportParams AttachedExport = new ExportParams();
        AttachedExport.setSheetName("附加数据表");
        Map<String, Object> attExportMap = new HashMap<>();
        attExportMap.put("title", AttachedExport);
        attExportMap.put("entity", Attached.class);
        attExportMap.put("data", attachedList);

        // 创建自定义数据-> map
        List<CustomDataVal> customDataValList = new ArrayList<>();
        for (QualityPlan qualityPlan : qPlanList) {
            String bo = new QualityPlanHandleBO(site, qualityPlan.getQualityPlan(), qualityPlan.getVersion()).getBo();
            QueryWrapper<CustomDataVal> customWrapper = new QueryWrapper<CustomDataVal>();
            customWrapper.eq(CustomDataVal.BO, bo);
            customDataValList.addAll(customDataValService.list(customWrapper));
        }
        ExportParams customExport = new ExportParams();
        customExport.setSheetName("自定义数据表");
        Map<String, Object> customMap = new HashMap<>();
        customMap.put("title", customExport);
        customMap.put("entity", CustomDataVal.class);
        customMap.put("data", customDataValList);

        // 将sheet1、sheet2、sheet3使用得map进行包装
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        sheetsList.add(qpExportMap);
        sheetsList.add(qppExportMap);
        sheetsList.add(attExportMap);
        sheetsList.add(customMap);
        // 执行方法
        ExcelUtils.exportExcel(sheetsList, "质量控制计划数据表", response);
    }

    /**
     * 导入文件
     *
     * @param file
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void importExcel(MultipartFile file) throws CommonException {

        List<QualityPlan> qualityPlanList = ExcelUtils.importExcel(file, 0, 1, QualityPlan.class);
        List<QualityPlanParameter> qualityPlanParameterList = ExcelUtils.importExcel(file, 1, 0, 1, QualityPlanParameter.class);
        List<Attached> attachedList = ExcelUtils.importExcel(file, 2, 0, 1, Attached.class);
        List<CustomDataVal> customDataValList = ExcelUtils.importExcel(file, 3, 0, 1, CustomDataVal.class);
        if (qualityPlanList.size() == 0) {
            throw new CommonException("质量控制计划表 数据不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        } else {
        }
        if (qualityPlanParameterList.size() == 0) {
            throw new CommonException("质量控制计划明细项 数据不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        } else {
        }
        super.saveBatch(qualityPlanList);
        qualityPlanParameterService.saveBatch(qualityPlanParameterList);

        if (attachedList.size() != 0) {
            attachedService.saveBatch(attachedList);
        }
        if (customDataValList.size() != 0) {

            customDataValService.saveBatch(customDataValList);

        }
    }


    @Override
    public List<QualityPlan> selectList() {
        QueryWrapper<QualityPlan> QueryWrapper = new QueryWrapper<QualityPlan>();
        //getQueryWrapper(QueryWrapper, qualityPlan);
        return super.list(QueryWrapper);
    }
}
