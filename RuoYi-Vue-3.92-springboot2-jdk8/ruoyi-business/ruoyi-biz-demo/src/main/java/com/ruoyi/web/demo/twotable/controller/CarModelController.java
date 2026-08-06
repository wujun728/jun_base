package com.ruoyi.web.demo.twotable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.demo.twotable.entity.CarModel;
import com.ruoyi.web.demo.twotable.service.ICarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.sys.controller
 * @title: 消息模版控制器
 * @description: 消息模版控制器 * @date: 2018-09-03 15:10:10
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */

@RestController
@RequestMapping("/demo/twotable/carmodel")
public class CarModelController extends BaseController {

    @Autowired
    private ICarModelService carModelService;


    /**
     * 根据页码和每页记录数，以及查询条件动态加载数据
     *
     * @param request
     * @throws IOException
     */
    @PreAuthorize("@ss.hasPermi('demo:twotable:car:list')")
    @GetMapping("/list")
    public TableDataInfo list(HttpServletRequest request) throws IOException {
        //加入条件
        QueryWrapper<CarModel> entityWrapper = new QueryWrapper<>();
        entityWrapper.orderByAsc( "sort");
        String keyword = request.getParameter("keyword");
        String carId = request.getParameter("carId");
        if (!StringUtils.isEmpty(carId) && !StringUtils.isEmpty(keyword)) {
            entityWrapper.eq("car_id", carId).and(i -> i.like("label", keyword).or().like("value", keyword));
        } else if (!StringUtils.isEmpty(carId)) {
            entityWrapper.eq("car_id", carId);
        }

        // 预处理
        Page<CarModel> page = carModelService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(), entityWrapper);
        return getDataTable(page.getRecords());
    }

    /**
     * 获取车辆型号详细信息
     */
    @PreAuthorize("@ss.hasPermi('demo:twotable:car:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(carModelService.getById(id));
    }

    /**
     * 新增车辆型号
     */
    @PreAuthorize("@ss.hasPermi('demo:twotable:car:add')")
    @Log(title = "车辆型号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarModel carModel) {
        return toAjax(carModelService.save(carModel));
    }

    /**
     * 修改车辆型号
     */
    @PreAuthorize("@ss.hasPermi('demo:twotable:car:edit')")
    @Log(title = "车辆型号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarModel carModel) {
        return toAjax(carModelService.updateById(carModel));
    }

    /**
     * 删除车辆型号
     */
    @PreAuthorize("@ss.hasPermi('demo:twotable:car:remove')")
    @Log(title = "车辆型号", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(carModelService.removeByIds(Arrays.asList(infoIds)));
    }

}
