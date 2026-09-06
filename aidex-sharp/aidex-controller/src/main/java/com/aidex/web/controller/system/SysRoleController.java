package com.aidex.web.controller.system;

import com.aidex.common.annotation.Log;
import com.aidex.common.core.controller.BaseController;
import com.aidex.common.core.domain.AjaxResult;
import com.aidex.common.core.domain.R;
import com.aidex.common.core.domain.entity.SysRole;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.enums.BusinessType;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.ServletUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.poi.ExcelUtil;
import com.aidex.framework.web.service.SysPermissionService;
import com.aidex.framework.web.service.TokenService;
import com.aidex.system.service.ISysRoleService;
import com.aidex.system.service.ISysUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;

    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public R<PageInfo> page(SysRole role, HttpServletRequest request, HttpServletResponse response)
    {
        role.setPage(new PageDomain(request, response));
        return R.data(roleService.findPage(role));
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @GetMapping("/export")
    public R export(SysRole role)
    {
        List<SysRole> list = roleService.findList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.exportExcel(list, "角色数据");
    }

    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable String roleId)
    {
        return AjaxResult.success(roleService.get(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@Validated @RequestBody SysRole role)
    {
        role.setCreateBy(SecurityUtils.getUsername());
        return R.status(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        if (roleService.updateRole(role) > 0)
        {
            // 更新缓存用户权限
            LoginUser loginUser = getLoginUser();
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin())
            {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
                tokenService.setLoginUser(loginUser);
            }
            return AjaxResult.success();
        }
        return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R changeStatus(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        return R.status(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable String[] roleIds)
    {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        return AjaxResult.success(roleService.selectRoleAll());
    }

    /**
     * 校验角色名称是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.CHECK)
    @GetMapping("/checkRoleNameUnique")
    public R checkRoleNameUnique(SysRole role) {
        Map<String,String> checkMap = new HashMap<String,String>(1);
        try{
            roleService.checkRoleNameUnique(role);
            checkMap.put("code","1");
        }catch (Exception e){
            checkMap.put("code","2");
        }
        return R.data(checkMap);
    }

    /**
     * 校验角色编码是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.CHECK)
    @GetMapping("/checkRoleKeyUnique")
    public R checkRoleKeyUnique(SysRole role) {
        Map<String,String> checkMap = new HashMap<String,String>(1);
        try{
            roleService.checkRoleKeyUnique(role);
            checkMap.put("code","1");
        }catch (Exception e){
            checkMap.put("code","2");
        }
        return R.data(checkMap);
    }

    /**
     * 获取最大排序号
     */
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @Log(title = "角色管理", businessType = BusinessType.SELECT)
    @GetMapping("/findMaxSort")
    public R findMaxSort() {
        SysRole sysRole = new SysRole();
        return R.data(roleService.findMaxSort(sysRole));
    }

    @PreAuthorize("@ss.hasAnyPermi('system:role:add,system:role:edit,system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/batchSave")
    public R batchSave(@Validated @RequestBody List<SysRole> roleList)
    {
        return R.status(roleService.batchSave(roleList));

    }
    /**
     * 删除角色下选中用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("deleteRoleUser/{roleId}/{userIds}")
    public R deleteRoleUser(@PathVariable String roleId,@PathVariable String[] userIds)
    {
        return R.status(roleService.deleteRoleUser(roleId,userIds));
    }


    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/saveRolePortlet")
    public R saveRolePortlet(@RequestBody  SysRole sysRole)
    {
        return R.data(roleService.saveRolePortlet(sysRole));
    }
}
