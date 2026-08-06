package com.ruoyi.oauth.wx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.auth.common.domain.OauthUser;
import com.ruoyi.auth.common.service.IOauthUserService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oauth.wx.service.Impl.WxMiniAppLoginServiceImpl;
import com.ruoyi.oauth.wx.service.Impl.WxPubLoginServiceImpl;

@RestController
@RequestMapping("/oauth/wx")
public class WxLoginController extends BaseController {

    @Autowired
    private IOauthUserService oauthUserService;

    @Autowired
    private WxMiniAppLoginServiceImpl wxMiniAppLoginServiceImpl;

    @Autowired
    private WxPubLoginServiceImpl wxPubLoginServiceImpl;

    @Anonymous
    @PostMapping("/login/{source}/{code}")
    public AjaxResult loginMiniApp(@PathVariable("source") String source, @PathVariable("code") String code) {
        String token = null;
        AjaxResult ajax = AjaxResult.success();
        if ("miniapp".equals(source)) {
            token = wxMiniAppLoginServiceImpl.doLogin(code, true);
        } else if ("pub".equals(source)) {
            token = wxPubLoginServiceImpl.doLogin(code, false);
        } else {
            return error("错误的登录方式");
        }
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @PostMapping("/register/{source}/{code}")
    public AjaxResult register(@PathVariable("source") String source, @PathVariable("code") String code) {
        OauthUser oauthUser = oauthUserService.selectOauthUserByUserId(getUserId());
        if (oauthUser != null) {
            return error("不可以重复绑定");
        } else {
            String msg = "";
            oauthUser = new OauthUser();
            oauthUser.setUserId(getUserId());
            oauthUser.setCode(code);
            if ("miniapp".equals(source)) {
                msg = wxMiniAppLoginServiceImpl.doRegister(oauthUser);
            } else if ("pub".equals(source)) {
                msg = wxPubLoginServiceImpl.doRegister(oauthUser);
            } else {
                return error("错误的注册方式");
            }
            return StringUtils.isEmpty(msg) ? success() : error(msg);
        }
    }

}
