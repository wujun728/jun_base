package ginyi.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.lang.management.ManagementFactory;

/**
 * JVM相关信息
 *
 * @author ruoyi
 */
@Data
public class SysJvm {
    /**
     * 当前JVM占用的内存总数，单位M
     */
    @ApiModelProperty("当前JVM占用的内存总数，单位M")
    private double total;

    /**
     * JVM最大可用内存总数，单位M
     */
    @ApiModelProperty("JVM最大可用内存总数，单位M")
    private double max;

    /**
     * JVM空闲内存，单位M
     */
    @ApiModelProperty("JVM空闲内存，单位M")
    private double free;

    /**
     * JDK名称
     */
    @ApiModelProperty("JDK名称")
    private String name;

    /**
     * JDK版本
     */
    @ApiModelProperty("JDK版本")
    private String version;

    /**
     * JDK路径
     */
    @ApiModelProperty("JDK路径")
    private String home;

    /**
     * 已用内存，单位M
     */
    @ApiModelProperty("已用内存，单位M")
    private double used;

    /**
     * 使用率
     */
    @ApiModelProperty("内存使用率")
    private double usage;

    /**
     * 运行参数
     */
    @ApiModelProperty("运行参数")
    private String inputArgs;


    /**
     * 运行时间
     */
    @ApiModelProperty("运行时间")
    private String runTime;

    /**
     * 启动时间
     */
    @ApiModelProperty("启动时间")
    private String startTime;

}
