package com.aidex.system.mapper;

import com.aidex.common.core.domain.entity.SysDept;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.mapper.BaseMapper;
import com.aidex.common.core.mapper.BaseTreeMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * 部门管理 数据层
 *
 * @author ruoyi
 */
public interface SysDeptMapper extends BaseTreeMapper<SysDept> {

    /**
     * 通过部门编码查询部门
     *
     * @param deptCode 部门编码
     * @return 部门对象信息
     */
    public SysDept selectDeptByDeptCode(@Param("deptCode") String deptCode, @Param("DEL_FLAG_NORMAL") String DEL_FLAG_NORMAL);
    /**
     * 批量更新
     *
     * @return 结果
     */
    public Integer updateBatch(List<SysDept> sysDepts);

    /**
     * 批量删除
     *
     * @return 结果
     */
    public Integer deleteBatch(List<SysDept> sysDepts);


    /**
     * 唯一性校验判断
     *
     * @return 结果
     */
    public Integer findCount(SysDept sysDept);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId            角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    public List<Integer> selectDeptListByRoleId(@Param("roleId") String roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysDept> selectChildrenDeptById(String deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @return 子部门数
     */
    public Integer selectNormalChildByParentIds(String parentIds);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public Integer hasChildByDeptId(String deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public Integer checkDeptExistUser(String deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") String parentId);

    /**
     * 修改所在部门的父级部门状态
     *
     * @param dept 部门
     */
    public void updateDeptStatus(SysDept dept);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    public Integer updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 查询最大编号
     * @param
     * @return int
     */
    public Integer findMaxSort(SysDept sysDept);

    public List<SysDept> searchSysDeptTree(SysDept sysDept);

    /**
     * 根据ids获取所有的节点
     * @param idsList ids
     * @return List<SysDept>
     */
    List<SysDept> searchSysDeptTreeByIds(@Param("ids")List<List<String>> idsList);



    /**
     * 根据父节点id获取对应的部门用户集合
     * @param sysDept
     * @return List<SysDept>
     */
    List<SysDept> findDeptAndUserChildListByParentId(SysDept sysDept);

    /**
     * 按部门检索人
     * @param sysDept
     * @return
     */
    public List<SysDept> searchSysDeptUserTree(SysDept sysDept);
    /**
     * 根据ids获取所有部门人员的节点
     * @param idsList ids
     * @return List<SysDept>
     */
    List<SysDept> searchSysDeptUserTreeByIds(@Param("ids")List<List<String>> idsList);


    /**
     * 根据id集合获取部门对象
     * @param idsList
     * @return
     */
    public List<Map<String,Object>> getDeptInfoByIds(@Param("ids")List<List<String>> idsList);

    /**
     * 获取所有下级节点中特定类型的集合
     * @param sysDept
     * @return
     */
    public List<SysDept> searchChildrenTypeByParentId(SysDept sysDept);
}
