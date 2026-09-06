package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysLoanHeader;

/**
 * 借出单-主Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysLoanHeaderService 
{
    /**
     * 查询借出单-主
     * 
     * @param id 借出单-主主键
     * @return 借出单-主
     */
    public SysLoanHeader selectSysLoanHeaderById(Long id);

    /**
     * 查询借出单-主列表
     * 
     * @param sysLoanHeader 借出单-主
     * @return 借出单-主集合
     */
    public List<SysLoanHeader> selectSysLoanHeaderList(SysLoanHeader sysLoanHeader);

    /**
     * 新增借出单-主
     * 
     * @param sysLoanHeader 借出单-主
     * @return 结果
     */
    public int insertSysLoanHeader(SysLoanHeader sysLoanHeader);

    /**
     * 修改借出单-主
     * 
     * @param sysLoanHeader 借出单-主
     * @return 结果
     */
    public int updateSysLoanHeader(SysLoanHeader sysLoanHeader);

    /**
     * 批量删除借出单-主
     * 
     * @param ids 需要删除的借出单-主主键集合
     * @return 结果
     */
    public int deleteSysLoanHeaderByIds(Long[] ids);

    /**
     * 删除借出单-主信息
     * 
     * @param id 借出单-主主键
     * @return 结果
     */
    public int deleteSysLoanHeaderById(Long id);
}
