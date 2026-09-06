package com.aidex.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.aidex.common.constant.Constants;
import com.aidex.common.constant.UserConstants;
import com.aidex.common.core.domain.TreeNode;
import com.aidex.common.core.domain.TreeSelect;
import com.aidex.common.core.domain.entity.SysMenu;
import com.aidex.common.core.domain.entity.SysRole;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.service.BaseTreeServiceImpl;
import com.aidex.common.exception.BizException;
import com.aidex.common.utils.NumberUtils;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.uuid.IdUtils;
import com.aidex.system.common.SysErrorCode;
import com.aidex.system.domain.vo.MetaVo;
import com.aidex.system.domain.vo.RouterVo;
import com.aidex.system.mapper.SysMenuMapper;
import com.aidex.system.mapper.SysRoleMapper;
import com.aidex.system.mapper.SysRoleMenuMapper;
import com.aidex.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysMenuServiceImpl extends BaseTreeServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService
{
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    private static final int ID_LIMIT = 500;

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    /**
     * 根据用户查询系统菜单列表
     * 
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(String userId)
    {
        return selectMenuList(new SysMenu(), userId);
    }

    /**
     * 查询系统菜单列表
     * 
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, String userId)
    {
        List<SysMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId))
        {
            menuList = mapper.findList(menu);
        }
        else
        {
            menu.getParams().put("userId", userId);
            menuList = mapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 树表格检索
     *
     * @param queryParams    检索对象
     * @return
     */
    @Override
    public List<SysMenu> searchMenuList(SysMenu queryParams){
        List<SysMenu> sysMenuList = new ArrayList<SysMenu>();
        List<SysMenu> searchNodes = mapper.searchSysMenuTree(queryParams);
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
            List<SysMenu> allNodes = mapper.searchSysMenuTreeByIds(idsList);
            sysMenuList = createTreeGridData(allNodes);
        }
        return sysMenuList;
    }


    /**
     * 新增保存部门信息
     *
     * @return 结果
     */
    @Override
    public boolean save(SysMenu sysMenu) {
        checkMenuNameUnique(sysMenu);
        checkMenuCodeUnique(sysMenu);
        SysMenu parentSysMenu = mapper.get(sysMenu.getParentId());

        /*if (UserConstants.YES_FRAME.equals(sysMenu.getIsFrame())
                && !StringUtils.startsWithAny(sysMenu.getPath(), Constants.HTTP, Constants.HTTPS)){
            throw new BizException(SysErrorCode.B_SYSMENU_PathStartsWithHttp, "路由地址必须以http(s)://开头");
        }*/
        if (StringUtils.isEmpty(sysMenu.getId())) {
            if (null != parentSysMenu) {
                // 如果父节点不为正常状态,则不允许新增子节点
                if (!UserConstants.DEPT_NORMAL.equals(parentSysMenu.getStatus())) {
                    throw new BizException(SysErrorCode.B_SYSMENU_MenuAlreadyUnNormal, "菜单停用，不允许新增子菜单");
                }
                setTreeProperties(sysMenu, null, Constants.OpType.insert);
                super.save(sysMenu);
            }else{
                //新增根节点
                sysMenu.setId(IdUtils.randomUUID());
                sysMenu.setNewRecord(true);
                sysMenu.setParentIds(sysMenu.getId());
                // 设置当前树全路径排序
                Integer treeSort = sysMenu.getTreeSort();
                String treeSorts = String.format("%06d", treeSort);
                sysMenu.setTreeSorts(treeSorts);
                sysMenu.setTreeLeaf(Constants.TREE_LEAF_Y);
                // 设置当前节点级别.
                sysMenu.setTreeLevel(1);
                super.save(sysMenu);
            }
        }else{
            //编辑
            SysMenu oldSysMenu = mapper.get(sysMenu);
            if (isMoveToSelfChildNode(sysMenu, oldSysMenu)) {
                throw new BizException(SysErrorCode.B_SYSMENU_MenuMoveUnNormal, "非法移动，不允许将节点移动到自身或者子节点下");
            }
            List<SysMenu> updateChildList = new ArrayList<SysMenu>();
            if (sysMenu.getParentId().equals(sysMenu.getId())) {
                throw new BizException(SysErrorCode.B_SYSMENU_MenuIdNotEqualParentId, "修改菜单失败，父级菜单不能是自己");
            }
            if (StringUtils.equals(UserConstants.DEPT_DISABLE, sysMenu.getStatus()) && mapper.selectNormalChildByParentIds(sysMenu.getParentIds()) > 0) {
                throw new BizException(SysErrorCode.B_SYSMENU_MenuHasNormalChild, "该菜单包含未停用的子菜单，不允许标记为停用");
            }
            if (isUpdateTreeProperties(sysMenu, oldSysMenu)) {
                setTreeProperties(sysMenu, oldSysMenu, Constants.OpType.update);
                updateChildList = updateChildTreeProperties(sysMenu, oldSysMenu);
                if (!CollectionUtils.isEmpty(updateChildList)) {
                    mapper.updateMenuChildren(updateChildList);
                }
            }
            parentSysMenu = mapper.get(sysMenu.getParentId());
            // 新的父节点变更为非叶子节点
            if (!Constants.TREE_ROOT.equals(sysMenu.getParentId()) && parentSysMenu.getTreeLeaf().equals(Constants.TREE_LEAF_Y)) {
                parentSysMenu.setTreeLeaf(Constants.TREE_LEAF_N);
                super.save(parentSysMenu);
            }
            String oldParentId = oldSysMenu.getParentId();
            super.save(sysMenu);
            // 判断原节点是否还有子节点，否则改为叶子节点
            if (!hasChildByDeptId(oldParentId)) {
                SysMenu oldParentSysMenu = mapper.get(oldParentId);
                oldParentSysMenu.setTreeLeaf(Constants.TREE_LEAF_Y);
                super.save(oldParentSysMenu);
            }
        }

        return Boolean.TRUE;
    }


    /**
     * 是否存在子节点
     *
     * @param sysMenuId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(String sysMenuId) {
        int result = findChildCountById(sysMenuId);
        return result > 0 ? true : false;
    }

    public Integer findChildCountById(String sysMenuId) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(sysMenuId);
        return mapper.findCount(sysMenu);
    }
    /**
     *  组建树形结构
     * @param entityNodes 节点集合
     * @return List<VueNode>
     */
    private List<SysMenu> createTreeGridData(List<SysMenu> entityNodes) {
        List<SysMenu> treeList = new ArrayList<SysMenu>();
        for (SysMenu sysMenu : entityNodes) {
            //找到根节点
            if (Constants.TREE_ROOT.equals(sysMenu.getParentId())) {
                treeList.add(sysMenu);
            }
            List<SysMenu> children = new ArrayList<SysMenu>();
            //再次遍历list，找到子节点
            for (SysMenu node : entityNodes) {
                if (node.getParentId().equals(sysMenu.getId())) {
                    children.add(node);
                }
            }
            sysMenu.setChildren(children);
            /*if (children.isEmpty()){
                sysMenu.setTreeLeaf("y");
            }else{
                sysMenu.setTreeLeaf("n");
            }*/
        }
        return treeList;
    }

    /**
     * 根据parentIds去重
     * @param entityNodes entityNodes
     */
    private Set<String> reDuplicationByTreePath(List<SysMenu> entityNodes) {
        Set<String> treeIdSet = new HashSet<String>();
        for (SysMenu dto : entityNodes) {
            String treePath = dto.getParentIds();
            treeIdSet.addAll(Arrays.asList(treePath.split("/")));
        }
        return treeIdSet;
    }

    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(String userId)
    {
        List<String> perms = mapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询菜单
     * 
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(String userId)
    {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId))
        {
            menus = mapper.selectMenuTreeAll();
        }
        else
        {
            menus = mapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, "0");
    }

    /**
     * 根据角色ID查询菜单树信息
     * 
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Integer> selectMenuListByRoleId(String roleId)
    {
        SysRole role = sysRoleMapper.get(roleId);
        return mapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    /**
     * 构建前端路由所需要的菜单
     * 
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus)
    {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus)
        {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setIsFrame(menu.getIsFrame());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()),menu.getRemark()));
            List<SysMenu> cMenus = (List<SysMenu>) menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType()))
            {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            else if (isMeunFrame(menu))
            {
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setIsFrame(menu.getIsFrame());
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()),menu.getRemark()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端所需要树结构
     * 
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<String> tempList = new ArrayList<String>();
        for (SysMenu menu : menus)
        {
            tempList.add(menu.getId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext();)
        {
            SysMenu menu = (SysMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId()))
            {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus)
    {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据菜单ID查询信息
     * 
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(String menuId)
    {
        return mapper.get(menuId);
    }

    /**
     * 是否存在菜单子节点
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(String menuId)
    {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(menuId);
        Integer result = mapper.findCount(sysMenu);
        return result > 0;
    }

    /**
     * 查询菜单使用数量
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(String menuId)
    {
        int result = sysRoleMenuMapper.checkMenuExistRole(menuId);
        return result > 0;
    }

    @Override
    public void checkMenuNameUnique(SysMenu menu) {
        SysMenu sysMenuUnique = new SysMenu();
        sysMenuUnique.setNotEqualId(menu.getId());
        sysMenuUnique.setMenuName(menu.getMenuName());
        sysMenuUnique.setParentId(menu.getParentId());
        if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysMenuUnique))) {
            throw new BizException(SysErrorCode.B_SYSMENU_MenuNameAlreadyExist);
        }
    }

    @Override
    public void checkMenuCodeUnique(SysMenu menu) {
        SysMenu sysMenuUnique = new SysMenu();
        sysMenuUnique.setNotEqualId(menu.getId());
        sysMenuUnique.setMenuCode(menu.getMenuCode());
        if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysMenuUnique))) {
            throw new BizException(SysErrorCode.B_SYSMENU_PathAlreadyExist);
        }
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     *//*
    @Override
    public String checkMenuNameUnique(SysMenu menu)
    {
        String menuId = menu.getId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getId().equals(menuId))
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
*/
    /**
     * 获取路由名称
     * 
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu)
    {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMeunFrame(menu))
        {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     * 
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu)
    {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if ("0".equals(menu.getParentId()) && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame()))
        {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMeunFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     * 
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu)
    {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMeunFrame(menu))
        {
            component = menu.getComponent();
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu))
        {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMeunFrame(SysMenu menu)
    {
        return "0".equals(menu.getParentId()) && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为parent_view组件
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu)
    {
        return !"0".equals(menu.getParentId()) && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     * 
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, String parentId)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (parentId.equals(t.getParentId()))
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     * 
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t)
    {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
            if (t.getId().equals(n.getParentId()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 根据展开层级和父节点递归获取展示的数据
     *
     * @param level
     * @param parentId
     * @return
     */
    @Override
    public List<SysMenu> listDataByLevel(int level, String parentId) {
        List<SysMenu> listData = new ArrayList<SysMenu>();
        level--;
        List<SysMenu> childrenList = findChildListByParentId(parentId);
        for (SysMenu dto : childrenList) {
            if ("n".equals(dto.getTreeLeaf()) && level != 0) {
                dto.setChildren(this.listDataByLevel(level, dto.getId()));
            } else {
                dto.setChildren(new ArrayList<>());
            }
            listData.add(dto);
        }
        return listData;
    }


    /**
     * 根据父ID查询当前子节点的个数
     *
     * @param parentId
     * @return
     */
    @Override
    public List<SysMenu> findChildListByParentId(String parentId) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(parentId);
        return mapper.findList(sysMenu);
    }

    /**
     * 删除菜单
     *
     * @param sysMenu
     * @return
     */
    @Override
    public boolean remove(SysMenu sysMenu) {
        sysMenu = mapper.get(sysMenu.getId());
        if (hasChildByDeptId(sysMenu.getId())) {
            throw new BizException(SysErrorCode.B_SYSMENU_MenuHasChildNotDelete, "该菜单包含子菜单，不允许删除");
        }
        //删除
        super.remove(sysMenu);
        //判断当前父节点下还是否有子节点，如果没有，则需要把父节点置为叶子节点
        SysMenu parentSysMenu = mapper.get(sysMenu.getParentId());
        if (findChildCountById(sysMenu.getParentId()) == 0) {
            parentSysMenu.setTreeLeaf(Constants.TREE_LEAF_Y);
            super.save(parentSysMenu);
        }
        return Boolean.TRUE;
    }


    /**
     * 构建前端所需要树结构
     *
     * @param level    展开层级
     * @param parentId 父节点ID
     * @return
     */
    @Override
    public List<TreeNode> buildMenuTree(int level, String parentId,String userId){
        return this.buildMenuTreeExcludeChild(level, parentId,userId, "");
    }
    /**
     * 构建前端所需要树结构
     *
     * @param level     展开层级
     * @param parentId  父节点ID
     * @param excludeId 排除节点ID
     * @return
     */
    public List<TreeNode> buildMenuTreeExcludeChild(int level, String parentId,String userId, String excludeId) {
        List<TreeNode> listData = new ArrayList<TreeNode>();
        level--;
        List<SysMenu> menuList = new ArrayList<SysMenu>();
        SysMenu menu = new SysMenu();
        menu.setParentId(parentId);
        menu.setStatus("0");
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId) || StringUtils.isEmpty(userId))
        {
            menuList = mapper.findList(menu);
        }
        else
        {
            menu.getParams().put("userId", userId);
            menuList = mapper.selectMenuListByUserId(menu);
        }
        for (SysMenu sysMenu : menuList) {
            if (!sysMenu.getId().equals(excludeId)) {
                TreeNode treeNode = this.transToTreeNodeData(sysMenu);
                if ("n".equals(sysMenu.getTreeLeaf()) && level != 0) {
                    treeNode.setChildren(this.buildMenuTreeExcludeChild(level, sysMenu.getId(),userId, excludeId));
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
     *  将实体类集合转化成VueNode格式
     * @param entityNodes  实体类节点集合
     * @return List<VueNode>
     */
    private List<TreeNode> transToTreeNodeData(List<SysMenu> entityNodes) {
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (SysMenu entityNode : entityNodes) {
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
    private TreeNode transToTreeNodeData(SysMenu entityNode) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(entityNode.getId());
        treeNode.setKey(entityNode.getId());
        treeNode.setTitle(entityNode.getMenuName());
        treeNode.setLabel(entityNode.getMenuName());
        treeNode.setSelectable(true);
        treeNode.setDisabled(false);
        treeNode.setDisableCheckbox(false);
        treeNode.setParentId(entityNode.getParentId());
        treeNode.setParentIds(entityNode.getParentIds());
        HashMap<String,Object> attr = new HashMap<>(3);
        attr.put("treeLeaf", entityNode.getTreeLeaf());
        attr.put("treeLevel", entityNode.getTreeLevel());
        attr.put("treePath", entityNode.getParentIds());
        treeNode.setAttributes(attr);
        if (Constants.TREE_LEAF_Y.equals(entityNode.getTreeLeaf())){
            treeNode.setIsLeaf(true);
        } else{
            treeNode.setIsLeaf(false);
        }
        JSONObject slotsValue = new JSONObject();
        JSONObject scopedSlotsValue = new JSONObject();
        treeNode.setSlots(slotsValue);
        treeNode.setScopedSlots(scopedSlotsValue);
        return treeNode;
    }

    @Override
    public Integer findMaxSort(SysMenu sysMenu){
        return NumberUtils.nextOrder(mapper.findMaxSort(sysMenu));
    }
}
