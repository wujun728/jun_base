package ginyi.system.domain.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CacheVo {

    /**
     * 键名
     */
    @ApiModelProperty("键名")
    private String key;

    /**
     * 键值
     */
    @ApiModelProperty("键名")
    private Object value;

    /**
     * 时间
     */
    @ApiModelProperty("剩余时间")
    private long expire;
}
