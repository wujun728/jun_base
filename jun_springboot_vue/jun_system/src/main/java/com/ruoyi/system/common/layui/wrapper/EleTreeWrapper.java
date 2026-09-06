package com.ruoyi.system.common.layui.wrapper;

import com.ruoyi.common.core.text.Convert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EleTreeWrapper {
    private static EleTreeWrapper instance = new EleTreeWrapper();
    private EleTreeWrapper(){}
    public static EleTreeWrapper getInstance(){
        if(instance == null){
            instance = new EleTreeWrapper();
        }
        return instance;
    }

    /**
     * 将id,pid形式的返回数据修正成children的形式
     * @param list
     * @return
     */
    public List getTree(List<Map<String, Object>> list,String keyPid,String keyId){
        for(Map<String, Object> child : list){
            List<Map> childList = list.stream().filter(item -> Convert.toStr(item.get(keyPid)).equals(Convert.toStr(child.get(keyId)))).collect(Collectors.toList());
            if(childList!=null && childList.size()>0){
                child.put("isLeaf",false);
                child.put("checked", false);
                child.put("children",childList);
            }else{
                child.put("isLeaf",true);
            }
        }
        list = list.stream().filter(item -> {
            String pid = Convert.toStr(item.get(keyPid));
            String isLeaf = Convert.toStr(item.get("isLeaf"));
            if(pid.equals("0")){
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return list;
    }
}
