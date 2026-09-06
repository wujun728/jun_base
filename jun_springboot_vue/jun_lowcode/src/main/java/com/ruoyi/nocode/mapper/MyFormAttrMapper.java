package com.ruoyi.nocode.mapper;

import java.util.List;
import com.ruoyi.nocode.domain.MyFormAttr;

/**
 * 表单属性Mapper接口
 *
 * @date 2022-08-09
 */
public interface MyFormAttrMapper
{
    /**
     * 查询表单属性
     *
     * @param id 表单属性主键
     * @return 表单属性
     */
    public MyFormAttr selectMyFormAttrById(String id);

    /**
     * 查询表单属性列表
     *
     * @param myFormAttr 表单属性
     * @return 表单属性集合
     */
    public List<MyFormAttr> selectMyFormAttrList(MyFormAttr myFormAttr);

    public List<MyFormAttr> selectShowAttrListByFormId(String formId);

    /**
     * 新增表单属性
     *
     * @param myFormAttr 表单属性
     * @return 结果
     */
    public int insertMyFormAttr(MyFormAttr myFormAttr);

    /**
     * 修改表单属性
     *
     * @param myFormAttr 表单属性
     * @return 结果
     */
    public int updateMyFormAttr(MyFormAttr myFormAttr);

    /**
     * 删除表单属性
     *
     * @param id 表单属性主键
     * @return 结果
     */
    public int deleteMyFormAttrById(String id);

    /**
     * 批量删除表单属性
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMyFormAttrByIds(String[] ids);

    public int deleteMyFormAttrByFormId(String formId);

    public int deleteMyFormAttrByFormIds(String[] formIds);
}
