package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysLoanLine;

/**
 * 借出单-明细Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysLoanLineService 
{
    /**
     * 查询借出单-明细
     * 
     * @param id 借出单-明细主键
     * @return 借出单-明细
     */
    public SysLoanLine selectSysLoanLineById(Long id);

    /**
     * 查询借出单-明细列表
     * 
     * @param sysLoanLine 借出单-明细
     * @return 借出单-明细集合
     */
    public List<SysLoanLine> selectSysLoanLineList(SysLoanLine sysLoanLine);

    /**
     * 新增借出单-明细
     * 
     * @param sysLoanLine 借出单-明细
     * @return 结果
     */
    public int insertSysLoanLine(SysLoanLine sysLoanLine);

    /**
     * 修改借出单-明细
     * 
     * @param sysLoanLine 借出单-明细
     * @return 结果
     */
    public int updateSysLoanLine(SysLoanLine sysLoanLine);

    /**
     * 批量删除借出单-明细
     * 
     * @param ids 需要删除的借出单-明细主键集合
     * @return 结果
     */
    public int deleteSysLoanLineByIds(Long[] ids);

    /**
     * 删除借出单-明细信息
     * 
     * @param id 借出单-明细主键
     * @return 结果
     */
    public int deleteSysLoanLineById(Long id);
}
