package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.StrNotNull;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CodeRuleFullVo;
import com.itl.mes.core.api.vo.CreateSnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author sky,
 * @date 2019/8/5
 * @time 15:36
 */
@Service
@Transactional
public class CreateSnServiceImpl implements CreateSnService {

    @Autowired
    private SnRuleService snRuleService;

    @Autowired
    private SnService snService;

    @Autowired
    private CodeRuleService codeRuleService;

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private ItemGroupService itemGroupService;

    @Autowired
    private SnLogService snLogService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemGroupMemberService itemGroupMemberService;

    @Resource
    private UserUtil userUtil;

    @Override
    public List<SnRule> querySnRuleByT() throws CommonException {
        QueryWrapper<SnRule> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(SnRule.RULE_TYPE, "T");
        List<SnRule> snRules = snRuleService.list(entityWrapper);
        if (snRules.size() == 0) {
            throw new CommonException("物料下拉框数据未维护!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return snRules;
    }

    @Override
    public List<SnRule> querySnRuleByS() throws CommonException {
        QueryWrapper<SnRule> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(SnRule.RULE_TYPE, "S");
        List<SnRule> snRules = snRuleService.list(entityWrapper);
        if (snRules.size() == 0) {
            throw new CommonException("工厂下拉框数据未维护!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return snRules;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void createSn(String number, String codeRuleType, Boolean whether, String shopOrder) throws CommonException {
        String site = UserUtils.getSite();
        CodeRule codeRule = codeRuleService.getExistCodeRule(new CodeRuleHandleBO(site, codeRuleType));

        List<Sn> sns = new ArrayList<>();
        int num = Integer.parseInt(number);

        if (num > 100000) {
            throw new CommonException("一次下达数量不能超过10万!",CommonExceptionDefinition.SN_ENCODING_EXCEPTION);
        }

        //获取工单信息
        ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(site, shopOrder));
        ItemHandleBO itemHandleBO = new ItemHandleBO(existShopOrder.getItemBo());

        HashMap<String, Object> params = new HashMap<>();
        CodeRuleFullVo fullVo = codeRuleService.getCodeRuleType(codeRuleType);
        fullVo.getCodeRuleItemVoList().forEach(ruleItem -> {
            if ("4".equals(ruleItem.getSectType())) {
                params.put(ruleItem.getSectParam(), itemHandleBO.getItem());
            }
        });
        //获取num个编码
        List<String> snsByRule = codeRuleService.generatorNextNumberList(codeRule.getBo(), num, params);

        //保存日志表
        SnLog snLog = new SnLog();
        if (whether) {
            for (int i = 1; i < num + 1; i++) {
                //保存到sn表
                String sn = snsByRule.get(i - 1);
                Sn snEntity = new Sn();
                snEntity.setBo(new SnHandleBO(site, sn).getBo());
                snEntity.setSite(site);
                snEntity.setSn(sn);
                snEntity.setShopOrder(existShopOrder.getShopOrder());
                snEntity.setItemBo(existShopOrder.getItemBo());
                snEntity.setOriginalItemBo(existShopOrder.getItemBo());
                snEntity.setState("401");
                snEntity.setQty(new BigDecimal(1));
                snEntity.setComplementCodeState("Y");
                snEntity.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
                sns.add(snEntity);

                if (i == 1) {
                    snLog.setStartNumber(snEntity.getSn());
                }
                if (i == num) {
                    snLog.setEndNumber(snEntity.getSn());
                }
            }
        } else {
            for (int i = 1; i < num + 1; i++) {
                String sn = snsByRule.get(i - 1);
                //保存到sn表
                Sn snEntity = new Sn();
                snEntity.setBo(new SnHandleBO(site, sn).getBo());
                snEntity.setSite(site);
                snEntity.setSn(sn);
                snEntity.setShopOrder(existShopOrder.getShopOrder());
                snEntity.setItemBo(existShopOrder.getItemBo());
                snEntity.setOriginalItemBo(existShopOrder.getItemBo());
                snEntity.setState("401");
                snEntity.setQty(new BigDecimal(1));
                snEntity.setComplementCodeState("N");
                snEntity.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
                sns.add(snEntity);

                if (i == 1) {
                    snLog.setStartNumber(snEntity.getSn());
                }
                if (i == num) {
                    snLog.setEndNumber(snEntity.getSn());
                }
            }
        }
        try {
            snService.saveBatch(sns);

            snLog.setBo(UUID.randomUUID().toString());
            snLog.setSite(UserUtils.getSite());

            snLog.setItem(itemHandleBO.getItem());
            //获取一个物料组
            List<String> assignedItemGroupList = itemGroupService.getAssignedItemGroupListBySiteAndItemBO(new ItemHandleBO(existShopOrder.getItemBo()));
            if (assignedItemGroupList.size() > 0) {
                snLog.setMaterialType(assignedItemGroupList.get(0));
            }
            snLog.setShopOrder(existShopOrder.getShopOrder());
            snLog.setCreateQuantity(num);
            snLog.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
            snLogService.save(snLog);
        } catch (Exception e) {
            throw new CommonException("对应规则生成条码会重复!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }


    @Override
    public IPage<Map<String, Object>> selectPageByShape(IPage<Map<String, Object>> page, Map<String, Object> params) {

        List<Map<String, Object>> list = snService.selectPageShopOrderByShape(page, params);
        page.setRecords(list);
        return page;
    }


    @Override
    public String selectTypeByShopOrder(String shopOrder) throws CommonException {
        //根据工单物料找物料组自定义字段维护的物料类型
        ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(UserUtils.getSite(), shopOrder));
        Item exitesItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(existShopOrder.getItemBo()));
        QueryWrapper<ItemGroupMember> itemGroupMemberEntityWrapper = new QueryWrapper<>();
        itemGroupMemberEntityWrapper.eq(ItemGroupMember.ITEM_BO, exitesItem.getBo());
        List<ItemGroupMember> itemGroupMembers = itemGroupMemberService.list(itemGroupMemberEntityWrapper);
        String vals = null;
        if (itemGroupMembers.size() > 0) {
            ItemGroup itemGroup = itemGroupService.getItemGroupByItemGroupBO(new ItemGroupHandleBO(itemGroupMembers.get(0).getItemGroupBo()));
            //查询物料组自定义字段CODE
            vals = snService.getSelfDefiningData(UserUtils.getSite(), "ITEM_GROUP", "CODE", itemGroup.getBo());

        }
        //根据RULE_CODE 查找编码规则名称
        String ruleName = null;
        if (vals != null) {
            QueryWrapper<SnRule> snRuleEntityWrapper = new QueryWrapper<>();
            snRuleEntityWrapper.eq(SnRule.RULE_CODE, vals).eq(SnRule.RULE_TYPE, "T");
            List<SnRule> snRules = snRuleService.list(snRuleEntityWrapper);
            if (snRules.size() > 0) {
                ruleName = snRules.get(0).getRuleName();
            }
        }
        return ruleName;
    }

    @Override
    public void importExcel(MultipartFile file) throws CommonException {
        List<CreateSnVo> createSnVos = ExcelUtils.importExcel(file, 1, 1, CreateSnVo.class);
        if (createSnVos.size() > 0) {
            for (CreateSnVo createSnVo : createSnVos) {
                StrNotNull.validateNotNull(createSnVo.getNumber(),"计划数量不能为空" );
                StrNotNull.validateNotNull(createSnVo.getCodeRuleType(),"编码规则不能为空" );
                StrNotNull.validateNotNull(createSnVo.getShopOrder(),"工单号不能为空" );
                //StrNotNull.validateNotNull(createSnVo.getWhether(),"是否补码不能为空" );
                //是否补码 默认为 否
                Boolean whether = false;
                if ("是".equals(createSnVo.getWhether())) {
                    whether = true;
                }
                createSn(createSnVo.getNumber(),createSnVo.getCodeRuleType(),whether,createSnVo.getShopOrder());
            }
        }
    }


    @Override
    public void exportExcel(String site, HttpServletResponse response) throws CommonException {
        List<CreateSnVo> createSnVos = new ArrayList<>();
        ExcelUtils.exportExcel(createSnVos, "条码生成模板", "条码生成模板", CreateSnVo.class, "条码生成模板.xls", response);
    }

}
