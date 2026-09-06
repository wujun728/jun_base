package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysSupplierContactMapper;
import com.template.system.entity.SysSupplierContact;
import com.template.system.service.ISysSupplierContactService;

/**
 * 供货商联系人Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysSupplierContactServiceImpl implements ISysSupplierContactService
{
    @Autowired
    private SysSupplierContactMapper sysSupplierContactMapper;

    /**
     * 查询供货商联系人
     *
     * @param id 供货商联系人主键
     * @return 供货商联系人
     */
    @Override
    public SysSupplierContact selectSysSupplierContactById(Long id)
    {
        return sysSupplierContactMapper.selectSysSupplierContactById(id);
    }

    /**
     * 查询供货商联系人列表
     *
     * @param sysSupplierContact 供货商联系人
     * @return 供货商联系人
     */
    @Override
    public List<SysSupplierContact> selectSysSupplierContactList(SysSupplierContact sysSupplierContact)
    {
        return sysSupplierContactMapper.selectSysSupplierContactList(sysSupplierContact);
    }

    /**
     * 新增供货商联系人
     *
     * @param sysSupplierContact 供货商联系人
     * @return 结果
     */
    @Override
    public int insertSysSupplierContact(SysSupplierContact sysSupplierContact)
    {
        sysSupplierContact.setCreateTime(DateUtils.getNowDate());
        return sysSupplierContactMapper.insertSysSupplierContact(sysSupplierContact);
    }

    /**
     * 修改供货商联系人
     *
     * @param sysSupplierContact 供货商联系人
     * @return 结果
     */
    @Override
    public int updateSysSupplierContact(SysSupplierContact sysSupplierContact)
    {
        sysSupplierContact.setUpdateTime(DateUtils.getNowDate());
        return sysSupplierContactMapper.updateSysSupplierContact(sysSupplierContact);
    }

    /**
     * 批量删除供货商联系人
     *
     * @param ids 需要删除的供货商联系人主键
     * @return 结果
     */
    @Override
    public int deleteSysSupplierContactByIds(Long[] ids)
    {
        return sysSupplierContactMapper.deleteSysSupplierContactByIds(ids);
    }

    /**
     * 删除供货商联系人信息
     *
     * @param id 供货商联系人主键
     * @return 结果
     */
    @Override
    public int deleteSysSupplierContactById(Long id)
    {
        return sysSupplierContactMapper.deleteSysSupplierContactById(id);
    }
}
