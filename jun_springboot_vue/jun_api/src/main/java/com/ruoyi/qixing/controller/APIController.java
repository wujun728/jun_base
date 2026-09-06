package com.ruoyi.qixing.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.spring.SpringUtils;
import io.github.wujun728.db.record.Db;
import io.github.wujun728.db.record.Record;
import io.github.wujun728.db.utils.RecordUtil;
import io.github.wujun728.db.utils.TreeBuildUtil;
import org.apache.commons.compress.utils.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujun
 */
@RestController
@RequestMapping("/api/")
public class APIController extends BaseController {


    @GetMapping(value = "/test")
    public R getInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("info", "sucess");
        map.put("rows", Lists.newArrayList());
        return R.ok(map);
    }


    @GetMapping("apiGroupTree")
    public R apiGroupTree() throws IOException {
        //ActiveRecordUtil.initActiveRecordPlugin("main", SpringUtils.getBean(DataSource.class));
        Db.init("main",SpringUtils.getBean(DataSource.class));
        String sql = "  SELECT * from api_group ";
        List<Record> recordList = Db.use().find(sql);
        List<Map<String, Object>> mapList = RecordUtil.recordToMaps(recordList, true);
        //mapList = (List) mapList.stream().map(item -> { return item; }).collect(Collectors.toList());
        List tree = TreeBuildUtil.listToTree(mapList, "0", "id", "pid");
        return R.ok(tree);
    }
    @GetMapping("apiGroupList")
    public R apiGroupList() throws IOException {
        Db.init("main",SpringUtils.getBean(DataSource.class));
        String sql = "  SELECT * from api_group ";
        List<Record> recordList = Db.use().find(sql);
        List<Map<String, Object>> mapList = RecordUtil.recordToMaps(recordList, true);
        //mapList = (List) mapList.stream().map(item -> { return item; }).collect(Collectors.toList());
        //List tree = TreeBuildUtil.listToTree(mapList, "0", "id", "pid");
        return R.ok(mapList);
    }


    @GetMapping("apiConfig")
    public R apiConfig() throws IOException {
        Db.init("main",SpringUtils.getBean(DataSource.class));
        String sql = " SELECT * from api_config  ";
        List<Record> recordList = Db.use().find(sql);
        List<Map<String, Object>> mapList = RecordUtil.recordToMaps(recordList, true);
        return R.ok(mapList);
    }

}
