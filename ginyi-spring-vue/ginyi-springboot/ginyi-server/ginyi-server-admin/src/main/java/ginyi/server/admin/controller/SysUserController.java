package ginyi.server.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import ginyi.common.annotation.Log;
import ginyi.common.enums.BusinessType;
import ginyi.common.result.CommonResult;
import ginyi.common.swagger.AddGroup;
import ginyi.common.swagger.UpdateGroup;
import ginyi.system.domain.SysUser;
import ginyi.system.domain.model.dto.DeptDto;
import ginyi.system.domain.model.dto.PostDto;
import ginyi.system.domain.model.dto.RoleDto;
import ginyi.system.domain.model.dto.UserDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.UserVo;
import ginyi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Set;

@Api(tags = "用户模块")
@RestController
@Slf4j
@RequestMapping("/api/user")
public class SysUserController {

    @Resource
    private ISysUserService userService;

    @ApiOperation("删除用户")
    @PostMapping("/delete/{userId}")
    @PreAuthorize("@ss.hasPermission('system:user:remove')")
    @Log(title = "用户模块", businessType = BusinessType.DELETE)
    public CommonResult delete(@PathVariable("userId") Long userId){
        userService.removeById(userId);
        return CommonResult.success();
    }

    @ApiOperation("用户详情")
    @GetMapping("/getUserByUserId/{userId}")
    @PreAuthorize("@ss.hasPermission('system:user:edit')")
    public CommonResult<UserVo> getUserByUserId(@PathVariable("userId") String userId) {
        UserVo user = userService.getUserByUserId(userId);
        return CommonResult.success(user);
    }

    @ApiOperation("批量删除用户")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:user:remove')")
    @Log(title = "用户模块", businessType = BusinessType.DELETE)
    public CommonResult delete(@RequestBody Set<Long> ids){
        userService.removeUserByIds(ids);
        return CommonResult.success();
    }

    @ApiOperation("更新用户状态")
    @PostMapping("/updateStatus")
    @PreAuthorize("@ss.hasPermission('system:user:edit')")
    @Log(title = "用户模块", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(includeParameters = {
            "userDto.userId",
            "userDto.status"
    })
    public CommonResult updateStatus(@RequestBody UserDto userDto){
        userService.updateStatus(userDto);
        return CommonResult.success();
    }

    @ApiOperation("添加用户")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermission('system:user:add')")
    @Log(title = "用户模块", businessType = BusinessType.INSERT)
    @ApiOperationSupport(ignoreParameters = {
            "userDto.beginTime",
            "userDto.endTime",
            "userDto.createBy",
            "userDto.createTime",
            "userDto.deleted",
            "userDto.loginDate",
            "userDto.loginIp",
            "userDto.params",
            "userDto.updateBy",
            "userDto.updateTime",
            "userDto.userId",
            "userDto.password",
    })
    public CommonResult add(@RequestBody @Validated(AddGroup.class) UserDto userDto) {
        userService.addUser(userDto);
        return CommonResult.success();
    }

    @ApiOperation("更新用户")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermission('system:user:edit')")
    @Log(title = "用户模块", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(ignoreParameters = {
            "userDto.beginTime",
            "userDto.endTime",
            "userDto.createBy",
            "userDto.createTime",
            "userDto.deleted",
            "userDto.loginDate",
            "userDto.loginIp",
            "userDto.params",
            "userDto.updateBy",
            "userDto.updateTime",
            "userDto.password",
    })
    public CommonResult update(@RequestBody @Validated(UpdateGroup.class) UserDto userDto) {
        userService.updateUser(userDto);
        return CommonResult.success();
    }


    @ApiOperation("用户列表")
    @PostMapping("/list")
    @PreAuthorize("@ss.hasPermission('system:user:list')")
    @ApiOperationSupport(ignoreParameters = {
            "userDto.deleted",
            "userDto.loginDate",
            "userDto.loginIp",
            "userDto.params",
            "userDto.userId",
            "userDto.password",
            "userDto.avatar",
            "userDto.updateBy",
            "userDto.updateTime",
            "userDto.createTime",
            "userDto.deptId",
            "userDto.postIds",
            "userDto.roleIds",
    })
    public CommonResult<BaseVo<UserVo>> list(@RequestBody UserDto userDto,
                             @RequestParam(value = "page", required = false) Long page,
                             @RequestParam(value = "pageSize", required = false) Long pageSize) {
        BaseVo<UserVo> baseVo = userService.list(userDto, page, pageSize);
        return CommonResult.success(baseVo);
    }

    @ApiOperation("根据部门id获取用户")
    @PostMapping("getUserListByDeptIds")
    @ApiOperationSupport(ignoreParameters = {
            "deptDto.deptId",
            "deptDto.parentId",
            "deptDto.ancestors",
            "deptDto.deptName",
            "deptDto.sort",
            "deptDto.leader",
            "deptDto.phone",
            "deptDto.email",
            "deptDto.beginTime",
            "deptDto.endTime",
            "deptDto.updateBy",
            "deptDto.updateTime",
            "deptDto.createBy",
            "deptDto.createTime",
            "deptDto.remark",
            "deptDto.params",
            "deptDto.status",
    })
    public CommonResult<BaseVo<HashMap<String, Object>>> getUserListByDeptIds(@RequestBody DeptDto deptDto){
        BaseVo<HashMap<String, Object>> userList = userService.getUserListByDeptIds(deptDto);
        return CommonResult.success(userList);
    }

    @ApiOperation("根据岗位id获取用户")
    @PostMapping("getUserListByPostIds")
    @ApiOperationSupport(ignoreParameters = {
            "postDto.postId",
            "postDto.postName",
            "postDto.sort",
            "postDto.postCode",
            "postDto.beginTime",
            "postDto.endTime",
            "postDto.updateBy",
            "postDto.updateTime",
            "postDto.createBy",
            "postDto.createTime",
            "postDto.remark",
            "postDto.params",
            "postDto.status",
    })
    public CommonResult<BaseVo<HashMap<String, Object>>> getUserListByPostIds(@RequestBody PostDto postDto){
        BaseVo<HashMap<String, Object>> userList = userService.getUserListByPostIds(postDto);
        return CommonResult.success(userList);
    }

    @ApiOperation("根据角色id获取用户")
    @PostMapping("getUserListByRoleIds")
    @ApiOperationSupport(ignoreParameters = {
            "roleDto.roleId",
            "roleDto.roleName",
            "roleDto.sort",
            "roleDto.roleKey",
            "roleDto.beginTime",
            "roleDto.endTime",
            "roleDto.updateBy",
            "roleDto.updateTime",
            "roleDto.createBy",
            "roleDto.createTime",
            "roleDto.remark",
            "roleDto.params",
            "roleDto.status",
            "roleDto.permissions",
    })
    public CommonResult<BaseVo<HashMap<String, Object>>> getUserListByPostIds(@RequestBody RoleDto roleDto){
        BaseVo<HashMap<String, Object>> userList = userService.getUserListByRoleIds(roleDto);
        return CommonResult.success(userList);
    }
}
