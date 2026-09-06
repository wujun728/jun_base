package com.aidex.system.service;

import com.alibaba.fastjson2.JSONObject;
import com.aidex.common.core.domain.TreeNode;
import com.aidex.common.core.domain.entity.SysDept;
import com.aidex.common.core.service.BaseTreeService;

import java.util.List;
import java.util.Map;

/**
 * 部门管理 服务层
 *
 * @author ruoyi
 */
public interface SysDeptService extends BaseTreeService<SysDept> {

    /**
     * 根据展开层级和父节点递归获取展示的数据
     *
     * @param level
     * @param parentId
     * @return
     */
    public List<SysDept> listDataByLevel(int level, String parentId);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return
     */
    public List<Integer> selectDeptListByRoleId(String roleId);

    /**
     * 是否存在部门子节点
     *
     * @param id) 部门ID
     * @return
     */
    public boolean hasChildByDeptId(String id);

    /**
     * 查询部门是否存在用户
     *
     * @param id) 部门ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistUser(String id);

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return
     */
    public void checkDeptNameUnique(SysDept dept);

    /**
     * 构建前端所需要树结构
     *
     * @param level     展开层级
     * @param parentId  父节点ID
     * @param excludeId 排除节点ID
     * @return
     */
    public List<TreeNode> buildDeptTreeExcludeChild(int level, String parentId, String excludeId);

    /**
     * 构建前端所需要树结构
     *
     * @param level    展开层级
     * @param parentId 父节点ID
     * @return
     */
    public List<TreeNode> buildDeptTree(int level, String parentId);

    /**
     * 树检索
     *
     * @param searchText    检索字符
     * @return
     */
    public List<TreeNode> search(String searchText);
    /**
     * 树表格检索
     *
     * @param dept    检索对象
     * @return
     */
    public List<SysDept> searchDeptList(SysDept dept);

    /**
     * 构建按部门选人树
     *
     * @param level    展开层级
     * @param parentId 父节点ID
     * @return
     */
    public List<TreeNode> buildDeptSelectUserTree(int level, String parentId);

    /**
     * 查询部门人员树
     * @param dept
     * @return
     */
    public List<TreeNode> searchDeptUserList(SysDept dept);

    /**
     * 根据部门id串查询部门列表
     * @param deptIdsObj
     * @return
     */
    public List<Map<String,Object>> getDeptInfoByIds(JSONObject deptIdsObj);

    /**
     * 根据部门编码查询部门
     * @param deptCode
     * @return
     */
    public SysDept selectDeptByDeptCode(String deptCode);

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    public void checkDeptDataScope(String deptId);

    /**
     * 刷新缓存
     * @return 结果
     */
    public void refreshCache();

    /**
     * 加载参数缓存数据
     */
    public void loadingDeptCache();

}
