package com.aidex.web.controller.system;

import com.aidex.common.annotation.Log;
import com.aidex.common.constant.Constants;
import com.aidex.common.core.controller.BaseController;
import com.aidex.common.core.domain.AjaxResult;
import com.aidex.common.core.domain.R;
import com.aidex.common.core.domain.TreeNode;
import com.aidex.common.core.domain.entity.SysDept;
import com.aidex.common.enums.BusinessType;
import com.aidex.common.utils.StringUtils;
import com.aidex.system.service.SysDeptService;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * 部门信息管理
 * @author ruoyi
 */

@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 根据层级展开部门树表格
     * @param level 展开层级
     * @param id 起始展开ID
     * @return com.aidex.common.core.domain.R
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/{level}/{id}")
    public R list(@PathVariable("level") @NotEmpty int level, @PathVariable("id") String id) {
        if (level == 0) {
            level = 2;
        }
        //默认为根节点
        if (StringUtils.isEmpty(id)) {
            id = Constants.TREE_ROOT;
        }
        List<SysDept> depts = sysDeptService.listDataByLevel(level, id);
        return R.data(depts);
    }
    /**
     * 部门树表格检索
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/searchDeptList")
    public R searchDeptList (SysDept sysDept)
    {
        List<SysDept> depts =  sysDeptService.searchDeptList(sysDept);
        return R.data(depts);
    }
    /**
     * 根据ID获取部门信息
     * @param id
     * @return com.aidex.common.core.domain.R
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{id}")
    public R detail(@NotBlank @PathVariable String id) {
        SysDept sysDept = sysDeptService.get(id);
        SysDept parentDept = sysDeptService.get(sysDept.getParentId());
        if(parentDept != null){
            sysDept.setParentName(parentDept.getDeptName());
            sysDept.setParentDeptType(parentDept.getDeptType());
        }
        return R.data(sysDept);
    }

    /**
     * 获取当前父节点下最大编号
     * @param parentId
     * @return com.aidex.common.core.domain.R
     */
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @Log(title = "岗位管理", businessType = BusinessType.SELECT)
    @GetMapping("/findMaxSort/{parentId}")
    public R findMaxSort(@PathVariable("parentId") String parentId) {
        SysDept sysDept = new SysDept();
        sysDept.setParentId(parentId);
        return R.data(sysDeptService.findMaxSort(sysDept));
    }

    /**
     * 校验同一个父部门下是否存在同名的部门
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @param id
     * @return com.aidex.common.core.domain.R
     */
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @Log(title = "部门管理", businessType = BusinessType.CHECK)
    @GetMapping(value = {"/validateDeptNameUnique/{deptName}/{parentId}/{id}", "/validateDeptNameUnique/{deptName}/{parentId}"})
    public R validateDeptNameUnique(@NotBlank(message = "部门名称不允许为空") @PathVariable("deptName") String deptName, @NotBlank(message = "ID不允许为空")  @PathVariable("parentId") String parentId, @PathVariable(value = "id", required = false) String id) {
        SysDept sysDept = new SysDept();
        sysDept.setParentId(parentId);
        sysDept.setDeptName(deptName);
        sysDept.setId(id);
        sysDeptService.checkDeptNameUnique(sysDept);
        return R.status(Boolean.TRUE);
    }

    /**
     * 新增部门
     * @param dept
     * @return com.aidex.common.core.domain.R
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody @Validated  SysDept dept) {
        sysDeptService.save(dept);
        return R.data(dept);
    }

    /**
     * 修改部门
     * @param dept
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody @Validated  SysDept dept) {
        sysDeptService.save(dept);
        return R.data(dept);
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R remove(@PathVariable  String id) {
        SysDept sysDept = new SysDept();
        sysDept.setId(id);
        return R.status(sysDeptService.remove(sysDept));
    }

    /**
     * 部门选择树
     * @param level 初始展开层级
     * @param id 展开节点ID
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/listTree/{level}/{id}")
    public R listTree(@NotBlank @PathVariable("level") int level, @PathVariable("id") String id) {
        if (level == 0) {
            level = 2;
        }
        //默认为根节点
        if (StringUtils.isEmpty(id)) {
            id = Constants.TREE_ROOT;
        }
        List<TreeNode> depts = sysDeptService.buildDeptTree(level, id);
        return R.data(depts);
    }
    /**
     * 部门树检索
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/search")
    public R search (SysDept sysDept)
    {
        List<TreeNode> depts =  sysDeptService.search(sysDept.getDeptName());
        return R.data(depts);
    }

    /**
     * 部门选择树
     * @param level
     * @param id
     * @param excludeId 排除节点
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping(value = {"/listTreeExcludeChild/{level}/{id}/{excludeId}"})
    public R listTreeExcludeChild(@NotBlank @PathVariable("level") int level, @PathVariable("id") String id, @PathVariable(value = "excludeId",required = false) String excludeId) {
        if (level == 0) {
            level = 2;
        }
        //默认为根节点
        if (StringUtils.isEmpty(id)) {
            id = Constants.TREE_ROOT;
        }
        List<TreeNode> depts = sysDeptService.buildDeptTreeExcludeChild(10, id,excludeId);
        return R.data(depts);
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public R roleDeptTreeselect(@PathVariable("roleId") String roleId) {
        List<SysDept> depts = sysDeptService.findList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", sysDeptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", sysDeptService.buildDeptTreeExcludeChild(-1, Constants.TREE_ROOT,null));
        return R.data(ajax);
    }

    /**
     * 选人页面按部门加载
     * @param level 初始展开层级
     * @param id 展开节点ID
     * @return
     */
    @GetMapping("/userSelectList/{level}/{id}")
    public R userSelectList(@NotBlank @PathVariable("level") int level, @PathVariable("id") String id) {
        if (level == 0) {
            level = 2;
        }
        //默认为根节点
        if (StringUtils.isEmpty(id)) {
            id = Constants.TREE_ROOT;
        }
        List<TreeNode> depts = sysDeptService.buildDeptSelectUserTree(level, id);
        return R.data(depts);
    }
    /**
     * 按部门树检索用户
     */
    @GetMapping("/searchDeptUserList")
    public R searchDeptUserList (SysDept sysDept)
    {
        List<TreeNode> depts =  sysDeptService.searchDeptUserList(sysDept);
        return R.data(depts);
    }


    /**
     * 通用选人页面根据用户ID
     * @return
     */
    @PostMapping(value = { "/getDeptInfoByIds"})
    public R getDeptInfoByIds(@Validated @RequestBody JSONObject deptIdsObj)
    {
        List<Map<String,Object>> sysDeptList = sysDeptService.getDeptInfoByIds(deptIdsObj);
        return R.data(sysDeptList);
    }

    /**
     * 刷新缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "部门管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R refreshCache() {
        sysDeptService.refreshCache();
        return R.status(true);
    }
}
