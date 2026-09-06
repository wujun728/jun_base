package com.aidex.system.service;

import com.aidex.common.core.domain.TreeNode;
import com.aidex.common.core.domain.TreeSelect;
import com.aidex.common.core.domain.entity.SysMenu;
import com.aidex.common.core.service.BaseTreeService;
import com.aidex.system.domain.vo.RouterVo;

import java.util.List;
import java.util.Set;

/**
 * 菜单 业务层
 * 
 * @author ruoyi
 */
public interface SysMenuService extends BaseTreeService<SysMenu>
{
    /**
     * 根据用户查询系统菜单列表
     * 
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(String userId);

    /**
     * 根据用户查询系统菜单列表
     * 
     * @param menu 菜单信息
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(SysMenu menu, String userId);

    /**
     * 树表格检索
     *
     * @param queryParams    检索对象
     * @return
     */
    public List<SysMenu> searchMenuList(SysMenu queryParams);

    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(String userId);

    /**
     * 根据用户ID查询菜单树信息
     * 
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuTreeByUserId(String userId);

    /**
     * 根据角色ID查询菜单树信息
     * 
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public List<Integer> selectMenuListByRoleId(String roleId);

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
     * 根据菜单ID查询信息
     * 
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    public SysMenu selectMenuById(String menuId);

    /**
     * 是否存在菜单子节点
     * 
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean hasChildByMenuId(String menuId);

    /**
     * 查询菜单是否存在角色
     * 
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkMenuExistRole(String menuId);

    /**
     * 校验菜单名称是否唯一
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    public void checkMenuNameUnique(SysMenu menu);

    /**
     * 校验菜单编码是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public void checkMenuCodeUnique(SysMenu menu);


    /**
     * 根据展开层级和父节点递归获取展示的数据
     *
     * @param level
     * @param parentId
     * @return
     */
    public List<SysMenu> listDataByLevel(int level, String parentId);

    /**
     * 根据父ID查询当前子节点的个数
     *
     * @param parentId
     * @return
     */
    @Override
    public List<SysMenu> findChildListByParentId(String parentId);

    /**
     * 是否存在子节点
     *
     * @param sysMenuId 部门ID
     * @return 结果
     */
    public boolean hasChildByDeptId(String sysMenuId);

    /**
     * 删除菜单
     *
     * @param sysMenu
     * @return
     */
    @Override
    public boolean remove(SysMenu sysMenu);

    /**
     * 异步构造菜单树
     * @param level
     * @param parentId
     * @param userId
     * @return
     */
    public List<TreeNode> buildMenuTree(int level, String parentId, String userId);

}
