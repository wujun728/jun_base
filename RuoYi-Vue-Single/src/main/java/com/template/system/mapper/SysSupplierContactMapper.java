package com.template.system.mapper;

import java.util.List;
import com.template.system.entity.SysSupplierContact;

/**
 * 供货商联系人Mapper接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface SysSupplierContactMapper 
{
    /**
     * 查询供货商联系人
     * 
     * @param id 供货商联系人主键
     * @return 供货商联系人
     */
    public SysSupplierContact selectSysSupplierContactById(Long id);

    /**
     * 查询供货商联系人列表
     * 
     * @param sysSupplierContact 供货商联系人
     * @return 供货商联系人集合
     */
    public List<SysSupplierContact> selectSysSupplierContactList(SysSupplierContact sysSupplierContact);

    /**
     * 新增供货商联系人
     * 
     * @param sysSupplierContact 供货商联系人
     * @return 结果
     */
    public int insertSysSupplierContact(SysSupplierContact sysSupplierContact);

    /**
     * 修改供货商联系人
     * 
     * @param sysSupplierContact 供货商联系人
     * @return 结果
     */
    public int updateSysSupplierContact(SysSupplierContact sysSupplierContact);

    /**
     * 删除供货商联系人
     * 
     * @param id 供货商联系人主键
     * @return 结果
     */
    public int deleteSysSupplierContactById(Long id);

    /**
     * 批量删除供货商联系人
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysSupplierContactByIds(Long[] ids);
}
