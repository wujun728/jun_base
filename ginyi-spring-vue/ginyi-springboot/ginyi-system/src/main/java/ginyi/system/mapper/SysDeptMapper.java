package ginyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ginyi.system.domain.SysDept;
import ginyi.system.domain.model.dto.DeptDto;
import org.apache.ibatis.annotations.Param;

public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 查询部门列表
     *
     * @param deptDto
     * @param page
     */
    public IPage<SysDept> list(@Param("deptDto") DeptDto deptDto, Page page);

    /**
     * 新增部门
     *
     * @param deptDto
     */
    public void insertDept(@Param("deptDto") DeptDto deptDto);

    /**
     * 更新部门
     *
     * @param deptDto
     */
    public void updateDept(@Param("deptDto") DeptDto deptDto);

    /**
     * 更新状态
     *
     * @param deptDto
     */
    public void updateDeptStatus(@Param("deptDto") DeptDto deptDto);
}
