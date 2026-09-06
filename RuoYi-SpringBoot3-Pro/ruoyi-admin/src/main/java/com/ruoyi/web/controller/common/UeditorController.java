package com.ruoyi.web.controller.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
//import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.utils.file.FileUploadUtils;

/**
 * Ueditor 请求处理
 *
 * @author ruoyi
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping("/ueditor")
public class UeditorController extends BaseController {
    private final String METHOD_HEAD = "ueditor";
    private final String IMGE_PATH = "/ueditor/images/";
    private final String VIDEO_PATH = "/ueditor/videos/";
    private final String FILE_PATH = "/ueditor/files/";

    @Autowired
    private ServerConfig serverConfig;

    /**
     * ueditor
     */
    @Anonymous
    @ResponseBody
    @RequestMapping(value = "/controller")
    public Object ueditor(HttpServletRequest request, @RequestParam(value = "action", required = true) String action,
                          MultipartFile upfile) throws Exception {
        List<Object> param = new ArrayList<Object>() {
            {
                add(action);
                add(upfile);
            }
        };
        Method method = this.getClass().getMethod(METHOD_HEAD + action, List.class, String.class);
        return method.invoke(this.getClass().newInstance(), param, serverConfig.getUrl());
    }

    /**
     * 读取配置文件
     */
    public JSONObject ueditorconfig(List<Object> param, String fileSuffixUrl) throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("ueditor-config.json");
        String jsonString = new BufferedReader(new InputStreamReader(classPathResource.getInputStream())).lines().parallel().collect(Collectors.joining(System.lineSeparator()));
        JSONObject json = JSON.parseObject(jsonString, JSONObject.class);
        return json;
    }

    /**
     * 上传图片
     */
    public JSONObject ueditoruploadimage(List<Object> param, String fileSuffixUrl) throws Exception {
        JSONObject json = new JSONObject();
        json.put("state", "SUCCESS");
        json.put("url", ueditorcore(param, IMGE_PATH, false, fileSuffixUrl));
        return json;
    }

    /**
     * 上传视频
     */
    public JSONObject ueditoruploadvideo(List<Object> param, String fileSuffixUrl) throws Exception {
        JSONObject json = new JSONObject();
        json.put("state", "SUCCESS");
        json.put("url", ueditorcore(param, VIDEO_PATH, false, fileSuffixUrl));
        return json;
    }

    /**
     * 上传附件
     */
    public JSONObject ueditoruploadfile(List<Object> param, String fileSuffixUrl) throws Exception {
        JSONObject json = new JSONObject();
        json.put("state", "SUCCESS");
        json.put("url", ueditorcore(param, FILE_PATH, true, fileSuffixUrl));
        return json;
    }

    public String ueditorcore(List<Object> param, String path, boolean isFileName, String fileSuffixUrl)
            throws Exception {
        MultipartFile upfile = (MultipartFile) param.get(1);
        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath();
        String fileName = FileUploadUtils.upload(filePath, upfile);
        String url = (StringUtils.equals("dev", RuoYiConfig.getEnv()) ? fileSuffixUrl : RuoYiConfig.getSiteUrl()) + fileName;
        return url;
    }
}
