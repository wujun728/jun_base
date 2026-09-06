package com.aidex.system.mapper;

import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户表 数据层
 * 
 * @author ruoyi
 */
public interface SysUserMapper extends BaseMapper<SysUser>
{
    /**
     * 根据条件分页查询用户列表
     * 
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
   // public List<SysUser> selectUserList(SysUser sysUser);

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(@Param("userName") String userName, @Param("DEL_FLAG_NORMAL") String DEL_FLAG_NORMAL);

    /**
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 记录登录信息
     *
     * @param sysUser 用户
     * @return 结果
     */
    public int updateUserLoginInfo(SysUser sysUser);

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteUserByIds(@Param("array") String[] userIds, @Param("DEL_FLAG_DELETE") String DEL_FLAG_DELETE);

    /**
     * 根据id集合获取用户对象
     * @param idsList
     * @return
     */
    public List<Map<String,Object>> getUserInfoByIds(@Param("ids")List<List<String>> idsList);

    /**
     * 获取角色下所有用户
     * @param sysUser
     * @return
     */
    public List<SysUser> findRoleUserList(SysUser sysUser);
}
