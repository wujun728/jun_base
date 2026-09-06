package com.ruoyi.client.annotation;

import com.ruoyi.client.config.AVueAutoConfiguration;
import com.ruoyi.client.config.AVueBeanDefinitionRegistrar;
import com.ruoyi.client.processor.DicDataProcess;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 描述: 开启AVue功能
 *
 * @author liukx
 * @date 2021/8/18 16:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({AVueBeanDefinitionRegistrar.class, AVueAutoConfiguration.class})
public @interface EnableAVue {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
     * declarations e.g.: {@code @ComponentScan("org.my.pkg")} instead of
     * {@code @ComponentScan(basePackages="org.my.pkg")}.
     *
     * @return the array of 'basePackages'.
     */
    String[] value() default {};

    /**
     * Base packages to scan for annotated components.
     * <p>
     * {@link #value()} is an alias for (and mutually exclusive with) this attribute.
     * <p>
     * Use {@link #basePackageClasses()} for a type-safe alternative to String-based
     * package names.
     *
     * @return the array of 'basePackages'.
     */
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to
     * scan for annotated components. The package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that
     * serves no purpose other than being referenced by this attribute.
     *
     * @return the array of 'basePackageClasses'.
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * 枚举包扫描
     *
     * @return
     */
    String[] enumsPackages() default {};

    /**
     * 枚举类
     *
     * @return
     */
    Class<?>[] enumsPackagesClasses() default {};

}
