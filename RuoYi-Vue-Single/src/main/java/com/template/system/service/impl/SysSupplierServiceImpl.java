package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysSupplierMapper;
import com.template.system.entity.SysSupplier;
import com.template.system.service.ISysSupplierService;

/**
 * 供货商Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysSupplierServiceImpl implements ISysSupplierService
{
    @Autowired
    private SysSupplierMapper sysSupplierMapper;

    /**
     * 查询供货商
     *
     * @param id 供货商主键
     * @return 供货商
     */
    @Override
    public SysSupplier selectSysSupplierById(Long id)
    {
        return sysSupplierMapper.selectSysSupplierById(id);
    }

    /**
     * 查询供货商列表
     *
     * @param sysSupplier 供货商
     * @return 供货商
     */
    @Override
    public List<SysSupplier> selectSysSupplierList(SysSupplier sysSupplier)
    {
        return sysSupplierMapper.selectSysSupplierList(sysSupplier);
    }

    /**
     * 新增供货商
     *
     * @param sysSupplier 供货商
     * @return 结果
     */
    @Override
    public int insertSysSupplier(SysSupplier sysSupplier)
    {
        sysSupplier.setCreateTime(DateUtils.getNowDate());
        return sysSupplierMapper.insertSysSupplier(sysSupplier);
    }

    /**
     * 修改供货商
     *
     * @param sysSupplier 供货商
     * @return 结果
     */
    @Override
    public int updateSysSupplier(SysSupplier sysSupplier)
    {
        sysSupplier.setUpdateTime(DateUtils.getNowDate());
        return sysSupplierMapper.updateSysSupplier(sysSupplier);
    }

    /**
     * 批量删除供货商
     *
     * @param ids 需要删除的供货商主键
     * @return 结果
     */
    @Override
    public int deleteSysSupplierByIds(Long[] ids)
    {
        return sysSupplierMapper.deleteSysSupplierByIds(ids);
    }

    /**
     * 删除供货商信息
     *
     * @param id 供货商主键
     * @return 结果
     */
    @Override
    public int deleteSysSupplierById(Long id)
    {
        return sysSupplierMapper.deleteSysSupplierById(id);
    }
}
