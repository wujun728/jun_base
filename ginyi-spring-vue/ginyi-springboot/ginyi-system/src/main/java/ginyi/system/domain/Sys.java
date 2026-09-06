package ginyi.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统相关信息
 *
 * @author ruoyi
 */
@Data
public class Sys {
    /**
     * 服务器名称
     */
    @ApiModelProperty("服务器名称")
    private String computerName;

    /**
     * 服务器Ip
     */
    @ApiModelProperty("服务器ip")
    private String computerIp;

    /**
     * 项目路径
     */
    @ApiModelProperty("项目路径")
    private String userDir;

    /**
     * 操作系统
     */
    @ApiModelProperty("操作系统")
    private String osName;

    /**
     * 系统架构
     */
    @ApiModelProperty("系统架构")
    private String osArch;
}
