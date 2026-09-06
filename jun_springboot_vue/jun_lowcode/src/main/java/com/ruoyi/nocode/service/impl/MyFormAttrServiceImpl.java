package com.ruoyi.nocode.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.nocode.mapper.MyFormAttrMapper;
import com.ruoyi.nocode.domain.MyFormAttr;
import com.ruoyi.nocode.service.IMyFormAttrService;

/**
 * 表单属性Service业务层处理
 *
 * @date 2022-08-09
 */
@Service
public class MyFormAttrServiceImpl implements IMyFormAttrService
{
    @Autowired
    private MyFormAttrMapper myFormAttrMapper;

    /**
     * 查询表单属性
     *
     * @param id 表单属性主键
     * @return 表单属性
     */
    @Override
    public MyFormAttr selectMyFormAttrById(String id)
    {
        return myFormAttrMapper.selectMyFormAttrById(id);
    }

    /**
     * 查询表单属性列表
     *
     * @param myFormAttr 表单属性
     * @return 表单属性
     */
    @Override
    public List<MyFormAttr> selectMyFormAttrList(MyFormAttr myFormAttr)
    {
        return myFormAttrMapper.selectMyFormAttrList(myFormAttr);
    }

    /**
     * 新增表单属性
     *
     * @param myFormAttr 表单属性
     * @return 结果
     */
    @Override
    public int insertMyFormAttr(MyFormAttr myFormAttr)
    {
        myFormAttr.setCreateTime(DateUtils.getNowDate());
        return myFormAttrMapper.insertMyFormAttr(myFormAttr);
    }

    /**
     * 修改表单属性
     *
     * @param myFormAttr 表单属性
     * @return 结果
     */
    @Override
    public int updateMyFormAttr(MyFormAttr myFormAttr)
    {
        myFormAttr.setUpdateTime(DateUtils.getNowDate());
        return myFormAttrMapper.updateMyFormAttr(myFormAttr);
    }

    /**
     * 批量删除表单属性
     *
     * @param ids 需要删除的表单属性主键
     * @return 结果
     */
    @Override
    public int deleteMyFormAttrByIds(String[] ids)
    {
        return myFormAttrMapper.deleteMyFormAttrByIds(ids);
    }

    /**
     * 删除表单属性信息
     *
     * @param id 表单属性主键
     * @return 结果
     */
    @Override
    public int deleteMyFormAttrById(String id)
    {
        return myFormAttrMapper.deleteMyFormAttrById(id);
    }
}
