package ginyi.system.service;

import ginyi.system.domain.SysDept;
import ginyi.system.domain.model.dto.DeptDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.DeptVo;

import java.util.Set;

/**
 * 系统部门
 */
public interface ISysDeptService {

    /**
     * 获取部门列表
     * @param deptDto
     * @param page
     * @param pageSize
     */
    public BaseVo<SysDept> list(DeptDto deptDto, Long page, Long pageSize);

    /**
     * 根据部门id获取部门详情
     * @param deptId
     * @return
     */
    public DeptVo getDeptByDeptId(Long deptId);

    /**
     * 新增部门
     * @param deptDto
     */
    public void addDept(DeptDto deptDto);

    /**
     * 更新部门
     * @param deptDto
     */
    public void updateDept(DeptDto deptDto);

    /**
     * 删除部门
     * @param deptId
     */
    public void removeDeptById(Long deptId);

    /**
     * 批量删除部门
     * @param ids
     */
    public void removeDeptByIds(Set<Long> ids);

    /**
     * 更改状态（0正常 1停用）
     * @param deptDto
     */
    public void updateStatus(DeptDto deptDto);
}
