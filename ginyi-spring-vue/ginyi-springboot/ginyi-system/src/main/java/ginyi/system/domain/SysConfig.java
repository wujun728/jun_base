package ginyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 参数配置表 sys_config
 *
 * @author ruoyi
 */
@Data
public class SysConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    @TableId
    private Long configId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;

}
