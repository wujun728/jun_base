package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysSupplierContact;

/**
 * 供货商联系人Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysSupplierContactService 
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
     * 批量删除供货商联系人
     * 
     * @param ids 需要删除的供货商联系人主键集合
     * @return 结果
     */
    public int deleteSysSupplierContactByIds(Long[] ids);

    /**
     * 删除供货商联系人信息
     * 
     * @param id 供货商联系人主键
     * @return 结果
     */
    public int deleteSysSupplierContactById(Long id);
}
