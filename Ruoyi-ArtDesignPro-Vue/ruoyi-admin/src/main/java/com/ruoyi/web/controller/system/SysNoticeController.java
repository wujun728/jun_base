package com.ruoyi.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeReadService;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.web.service.SysNoticeBroadcastService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 公告 信息操作处理
 * 
 * @author ruoyi
 */
@Tag(name = "通知公告")
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private ISysNoticeReadService noticeReadService;

    @Autowired
    private SysNoticeBroadcastService noticeBroadcastService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @Operation(summary = "查询通知公告列表")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @Operation(summary = "获取通知公告详情")
    @GetMapping(value = "/{noticeId}")
    @io.swagger.v3.oas.annotations.Parameter(description = "通知公告ID")
    public AjaxResult getInfo(@PathVariable Long noticeId)
    {
        return success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @Operation(summary = "新增通知公告")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice)
    {
        notice.setCreateBy(getUsername());
        int rows = noticeService.insertNotice(notice);
        if (rows > 0)
        {
            noticeBroadcastService.created(notice);
        }
        return toAjax(rows);
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @Operation(summary = "修改通知公告")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(getUsername());
        int rows = noticeService.updateNotice(notice);
        if (rows > 0)
        {
            noticeBroadcastService.updated(notice);
        }
        return toAjax(rows);
    }

    /**
     * 首页顶部公告列表（返回全部正常公告，带当前用户已读标记，最多5条）
     */
    @Operation(summary = "查询置顶通知公告")
    @GetMapping("/listTop")
    @ResponseBody
    public AjaxResult listTop(String noticeType)
    {
        Long userId = getUserId();
        List<SysNotice> list = noticeReadService.selectNoticeListWithReadStatus(userId, noticeType, 5);
        int unreadCount = noticeReadService.selectUnreadCount(userId, noticeType);
        AjaxResult result = AjaxResult.success(list);
        result.put("unreadCount", unreadCount);
        return result;
    }

    /**
     * 标记公告已读
     */
    @Operation(summary = "标记通知已读")
    @PostMapping("/markRead")
    @ResponseBody
    public AjaxResult markRead(Long noticeId)
    {
        Long userId = getUserId();
        noticeReadService.markRead(noticeId, userId);
        return success();
    }

    /**
     * 批量标记已读
     */
    @Operation(summary = "全部标记为已读")
    @PostMapping("/markReadAll")
    @ResponseBody
    public AjaxResult markReadAll(String ids)
    {
        Long userId = getUserId();
        if (ids == null || ids.isEmpty())
        {
            noticeReadService.markReadAll(userId);
        }
        else
        {
            Long[] noticeIds = Convert.toLongArray(ids);
            noticeReadService.markReadBatch(userId, noticeIds);
        }
        return success();
    }

    /**
     * 已读用户列表数据
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @Operation(summary = "查询通知已读用户列表")
    @GetMapping("/readUsers/list")
    @ResponseBody
    public TableDataInfo readUsersList(Long noticeId, String searchValue)
    {
        startPage();
        List<?> list = noticeReadService.selectReadUsersByNoticeId(noticeId, searchValue);
        return getDataTable(list);
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @Operation(summary = "删除通知公告")
    @DeleteMapping("/{noticeIds}")
    @io.swagger.v3.oas.annotations.Parameter(description = "通知公告ID数组")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        noticeReadService.deleteByNoticeIds(noticeIds);
        int rows = noticeService.deleteNoticeByIds(noticeIds);
        if (rows > 0)
        {
            noticeBroadcastService.deleted(noticeIds);
        }
        return toAjax(rows);
    }
}
