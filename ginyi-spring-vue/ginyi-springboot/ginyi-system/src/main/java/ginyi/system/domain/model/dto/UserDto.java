package ginyi.system.domain.model.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import ginyi.common.swagger.AddGroup;
import ginyi.common.swagger.UpdateGroup;
import ginyi.system.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("系统用户请求参数")
public class UserDto extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @NotNull(groups = UpdateGroup.class, message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty("用户账号")
    @NotBlank(groups = {UpdateGroup.class, AddGroup.class}, message = "用户账号不能为空")
    private String userName;

    @ApiModelProperty("部门id")
    @NotNull(groups = {UpdateGroup.class, AddGroup.class}, message = "部门id不能为空")
    private Long deptId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty("手机号码")
    @Pattern(regexp = "(?:0|86|\\+86)?1[3-9]\\d{9}", message = "手机号码格式不正确")
    private String phoneNumber;

    @ApiModelProperty("性别（0男 1女 2未知）")
    @Size(min = 0, max = 2, message = "性别不合法，0男 1女 2未知")
    private String sex;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("状态（0正常 1停用）")
    @Size(min = 0, max = 1, message = "状态不合法")
    private String status;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    @ApiModelProperty("创建时间，开始时间")
    private Date beginTime;

    @ApiModelProperty("创建时间，结束时间")
    private Date endTime;

    @TableLogic
    private String deleted;

    @ApiModelProperty("岗位id")
    private List<Long> postIds;

    @ApiModelProperty("角色id")
    private List<Long> roleIds;

}
