package com.ruoyi.online.controller;

import java.util.List;

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

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.online.domain.OnlineMb;
import com.ruoyi.online.service.IOnlineMbService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

/**
 * mybatis在线接口Controller
 * 
 * @author Dftre
 * @date 2024-01-26
 */
@RestController
@RequestMapping("/online/mb")
@Tag(name = "【mybatis在线接口】管理")
public class OnlineMbController extends BaseController
{
    @Autowired
    private IOnlineMbService onlineMbService;

    /**
     * 查询mybatis在线接口列表
     */
    @Operation(summary = "查询mybatis在线接口列表")
    @PreAuthorize("@ss.hasPermi('online:mb:list')")
    @GetMapping("/list")
    public TableDataInfo list(OnlineMb onlineMb)
    {
        startPage();
        List<OnlineMb> list = onlineMbService.selectOnlineMbList(onlineMb);
        return getDataTable(list);
    }

    /**
     * 导出mybatis在线接口列表
     */
    @Operation(summary = "导出mybatis在线接口列表")
    @PreAuthorize("@ss.hasPermi('online:mb:export')")
    @Log(title = "mybatis在线接口", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OnlineMb onlineMb)
    {
        List<OnlineMb> list = onlineMbService.selectOnlineMbList(onlineMb);
        ExcelUtil<OnlineMb> util = new ExcelUtil<OnlineMb>(OnlineMb.class);
        util.exportExcel(response, list, "mybatis在线接口数据");
    }

    /**
     * 获取mybatis在线接口详细信息
     */
    @Operation(summary = "获取mybatis在线接口详细信息")
    @PreAuthorize("@ss.hasPermi('online:mb:query')")
    @GetMapping(value = "/{mbId}")
    public AjaxResult getInfo(@PathVariable("mbId") Long mbId)
    {
        return success(onlineMbService.selectOnlineMbByMbId(mbId));
    }

    /**
     * 新增mybatis在线接口
     */
    @Operation(summary = "新增mybatis在线接口")
    @PreAuthorize("@ss.hasPermi('online:mb:add')")
    @Log(title = "mybatis在线接口", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OnlineMb onlineMb)
    {
        return toAjax(onlineMbService.insertOnlineMb(onlineMb));
    }

    /**
     * 修改mybatis在线接口
     */
    @Operation(summary = "修改mybatis在线接口")
    @PreAuthorize("@ss.hasPermi('online:mb:edit')")
    @Log(title = "mybatis在线接口", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OnlineMb onlineMb)
    {
        return toAjax(onlineMbService.updateOnlineMb(onlineMb));
    }

    /**
     * 删除mybatis在线接口
     */
    @Operation(summary = "删除mybatis在线接口")
    @PreAuthorize("@ss.hasPermi('online:mb:remove')")
    @Log(title = "mybatis在线接口", businessType = BusinessType.DELETE)
	@DeleteMapping("/{mbIds}")
    public AjaxResult remove(@PathVariable( name = "mbIds" ) Long[] mbIds) 
    {
        return toAjax(onlineMbService.deleteOnlineMbByMbIds(mbIds));
    }
}
