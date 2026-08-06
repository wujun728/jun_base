package com.ruoyi.web.demo.twotable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.demo.twotable.entity.Car;
import com.ruoyi.web.demo.twotable.service.ICarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/demo/twotable/car")
public class CarController extends BaseController {

    @Autowired
    private ICarService carService;


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
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc( "create_time");
        String keyword = request.getParameter("keyword");
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("name", keyword).or().like("code", keyword);
        }
        // 预处理
        Page<Car> page = carService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(), queryWrapper);
        return getDataTable(page.getRecords());
    }

    /**
     * 获取车辆品牌详细信息
     */
    @PreAuthorize("@ss.hasPermi('demo:twotable:car:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(carService.getById(id));
    }

    /**
     * 新增车辆品牌
     */
    @PreAuthorize("@ss.hasPermi('demo:twotable:car:add')")
    @Log(title = "车辆品牌", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Car car) {
        return toAjax(carService.save(car));
    }

    /**
     * 修改车辆品牌
     */
    @PreAuthorize("@ss.hasPermi('demo:twotable:car:edit')")
    @Log(title = "车辆品牌", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Car car) {
        return toAjax(carService.updateById(car));
    }

    /**
     * 删除车辆品牌
     */
    @PreAuthorize("@ss.hasPermi('demo:twotable:car:remove')")
    @Log(title = "车辆品牌", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(carService.removeByIds(Arrays.asList(infoIds)));
    }


}
