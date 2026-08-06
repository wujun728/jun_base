package com.ruoyi.oauth.wx.service;

import com.ruoyi.auth.common.domain.OauthUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.JSON;
import com.ruoyi.common.utils.http.HttpUtils;

import com.fasterxml.jackson.databind.JsonNode;

public interface WxLoginService {

    public String doLogin(String code, boolean autoRegister);

    public String doRegister(OauthUser oauthUser);

    public default JsonNode doAuth(String url, String appid, String secret, String code) {
        StringBuilder builder = new StringBuilder(url);
        builder.append("?appid=").append(appid)
                .append("&secret=").append(secret)
                .append("&js_code=").append(code)
                .append("&grant_type=").append("authorization_code");
        String getMessageUrl = builder.toString();
    String result = HttpUtils.get(getMessageUrl);
        JsonNode jsonObject = JSON.parseObject(result);
        if (jsonObject.has("openid")) {
            String openid = jsonObject.get("openid").asText();
            String sessionKey = jsonObject.get("session_key").asText();
            System.out.println("openid:" + openid);
            System.out.println("sessionKey:" + sessionKey);
            return jsonObject;
        } else {
            throw new ServiceException(jsonObject.get("errmsg").asText(), jsonObject.get("errcode").asInt());
        }
    }
}
