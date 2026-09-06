package com.geek.system.mapper;

import static com.geek.common.core.domain.entity.table.SysRoleTableDef.*;

import com.geek.common.core.domain.entity.SysDept;
import com.geek.common.core.domain.entity.SysRole;
import com.geek.common.core.domain.entity.SysUser;
import com.geek.system.domain.SysUserRole;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryMethods;

/**
 * 角色表 数据层
 * 
 * @author geek
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    default public QueryChain<SysRole> baseQueryChain() {
        return QueryChain.of(SysRole.class)
                .select(QueryMethods.distinct(SYS_ROLE.DEFAULT_COLUMNS))
                .leftJoin(SysUserRole.class)
                .on(SysUserRole::getRoleId, SysRole::getRoleId)
                .leftJoin(SysUser.class).as("u")
                .on(SysUser::getUserId, SysUserRole::getUserId)
                .leftJoin(SysDept.class).as("d")
                .on(SysUser::getDeptId, SysDept::getDeptId);
    }
}
