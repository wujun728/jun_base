package com.ruoyi.client.annotation;

import com.ruoyi.client.enums.AVueAttrLevel;

import java.lang.annotation.*;

/**
 * 描述:  这里应对框架中封装的不够的属性,交给用户自定义K,V
 *
 * @author liukx
 * @date 2021/8/16 18:51
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AVueRule {

    boolean required() default false;

    String type() default "string";

    String message() default "";

    String pattern() default "";

    int min() default Integer.MIN_VALUE;

    int max() default Integer.MAX_VALUE;

    int len() default Integer.MAX_VALUE;

    String[] enums() default {};

    //    Class<? extends AVueAttr>[] children() default {};

}
