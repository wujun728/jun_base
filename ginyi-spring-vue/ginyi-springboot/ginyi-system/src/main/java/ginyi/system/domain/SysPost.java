package ginyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.annotations.Select;

/**
 * 岗位表 sys_post
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysPost extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 岗位序号
     */
    @TableId
    private Long postId;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 岗位排序
     */
    private Integer sort;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 用户是否存在此岗位标识 默认不存在
     */
    @TableField(select = false, exist = false)
    private boolean flag = false;

}
