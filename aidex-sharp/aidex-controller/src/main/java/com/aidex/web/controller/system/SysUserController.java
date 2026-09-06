package com.aidex.web.controller.system;

import com.aidex.common.annotation.Log;
import com.aidex.common.core.controller.BaseController;
import com.aidex.common.core.domain.AjaxResult;
import com.aidex.common.core.domain.R;
import com.aidex.common.core.domain.entity.SysRole;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.enums.BusinessType;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.poi.ExcelUtil;
import com.aidex.system.domain.SysPost;
import com.aidex.system.domain.SysUserRole;
import com.aidex.system.service.ISysRoleService;
import com.aidex.system.service.ISysUserService;
import com.aidex.system.service.SysPostService;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private SysPostService postService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public R<PageInfo> page(SysUser user, HttpServletRequest request, HttpServletResponse response) {
        user.setPage(new PageDomain(request, response));
        return R.data(userService.findPage(user));
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public R export(SysUser user) {
        List<SysUser> list = userService.findList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        LoginUser loginUser = getLoginUser();
        String operName = loginUser.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public R importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) String userId) {
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.findList(new SysPost()));
        if (StringUtils.isNotNull(userId)) {
            ajax.put(AjaxResult.DATA_TAG, userService.get(userId));
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@Validated @RequestBody SysUser user) {
        return R.status(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        return R.status(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R remove(@PathVariable("userIds") String[] userIds) {
        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.fail("当前用户不能删除");
        }
        return R.status(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public R resetPwd(@RequestBody SysUser user) {
        SysUser dbUser = userService.get(user.getId());
        userService.checkUserAllowed(dbUser);
        return R.status(userService.resetUserPwd(dbUser.getUserName(),SecurityUtils.encryptPassword(user.getPassword())));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return R.status(userService.updateUserStatus(user));
    }

    /**
     * 校验角色编码是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.CHECK)
    @GetMapping("/checkUserNameUnique")
    public R checkUserNameUnique(SysUser user) {
        Map<String, String> checkMap = new HashMap<String, String>(1);
        try {
            userService.checkUserNameUnique(user);
            checkMap.put("code", "1");
        } catch (Exception e) {
            checkMap.put("code", "2");
        }
        return R.data(checkMap);
    }

    /**
     * 校验角色编码是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.CHECK)
    @GetMapping("/checkEmailUnique")
    public R checkEmailUnique(SysUser user) {
        Map<String, String> checkMap = new HashMap<String, String>(1);
        try {
            userService.checkEmailUnique(user);
            checkMap.put("code", "1");
        } catch (Exception e) {
            checkMap.put("code", "2");
        }
        return R.data(checkMap);
    }

    /**
     * 校验角色编码是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.CHECK)
    @GetMapping("/checkPhoneUnique")
    public R checkPhoneUnique(SysUser user) {
        Map<String, String> checkMap = new HashMap<String, String>(1);
        try {
            userService.checkPhoneUnique(user);
            checkMap.put("code", "1");
        } catch (Exception e) {
            checkMap.put("code", "2");
        }
        return R.data(checkMap);
    }

    /**
     * 通用选人页面根据用户ID
     *
     * @return
     */
    @PostMapping(value = {"/getUserInfoByIds"})
    public R getUserInfoByIds(@Validated @RequestBody JSONObject userIdsObj) {
        List<Map<String, Object>> sysUserList = userService.getUserInfoByIds(userIdsObj);
        return R.data(sysUserList);
    }

    /**
     * 获取角色下所有用户信息
     *
     * @param sysUser
     * @param request
     * @param response
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/roleUserList")
    public R<PageInfo> roleUserPage(SysUser sysUser, HttpServletRequest request, HttpServletResponse response) {
        sysUser.setPage(new PageDomain(request, response));
        return R.data(userService.findRoleUserPage(sysUser));
    }

    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/addRoleUser")
    public R addRoleUser(@Validated @RequestBody SysUserRole sysUserRole) {
        return R.status(userService.insertRoleUser(sysUserRole));
    }

    /**
     * 刷新缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "用户管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R refreshCache() {
        userService.refreshCache();
        return R.status(true);
    }
}
