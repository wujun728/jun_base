package ginyi.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统文件相关信息
 *
 * @author ruoyi
 */
@Data

public class SysFile {
    /**
     * 盘符路径
     */
    @ApiModelProperty("盘符路径")
    private String dirName;

    /**
     * 文件类型
     */
    @ApiModelProperty("文件类型")
    private String typeName;

    /**
     * 总大小，单位G
     */
    @ApiModelProperty("总大小，单位G")
    private double total;

    /**
     * 剩余大小，单位G
     */
    @ApiModelProperty("可用大小，单位G")
    private double free;

    /**
     * 已经使用量，单位G
     */
    @ApiModelProperty("已用大小，单位G")
    private double used;

    /**
     * 资源的使用率
     */
    @ApiModelProperty("已用百分比")
    private double usage;

}
