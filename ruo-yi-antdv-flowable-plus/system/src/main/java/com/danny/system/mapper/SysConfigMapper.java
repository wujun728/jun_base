package com.danny.system.mapper;

import com.danny.common.core.mapper.BaseMapper;
import com.danny.system.domain.SysConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 参数配置 数据层
 * 
 * @author ruoyi
 */
public interface SysConfigMapper extends BaseMapper<SysConfig>
{
    /**
     * 批量删除参数信息
     * 
     * @param configIds 需要删除的参数ID
     * @return 结果。
     */
    public int deleteConfigByIds(@Param("configIds") String[] configIds, @Param("DEL_FLAG_DELETE") String DEL_FLAG_DELETE);


    /**
     * 唯一性校验判断
     * @param
     * @return
     */
    @Override
    public List<SysConfig> findListWithUnique(SysConfig sysConfig);


}
