package com.ruoyi.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.biz.domain.Region;

import java.util.List;

/**
 * 中国行政区划（省市区县乡）Service接口
 *
 * @author 姜彦汐
 * @site https://www.undsky.com
 * @date 2025-09-12
 */
public interface IRegionService extends IService<Region> {
    public IPage<Region> pageRegion(Page<Region> page, QueryWrapper<Region> queryWrapper);

    public String idsToNames(String ids);

    public String namesToIds(String names);

    /**
     * 查询中国行政区划（省市区县乡）
     *
     * @param id 中国行政区划（省市区县乡）主键
     * @return 中国行政区划（省市区县乡）
     */
    public Region selectRegionById(String id);

    /**
     * 查询中国行政区划（省市区县乡）列表
     *
     * @param region 中国行政区划（省市区县乡）
     * @return 中国行政区划（省市区县乡）集合
     */
    public List<Region> selectRegionList(Region region);

    /**
     * 新增中国行政区划（省市区县乡）
     *
     * @param region 中国行政区划（省市区县乡）
     * @return 结果
     */
    public int insertRegion(Region region);

    /**
     * 修改中国行政区划（省市区县乡）
     *
     * @param region 中国行政区划（省市区县乡）
     * @return 结果
     */
    public int updateRegion(Region region);

    /**
     * 批量删除中国行政区划（省市区县乡）
     *
     * @param ids 需要删除的中国行政区划（省市区县乡）主键集合
     * @return 结果
     */
    public int deleteRegionByIds(String[] ids);

    /**
     * 删除中国行政区划（省市区县乡）信息
     *
     * @param id 中国行政区划（省市区县乡）主键
     * @return 结果
     */
    public int deleteRegionById(String id);

    /**
     * 导入中国行政区划（省市区县乡）数据
     *
     * @param list            数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public String importRegion(
            List<Region> list, int titleNum, Boolean
            isUpdateSupport,
            String operName);
}
