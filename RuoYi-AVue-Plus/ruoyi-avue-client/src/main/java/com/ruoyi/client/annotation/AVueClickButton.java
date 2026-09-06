package com.ruoyi.client.annotation;

import java.lang.annotation.*;

/**
 * 自定义的事件按钮
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/9/10 - 13:49
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AVueClickButton {

    /**
     * 类型 参考 element-ui -> https://element.eleme.cn/#/zh-CN/component/button
     *
     * @return
     */
    String type() default "";

    /**
     * 标题
     *
     * @return
     */
    String icon() default "";

    /**
     * 按钮大小
     *
     * @return
     */
    String size() default "";

    /**
     * 事件方法
     *
     * @return
     */
    String methodName();

    /**
     * 按钮名称
     *
     * @return
     */
    String btnName();

    /**
     * 该方法触发的字段名称
     *
     * @return
     */
    String[] fieldNames() default {};

    /**
     * 自定义字段名称,该注解跟随触发事件的方法进行指定
     *
     * @return
     */
    AVueAttr[] attrExt() default {};

}
