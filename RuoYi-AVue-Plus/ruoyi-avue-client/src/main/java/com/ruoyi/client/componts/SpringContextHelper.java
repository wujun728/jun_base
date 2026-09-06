package com.ruoyi.client.componts;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/9/9 - 13:10
 */
@Component
public class SpringContextHelper implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public String parsePropertiesString(String value) {
        AtomicReference<String> newValue = new AtomicReference<>();
        newValue.set(value);
        parseProperties(value, (result) -> {
            String property = this.applicationContext.getEnvironment().getProperty(result);
            if (!StringUtils.isEmpty(result)) {
                newValue.set(newValue.get().replaceAll("\\$\\{" + result + "}", property));
            }
        });
        return newValue.get();
    }

    public Map<String, String> parsePropertiesMap(String value) {
        Map<String, String> replaceMap = new HashMap<>();

        parseProperties(value, (result) -> {
            String property = this.applicationContext.getEnvironment().getProperty(result);
            if (!StringUtils.isEmpty(result)) {
                replaceMap.put(result, property);
            }
        });

        return replaceMap;
    }

    private void parseProperties(String value, Consumer<String> consumer) {
        int startIndex = 0;

        while (true) {
            int start = value.indexOf("${", startIndex);
            int end = value.indexOf("}", start);
            if (start < 0 || end < 0) {
                break;
            }
            String result = value.substring(start + 2, end);

            consumer.accept(result);

            startIndex = end;
        }
    }
}
