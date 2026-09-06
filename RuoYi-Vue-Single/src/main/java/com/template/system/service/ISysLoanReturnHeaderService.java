package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysLoanReturnHeader;

/**
 * 归还单-主Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysLoanReturnHeaderService 
{
    /**
     * 查询归还单-主
     * 
     * @param id 归还单-主主键
     * @return 归还单-主
     */
    public SysLoanReturnHeader selectSysLoanReturnHeaderById(Long id);

    /**
     * 查询归还单-主列表
     * 
     * @param sysLoanReturnHeader 归还单-主
     * @return 归还单-主集合
     */
    public List<SysLoanReturnHeader> selectSysLoanReturnHeaderList(SysLoanReturnHeader sysLoanReturnHeader);

    /**
     * 新增归还单-主
     * 
     * @param sysLoanReturnHeader 归还单-主
     * @return 结果
     */
    public int insertSysLoanReturnHeader(SysLoanReturnHeader sysLoanReturnHeader);

    /**
     * 修改归还单-主
     * 
     * @param sysLoanReturnHeader 归还单-主
     * @return 结果
     */
    public int updateSysLoanReturnHeader(SysLoanReturnHeader sysLoanReturnHeader);

    /**
     * 批量删除归还单-主
     * 
     * @param ids 需要删除的归还单-主主键集合
     * @return 结果
     */
    public int deleteSysLoanReturnHeaderByIds(Long[] ids);

    /**
     * 删除归还单-主信息
     * 
     * @param id 归还单-主主键
     * @return 结果
     */
    public int deleteSysLoanReturnHeaderById(Long id);
}
