package com.ruoyi.logic.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.logic.domain.LogicLogic;
import com.ruoyi.logic.service.ILogicLogicService;
import com.ruoyi.common.utils.poi.ExcelMergeExportUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 逻辑演示Controller
 *
 * @author ruoyi
 * @date 2026-01-26
 */
@RestController
@RequestMapping("/logic/LogicLogic")
public class LogicLogicController extends BaseController {
    @Autowired
    private ILogicLogicService logicLogicService;

    /**
     * 查询逻辑演示列表
     */
    @PreAuthorize("@ss.hasPermi('logic:LogicLogic:list')")
    @GetMapping("/list")
    public TableDataInfo list(LogicLogic logicLogic) {
        startPage();
        List<LogicLogic> list = logicLogicService.selectLogicLogicList(logicLogic);
        return getDataTable(list);
    }

    /**
     * 查询逻辑演示列表不分页
     */
    @PreAuthorize("@ss.hasPermi('logic:LogicLogic:list')")
    @GetMapping("/listAll")
    public TableDataInfo listAll(LogicLogic logicLogic) {
        List<LogicLogic> list = logicLogicService.selectLogicLogicList(logicLogic);
        return getDataTable(list);
    }

    /**
     * 导出逻辑演示列表
     */
    @PreAuthorize("@ss.hasPermi('logic:LogicLogic:export')")
    @Log(title = "逻辑演示", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LogicLogic logicLogic) {
        List<LogicLogic> list = logicLogicService.selectLogicLogicList(logicLogic);
        ExcelMergeExportUtil.exportWithMerge(response, list, "逻辑演示数据");
    }

    /**
     * 获取逻辑演示详细信息
     */
    @PreAuthorize("@ss.hasPermi('logic:LogicLogic:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(logicLogicService.selectLogicLogicById(id));
    }

    /**
     * 新增逻辑演示
     */
    @PreAuthorize("@ss.hasPermi('logic:LogicLogic:add')")
    @Log(title = "逻辑演示", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LogicLogic logicLogic) {
        logicLogic.setCreateBy(getUsername());
        logicLogic.setUserId(getUserId());
        logicLogic.setDeptId(getDeptId());
        return toAjax(logicLogicService.insertLogicLogic(logicLogic));
    }

    /**
     * 修改逻辑演示
     */
    @PreAuthorize("@ss.hasPermi('logic:LogicLogic:edit')")
    @Log(title = "逻辑演示", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LogicLogic logicLogic) {
        logicLogic.setUpdateBy(getUsername());
        return toAjax(logicLogicService.updateLogicLogic(logicLogic));
    }

    /**
     * 删除逻辑演示
     */
    @PreAuthorize("@ss.hasPermi('logic:LogicLogic:remove')")
    @Log(title = "逻辑演示", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(logicLogicService.deleteLogicLogicByIds(ids));
    }
}
