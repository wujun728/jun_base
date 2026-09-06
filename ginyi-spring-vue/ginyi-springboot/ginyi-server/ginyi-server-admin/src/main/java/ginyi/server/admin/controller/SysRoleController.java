package ginyi.server.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import ginyi.common.annotation.Log;
import ginyi.common.enums.BusinessType;
import ginyi.common.result.CommonResult;
import ginyi.common.swagger.AddGroup;
import ginyi.common.swagger.UpdateGroup;
import ginyi.system.domain.model.dto.PostDto;
import ginyi.system.domain.model.dto.RoleDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.RoleVo;
import ginyi.system.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

@Api(tags = "角色模块")
@RestController
@RequestMapping("/api/role")
public class SysRoleController {

    @Resource
    private ISysRoleService roleService;

    @ApiOperation("角色列表")
    @PostMapping("/list")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    @ApiOperationSupport(ignoreParameters = {
            "roleDto.updateBy",
            "roleDto.updateTime",
            "roleDto.createTime",
            "roleDto.params",
            "roleDto.sort",
            "roleDto.permissions",
            "roleDto.roleId",
    })
    public CommonResult<BaseVo<RoleVo>> list(@RequestBody @Validated RoleDto roleDto,
                                             @RequestParam(value = "page", required = false) Long page,
                                             @RequestParam(value = "pageSize", required = false) Long pageSize) {
        BaseVo<RoleVo> list = roleService.list(roleDto, page, pageSize);
        return CommonResult.success(list);
    }

    @ApiOperation("角色详情")
    @GetMapping("/getRoleByRoleId/{roleId}")
    @PreAuthorize("@ss.hasPermission('system:role:edit')")
    public CommonResult getRoleByRoleId(@PathVariable("roleId") Long roleId){
        RoleVo role = roleService.getRoleByRoleId(roleId);
        return CommonResult.success(role);
    }

    @ApiOperation("新增角色")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermission('system:role:add')")
    @Log(title = "角色模块", businessType = BusinessType.INSERT)
    @ApiOperationSupport(ignoreParameters = {
            "roleDto.updateBy",
            "roleDto.updateTime",
            "roleDto.createBy",
            "roleDto.createTime",
            "roleDto.beginTime",
            "roleDto.endTime",
            "roleDto.params",
            "roleDto.roleId",
    })
    public CommonResult addRole(@RequestBody @Validated(AddGroup.class) RoleDto roleDto){
        roleService.addRole(roleDto);
        return CommonResult.success();
    }

    @ApiOperation("更新角色")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermission('system:role:add')")
    @Log(title = "角色模块", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(ignoreParameters = {
            "roleDto.updateBy",
            "roleDto.updateTime",
            "roleDto.createBy",
            "roleDto.createTime",
            "roleDto.beginTime",
            "roleDto.endTime",
            "roleDto.params",
    })
    public CommonResult update(@RequestBody @Validated(UpdateGroup.class) RoleDto roleDto){
        roleService.updateRole(roleDto);
        return CommonResult.success();
    }

    @ApiOperation("删除角色")
    @PostMapping("/delete/{roleId}")
    @PreAuthorize("@ss.hasPermission('system:role:remove')")
    @Log(title = "角色模块", businessType = BusinessType.DELETE)
    public CommonResult delete(@PathVariable("roleId") Long roleId){
        roleService.removeByRoleId(roleId);
        return CommonResult.success();
    }

    @ApiOperation("批量删除角色")
    @PreAuthorize("@ss.hasPermission('system:role:remove')")
    @PostMapping("/delete")
    @Log(title = "角色模块", businessType = BusinessType.DELETE)
    public CommonResult delete(@RequestBody Set<Long> ids){
        roleService.removeByRoleIds(ids);
        return CommonResult.success();
    }

    @ApiOperation("更新角色状态")
    @PostMapping("/updateStatus")
    @PreAuthorize("@ss.hasPermission('system:post:edit')")
    @Log(title = "角色模块", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(includeParameters = {
            "roleDto.roleId",
            "roleDto.status"
    })
    public CommonResult updateStatus(@RequestBody RoleDto roleDto){
        roleService.updateStatus(roleDto);
        return CommonResult.success();
    }

}
