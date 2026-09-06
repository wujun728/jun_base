package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysPoLine;

/**
 * 采购订单-明细Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysPoLineService 
{
    /**
     * 查询采购订单-明细
     * 
     * @param id 采购订单-明细主键
     * @return 采购订单-明细
     */
    public SysPoLine selectSysPoLineById(Long id);

    /**
     * 查询采购订单-明细列表
     * 
     * @param sysPoLine 采购订单-明细
     * @return 采购订单-明细集合
     */
    public List<SysPoLine> selectSysPoLineList(SysPoLine sysPoLine);

    /**
     * 新增采购订单-明细
     * 
     * @param sysPoLine 采购订单-明细
     * @return 结果
     */
    public int insertSysPoLine(SysPoLine sysPoLine);

    /**
     * 修改采购订单-明细
     * 
     * @param sysPoLine 采购订单-明细
     * @return 结果
     */
    public int updateSysPoLine(SysPoLine sysPoLine);

    /**
     * 批量删除采购订单-明细
     * 
     * @param ids 需要删除的采购订单-明细主键集合
     * @return 结果
     */
    public int deleteSysPoLineByIds(Long[] ids);

    /**
     * 删除采购订单-明细信息
     * 
     * @param id 采购订单-明细主键
     * @return 结果
     */
    public int deleteSysPoLineById(Long id);
}
