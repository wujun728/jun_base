package com.geek.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.DateUtils;
import com.geek.common.utils.poi.ExcelUtil;
import com.geek.system.domain.SysLogininfor;
import com.geek.system.mapper.SysLogininforMapper;
import com.geek.system.service.ISysLogininforService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author geek
 */
@Service
public class SysLogininforServiceImpl extends ServiceImpl<SysLogininforMapper, SysLogininfor>
        implements ISysLogininforService {
    /**
     * 新增系统登录日志
     * 
     * @param logininfor 访问日志对象
     */
    @Override
    public boolean save(SysLogininfor logininfor) {
        logininfor.setLoginTime(DateUtils.getNowDate());
        return super.save(logininfor);
    }

    /**
     * 查询系统登录日志集合
     * 
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    public QueryChain<SysLogininfor> selectLogininforList(SysLogininfor logininfor) {
        return this.queryChain()
                .like(SysLogininfor::getIpaddr, logininfor.getIpaddr())
                .eq(SysLogininfor::getStatus, logininfor.getStatus())
                .like(SysLogininfor::getUserName, logininfor.getUserName())
                .ge(SysLogininfor::getLoginTime, logininfor.getLoginTime())
                .le(SysLogininfor::getLoginTime, logininfor.getLoginTime())
                .orderBy(SysLogininfor::getInfoId, false);
    }

    @Override
    public Page<SysLogininfor> page(SysLogininfor logininfor, int pageNum, int pageSize) {
        return this.selectLogininforList(logininfor).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(SysLogininfor logininfor, HttpServletResponse response) {
        List<SysLogininfor> list = this.selectLogininforList(logininfor).list();
        ExcelUtil<SysLogininfor> util = new ExcelUtil<>(SysLogininfor.class);
        util.exportExcel(response, list, "登录日志");
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        mapper.cleanLogininfor();
    }
}
