package com.template.system.mapper;

import java.util.List;
import com.template.system.entity.SysReturnHeader;

/**
 * 退库单-主Mapper接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface SysReturnHeaderMapper 
{
    /**
     * 查询退库单-主
     * 
     * @param id 退库单-主主键
     * @return 退库单-主
     */
    public SysReturnHeader selectSysReturnHeaderById(Long id);

    /**
     * 查询退库单-主列表
     * 
     * @param sysReturnHeader 退库单-主
     * @return 退库单-主集合
     */
    public List<SysReturnHeader> selectSysReturnHeaderList(SysReturnHeader sysReturnHeader);

    /**
     * 新增退库单-主
     * 
     * @param sysReturnHeader 退库单-主
     * @return 结果
     */
    public int insertSysReturnHeader(SysReturnHeader sysReturnHeader);

    /**
     * 修改退库单-主
     * 
     * @param sysReturnHeader 退库单-主
     * @return 结果
     */
    public int updateSysReturnHeader(SysReturnHeader sysReturnHeader);

    /**
     * 删除退库单-主
     * 
     * @param id 退库单-主主键
     * @return 结果
     */
    public int deleteSysReturnHeaderById(Long id);

    /**
     * 批量删除退库单-主
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysReturnHeaderByIds(Long[] ids);
}
