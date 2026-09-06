package com.aidex.web.controller.system;

import com.aidex.common.annotation.Log;
import com.aidex.common.core.controller.BaseController;
import com.aidex.common.core.domain.R;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.enums.BusinessType;
import com.aidex.common.utils.poi.ExcelUtil;
import com.aidex.system.domain.SysDictType;
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
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController
{
    @Autowired
    private SysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/page")
    public R<PageInfo> list(SysDictType dictType, HttpServletRequest request, HttpServletResponse response)
    {
        dictType.setPage(new PageDomain(request, response));
        return R.data(dictTypeService.findPage(dictType));
    }

    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @GetMapping("/export")
    public R export(SysDictType dictType)
    {
        List<SysDictType> list = dictTypeService.findList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        return util.exportExcel(list, "字典类型");
    }

    /**
     * 查询字典类型详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{id}")
    public R<SysDictType> getInfo(@PathVariable String id)
    {
        return R.data(dictTypeService.get(id));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public R save(@Validated @RequestBody SysDictType dict)
    {
        return R.status(dictTypeService.save(dict));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids)
    {
        dictTypeService.deleteDictTypeByIds(ids);
        return R.success();
    }

    /**
     * 刷新缓存
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R refreshCache()
    {
        dictTypeService.refreshCache();
        return R.status(true);
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public R optionselect()
    {
        List<SysDictType> dictTypes = dictTypeService.findList(new SysDictType());
        return R.data(dictTypes);
    }

    /**
     * 校验数据字典类型是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.CHECK)
    @GetMapping("/checkDictTypeUnique")
    public R checkDictTypeUnique(SysDictType sysDictType) {
        Map<String,String> checkMap = new HashMap<String,String>(16);
        try{
            dictTypeService.checkDictTypeUnique(sysDictType);
            checkMap.put("code","1");
        }catch (Exception e){
            checkMap.put("code","2");
        }
        return R.data(checkMap);
    }
}
