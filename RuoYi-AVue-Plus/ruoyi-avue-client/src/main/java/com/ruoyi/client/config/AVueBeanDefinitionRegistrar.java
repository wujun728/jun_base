package com.ruoyi.client.config;

import com.ruoyi.client.annotation.AVueRouteKey;
import com.ruoyi.client.annotation.EnableAVue;
import com.ruoyi.client.attrDate.DicEnumCallback;
import com.ruoyi.client.enums.DicEnumData;
import com.ruoyi.client.helper.DicEnumsHelper;
import com.ruoyi.client.helper.RouteKeyHelper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * AVue的实例注册
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/8/18 - 17:13
 */
public class AVueBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private ClassPathScanningCandidateComponentProvider provider =
        new ClassPathScanningCandidateComponentProvider(false);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes =
            importingClassMetadata.getAnnotationAttributes(EnableAVue.class.getName());

        Set<String> basePackages = getBasePackages(annotationAttributes);
        provider.addIncludeFilter(new AnnotationTypeFilter(AVueRouteKey.class));
        for (String basePackage : basePackages) {
            Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents(basePackage);

            beanDefinitions.forEach(beanDefinition -> {
                try {
                    Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                    String value = clazz.getAnnotation(AVueRouteKey.class).groupKey();
                    RouteKeyHelper.getInstance().register(value, clazz);
                } catch (ClassNotFoundException e) {

                }
            });
        }


        Set<String> enumPackages = getEnumPackages(annotationAttributes);

        provider.resetFilters(true);

        provider.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            String superClassName = metadataReader.getClassMetadata().getSuperClassName();
            return superClassName != null && superClassName.equals("java.lang.Enum");
        });


        for (String enumPackage : enumPackages) {
            Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents(enumPackage);
            beanDefinitions.forEach(beanDefinition -> {
                try {
                    Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                    DicEnumsHelper.getINSTANCE().register((Class<? extends Enum>)clazz);
                } catch (ClassNotFoundException e) {

                }
            });
        }
    }

    protected Set<String> getEnumPackages(Map<String, Object> attributes) {

        Set<String> basePackages = new HashSet<>();

        for (String pkg : (String[])attributes.get("enumsPackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[])attributes.get("enumsPackagesClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        return basePackages;
    }


    protected Set<String> getBasePackages(Map<String, Object> attributes) {

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[])attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[])attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[])attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        return basePackages;
    }
}
