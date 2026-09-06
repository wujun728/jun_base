package ginyi.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * CPU相关信息
 * @author ruoyi
 */
@Data
public class SysCpu {
    /**
     * 核心数，直接回显数字
     */
    @ApiModelProperty("核心数")
    private int cpuNum;

    /**
     * CPU总的使用率，不需要回显
     */
    @ApiModelProperty("CPU总使用率，不需要回显")
    private double total;

    /**
     * CPU系统使用率，拼接 % 回显
     */
    @ApiModelProperty("CPU系统使用率")
    private double sys;

    /**
     * CPU用户使用率，拼接 % 回显
     */
    @ApiModelProperty("CPU用户使用率")
    private double used;

    /**
     * CPU当前等待率，拼接 % 回显
     */
    @ApiModelProperty("CPU当前等待率")
    private double wait;

    /**
     * CPU当前空闲率，拼接 % 回显
     */
    @ApiModelProperty("CPU当前空闲率")
    private double free;

}
