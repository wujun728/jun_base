package com.template.system.controller;

import com.template.framework.config.system.SystemConfig;
import com.template.framework.core.controller.BaseController;
import com.template.framework.core.domain.Result;
import com.template.framework.core.service.SysRegisterService;
import com.template.framework.utils.service.StringUtils;
import com.template.system.entity.model.RegisterBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 *
 * @author template
 */
@RestController
public class SysRegisterController extends BaseController {
    @Autowired
    private SysRegisterService registerService;


    @PostMapping("/register")
    public Result register(@RequestBody RegisterBody user) {
        if (!SystemConfig.getRegisterUser()) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
