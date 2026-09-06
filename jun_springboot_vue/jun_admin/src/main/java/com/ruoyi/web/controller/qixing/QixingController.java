package com.ruoyi.web.controller.qixing;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.SecurityUtils;
import io.github.wujun728.db.record.Db;
import io.github.wujun728.db.record.Record;
import io.github.wujun728.db.utils.RecordUtil;
import io.github.wujun728.db.utils.TreeBuildUtil;
import org.apache.commons.compress.utils.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author wujun
 */
@RestController
@RequestMapping("/qixing")
public class QixingController extends BaseController
{

    @PostConstruct
    public void init() {
        //Db.init("main", SpringUtils.getBean(DataSource.class));
    }

    @GetMapping("getRouters1")
    public AjaxResult getRouters1() throws IOException {
        Long userId = SecurityUtils.getUserId();
        //List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        /*select distinct m.menu_id, m.parent_id, m.menu_name, m.path, m.component, m.`query`, m.visible, m.status, ifnull(m.perms,'') as perms, m.is_frame, m.is_cache, m.menu_type, m.icon, m.order_num, m.create_time
        from sys_menu m
        left join sys_role_menu rm on m.menu_id = rm.menu_id
        left join sys_user_role ur on rm.role_id = ur.role_id
        left join sys_role ro on ur.role_id = ro.role_id
        left join sys_user u on ur.user_id = u.user_id
        where u.user_id = #{userId} and m.menu_type in ('M', 'C') and m.status = 0  AND ro.status = 0
        order by m.parent_id, m.order_num*/
        //ActiveRecordUtil.initActiveRecordPlugin("main", SpringUtils.getBean(DataSource.class));

        List<Record> apps = Db.use().find(" SELECT * from sys_menu where parent_id = 0 ");
        List apps1 = RecordUtil.recordToMaps(apps,true);
        List<Record> menus = Db.use().find(" SELECT * from sys_menu where menu_type != 'F' ORDER BY parent_id,order_num ");
        List<Map<String, Object>> menus1 = RecordUtil.recordToMaps(menus,true);
        for(Map map : menus1){
            if(ObjUtil.isNotEmpty(map.get("component"))){
                map.put("component",map.get("component")+".html");
            }
        }
        menus1 = (List) menus1.stream().map(item->{return item;}).collect(Collectors.toList());
        List<Map<String, Object>> menus2 = TreeBuildUtil.listToTree(menus1,"0","menu_id","parent_id");
        Map data = new HashMap<>();
        data.put("apps",apps1);
        data.put("menus",menus2);
        return AjaxResult.success(data);
    }


    //@Autowired
    //private ISysMenuService menuService;

