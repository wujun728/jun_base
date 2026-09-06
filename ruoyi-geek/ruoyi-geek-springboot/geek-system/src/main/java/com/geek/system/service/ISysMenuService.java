package com.geek.system.service;

import java.util.List;
import java.util.Set;

import com.geek.common.constant.UserConstants;
import com.geek.common.core.domain.TreeSelect;
import com.geek.common.core.domain.entity.SysMenu;
import com.geek.common.utils.StringUtils;
import com.geek.system.domain.vo.RouterVo;
import com.mybatisflex.core.service.IService;

/**
 * 菜单 业务层
 * 
 * @author geek
 */
public interface ISysMenuService extends IService<SysMenu> {
    /**
     * 根据用户查询系统菜单列表
     * 
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(Long userId);

    /**
     * 根据用户查询系统菜单列表
     * 
     * @param menu   菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId);

    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据角色ID查询权限
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询菜单树信息
     * 
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     * 
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public List<Long> selectMenuListByRoleId(Long roleId);

    /**
     * 构建前端路由所需要的菜单
     * 
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * 构建前端所需要树结构
     * 
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * 是否存在菜单子节点
     * 
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean hasChildByMenuId(Long menuId);

    /**
     * 查询菜单是否存在角色
     * 
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkMenuExistRole(Long menuId);

    /**
     * 校验菜单名称是否唯一
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean checkMenuNameUnique(SysMenu menu);

    /**
     * 校验路由组合是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean checkRouteConfigUnique(SysMenu menu);

    /**
     * 更新或新增前检查
     */
    default public void checkMenuAllowed(SysMenu menu) {
        if (!checkMenuNameUnique(menu)) {
            throw new IllegalArgumentException("菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            throw new IllegalArgumentException("地址必须以http(s)://开头");
        } else if (menu.getParentId().equals(menu.getMenuId())) {
            throw new IllegalArgumentException("上级菜单不能选择自己");
        } else if (!checkRouteConfigUnique(menu)) {
            throw new IllegalArgumentException("路由名称或地址已存在");
        }
    }
}
