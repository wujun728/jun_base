package com.aidex.web.controller.system;

import com.aidex.common.annotation.Log;
import com.aidex.common.core.controller.BaseController;
import com.aidex.common.core.domain.R;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.enums.BusinessType;
import com.aidex.common.utils.poi.ExcelUtil;
import com.aidex.framework.cache.ConfigUtils;
import com.aidex.system.domain.SysConfig;
import com.aidex.system.service.SysConfigService;
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
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
    @Autowired
    private SysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/page")
    public R<PageInfo> page(SysConfig config, HttpServletRequest request, HttpServletResponse response) {
        config.setPage(new PageDomain(request, response));
        return R.data(configService.findPage(config));
    }

    /**
     * 根据ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{id}")
    public R<SysConfig> detail(@PathVariable String id) {
        return R.data(configService.get(id));
    }


    /**
     * 新增参数
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R save(@Validated @RequestBody SysConfig config) {
        return R.status(configService.save(config));
    }

    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @GetMapping("/export")
    public R export(SysConfig config) {
        List<SysConfig> list = configService.findList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        return util.exportExcel(list, "参数数据");
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public R getConfigKey(@PathVariable String configKey) {
        return R.data(ConfigUtils.getConfigValueByKey(configKey));
    }


    /**
     * 删除岗位
     *
     * @param ids
     * @return R
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        configService.deleteConfigByIds(ids);
        return R.success();
    }


    /**
     * 刷新缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R refreshCache() {
        configService.refreshCache();
        return R.status(true);
    }

    /**
     * 校验参数键名是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.CHECK)
    @GetMapping("/checkConfigKeyUnique")
    public R checkDictDataValueUnique(SysConfig sysConfig) {
        Map<String, String> checkMap = new HashMap<String, String>(1);
        try {
            configService.checkConfigKeyUnique(sysConfig);
            checkMap.put("code", "1");
        } catch (Exception e) {
            checkMap.put("code", "2");
        }
        return R.data(checkMap);
    }
}
