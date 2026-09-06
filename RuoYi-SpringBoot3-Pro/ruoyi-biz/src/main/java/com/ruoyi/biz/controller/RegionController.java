package com.ruoyi.biz.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.biz.domain.Region;
import com.ruoyi.biz.service.IRegionService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 中国行政区划（省市区县乡）Controller
 *
 * @author 姜彦汐
 * @site https://www.undsky.com
 * @date 2025-09-12
 */
@RestController
@RequestMapping("/biz/Region")
@RequiredArgsConstructor
public class RegionController extends BaseController {
    private final IRegionService regionService;
//    private final RegionMapper regionMapper;

    /**
     * 查询中国行政区划（省市区县乡）列表
     */
    //@PreAuthorize("@ss.hasPermi('biz:Region:list')")
    @Anonymous
    @GetMapping("/list")
    public AjaxResult list(Region region) {
        QueryWrapper<Region> queryWrapper = new QueryWrapper<>();
        if (region.getLevel() == null) {
            region.setLevel(1);
        }
        queryWrapper.eq("level", region.getLevel());
        if (region.getLevel() > 1) {
            if (StringUtils.isEmpty(region.getParentId())) {
                return error("need parentId");
            } else {
                queryWrapper.eq("parent_id", region.getParentId());
            }
        }
        return success(regionService.list(queryWrapper));
    }

    /**
     * 导出中国行政区划（省市区县乡）列表
     */
    @PreAuthorize("@ss.hasPermi('biz:Region:export')")
    @Log(title = "中国行政区划（省市区县乡）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Region region) {
        List<Region> list = regionService.selectRegionList(region);
        ExcelUtil<Region> util = new ExcelUtil<Region>(Region.class);
        util.exportExcel(response, list, "中国行政区划（省市区县乡）");
    }

    /**
     * 导入中国行政区划（省市区县乡）数据
     */
    @PreAuthorize("@ss.hasPermi('biz:Region:import')")
    @Log(title = "中国行政区划（省市区县乡）", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        int titleNum = 0;
        ExcelUtil<Region> util = new ExcelUtil<Region>(Region.class);
        List<Region> list = util.importExcel(file.getInputStream(), titleNum);
        String operName = getUsername();
        String message = regionService.importRegion(list, titleNum, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response, Region region) {
        List<Region> list = new ArrayList<>();
        ExcelUtil<Region> util = new ExcelUtil<Region>(Region.class);
        util.exportExcel(response, list, "中国行政区划（省市区县乡）");
    }

    /**
     * 获取中国行政区划（省市区县乡）详细信息
     */
//    @PreAuthorize("@ss.hasPermi('biz:Region:query')")
    @Anonymous
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(regionService.selectRegionById(id));
    }

    /**
     * 新增中国行政区划（省市区县乡）
     */
    @PreAuthorize("@ss.hasPermi('biz:Region:add')")
    @Log(title = "中国行政区划（省市区县乡）", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody Region region) {
        regionService.insertRegion(region);
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("id", region.getId());
        return success(jsonObject);
    }

    /**
     * 修改中国行政区划（省市区县乡）
     */
    @PreAuthorize("@ss.hasPermi('biz:Region:edit')")
    @Log(title = "中国行政区划（省市区县乡）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Region region) {
        return toAjax(regionService.updateRegion(region));
    }

    /**
     * 删除中国行政区划（省市区县乡）
     */
    @PreAuthorize("@ss.hasPermi('biz:Region:remove')")
    @Log(title = "中国行政区划（省市区县乡）", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(regionService.deleteRegionByIds(ids));
    }
}
