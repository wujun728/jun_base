package com.geek.web.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.geek.common.annotation.Anonymous;
import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.utils.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Anonymous
@RestController
@RequestMapping("/captcha")
public class CaptchaController extends BaseController {
    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/get")
    public AjaxResult get(@RequestBody CaptchaVO data, HttpServletRequest request) {
        assert request.getRemoteHost() != null;
        data.setBrowserInfo(getRemoteId(request));
        ResponseModel rm = captchaService.get(data);
        if (rm.isSuccess()) {
            return AjaxResult.success(rm.getRepMsg(), rm.getRepData());
        } else {
            return AjaxResult.error(rm.getRepMsg());
        }
    }

    @PostMapping("/check")
    public AjaxResult check(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        ResponseModel rm = captchaService.check(data);
        if (rm.isSuccess()) {
            return AjaxResult.success(rm.getRepMsg(), rm.getRepData());
        } else {
            return AjaxResult.error(rm.getRepMsg());
        }
    }

    /***
     * 服务端验证接口，独立部署的场景使用，集成部署的场景：服务内部调用，不需要调用此接口，可注释掉
     * 
     * @param data
     * @param request
     * @return
     */
    @PostMapping("/verify")
    public AjaxResult verify(@RequestBody CaptchaVO data, HttpServletRequest request) {
        ResponseModel rm = captchaService.verification(data);
        if (rm.isSuccess()) {
            return AjaxResult.success(rm.getRepMsg(), rm.getRepData());
        } else {
            return AjaxResult.error(rm.getRepMsg());
        }
    }

    public static final String getRemoteId(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromXfwd(xfwd);
        String ua = request.getHeader("user-agent");
        if (StringUtils.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }

    private static String getRemoteIpFromXfwd(String xfwd) {
        if (StringUtils.isNotBlank(xfwd)) {
            String[] ipList = xfwd.split(",");
            return StringUtils.trim(ipList[0]);
        }
        return null;
    }
}
