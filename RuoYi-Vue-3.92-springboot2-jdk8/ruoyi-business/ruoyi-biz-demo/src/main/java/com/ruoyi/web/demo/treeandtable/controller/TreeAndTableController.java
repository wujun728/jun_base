package com.ruoyi.web.demo.treeandtable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.demo.treeandtable.entity.TreeAndTable;
import com.ruoyi.web.demo.treeandtable.service.ITreeAndTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package test.treeandtable
 * @title: 左树右表控制器
 * @description: 左树右表控制器
 * @author: admin
 * @date: 2019-11-13 21:24:49
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/demo/treeandtable")
public class TreeAndTableController extends BaseController {

    @Autowired
    private ITreeAndTableService treeAndTableService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PreAuthorize("@ss.hasPermi('demo:treeandtable:list')")
    @GetMapping("/list")
    public TableDataInfo list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<TreeAndTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        String type = request.getParameter("type");
        if (!StringUtils.isEmpty(type)) {
            queryWrapper.eq("type", type);
        }
        String areaId = request.getParameter("areaId");
        if (!StringUtils.isEmpty(areaId)) {
            queryWrapper.eq("area_id", areaId);
        }
        // 预处理
        Page pageBean = treeAndTableService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(), queryWrapper);
        return getDataTable(pageBean.getRecords());
    }

    @PreAuthorize("@ss.hasPermi('demo:treeandtable:export')")
    @Log(title = "左树右表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<TreeAndTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        String type = request.getParameter("type");
        if (!StringUtils.isEmpty(type)) {
            queryWrapper.eq("type", type);
        }
        String areaId = request.getParameter("areaId");
        if (!StringUtils.isEmpty(areaId)) {
            queryWrapper.apply("area_id", areaId);
        }
        List<TreeAndTable> list = treeAndTableService.list(queryWrapper);
        ExcelUtil<TreeAndTable> util = new ExcelUtil<>(TreeAndTable.class);
        return util.exportExcel(list, "左树右表数据");
    }

    @PreAuthorize("@ss.hasPermi('demo:treeandtable:add')")
    @Log(title = "左树右表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TreeAndTable entity) {
        return toAjax(treeAndTableService.save(entity));
    }

    @PreAuthorize("@ss.hasPermi('demo:treeandtable:edit')")
    @Log(title = "左树右表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody TreeAndTable entity) {
        return toAjax(treeAndTableService.updateById(entity));
    }

    /**
     * 获取左树右表详细信息
     */
    @PreAuthorize("@ss.hasPermi('demo:treeandtable:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(treeAndTableService.getById(id));
    }

    @PreAuthorize("@ss.hasPermi('demo:treeandtable:remove')")
    @Log(title = "左树右表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult batchDelete(@PathVariable String[] infoIds) {
        return toAjax(treeAndTableService.removeByIds(Arrays.asList(infoIds)));
    }
}
