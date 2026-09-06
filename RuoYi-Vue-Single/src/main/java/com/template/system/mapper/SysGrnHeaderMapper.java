package com.template.system.mapper;

import java.util.List;
import com.template.system.entity.SysGrnHeader;

/**
 * 采购进货单-主Mapper接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface SysGrnHeaderMapper 
{
    /**
     * 查询采购进货单-主
     * 
     * @param id 采购进货单-主主键
     * @return 采购进货单-主
     */
    public SysGrnHeader selectSysGrnHeaderById(Long id);

    /**
     * 查询采购进货单-主列表
     * 
     * @param sysGrnHeader 采购进货单-主
     * @return 采购进货单-主集合
     */
    public List<SysGrnHeader> selectSysGrnHeaderList(SysGrnHeader sysGrnHeader);

    /**
     * 新增采购进货单-主
     * 
     * @param sysGrnHeader 采购进货单-主
     * @return 结果
     */
    public int insertSysGrnHeader(SysGrnHeader sysGrnHeader);

    /**
     * 修改采购进货单-主
     * 
     * @param sysGrnHeader 采购进货单-主
     * @return 结果
     */
    public int updateSysGrnHeader(SysGrnHeader sysGrnHeader);

    /**
     * 删除采购进货单-主
     * 
     * @param id 采购进货单-主主键
     * @return 结果
     */
    public int deleteSysGrnHeaderById(Long id);

    /**
     * 批量删除采购进货单-主
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysGrnHeaderByIds(Long[] ids);
}
