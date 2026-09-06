package com.geek.common.core.domain.model;

import com.anji.captcha.model.vo.CaptchaVO;
import com.geek.common.core.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户登录对象
 * 
 * @author geek
 */
@Schema(title = "用户登录对象")
@Data
@EqualsAndHashCode(callSuper=true)
public class LoginBody extends BaseEntity {
    /** 用户名 */
    @Schema(title = "用户名")
    private String username;

    /** 用户密码 */
    @Schema(title = "用户密码")
    private String password;

    /** 手机号码 */
    @Schema(title = "手机号码")
    private String phonenumber;

    /** 邮箱 */
    @Schema(title = "邮箱")
    private String email;

    /** 验证码 */
    @Schema(title = "验证码")
    private String code;

    /** 唯一标识 */
    @Schema(title = "唯一标识")
    private String uuid;

    /** 验证码 */
    @Schema(title = "验证码")
    private CaptchaVO captcha;
}
