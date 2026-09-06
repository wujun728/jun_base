package ginyi.server.admin.controller;

import ginyi.common.result.CommonResult;
import ginyi.system.service.IVerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@Api(tags = "验证码模块")
@RequestMapping("/api/verify")
public class SysVerifyController {

    @Resource
    private IVerifyService verifyService;

    @ApiOperation("图片验证码")
    @GetMapping("/captcha")
    public CommonResult captcha(){
        String img = verifyService.captcha();
        HashMap<String, String> map = new HashMap<>();
        map.put("img", img);
        return CommonResult.success(map);
    }

}
