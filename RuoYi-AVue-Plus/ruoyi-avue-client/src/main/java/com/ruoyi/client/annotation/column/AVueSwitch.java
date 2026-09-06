package com.ruoyi.client.annotation.column;

import com.ruoyi.client.annotation.AVueAttr;
import com.ruoyi.client.annotation.AVueRule;

import java.lang.annotation.*;

/**
 * 描述: 开关属性
 *
 * @author liukx
 * @date 2021/8/16 18:51
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AVueSwitch {
    /**
     * 默认的类型
     *
     * @return
     */
    String type() default "switch";
    /**
     * 属性名称
     *
     * @return
     */
    String prop();

    /**
     * 文本描述
     *
     * @return
     */
    String label();

    /**
     * 字段排序
     *
     * @return
     */
    int order() default 0;

    /**
     * 是否单独成行
     *
     * @return
     */
    boolean row() default false;

    /**
     * 是否可以清空选项
     *
     * @return
     */
    boolean clearable() default false;

    /**
     * 是否禁止编辑
     *
     * @return
     */
    boolean disabled() default false;

    /**
     * 文字提示
     *
     * @return
     */
    String tip() default "";

    /**
     * 标题文字提示
     *
     * @return
     */
    String labelTip() default "";

    /**
     * 辅助语: 比如'请选择','请输入'
     *
     * @return
     */
    String placeholder() default "";

    /**
     * 深结构数据绑定取值
     * <p>
     * 比如 : deep.deep.deep.value
     *
     * @return
     */
    String bind() default "";

    /**
     * 是否只读
     * 比如创建时间
     *
     * @return
     */
    boolean readonly() default false;

    /**
     * 是否可见
     *
     * @return
     */
    boolean display() default false;
    /* ********************************** 表单常用属性 ***************************************** */
    /**
     * 比例 12 代表一行的一半，24则铺满整行
     *
     * @return
     */
    int span() default 12;
    /**
     * 表格页面是否展示
     * @return
     */
    boolean hide() default false;
    /**
     * 表单新增时是否禁止
     *
     * @return
     */
    boolean addDisabled() default false;

    /**
     * 表单新增时是否可见
     *
     * @return
     */
    boolean addDisplay() default true;

    /**
     * 表单新增时是否为查看模式
     *
     * @return
     */
    boolean addDetail() default false;

    /**
     * 表单编辑时是否禁止
     *
     * @return
     */
    boolean editDisabled() default false;

    /**
     * 表单编辑时是否可见
     *
     * @return
     */
    boolean editDisplay() default false;

    /**
     * 表单编辑时是否为查看模式
     *
     * @return
     */
    boolean editDetail() default false;

    /**
     * 开启排序
     *
     * @return
     */
    boolean sortable() default false;

    /* ********************************** ********** ******************************************/
    /**
     * 滑块的宽度
     *
     * @return
     */
    int len() default 40;

    /**
     * 打开时所显示图标的类名，设置此项会忽略active-text
     *
     * @return
     */
    String activeIconClass() default "";

    /**
     * 关闭时所显示图标的类名，设置此项会忽略 inactive-text
     *
     * @return
     */
    String inactiveIconClass() default "";

    /**
     * 打开时的背景色
     *
     * @return
     */
    String activeColor() default "#409EFF";

    /**
     * 关闭时的背景色
     *
     * @return
     */
    String inactiveColor() default "#C0CCDA";
    /**
     * 是否作为搜索字段
     *
     * @return
     */
    boolean search() default false;

    /**
     * 本地字典
     *
     * @return
     */
    String dicData() default "";
    /**
     * 拓展字段
     *
     * @return
     */
    AVueAttr[] attrExt() default {};

    /**
     * 校验规则
     *
     * @return
     */
    AVueRule[] rules() default {};

    boolean required() default false;
    /**
     * 查询的单独校验规则
     *
     * @return
     */
    AVueRule[] searchRequireds() default {};

    /**
     * 查询是否必填
     *
     * @return
     */
    boolean searchRequired() default false;
}
