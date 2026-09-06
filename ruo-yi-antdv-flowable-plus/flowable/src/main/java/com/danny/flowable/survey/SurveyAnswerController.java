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
 * 问卷填写Controller
 * 
 * @author 2y
 * @date 2021-03-21
 */
@RestController
@RequestMapping("/survey/answer")
public class SurveyAnswerController extends BaseController
{
//    @Autowired
//    private ISurveyAnswerService surveyAnswerService;
//
//    /**
//     * 查询问卷填写列表
//     */
//    @PreAuthorize("@ss.hasPermi('survey:answer:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(SurveyAnswer surveyAnswer)
//    {
//        startPage();
//        List<SurveyAnswer> list = surveyAnswerService.selectSurveyAnswerList(surveyAnswer);
//        return getDataTable(list);
//    }
//
//    /**
//     * 查询问卷填写列表
//     */
//    @PreAuthorize("@ss.hasPermi('survey:answer:list')")
//    @GetMapping("/allList")
//    public TableDataInfo allList(SurveyAnswerDto surveyAnswer)
//    {
//        startPage();
//        List<SurveyAnswerDto> list = surveyAnswerService.selectAllList(surveyAnswer);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出问卷填写列表
//     */
//    @PreAuthorize("@ss.hasPermi('survey:answer:export')")
//    @Log(title = "问卷填写", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(SurveyAnswer surveyAnswer)
//    {
//        List<SurveyAnswer> list = surveyAnswerService.selectSurveyAnswerList(surveyAnswer);
//        ExcelUtil<SurveyAnswer> util = new ExcelUtil<SurveyAnswer>(SurveyAnswer.class);
//        return util.exportExcel(list, "answer");
//    }
//
//    /**
//     * 获取问卷填写详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('survey:answer:query')")
//    @GetMapping(value = "/{answerId}")
//    public AjaxResult getInfo(@PathVariable("answerId") Long answerId)
//    {
//        return AjaxResult.success(surveyAnswerService.selectSurveyAnswerById(answerId));
//    }
//
//    /**
//     * 根据用户编号调查问卷查询填写详情
//     */
//    @GetMapping(value = "/detail/{paperId}/{userId}")
//    public AjaxResult getDetailInfo(@PathVariable("paperId") Long paperId, @PathVariable("userId") Long userId)
//    {
//        return AjaxResult.success(surveyAnswerService.searchAllByUserId(paperId,userId));
//    }
//
//    /**
//     * 新增问卷填写
//     */
//    @PreAuthorize("@ss.hasPermi('survey:answer:add')")
//    @Log(title = "问卷填写", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody SurveyAnswer surveyAnswer)
//    {
//        return toAjax(surveyAnswerService.insertSurveyAnswer(surveyAnswer));
//    }
//
//    /**
//     * 新增问卷填写
//     */
//    @Log(title = "问卷填写", businessType = BusinessType.INSERT)
//    @PostMapping("/addBatch")
//    public AjaxResult addBatch(@RequestBody List<SurveyAnswer> surveyAnswer)
//    {
//        return toAjax(surveyAnswerService.addBatch(surveyAnswer));
//    }
//
//    /**
//     * 修改问卷填写
//     */
//    @PreAuthorize("@ss.hasPermi('survey:answer:edit')")
//    @Log(title = "问卷填写", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody SurveyAnswer surveyAnswer)
//    {
//        return toAjax(surveyAnswerService.updateSurveyAnswer(surveyAnswer));
//    }
//
//    /**
//     * 删除问卷填写
//     */
//    @PreAuthorize("@ss.hasPermi('survey:answer:remove')")
//    @Log(title = "问卷填写", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{answerIds}")
//    public AjaxResult remove(@PathVariable Long[] answerIds)
//    {
//        return toAjax(surveyAnswerService.deleteSurveyAnswerByIds(answerIds));
//    }
}
