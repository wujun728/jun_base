package com.geek.system.mapper;

import java.util.List;

import com.geek.common.core.domain.entity.SysUser;
import com.mybatisflex.core.BaseMapper;

/**
 * 用户表 数据层
 * 
 * @author geek
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

}
