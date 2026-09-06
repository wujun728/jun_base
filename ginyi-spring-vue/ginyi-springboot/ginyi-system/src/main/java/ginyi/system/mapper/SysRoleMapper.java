package ginyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ginyi.system.domain.SysRole;
import ginyi.system.domain.model.dto.RoleDto;
import ginyi.system.domain.model.vo.RoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author ruoyi
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {


    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * 查询角色列表
     *
     * @param roleDto
     * @param page
     * @return
     */
    public IPage<RoleVo> list(@Param("roleDto") RoleDto roleDto, Page page);

    /**
     * 新增角色
     *
     * @param roleDto
     */
    public void insertRole(@Param("roleDto") RoleDto roleDto);

    /**
     * 新增角色对应的菜单权限
     *
     * @param roleDto
     */
    public void insertRoleMenu(@Param("roleDto") RoleDto roleDto);

    /**
     * 更新角色
     *
     * @param roleDto
     */
    public void updateRole(@Param("roleDto") RoleDto roleDto);

    /**
     * 更新角色对应的菜单权限
     *
     * @param roleDto
     */
    public void updateRoleMenu(@Param("roleDto") RoleDto roleDto);

    /**
     * 根据角色id查询角色详情
     * @param roleId
     * @return
     */
    public RoleVo selectRoleByRoleId(Long roleId);

    /**
     * 更新状态
     * @param roleDto
     */
    public void updateRoleStatus(@Param("roleDto") RoleDto roleDto);
}
