package com.aidex.system.mapper;

import com.aidex.common.core.mapper.BaseMapper;
import com.aidex.system.domain.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典表 数据层
 * 
 * @author ruoyi
 */
public interface SysDictDataMapper extends BaseMapper<SysDictData>
{

    /**
     * 批量删除字典数据信息
     * 
     * @param dictDataIds 需要删除的字典数据ID
     * @return 结果
     */
    public int deleteDictDataByIds(@Param("dictDataIds") String[] dictDataIds, @Param("DEL_FLAG_DELETE") String DEL_FLAG_DELETE);

    /**
     * 同步修改字典类型
     * 
     * @param oldDictType 旧字典类型
     * @param newDictType 新旧字典类型
     * @return 结果
     */
    public int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);


    /**
     * 获取最大排序
     * @return
     */
    public Integer findMaxSort(SysDictData sysDictData);
}
