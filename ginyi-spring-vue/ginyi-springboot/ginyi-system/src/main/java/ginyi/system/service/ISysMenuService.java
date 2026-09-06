package ginyi.system.service;

import ginyi.system.domain.SysMenu;
import ginyi.system.domain.model.dto.MenuDto;
import ginyi.system.domain.model.vo.BaseVo;

import java.util.Set;

/**
 * 菜单 业务层
 *
 * @author ruoyi
 */
public interface ISysMenuService {
    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @return 菜单列表
     */
    public BaseVo<SysMenu> selectMenuList();

    /**
     * 管理员查询系统菜单列表
     *
     * @return 菜单列表
     */
    public BaseVo<SysMenu> list(MenuDto menuDto);

    /**
     * 添加菜单
     * @param menuDto
     */
    public void addMenu(MenuDto menuDto);

    /**
     * 根据id获取菜单详情
     * @param menuId
     * @return
     */
    public SysMenu getMenuById(Long menuId);

    /**
     * 更新菜单
     * @param menuDto
     */
    public void updateMenu(MenuDto menuDto);

    /**
     * 根据菜单id删除
     * @param menuId
     */
    public void removeMenuById(Long menuId);

    /**
     * 根据ids批量删除菜单
     * @param ids
     */
    public void removeMenuByIds(Set<Long> ids);

    /**
     * 更改状态（0正常 1停用）
     * @param menuDto
     */
    public void updateStatus(MenuDto menuDto);
}
