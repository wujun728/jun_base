package ginyi.server.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import ginyi.common.annotation.Log;
import ginyi.common.enums.BusinessType;
import ginyi.common.result.CommonResult;
import ginyi.common.swagger.AddGroup;
import ginyi.common.swagger.UpdateGroup;
import ginyi.system.domain.SysMenu;
import ginyi.system.domain.model.dto.MenuDto;
import ginyi.system.domain.model.dto.UserDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

@Api(tags = "菜单模块")
@RestController
@RequestMapping("/api/menu")
public class SysMenuController {

    @Resource
    private ISysMenuService menuService;

    @ApiOperation("删除菜单")
    @PostMapping("/delete/{menuId}")
    @PreAuthorize("@ss.hasPermission('system:menu:remove')")
    @Log(title = "菜单模块", businessType = BusinessType.DELETE)
    public CommonResult delete(@PathVariable("menuId") Long menuId) {
        menuService.removeMenuById(menuId);
        return CommonResult.success();
    }

    @ApiOperation("批量删除菜单")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:menu:remove')")
    @Log(title = "菜单模块", businessType = BusinessType.DELETE)
    public CommonResult delete(@RequestBody Set<Long> ids) {
        menuService.removeMenuByIds(ids);
        return CommonResult.success();
    }

    @ApiOperation("菜单详情")
    @GetMapping("/getMenuById/{menuId}")
    @PreAuthorize("@ss.hasPermission('system:menu:edit')")
    public CommonResult<SysMenu> getMenuById(@PathVariable("menuId") Long menuId) {
        SysMenu menu = menuService.getMenuById(menuId);
        return CommonResult.success(menu);
    }

    @ApiOperation("路由菜单列表")
    @GetMapping("/getRouterList")
    public CommonResult<BaseVo<SysMenu>> list() {
        BaseVo<SysMenu> baseVo = menuService.selectMenuList();
        return CommonResult.success(baseVo);
    }

    @ApiOperation("菜单列表")
    @PostMapping("/list")
    @PreAuthorize("@ss.hasPermission('system:menu:list')")
    @ApiOperationSupport(ignoreParameters = {
            "menuDto.updateBy",
            "menuDto.updateTime",
            "menuDto.createTime",
            "menuDto.params",
    })
    public CommonResult<BaseVo<SysMenu>> list(@RequestBody @Validated MenuDto menuDto) {
        BaseVo<SysMenu> baseVo = menuService.list(menuDto);
        return CommonResult.success(baseVo);
    }

    @ApiOperation("新增菜单")
    @PostMapping("/add")
    @Log(title = "菜单模块", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermission('system:menu:add')")
    @ApiOperationSupport(ignoreParameters = {
            "menuDto.updateBy",
            "menuDto.updateTime",
            "menuDto.createTime",
            "menuDto.createBy",
            "menuDto.params",
            "menuDto.menuId",
    })
    public CommonResult add(@RequestBody @Validated({AddGroup.class}) MenuDto menuDto) {
        menuService.addMenu(menuDto);
        return CommonResult.success();
    }


    @ApiOperation("更新菜单")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermission('system:menu:edit')")
    @Log(title = "菜单模块", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(ignoreParameters = {
            "menuDto.updateBy",
            "menuDto.updateTime",
            "menuDto.createTime",
            "menuDto.createBy",
            "menuDto.params",
    })
    public CommonResult update(@RequestBody @Validated({UpdateGroup.class}) MenuDto menuDto) {
        menuService.updateMenu(menuDto);
        return CommonResult.success();
    }

    @ApiOperation("更新菜单状态")
    @PostMapping("/updateStatus")
    @PreAuthorize("@ss.hasPermission('system:menu:edit')")
    @Log(title = "菜单模块", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(includeParameters = {
            "menuDto.menuId",
            "menuDto.status"
    })
    public CommonResult updateStatus(@RequestBody MenuDto menuDto){
        menuService.updateStatus(menuDto);
        return CommonResult.success();
    }
}
