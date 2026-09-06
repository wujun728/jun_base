package com.aidex.system.service.impl;

import com.aidex.common.core.domain.BaseEntity;
import com.aidex.common.core.domain.entity.SysRole;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.common.exception.BizException;
import com.aidex.common.exception.CustomException;
import com.aidex.common.utils.NumberUtils;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.spring.SpringUtils;
import com.aidex.system.common.SysErrorCode;
import com.aidex.system.domain.SysRoleDept;
import com.aidex.system.domain.SysRoleMenu;
import com.aidex.system.mapper.SysRoleDeptMapper;
import com.aidex.system.mapper.SysRoleMapper;
import com.aidex.system.mapper.SysRoleMenuMapper;
import com.aidex.system.mapper.SysUserRoleMapper;
import com.aidex.system.service.ISysRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysRoleServiceImpl  extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired(required = false)
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired(required = false)
    private SysUserRoleMapper userRoleMapper;

    @Autowired(required = false)
    private SysRoleDeptMapper roleDeptMapper;

    @Autowired(required = false)
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(String userId) {
        List<SysRole> perms = mapper.selectRolePermissionByUserId(BaseEntity.DEL_FLAG_NORMAL,userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return SpringUtils.getAopProxy(this).findList(new SysRole());
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<String> selectRoleListByUserId(String userId) {
        return mapper.selectRoleListByUserId(userId);
    }


    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public void checkRoleNameUnique(SysRole role) {
        SysRole sysRoleUnique = new SysRole();
        sysRoleUnique.setNotEqualId(role.getId());
        sysRoleUnique.setRoleName(role.getRoleName());
        if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysRoleUnique))) {
            throw new BizException(SysErrorCode.B_SYSROLE_RoleNameAlreadyExist);
        }
    }

    /**
     * 校验角色权限是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public void checkRoleKeyUnique(SysRole role)
    {
        SysRole sysRoleUnique = new SysRole();
        sysRoleUnique.setNotEqualId(role.getId());
        sysRoleUnique.setRoleKey(role.getRoleKey());
        if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysRoleUnique))) {
            throw new BizException(SysErrorCode.B_SYSROLE_RoleKeyAlreadyExist);
        }
    }

    /**
     * 校验角色是否允许操作
     * 
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role)
    {
        if (StringUtils.isNotBlank(role.getId()) && role.isAdmin())
        {
            throw new CustomException("不允许操作超级管理员角色");
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(String roleId)
    {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 新增保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role)
    {
        checkRoleNameUnique(role);
        checkRoleKeyUnique(role);
        // 新增角色信息
        super.save(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role)
    {
        checkRoleNameUnique(role);
        checkRoleKeyUnique(role);
        // 修改角色信息
        super.save(role);

        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getId());
        return insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean updateRoleStatus(SysRole role)
    {
        return super.save(role);
    }

    /**
     * 修改数据权限信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role)
    {
        // 修改角色信息
        super.save(role);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(role.getId());

        if("2".equals(role.getDataScope())){
            // 新增角色和部门信息（数据权限）
            return insertRoleDept(role);
        } else {
            return 1;
        }

    }

    /**
     * 新增角色菜单信息
     * 
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role)
    {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (String menuId : role.getMenuIds())
        {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(SysRole role)
    {
        int rows = 1;
        // 新增角色与部门（数据权限）管理
        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (String deptId : role.getDeptIds())
        {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0)
        {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleById(String roleId)
    {
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleId(roleId);
        //删除角色与用户关联
        sysUserRoleMapper.deleteRoleUserByRoleId(roleId);
        return mapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色信息
     * 
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(String[] roleIds)
    {
        for (String roleId : roleIds)
        {
            checkRoleAllowed(new SysRole(roleId));
            SysRole role = get(roleId);
            if (countUserRoleByRoleId(roleId) > 0)
            {
                throw new CustomException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenu(roleIds);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDept(roleIds);
        return mapper.deleteRoleByIds(roleIds);
    }


    /**
     * 获取最大排序
     * @return
     */
    @Override
    public int findMaxSort(SysRole sysRole){
        return NumberUtils.nextOrder(mapper.findMaxSort(sysRole));
    }

    @Override
    @Transactional
    public int batchSave(List<SysRole> roleList)
    {
        if(!CollectionUtils.isEmpty(roleList)){
            for (SysRole sysRole: roleList) {
                if(StringUtils.isNotBlank(sysRole.getHandleType()) && "add".equals(sysRole.getHandleType())){
                  //获取最大编号
                    SysRole getMaxSortRole = new SysRole();
                    int maxSort = findMaxSort(getMaxSortRole);
                    sysRole.setSort(maxSort+"");
                    sysRole.setId("");//清空前台临时ID
                    sysRole.setDataScope("2");
                    sysRole.setMenuCheckStrictly(true);
                    sysRole.setDeptCheckStrictly(true);
                    super.save(sysRole);
                }else if(StringUtils.isNotBlank(sysRole.getHandleType()) && "edit".equals(sysRole.getHandleType())){
                    super.save(sysRole);
                }else if(StringUtils.isNotBlank(sysRole.getHandleType()) && "delete".equals(sysRole.getHandleType())){
                        //删除角色的同时需要删除对应的角色下用户
                    deleteRoleById(sysRole.getId());
                }
            }
        }
        return 1;
    }

    @Override
    @Transactional
    public int deleteRoleUser(String roleId,String[] userIds)
    {
        return sysUserRoleMapper.deleteUserRoleInfos(roleId,userIds);
    }

    /**
     * 给角色添加小页授权
     * @param role
     * @return
     */
    @Override
    @Transactional
    public int saveRolePortlet(SysRole role)
    {
        if("add".equals(role.getOption())){
            return insertRoleMenu(role);
        }else
        {
            return batchDelRoleMenu(role);
        }
    }

    /**
     * 批量删除角色下菜单授权
     *
     * @param role 角色对象
     */
    public int batchDelRoleMenu(SysRole role)
    {
        int rows = 1;
        if(role.getMenuIds() != null && role.getMenuIds().length>0){
            List<String> menuIdsList = new ArrayList<String>();
            Collections.addAll(menuIdsList, role.getMenuIds());
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(role.getId());
            sysRoleMenu.setMenuIdList(menuIdsList);
            rows = roleMenuMapper.batchDelRoleMenu(sysRoleMenu);
        }
        return rows;
    }

    /**
     * 校验角色是否有数据权限
     *
     * @param roleId 角色id
     */
    @Override
    public void checkRoleDataScope(String roleId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysRole role = new SysRole();
            role.setId(roleId);
            List<SysRole> roles = SpringUtils.getAopProxy(this).findList(role);
            if (StringUtils.isEmpty(roles))
            {
                throw new BizException("没有权限访问角色数据！");
            }
        }
    }
}
