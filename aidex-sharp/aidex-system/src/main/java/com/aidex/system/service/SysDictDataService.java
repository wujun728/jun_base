package com.aidex.system.service;

import com.aidex.common.core.service.BaseService;
import com.aidex.system.domain.SysDictData;

/**
 * 字典 业务层
 *
 * @author ruoyi
 */
public interface SysDictDataService extends BaseService<SysDictData>
{

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String selectDictLabel(String dictType, String dictValue);


    /**
     * 批量删除字典数据信息
     *
     * @param dictDataIds 需要删除的字典数据ID
     * @return 结果
     */
    public void deleteDictDataByIds(String[] dictDataIds);

    /**
     * 校验字典值是否唯一
     *
     * @param sysDictData 字典类型
     * @return 结果
     */
    public void checkDictDataValueUnique(SysDictData sysDictData);

    /**
     * 获取最大排序
     * @param sysDictData
     * @return
     */
    public int findMaxSort(SysDictData sysDictData);
}
