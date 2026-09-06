package ginyi.system.domain.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("缓存数据请求参数")
@Data
public class CacheDto {

    /**
     * 键名
     */
    @ApiModelProperty("键名")
    private String key;

    /**
     * 类型
     */
    @ApiModelProperty("数据类型")
    private String type;

}
