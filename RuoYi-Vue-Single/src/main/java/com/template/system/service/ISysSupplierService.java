package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysSupplier;

/**
 * 供货商Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysSupplierService 
{
    /**
     * 查询供货商
     * 
     * @param id 供货商主键
     * @return 供货商
     */
    public SysSupplier selectSysSupplierById(Long id);

    /**
     * 查询供货商列表
     * 
     * @param sysSupplier 供货商
     * @return 供货商集合
     */
    public List<SysSupplier> selectSysSupplierList(SysSupplier sysSupplier);

    /**
     * 新增供货商
     * 
     * @param sysSupplier 供货商
     * @return 结果
     */
    public int insertSysSupplier(SysSupplier sysSupplier);

    /**
     * 修改供货商
     * 
     * @param sysSupplier 供货商
     * @return 结果
     */
    public int updateSysSupplier(SysSupplier sysSupplier);

    /**
     * 批量删除供货商
     * 
     * @param ids 需要删除的供货商主键集合
     * @return 结果
     */
    public int deleteSysSupplierByIds(Long[] ids);

    /**
     * 删除供货商信息
     * 
     * @param id 供货商主键
     * @return 结果
     */
    public int deleteSysSupplierById(Long id);
}
