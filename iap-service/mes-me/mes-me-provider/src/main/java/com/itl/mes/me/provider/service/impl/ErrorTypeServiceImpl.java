package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.constants.CommonConstants;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.TreeUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.me.api.entity.ErrorType;
import com.itl.mes.me.api.service.ErrorTypeService;
import com.itl.mes.me.provider.mapper.ErrorTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 异常代码维护服务实现类
 * @author dengou
 * @date 2021/11/3
 */
@Service
public class ErrorTypeServiceImpl extends ServiceImpl<ErrorTypeMapper, ErrorType> implements ErrorTypeService {

    @Override
    public List<ErrorType> getTree() {
        List<ErrorType> list = lambdaQuery().eq(ErrorType::getSite, UserUtils.getSite())
                .orderByDesc(ErrorType::getCreateTime)
                .list();
        return TreeUtils.buildTree(list);
    }

    @Override
    public List<ErrorType> getLovTree() {
        List<ErrorType> list = lambdaQuery().eq(ErrorType::getSite, UserUtils.getSite())
                .eq(ErrorType::getIsDisableFlag, CommonConstants.FLAG_Y)
                .orderByDesc(ErrorType::getCreateTime)
                .list();
        return TreeUtils.buildTree(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(ErrorType errorType) {
        Assert.valid(errorType == null, "参数不能为空");
        errorType.setId(null);

        //校验code是否存在
        Assert.valid(checkCodeExists(errorType), "异常代码已存在");

        if(StrUtil.isBlank(errorType.getSite())) {
            errorType.setSite(UserUtils.getSite());
        }
        if(StrUtil.isBlank(errorType.getParentId())) {
            errorType.setParentId(null);
        }
        errorType.setCreateTime(new Date());
        errorType.setCreateUser(UserUtils.getUserName());
        errorType.setUpdateTime(null);
        errorType.setUpdateUser(null);
        if(StrUtil.isBlank(errorType.getIsDisableFlag())) {
            errorType.setIsDisableFlag(CommonConstants.FLAG_Y);
        }

        return save(errorType);
    }

    /**
     * 校验code是否存在
     * @return 是否存在
     * */
    private Boolean checkCodeExists(ErrorType errorType) {
        LambdaQueryChainWrapper<ErrorType> wrapper = lambdaQuery().eq(ErrorType::getSite, UserUtils.getSite())
                .eq(ErrorType::getErrorCode, errorType.getErrorCode());
        if(StrUtil.isNotBlank(errorType.getId())) {
            wrapper.ne(ErrorType::getId, errorType.getId());
        }
        return wrapper.count() > 0;
    }

    @Override
    public ErrorType detail(String id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateErrorType(ErrorType errorType) {
        Assert.valid(errorType == null, "参数不能为空");
        Assert.valid(StrUtil.isBlank(errorType.getId()), "id不能为空");
        //校验异常类型是否存在
        Assert.valid(!checkErrorTypeExists(errorType.getId(), UserUtils.getSite()), "未找到异常类型");
        //校验code是否存在
        Assert.valid(checkCodeExists(errorType), "异常代码已存在");

        if(StrUtil.isBlank(errorType.getParentId())) {
            errorType.setParentId(null);
        }
        errorType.setCreateTime(null);
        errorType.setCreateUser(null);
        errorType.setUpdateTime(new Date());
        errorType.setUpdateUser(UserUtils.getUserName());
        if(StrUtil.isBlank(errorType.getIsDisableFlag())) {
            errorType.setIsDisableFlag(CommonConstants.FLAG_Y);
        }

        boolean update = updateById(errorType);
        Assert.valid(!update, "更新失败");
        return update;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(String id) {
        Assert.valid(StrUtil.isBlank(id), "id不能为空");
        //是否存在
        Assert.valid(!checkErrorTypeExists(id, UserUtils.getSite()), "未找到异常类型");
        //判断是否有子节点
        Integer childCount = lambdaQuery().eq(ErrorType::getSite, UserUtils.getSite()).eq(ErrorType::getParentId, id).count();
        Assert.valid(childCount > 0, "删除失败，该节点下还有"+ childCount + "个子节点");

        return removeById(id);
    }

    /**
     * 根据id和site查询异常类型是否存在
     * @param id id
     * @param site 工厂id
     * @return 是否存在
     * */
    private Boolean checkErrorTypeExists(String id, String site) {
        Integer count = lambdaQuery().eq(ErrorType::getSite, site).eq(ErrorType::getId, id).count();
        return count > 0;
    }



    /**
     * 获取异常分类
     *
     * @param site          site
     * @return List.class
     */
    public List<ErrorType> getErrorType(String site) {

        LambdaQueryWrapper<ErrorType> queryWrapper = new QueryWrapper<ErrorType>().lambda()

                .eq(ErrorType::getSite, site);
        List<ErrorType> errorTypes = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(errorTypes)) {
            throw new CommonException("异常分类数据为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return errorTypes;
    }
}
