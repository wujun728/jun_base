package com.ruoyi.modelMessage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.modelMessage.domain.MessageSystem;
import com.ruoyi.modelMessage.service.IMessageSystemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 消息管理Controller
 * 
 * @author ruoyi
 * @date 2024-12-21
 */
@RestController
@RequestMapping("/modelMessage/messageSystem")
@Tag(name = "【消息管理】管理")
public class MessageSystemController extends BaseController
{
    @Autowired
    private IMessageSystemService messageSystemService;

    /**
     * 查询消息管理列表
     */
    @Operation(summary = "查询消息管理列表")
    //@PreAuthorize("@ss.hasPermi('modelMessage:messageSystem:list')")
    @GetMapping("/list")
    public TableDataInfo list(MessageSystem messageSystem)
    {
        startPage();
        messageSystem.setCreateBy(getUsername());
        messageSystem.setMessageRecipient(getUsername());
        List<MessageSystem> list = messageSystemService.selectMessageSystemList(messageSystem);
        return getDataTable(list);
    }

    /**
     * 导出消息管理列表
     */
    @Operation(summary = "导出消息管理列表")
    @PreAuthorize("@ss.hasPermi('modelMessage:messageSystem:export')")
    @Log(title = "消息管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MessageSystem messageSystem)
    {
        List<MessageSystem> list = messageSystemService.selectMessageSystemList(messageSystem);
        ExcelUtil<MessageSystem> util = new ExcelUtil<MessageSystem>(MessageSystem.class);
        util.exportExcel(response, list, "消息管理数据");
    }

    /**
     * 获取消息管理详细信息
     */
    @Operation(summary = "获取消息管理详细信息")
    //@PreAuthorize("@ss.hasPermi('modelMessage:messageSystem:query')")
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable("messageId") Long messageId)
    {
        return success(messageSystemService.selectMessageSystemByMessageId(messageId));
    }

    /**
     * 修改消息管理
     */
    @Operation(summary = "修改消息管理")
    //@PreAuthorize("@ss.hasPermi('modelMessage:messageSystem:edit')")
    @Log(title = "消息管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MessageSystem messageSystem)
    {
        messageSystem.setUpdateBy(getUsername());
        messageSystem.setUpdateTime(DateUtils.getNowDate());
        return toAjax(messageSystemService.updateMessageSystem(messageSystem));
    }

    /**
     * 删除消息管理
     */
    @Operation(summary = "删除消息管理")
    //@PreAuthorize("@ss.hasPermi('modelMessage:messageSystem:remove')")
    @Log(title = "消息管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable( name = "messageIds" ) Long[] messageIds) 
    {
        return toAjax(messageSystemService.deleteMessageSystemByMessageIds(messageIds));
    }

    /**
     * 批量发送消息
     */
    @Operation(summary = "批量发送消息")
    @Log(title = "批量发送消息", businessType = BusinessType.INSERT)
    @PostMapping
    @Transactional
    public AjaxResult batchAdd(@RequestBody List<MessageSystem> messageSystemList) {
        try {
            messageSystemList.forEach(messageSystemService::processMessageSystem);
            messageSystemService.batchInsertMessageSystem(messageSystemList);
            return AjaxResult.success("消息发送成功!");
        } catch (Exception e) {
            return AjaxResult.error("消息发送失败", e);
        }
    }

    /**
     * 点击信息详情状态调整为已读
     */
    @Operation(summary = "点击信息详情状态调整为已读")
    @PostMapping("/{messageId}")
    public AjaxResult update(@PathVariable Long messageId){
        return success(messageSystemService.updateState(messageId));
    }

    /**
     * 统一查询系统资源信息（角色、部门、用户）
     *
     * @param type     查询类型：role-角色信息, dept-部门信息, user-用户信息, usersbyrole-根据角色查用户, usersbydept-根据部门查用户, usersbysendmode-根据发送方式查用户
     * @param id       可选参数，当查询特定角色或部门下的用户时使用
     * @param sendMode 可选参数，当查询特定发送方式的用户时使用（phone/email）
     * @return 查询结果
     */
    @Operation(summary = "统一查询系统资源信息")
    @GetMapping("/systemResource")
    public AjaxResult getSystemResource(@RequestParam String type, @RequestParam(required = false) Long id,
            @RequestParam(required = false) String sendMode) {
        try {
            Object result = messageSystemService.querySystemResource(type, id, sendMode);
            return AjaxResult.success(result);
        } catch (ServiceException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
