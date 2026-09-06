package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysLoanReturnLine;

/**
 * 归还单-明细Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysLoanReturnLineService 
{
    /**
     * 查询归还单-明细
     * 
     * @param id 归还单-明细主键
     * @return 归还单-明细
     */
    public SysLoanReturnLine selectSysLoanReturnLineById(Long id);

    /**
     * 查询归还单-明细列表
     * 
     * @param sysLoanReturnLine 归还单-明细
     * @return 归还单-明细集合
     */
    public List<SysLoanReturnLine> selectSysLoanReturnLineList(SysLoanReturnLine sysLoanReturnLine);

    /**
     * 新增归还单-明细
     * 
     * @param sysLoanReturnLine 归还单-明细
     * @return 结果
     */
    public int insertSysLoanReturnLine(SysLoanReturnLine sysLoanReturnLine);

    /**
     * 修改归还单-明细
     * 
     * @param sysLoanReturnLine 归还单-明细
     * @return 结果
     */
    public int updateSysLoanReturnLine(SysLoanReturnLine sysLoanReturnLine);

    /**
     * 批量删除归还单-明细
     * 
     * @param ids 需要删除的归还单-明细主键集合
     * @return 结果
     */
    public int deleteSysLoanReturnLineByIds(Long[] ids);

    /**
     * 删除归还单-明细信息
     * 
     * @param id 归还单-明细主键
     * @return 结果
     */
    public int deleteSysLoanReturnLineById(Long id);
}
