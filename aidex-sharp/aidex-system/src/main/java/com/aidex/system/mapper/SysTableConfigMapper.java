package com.aidex.system.mapper;

import com.aidex.common.core.mapper.BaseMapper;
import com.aidex.system.domain.SysTableConfig;

import java.util.List;

/**
 * 个性化配置Mapper接口
 * @author aidex
 * @email aidex@qq.com
 * @date 2021-03-31
 */
public interface SysTableConfigMapper extends BaseMapper<SysTableConfig>
{
    /**
     * 批量删除个性化配置
     *
     * @param tableKeys 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysTableConfigByIds(String[] tableKeys);

    /**
     * 根据传入用户id和表格唯一编码查询表格列配置信息，如果没有则取管理员配置
     * @param sysTableConfig
     * @return
     */
    public List<SysTableConfig> getInfoByTableKey(SysTableConfig sysTableConfig);
}
