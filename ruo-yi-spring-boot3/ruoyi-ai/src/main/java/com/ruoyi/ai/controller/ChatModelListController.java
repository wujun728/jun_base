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
import com.ruoyi.ai.domain.ChatModelList;
import com.ruoyi.ai.service.IChatModelListService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 模型列表Controller
 * 
 * @author 欧阳日峰
 * @date 2025-02-06
 */
@RestController
@RequestMapping("/ai/modelList")
public class ChatModelListController extends BaseController
{
    @Autowired
    private IChatModelListService chatModelListService;

    /**
     * 查询模型列表列表
     */
    @PreAuthorize("@ss.hasPermi('ai:modelList:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatModelList chatModelList)
    {
        startPage();
        List<ChatModelList> list = chatModelListService.selectChatModelListList(chatModelList);
        return getDataTable(list);
    }

    /**
     * 导出模型列表列表
     */
    @PreAuthorize("@ss.hasPermi('ai:modelList:export')")
    @Log(title = "模型列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatModelList chatModelList)
    {
        List<ChatModelList> list = chatModelListService.selectChatModelListList(chatModelList);
        ExcelUtil<ChatModelList> util = new ExcelUtil<ChatModelList>(ChatModelList.class);
        util.exportExcel(response, list, "模型列表数据");
    }

    /**
     * 获取模型列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:modelList:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(chatModelListService.selectChatModelListById(id));
    }

    /**
     * 新增模型列表
     */
    @PreAuthorize("@ss.hasPermi('ai:modelList:add')")
    @Log(title = "模型列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatModelList chatModelList)
    {
        return toAjax(chatModelListService.insertChatModelList(chatModelList));
    }

    /**
     * 修改模型列表
     */
    @PreAuthorize("@ss.hasPermi('ai:modelList:edit')")
    @Log(title = "模型列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatModelList chatModelList)
    {
        return toAjax(chatModelListService.updateChatModelList(chatModelList));
    }

    /**
     * 删除模型列表
     */
    @PreAuthorize("@ss.hasPermi('ai:modelList:remove')")
    @Log(title = "模型列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(chatModelListService.deleteChatModelListByIds(ids));
    }
}
