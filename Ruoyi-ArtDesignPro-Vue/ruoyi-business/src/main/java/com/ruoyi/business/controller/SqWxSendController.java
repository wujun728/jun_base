package com.ruoyi.business.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.business.domain.SqWxSend;
import com.ruoyi.business.service.ISqWxSendService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/wx-send")
public class SqWxSendController extends BaseController
{
    @Autowired
    private ISqWxSendService sqWxSendService;

    @PreAuthorize("@ss.hasPermi('business:wxSend:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqWxSend send)
    {
        startPage();
        return getDataTable(sqWxSendService.selectSqWxSendList(send));
    }

    @Log(title = "企业微信推送", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:wxSend:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqWxSend send)
    {
        List<SqWxSend> list = sqWxSendService.selectSqWxSendList(send);
        new ExcelUtil<SqWxSend>(SqWxSend.class).exportExcel(response, list, "企业微信推送");
    }

    @PreAuthorize("@ss.hasPermi('business:wxSend:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqWxSendService.selectSqWxSendById(id));
    }

    @Log(title = "企业微信推送", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:wxSend:add')")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SqWxSend send)
    {
        send.setCreateUser(getUsername());
        return toAjax(sqWxSendService.insertSqWxSend(send));
    }

    @Log(title = "企业微信推送", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:wxSend:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SqWxSend send)
    {
        send.setUpdateUser(getUsername());
        return toAjax(sqWxSendService.updateSqWxSend(send));
    }

    @Log(title = "企业微信推送", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:wxSend:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqWxSendService.deleteSqWxSendByIds(ids));
    }
}
