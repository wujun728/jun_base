package com.ruoyi.nocode.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.nocode.mapper.MyFormAttrMapper;
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
import com.ruoyi.nocode.domain.MyFormAttr;
import com.ruoyi.nocode.service.IMyFormAttrService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 表单属性Controller
 *
 * @date 2022-08-09
 */
@RestController
@RequestMapping("/nocode/attr")
public class FormAttrController extends BaseController {
    @Autowired
    private IMyFormAttrService myFormAttrService;
    @Autowired
    private MyFormAttrMapper myFormAttrMapper;

    /**
     * 查询表单属性列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MyFormAttr myFormAttr) {
        startPage();
        List<MyFormAttr> list = myFormAttrService.selectMyFormAttrList(myFormAttr);
        return getDataTable(list);
    }

    @GetMapping("/show/{formId}")
    public AjaxResult allFormId(@PathVariable("formId") String formId) {
        List<MyFormAttr> list = myFormAttrMapper.selectShowAttrListByFormId(formId);
        return AjaxResult.success(list);
    }

    /**
     * 导出表单属性列表
     */
    @Log(title = "表单属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MyFormAttr myFormAttr) {
        List<MyFormAttr> list = myFormAttrService.selectMyFormAttrList(myFormAttr);
        ExcelUtil<MyFormAttr> util = new ExcelUtil<MyFormAttr>(MyFormAttr.class);
        util.exportExcel(response, list, "表单属性数据");
    }

    /**
     * 获取表单属性详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(myFormAttrService.selectMyFormAttrById(id));
    }

    /**
     * 新增表单属性
     */
    @Log(title = "表单属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MyFormAttr myFormAttr) {
        return toAjax(myFormAttrService.insertMyFormAttr(myFormAttr));
    }

    /**
     * 修改表单属性
     */
    @Log(title = "表单属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MyFormAttr myFormAttr) {
        return toAjax(myFormAttrService.updateMyFormAttr(myFormAttr));
    }

    /**
     * 删除表单属性
     */
    @Log(title = "表单属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(myFormAttrService.deleteMyFormAttrByIds(ids));
    }
}
