package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.system.api.dto.IapDictItemTDto;
import com.itl.iap.system.api.entity.IapDictItemT;

import java.util.List;
import java.util.Map;

/**
 * 字典表详情表Service
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
public interface IapDictItemTService extends IService<IapDictItemT> {

    /**
     * 新增字典
     *
     * @param iapDictItemTDto 字典详情
     * @return 新增数量
     */
    Integer insertIapDictItemT(IapDictItemTDto iapDictItemTDto) throws CommonException;

    /**
     * 修改字典
     *
     * @param iapDictItemTDto 字典详情
     * @return 修改数量
     */
    Integer updateIapDictItemT(IapDictItemTDto iapDictItemTDto) throws CommonException;

    /**
     * 通过id批量删除数据
     *
     * @param ids 主键集合
     * @param parentCode 字典code
     * @return 是否成功
     */
    boolean deleteByIds(List<String> ids, String parentCode);

    /**
     * 通过字典编号和名称查询
     *
     * @param iapDictItemTDto 字典详情
     * @return 字典详情集合
     */
    List<IapDictItemTDto> queryDictCodeOrName(IapDictItemTDto iapDictItemTDto);

    /**
     * 通过字典编号查询
     *
     * @param dictCode 字典编码
     * @return 字典详情集合
     */
    List<IapDictItemTDto> queryDictCode(String dictCode);

    /**
     * 通过IapDictId查询
     *
     * @param ids 字典主表id集合
     * @return 字典详情集合
     */
    List<IapDictItemTDto> selectByIapDictIdList(List<String> ids);

    /**
     * 根据字典主表Code查字典详情取值
     *
     * @param code 字典主表code
     * @return 字典详情集合
     */
    List<IapDictItemT> getCode(String code);

    /**
     * 根据字典code查询字典项map。 Key=字典项keyvalue;value=字典项名称
     * @param code 字典编码code
     * @return Map.
     * */
    Map<String, String> getDictItemMapByParentCode(String code);

    /**
     * 根据字典项id查询字典项信息
     * @param id 字典项id
     * */
    List<IapDictItemTDto> getItemDtoByIds(List<String> id);
}
