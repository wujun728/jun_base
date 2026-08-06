package com.ruoyi.form.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.form.mapper.FormDataMapper;
import com.ruoyi.form.domain.FormData;
import com.ruoyi.form.service.IFormDataService;

/**
 * 单数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-12
 */
@Service
public class FormDataServiceImpl implements IFormDataService 
{
    @Autowired
    private FormDataMapper formDataMapper;

    /**
     * 查询单数据
     * 
     * @param dataId 单数据主键
     * @return 单数据
     */
    @Override
    public FormData selectFormDataByDataId(Long dataId)
    {
        return formDataMapper.selectFormDataByDataId(dataId);
    }

    /**
     * 查询单数据列表
     * 
     * @param formData 单数据
     * @return 单数据
     */
    @Override
    public List<FormData> selectFormDataList(FormData formData)
    {
        return formDataMapper.selectFormDataList(formData);
    }

    /**
     * 新增单数据
     * 
     * @param formData 单数据
     * @return 结果
     */
    @Override
    public int insertFormData(FormData formData)
    {
        formData.setCreateTime(DateUtils.getNowDate());
        return formDataMapper.insertFormData(formData);
    }

    /**
     * 修改单数据
     * 
     * @param formData 单数据
     * @return 结果
     */
    @Override
    public int updateFormData(FormData formData)
    {
        formData.setUpdateTime(DateUtils.getNowDate());
        return formDataMapper.updateFormData(formData);
    }

    /**
     * 批量删除单数据
     * 
     * @param dataIds 需要删除的单数据主键
     * @return 结果
     */
    @Override
    public int deleteFormDataByDataIds(Long[] dataIds)
    {
        return formDataMapper.deleteFormDataByDataIds(dataIds);
    }

    /**
     * 删除单数据信息
     * 
     * @param dataId 单数据主键
     * @return 结果
     */
    @Override
    public int deleteFormDataByDataId(Long dataId)
    {
        return formDataMapper.deleteFormDataByDataId(dataId);
    }
}
