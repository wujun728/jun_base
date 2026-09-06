package com.geek.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geek.common.config.GeekConfig;
import com.geek.common.utils.StringUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 首页
 *
 * @author geek
 */
@Tag(name = "首页")
@RestController
public class SysIndexController {

    /** 系统基础配置 */
    @Autowired
    private GeekConfig geekConfig;

    /**
     * 访问首页，提示语
     */
    @Operation(summary = "访问首页", description = "提示语")
    @RequestMapping("/")
    public String index() {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", geekConfig.getName(), geekConfig.getVersion());
    }
}
