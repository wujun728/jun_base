package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysWarehouseMapper;
import com.template.system.entity.SysWarehouse;
import com.template.system.service.ISysWarehouseService;

/**
 * 仓库Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysWarehouseServiceImpl implements ISysWarehouseService
{
    @Autowired
    private SysWarehouseMapper sysWarehouseMapper;

    /**
     * 查询仓库
     *
     * @param id 仓库主键
     * @return 仓库
     */
    @Override
    public SysWarehouse selectSysWarehouseById(Long id)
    {
        return sysWarehouseMapper.selectSysWarehouseById(id);
    }

    /**
     * 查询仓库列表
     *
     * @param sysWarehouse 仓库
     * @return 仓库
     */
    @Override
    public List<SysWarehouse> selectSysWarehouseList(SysWarehouse sysWarehouse)
    {
        return sysWarehouseMapper.selectSysWarehouseList(sysWarehouse);
    }

    /**
     * 新增仓库
     *
     * @param sysWarehouse 仓库
     * @return 结果
     */
    @Override
    public int insertSysWarehouse(SysWarehouse sysWarehouse)
    {
        sysWarehouse.setCreateTime(DateUtils.getNowDate());
        return sysWarehouseMapper.insertSysWarehouse(sysWarehouse);
    }

    /**
     * 修改仓库
     *
     * @param sysWarehouse 仓库
     * @return 结果
     */
    @Override
    public int updateSysWarehouse(SysWarehouse sysWarehouse)
    {
        sysWarehouse.setUpdateTime(DateUtils.getNowDate());
        return sysWarehouseMapper.updateSysWarehouse(sysWarehouse);
    }

    /**
     * 批量删除仓库
     *
     * @param ids 需要删除的仓库主键
     * @return 结果
     */
    @Override
    public int deleteSysWarehouseByIds(Long[] ids)
    {
        return sysWarehouseMapper.deleteSysWarehouseByIds(ids);
    }

    /**
     * 删除仓库信息
     *
     * @param id 仓库主键
     * @return 结果
     */
    @Override
    public int deleteSysWarehouseById(Long id)
    {
        return sysWarehouseMapper.deleteSysWarehouseById(id);
    }
}
