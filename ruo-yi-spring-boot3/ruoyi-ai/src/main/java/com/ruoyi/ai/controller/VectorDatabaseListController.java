package com.ruoyi.ai.controller;

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
import com.ruoyi.ai.domain.VectorDatabaseList;
import com.ruoyi.ai.service.IVectorDatabaseListService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 向量库列表Controller
 * 
 * @author 欧阳日峰
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/ai/databaseList")
public class VectorDatabaseListController extends BaseController
{
    @Autowired
    private IVectorDatabaseListService vectorDatabaseListService;

    /**
     * 查询向量库列表列表
     */
    @PreAuthorize("@ss.hasPermi('ai:databaseList:list')")
    @GetMapping("/list")
    public TableDataInfo list(VectorDatabaseList vectorDatabaseList)
    {
        startPage();
        List<VectorDatabaseList> list = vectorDatabaseListService.selectVectorDatabaseListList(vectorDatabaseList);
        return getDataTable(list);
    }

    /**
     * 导出向量库列表列表
     */
    @PreAuthorize("@ss.hasPermi('ai:databaseList:export')")
    @Log(title = "向量库列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, VectorDatabaseList vectorDatabaseList)
    {
        List<VectorDatabaseList> list = vectorDatabaseListService.selectVectorDatabaseListList(vectorDatabaseList);
        ExcelUtil<VectorDatabaseList> util = new ExcelUtil<VectorDatabaseList>(VectorDatabaseList.class);
        util.exportExcel(response, list, "向量库列表数据");
    }

    /**
     * 获取向量库列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:databaseList:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(vectorDatabaseListService.selectVectorDatabaseListById(id));
    }

    /**
     * 新增向量库列表
     */
    @PreAuthorize("@ss.hasPermi('ai:databaseList:add')")
    @Log(title = "向量库列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VectorDatabaseList vectorDatabaseList)
    {
        return toAjax(vectorDatabaseListService.insertVectorDatabaseList(vectorDatabaseList));
    }

    /**
     * 修改向量库列表
     */
    @PreAuthorize("@ss.hasPermi('ai:databaseList:edit')")
    @Log(title = "向量库列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VectorDatabaseList vectorDatabaseList)
    {
        return toAjax(vectorDatabaseListService.updateVectorDatabaseList(vectorDatabaseList));
    }

    /**
     * 删除向量库列表
     */
    @PreAuthorize("@ss.hasPermi('ai:databaseList:remove')")
    @Log(title = "向量库列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(vectorDatabaseListService.deleteVectorDatabaseListByIds(ids));
    }
}
