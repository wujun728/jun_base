package com.ruoyi.web.demo.twotable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;

@TableName("demo_car_model")
@SuppressWarnings("serial")
public class CarModel extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    @TableField(value = "car_id")
    private String carId;
    @TableField(value = "name")
    private String name;
    @TableField(value = "value")
    private String value;

    @TableField(value = "sort")
    private Integer sort;
    @TableField(exist = false)
    private String code;

    public String getCarId() {
        return this.carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
