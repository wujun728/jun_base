package com.ruoyi.form.mapper;

import java.util.List;
import com.ruoyi.form.domain.FormData;

/**
 * 单数据Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-12
 */
public interface FormDataMapper 
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
     * 删除单数据
     * 
     * @param dataId 单数据主键
     * @return 结果
     */
    public int deleteFormDataByDataId(Long dataId);

    /**
     * 批量删除单数据
     * 
     * @param dataIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFormDataByDataIds(Long[] dataIds);
}
