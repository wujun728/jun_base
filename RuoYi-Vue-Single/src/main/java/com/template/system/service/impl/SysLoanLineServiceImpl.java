package com.template.system.service.impl;

import java.util.List;
import com.template.framework.utils.service.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.template.system.mapper.SysLoanLineMapper;
import com.template.system.entity.SysLoanLine;
import com.template.system.service.ISysLoanLineService;

/**
 * 借出单-明细Service业务层处理
 *
 * @author template
 * @date 2025-10-04
 */
@Service
public class SysLoanLineServiceImpl implements ISysLoanLineService
{
    @Autowired
    private SysLoanLineMapper sysLoanLineMapper;

    /**
     * 查询借出单-明细
     *
     * @param id 借出单-明细主键
     * @return 借出单-明细
     */
    @Override
    public SysLoanLine selectSysLoanLineById(Long id)
    {
        return sysLoanLineMapper.selectSysLoanLineById(id);
    }

    /**
     * 查询借出单-明细列表
     *
     * @param sysLoanLine 借出单-明细
     * @return 借出单-明细
     */
    @Override
    public List<SysLoanLine> selectSysLoanLineList(SysLoanLine sysLoanLine)
    {
        return sysLoanLineMapper.selectSysLoanLineList(sysLoanLine);
    }

    /**
     * 新增借出单-明细
     *
     * @param sysLoanLine 借出单-明细
     * @return 结果
     */
    @Override
    public int insertSysLoanLine(SysLoanLine sysLoanLine)
    {
        sysLoanLine.setCreateTime(DateUtils.getNowDate());
        return sysLoanLineMapper.insertSysLoanLine(sysLoanLine);
    }

    /**
     * 修改借出单-明细
     *
     * @param sysLoanLine 借出单-明细
     * @return 结果
     */
    @Override
    public int updateSysLoanLine(SysLoanLine sysLoanLine)
    {
        sysLoanLine.setUpdateTime(DateUtils.getNowDate());
        return sysLoanLineMapper.updateSysLoanLine(sysLoanLine);
    }

    /**
     * 批量删除借出单-明细
     *
     * @param ids 需要删除的借出单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysLoanLineByIds(Long[] ids)
    {
        return sysLoanLineMapper.deleteSysLoanLineByIds(ids);
    }

    /**
     * 删除借出单-明细信息
     *
     * @param id 借出单-明细主键
     * @return 结果
     */
    @Override
    public int deleteSysLoanLineById(Long id)
    {
        return sysLoanLineMapper.deleteSysLoanLineById(id);
    }
}
