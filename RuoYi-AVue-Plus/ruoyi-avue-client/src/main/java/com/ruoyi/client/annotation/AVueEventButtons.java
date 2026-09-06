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
public @interface AVueEventButtons {

    /**
     * 基于行的按钮点击事件
     *
     * @return
     */
    AVueClickButton[] tableRowButtons() default {};

    /**
     * 基于表格左上方的按钮点击事件
     *
     * @return
     */
    AVueClickButton[] tableTopLeftButtons() default {};

    /**
     * 基于表格右上方的按钮点击事件
     *
     * @return
     */
    AVueClickButton[] tableTopRightButtons() default {};

}
