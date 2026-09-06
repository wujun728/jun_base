package ginyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ginyi.system.domain.SysMenu;
import ginyi.system.domain.model.dto.MenuDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author ruoyi
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public List<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuListByUserId(Long userId);

    /**
     * 管理员查询（管理）菜单列表
     * @return
     */
    public List<SysMenu> selectMenuListByAdmin(@Param("menuDto") MenuDto menuDto);

    /**
     * 添加菜单
     * @param menuDto
     * @return
     */
    public boolean insertMenu(@Param("menuDto") MenuDto menuDto);

    /**
     * 更新菜单
     * @param menuDto
     */
    public void updateMenu(@Param("menuDto") MenuDto menuDto);

    /**
     * 更新状态
     * @param menuDto
     */
    public void updateMenuStatus(@Param("menuDto") MenuDto menuDto);
}
