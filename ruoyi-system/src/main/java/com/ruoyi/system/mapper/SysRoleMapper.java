package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.common.core.domain.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色表 数据层
 * 
 * @author ruoyi
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole>
{
    /**
     * 根据条件分页查询角色数据
     *
     * @param page    分页信息
     * @param sysRole 角色信息
     * @return 角色数据集合信息
     */
    public IPage<SysRole> selectRoleList(IPage<SysRole> page, @Param("entity") SysRole sysRole);

    /**
     * 根据分页查询角色数据
     *
     * @param sysRole 角色信息
     * @return 角色数据集合信息
     */
    public List<SysRole> selectRoleList(@Param("entity") SysRole sysRole);

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    public List<Long> selectRoleListByUserId(Long userId);

    /**
     * 根据条件查询角色
     *
     * @param queryWrapper 查询条件
     * @return 角色列表
     */
    public SysRole selectRole(@Param(Constants.WRAPPER) Wrapper<SysRole> queryWrapper);

    /**
     * 根据条件查询角色
     *
     * @param queryWrapper 查询条件
     * @return 角色列表
     */
    public List<SysRole> selectRoles(@Param(Constants.WRAPPER) Wrapper<SysRole> queryWrapper);
}
