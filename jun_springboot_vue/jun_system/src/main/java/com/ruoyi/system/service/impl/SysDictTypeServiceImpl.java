package com.ruoyi.system.service.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.constant.SqlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.mapper.SysDictTypeMapper;
import com.ruoyi.system.service.ISysDictTypeService;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService
{
    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init()
    {
        loadingDictCache();
    }

    /**
     * 根据条件分页查询字典类型
     * 
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public IPage<SysDictType> selectDictTypeList(IPage<SysDictType> page, SysDictType dictType)
    {
        LambdaQueryWrapper<SysDictType> query = new QueryWrapper<SysDictType>().lambda();

        query.like(StringUtils.isNotEmpty(dictType.getDictName()), SysDictType::getDictName, dictType.getDictName());
        query.eq(StringUtils.isNotEmpty(dictType.getStatus()), SysDictType::getStatus, dictType.getStatus());
        query.like(StringUtils.isNotEmpty(dictType.getDictType()), SysDictType::getDictType, dictType.getDictType());
        if (StringUtils.isNotNull(dictType.getParams()) && StringUtils.isNotNull(dictType.getParams().get(SqlConstants.FIELD_NAME_BEGIN_TIME)) && StringUtils.isNotNull(dictType.getParams().get(SqlConstants.FIELD_NAME_END_TIME))) {
            query.between(SysDictType::getCreateTime, dictType.getParams().get(SqlConstants.FIELD_NAME_BEGIN_TIME), dictType.getParams().get(SqlConstants.FIELD_NAME_END_TIME));
        }

        return dictTypeMapper.selectPage(page, query);
    }

    /**
     * 根据所有字典类型
     * 
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeAll()
    {
        return dictTypeMapper.selectList(null);
    }

    /**
     * 根据字典类型查询字典数据
     * 
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType)
    {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas))
        {
            return dictDatas;
        }
        dictDatas = dictDataMapper.selectList(new QueryWrapper<SysDictData>().lambda().eq(SysDictData::getStatus, UserConstants.NORMAL).eq(SysDictData::getDictType, dictType).orderByAsc(SysDictData::getDictSort));;
        if (StringUtils.isNotEmpty(dictDatas))
        {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据字典类型ID查询信息
     * 
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeById(Long dictId)
    {
        return dictTypeMapper.selectById(dictId);
    }

    /**
     * 根据字典类型查询信息
     * 
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeByType(String dictType)
    {
        return dictTypeMapper.selectOne(new QueryWrapper<SysDictType>().lambda().eq(SysDictType::getDictType, dictType));
    }

    /**
     * 批量删除字典类型信息
     * 
     * @param dictIds 需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(Long[] dictIds)
    {
        for (Long dictId : dictIds)
        {
            SysDictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.selectCount(new QueryWrapper<SysDictData>().lambda().eq(SysDictData::getDictType, dictType.getDictType())) > 0)
            {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            dictTypeMapper.deleteBatchIds(Arrays.asList(dictIds));
            DictUtils.removeDictCache(dictType.getDictType());
        }
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache()
    {
        Map<String, List<SysDictData>> dictDataMap = dictDataMapper.selectList(new QueryWrapper<SysDictData>().lambda().eq(SysDictData::getStatus, UserConstants.NORMAL).orderByAsc(SysDictData::getDictSort)).stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet())
        {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
        }
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache()
    {
        DictUtils.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache()
    {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     * 
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(SysDictType dict)
    {
        int row = dictTypeMapper.insert(dict);
        if (row > 0)
        {
            DictUtils.setDictCache(dict.getDictType(), null);
        }
        return row;
    }

    /**
     * 修改保存字典类型信息
     * 
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDictType(SysDictType dict)
    {
        SysDictType oldDict = dictTypeMapper.selectById(dict.getDictId());
        dictDataMapper.update(new SysDictData(), new UpdateWrapper<SysDictData>().lambda().set(SysDictData::getDictType, dict.getDictType()).eq(SysDictData::getDictType, oldDict.getDictType()));
        int row = dictTypeMapper.updateById(dict);
        if (row > 0)
        {
            List<SysDictData> dictDatas = dictDataMapper.selectList(new QueryWrapper<SysDictData>().lambda().eq(SysDictData::getStatus, UserConstants.NORMAL).eq(SysDictData::getDictType, dict.getDictType()).orderByAsc(SysDictData::getDictSort));
            DictUtils.setDictCache(dict.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 校验字典类型称是否唯一
     * 
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public boolean checkDictTypeUnique(SysDictType dict)
    {
        Long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
        SysDictType dictType = dictTypeMapper.selectOne(new QueryWrapper<SysDictType>().lambda().eq(SysDictType::getDictType, dict.getDictType()));
        if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
