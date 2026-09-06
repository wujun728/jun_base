package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysStockCountLine;

/**
 * 盘点单-明细Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysStockCountLineService 
{
    /**
     * 查询盘点单-明细
     * 
     * @param id 盘点单-明细主键
     * @return 盘点单-明细
     */
    public SysStockCountLine selectSysStockCountLineById(Long id);

    /**
     * 查询盘点单-明细列表
     * 
     * @param sysStockCountLine 盘点单-明细
     * @return 盘点单-明细集合
     */
    public List<SysStockCountLine> selectSysStockCountLineList(SysStockCountLine sysStockCountLine);

    /**
     * 新增盘点单-明细
     * 
     * @param sysStockCountLine 盘点单-明细
     * @return 结果
     */
    public int insertSysStockCountLine(SysStockCountLine sysStockCountLine);

    /**
     * 修改盘点单-明细
     * 
     * @param sysStockCountLine 盘点单-明细
     * @return 结果
     */
    public int updateSysStockCountLine(SysStockCountLine sysStockCountLine);

    /**
     * 批量删除盘点单-明细
     * 
     * @param ids 需要删除的盘点单-明细主键集合
     * @return 结果
     */
    public int deleteSysStockCountLineByIds(Long[] ids);

    /**
     * 删除盘点单-明细信息
     * 
     * @param id 盘点单-明细主键
     * @return 结果
     */
    public int deleteSysStockCountLineById(Long id);
}
