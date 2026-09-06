package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysInventoryBalanceMapper;
import com.template.system.entity.SysInventoryBalance;
import com.template.system.service.ISysInventoryBalanceService;

/**
 * 库存现势(按仓库+产品)Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysInventoryBalanceServiceImpl implements ISysInventoryBalanceService
{
    @Autowired
    private SysInventoryBalanceMapper sysInventoryBalanceMapper;

    /**
     * 查询库存现势(按仓库+产品)
     *
     * @param id 库存现势(按仓库+产品)主键
     * @return 库存现势(按仓库+产品)
     */
    @Override
    public SysInventoryBalance selectSysInventoryBalanceById(Long id)
    {
        return sysInventoryBalanceMapper.selectSysInventoryBalanceById(id);
    }

    /**
     * 查询库存现势(按仓库+产品)列表
     *
     * @param sysInventoryBalance 库存现势(按仓库+产品)
     * @return 库存现势(按仓库+产品)
     */
    @Override
    public List<SysInventoryBalance> selectSysInventoryBalanceList(SysInventoryBalance sysInventoryBalance)
    {
        return sysInventoryBalanceMapper.selectSysInventoryBalanceList(sysInventoryBalance);
    }

    /**
     * 新增库存现势(按仓库+产品)
     *
     * @param sysInventoryBalance 库存现势(按仓库+产品)
     * @return 结果
     */
    @Override
    public int insertSysInventoryBalance(SysInventoryBalance sysInventoryBalance)
    {
        sysInventoryBalance.setCreateTime(DateUtils.getNowDate());
        return sysInventoryBalanceMapper.insertSysInventoryBalance(sysInventoryBalance);
    }

    /**
     * 修改库存现势(按仓库+产品)
     *
     * @param sysInventoryBalance 库存现势(按仓库+产品)
     * @return 结果
     */
    @Override
    public int updateSysInventoryBalance(SysInventoryBalance sysInventoryBalance)
    {
        sysInventoryBalance.setUpdateTime(DateUtils.getNowDate());
        return sysInventoryBalanceMapper.updateSysInventoryBalance(sysInventoryBalance);
    }

    /**
     * 批量删除库存现势(按仓库+产品)
     *
     * @param ids 需要删除的库存现势(按仓库+产品)主键
     * @return 结果
     */
    @Override
    public int deleteSysInventoryBalanceByIds(Long[] ids)
    {
        return sysInventoryBalanceMapper.deleteSysInventoryBalanceByIds(ids);
    }

    /**
     * 删除库存现势(按仓库+产品)信息
     *
     * @param id 库存现势(按仓库+产品)主键
     * @return 结果
     */
    @Override
    public int deleteSysInventoryBalanceById(Long id)
    {
        return sysInventoryBalanceMapper.deleteSysInventoryBalanceById(id);
    }
}
