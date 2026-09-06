package com.ruoyi.client.helper;

import com.ruoyi.client.enums.AVueAttrLevel;
import com.ruoyi.client.processor.AVueAttrPostProcess;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Map;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/17 - 16:22
 */
@Component
public class AttrPostProcessHelper implements InitializingBean {

    @Autowired(required = false)
    private List<AVueAttrPostProcess> attrProcesses;

    public void handler(AVueAttrLevel level, AnnotatedElement element, Map<String, Object> attrMap) {
        if (attrProcesses != null && attrMap.size() > 0) {
            for (AVueAttrPostProcess process : attrProcesses) {
                process.attrPostProcess(level, element, attrMap);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
