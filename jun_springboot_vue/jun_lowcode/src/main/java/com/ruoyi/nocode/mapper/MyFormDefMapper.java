package com.ruoyi.nocode.mapper;


import com.ruoyi.nocode.domain.MyFormDef;

import java.util.List;

/**
 * 单定义Mapper接口
 *
 * @date 2022-07-27
 */
public interface MyFormDefMapper
{
    /**
     * 查询单定义
     *
     * @param id 单定义ID
     * @return 单定义
     */
    public MyFormDef selectMyFormDefById(String id);

    public List<MyFormDef>  selectMyFormDefByProcKey(String refProcKey);

    /**
     * 查询单定义列表
     *
     * @param myFormDef 单定义
     * @return 单定义集合
     */
    public List<MyFormDef> selectMyFormDefList(MyFormDef myFormDef);

    /**
     * 新增单定义
     *
     * @param myFormDef 单定义
     * @return 结果
     */
    public int insertMyFormDef(MyFormDef myFormDef);

    /**
     * 修改单定义
     *
     * @param myFormDef 单定义
     * @return 结果
     */
    public int updateMyFormDef(MyFormDef myFormDef);

    /**
     * 删除单定义
     *
     * @param id 单定义ID
     * @return 结果
     */
    public int deleteMyFormDefById(String id);

    /**
     * 批量删除单定义
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMyFormDefByIds(String[] ids);
}
