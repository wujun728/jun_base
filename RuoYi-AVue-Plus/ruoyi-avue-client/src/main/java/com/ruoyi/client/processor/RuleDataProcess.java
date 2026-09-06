package com.ruoyi.client.processor;

import com.ruoyi.client.annotation.AVueRule;
import com.ruoyi.client.annotation.column.*;
import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.enums.AVueConstants;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 校验规则执行
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/25 - 10:18
 */
@Component
public class RuleDataProcess implements AVueAttrPostProcess {

    private static Map<Class<? extends Annotation>, String> convertMap = new HashMap<>();

    private String required = "required";

    private String searchRequired = AVueConstants.SEARCH_REQUIRED;

    static {
        convertMap.put(AVueDatetime.class, "date");
        convertMap.put(AVueDatetimeRange.class, "date");
        convertMap.put(AVueDateRange.class, "date");
        convertMap.put(AVueTime.class, "date");
        convertMap.put(AVueTimeRange.class, "date");
        convertMap.put(AVueNumber.class, "number");
    }

    @Override
    public void attrPostProcess(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap) {
        if (level == AVueAttrLevel.OPTION_COLUMN) {
            // 必填属性
            String rulesName = AVueAttrLevel.RULES.getKeyName();
            Object require = attrMap.get(required);
            if (require != null) {
                Object ruleObject = attrMap.get(rulesName);
                builderRules(attrMap, rulesName, ruleObject, true);
                attrMap.remove(required);
            }

            String search = AVueAttrLevel.SEARCH.getKeyName();
            String searchRulesName = AVueAttrLevel.SEARCH_RULES.getKeyName();
            Object isSearch = attrMap.get(search);
            if (isSearch != null) {
                Object isSearchRequired = attrMap.get(searchRequired);
                Object searchRules = attrMap.get(searchRulesName);
                if (isSearchRequired != null) {
                    builderRules(attrMap, searchRulesName, searchRules, true);
                } else {
                    // 如果有必填项，并且又是搜索项目，那么默认的就是搜索必填，但是如果没有的话，需要手动指定非必填
                    builderRules(attrMap, searchRulesName, searchRules, false);
                }
            }
        }
    }

    private void builderRules(Map<String, Object> attrMap, String ruleName, Object ruleObject, boolean required) {
        if (ruleObject == null || ruleObject instanceof AVueRule[] && ((AVueRule[])ruleObject).length == 0) {
            Map<String, Object> defaultValidator = getDefaultRequiredValidator(attrMap, required);
            attrMap.put(ruleName, Arrays.asList(defaultValidator));
        } else {
            Map<String, Object> defaultValidator = getDefaultRequiredValidator(attrMap, required);
            ((List)ruleObject).add(defaultValidator);
        }
    }

    private Map<String, Object> getDefaultRequiredValidator(Map<String, Object> attrMap, boolean required) {
        Map<String, Object> ruleConfigMap = new HashMap<>();
        ruleConfigMap.put("required", required);
        if (required) {
            ruleConfigMap.put("message", attrMap.get("label").toString() + " 是必填项!");
        }
        return ruleConfigMap;
    }
}
