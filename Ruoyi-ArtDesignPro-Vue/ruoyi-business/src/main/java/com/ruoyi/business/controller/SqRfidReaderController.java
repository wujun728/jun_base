package com.ruoyi.business.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.business.domain.SqRfidReader;
import com.ruoyi.business.service.ISqRfidReaderService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/business/sq/rfid-reader")
public class SqRfidReaderController extends BaseController
{
    @Autowired
    private ISqRfidReaderService sqRfidReaderService;

    @PreAuthorize("@ss.hasPermi('business:rfidReader:list')")
    @GetMapping("/list")
    public TableDataInfo list(SqRfidReader reader)
    {
        startPage();
        return getDataTable(sqRfidReaderService.selectSqRfidReaderList(reader));
    }

    @Log(title = "RFID阅读器管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:rfidReader:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SqRfidReader reader)
    {
        List<SqRfidReader> list = sqRfidReaderService.selectSqRfidReaderList(reader);
        new ExcelUtil<SqRfidReader>(SqRfidReader.class).exportExcel(response, list, "RFID阅读器管理");
    }

    @PreAuthorize("@ss.hasPermi('business:rfidReader:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(sqRfidReaderService.selectSqRfidReaderById(id));
    }

    @Log(title = "RFID阅读器管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('business:rfidReader:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SqRfidReader reader)
    {
        return toAjax(sqRfidReaderService.insertSqRfidReader(reader));
    }

    @Log(title = "RFID阅读器管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('business:rfidReader:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SqRfidReader reader)
    {
        return toAjax(sqRfidReaderService.updateSqRfidReader(reader));
    }

    @Log(title = "RFID阅读器管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('business:rfidReader:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sqRfidReaderService.deleteSqRfidReaderByIds(ids));
    }
}
