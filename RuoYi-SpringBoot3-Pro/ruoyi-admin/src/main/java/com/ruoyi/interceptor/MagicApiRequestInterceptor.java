package com.ruoyi.interceptor;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.controller.common.CommonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.interceptor.RequestInterceptor;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.model.JsonBean;
import org.ssssssss.magicapi.core.model.Options;
import org.ssssssss.magicapi.core.servlet.MagicHttpServletRequest;
import org.ssssssss.magicapi.core.servlet.MagicHttpServletResponse;
import org.ssssssss.script.MagicScriptContext;

/**
 * magic-api 接口鉴权
 */
@Component
public class MagicApiRequestInterceptor implements RequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(MagicApiRequestInterceptor.class);

    /**
     * 接口请求之前
     *
     * @param info    接口信息
     * @param context 脚本变量信息
     */
    @Override
    public Object preHandle(ApiInfo info, MagicScriptContext context, MagicHttpServletRequest request, MagicHttpServletResponse response) throws Exception {
        boolean needLogin = StringUtils.equals("true", info.getOptionValue(Options.REQUIRE_LOGIN));
        String role = info.getOptionValue(Options.ROLE);
        boolean needRole = StringUtils.isNotBlank(role);
        String permission = info.getOptionValue(Options.PERMISSION);
        boolean needPermission = StringUtils.isNotBlank(permission);
        if (needRole || needPermission) {
            needLogin = true;
        }
        if (needLogin) {
            try {
                SecurityUtils.getLoginUser();
            } catch (Exception e) {
                return new JsonBean<>(401, "用户未登录");
            }
        }
        if (needRole) {
            if (!SecurityUtils.hasRole(role)) {
                return new JsonBean<>(403, "用户权限不足");
            }
        }
        if (needPermission) {
            if (!SecurityUtils.hasPermi(permission)) {
                return new JsonBean<>(403, "用户权限不足");
            }
        }
        return null;
    }
}
