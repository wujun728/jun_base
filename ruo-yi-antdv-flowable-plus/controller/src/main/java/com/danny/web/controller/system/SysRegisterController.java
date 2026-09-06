package com.danny.web.controller.system;

import com.danny.common.core.controller.BaseController;
import com.danny.common.core.domain.R;
import com.danny.common.core.domain.entity.RegisterBody;
import com.danny.framework.cache.ConfigUtils;
import com.danny.framework.web.service.SysRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 注册验证
 *
 * @author ruoyi
 */
@RestController
public class SysRegisterController extends BaseController {

    @Autowired
    private SysRegisterService registerService;

    @PostMapping("/register")
    public R register(@RequestBody RegisterBody user) {
        if (!ConfigUtils.getConfigBooleanValueByKey("sys.account.registerUser", Boolean.FALSE)) {
            return R.fail("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? R.success() : R.fail(msg);
    }
}
