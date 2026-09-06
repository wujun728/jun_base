package com.ruoyi.business.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.business.domain.SqTransportTeam;
import com.ruoyi.business.service.ISqTransportTeamService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "旧系统SQ运输管理")
@RestController
@RequestMapping("/business/sq/transport-team")
public class SqTransportTeamController extends BaseController
{
    @Autowired
    private ISqTransportTeamService sqTransportTeamService;

    @PreAuthorize("@ss.hasPermi('business:transportTeam:list')")
    @Operation(summary = "查询运输管理列表")
    @GetMapping("/list")
    public TableDataInfo list(SqTransportTeam team)
    {
        startPage();
        return getDataTable(sqTransportTeamService.selectSqTransportTeamList(team));
    }

    @PreAuthorize("@ss.hasAnyPermi('business:transportTeam:list,business:transportCar:list')")
    @Operation(summary = "查询运输管理选项")
    @GetMapping("/options")
    public AjaxResult options(SqTransportTeam team)
    {
        return success(sqTransportTeamService.selectSqTransportTeamOptions(team));
    }

    @Log(title = "运输管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:transportTeam:export')")
    @Operation(summary = "导出运输管理")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqTransportTeam team)
    {
        List<SqTransportTeam> list = sqTransportTeamService.selectSqTransportTeamList(team);
        new ExcelUtil<SqTransportTeam>(SqTransportTeam.class).exportExcel(response, list, "运输管理");
    }

    @PreAuthorize("@ss.hasPermi('business:transportTeam:query')")
    @Operation(summary = "获取运输管理")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqTransportTeamService.selectSqTransportTeamById(id));
    }

    @Log(title = "运输管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:transportTeam:add')")
    @Operation(summary = "新增运输管理")
    @PostMapping
    public AjaxResult add(@RequestBody SqTransportTeam team)
    {
        team.setCreateUser(getUsername());
        return toAjax(sqTransportTeamService.insertSqTransportTeam(team));
    }

    @Log(title = "运输管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:transportTeam:edit')")
    @Operation(summary = "修改运输管理")
    @PutMapping
    public AjaxResult edit(@RequestBody SqTransportTeam team)
    {
        team.setUpdateUser(getUsername());
        return toAjax(sqTransportTeamService.updateSqTransportTeam(team));
    }

    @Log(title = "运输管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:transportTeam:remove')")
    @Operation(summary = "删除运输管理")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqTransportTeamService.deleteSqTransportTeamByIds(ids));
    }
}
