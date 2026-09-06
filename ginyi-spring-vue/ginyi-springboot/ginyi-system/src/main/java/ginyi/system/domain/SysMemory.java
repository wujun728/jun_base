package ginyi.system.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 內存相关信息
 *
 * @author ruoyi
 */
@Data
public class SysMemory {
    /**
     * 内存总量，单位G
     */
    @ApiModelProperty("内存总数，单位G")
    private double total;

    /**
     * 已用内存，单位G
     */
    @ApiModelProperty("已用内存，单位G")
    private double used;

    /**
     * 剩余内存，单位G
     */
    @ApiModelProperty("可用内存，单位G")
    private double free;

    /**
     * 内存使用率
     */
    @ApiModelProperty("使用率")
    private double usage;

}
