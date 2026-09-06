package ginyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ginyi.system.domain.SysUser;
import ginyi.system.domain.model.dto.UserDto;
import ginyi.system.domain.model.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表
 */
public interface SysUserMapper extends BaseMapper<SysUser> {


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
     * @param userName 用户名称
     * @return 结果
     */
    public SysUser checkUserNameUnique(String userName);

    /**
     * 新增用户信息
     *
     * @param userDto 用户信息
     * @return 结果
     */
    public int insertUser(@Param("userDto") UserDto userDto);

    /**
     * 插入用户岗位中间表, 插入用户对应的岗位信息
     * @param userDto
     * @return
     */
    public int insertUserPostIds(@Param("userDto") UserDto userDto);

    /**
     * 插入用户角色中间表, 插入用户对应的角色信息
     * @param userDto
     * @return
     */
    public int insertUserRoleIds(@Param("userDto") UserDto userDto);

    /**
     * 修改用户信息
     *
     * @param userDto 用户信息
     * @return 结果
     */
    public int updateUser(@Param("userDto") UserDto userDto);

    /**
     * 更新用户岗位中间表, 更新用户对应的岗位信息
     * @param userDto
     * @return
     */
    public int updateUserPostIds(@Param("userDto") UserDto userDto);

    /**
     * 更新用户角色中间表, 更新用户对应的角色信息
     * @param userDto
     * @return
     */
    public int updateUserRoleIds(@Param("userDto") UserDto userDto);

    /**
     * 根据用户id查询用户
     * @param userId
     * @return
     */
    public UserVo selectUserByUserId(String userId);

    /**
     * 查询用户列表(不含admin)
     * @param userDto
     * @param page
     * @return
     */
    public IPage<UserVo> list(@Param("userDto") UserDto userDto, Page page);

    /**
     * 更新用户状态
     * @param userDto
     */
    public void updateUserStatus(@Param("userDto") UserDto userDto);

    /**
     * 根据部门ids获取用户列表
     * @param deptIds
     * @return
     */
    public List<SysUser> selectUserByDeptIds(List<Long> deptIds);

}