    //@PreAuthorize("@ss.hasPermi('tool:gen:query')")
    @GetMapping(value = "/test")
    public R getInfo()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("info", "sucess");
        map.put("rows", Lists.newArrayList());
        return R.ok(map);
    }


    @GetMapping("getRouters")
    public R getRouters() throws IOException {
        //ActiveRecordUtil.initActiveRecordPlugin("main", SpringUtils.getBean(DataSource.class));
        //Db.init("main",SpringUtils.getBean(DataSource.class));
        List<Record> apps = Db.use().find(" SELECT * from sys_menu where parent_id = 0 ");
        List<Map<String, Object>> apps1 = RecordUtil.recordToMaps(apps,true);
        List<Record> menus = Db.use().find(" SELECT * from sys_menu where menu_type != 'F' ORDER BY parent_id,order_num ");
        List<Map<String, Object>> menus1 = RecordUtil.recordToMaps(menus,true);
        /*for(Map map : menus1){
            if(ObjUtil.isNotEmpty(map.get("component"))){
                map.put("component",map.get("component")+".html");
            }
        }*/
        menus1 = (List) menus1.stream().map(item->{
            if(ObjUtil.isNotEmpty(item.get("component"))){
                item.put("component",item.get("component")+".html");
            }
            return item;
        }).collect(Collectors.toList());
        List menus2 = TreeBuildUtil.listToTree(menus1,"0","menu_id","parent_id");
        Map data = new HashMap<>();
        data.put("apps",apps1);
        data.put("menus",menus2);
        return R.ok(data);
    }

    @GetMapping("getRoutersTemp")
    public R getRoutersTemp() throws IOException {
        List<Record> apps = Db.use().find(" SELECT * from sys_menu where parent_id = 0 ");
        List<Map<String, Object>> apps1 = RecordUtil.recordToMaps(apps,true);
        List<Record> menus = Db.use().find(" SELECT * from sys_menu where menu_type != 'F' ORDER BY parent_id,order_num ");
        List<Map<String, Object>> menus1 = RecordUtil.recordToMaps(menus,true);
        /*for(Map map : menus1){
            if(ObjUtil.isNotEmpty(map.get("component"))){
                map.put("component",map.get("component")+".html");
            }
        }*/
        menus1 = (List) menus1.stream().map(item->{
            if(ObjUtil.isNotEmpty(item.get("component"))){
                item.put("component",item.get("component")+".html");
            }
            return item;
        }).collect(Collectors.toList());
        List<Map<String, Object>> menus2 = TreeBuildUtil.listToTree(menus1,"0","menu_id","parent_id");
        Map data = new HashMap<>();
        List appList = Lists.newArrayList();
        List menuList = Lists.newArrayList();
        for(Map map : apps1){
            Map tmp = new HashMap<>();
            String code = MapUtil.getStr(map,"menu_name");
            tmp.put("code", code);
            tmp.put("name", code);
            tmp.put("active", false);
            appList.add(tmp);
            Map tmp2 = new HashMap<>();
            tmp2.put("code", code);
            tmp2.put("value", menus2.stream().filter(item->code.equalsIgnoreCase(MapUtil.getStr(item,"menu_name"))).collect(Collectors.toList()));
            menuList.add(tmp2);
        }
        data.put("apps",appList);
        data.put("menus",menuList);
        return R.ok(data);
    }

    @GetMapping("dictTree")
    public R dictTree() throws IOException {
        String sql = "  SELECT dict_type as id,0 as pid,dict_type as code,dict_name as name from sys_dict_type UNION all\n" +
            "SELECT dict_code as id,dict_type as pid,dict_value as code,dict_label as name from sys_dict_data ";
        List<Record> recordList = Db.use().find(sql);
        List<Map<String, Object>> mapList = RecordUtil.recordToMaps(recordList,true);
        mapList = (List) mapList.stream().map(item->{return item;}).collect(Collectors.toList());
        List tree = TreeBuildUtil.listToTree(mapList,"0","id","pid");
        return R.ok(tree);
    }

    @GetMapping("menuTree")
    public R menuTree() throws IOException {
        String sql = " SELECT  m.menu_id AS 'id',  m.parent_id AS 'parentId',  m.menu_name AS 'title',   \n" +
                "m.menu_id AS 'value',  1 AS 'weight',  m.parent_id AS 'pid' \n" +
                "FROM  sys_menu m  WHERE  menu_type != 'F'  ";
        List<Record> recordList = Db.use().find(sql);
        List<Map<String, Object>> mapList = RecordUtil.recordToMaps(recordList);
        mapList = (List) mapList.stream().map(item->{return item;}).collect(Collectors.toList());
        List tree = TreeBuildUtil.listToTree(mapList,"0","id","parentId");
        return R.ok(tree);
    }

    @GetMapping("deptTree")
    public R deptTree() throws IOException {
        String sql = " SELECT  m.dept_id AS 'id',  m.parent_id AS 'parentId',  m.dept_name AS 'title',   \n" +
                "m.dept_id AS 'value',  1 AS 'weight',  m.parent_id AS 'pid' \n" +
                "FROM  sys_dept m  ;  ";
        List<Record> recordList = Db.use().find(sql);
        List<Map<String, Object>> mapList = RecordUtil.recordToMaps(recordList,true);
        mapList = (List) mapList.stream().map(item->{return item;}).collect(Collectors.toList());
        List tree = TreeBuildUtil.listToTree(mapList,"0","id","parentId");
        return R.ok(tree);
    }

}
