package com.aidex.system.mapper;

import java.util.List;

import com.aidex.common.core.domain.entity.SysDept;
import com.aidex.common.core.mapper.BaseMapper;
import com.aidex.common.core.mapper.BaseTreeMapper;
import org.apache.ibatis.annotations.Param;
import com.aidex.common.core.domain.entity.SysMenu;

/**
 * 菜单表 数据层
 *
 * @author ruoyi
 */
public interface SysMenuMapper extends BaseTreeMapper<SysMenu>
{
    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> selectMenuPermsByUserId(String userId);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuTreeByUserId(String userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    public List<Integer> selectMenuListByRoleId(@Param("roleId") String roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);


    /**
     * 唯一性校验判断
     *
     * @return 结果
     */
    public Integer findCount(SysMenu sysMenu);


    public List<SysMenu> searchSysMenuTree(SysMenu sysMenu);

    /**
     * 根据ids获取所有的节点
     * @param idsList ids
     * @return List<SysDept>
     */
    List<SysMenu> searchSysMenuTreeByIds(@Param("ids")List<List<String>> idsList);

    /**
     * 修改子元素关系
     *
     * @param menus 子元素
     * @return 结果
     */
    public Integer updateMenuChildren(@Param("menus") List<SysMenu> menus);

    /**
     * 根据ID查询所有子菜单（正常状态）
     *
     * @return 子部门数
     */
    public Integer selectNormalChildByParentIds(String parentIds);

    /**
     * 查询最大编号
     * @param
     * @return int
     */
    public Integer findMaxSort(SysMenu sysMenu);
}
