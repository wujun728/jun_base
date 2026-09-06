package com.geek.system.service.impl;

import static com.geek.common.core.domain.entity.table.SysDeptTableDef.*;
import static com.geek.system.domain.table.SysRoleDeptTableDef.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geek.common.annotation.DataScope;
import com.geek.common.constant.UserConstants;
import com.geek.common.core.domain.TreeSelect;
import com.geek.common.core.domain.entity.SysDept;
import com.geek.common.core.domain.entity.SysRole;
import com.geek.common.core.domain.entity.SysUser;
import com.geek.common.core.domain.entity.table.SysDeptTableDef;
import com.geek.common.core.text.Convert;
import com.geek.common.exception.ServiceException;
import com.geek.common.utils.SecurityUtils;
import com.geek.common.utils.StringUtils;
import com.geek.common.utils.spring.SpringUtils;
import com.geek.common.utils.sql.SqlUtil;
import com.geek.system.domain.table.SysRoleDeptTableDef;
import com.geek.system.mapper.SysDeptMapper;
import com.geek.system.mapper.SysRoleMapper;
import com.geek.system.service.ISysDeptService;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 部门管理 服务实现
 * 
 * @author geek
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept) {
        return this.queryChain()
                .from(SysDept.class)
                .as("d")
                .eq(SysDept::getDeptId, dept.getDeptId())
                .eq(SysDept::getParentId, dept.getParentId())
                .like(SysDept::getDeptName, dept.getDeptName())
                .eq(SysDept::getStatus, dept.getStatus())
                .orderBy(SysDept::getParentId, true)
                .orderBy(SysDept::getOrderNum, true)
                .list();
    }

    /**
     * 查询部门树结构信息
     * 
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDept dept) {
        List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
        return buildDeptTreeSelect(depts);
    }

    /**
     * 构建前端所需要树结构
     * 
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<>();
        List<Long> tempList = depts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        for (SysDept dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     * 
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectOneById(roleId);
        SysDeptTableDef D = SYS_DEPT.as("d");
        SysRoleDeptTableDef RD = SYS_ROLE_DEPT.as("rd");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(D.DEPT_ID).from(D)
                .leftJoin(RD).on(RD.DEPT_ID.eq(D.DEPT_ID))
                .where(RD.ROLE_ID.eq(roleId));
        if (role.isDeptCheckStrictly()) {
            queryWrapper.and(D.DEPT_ID.notIn(QueryWrapper.create()
                    .select(D.PARENT_ID).from(D)
                    .innerJoin(RD).on(D.DEPT_ID.eq(RD.DEPT_ID).and(RD.ROLE_ID.eq(roleId)))));
        }
        queryWrapper.orderBy(D.PARENT_ID, true).orderBy(D.ORDER_NUM, true);
        return mapper.selectListByQueryAs(queryWrapper, Long.class);
    }

    /**
     * 根据部门ID查询信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(Long deptId) {
        return this.getById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     * 
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public long selectNormalChildrenDeptById(Long deptId) {
        return this.queryChain()
                .eq(SysDept::getStatus, UserConstants.DEPT_NORMAL)
                .and(SqlUtil.findInSet(deptId.toString(), SYS_DEPT.ANCESTORS.getName()))
                .count();
    }

    /**
     * 是否存在子节点
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        return exists(SYS_DEPT.PARENT_ID.eq(deptId));
    }

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        return this.queryChain()
                .from(SysUser.class)
                .eq(SysUser::getDeptId, deptId)
                .exists();
    }

    /**
     * 校验部门名称是否唯一
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean checkDeptNameUnique(SysDept dept) {
        return !this.queryChain()
                .eq(SysDept::getDeptName, dept.getDeptName())
                .eq(SysDept::getParentId, dept.getParentId())
                .ne(SysDept::getDeptId, dept.getDeptId())
                .exists();
    }

    /**
     * 校验部门是否有数据权限
     * 
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(Long deptId) {
        if (!SecurityUtils.isAdmin() && StringUtils.isNotNull(deptId)) {
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    /**
     * 新增保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean insertDept(SysDept dept) {
        SysDept info = this.getById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return save(dept);
    }

    /**
     * 修改保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean updateDept(SysDept dept) {
        SysDept newParentDept = this.getById(dept.getParentId());
        SysDept oldDept = this.getById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        boolean result = this.updateById(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     * 
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        this.updateChain()
                .set(SysDept::getStatus, UserConstants.DEPT_NORMAL)
                .in(SysDept::getDeptId, (Object[]) deptIds)
                .update();
    }

    /**
     * 修改子元素关系
     * 
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        LogicDeleteManager.execWithoutLogicDelete(() -> {
            List<SysDept> children = this.queryChain()
                    .where(SqlUtil.findInSet(deptId.toString(), SYS_DEPT.ANCESTORS.getName()))
                    .list();

            for (SysDept child : children) {
                child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
            }
            if (children.size() > 0) {
                this.updateBatch(children);
            }
        });
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0;
    }
}
