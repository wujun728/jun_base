package com.ruoyi.client.controller;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.ruoyi.client.enums.CheckStatusEnums;
import com.ruoyi.client.enums.InterestEnums;
import com.ruoyi.client.enums.SexEnums;
import com.ruoyi.client.enums.StatusEnums;
import com.ruoyi.client.model.AVueCrudModel;
import com.ruoyi.client.utils.JsonParseUtils;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Controller
public class AVueControllerTest extends TestCase {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String HAL_MEDIA_TYPE = "application/hal+json";

    public static final String LIST_URL = "/liukx/list";
    public static final String UPDATE_URL = "/liukx/update";
    public static final String SAVE_URL = "/liukx/save";
    public static final String BODY_URL = "/test/body";

    @RequestMapping(value = LIST_URL, method = RequestMethod.POST, produces = {APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    @ResponseBody
    public ResponseEntity<Object> list(
        @RequestBody
            AVueCrudModel body) {
        logger.info("list request : " + JsonParseUtils.toJson(body));

        List<AVueCrudModel> crudModels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AVueCrudModel crudModel = new AVueCrudModel();
            crudModel.setAge(mockInteger(18, 30));
            crudModel.setCheckStatus(JMockData.mock(CheckStatusEnums.class).getCode().toString());
            crudModel.setId(JMockData.mock(Integer.class).toString());
            crudModel.setInterest(Arrays.asList(JMockData.mock(InterestEnums.class).getCode().toString(),
                JMockData.mock(InterestEnums.class).getCode().toString()));

            crudModel.setLikeStar(mockInteger(1, 4));

            crudModel.setSex((Integer)JMockData.mock(SexEnums.class).getCode());
            crudModel.setStatus((Integer)JMockData.mock(StatusEnums.class).getCode());
            crudModel.setValidDate(JMockData.mock(Date.class));
            //            crudModel.setTimeRange(JMockData.mock(Timestamp.class));
            crudModel.setTime(JMockData.mock(Date.class));
            //            crudModel.setDateRange();
            crudModel.setUsername(JMockData.mock(String.class));
            crudModels.add(crudModel);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", 100);
        resultMap.put("pageSize", 10);
        resultMap.put("data", crudModels);
        //        String json =
        //            "{\"pageSize\":5,\"total\":20,\"data\":[{\"id\":1,\"parentId\":2,\"label\":\"标签11\",\"serverId\":1,\"path\":\"/tags\",\"component\":1,\"icon\":\"icon-caidan\",\"creator\":\"liukx\",\"created\":\"2021-02-01 23:55:33\"},{\"id\":2,\"parentId\":1,\"label\":\"标签2\",\"serverId\":2,\"path\":\"/tags\",\"component\":1,\"icon\":\"icon-caidan\",\"creator\":\"liukx\",\"created\":\"2021-02-01 23:55:33\"}]}";
        //        Map data = JsonParseUtils.parse(json, Map.class);
        return ResponseEntity.ok(resultMap);
    }

    @RequestMapping(value = UPDATE_URL, method = RequestMethod.POST, produces = {APPLICATION_JSON_VALUE,
        HAL_MEDIA_TYPE})
    @ResponseBody
    public ResponseEntity update(
        @RequestBody
            AVueCrudModel body) {
        logger.info("update request : " + JsonParseUtils.toJson(body));
        return ResponseEntity.ok(ok(null));
    }

    @RequestMapping(value = SAVE_URL, method = RequestMethod.POST, produces = {APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    @ResponseBody
    public ResponseEntity save(
        @RequestBody
            AVueCrudModel body) {
        logger.info("save request : " + JsonParseUtils.toJson(body));
        return ResponseEntity.ok(ok(null));
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Map> upload(
        @RequestParam("file")
            MultipartFile file) {
        System.out.println(file);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        Map<String, Object> objResult = new HashMap<>();
        objResult.put("fileName", file.getOriginalFilename());
        objResult.put("url", "https://avuejs.com/imgview/224a5a854632ed8bce5287e9a43b1921.jpg");
        result.put("single", objResult);
        return ResponseEntity.ok(result);
    }

    public Map<String, Object> ok(Object obj) {
        return result(true, null, obj);
    }

    private Map<String, Object> result(boolean flag, String message, Object obj) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", true);
        resultMap.put("message", message);
        resultMap.put("data", obj);
        return resultMap;
    }

    public Map<String, Object> no(String message) {
        return result(false, message, null);
    }

    @PostMapping(BODY_URL)
    @ResponseBody
    public ResponseEntity testBody(
        @RequestBody
            Map<String, Object> request) {
        logger.info("后台请求到的数据: " + JsonParseUtils.toJson(request));
        return ResponseEntity.ok(ok(null));
    }

    private Integer mockInteger(int min, int max) {
        MockConfig mockConfig = new MockConfig();
        mockConfig.intRange(min, max);
        return JMockData.mock(Integer.class, mockConfig);
    }
}