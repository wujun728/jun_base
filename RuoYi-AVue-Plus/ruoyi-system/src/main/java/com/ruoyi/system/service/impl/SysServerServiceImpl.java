package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysServerMapper;
import com.ruoyi.system.domain.SysServer;
import com.ruoyi.system.service.ISysServerService;

/**
 * 服务注册Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-08-20
 */
@Service
public class SysServerServiceImpl implements ISysServerService 
{
    @Autowired
    private SysServerMapper sysServerMapper;

    /**
     * 查询服务注册
     * 
     * @param id 服务注册主键
     * @return 服务注册
     */
    @Override
    public SysServer selectSysServerById(Long id)
    {
        return sysServerMapper.selectSysServerById(id);
    }

    /**
     * 查询服务注册列表
     * 
     * @param sysServer 服务注册
     * @return 服务注册
     */
    @Override
    public List<SysServer> selectSysServerList(SysServer sysServer)
    {
        return sysServerMapper.selectSysServerList(sysServer);
    }

    /**
     * 新增服务注册
     * 
     * @param sysServer 服务注册
     * @return 结果
     */
    @Override
    public int insertSysServer(SysServer sysServer)
    {
        sysServer.setCreateTime(DateUtils.getNowDate());
        return sysServerMapper.insertSysServer(sysServer);
    }

    /**
     * 修改服务注册
     * 
     * @param sysServer 服务注册
     * @return 结果
     */
    @Override
    public int updateSysServer(SysServer sysServer)
    {
        sysServer.setUpdateTime(DateUtils.getNowDate());
        return sysServerMapper.updateSysServer(sysServer);
    }

    /**
     * 批量删除服务注册
     * 
     * @param ids 需要删除的服务注册主键
     * @return 结果
     */
    @Override
    public int deleteSysServerByIds(Long[] ids)
    {
        return sysServerMapper.deleteSysServerByIds(ids);
    }

    /**
     * 删除服务注册信息
     * 
     * @param id 服务注册主键
     * @return 结果
     */
    @Override
    public int deleteSysServerById(Long id)
    {
        return sysServerMapper.deleteSysServerById(id);
    }
}
