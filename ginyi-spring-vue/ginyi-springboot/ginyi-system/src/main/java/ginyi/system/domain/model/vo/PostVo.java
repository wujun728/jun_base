package ginyi.system.domain.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import ginyi.system.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("系统岗位返回数据")
public class PostVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("岗位序号")
    private Long postId;

    @ApiModelProperty("岗位编码")
    private String postCode;

    @ApiModelProperty("岗位名称")
    private String postName;

    @ApiModelProperty("岗位排序")
    private Integer sort;

    @ApiModelProperty("状态（0正常 1停用）")
    private String status;

}
