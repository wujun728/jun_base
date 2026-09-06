package com.template.system.service;

import java.util.List;
import com.template.system.entity.SysInventoryBalance;

/**
 * 库存现势(按仓库+产品)Service接口
 * 
 * @author template
 * @date 2025-10-04
 */
public interface ISysInventoryBalanceService 
{
    /**
     * 查询库存现势(按仓库+产品)
     * 
     * @param id 库存现势(按仓库+产品)主键
     * @return 库存现势(按仓库+产品)
     */
    public SysInventoryBalance selectSysInventoryBalanceById(Long id);

    /**
     * 查询库存现势(按仓库+产品)列表
     * 
     * @param sysInventoryBalance 库存现势(按仓库+产品)
     * @return 库存现势(按仓库+产品)集合
     */
    public List<SysInventoryBalance> selectSysInventoryBalanceList(SysInventoryBalance sysInventoryBalance);

    /**
     * 新增库存现势(按仓库+产品)
     * 
     * @param sysInventoryBalance 库存现势(按仓库+产品)
     * @return 结果
     */
    public int insertSysInventoryBalance(SysInventoryBalance sysInventoryBalance);

    /**
     * 修改库存现势(按仓库+产品)
     * 
     * @param sysInventoryBalance 库存现势(按仓库+产品)
     * @return 结果
     */
    public int updateSysInventoryBalance(SysInventoryBalance sysInventoryBalance);

    /**
     * 批量删除库存现势(按仓库+产品)
     * 
     * @param ids 需要删除的库存现势(按仓库+产品)主键集合
     * @return 结果
     */
    public int deleteSysInventoryBalanceByIds(Long[] ids);

    /**
     * 删除库存现势(按仓库+产品)信息
     * 
     * @param id 库存现势(按仓库+产品)主键
     * @return 结果
     */
    public int deleteSysInventoryBalanceById(Long id);
}
