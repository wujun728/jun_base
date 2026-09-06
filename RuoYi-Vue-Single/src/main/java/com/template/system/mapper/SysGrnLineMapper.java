package com.template.system.mapper;

import java.util.List;
import com.template.system.entity.SysGrnLine;

/**
 * 采购进货单-明细Mapper接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface SysGrnLineMapper 
{
    /**
     * 查询采购进货单-明细
     * 
     * @param id 采购进货单-明细主键
     * @return 采购进货单-明细
     */
    public SysGrnLine selectSysGrnLineById(Long id);

    /**
     * 查询采购进货单-明细列表
     * 
     * @param sysGrnLine 采购进货单-明细
     * @return 采购进货单-明细集合
     */
    public List<SysGrnLine> selectSysGrnLineList(SysGrnLine sysGrnLine);

    /**
     * 新增采购进货单-明细
     * 
     * @param sysGrnLine 采购进货单-明细
     * @return 结果
     */
    public int insertSysGrnLine(SysGrnLine sysGrnLine);

    /**
     * 修改采购进货单-明细
     * 
     * @param sysGrnLine 采购进货单-明细
     * @return 结果
     */
    public int updateSysGrnLine(SysGrnLine sysGrnLine);

    /**
     * 删除采购进货单-明细
     * 
     * @param id 采购进货单-明细主键
     * @return 结果
     */
    public int deleteSysGrnLineById(Long id);

    /**
     * 批量删除采购进货单-明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysGrnLineByIds(Long[] ids);
}
