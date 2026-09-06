package ginyi.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 服务器相关信息
 *
 * @author ruoyi
 */
@Data
public class SysServer {

    /**
     * 服务器信息
     */
    @ApiModelProperty("服务器信息")
    private Sys sys;

    /**
     * cpu
     */
    @ApiModelProperty("cpu相关信息")
    private SysCpu cpu;

    /**
     * 硬盘
     */
    @ApiModelProperty("系统硬盘")
    private List<SysFile> file;

    /**
     * jvm
     */
    @ApiModelProperty("Java虚拟机")
    private SysJvm jvm;

    /**
     * 内存
     */
    @ApiModelProperty("内存信息")
    private SysMemory memory;

}
