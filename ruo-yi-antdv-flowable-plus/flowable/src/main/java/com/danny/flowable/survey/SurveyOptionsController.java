package com.danny.flowable.survey;

import java.util.List;

import com.danny.common.core.controller.BaseController;
import com.danny.common.core.domain.AjaxResult;
import com.danny.common.core.page.TableDataInfo;
import com.danny.common.enums.BusinessType;
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


/**
 * 题目选项Controller
 * 
 * @author 2y
 * @date 2021-03-21
 */
@RestController
@RequestMapping("/survey/options")
public class SurveyOptionsController extends BaseController
{
//    @Autowired
//    private ISurveyOptionsService surveyOptionsService;
//
//    /**
//     * 查询题目选项列表
//     */
//    @PreAuthorize("@ss.hasPermi('survey:options:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(SurveyOptions surveyOptions)
//    {
//        startPage();
//        List<SurveyOptions> list = surveyOptionsService.selectSurveyOptionsList(surveyOptions);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出题目选项列表
//     */
//    @PreAuthorize("@ss.hasPermi('survey:options:export')")
//    @Log(title = "题目选项", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(SurveyOptions surveyOptions)
//    {
//        List<SurveyOptions> list = surveyOptionsService.selectSurveyOptionsList(surveyOptions);
//        ExcelUtil<SurveyOptions> util = new ExcelUtil<SurveyOptions>(SurveyOptions.class);
//        return util.exportExcel(list, "options");
//    }
//
//    /**
//     * 获取题目选项详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('survey:options:query')")
//    @GetMapping(value = "/{optionId}")
//    public AjaxResult getInfo(@PathVariable("optionId") Long optionId)
//    {
//        return AjaxResult.success(surveyOptionsService.selectSurveyOptionsById(optionId));
//    }
//
//    /**
//     * 新增题目选项
//     */
//    @PreAuthorize("@ss.hasPermi('survey:options:add')")
//    @Log(title = "题目选项", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody SurveyOptions surveyOptions)
//    {
//        return toAjax(surveyOptionsService.insertSurveyOptions(surveyOptions));
//    }
//
//    /**
//     * 修改题目选项
//     */
//    @PreAuthorize("@ss.hasPermi('survey:options:edit')")
//    @Log(title = "题目选项", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody SurveyOptions surveyOptions)
//    {
//        return toAjax(surveyOptionsService.updateSurveyOptions(surveyOptions));
//    }
//
//    /**
//     * 删除题目选项
//     */
//    @PreAuthorize("@ss.hasPermi('survey:options:remove')")
//    @Log(title = "题目选项", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{optionIds}")
//    public AjaxResult remove(@PathVariable Long[] optionIds)
//    {
//        return toAjax(surveyOptionsService.deleteSurveyOptionsByIds(optionIds));
//    }
}
