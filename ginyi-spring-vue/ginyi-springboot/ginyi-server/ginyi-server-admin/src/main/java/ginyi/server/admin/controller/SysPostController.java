package ginyi.server.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import ginyi.common.annotation.Log;
import ginyi.common.enums.BusinessType;
import ginyi.common.result.CommonResult;
import ginyi.common.swagger.AddGroup;
import ginyi.system.domain.model.dto.DeptDto;
import ginyi.system.domain.model.dto.PostDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.PostVo;
import ginyi.system.service.ISysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;


@Api(tags = "岗位模块")
@RestController
@RequestMapping("/api/post")
public class SysPostController {

    @Resource
    private ISysPostService postService;

    @ApiOperation("岗位详情")
    @GetMapping("/getPostById/{postId}")
    @PreAuthorize("@ss.hasPermission('system:post:edit')")
    public CommonResult<PostVo> getPostByDeptId(@PathVariable("postId") Long postId) {
        PostVo postVo = postService.getPostByPostId(postId);
        return CommonResult.success(postVo);
    }

    @ApiOperation("删除岗位")
    @PostMapping("/delete/{postId}")
    @PreAuthorize("@ss.hasPermission('system:post:remove')")
    @Log(title = "岗位模块", businessType = BusinessType.DELETE)
    public CommonResult delete(@PathVariable("postId") Long postId) {
        postService.removePostById(postId);
        return CommonResult.success();
    }

    @ApiOperation("批量删除岗位")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:post:remove')")
    @Log(title = "岗位模块", businessType = BusinessType.DELETE)
    public CommonResult delete(@RequestBody Set<Long> ids) {
        postService.removeDeptByIds(ids);
        return CommonResult.success();
    }

    @ApiOperation("岗位列表")
    @PostMapping("/list")
    @PreAuthorize("@ss.hasPermission('system:post:list')")
    @ApiOperationSupport(ignoreParameters = {
            "postDto.createTime",
            "postDto.params",
            "postDto.updateBy",
            "postDto.updateTime",
            "postDto.postId",
            "postDto.sort",
    })
    public CommonResult<BaseVo<PostVo>> list(@RequestBody PostDto postDto,
                                             @RequestParam(value = "page", required = false) Long page,
                                             @RequestParam(value = "pageSize", required = false) Long pageSize) {
        BaseVo<PostVo> list = postService.list(postDto, page, pageSize);
        return CommonResult.success(list);
    }


    @ApiOperation("新增岗位")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermission('system:post:add')")
    @Log(title = "岗位模块", businessType = BusinessType.INSERT)
    @ApiOperationSupport(ignoreParameters = {
            "postDto.createBy",
            "postDto.createTime",
            "postDto.beginTime",
            "postDto.endTime",
            "postDto.params",
            "postDto.updateBy",
            "postDto.updateTime",
            "postDto.postId",
    })
    public CommonResult addPost(@RequestBody @Validated(AddGroup.class) PostDto postDto) {
        postService.addPost(postDto);
        return CommonResult.success();
    }

    @ApiOperation("更新岗位")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermission('system:post:edit')")
    @Log(title = "岗位模块", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(ignoreParameters = {
            "postDto.createBy",
            "postDto.createTime",
            "postDto.beginTime",
            "postDto.endTime",
            "postDto.params",
            "postDto.updateBy",
            "postDto.updateTime",
    })
    public CommonResult update(@RequestBody @Validated PostDto postDto){
        postService.updatePost(postDto);
        return CommonResult.success();
    }

    @ApiOperation("更新岗位状态")
    @PostMapping("/updateStatus")
    @PreAuthorize("@ss.hasPermission('system:post:edit')")
    @Log(title = "岗位模块", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(includeParameters = {
            "postDto.postId",
            "postDto.status"
    })
    public CommonResult updateStatus(@RequestBody PostDto postDto){
        postService.updateStatus(postDto);
        return CommonResult.success();
    }
}
