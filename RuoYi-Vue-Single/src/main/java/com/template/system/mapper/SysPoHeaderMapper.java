package com.template.system.mapper;

import java.util.List;
import com.template.system.entity.SysPoHeader;

/**
 * 采购订单-主Mapper接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface SysPoHeaderMapper 
{
    /**
     * 查询采购订单-主
     * 
     * @param id 采购订单-主主键
     * @return 采购订单-主
     */
    public SysPoHeader selectSysPoHeaderById(Long id);

    /**
     * 查询采购订单-主列表
     * 
     * @param sysPoHeader 采购订单-主
     * @return 采购订单-主集合
     */
    public List<SysPoHeader> selectSysPoHeaderList(SysPoHeader sysPoHeader);

    /**
     * 新增采购订单-主
     * 
     * @param sysPoHeader 采购订单-主
     * @return 结果
     */
    public int insertSysPoHeader(SysPoHeader sysPoHeader);

    /**
     * 修改采购订单-主
     * 
     * @param sysPoHeader 采购订单-主
     * @return 结果
     */
    public int updateSysPoHeader(SysPoHeader sysPoHeader);

    /**
     * 删除采购订单-主
     * 
     * @param id 采购订单-主主键
     * @return 结果
     */
    public int deleteSysPoHeaderById(Long id);

    /**
     * 批量删除采购订单-主
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysPoHeaderByIds(Long[] ids);
}
