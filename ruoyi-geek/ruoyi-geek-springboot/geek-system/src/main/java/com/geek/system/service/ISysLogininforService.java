package com.geek.system.service;

import com.geek.system.domain.SysLogininfor;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 系统访问日志情况信息 服务层
 * 
 * @author geek
 */
public interface ISysLogininforService extends IService<SysLogininfor> {
    /**
     * 清空系统登录日志
     */
    public void cleanLogininfor();

    public Page<SysLogininfor> page(SysLogininfor logininfor, int pageNum, int pageSize);

    public void export(SysLogininfor logininfor, HttpServletResponse response);
}
