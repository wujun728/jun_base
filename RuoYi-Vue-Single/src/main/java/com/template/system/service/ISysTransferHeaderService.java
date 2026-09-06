package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysTransferHeader;

/**
 * 调拨单-主Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysTransferHeaderService 
{
    /**
     * 查询调拨单-主
     * 
     * @param id 调拨单-主主键
     * @return 调拨单-主
     */
    public SysTransferHeader selectSysTransferHeaderById(Long id);

    /**
     * 查询调拨单-主列表
     * 
     * @param sysTransferHeader 调拨单-主
     * @return 调拨单-主集合
     */
    public List<SysTransferHeader> selectSysTransferHeaderList(SysTransferHeader sysTransferHeader);

    /**
     * 新增调拨单-主
     * 
     * @param sysTransferHeader 调拨单-主
     * @return 结果
     */
    public int insertSysTransferHeader(SysTransferHeader sysTransferHeader);

    /**
     * 修改调拨单-主
     * 
     * @param sysTransferHeader 调拨单-主
     * @return 结果
     */
    public int updateSysTransferHeader(SysTransferHeader sysTransferHeader);

    /**
     * 批量删除调拨单-主
     * 
     * @param ids 需要删除的调拨单-主主键集合
     * @return 结果
     */
    public int deleteSysTransferHeaderByIds(Long[] ids);

    /**
     * 删除调拨单-主信息
     * 
     * @param id 调拨单-主主键
     * @return 结果
     */
    public int deleteSysTransferHeaderById(Long id);
}
