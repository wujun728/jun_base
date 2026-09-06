package ginyi.system.service;

import ginyi.system.domain.SysUser;
import ginyi.system.domain.model.dto.DeptDto;
import ginyi.system.domain.model.dto.PostDto;
import ginyi.system.domain.model.dto.RoleDto;
import ginyi.system.domain.model.dto.UserDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.UserVo;

import java.util.HashMap;
import java.util.Set;

public interface ISysUserService {

    /**
     * 修改用户基本信息
     *
     * @param userDto 用户信息
     * @return 结果
     */
    public void updateUser(UserDto userDto);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public String checkUserNameUnique(SysUser user);

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean registerUser(SysUser user);

    /**
     * 新增用户
     *
     * @param userDto
     */
    public void addUser(UserDto userDto);

    /**
     * 根据用户id查询用户
     * @param userId
     */
    public UserVo getUserByUserId(String userId);

    /**
     * 查询用户列表（不含admin）
     * @param userDto
     * @param page
     * @param pageSize
     * @return
     */
    public BaseVo<UserVo> list(UserDto userDto, Long page, Long pageSize);

    /**
     * 根据用户id删除用户
     * @param userId
     */
    public void removeById(Long userId);

    /**
     * 根据userId批量删除用户
     * @param ids
     */
    public void removeUserByIds(Set<Long> ids);

    /**
     * 更改状态（0正常 1停用）
     * @param userDto
     */
    public void updateStatus(UserDto userDto);

    /**
     * 根据部门 id 获取用户列表
     * @param deptDto
     * @return
     */
    public BaseVo<HashMap<String, Object>> getUserListByDeptIds(DeptDto deptDto);

    /**
     * 根据岗位 id 获取用户列表
     * @param postDto
     * @return
     */
    public BaseVo<HashMap<String, Object>> getUserListByPostIds(PostDto postDto);

    /**
     * 根据角色 id 获取用户列表
     * @param roleDto
     * @return
     */
    public BaseVo<HashMap<String, Object>> getUserListByRoleIds(RoleDto roleDto);
}
