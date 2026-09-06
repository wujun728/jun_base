package ginyi.system.domain.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CacheKeyVo {

    /**
     * 键名
     */
    @ApiModelProperty("键名")
    private String key;

    /**
     * 数据类型
     */
    @ApiModelProperty("类型")
    private String type;
}
