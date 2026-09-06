package com.geek.framework.processor;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.geek.common.annotation.Anonymous;

/**
 * 设置Anonymous注解允许匿名访问的url
 * 
 * @author geek
 */
@Configuration
public class PermitAllUrlProperties implements InitializingBean, ApplicationContextAware {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    public static final String ASTERISK = "*";

    private ApplicationContext applicationContext;

    private Set<String> urls = new HashSet<>();

    @Override
    public void afterPropertiesSet() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean("requestMappingHandlerMapping",
                RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.entrySet().stream()
                .flatMap(entry -> {
                    HandlerMethod handlerMethod = entry.getValue();
                    Anonymous method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Anonymous.class);
                    Anonymous controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Anonymous.class);
                    return List.of(
                            new SimpleImmutableEntry<>(entry.getKey(), method),
                            new SimpleImmutableEntry<>(entry.getKey(), controller)).stream();
                })
                .filter(pair -> pair.getValue() != null)
                .map(pair -> pair.getKey().getPatternValues())
                .flatMap(patterns -> Objects.requireNonNull(patterns).stream())
                .map(url -> RegExUtils.replaceAll((CharSequence) url, PATTERN, ASTERISK))
                .forEach(urls::add);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.applicationContext = context;
    }

    public Set<String> getUrls() {
        return urls;
    }
}
