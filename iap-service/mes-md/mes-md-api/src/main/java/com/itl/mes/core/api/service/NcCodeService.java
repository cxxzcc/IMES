package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.api.vo.NcCodeVo;
import com.itl.mes.core.api.vo.NcGroupVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 不合格代码表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */
public interface NcCodeService extends IService<NcCode> {

    List<NcCode> selectList();

    void saveNcCode(NcCodeVo ncCodeVo)throws CommonException;

    void deleteNcCode(String ncCode, Date modifyDate)throws  CommonException;

    NcCode selectByNcCode(String ncCode)throws CommonException;

    NcCode selectByNcCode(String ncCode , String ncName , String ncType , String state)throws CommonException;

    List<NcCode> selectByNcCodeVo(NcCodeVo ncCodeVo)throws CommonException;

    List<NcCode> selectNcCode(String ncCode, String ncName, String ncDesc)throws CommonException;

    NcCodeVo getNcCodeVoByNcCode(String ncCode)throws CommonException;

    NcCodeVo getNcCodeVoByNcCode(String ncCode , String ncName , String ncType , String state)throws CommonException;

    List<NcGroupVo> getNcGroupVoList()throws CommonException;

    /**
     * 根据不合格代码组bo列表查询不合格代码组code集合
     * @param site 工厂id
     * @param ncgBos 不合格代码组bo列表
     * @return 不合格代码组
     * */
    List<NcCodeVo> getNcCodeByNcgBos(List<String> ncgBos, String site);

    /**
     * 根据id查询
     * @param site 工厂
     * @param ncCodeBos 不合格代码bo列表
     * */
    List<NcCode> getNcCodeByIds(List<String> ncCodeBos, String site);

}