package com.ruoyi.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.biz.domain.Region;
import com.ruoyi.biz.mapper.RegionMapper;
import com.ruoyi.biz.service.IRegionService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 中国行政区划（省市区县乡）Service业务层处理
 *
 * @author 姜彦汐
 * @site https://www.undsky.com
 * @date 2025-09-12
 */
@Service
@RequiredArgsConstructor
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {
    private final RegionMapper regionMapper;
    protected final Validator validator;

    @Override
    public String importRegion(
            List<Region> list, int titleNum, Boolean
            isUpdateSupport,
            String operName) {
        if (StringUtils.isNull(list) || list.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Region region = list.get(i);
            try {
                LambdaQueryWrapper<Region> queryWrapper = new LambdaQueryWrapper<>();
                List<Region> checkList = regionMapper.selectList(queryWrapper);
                if (checkList.size() == 0) {
                    BeanValidators.validateWithException(validator, region);
                    insertRegion(region);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、记录" + (i + titleNum + 2) + "：" + region.getId() + " 导入成功")
                    ;
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, region);
                    region.setId(checkList.get(0).getId());
                    updateRegion(region);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、记录" + (i + titleNum + 2) + "：" + region.getId() + " 更新成功")
                    ;
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、记录" + (i + titleNum + 2) + "：" + region.getId() + " 已存在")
                    ;
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、记录" + (i + titleNum + 2) + "：" + region.
                        getId() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public IPage<Region> pageRegion(Page<Region> page, QueryWrapper<Region> queryWrapper) {
        return regionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public String idsToNames(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return null;
        }
        String[] regionIds = StringUtils.split(ids, ",");
        String[] regionNames = new String[regionIds.length];
        for (int i = 0; i < regionIds.length; i++) {
            Region region = this.getById(regionIds[i]);
            if (region != null) {
                regionNames[i] = region.getName();
            }
        }
        return StringUtils.join(regionNames, "/");
    }

    @Override
    public String namesToIds(String names) {
        if (StringUtils.isEmpty(names)) {
            return null;
        }
        String[] regionNames = StringUtils.split(names, "/");
        String[] regionIds = new String[regionNames.length];
        for (int i = 0; i < regionNames.length; i++) {
            QueryWrapper<Region> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("level", i + 1);
            queryWrapper.like("name", regionNames[i]);
            if (i > 0) {
                queryWrapper.eq("parent_id", regionIds[i - 1]);
            }
            Region region = this.getOne(queryWrapper);
            if (region != null) {
                regionIds[i] = region.getId();
            } else {
                return null;
            }
        }
        return StringUtils.join(regionIds, ",");
    }

    /**
     * 查询中国行政区划（省市区县乡）
     *
     * @param id 中国行政区划（省市区县乡）主键
     * @return 中国行政区划（省市区县乡）
     */
    @Override
    public Region selectRegionById(String id) {
        return regionMapper.selectById(id);
    }

    /**
     * 查询中国行政区划（省市区县乡）列表
     *
     * @param region 中国行政区划（省市区县乡）
     * @return 中国行政区划（省市区县乡）
     */
    @Override
    public List<Region> selectRegionList(Region region) {
        return regionMapper.selectRegionList(region);
    }

    /**
     * 新增中国行政区划（省市区县乡）
     *
     * @param region 中国行政区划（省市区县乡）
     * @return 结果
     */
    @Override
    public int insertRegion(Region region) {
        return regionMapper.insert(region);
    }

    /**
     * 修改中国行政区划（省市区县乡）
     *
     * @param region 中国行政区划（省市区县乡）
     * @return 结果
     */
    @Override
    public int updateRegion(Region region) {
        return regionMapper.updateById(region);
    }

    /**
     * 批量删除中国行政区划（省市区县乡）
     *
     * @param ids 需要删除的中国行政区划（省市区县乡）主键
     * @return 结果
     */
    @Override
    public int deleteRegionByIds(String[] ids) {
        return regionMapper.deleteByIds(Arrays.asList(ids));
    }

    /**
     * 删除中国行政区划（省市区县乡）信息
     *
     * @param id 中国行政区划（省市区县乡）主键
     * @return 结果
     */
    @Override
    public int deleteRegionById(String id) {
        return regionMapper.deleteById(id);
    }
}
