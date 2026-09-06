package com.aidex.system.service.impl;

import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.spring.SpringUtils;
import com.aidex.framework.cache.DeptUtils;
import com.alibaba.fastjson2.JSONObject;
import com.aidex.common.constant.Constants;
import com.aidex.common.constant.UserConstants;
import com.aidex.common.core.domain.BaseEntity;
import com.aidex.common.core.domain.TreeNode;
import com.aidex.common.core.domain.entity.SysDept;
import com.aidex.common.core.domain.entity.SysRole;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.service.BaseTreeServiceImpl;
import com.aidex.common.exception.BizException;
import com.aidex.common.utils.NumberUtils;
import com.aidex.common.utils.PinYin4JCn;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.uuid.IdUtils;
import com.aidex.system.common.SysErrorCode;
import com.aidex.system.mapper.SysDeptMapper;
import com.aidex.system.mapper.SysRoleMapper;
import com.aidex.system.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 部门管理 服务实现
 *
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl extends BaseTreeServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Autowired
    private SysRoleMapper roleMapper;

    private static final int ID_LIMIT = 500;
    /**
     * 新增保存部门信息
     *
     * @return 结果
     */
    @Override
    public boolean save(SysDept sysDept) {
        checkDeptNameUnique(sysDept);
        SysDept parentSysDept = mapper.get(sysDept.getParentId());
        sysDept.setDeptPinyin(PinYin4JCn.getFullAndSimplePinYin(sysDept.getDeptName(),500));
        if (null != parentSysDept || StringUtils.isNotBlank(sysDept.getId())) {
            //没有父节点或者当前数据有ID(编辑跟节点时)
            //新增
            if (StringUtils.isEmpty(sysDept.getId())) {
                // 如果父节点不为正常状态,则不允许新增子节点
                if (!UserConstants.DEPT_NORMAL.equals(parentSysDept.getStatus())) {
                    throw new BizException(SysErrorCode.B_SYSDEPT_DeptAlreadyUnNormal, "部门停用，不允许新增子部门");
                }
                setTreeProperties(sysDept, null, Constants.OpType.insert);
                super.save(sysDept);
            } else {
                //编辑
                SysDept oldSysDept = mapper.get(sysDept);
                if (isMoveToSelfChildNode(sysDept, oldSysDept)) {
                    throw new BizException(SysErrorCode.B_SYSDEPT_DeptMoveUnNormal, "非法移动，不允许将节点移动到自身或者子节点下");
                }
                List<SysDept> updateChildList = new ArrayList<SysDept>();
                if (sysDept.getParentId().equals(sysDept.getId())) {
                    throw new BizException(SysErrorCode.B_SYSDEPT_DeptIdNotEqualParentId, "修改部门失败，上级部门不能是自己");
                }
                if (StringUtils.equals(UserConstants.DEPT_DISABLE, sysDept.getStatus()) && mapper.selectNormalChildByParentIds(sysDept.getParentIds()) > 0) {
                    throw new BizException(SysErrorCode.B_SYSDEPT_DeptHasNormalChild, "该部门包含未停用的子部门，不允许标记为停用");
                }
                if("dept".equals(sysDept.getDeptType())){
                    //当修改类型为部门时需要判断当前节点下所有子孙节点中是否存在公司节点
//                    SysDept checkSysDept = sysDept;//避免对象引用
                    SysDept checkSysDept = new SysDept();
                    checkSysDept.setId(sysDept.getId());
                    checkSysDept.setDeptType("company");
                    List<SysDept> checkList = mapper.searchChildrenTypeByParentId(checkSysDept);
                    if(!CollectionUtils.isEmpty(checkList)){
                        throw new BizException(SysErrorCode.B_SYSDEPT_NoAllowUpdateType);
                    }
                }
                if (isUpdateTreeProperties(sysDept, oldSysDept)) {
                    setTreeProperties(sysDept, oldSysDept, Constants.OpType.update);
                    updateChildList = updateChildTreeProperties(sysDept, oldSysDept);
                    if (!CollectionUtils.isEmpty(updateChildList)) {
                        mapper.updateDeptChildren(updateChildList);
                    }
                }
                parentSysDept = mapper.get(sysDept.getParentId());
                // 新的父节点变更为非叶子节点
                if (!Constants.TREE_ROOT.equals(sysDept.getParentId()) && parentSysDept.getTreeLeaf().equals(Constants.TREE_LEAF_Y)) {
                    parentSysDept.setTreeLeaf(Constants.TREE_LEAF_N);
                    super.save(parentSysDept);
                }
                String oldParentId = oldSysDept.getParentId();
                super.save(sysDept);
                // 判断原节点是否还有子节点，否则改为叶子节点
                if (!hasChildByDeptId(oldParentId)) {
                    SysDept oldParentSysDept = mapper.get(oldParentId);
                    oldParentSysDept.setTreeLeaf(Constants.TREE_LEAF_Y);
                    super.save(oldParentSysDept);
                }
            }
        } else {
            //新增根节点
            sysDept.setId(IdUtils.randomUUID());
            sysDept.setNewRecord(true);
            sysDept.setParentIds(sysDept.getId());
            // 设置当前树全路径排序
            Integer treeSort = sysDept.getTreeSort();
            String treeSorts = String.format("%06d", treeSort);
            sysDept.setTreeSorts(treeSorts);
            sysDept.setTreeLeaf(Constants.TREE_LEAF_Y);
            // 设置当前节点级别.
            sysDept.setTreeLevel(1);
            super.save(sysDept);
        }
        return Boolean.TRUE;
    }

    /**
     * 删除部门
     *
     * @param sysDept
     * @return
     */
    @Override
    public boolean remove(SysDept sysDept) {
        sysDept = mapper.get(sysDept.getId());
        if (hasChildByDeptId(sysDept.getId())) {
            throw new BizException(SysErrorCode.B_SYSDEPT_DeptHasChildNotDelete, "该部门包含子部门，不允许删除");
        }
        if (checkDeptExistUser(sysDept.getId())) {
            throw new BizException(SysErrorCode.B_SYSDEPT_DeptHasUserNotDelete, "该部门包含用户，不允许删除");
        }
        //删除
        super.remove(sysDept);
        //判断当前父节点下还是否有子节点，如果没有，则需要把父节点置为叶子节点
        SysDept parentSysDept = mapper.get(sysDept.getParentId());
        if (findChildCountById(sysDept.getParentId()) == 0) {
            parentSysDept.setTreeLeaf(Constants.TREE_LEAF_Y);
            super.save(parentSysDept);
        }
        return Boolean.TRUE;
    }

    public Integer findChildCountById(String sysDeptId) {
        SysDept sysDept = new SysDept();
        sysDept.setParentId(sysDeptId);
        return mapper.findCount(sysDept);
    }

    /**
     * 查询最大排序号
     *
     * @param sysDept
     * @return
     */
    @Override
    public Integer findMaxSort(SysDept sysDept) {
        return NumberUtils.nextOrder(mapper.findMaxSort(sysDept));
    }

    /**
     * 是否存在子节点
     *
     * @param sysDeptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(String sysDeptId) {
        int result = findChildCountById(sysDeptId);
        return result > 0;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(String deptId) {
        int result = mapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public void checkDeptNameUnique(SysDept dept) {
        SysDept sysDeptUnique = new SysDept();
        sysDeptUnique.setNotEqualId(dept.getId());
        sysDeptUnique.setDeptName(dept.getDeptName());
        sysDeptUnique.setParentId(dept.getParentId());
        if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysDeptUnique))) {
            throw new BizException(SysErrorCode.B_SYSDEPT_DeptNameAlreadyExist);
        }
    }

    /**
     * 根据展开层级和父节点递归获取展示的数据
     *
     * @param level
     * @param parentId
     * @return
     */
    @Override
    public List<SysDept> listDataByLevel(int level, String parentId) {
        List<SysDept> listData = new ArrayList<SysDept>();
        level--;
        List<SysDept> childrenList = findChildListByParentId(parentId);
        for (SysDept dto : childrenList) {
            if ("n".equals(dto.getTreeLeaf()) && level > 0) {
                dto.setChildren(this.listDataByLevel(level, dto.getId()));
            } else {
                dto.setChildren(new ArrayList<>());
            }
            listData.add(dto);
        }
        return listData;
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Integer> selectDeptListByRoleId(String roleId) {
        SysRole role = roleMapper.get(roleId);
        return mapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param level    展开层级
     * @param parentId 父节点ID
     * @return
     */
    @Override
    public List<TreeNode> buildDeptTree(int level, String parentId) {
        return this.buildDeptTreeExcludeChild(level, parentId, "");
    }

    /**
     * 构建前端所需要树结构
     *
     * @param level     展开层级
     * @param parentId  父节点ID
     * @param excludeId 排除节点ID
     * @return
     */
    @Override
    public List<TreeNode> buildDeptTreeExcludeChild(int level, String parentId, String excludeId) {
        List<TreeNode> listData = new ArrayList<TreeNode>();
        level--;
        List<SysDept> childrenList = findChildListByParentId(parentId);
        for (SysDept sysDept : childrenList) {
            if (!sysDept.getId().equals(excludeId)) {
                TreeNode treeNode = this.transToTreeNodeData(sysDept);
                if ("n".equals(sysDept.getTreeLeaf()) && level != 0) {
                    treeNode.setChildren(this.buildDeptTreeExcludeChild(level, sysDept.getId(), excludeId));
                } else {
                    treeNode.setChildren(null);
                }
                //不展开节点的父节点数据处理
                if (CollectionUtils.isEmpty(treeNode.getChildren())) {
                    treeNode.setChildren(null);
                }
                listData.add(treeNode);
            }
        }
        return listData;
    }
    /**
     * 树表格检索
     *
     * @param queryParams    检索对象
     * @return
     */
    @Override
    public List<SysDept> searchDeptList(SysDept queryParams){
        List<SysDept> sysDeptList = new ArrayList<SysDept>();
        List<SysDept> searchNodes = mapper.searchSysDeptTree(queryParams);
        if (!CollectionUtils.isEmpty(searchNodes)){
            Set<String> treeIdSet = reDuplicationByTreePath(searchNodes);
            int i = 0;
            //考虑mybatis foreach的限制，所以定义参数格式为list内还是list
            List<List<String>> idsList = new ArrayList<List<String>>();
            List<String> idList = new ArrayList<String>();
            for (String treeId : treeIdSet) {
                //当id个数超出限制后，则新new一个list来存放
                if(i % ID_LIMIT == 0 && i > 0){
                    idsList.add(idList);
                    idList = new ArrayList<String>();
                }
                idList.add(treeId);
                i++;
            }
            idsList.add(idList);
            List<SysDept> allNodes = mapper.searchSysDeptTreeByIds(idsList);
            sysDeptList = createTreeGridData(allNodes);
        }
        return sysDeptList;
    }
    /**
     *  组建树形结构
     * @param entityNodes 节点集合
     * @return List<VueNode>
     */
    private List<SysDept> createTreeGridData(List<SysDept> entityNodes) {
        List<SysDept> treeList = new ArrayList<SysDept>();
        for (SysDept sysDept : entityNodes) {
            //找到根节点
            if (Constants.TREE_ROOT.equals(sysDept.getParentId())) {
                treeList.add(sysDept);
            }
            List<SysDept> children = new ArrayList<SysDept>();
            //再次遍历list，找到子节点
            for (SysDept node : entityNodes) {
                if (node.getParentId().equals(sysDept.getId())) {
                    children.add(node);
                }
            }
            sysDept.setChildren(children);
            if (children.isEmpty()){
                sysDept.setTreeLeaf("y");
            }else{
                sysDept.setTreeLeaf("n");
            }
        }
        return treeList;
    }
    /**
     * 检索树
     * @param searchText    检索字符
     * @return
     */
    @Override
    public List<TreeNode> search(String searchText){
        List<TreeNode> tree = new ArrayList<TreeNode>();
        SysDept queryParams = new SysDept();
        queryParams.setSearchText(searchText);
        List<SysDept> searchNodes = mapper.searchSysDeptTree(queryParams);
        if (!CollectionUtils.isEmpty(searchNodes)){
            Set<String> treeIdSet = reDuplicationByTreePath(searchNodes);
            int i = 0;
            //考虑mybatis foreach的限制，所以定义参数格式为list内还是list
            List<List<String>> idsList = new ArrayList<List<String>>();
            List<String> idList = new ArrayList<String>();
            for (String treeId : treeIdSet) {
                //当id个数超出限制后，则新new一个list来存放
                if(i % ID_LIMIT == 0 && i > 0){
                    idsList.add(idList);
                    idList = new ArrayList<String>();
                }
                idList.add(treeId);
                i++;
            }
            idsList.add(idList);
            List<SysDept> allNodes = mapper.searchSysDeptTreeByIds(idsList);
            tree = createTreeData(allNodes);
        }
        return tree;
    }
    /**
     * 根据parentIds去重
     * @param entityNodes entityNodes
     */
    private Set<String> reDuplicationByTreePath(List<SysDept> entityNodes) {
        Set<String> treeIdSet = new HashSet<String>();
        for (SysDept dto : entityNodes) {
            String treePath = dto.getParentIds();
            treeIdSet.addAll(Arrays.asList(treePath.split("/")));
        }
        return treeIdSet;
    }
    /**
     *  组建树形结构
     * @param entityNodes 节点集合
     * @return List<VueNode>
     */
    private List<TreeNode> createTreeData(List<SysDept> entityNodes) {
        List<TreeNode> list = transToTreeNodeData(entityNodes);
        List<TreeNode> tree = new ArrayList<TreeNode>();
        for (TreeNode treeNode : list) {
            //找到根节点
            if (Constants.TREE_ROOT.equals(treeNode.getParentId())) {
                tree.add(treeNode);
            }
            List<TreeNode> children = new ArrayList<TreeNode>();
            //再次遍历list，找到子节点
            for (TreeNode node : list) {
                if (node.getParentId().equals(treeNode.getId())) {
                    children.add(node);
                }
            }
            treeNode.setChildren(children);
            if (children.isEmpty()){
                treeNode.setIsLeaf(true);
            }
        }
        return tree;
    }
    /**
     *  将实体类集合转化成VueNode格式
     * @param entityNodes  实体类节点集合
     * @return List<VueNode>
     */
    private List<TreeNode> transToTreeNodeData(List<SysDept> entityNodes) {
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (SysDept entityNode : entityNodes) {
            TreeNode treeNode = transToTreeNodeData(entityNode);
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }
    /**
     *  将实体类转化成VueNode格式
     * @param entityNode  实体类节点集合
     * @return List<VueNode>
     */
    private TreeNode transToTreeNodeData(SysDept entityNode) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(entityNode.getId());
        treeNode.setKey(entityNode.getId());
        treeNode.setTitle(entityNode.getDeptName());
        treeNode.setLabel(entityNode.getDeptName());
        treeNode.setSelectable(true);
        treeNode.setDisabled(false);
        treeNode.setDisableCheckbox(false);
        treeNode.setParentId(entityNode.getParentId());
        treeNode.setParentIds(entityNode.getParentIds());
        HashMap<String,Object> attr = new HashMap<>();
        attr.put("treeLeaf", entityNode.getTreeLeaf());
        attr.put("treeLevel", entityNode.getTreeLevel());
        attr.put("treePath", entityNode.getParentIds());
        attr.put("deptType", entityNode.getDeptType());
        attr.put("subtitle", entityNode.getSubtitle());
        attr.put("deptPinyin", entityNode.getDeptPinyin());
        treeNode.setAttributes(attr);
        if (Constants.TREE_LEAF_Y.equals(entityNode.getTreeLeaf())){
            treeNode.setIsLeaf(true);
        } else{
            treeNode.setIsLeaf(false);
        }
        JSONObject slotsValue = new JSONObject();
        String deptType = StringUtils.isNotBlank(entityNode.getDeptType()) ? entityNode.getDeptType() : "";
        if ("org".equals(deptType)) {
            slotsValue.put("icon", "org");
            treeNode.setDisableCheckbox(true);
        } else if ("company".equals(deptType)) {
            slotsValue.put("icon", "company");
            treeNode.setDisableCheckbox(true);
        } else if ("dept".equals(deptType)) {
            slotsValue.put("icon", "dept");
            treeNode.setDisableCheckbox(false);
        } else if ("user".equals(deptType)) {
            slotsValue.put("icon", "user");
            treeNode.setDisableCheckbox(false);
        }
        JSONObject scopedSlotsValue = new JSONObject();
        scopedSlotsValue.put("title","title");
        scopedSlotsValue.put("attributes","attributes");
        treeNode.setSlots(slotsValue);
        treeNode.setScopedSlots(scopedSlotsValue);
        return treeNode;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param level    展开层级
     * @param parentId 父节点ID
     * @return
     */
    @Override
    public List<TreeNode> buildDeptSelectUserTree(int level, String parentId) {
        List<TreeNode> listData = new ArrayList<TreeNode>();
        level--;
        SysDept querySysDept = new SysDept();
        querySysDept.setParentId(parentId);
        List<SysDept> childrenList = mapper.findDeptAndUserChildListByParentId(querySysDept);
        for (SysDept sysDept : childrenList) {
                TreeNode treeNode = this.transToTreeNodeData(sysDept);
                if ("n".equals(sysDept.getTreeLeaf())  && level > 0) {
                    treeNode.setChildren(this.buildDeptSelectUserTree(level, sysDept.getId()));
                } else {
                    treeNode.setChildren(null);
                }
                //不展开节点的父节点数据处理
                if (CollectionUtils.isEmpty(treeNode.getChildren())) {
                    treeNode.setChildren(null);
                }
                listData.add(treeNode);
        }
        return listData;
    }
    /**
     * 按部门检索用户
     * @param sysDept    检索字符
     * @return
     */
    @Override
    public List<TreeNode> searchDeptUserList(SysDept sysDept){
        List<TreeNode> tree = new ArrayList<TreeNode>();
        SysDept queryParams = new SysDept();
        queryParams.setSearchText(sysDept.getSearchText());
        List<SysDept> searchNodes = mapper.searchSysDeptUserTree(queryParams);
        if (!CollectionUtils.isEmpty(searchNodes)){
            Set<String> treeIdSet = reDuplicationByTreePath(searchNodes);
            int i = 0;
            //考虑mybatis foreach的限制，所以定义参数格式为list内还是list
            List<List<String>> idsList = new ArrayList<List<String>>();
            List<String> idList = new ArrayList<String>();
            for (String treeId : treeIdSet) {
                //当id个数超出限制后，则新new一个list来存放
                if(i % ID_LIMIT == 0 && i > 0){
                    idsList.add(idList);
                    idList = new ArrayList<String>();
                }
                idList.add(treeId);
                i++;
            }
            idsList.add(idList);
            List<SysDept> allNodes = mapper.searchSysDeptUserTreeByIds(idsList);
            tree = createTreeData(allNodes);
        }
        return tree;
    }

    @Override
    public List<Map<String,Object>> getDeptInfoByIds(JSONObject deptIdsObj){
        String[] deptIds = deptIdsObj.getString("deptIds").split(";");
        int i = 0;
        //考虑mybatis foreach的限制，所以定义参数格式为list内还是list
        List<List<String>> idsList = new ArrayList<List<String>>();
        List<String> idList = new ArrayList<String>();
        for (String deptId : deptIds) {
            //当id个数超出限制后，则新new一个list来存放
            if(i % 500 == 0 && i > 0){
                idsList.add(idList);
                idList = new ArrayList<String>();
            }
            idList.add(deptId);
            i++;
        }
        idsList.add(idList);
        return mapper.getDeptInfoByIds(idsList);
    }


    /**
     * 通过部门编码查询部门
     *
     * @param deptCode 部门编码
     * @return 部门对象信息
     */
    @Override
    public SysDept selectDeptByDeptCode(String deptCode)
    {
        return mapper.selectDeptByDeptCode(deptCode, BaseEntity.DEL_FLAG_NORMAL);
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(String deptId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysDept dept = new SysDept();
            dept.setId(deptId);
            List<SysDept> depts = SpringUtils.getAopProxy(this).findList(dept);
            if (StringUtils.isEmpty(depts))
            {
                throw new BizException("没有权限访问部门数据！");
            }
        }
    }

    /**
     * 刷新缓存
     * @return 结果
     */
    @Override
    public void refreshCache() {
        DeptUtils.clearDeptCache();
        loadingDeptCache();
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingDeptCache()
    {
        List<SysDept> deptList = findList(new SysDept());
        for (SysDept sysDept : deptList)
        {
            DeptUtils.setDeptCache(sysDept);
        }
    }
}
