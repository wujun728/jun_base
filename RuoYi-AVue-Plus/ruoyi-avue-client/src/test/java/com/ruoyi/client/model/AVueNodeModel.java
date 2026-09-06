package com.ruoyi.client.model;

import com.ruoyi.client.annotation.AVueRule;
import com.ruoyi.client.annotation.column.AVueInput;
import com.ruoyi.client.annotation.column.AVueNumber;
import com.ruoyi.client.annotation.column.AVueSelect;
import com.ruoyi.client.annotation.column.AVueSwitch;

/**
 * 子对象处理
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/30 - 17:18
 */
public class AVueNodeModel {
    @AVueInput(prop = "id", label = "主键", addDisplay = false, row = true, editDisabled = true)
    private String id;

    @AVueInput(prop = "configGroup", label = "组名称", row = true, rules = {@AVueRule(required = true, message = "组名称要填咧"),
        @AVueRule(min = 5, max = 10, message = "我跟你讲最小5个,最大10个.")})
    private String configGroup;

    @AVueInput(prop = "configName", label = "配置名称", required = true, row = true)
    private String configName;

    @AVueSelect(prop = "configCode", label = "配置值", dicData = "StatusEnums", required = true, row = true)
    private String configCode;

    @AVueNumber(prop = "validDay", label = "有效天数", required = true, row = true)
    private Integer validDay;

    @AVueSwitch(prop = "status", label = "状态", dicData = "StatusEnums", row = true)
    private int status;
}
