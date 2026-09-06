package com.ruoyi.client.annotation.column;

import com.ruoyi.client.annotation.AVueAttr;
import com.ruoyi.client.annotation.AVueRule;

import java.lang.annotation.*;

/**
 * 描述: 树型下拉框
 *
 * @author liukx
 * @date 2021/8/16 18:51
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AVueTree {

    /**
     * 默认的类型
     *
     * @return
     */
    String type() default "tree";

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
     * 多选
     *
     * @return
     */
    boolean multiple() default false;

    /**
     * 多选时是否将选中值按文字的形式展示
     *
     * @return
     */
    boolean tags() default false;

    /**
     * 在显示复选框的情况下，是否严格的遵循父子不互相关联的做法
     *
     * @return
     */
    boolean checkStrictly() default false;

    /**
     * 是否每次只打开一个同级树节点展开
     *
     * @return
     */
    boolean accordion() default false;

    /**
     * 是否默认展开所有节点
     *
     * @return
     */
    boolean defaultExpandAll() default false;

    /**
     * 是否在点击节点的时候展开或者收缩节点,如果为 false，则只有点箭头图标的时候才会展开或者收缩节点
     *
     * @return
     */
    boolean expandOnClickNode() default false;

    /**
     * 默认展开的节点的 key 的数组
     *
     * @return
     */
    String[] defaultExpandedKeys() default {};

    /**
     * 默认勾选的节点的 key 的数组
     *
     * @return
     */
    String[] defaultCheckedKeys() default {};

    /**
     * 是否开启条件筛选
     *
     * @return
     */
    boolean filters() default true;

    /**
     * 条件筛选提示语
     *
     * @return
     */
    String filterText() default "请输入搜索关键字";

    /**
     * 条件筛选提示语
     *
     * @return
     */
    String iconClass() default "";

    /**
     * 子类全选时勾选值是否包含父类
     *
     * @return
     */
    boolean leafOnly() default true;

    /**
     * 父类是否可以点击和选择
     *
     * @return
     */
    boolean parent() default true;

    /**
     * 条件筛选提示语
     *
     * @return
     */
    boolean lazy() default false;

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
