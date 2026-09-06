package ginyi.system.service;

import ginyi.system.domain.model.dto.RoleDto;
import ginyi.system.domain.model.dto.UserDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.RoleVo;

import java.util.Set;

public interface ISysRoleService {
    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 角色列表
     *
     * @param roleDto
     * @param page
     * @param pageSize
     * @return
     */
    BaseVo<RoleVo> list(RoleDto roleDto, Long page, Long pageSize);

    /**
     * 根据角色id获取角色
     *
     * @param roleId
     * @return
     */
    public RoleVo getRoleByRoleId(Long roleId);

    /**
     * 新增角色
     *
     * @param roleDto
     */
    public void addRole(RoleDto roleDto);

    /**
     * 跟新角色
     *
     * @param roleDto
     */
    public void updateRole(RoleDto roleDto);

    /**
     * 删除角色
     *
     * @param roleId
     */
    public void removeByRoleId(Long roleId);

    /**
     * 批量删除角色
     *
     * @param ids
     */
    public void removeByRoleIds(Set<Long> ids);

    /**
     * 更改状态（0正常 1停用）
     * @param roleDto
     */
    public void updateStatus(RoleDto roleDto);
}
