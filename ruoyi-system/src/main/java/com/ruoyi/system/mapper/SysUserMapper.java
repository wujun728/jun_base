package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 用户表 数据层
 * 
 * @author ruoyi
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser>
{
//    /**
//     * 通根据条件查询用户
//     *
//     * @param queryWrapper 用户名
//     * @return 用户对象信息
//     */
//    public SysUser selectUserVo(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 根据条件分页查询用户列表
     *
     * @param page    分页信息
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public IPage<SysUser> selectUserList(IPage<SysUser> page,  @Param("entity") SysUser sysUser);
//    /**
//     * 根据条件分页查询用户列表
//     *
//     * @param sysUser 用户信息
//     * @return 用户信息集合信息
//     */
//    public List<SysUser> selectUserList(SysUser sysUser);
//
    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public IPage<SysUser> selectAllocatedList(IPage<SysUser> page, @Param("entity") SysUser user);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public IPage<SysUser> selectUnallocatedList(IPage<SysUser> page, @Param("entity") SysUser user);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long userId);

}
