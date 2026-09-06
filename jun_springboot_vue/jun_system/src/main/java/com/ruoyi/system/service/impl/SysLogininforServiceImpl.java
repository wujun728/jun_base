package com.ruoyi.system.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.constant.SqlConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.mapper.SysLogininforMapper;
import com.ruoyi.system.service.ISysLogininforService;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysLogininforServiceImpl implements ISysLogininforService
{

    @Autowired
    private SysLogininforMapper logininforMapper;

    /**
     * 新增系统登录日志
     * 
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(SysLogininfor logininfor)
    {
        logininforMapper.insert(logininfor);
    }

    /**
     * 查询系统登录日志集合
     * 
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public IPage<SysLogininfor> selectLogininforList(IPage<SysLogininfor> page, SysLogininfor logininfor)
    {
        LambdaQueryWrapper<SysLogininfor> query = new QueryWrapper<SysLogininfor>().lambda();
        query.like(StringUtils.isNotEmpty(logininfor.getIpaddr()), SysLogininfor::getIpaddr, logininfor.getIpaddr());
        query.eq(StringUtils.isNotEmpty(logininfor.getStatus()), SysLogininfor::getStatus, logininfor.getStatus());
        query.like(StringUtils.isNotEmpty(logininfor.getUserName()), SysLogininfor::getUserName, logininfor.getUserName());

        if (StringUtils.isNotNull(logininfor.getParams()) && StringUtils.isNotNull(logininfor.getParams().get(SqlConstants.FIELD_NAME_BEGIN_TIME)) && StringUtils.isNotNull(logininfor.getParams().get(SqlConstants.FIELD_NAME_END_TIME))) {
            query.between(SysLogininfor::getLoginTime,
                    logininfor.getParams().get(SqlConstants.FIELD_NAME_BEGIN_TIME), logininfor.getParams().get(SqlConstants.FIELD_NAME_END_TIME));
        }

        return logininforMapper.selectPage(page, query);
    }

    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds)
    {
        return logininforMapper.deleteBatchIds(Arrays.asList(infoIds));
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor()
    {
        logininforMapper.delete(new QueryWrapper<SysLogininfor>().lambda().eq(SysLogininfor::getStatus, UserConstants.NORMAL).or().eq(SysLogininfor::getStatus, UserConstants.EXCEPTION));;
    }
}
