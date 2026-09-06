package ginyi.server.admin.controller;

import ginyi.common.result.CommonResult;
import ginyi.system.domain.SysLogLogin;
import ginyi.system.domain.SysLogOperation;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "日志模块")
@RestController
@RequestMapping("/api/log")
public class SysLogController {

    @Resource
    private ISysLogService logService;

    @ApiOperation("登录日志")
    @GetMapping("/getLoginLogList")
    @PreAuthorize("@ss.hasPermission('monitor:operlog:list')")
    public CommonResult<BaseVo<SysLogLogin>> loginLogList(@RequestParam(value = "page", required = false) Long page,
                                                          @RequestParam(value = "pageSize", required = false) Long pageSize) {
        BaseVo<SysLogLogin> list = logService.getLoginLogList(page, pageSize);
        return CommonResult.success(list);
    }

    @ApiOperation("操作日志")
    @GetMapping("/getOperationLogList")
    @PreAuthorize("@ss.hasPermission('monitor:loginlog:list')")
    public CommonResult<BaseVo<SysLogOperation>> operationLogList(@RequestParam(value = "page", required = false) Long page,
                                                                  @RequestParam(value = "pageSize", required = false) Long pageSize) {
        BaseVo<SysLogOperation> list = logService.getOperationLogList(page, pageSize);
        return CommonResult.success(list);
    }
}
