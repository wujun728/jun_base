package com.aidex.web.controller.system;

import com.aidex.common.annotation.Log;
import com.aidex.common.constant.Constants;
import com.aidex.common.core.controller.BaseController;
import com.aidex.common.core.domain.AjaxResult;
import com.aidex.common.core.domain.R;
import com.aidex.common.core.domain.TreeNode;
import com.aidex.common.core.domain.entity.SysMenu;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.enums.BusinessType;
import com.aidex.common.utils.StringUtils;
import com.aidex.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController
{
    @Autowired
    private SysMenuService menuService;

    /**
     * 根据层级展开部门树表格
     * @param level 展开层级
     * @param id 起始展开ID
     * @return com.aidex.common.core.domain.R
     */
    @GetMapping("/list/{level}/{id}")
    public R list(@PathVariable("level") @NotEmpty int level, @PathVariable("id") String id) {
        if (level == 0) {
            level = 2;
        }
        //默认为根节点
        if (StringUtils.isEmpty(id)) {
            id = Constants.TREE_ROOT;
        }
        List<SysMenu> menus = menuService.listDataByLevel(level, id);
        return R.data(menus);
    }

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public R list(SysMenu menu)
    {
        LoginUser loginUser = getLoginUser();
        String userId = loginUser.getUser().getId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return R.data(menus);
    }

    /**
     * 部门树表格检索
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/searchMenuList")
    public R searchDeptList (SysMenu menu)
    {
        List<SysMenu> menus =  menuService.searchMenuList(menu);
        return R.data(menus);
    }
    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{id}")
    public R detail(@NotBlank @PathVariable String id) {
        return R.data(menuService.get(id));
    }

    /**
     * 获取当前父节点下最大编号
     * @param parentId
     * @return com.aidex.common.core.domain.R
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @Log(title = "菜单管理", businessType = BusinessType.SELECT)
    @GetMapping("/findMaxSort/{parentId}")
    public R findMaxSort(@PathVariable("parentId") String parentId) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(parentId);
        return R.data(menuService.findMaxSort(sysMenu));
    }

    @GetMapping("/treeselect/{level}/{id}")
    public R treeselect(SysMenu menu,@NotBlank @PathVariable("level") int level, @PathVariable("id") String id) {
        if (level == 0) {
            level = 2;
        }
        LoginUser loginUser = getLoginUser();
        String userId = loginUser.getUser().getId();
        //默认为根节点
        if (StringUtils.isEmpty(id)) {
            id = Constants.TREE_ROOT;
        }
        List<TreeNode> depts = menuService.buildMenuTree(level, id,userId);
        return R.data(depts);
    }
    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public R roleMenuTreeselect(@PathVariable("roleId") String roleId)
    {
        LoginUser loginUser = getLoginUser();
        List<SysMenu> menus = menuService.selectMenuList(loginUser.getUser().getId());
        Map<String, Object> ajax = new HashMap<String, Object>(2);
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTree(-1, Constants.TREE_ROOT, null));
        return R.data(ajax);
    }

    /**
     * 新增菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R save(@Validated @RequestBody SysMenu menu)
    {
        menuService.save(menu);
        return R.data(menu);
    }


    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") String menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        SysMenu sysMenu = new SysMenu();
        sysMenu.setId(menuId);
        return toAjax(menuService.remove(sysMenu));
    }

    /**
     * 校验菜单名称是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.CHECK)
    @GetMapping("/checkMenuNameUnique")
    public R checkMenuNameUnique(SysMenu sysMenu) {
        Map<String,String> checkMap = new HashMap<String,String>(2);
        try{
            menuService.checkMenuNameUnique(sysMenu);
            checkMap.put("code","1");
        }catch (Exception e){
            checkMap.put("code","2");
        }
        return R.data(checkMap);
    }

    /**
     * 校验路由地址是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.CHECK)
    @GetMapping("/checkMenuCodeUnique")
    public R checkMenuCodeUnique(SysMenu sysMenu) {
        Map<String,String> checkMap = new HashMap<String,String>(2);
        try{
            menuService.checkMenuCodeUnique(sysMenu);
            checkMap.put("code","1");
        }catch (Exception e){
            checkMap.put("code","2");
        }
        return R.data(checkMap);
    }
}