package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysStockCountHeader;

/**
 * 盘点单-主Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysStockCountHeaderService 
{
    /**
     * 查询盘点单-主
     * 
     * @param id 盘点单-主主键
     * @return 盘点单-主
     */
    public SysStockCountHeader selectSysStockCountHeaderById(Long id);

    /**
     * 查询盘点单-主列表
     * 
     * @param sysStockCountHeader 盘点单-主
     * @return 盘点单-主集合
     */
    public List<SysStockCountHeader> selectSysStockCountHeaderList(SysStockCountHeader sysStockCountHeader);

    /**
     * 新增盘点单-主
     * 
     * @param sysStockCountHeader 盘点单-主
     * @return 结果
     */
    public int insertSysStockCountHeader(SysStockCountHeader sysStockCountHeader);

    /**
     * 修改盘点单-主
     * 
     * @param sysStockCountHeader 盘点单-主
     * @return 结果
     */
    public int updateSysStockCountHeader(SysStockCountHeader sysStockCountHeader);

    /**
     * 批量删除盘点单-主
     * 
     * @param ids 需要删除的盘点单-主主键集合
     * @return 结果
     */
    public int deleteSysStockCountHeaderByIds(Long[] ids);

    /**
     * 删除盘点单-主信息
     * 
     * @param id 盘点单-主主键
     * @return 结果
     */
    public int deleteSysStockCountHeaderById(Long id);
}
