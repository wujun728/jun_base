package com.template.system.mapper;

import java.util.List;
import com.template.system.entity.SysReturnLine;

/**
 * 退库单-明细Mapper接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface SysReturnLineMapper 
{
    /**
     * 查询退库单-明细
     * 
     * @param id 退库单-明细主键
     * @return 退库单-明细
     */
    public SysReturnLine selectSysReturnLineById(Long id);

    /**
     * 查询退库单-明细列表
     * 
     * @param sysReturnLine 退库单-明细
     * @return 退库单-明细集合
     */
    public List<SysReturnLine> selectSysReturnLineList(SysReturnLine sysReturnLine);

    /**
     * 新增退库单-明细
     * 
     * @param sysReturnLine 退库单-明细
     * @return 结果
     */
    public int insertSysReturnLine(SysReturnLine sysReturnLine);

    /**
     * 修改退库单-明细
     * 
     * @param sysReturnLine 退库单-明细
     * @return 结果
     */
    public int updateSysReturnLine(SysReturnLine sysReturnLine);

    /**
     * 删除退库单-明细
     * 
     * @param id 退库单-明细主键
     * @return 结果
     */
    public int deleteSysReturnLineById(Long id);

    /**
     * 批量删除退库单-明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysReturnLineByIds(Long[] ids);
}
