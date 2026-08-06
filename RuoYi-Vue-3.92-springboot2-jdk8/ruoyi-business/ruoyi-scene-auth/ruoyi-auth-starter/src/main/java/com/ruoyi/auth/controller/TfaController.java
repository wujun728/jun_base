package com.ruoyi.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.auth.common.service.TfaService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.RegisterBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/auth/{channel}") // dySms mail
@Tag(name = "【第三方认证】双因素认证")
public class TfaController extends BaseController {

    @Autowired(required = false)
    Map<String, TfaService> tfaServiceMap;

    @PostConstruct
    public void init() {
        if (tfaServiceMap == null) {
            tfaServiceMap = new HashMap<>();
            logger.warn("请注意，没有加载任何双认证服务");
        } else {
            tfaServiceMap.forEach((k, v) -> {
                logger.info("已加载双认证服务 {}", k);
            });
        }
    }

    @Operation(summary = "发送注册验证码")
    @PostMapping("/send/register")
    @Anonymous
    public AjaxResult sendRegister(@PathVariable String channel, @RequestBody RegisterBody registerBody) {
        TfaService tfaService = tfaServiceMap.get("auth:service:" + channel);
        tfaService.doRegister(registerBody);
        return success();
    }

    @Operation(summary = "验证注册验证码")
    @PostMapping("/verify/register")
    @Anonymous
    public AjaxResult verifyRegister(@PathVariable String channel, @RequestBody RegisterBody registerBody) {
        TfaService tfaService = tfaServiceMap.get("auth:service:" + channel);
        tfaService.doRegisterVerify(registerBody);
        return success();
    }

    @Operation(summary = "发送登录验证码")
    @PostMapping("/send/login")
    @Anonymous
    public AjaxResult sendLogin(@PathVariable String channel, @RequestBody LoginBody loginBody,
            @RequestParam(defaultValue = "false") boolean autoRegister) {
        TfaService tfaService = tfaServiceMap.get("auth:service:" + channel);
        tfaService.doLogin(loginBody, autoRegister);
        return success();
    }

    @Operation(summary = "验证登录验证码")
    @PostMapping("/verify/login")
    @Anonymous
    public AjaxResult verifyLogin(@PathVariable String channel, @RequestBody LoginBody loginBody,
            @RequestParam(defaultValue = "false") boolean autoRegister) {
        TfaService tfaService = tfaServiceMap.get("auth:service:" + channel);
        return success(tfaService.doLoginVerify(loginBody, autoRegister));
    }

    @Operation(summary = "发送绑定验证码")
    @PostMapping("/send/bind")
    public AjaxResult send(@PathVariable String channel, @RequestBody LoginBody loginBody) {
        TfaService tfaService = tfaServiceMap.get("auth:service:" + channel);
        tfaService.doBind(loginBody);
        return success();
    }

    @Operation(summary = "验证绑定验证码")
    @PostMapping("/verify/bind") // 发送验证码
    public AjaxResult verify(@PathVariable String channel, @RequestBody LoginBody loginBody) {
        TfaService tfaService = tfaServiceMap.get("auth:service:" + channel);
        tfaService.doBindVerify(loginBody);
        return success();
    }

    @Operation(summary = "发送重置验证码")
    @PostMapping("/send/reset")
    public AjaxResult sendReset(@PathVariable String channel, @RequestBody RegisterBody registerBody) {
        TfaService tfaService = tfaServiceMap.get("auth:service:" + channel);
        tfaService.doReset(registerBody);
        return success();
    }

    @Operation(summary = "验证重置验证码")
    @PostMapping("/verify/reset")
    public AjaxResult verifyReset(@PathVariable String channel, @RequestBody RegisterBody registerBody) {
        TfaService tfaService = tfaServiceMap.get("auth:service:" + channel);
        tfaService.doResetVerify(registerBody);
        return success();
    }
}
