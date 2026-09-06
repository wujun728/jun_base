package com.aidex.system.service;

import com.aidex.common.core.domain.entity.SysRole;
import com.aidex.common.core.service.BaseService;

import java.util.List;
import java.util.Set;

/**
 * 角色业务层
 * 
 * @author ruoyi
 */
public interface ISysRoleService extends BaseService<SysRole>
{

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectRolePermissionByUserId(String userId);

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
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    public void checkRoleNameUnique(SysRole role);

    /**
     * 校验角色权限是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    public void checkRoleKeyUnique(SysRole role);

    /**
     * 校验角色是否允许操作
     * 
     * @param role 角色信息
     */
    public void checkRoleAllowed(SysRole role);

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(String roleId);

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(SysRole role);

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(SysRole role);

    /**
     * 修改角色状态
     * 
     * @param role 角色信息
     * @return 结果
     */
    public boolean updateRoleStatus(SysRole role);

    /**
     * 修改数据权限信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int authDataScope(SysRole role);

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleById(String roleId);

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
    public int findMaxSort(SysRole sysRole);

    public int batchSave(List<SysRole> roleList);

    public int deleteRoleUser(String roleId,String[] userIds);

    public int saveRolePortlet(SysRole role);

    /**
     * 校验角色是否有数据权限
     *
     * @param roleId 角色id
     */
    public void checkRoleDataScope(String roleId);
}
