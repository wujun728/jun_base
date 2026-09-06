package com.geek.system.service.impl;

import static com.geek.common.core.domain.entity.table.SysRoleTableDef.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geek.common.annotation.DataScope;
import com.geek.common.core.domain.entity.SysRole;
import com.geek.common.exception.ServiceException;
import com.geek.common.utils.SecurityUtils;
import com.geek.common.utils.StringUtils;
import com.geek.common.utils.poi.ExcelUtil;
import com.geek.system.domain.SysRoleDept;
import com.geek.system.domain.SysRoleMenu;
import com.geek.system.domain.SysUserRole;
import com.geek.system.mapper.SysRoleDeptMapper;
import com.geek.system.mapper.SysRoleMapper;
import com.geek.system.mapper.SysRoleMenuMapper;
import com.geek.system.mapper.SysUserRoleMapper;
import com.geek.system.service.ISysRoleService;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 角色 业务层处理
 * 
 * @author geek
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    /**
     * 根据条件分页查询角色数据
     * 
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public QueryChain<SysRole> selectRoleList(SysRole role) {
        QueryChain<SysRole> q = mapper.baseQueryChain()
                .eq(SysRole::getRoleId, role.getRoleId())
                .like(SysRole::getRoleName, role.getRoleName())
                .eq(SysRole::getStatus, role.getStatus())
                .like(SysRole::getRoleKey, role.getRoleKey())
                .le(SysRole::getCreateTime, role.getParams().get("beginTime"))
                .ge(SysRole::getCreateTime, role.getParams().get("endTime"));
        return q;
    }

    @Override
    @DataScope(deptAlias = "d")
    public Page<SysRole> page(SysRole role, int pageNum, int pageSize) {
        QueryChain<SysRole> q = selectRoleList(role);
        Page<SysRole> pg = Page.of(pageNum, pageSize);
        pg.setOptimizeCountQuery(false);
        return q.page(pg);
    }

    @Override
    @DataScope(deptAlias = "d")
    public void export(SysRole role, HttpServletResponse response) {
        List<SysRole> list = selectRoleList(role).list();
        ExcelUtil<SysRole> util = new ExcelUtil<>(SysRole.class);
        util.exportExcel(response, list, "角色数据");

    }

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        List<SysRole> userRoles = mapper.baseQueryChain().eq(SysUserRole::getUserId, userId).list();
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = mapper.baseQueryChain().eq(SysUserRole::getUserId, userId).list();
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
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleAll() {
        return mapper.baseQueryChain().list();
    }

    /**
     * 根据用户ID获取角色选择框列表
     * 
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> mapper.baseQueryChain()
                .select(SYS_ROLE.ROLE_ID)
                .eq(SysUserRole::getUserId, userId)
                .listAs(Long.class));
    }

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> mapper.baseQueryChain()
                .eq(SysRole::getRoleId, roleId)
                .one());
    }

    /**
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        return !mapper.baseQueryChain()
                .eq(SysRole::getRoleName, role.getRoleName())
                .ne(SysRole::getRoleId, role.getRoleId())
                .exists();
    }

    /**
     * 校验角色权限是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        return !mapper.baseQueryChain()
                .eq(SysRole::getRoleKey, role.getRoleKey())
                .ne(SysRole::getRoleId, role.getRoleId())
                .exists();
    }

    /**
     * 校验角色是否允许操作
     * 
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 校验角色是否有数据权限
     * 
     * @param roleId 角色id
     */
    @Override
    @DataScope(deptAlias = "d")
    public void checkRoleDataScope(Long roleId) {
        if (!SecurityUtils.isAdmin()) {
            SysRole role = new SysRole();
            role.setRoleId(roleId);
            List<SysRole> roles = selectRoleList(role).list();
            if (StringUtils.isEmpty(roles)) {
                throw new ServiceException("没有权限访问角色数据！");
            }
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
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
    public int insertRole(SysRole role) {
        // 新增角色信息
        this.save(role);
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
    public int updateRole(SysRole role) {
        // 修改角色信息
        this.updateById(role);
        // 删除角色与菜单关联
        UpdateChain.of(SysRoleMenu.class)
                .eq(SysRoleMenu::getRoleId, role.getRoleId())
                .remove();
        return insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean updateRoleStatus(SysRole role) {
        return this.updateById(role);
    }

    /**
     * 修改数据权限信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role) {
        // 修改角色信息
        this.updateById(role);
        // 删除角色与部门关联
        UpdateChain.of(SysRoleDept.class)
                .eq(SysRoleDept::getRoleId, role.getRoleId())
                .remove();
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    /**
     * 新增角色菜单信息
     * 
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = roleMenuMapper.insertBatch(list);
        }
        return rows;
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(SysRole role) {
        int rows = 1;
        // 新增角色与部门（数据权限）管理
        List<SysRoleDept> list = new ArrayList<>();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0) {
            rows = roleDeptMapper.insertBatch(list);
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
    public boolean deleteRoleById(Long roleId) {
        // 删除角色与菜单关联
        UpdateChain.of(SysRoleMenu.class)
                .eq(SysRoleMenu::getRoleId, roleId)
                .remove();
        // 删除角色与部门关联
        UpdateChain.of(SysRoleDept.class)
                .eq(SysRoleDept::getRoleId, roleId)
                .remove();
        return super.removeById(roleId);
    }

    /**
     * 批量删除角色信息
     * 
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public boolean deleteRoleByIds(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException("%1$s已分配,不能删除".formatted(role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        UpdateChain.of(SysRoleMenu.class)
                .in(SysRoleMenu::getRoleId, roleIds)
                .remove();
        // 删除角色与部门关联
        UpdateChain.of(SysRoleDept.class)
                .in(SysRoleDept::getRoleId, roleIds)
                .remove();

        return super.removeByIds(roleIds);
    }

    /**
     * 取消授权用户角色
     * 
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole) {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 批量取消授权用户角色
     * 
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    /**
     * 批量选择授权用户角色
     * 
     * @param roleId  角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds) {
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<>();
        for (Long userId : userIds) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }
}
