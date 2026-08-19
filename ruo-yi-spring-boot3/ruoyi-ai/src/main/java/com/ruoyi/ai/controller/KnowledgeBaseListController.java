package com.ruoyi.ai.controller;

import java.util.List;

import com.ruoyi.ai.domain.KnowledgeBaseLine;
import com.ruoyi.ai.service.IKnowledgeBaseLineService;
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
import com.ruoyi.ai.domain.KnowledgeBaseList;
import com.ruoyi.ai.service.IKnowledgeBaseListService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 知识库列表Controller
 * 
 * @author 欧阳日峰
 * @date 2025-04-10
 */
@RestController
@RequestMapping("/ai/knowledgeBaseList")
public class KnowledgeBaseListController extends BaseController
{
    @Autowired
    private IKnowledgeBaseListService knowledgeBaseListService;

    @Autowired
    private IKnowledgeBaseLineService knowledgeBaseLineService;

    /**
     * 查询知识库列表列表
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseList:list')")
    @GetMapping("/list")
    public TableDataInfo list(KnowledgeBaseList knowledgeBaseList)
    {
        startPage();
        List<KnowledgeBaseList> list = knowledgeBaseListService.selectKnowledgeBaseListList(knowledgeBaseList);
        return getDataTable(list);
    }

    /**
     * 导出知识库列表列表
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseList:export')")
    @Log(title = "知识库列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KnowledgeBaseList knowledgeBaseList)
    {
        List<KnowledgeBaseList> list = knowledgeBaseListService.selectKnowledgeBaseListList(knowledgeBaseList);
        ExcelUtil<KnowledgeBaseList> util = new ExcelUtil<KnowledgeBaseList>(KnowledgeBaseList.class);
        util.exportExcel(response, list, "知识库列表数据");
    }

    /**
     * 获取知识库列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseList:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(knowledgeBaseListService.selectKnowledgeBaseListById(id));
    }

    /**
     * 获取知识库列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseList:query')")
    @GetMapping(value = "/getLineInfo/{id}")
    public TableDataInfo getLineInfo(@PathVariable("id") Long id)
    {
        startPage();
        List<KnowledgeBaseLine> list = knowledgeBaseLineService.getLineInfo(id);
        System.out.println(list);
        return getDataTable(list);
    }

    /**
     * 新增知识库列表
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseList:add')")
    @Log(title = "知识库列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KnowledgeBaseList knowledgeBaseList)
    {
        knowledgeBaseList.setCreateBy(getUsername());
        return toAjax(knowledgeBaseListService.insertKnowledgeBaseList(knowledgeBaseList));
    }

    /**
     * 修改知识库列表
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseList:edit')")
    @Log(title = "知识库列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KnowledgeBaseList knowledgeBaseList)
    {
        knowledgeBaseList.setUpdateBy(getUsername());
        return toAjax(knowledgeBaseListService.updateKnowledgeBaseList(knowledgeBaseList));
    }

    /**
     * 删除知识库列表
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledgeBaseList:remove')")
    @Log(title = "知识库列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(knowledgeBaseListService.deleteKnowledgeBaseListByIds(ids));
    }
}
