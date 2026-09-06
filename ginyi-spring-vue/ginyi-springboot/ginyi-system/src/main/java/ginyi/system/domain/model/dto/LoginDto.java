package ginyi.system.domain.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 用户登录对象
 *
 * @author ruoyi
 */
@Data
@ApiModel("登录用户参数")
public class LoginDto {

    @ApiModelProperty(value = "用户名", required = true)
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 5, max = 15, message = "用户名长度必须在5~15个字符之间")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 15, message = "密码长度必须在6~15个字符之间")
    private String password;

    @ApiModelProperty(value = "验证码")
    private String code;

}
