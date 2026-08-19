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
import com.ruoyi.ai.domain.ChatSessionDetail;
import com.ruoyi.ai.service.IChatSessionDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 聊天会话详情Controller
 *
 * @author ruoyi
 * @date 2025-03-04
 */
@RestController
@RequestMapping("/ai/detail")
public class ChatSessionDetailController extends BaseController
{
    @Autowired
    private IChatSessionDetailService chatSessionDetailService;

    /**
     * 查询聊天会话详情列表
     */
    @PreAuthorize("@ss.hasPermi('ai:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatSessionDetail chatSessionDetail)
    {
        List<ChatSessionDetail> list = chatSessionDetailService.selectChatSessionDetailList(chatSessionDetail);
        return getDataTable(list);
    }

    /**
     * 导出聊天会话详情列表
     */
    @PreAuthorize("@ss.hasPermi('ai:detail:export')")
    @Log(title = "聊天会话详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatSessionDetail chatSessionDetail)
    {
        List<ChatSessionDetail> list = chatSessionDetailService.selectChatSessionDetailList(chatSessionDetail);
        ExcelUtil<ChatSessionDetail> util = new ExcelUtil<ChatSessionDetail>(ChatSessionDetail.class);
        util.exportExcel(response, list, "聊天会话详情数据");
    }

    /**
     * 获取聊天会话详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:detail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(chatSessionDetailService.selectChatSessionDetailById(id));
    }

    /**
     * 新增聊天会话详情
     */
    @PreAuthorize("@ss.hasPermi('ai:detail:add')")
    @Log(title = "聊天会话详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatSessionDetail chatSessionDetail)
    {
        return toAjax(chatSessionDetailService.insertChatSessionDetail(chatSessionDetail));
    }

    /**
     * 修改聊天会话详情
     */
    @PreAuthorize("@ss.hasPermi('ai:detail:edit')")
    @Log(title = "聊天会话详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatSessionDetail chatSessionDetail)
    {
        return toAjax(chatSessionDetailService.updateChatSessionDetail(chatSessionDetail));
    }

    /**
     * 删除聊天会话详情
     */
    @PreAuthorize("@ss.hasPermi('ai:detail:remove')")
    @Log(title = "聊天会话详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(chatSessionDetailService.deleteChatSessionDetailByIds(ids));
    }
}
