package com.ruoyi.web.demo.expandtable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.demo.expandtable.entity.ExpandTable;
import com.ruoyi.web.demo.expandtable.service.IExpandTableService;
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
@RequestMapping("/demo/expandTable")
public class ExpandTableController extends BaseController {
    @Autowired
    private IExpandTableService expandTableService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('demo:expandTable:list')")
    @GetMapping("/list")
    public TableDataInfo list(HttpServletRequest request) {
        //加入条件
        QueryWrapper<ExpandTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        String title = request.getParameter("name");
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }

        Page<ExpandTable> page = expandTableService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(), queryWrapper);
        return getDataTable(page.getRecords());
    }

    /**
     * 导出展开表格列表
     */
    @PreAuthorize("@ss.hasPermi('demo:expandTable:export')")
    @Log(title = "展开表格", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HttpServletRequest request) {
        //加入条件
        QueryWrapper<ExpandTable> queryWrapper = new QueryWrapper<>();
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

        List<ExpandTable> list = expandTableService.list(queryWrapper);
        ExcelUtil<ExpandTable> util = new ExcelUtil<>(ExpandTable.class);
        return util.exportExcel(list, "展开表格数据");
    }

    /**
     * 获取展开表格详细信息
     */
    @PreAuthorize("@ss.hasPermi('demo:expandTable:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(expandTableService.getById(id));
    }

    /**
     * 新增展开表格
     */
    @PreAuthorize("@ss.hasPermi('demo:expandTable:add')")
    @Log(title = "展开表格", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpandTable expandTable) {
        return toAjax(expandTableService.save(expandTable));
    }

    /**
     * 修改展开表格
     */
    @PreAuthorize("@ss.hasPermi('demo:expandTable:edit')")
    @Log(title = "展开表格", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpandTable expandTable) {
        return toAjax(expandTableService.updateById(expandTable));
    }

    /**
     * 删除展开表格
     */
    @PreAuthorize("@ss.hasPermi('demo:expandTable:remove')")
    @Log(title = "展开表格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(expandTableService.removeByIds(Arrays.asList(infoIds)));
    }
}
