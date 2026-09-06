package com.ruoyi.biz.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.biz.domain.Region;

/**
 * 中国行政区划（省市区县乡）Mapper接口
 *
 * @author 姜彦汐
 * @site https://www.undsky.com
 * @date 2025-09-12
 */
public interface RegionMapper extends BaseMapper<Region> {
    /**
     }
     * 查询中国行政区划（省市区县乡）列表
     *
     * @param region 中国行政区划（省市区县乡）
     * @return 中国行政区划（省市区县乡）集合
     */
    public List<Region> selectRegionList(Region region);
}
