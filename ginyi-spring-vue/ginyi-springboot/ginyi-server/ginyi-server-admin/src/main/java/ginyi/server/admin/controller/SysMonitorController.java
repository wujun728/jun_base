package ginyi.server.admin.controller;

import ginyi.common.annotation.Log;
import ginyi.common.result.CommonResult;
import ginyi.system.domain.SysServer;
import ginyi.system.domain.model.dto.CacheDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.CacheKeyVo;
import ginyi.system.domain.model.vo.CacheVo;
import ginyi.system.service.ISysMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "监控模块")
@RestController
@RequestMapping("/api/monitor")
public class SysMonitorController {

    @Resource
    private ISysMonitorService monitorService;

    @ApiOperation("获取系统服务信息")
    @GetMapping("/getServerInfo")
    @PreAuthorize("@ss.hasPermission('monitor:server:list')")
    public CommonResult<SysServer> getServerInfo() throws InterruptedException {
        SysServer server = monitorService.getServerInfo();
        return CommonResult.success(server);
    }

    @ApiOperation("获取缓存列表")
    @GetMapping("/getCacheList")
    @PreAuthorize("@ss.hasPermission('monitor:cache:list')")
    public CommonResult<BaseVo<CacheKeyVo>> getCacheList(){
        BaseVo<CacheKeyVo> baseVo = monitorService.getCacheList();
        return CommonResult.success(baseVo);
    }

    @ApiOperation("获取缓存详情")
    @PostMapping("/getCacheValue")
    @PreAuthorize("@ss.hasPermission('monitor:cache:details')")
    public CommonResult<CacheVo> getCacheDetails(@RequestBody CacheDto cacheDto){
        CacheVo cacheVo = monitorService.getCacheDetails(cacheDto);
        return CommonResult.success(cacheVo);
    }

    @ApiOperation("删除缓存")
    @PostMapping("/removeCache/{key}")
    @Log(title = "缓存模块")
    @PreAuthorize("@ss.hasPermission('monitor:cache:remove')")
    public CommonResult removeCache(@PathVariable String key){
        monitorService.removeCache(key);
        return CommonResult.success();
    }

}
