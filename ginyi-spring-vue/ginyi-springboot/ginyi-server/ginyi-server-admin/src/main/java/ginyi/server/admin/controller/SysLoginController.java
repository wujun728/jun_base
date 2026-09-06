package ginyi.server.admin.controller;

import ginyi.common.result.CommonResult;
import ginyi.system.domain.model.dto.LoginDto;
import ginyi.system.domain.model.dto.RegisterDto;
import ginyi.system.domain.model.vo.LoginVo;
import ginyi.system.service.ISysLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Api(tags = "登录模块")
@Slf4j
public class SysLoginController {

    @Autowired
    private ISysLoginService loginService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public CommonResult<LoginVo> login(@RequestBody @Validated LoginDto loginDto) {
        LoginVo loginVo = loginService.login(loginDto);
        return CommonResult.success(loginVo);
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public CommonResult register(@RequestBody @Validated RegisterDto registerDto) {
        loginService.register(registerDto);
        return CommonResult.success();
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public CommonResult logout() {
        return CommonResult.success();
    }
}
