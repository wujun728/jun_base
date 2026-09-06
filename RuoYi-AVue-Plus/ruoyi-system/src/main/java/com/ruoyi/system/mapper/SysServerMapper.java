package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysServer;

/**
 * 服务注册Mapper接口
 * 
 * @author ruoyi
 * @date 2021-08-20
 */
public interface SysServerMapper 
{
    /**
     * 查询服务注册
     * 
     * @param id 服务注册主键
     * @return 服务注册
     */
    public SysServer selectSysServerById(Long id);

    /**
     * 查询服务注册列表
     * 
     * @param sysServer 服务注册
     * @return 服务注册集合
     */
    public List<SysServer> selectSysServerList(SysServer sysServer);

    /**
     * 新增服务注册
     * 
     * @param sysServer 服务注册
     * @return 结果
     */
    public int insertSysServer(SysServer sysServer);

    /**
     * 修改服务注册
     * 
     * @param sysServer 服务注册
     * @return 结果
     */
    public int updateSysServer(SysServer sysServer);

    /**
     * 删除服务注册
     * 
     * @param id 服务注册主键
     * @return 结果
     */
    public int deleteSysServerById(Long id);

    /**
     * 批量删除服务注册
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysServerByIds(Long[] ids);
}
