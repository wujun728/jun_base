package com.aidex.system.mapper;

import com.aidex.common.core.domain.entity.SysRole;
import com.aidex.common.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表 数据层
 * 
 * @author ruoyi
 */
public interface SysRoleMapper extends BaseMapper<SysRole>
{

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysRole> selectRolePermissionByUserId(@Param("DEL_FLAG_NORMAL") String DEL_FLAG_NORMAL, @Param("userId") String userId);

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    public List<SysRole> selectRoleAll();

    /**
     * 根据用户ID获取角色选择框列表
     * 
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    public List<String> selectRoleListByUserId(String userId);


    /**
     * 根据用户ID查询角色
     * 
     * @param userName 用户名
     * @return 角色列表
     */
    public List<SysRole> selectRolesByUserName(@Param("DEL_FLAG_NORMAL") String DEL_FLAG_NORMAL, @Param("userName") String userName);


    /**
     * 删除角色信息
     *
     * @param roleId 需要删除的角色ID
     * @return 结果
     */
    public int deleteRoleById(@Param("roleId") String roleId);

    /**
     * 批量删除角色信息
     * 
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    public int deleteRoleByIds(String[] roleIds);

    /**
     * 获取最大排序
     * @return
     */
    public Integer findMaxSort(SysRole sysRole);
}
