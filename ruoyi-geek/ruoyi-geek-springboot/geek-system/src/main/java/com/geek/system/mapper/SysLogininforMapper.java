package com.geek.system.mapper;

import org.apache.ibatis.annotations.Update;

import com.geek.system.domain.SysLogininfor;
import com.mybatisflex.core.BaseMapper;

/**
 * 系统访问日志情况信息 数据层
 * 
 * @author geek
 */
public interface SysLogininforMapper extends BaseMapper<SysLogininfor> {

    /**
     * 清空系统登录日志
     * 
     * @return 结果
     */
    @Update("truncate table sys_logininfor")
    public int cleanLogininfor();
}
