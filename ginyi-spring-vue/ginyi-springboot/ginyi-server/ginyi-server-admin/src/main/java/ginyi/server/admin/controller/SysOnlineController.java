package ginyi.server.admin.controller;

import ginyi.common.annotation.Log;
import ginyi.common.enums.BusinessType;
import ginyi.common.result.CommonResult;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.SessionUserVo;
import ginyi.system.service.ISysOnlineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

@Api(tags = "在线用户")
@RestController
@RequestMapping("/api/online")
public class SysOnlineController {

    @Resource
    private ISysOnlineService onlineService;

    @ApiOperation("在线用户列表")
    @GetMapping("/getOnlineUserList")
    public CommonResult<BaseVo<SessionUserVo>> getOnlineUserList(@RequestParam(value = "page", required = false) Long page,
                                                                 @RequestParam(value = "pageSize", required = false) Long pageSize) {
        BaseVo<SessionUserVo> list = onlineService.getOnlineUserList(page, pageSize);
        return CommonResult.success(list);
    }

    @ApiOperation("强制用户退出")
    @PostMapping("/removeUser/{token}")
    @Log(title = "在线用户模块", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermission('monitor:online:forceLogout')")
    public CommonResult removeUser(@PathVariable("token") String sessionId) {
        onlineService.removeUser(sessionId);
        return CommonResult.success();
    }

    @ApiOperation("批量强制用户退出")
    @PostMapping("/removeUser")
    @Log(title = "在线用户模块", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermission('monitor:online:batchLogout')")
    public CommonResult removeUser(@RequestBody Set<String> ids) {
        onlineService.removeUser(ids);
        return CommonResult.success();
    }

}
