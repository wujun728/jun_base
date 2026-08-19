package com.ruoyi.ai.controller;

import java.util.List;

import com.ruoyi.ai.utils.ChatSessionManager;
import com.ruoyi.common.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
import com.ruoyi.ai.domain.ChatSession;
import com.ruoyi.ai.service.IChatSessionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 聊天会话Controller
 *
 * @author ruoyi
 * @date 2025-03-04
 */
@RestController
@RequestMapping("/ai/session")
public class ChatSessionController extends BaseController
{
    @Autowired
    private IChatSessionService chatSessionService;

    @Autowired
    private ChatSessionManager chatSessionManager;
    /**
     * 查询聊天会话列表
     */
    @PreAuthorize("@ss.hasPermi('ai:session:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatSession chatSession)
    {
        startPage();
        List<ChatSession> list = chatSessionService.selectChatSessionList(chatSession);
        return getDataTable(list);
    }

    /**
     * 导出聊天会话列表
     */
    @PreAuthorize("@ss.hasPermi('ai:session:export')")
    @Log(title = "聊天会话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatSession chatSession)
    {
        List<ChatSession> list = chatSessionService.selectChatSessionList(chatSession);
        ExcelUtil<ChatSession> util = new ExcelUtil<ChatSession>(ChatSession.class);
        util.exportExcel(response, list, "聊天会话数据");
    }

    /**
     * 获取聊天会话详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:session:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(chatSessionService.selectChatSessionById(id));
    }

    /**
     * 新增聊天会话
     */
    @PreAuthorize("@ss.hasPermi('ai:session:add')")
    @Log(title = "聊天会话", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatSession chatSession)
    {
        int row = chatSessionService.insertChatSession(chatSession);
        chatSessionManager.createSession(chatSession);
        return toAjax(row);
    }

    /**
     * 修改聊天会话
     */
    @PreAuthorize("@ss.hasPermi('ai:session:edit')")
    @Log(title = "聊天会话", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatSession chatSession)
    {
        return toAjax(chatSessionService.updateChatSession(chatSession));
    }

    /**
     * 删除聊天会话
     */
    @PreAuthorize("@ss.hasPermi('ai:session:remove')")
    @Log(title = "聊天会话", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(chatSessionService.deleteChatSessionByIds(ids));
    }

    /**
     * 删除所有聊天会话
     */
    @PreAuthorize("@ss.hasPermi('ai:session:remove')")
    @Log(title = "聊天会话", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        ChatSession query = new ChatSession();
        query.setUserId(SecurityUtils.getUserId());
        List<ChatSession> list = chatSessionService.selectChatSessionList(query);
        if (!CollectionUtils.isEmpty(list)) {
            return toAjax(chatSessionService.deleteChatSessionByIds(list.stream().map(ChatSession::getId).toArray(Long[]::new)));
        } else {
            return toAjax(0);
        }
    }
}
