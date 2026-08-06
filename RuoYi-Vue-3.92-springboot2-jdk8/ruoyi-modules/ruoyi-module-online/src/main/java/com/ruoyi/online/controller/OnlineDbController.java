package com.ruoyi.online.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.online.mapper.OnlineDbMapper;

/**
 * mysql数据库Controller接口
 * 
 * @author Dftre
 * @date 2024-01-26
 */
@RestController
@RequestMapping("/online/db")
@Anonymous
public class OnlineDbController extends BaseController {

    @Autowired
    private OnlineDbMapper onlineDbMapper;

    @GetMapping("/table/list")
    public TableDataInfo selectDbTableList(BaseEntity baseEntity){
        startPage();
        return getDataTable(onlineDbMapper.selectDbTableList(baseEntity));
    }

    @GetMapping("/column/list")
    public TableDataInfo selectDbColumnsListByTableName(String tableName){
        return getDataTable(onlineDbMapper.selectDbColumnsListByTableName(tableName));
    }
}
