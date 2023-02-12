package com.itl.mes.andon.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.andon.api.bo.GradeHandleBO;
import com.itl.mes.andon.api.entity.Grade;
import com.itl.mes.andon.api.service.GradeService;
import com.itl.mes.andon.provider.mapper.GradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("gradeService")
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {

    @Autowired
    GradeMapper gradeMapper;
    @Autowired
    private UserUtil userUtil;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveGrade(Grade grade) throws CommonException {

        String site = UserUtils.getSite();
        Grade grade1 =
                gradeMapper.selectById(
                        new GradeHandleBO(site, grade.getAndonGrade()).getBo());

        grade.setBo(new GradeHandleBO(site, grade.getAndonGrade()).getBo());
        grade.setModifyUser(userUtil.getUser().getUserName());
        grade.setModifyDate(new Date());
        if (grade1 == null) {
            /* 保存 */
            this.verifyZone(grade.getStartTime(), grade.getEndTime(), this.lambdaQuery().eq(Grade::getSite, site).list());
            grade.setSite(site);
            grade.setCreateUser(userUtil.getUser().getUserName());
            gradeMapper.insert(grade);
        } else {
            /* 更新 */
            this.verifyZone(grade.getStartTime(), grade.getEndTime(), this.lambdaQuery().eq(Grade::getSite, site).ne(Grade::getBo, grade.getBo()).list());
            gradeMapper.updateById(grade);
        }
    }

    /**
     * 校验区间不重合
     * @param startTime 开始
     * @param endTime 结束
     * @param list 已有区间
     */
    private void verifyZone(Integer startTime, Integer endTime, List<Grade> list) {
        if(list.size() == 0) {
            return;
        }
        if (list.stream().anyMatch(x -> x.getStartTime() < endTime && x.getEndTime() > startTime)) {
            throw new RuntimeException("检测到有重合区间");
        }
    }
}
