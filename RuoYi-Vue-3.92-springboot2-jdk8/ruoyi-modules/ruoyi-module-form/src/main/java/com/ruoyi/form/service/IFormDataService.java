package com.ruoyi.form.service;

import java.util.List;
import com.ruoyi.form.domain.FormData;

/**
 * 单数据Service接口
 * 
 * @author ruoyi
 * @date 2025-05-12
 */
public interface IFormDataService 
{
    /**
     * 查询单数据
     * 
     * @param dataId 单数据主键
     * @return 单数据
     */
    public FormData selectFormDataByDataId(Long dataId);

    /**
     * 查询单数据列表
     * 
     * @param formData 单数据
     * @return 单数据集合
     */
    public List<FormData> selectFormDataList(FormData formData);

    /**
     * 新增单数据
     * 
     * @param formData 单数据
     * @return 结果
     */
    public int insertFormData(FormData formData);

    /**
     * 修改单数据
     * 
     * @param formData 单数据
     * @return 结果
     */
    public int updateFormData(FormData formData);

    /**
     * 批量删除单数据
     * 
     * @param dataIds 需要删除的单数据主键集合
     * @return 结果
     */
    public int deleteFormDataByDataIds(Long[] dataIds);

    /**
     * 删除单数据信息
     * 
     * @param dataId 单数据主键
     * @return 结果
     */
    public int deleteFormDataByDataId(Long dataId);
}
