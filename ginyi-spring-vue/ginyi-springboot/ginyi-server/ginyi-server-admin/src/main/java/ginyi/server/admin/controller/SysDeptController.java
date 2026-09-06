package ginyi.server.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import ginyi.common.annotation.Log;
import ginyi.common.enums.BusinessType;
import ginyi.common.result.CommonResult;
import ginyi.common.swagger.AddGroup;
import ginyi.common.swagger.UpdateGroup;
import ginyi.system.domain.SysDept;
import ginyi.system.domain.model.dto.DeptDto;
import ginyi.system.domain.model.dto.UserDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.DeptVo;
import ginyi.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

@Api(tags = "部门模块")
@RestController
@RequestMapping("/api/dept")
public class SysDeptController {

    @Resource
    private ISysDeptService deptService;

    @ApiOperation("删除部门")
    @PostMapping("/delete/{deptId}")
    @PreAuthorize("@ss.hasPermission('system:dept:remove')")
    @Log(title = "部门模块", businessType = BusinessType.DELETE)
    public CommonResult delete(@PathVariable("deptId") Long deptId) {
        deptService.removeDeptById(deptId);
        return CommonResult.success();
    }

    @ApiOperation("批量删除部门")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:dept:remove')")
    @Log(title = "部门模块", businessType = BusinessType.DELETE)
    public CommonResult delete(@RequestBody Set<Long> ids) {
        deptService.removeDeptByIds(ids);
        return CommonResult.success();
    }

    @ApiOperation("部门详情")
    @GetMapping("/getDeptByDeptId/{deptId}")
    @PreAuthorize("@ss.hasPermission('system:dept:edit')")
    public CommonResult<DeptVo> getDeptByDeptId(@PathVariable("deptId") Long deptId) {
        DeptVo deptVo = deptService.getDeptByDeptId(deptId);
        return CommonResult.success(deptVo);
    }


    @ApiOperation("部门列表")
    @PostMapping("/list")
    @PreAuthorize("@ss.hasPermission('system:dept:list')")
    @ApiOperationSupport(ignoreParameters = {
            "deptDto.updateBy",
            "deptDto.updateTime",
            "deptDto.createTime",
            "deptDto.params",
            "deptDto.ancestors",
            "deptDto.deptId",
            "deptDto.sort",
    })
    public CommonResult<BaseVo<SysDept>> list(@RequestBody DeptDto deptDto,
                                              @RequestParam(value = "page", required = false) Long page,
                                              @RequestParam(value = "pageSize", required = false) Long pageSize) {
        BaseVo<SysDept> list = deptService.list(deptDto, page, pageSize);
        return CommonResult.success(list);
    }

    @ApiOperation("新增部门")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermission('system:dept:add')")
    @Log(title = "部门模块", businessType = BusinessType.INSERT)
    @ApiOperationSupport(ignoreParameters = {
            "deptDto.updateBy",
            "deptDto.updateTime",
            "deptDto.createTime",
            "deptDto.createBy",
            "deptDto.params",
            "deptDto.ancestors",
            "deptDto.deptId",
            "deptDto.beginTime",
            "deptDto.endTime",
    })
    public CommonResult addDept(@RequestBody @Validated(AddGroup.class) DeptDto deptDto) {
        deptService.addDept(deptDto);
        return CommonResult.success();
    }

    @ApiOperation("更新部门")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermission('system:dept:edit')")
    @Log(title = "部门模块", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(ignoreParameters = {
            "deptDto.updateBy",
            "deptDto.updateTime",
            "deptDto.createTime",
            "deptDto.createBy",
            "deptDto.params",
            "deptDto.ancestors",
            "deptDto.beginTime",
            "deptDto.endTime",
    })
    public CommonResult update(@RequestBody @Validated(UpdateGroup.class) DeptDto deptDto) {
        deptService.updateDept(deptDto);
        return CommonResult.success();
    }

    @ApiOperation("更新部门状态")
    @PostMapping("/updateStatus")
    @PreAuthorize("@ss.hasPermission('system:dept:edit')")
    @Log(title = "部门模块", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(includeParameters = {
            "deptDto.deptId",
            "deptDto.status"
    })
    public CommonResult updateStatus(@RequestBody DeptDto deptDto){
        deptService.updateStatus(deptDto);
        return CommonResult.success();
    }

}
