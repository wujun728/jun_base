package com.ruoyi.nocode.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.nocode.domain.MyFormDef;
import com.ruoyi.nocode.domain.vo.UploadResVO;
import com.ruoyi.nocode.service.IMyFormDefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 表单定义Controller
 *
 * @date 2022-07-27
 */
@RestController
@RequestMapping("/nocode/form")
public class FormController extends BaseController {
    @Autowired
    private IMyFormDefService myFormDefService;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 查询表单定义列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MyFormDef myFormDef) {
        startPage();
        List<MyFormDef> list = myFormDefService.selectMyFormDefList(myFormDef);
        return getDataTable(list);
    }

    /**
     * 导出表单定义列表
     */
    @Log(title = "表单定义", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MyFormDef myFormDef) {
        List<MyFormDef> list = myFormDefService.selectMyFormDefList(myFormDef);
        ExcelUtil<MyFormDef> util = new ExcelUtil<MyFormDef>(MyFormDef.class);
        return util.exportExcel(list, "def");
    }

    /**
     * 获取表单定义详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(myFormDefService.selectMyFormDefById(id));
    }

    /**
     * 新增表单定义
     */
    @Log(title = "表单定义", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MyFormDef myFormDef) {
        return toAjax(myFormDefService.insertMyFormDef(myFormDef));
    }

    /**
     * 修改表单定义
     */
    @Log(title = "表单定义", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MyFormDef myFormDef) {
        return myFormDefService.updateMyFormDef(myFormDef);
    }

    /**
     * 删除表单定义
     */
    @Log(title = "表单定义", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(myFormDefService.deleteMyFormDefByIds(ids));
    }

    @Anonymous
    @PostMapping("/upload")
    public UploadResVO uploadFile(MultipartFile file) {
        UploadResVO uploadResVO = new UploadResVO();
        try {
            String filePath = RuoYiConfig.getUploadPath();
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            uploadResVO.setUrl(url);
        } catch (Exception e) {
            uploadResVO.setUrl(null);
        }
        return uploadResVO;
    }

}
