package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.system.domain.SysLogininfor;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface SysLogininforMapper extends BaseMapper<SysLogininfor> {
}
