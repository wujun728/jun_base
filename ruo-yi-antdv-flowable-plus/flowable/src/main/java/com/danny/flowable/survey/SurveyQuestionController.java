package com.danny.flowable.survey;

import java.util.List;

import com.danny.common.core.controller.BaseController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 问卷题目Controller
 * 
 * @author survey
 * @date 2021-03-21
 */
@RestController
@RequestMapping("/survey/question")
public class SurveyQuestionController extends BaseController
{
//    @Autowired
//    private ISurveyQuestionService surveyQuestionService;
//
//    /**
//     * 查询问卷题目列表
//     */
//    @PreAuthorize("@ss.hasPermi('survey:question:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(SurveyQuestion surveyQuestion)
//    {
//        startPage();
//        List<SurveyQuestion> list = surveyQuestionService.selectSurveyQuestionList(surveyQuestion);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出问卷题目列表
//     */
//    @PreAuthorize("@ss.hasPermi('survey:question:export')")
//    @Log(title = "问卷题目", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public AjaxResult export(SurveyQuestion surveyQuestion)
//    {
//        List<SurveyQuestion> list = surveyQuestionService.selectSurveyQuestionList(surveyQuestion);
//        ExcelUtil<SurveyQuestion> util = new ExcelUtil<SurveyQuestion>(SurveyQuestion.class);
//        return util.exportExcel(list, "question");
//    }
//
//    /**
//     * 获取问卷题目详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('survey:question:query')")
//    @GetMapping(value = "/{questionId}")
//    public AjaxResult getInfo(@PathVariable("questionId") Long questionId)
//    {
//        return AjaxResult.success(surveyQuestionService.selectSurveyQuestionById(questionId));
//    }
//
//    /**
//     * 新增问卷题目
//     */
//    @PreAuthorize("@ss.hasPermi('survey:question:add')")
//    @Log(title = "问卷题目", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody SurveyQuestion surveyQuestion)
//    {
//        return toAjax(surveyQuestionService.insertSurveyQuestion(surveyQuestion));
//    }
//
//    /**
//     * 修改问卷题目
//     */
//    @PreAuthorize("@ss.hasPermi('survey:question:edit')")
//    @Log(title = "问卷题目", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody SurveyQuestion surveyQuestion)
//    {
//        return toAjax(surveyQuestionService.updateSurveyQuestion(surveyQuestion));
//    }
//
//    /**
//     * 删除问卷题目
//     */
//    @PreAuthorize("@ss.hasPermi('survey:question:remove')")
//    @Log(title = "问卷题目", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{questionIds}")
//    public AjaxResult remove(@PathVariable Long[] questionIds)
//    {
//        return toAjax(surveyQuestionService.deleteSurveyQuestionByIds(questionIds));
//    }
}
