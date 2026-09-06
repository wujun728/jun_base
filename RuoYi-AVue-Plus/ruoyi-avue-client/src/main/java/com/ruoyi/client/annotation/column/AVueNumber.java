package com.ruoyi.client.annotation.column;

import com.ruoyi.client.annotation.AVueAttr;
import com.ruoyi.client.annotation.AVueRule;

import java.lang.annotation.*;

/**
 * 描述: input属性
 *
 * @author liukx
 * @date 2021/8/16 18:51
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AVueNumber {
    /**
     * 展现类型
     *
     * @return
     */
    String type() default "number";
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
     * 显示边框
     *
     * @return
     */
    int precision() default 2;

    /**
     * 设置计数器允许的最小值
     *
     * @return
     */
    int minRows() default Integer.MIN_VALUE;

    /**
     * 设置计数器允许的最大值
     *
     * @return
     */
    int maxRows() default Integer.MAX_VALUE;

    /**
     * 计数器步长
     *
     * @return
     */
    int step() default 1;

    /**
     * 是否使用控制按钮
     *
     * @return
     */
    boolean controls() default true;

    /**
     * 控制按钮位置
     *
     * @return
     */
    String controlsPosition() default "top";
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
