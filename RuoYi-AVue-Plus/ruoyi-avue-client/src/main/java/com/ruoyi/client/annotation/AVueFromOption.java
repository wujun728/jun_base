package com.ruoyi.client.annotation;

import java.lang.annotation.*;

/**
 * 描述: 选项配置
 *
 * @author liukx
 * @date 2021/8/16 18:51
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AVueFromOption {

    /** -------------------------------- 非公共属性 -------------------------------------------------- **/
    /**
     * 标题
     *
     * @return
     */
    String title() default "";

    /**
     * 回车按键触发提交表单
     *
     * @return
     */
    boolean enter() default false;

    /**
     * 弹出表单的标题的统一宽度
     *
     * @return
     */
    int labelWidth() default 110;

    /**
     * 表单操作菜单的显影
     *
     * @return
     */
    boolean menuBtn() default true;

    /**
     * 表单操作菜单栅格占据的列数
     *
     * @return
     */
    int menuSpan() default 24;

    /**
     * 表单菜单按钮的排列方式
     *
     * @return
     */
    String menuPosition() default "center";

    /**
     * 分组表单,放入正常的column配置就行
     *
     * @return
     */
    String[] group() default {};

    /**
     * 表格总控件大小 [medium/small/mini]
     *
     * @return
     */
    String size() default "small";

    /**
     * 重值不清空的字段
     *
     * @return
     */
    String[] clearExclude() default {};

    /**
     * 提交按钮显隐
     *
     * @return
     */
    boolean submitBtn() default true;

    /**
     * 提交按钮文案
     *
     * @return
     */
    String submitText() default "提交";

    /**
     * 清空按钮显隐
     *
     * @return
     */
    boolean emptyBtn() default true;

    /**
     * 数据为空文案
     *
     * @return
     */
    String emptyText() default "暂无数据";

    AVueAttr[] attrExt() default {};
}
