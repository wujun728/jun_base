package com.ruoyi.system.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.service.ISysDictDataService;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService
{
    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典数据
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public IPage<SysDictData> selectDictDataList(IPage<SysDictData> page, SysDictData dictData)
    {
        LambdaQueryWrapper<SysDictData> queryWrapper = new QueryWrapper<SysDictData>().lambda();
        queryWrapper.eq(StringUtils.isNotEmpty(dictData.getDictType()), SysDictData::getDictType, dictData.getDictType());
        queryWrapper.like(StringUtils.isNotEmpty(dictData.getDictLabel()), SysDictData::getDictLabel, dictData.getDictLabel());
        queryWrapper.eq(StringUtils.isNotEmpty(dictData.getStatus()), SysDictData::getStatus, dictData.getStatus());
        queryWrapper.orderByAsc(SysDictData::getDictSort);

        return dictDataMapper.selectPage(page, queryWrapper);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue)
    {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode)
    {
        return dictDataMapper.selectById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     * 
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes)
    {
        for (Long dictCode : dictCodes)
        {
            SysDictData data = selectDictDataById(dictCode);
            dictDataMapper.deleteById(dictCode);
            List<SysDictData> dictDatas = dictDataMapper.selectList(new QueryWrapper<SysDictData>().lambda().eq(SysDictData::getStatus, UserConstants.NORMAL).eq(SysDictData::getDictType, data.getDictType()).orderByAsc(SysDictData::getDictSort));
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
    }

    /**
     * 新增保存字典数据信息
     * 
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData data)
    {
        int row = dictDataMapper.insert(data);
        if (row > 0)
        {
            List<SysDictData> dictDatas = dictDataMapper.selectList(new QueryWrapper<SysDictData>().lambda().eq(SysDictData::getStatus, UserConstants.NORMAL).eq(SysDictData::getDictType, data.getDictType()).orderByAsc(SysDictData::getDictSort));;
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     * 
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysDictData data)
    {
        int row = dictDataMapper.updateById(data);
        if (row > 0)
        {
            List<SysDictData> dictDatas = dictDataMapper.selectList(new QueryWrapper<SysDictData>().lambda().eq(SysDictData::getStatus, UserConstants.NORMAL).eq(SysDictData::getDictType, data.getDictType()).orderByAsc(SysDictData::getDictSort));;
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }
}
