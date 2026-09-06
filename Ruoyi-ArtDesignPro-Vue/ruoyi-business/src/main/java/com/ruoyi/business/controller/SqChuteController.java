package com.ruoyi.business.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.business.domain.SqChute;
import com.ruoyi.business.service.ISqChuteService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "旧系统SQ溜井管理")
@RestController
@RequestMapping("/business/sq/chute")
public class SqChuteController extends BaseController
{
    @Autowired
    private ISqChuteService sqChuteService;

    @PreAuthorize("@ss.hasPermi('business:chute:list')")
    @Operation(summary = "查询溜井基本信息列表")
    @GetMapping("/list")
    public TableDataInfo list(SqChute chute)
    {
        startPage();
        List<SqChute> list = sqChuteService.selectSqChuteList(chute);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('business:chuteHeight:list')")
    @Operation(summary = "查询溜井高度校验列表")
    @GetMapping("/height/list")
    public AjaxResult heightList(SqChute chute)
    {
        return success(sqChuteService.selectSqChuteHeightList(chute));
    }

    @PreAuthorize("@ss.hasAnyPermi('business:chute:list,business:chuteHeight:list,business:feedLog:list,business:outLog:list,business:levelLog:list,business:offlineTask:list,business:feedOfflineLog:list')")
    @Operation(summary = "查询溜井选项")
    @GetMapping("/options")
    public AjaxResult options(SqChute chute)
    {
        return success(sqChuteService.selectSqChuteOptions(chute));
    }

    @Log(title = "溜井基本信息", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:chute:export')")
    @Operation(summary = "导出溜井基本信息")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqChute chute)
    {
        List<SqChute> list = sqChuteService.selectSqChuteList(chute);
        ExcelUtil<SqChute> util = new ExcelUtil<SqChute>(SqChute.class);
        util.exportExcel(response, list, "溜井基本信息");
    }

    @PreAuthorize("@ss.hasPermi('business:chute:query')")
    @Operation(summary = "获取溜井基本信息")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqChuteService.selectSqChuteById(id));
    }

    @Log(title = "溜井基本信息", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:chute:add')")
    @Operation(summary = "新增溜井基本信息")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SqChute chute)
    {
        chute.setCreateUser(getUsername());
        return toAjax(sqChuteService.insertSqChute(chute));
    }

    @Log(title = "溜井基本信息", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:chute:edit')")
    @Operation(summary = "修改溜井基本信息")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SqChute chute)
    {
        chute.setUpdateUser(getUsername());
        return toAjax(sqChuteService.updateSqChute(chute));
    }

    @Log(title = "溜井高度校验", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:chuteHeight:edit')")
    @Operation(summary = "校验溜井高度")
    @PutMapping("/height")
    public AjaxResult editHeight(@RequestBody SqChute chute)
    {
        chute.setUpdateUser(getUsername());
        return toAjax(sqChuteService.updateSqChuteHeight(chute));
    }

    @Log(title = "溜井基本信息", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:chute:remove')")
    @Operation(summary = "删除溜井基本信息")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqChuteService.deleteSqChuteByIds(ids));
    }
}
