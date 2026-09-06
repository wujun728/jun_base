package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.SysRoleDept;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与部门关联表 数据层
 * 
 * @author ruoyi
 */
@Mapper
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept>
{

    /**
     * 批量新增角色部门信息
     *
     * @param roleDeptList 角色部门列表
     * @return 结果
     */
    public int batchRoleDept(List<SysRoleDept> roleDeptList);
}
