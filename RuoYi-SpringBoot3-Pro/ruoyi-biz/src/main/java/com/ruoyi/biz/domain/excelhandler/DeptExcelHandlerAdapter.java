package com.ruoyi.biz.domain.excelhandler;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.poi.ExcelHandlerAdapter;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.impl.SysDeptServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

public class DeptExcelHandlerAdapter implements ExcelHandlerAdapter {

    private ISysDeptService sysDeptService = SpringUtils.getBean(SysDeptServiceImpl.class);

    @Override
    public Object format(Object value, String[] args, Cell cell, Workbook wb) {
        SysDept dept = sysDeptService.selectDeptById(Long.valueOf(String.valueOf(value)));
        return dept != null ? dept.getDeptName() : null;
    }
}
