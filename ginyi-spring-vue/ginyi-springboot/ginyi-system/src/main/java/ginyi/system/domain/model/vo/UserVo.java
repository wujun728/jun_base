package ginyi.system.domain.model.vo;

import ginyi.system.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("系统用户返回数据")
public class UserVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号码")
    private String phoneNumber;

    @ApiModelProperty("性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("岗位id")
    private List<Long> postIds;

    @ApiModelProperty("角色id")
    private List<Long> roleIds;
}
