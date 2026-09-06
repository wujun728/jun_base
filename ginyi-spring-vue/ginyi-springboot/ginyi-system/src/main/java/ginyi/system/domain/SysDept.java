package ginyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门id")
    @TableId
    private Long deptId;

    /**
     * 父部门ID
     */
    @ApiModelProperty("父级id")
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    private String deptName;

    /**
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    /**
     * 负责人
     */
    @ApiModelProperty("部门负责人")
    private String leader;

    /**
     * 联系电话
     */
    @ApiModelProperty("部门负责人电话")
    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty("部门负责人邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    /**
     * 部门状态:0正常,1停用
     */
    @ApiModelProperty("状态，0正常，1停用")
    private String status;

    /**
     * 删除标志
     */
    @TableLogic
    private String deleted;


    /**
     * 父部门名称
     */
    @TableField(select = false, exist = false)
    @ApiModelProperty("父级部门名称")
    private String parentName;

    /**
     * 子部门
     */
    @TableField(select = false, exist = false)
    @ApiModelProperty("子部门")
    private List<SysDept> children = new ArrayList<SysDept>();

}
