package com.ruoyi.web.demo.table.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.demo.table.entity.Table;
import com.ruoyi.web.demo.table.service.ITableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/demo/table")
public class TableController extends BaseController {
    @Autowired
    private ITableService tableService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('demo:table:list')")
    @GetMapping("/list")
    public TableDataInfo list(HttpServletRequest request) {
        //加入条件
        QueryWrapper<Table> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        String title = request.getParameter("title");
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.eq("title", title);
        }
        String level = request.getParameter("level");
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.eq("level", level);
        }
        String type = request.getParameter("type");
        if (!StringUtils.isEmpty(type)) {
            queryWrapper.eq("type", type);
        }

        Page<Table> page = tableService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(), queryWrapper);
        return getDataTable(page.getRecords());
    }

    /**
     * 导出综合表格列表
     */
    @PreAuthorize("@ss.hasPermi('demo:table:export')")
    @Log(title = "综合表格", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HttpServletRequest request) {
        //加入条件
        QueryWrapper<Table> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        String title = request.getParameter("title");
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.eq("title", title);
        }
        String level = request.getParameter("level");
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.eq("level", level);
        }
        String type = request.getParameter("type");
        if (!StringUtils.isEmpty(type)) {
            queryWrapper.eq("type", type);
        }

        List<Table> list = tableService.list(queryWrapper);
        ExcelUtil<Table> util = new ExcelUtil<>(Table.class);
        return util.exportExcel(list, "综合表格数据");
    }

    /**
     * 获取综合表格详细信息
     */
    @PreAuthorize("@ss.hasPermi('demo:table:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tableService.getById(id));
    }

    /**
     * 新增综合表格
     */
    @PreAuthorize("@ss.hasPermi('demo:table:add')")
    @Log(title = "综合表格", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Table table) {
        return toAjax(tableService.save(table));
    }

    /**
     * 修改综合表格
     */
    @PreAuthorize("@ss.hasPermi('demo:table:edit')")
    @Log(title = "综合表格", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Table table) {
        return toAjax(tableService.updateById(table));
    }

    /**
     * 删除综合表格
     */
    @PreAuthorize("@ss.hasPermi('demo:table:remove')")
    @Log(title = "综合表格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(tableService.removeByIds(Arrays.asList(infoIds)));
    }
}
