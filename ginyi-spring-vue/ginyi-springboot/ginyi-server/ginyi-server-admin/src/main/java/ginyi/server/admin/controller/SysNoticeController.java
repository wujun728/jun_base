package ginyi.server.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import ginyi.common.annotation.Log;
import ginyi.common.enums.BusinessType;
import ginyi.common.result.CommonResult;
import ginyi.common.swagger.AddGroup;
import ginyi.common.swagger.UpdateGroup;
import ginyi.system.domain.SysNotice;
import ginyi.system.domain.model.dto.NoticeDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.NoticeVo;
import ginyi.system.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "通知公告模块")
@RestController
@RequestMapping("/api/notice")
public class SysNoticeController {

    @Resource
    private ISysNoticeService noticeService;

    @ApiOperation("通知公告列表（管理员）")
    @PostMapping("/list")
    @ApiOperationSupport(ignoreParameters = {
            "noticeDto.noticeId",
            "noticeDto.remark",
            "noticeDto.updateBy",
            "noticeDto.updateTime",
            "noticeDto.createBy",
            "noticeDto.createTime",
            "noticeDto.params",
            "noticeDto.content",
            "noticeDto.userIds",
            "noticeDto.userReadIds",
    })
    @PreAuthorize("@ss.hasPermission('system:notice:query:admin')")
    public CommonResult<BaseVo<SysNotice>> list(@RequestBody NoticeDto noticeDto,
                                               @RequestParam(value = "page", required = false) Long page,
                                               @RequestParam(value = "pageSize", required = false) Long pageSize) {
        BaseVo<SysNotice> baseVo = noticeService.list(noticeDto, page, pageSize);
        return CommonResult.success(baseVo);
    }

    @ApiOperation("获取用户通知公告列表")
    @GetMapping("/getUserNoticeList")
    @PreAuthorize("@ss.hasPermission('system:notice:query')")
    public CommonResult<BaseVo<NoticeVo>> getUserNoticeList(@RequestParam(value = "page", required = false) Long page,
                                                            @RequestParam(value = "pageSize", required = false) Long pageSize){
        BaseVo<NoticeVo> baseVo = noticeService.getUserNoticeList(page, pageSize);
        return CommonResult.success(baseVo);
    }

    @ApiOperation("发布通知公告")
    @PostMapping("/add")
    @ApiOperationSupport(ignoreParameters = {
            "noticeDto.noticeId",
            "noticeDto.remark",
            "noticeDto.updateBy",
            "noticeDto.updateTime",
            "noticeDto.createBy",
            "noticeDto.createTime",
            "noticeDto.params",
            "noticeDto.userReadIds",
    })
    @PreAuthorize("@ss.hasPermission('system:notice:add')")
    @Log(title = "通知公告模块", businessType = BusinessType.INSERT)
    public CommonResult add(@RequestBody @Validated(AddGroup.class) NoticeDto noticeDto){
        noticeService.add(noticeDto);
        return CommonResult.success();
    }

    @ApiOperation("更新通知公告")
    @PostMapping("/update")
    @ApiOperationSupport(ignoreParameters = {
            "noticeDto.remark",
            "noticeDto.updateBy",
            "noticeDto.updateTime",
            "noticeDto.createBy",
            "noticeDto.createTime",
            "noticeDto.params",
            "noticeDto.userReadIds",
    })
    @PreAuthorize("@ss.hasPermission('system:notice:edit')")
    @Log(title = "通知公告模块", businessType = BusinessType.UPDATE)
    public CommonResult update(@RequestBody @Validated(UpdateGroup.class) NoticeDto noticeDto){
        noticeService.updateNotice(noticeDto);
        return CommonResult.success();
    }

    @ApiOperation("确认收到通知公告")
    @PostMapping("/haveRead/{noticeId}")
    @PreAuthorize("@ss.hasPermission('system:notice:haveRead')")
    @Log(title = "通知公告模块", businessType = BusinessType.UPDATE)
    public CommonResult haveRead(@PathVariable Long noticeId){
        noticeService.haveRead(noticeId);
        return CommonResult.success();
    }

    @ApiOperation("删除通知公告")
    @PostMapping("/delete/{noticeId}")
    @PreAuthorize("@ss.hasPermission('system:notice:remove')")
    @Log(title = "通知公告模块", businessType = BusinessType.DELETE)
    public CommonResult remove(@PathVariable Long noticeId){
        noticeService.remove(noticeId);
        return CommonResult.success();
    }

}
