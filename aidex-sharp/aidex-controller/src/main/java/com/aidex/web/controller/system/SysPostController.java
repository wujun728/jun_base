package com.aidex.web.controller.system;

import com.aidex.common.annotation.Log;
import com.aidex.common.core.controller.BaseController;
import com.aidex.common.core.domain.R;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.enums.BusinessType;
import com.aidex.common.utils.poi.ExcelUtil;
import com.aidex.generator.domain.GenConfigTemplate;
import com.aidex.system.domain.SysPost;
import com.aidex.system.service.SysPostService;
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
 * 岗位管理
 * @author aidex
 * @email aidex@qq.com
 */
@RestController
@RequestMapping("/system/post")
public class SysPostController extends BaseController {

    @Autowired(required = true)
    private SysPostService postService;

    /**
     * 获取岗位列表
     */
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @GetMapping("/page")
    public R<PageInfo> page(SysPost post, HttpServletRequest request, HttpServletResponse response) {
        post.setPage(new PageDomain(request, response));
        return R.data(postService.findPage(post));
    }

    /**
     * 根据ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping(value = "/{id}")
    public R<SysPost> detail(@PathVariable String id) {
        return R.data(postService.get(id));
    }

    /**
     * 获取最大排序号
     */
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @Log(title = "岗位管理", businessType = BusinessType.SELECT)
    @GetMapping("/findMaxSort")
    public R findMaxSort() {
        return R.data(postService.findMaxSort(new SysPost()));
    }


    /**
     * 新增岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:add')")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R save(@Validated @RequestBody SysPost post) {
        return R.status(postService.save(post));
    }

    /**
     * 修改岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@Validated @RequestBody SysPost post) {
        return R.status(postService.save(post));
    }


    /**
     * 校验岗位名称是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @Log(title = "岗位管理", businessType = BusinessType.CHECK)
    @GetMapping("/checkPostNameUnique")
    public R checkPostNameUnique(SysPost post) {
        Map<String,String> checkMap = new HashMap<String,String>(1);
        try{
            postService.checkPostNameUnique(post);
            checkMap.put("code","1");
        }catch (Exception e){
            checkMap.put("code","2");
        }
        return R.data(checkMap);
    }


    /**
     * 删除岗位
     *
     * @param ids
     * @return R
     */
    @PreAuthorize("@ss.hasPermi('system:post:remove')")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return R.status(postService.deletePostByIds(ids));
    }

    /**
     * 方法描述
     *
     * @param post
     * @return com.aidex.common.core.domain.AjaxResult
     */
    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:post:export')")
    @GetMapping("/export")
    public R export(SysPost post) {
        List<SysPost> list = postService.findList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        return util.exportExcel(list, "岗位数据");
    }

    /**
     * 获取岗位列表
     *
     * @param
     * @return R
     */
    @Log(title = "岗位管理", businessType = BusinessType.SELECT)
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @GetMapping("/list")
    public R list() {
        return R.data(postService.findList(new SysPost()));
    }


    /**
     * 校验参数键名是否存在
     */
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @Log(title = "岗位管理", businessType = BusinessType.CHECK)
    @GetMapping("/checkPostCodeUnique")
    public R checkPostCodeUnique(SysPost sysPost) {
        Map<String,String> checkMap = new HashMap<String,String>(1);
        try{
            postService.checkPostCodeUnique(sysPost);
            checkMap.put("code","1");
        }catch (Exception e){
            checkMap.put("code","2");
        }
        return R.data(checkMap);
    }
}
