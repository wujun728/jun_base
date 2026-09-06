package com.ruoyi.client.controller;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.MockConfig;
import com.github.jsonzou.jmockdata.TypeReference;
import com.ruoyi.client.enums.CheckStatusEnums;
import com.ruoyi.client.enums.InterestEnums;
import com.ruoyi.client.enums.SexEnums;
import com.ruoyi.client.enums.StatusEnums;
import com.ruoyi.client.model.AVueCrudModel;
import com.ruoyi.client.model.AVueSimpleModel;
import com.ruoyi.client.utils.JsonParseUtils;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Controller
public class AVueConfigControllerTest extends TestCase {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String HAL_MEDIA_TYPE = "application/hal+json";

    public static final String LIST_URL = "/config/list";
    public static final String UPDATE_URL = "/config/update";
    public static final String SAVE_URL = "/config/save";

    @RequestMapping(value = LIST_URL, method = RequestMethod.POST, produces = {APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    @ResponseBody
    public ResponseEntity<Object> list(@RequestBody AVueCrudModel body) {
        logger.info("list request : " + JsonParseUtils.toJson(body));

        //        List<AVueCrudModel> crudModels = new ArrayList<>();
        //        for (int i = 0; i < 10; i++) {
        //            AVueSimpleModel crudModel = new AVueSimpleModel();
        //            crudModel.setconfig
        //            crudModels.add(crudModel);
        //        }
        List<AVueSimpleModel> mockList = JMockData.mock(new TypeReference<List<AVueSimpleModel>>() {
        });

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", 100);
        resultMap.put("pageSize", 10);
        resultMap.put("data", mockList);
        //        String json =
        //            "{\"pageSize\":5,\"total\":20,\"data\":[{\"id\":1,\"parentId\":2,\"label\":\"标签11\",\"serverId\":1,\"path\":\"/tags\",\"component\":1,\"icon\":\"icon-caidan\",\"creator\":\"liukx\",\"created\":\"2021-02-01 23:55:33\"},{\"id\":2,\"parentId\":1,\"label\":\"标签2\",\"serverId\":2,\"path\":\"/tags\",\"component\":1,\"icon\":\"icon-caidan\",\"creator\":\"liukx\",\"created\":\"2021-02-01 23:55:33\"}]}";
        //        Map data = JsonParseUtils.parse(json, Map.class);
        return ResponseEntity.ok(resultMap);
    }

    @RequestMapping(value = UPDATE_URL, method = RequestMethod.POST, produces = {APPLICATION_JSON_VALUE,
        HAL_MEDIA_TYPE})
    @ResponseBody
    public ResponseEntity<Boolean> update(@RequestBody AVueCrudModel body) {
        logger.info("update request : " + JsonParseUtils.toJson(body));
        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = SAVE_URL, method = RequestMethod.POST, produces = {APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    @ResponseBody
    public ResponseEntity<Boolean> save(@RequestBody AVueCrudModel body) {
        logger.info("save request : " + JsonParseUtils.toJson(body));
        return ResponseEntity.ok(true);
    }

    private Integer mockInteger(int min, int max) {
        MockConfig mockConfig = new MockConfig();
        mockConfig.intRange(min, max);
        return JMockData.mock(Integer.class, mockConfig);
    }
}