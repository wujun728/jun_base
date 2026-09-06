package com.template.system.mapper;

import java.util.List;
import com.template.system.entity.SysTransferLine;

/**
 * 调拨单-明细Mapper接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface SysTransferLineMapper 
{
    /**
     * 查询调拨单-明细
     * 
     * @param id 调拨单-明细主键
     * @return 调拨单-明细
     */
    public SysTransferLine selectSysTransferLineById(Long id);

    /**
     * 查询调拨单-明细列表
     * 
     * @param sysTransferLine 调拨单-明细
     * @return 调拨单-明细集合
     */
    public List<SysTransferLine> selectSysTransferLineList(SysTransferLine sysTransferLine);

    /**
     * 新增调拨单-明细
     * 
     * @param sysTransferLine 调拨单-明细
     * @return 结果
     */
    public int insertSysTransferLine(SysTransferLine sysTransferLine);

    /**
     * 修改调拨单-明细
     * 
     * @param sysTransferLine 调拨单-明细
     * @return 结果
     */
    public int updateSysTransferLine(SysTransferLine sysTransferLine);

    /**
     * 删除调拨单-明细
     * 
     * @param id 调拨单-明细主键
     * @return 结果
     */
    public int deleteSysTransferLineById(Long id);

    /**
     * 批量删除调拨单-明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysTransferLineByIds(Long[] ids);
}
