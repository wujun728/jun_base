package com.template.system.mapper;

import java.util.List;
import com.template.system.entity.SysWarehouse;

/**
 * 仓库Mapper接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface SysWarehouseMapper 
{
    /**
     * 查询仓库
     * 
     * @param id 仓库主键
     * @return 仓库
     */
    public SysWarehouse selectSysWarehouseById(Long id);

    /**
     * 查询仓库列表
     * 
     * @param sysWarehouse 仓库
     * @return 仓库集合
     */
    public List<SysWarehouse> selectSysWarehouseList(SysWarehouse sysWarehouse);

    /**
     * 新增仓库
     * 
     * @param sysWarehouse 仓库
     * @return 结果
     */
    public int insertSysWarehouse(SysWarehouse sysWarehouse);

    /**
     * 修改仓库
     * 
     * @param sysWarehouse 仓库
     * @return 结果
     */
    public int updateSysWarehouse(SysWarehouse sysWarehouse);

    /**
     * 删除仓库
     * 
     * @param id 仓库主键
     * @return 结果
     */
    public int deleteSysWarehouseById(Long id);

    /**
     * 批量删除仓库
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysWarehouseByIds(Long[] ids);
}
