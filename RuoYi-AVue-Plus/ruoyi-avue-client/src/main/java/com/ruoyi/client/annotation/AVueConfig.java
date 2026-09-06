package com.ruoyi.client.annotation;

import java.lang.annotation.*;

/**
 * 描述: 后端服务路径配置
 *
 * @author liukx
 * @date 2021/8/16 18:51
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AVueConfig {
    /**
     * 列表的服务路径
     *
     * @return
     */
    String list();

    /**
     * 保存的服务请求路径
     *
     * @return
     */
    String save() default "";

    /**
     * 修改的服务路径
     *
     * @return
     */
    String update() default "";

    /**
     * 删除的服务路径
     *
     * @return
     */
    String del() default "";

    /**
     * 接口的消息字段
     *
     * @return
     */
    String messageField() default "";

    /**
     * 接口的标识成功的字段名称
     *
     * @return
     */
    String successField() default "";

    /**
     * 接口的成功关键字
     *
     * @return
     */
    String successKeyword() default "";

    AVueAttr[] attrExt() default {};
}
