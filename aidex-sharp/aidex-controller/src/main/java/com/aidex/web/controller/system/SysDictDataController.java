package com.aidex.web.controller.system;

import com.aidex.common.annotation.Log;
import com.aidex.common.annotation.RepeatSubmit;
import com.aidex.common.core.controller.BaseController;
import com.aidex.common.core.domain.R;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.enums.BusinessType;
import com.aidex.common.utils.poi.ExcelUtil;
import com.aidex.framework.cache.DictUtils;
import com.aidex.system.domain.SysDictData;
import com.aidex.system.service.SysDictDataService;
import com.aidex.system.service.SysDictTypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {

    @Autowired
    private SysDictDataService dictDataService;

    @Autowired
    private SysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/page")
    public R<PageInfo> list(SysDictData dictData, HttpServletRequest request, HttpServletResponse response) {
        dictData.setPage(new PageDomain(request, response));
        return R.data(dictDataService.findPage(dictData));
    }

    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @GetMapping("/export")
    public R export(SysDictData dictData) {
        List<SysDictData> list = dictDataService.findList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        return util.exportExcel(list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{id}")
    public R<SysDictData> getInfo(@PathVariable String id) {
        return R.data(dictDataService.get(id));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public R<List<SysDictData>> dictType(@PathVariable String dictType) {
        return R.data(DictUtils.getDictList(dictType));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/all/type/{dictType}")
    public R<List<SysDictData>> allDictType(@PathVariable String dictType) {
        return R.data(DictUtils.getAllDictList(dictType));
    }


    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public R save(@Validated @RequestBody SysDictData dictData) {
        return R.status(dictDataService.save(dictData));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        dictDataService.deleteDictDataByIds(ids);
        return R.success();
    }

    /**
     * 校验数据字典值是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.CHECK)
    @GetMapping("/checkDictDataValueUnique")
    public R checkDictDataValueUnique(SysDictData sysDictData) {
        Map<String, String> checkMap = new HashMap<String, String>(16);
        try {
            dictDataService.checkDictDataValueUnique(sysDictData);
            checkMap.put("code", "1");
        } catch (Exception e) {
            checkMap.put("code", "2");
        }
        return R.data(checkMap);
    }

    /**
     * 获取最大排序号
     */
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @Log(title = "字典类型", businessType = BusinessType.SELECT)
    @GetMapping("/findMaxSort/{dictType}")
    public R findMaxSort(@PathVariable("dictType") String dictType) {
        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictType(dictType);
        return R.data(dictDataService.findMaxSort(sysDictData));
    }

}
