package com.ruoyi.web.demo.treetable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.demo.treetable.entity.TreeTable;
import com.ruoyi.web.demo.treetable.service.ITreeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package test.treetable
 * @title: 树形结构表控制器
 * @description: 树形结构表控制器
 * @author: admin
 * @date: 2019-11-13 21:38:32
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("demo/treeTable")
public class TreeTableController extends BaseController {

    @Autowired
    private ITreeTableService treeTableService;

    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PreAuthorize("@ss.hasPermi('demo:treetable:list')")
    @GetMapping("/list")
    public AjaxResult list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<TreeTable> entityWrapper = new QueryWrapper<>();
        entityWrapper.orderByDesc( "create_time");
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            entityWrapper.like("name", name);
        }

        // 预处理
        List<TreeTable> treeNodeList = treeTableService.list(entityWrapper);
        return AjaxResult.success(treeNodeList);
    }


    /**
     * 获取树形表格详细信息
     */
    @PreAuthorize("@ss.hasPermi('demo:treeTable:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(treeTableService.getById(id));
    }

    /**
     * 新增树形表格
     */
    @PreAuthorize("@ss.hasPermi('demo:treeTable:add')")
    @Log(title = "树形表格", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TreeTable treeTable)
    {
        return toAjax(treeTableService.save(treeTable));
    }

    /**
     * 修改树形表格
     */
    @PreAuthorize("@ss.hasPermi('demo:treeTable:edit')")
    @Log(title = "树形表格", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TreeTable treeTable)
    {
        return toAjax(treeTableService.updateById(treeTable));
    }

    /**
     * 删除树形表格
     */
    @PreAuthorize("@ss.hasPermi('demo:treeTable:remove')")
    @Log(title = "树形表格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(treeTableService.removeByIds(Arrays.asList(ids)));
    }
}
