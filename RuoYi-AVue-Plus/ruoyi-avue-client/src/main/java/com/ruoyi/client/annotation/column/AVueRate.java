package com.ruoyi.client.annotation.column;

import com.ruoyi.client.annotation.AVueAttr;
import com.ruoyi.client.annotation.AVueRule;

import java.lang.annotation.*;

/**
 * 描述: 评价选择器
 *
 * @author liukx
 * @date 2021/8/16 18:51
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AVueRate {
    /**
     * 展现类型
     *
     * @return
     */
    String type() default "rate";
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
     * 是否显示辅助文字，若为真，则会从 texts 数组中选取当前分数对应的文字内容
     *
     * @return
     */
    boolean showText() default false;

    /**
     * icon 的类名。若传入数组，共有 3 个元素，为 3 个分段所对应的类名；若传入对象，可自定义分段，键名为分段的界限值，键值为对应的类名
     *
     * @return
     */
    String[] inactiveIconClass() default {};

    /**
     * 未选中 icon 的类名
     *
     * @return
     */
    String voidIconClass() default "el-icon-star-off";

    /**
     * 最大分值
     *
     * @return
     */
    int max() default 5;

    /**
     * icon 的颜色。若传入数组，共有 3 个元素，为 3 个分段所对应的颜色；若传入对象，可自定义分段，键名为分段的界限值，键值为对应的颜色
     *
     * @return
     */
    String[] colors() default {"#F7BA2A", "#F7BA2A", "#F7BA2A"};
    /**
     * 是否作为搜索字段
     *
     * @return
     */
    boolean search() default false;
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
