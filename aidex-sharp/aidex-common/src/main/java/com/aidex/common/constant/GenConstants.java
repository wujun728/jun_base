package com.aidex.common.constant;

/**
 * 代码生成通用常量
 *
 * @author ruoyi
 */
public class GenConstants {
    /**
     * 单表（增删改查）
     */
    public static final String TPL_CRUD = "crud";

    /**
     * 单表行编辑（增删改查）
     */
    public static final String TPL_CRUD_ROW_EDIT = "crudrowedit";

    /**
     * 树表（增删改查）
     */
    public static final String TPL_TREE = "tree";

    /**
     * 主子表左右布局（增删改查）
     */
    public static final String TPL_SUB = "sub";


    /**
     * 主子表嵌套（增删改查）
     */
    public static final String TPL_SUB_NESTED = "subnested";

    /**
     * 树（增删改查）
     */
    public static final String TPL_TREEGRID = "treegrid";

    /**
     * 树编码字段
     */
    public static final String TREE_CODE = "treeCode";

    /**
     * 树父编码字段
     */
    public static final String TREE_PARENT_CODE = "treeParentCode";

    /**
     * 树名称字段
     */
    public static final String TREE_NAME = "treeName";

    /**
     * 上级菜单ID字段
     */
    public static final String PARENT_MENU_ID = "parentMenuId";

    /**
     * 上级菜单名称字段
     */
    public static final String PARENT_MENU_NAME = "parentMenuName";

    /**
     * 是否可上传附件配置(1：是；2：否)
     */
    public static final String ATTACH_OPTION = "attachOption";

    /**
     * 是否可上传附件配置(1：是；2：否)
     */
    public static final String DISABLE_ENABLE_OPTION = "disableEnableOption";

    /**
     * 是否保存并继续添加
     */
    public static final String SAVE_AND_ADD_OPTION = "saveAndAddOption";

    /**
     * 是否有复制功能
     */
    public static final String COPY_RECORD_OPTION = "copyRecordOption";

    /**
     * 默认上级菜单，系统工具
     */
    public static final String DEFAULT_PARENT_MENU_ID = "3";

    /**
     * 菜单ICON
     */
    public static final String MENU_ICON = "menuIcon";

    /**
     * 数据库字符串类型
     */
    public static final String[] COLUMNTYPE_STR = {"char", "varchar", "nvarchar", "varchar2"};

    /**
     * 数据库文本类型
     */
    public static final String[] COLUMNTYPE_TEXT = {"tinytext", "text", "mediumtext", "longtext"};

    /**
     * 数据库时间类型
     */
    public static final String[] COLUMNTYPE_TIME = {"datetime", "time", "date", "timestamp"};

    /**
     * 数据库数字类型
     */
    public static final String[] COLUMNTYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "double", "decimal"};

    /**
     * 页面不需要编辑字段
     */
    public static final String[] COLUMNNAME_NOT_EDIT = {"id", "create_by", "create_dept", "create_time", "update_by", "update_time", "update_ip", "remark", "version", "del_flag"};

    /**
     * 表中必须包含的常用字段
     */
    public static final String[] COLUMNNAME_REQUIRE_COLUMN = {"remark", "id", "create_by", "create_dept", "create_time", "update_by", "update_time", "update_ip", "version", "del_flag"};

    /**
     * 页面常用字段
     */
    public static final String[] COLUMNNAME_COMMON_COLUMN = {"tree_sort", "sort", "status", "remark", "id", "parent_id", "parent_ids", "tree_sorts", "tree_level", "tree_leaf", "create_by", "create_dept", "create_time", "update_by", "update_time", "update_ip", "version", "del_flag"};

    /**
     * 页面不需要显示的列表字段
     */
    public static final String[] COLUMNNAME_NOT_LIST = {"parent_id", "parent_ids", "tree_sorts", "tree_level", "tree_leaf", "id", "create_by", "create_dept", "create_time", "update_by", "update_time", "update_ip", "remark", "version", "del_flag"};

    /**
     * 页面不需要查询字段
     */
    public static final String[] COLUMNNAME_NOT_QUERY = {"parentId", "parentIds", "treeSort", "treeSorts", "treeLevel", "treeLeaf", "id", "create_by", "create_time", "del_flag", "update_by", "update_time", "remark"};

    /**
     * Entity基类字段
     */
    public static final String[] BASE_ENTITY = {"id", "createBy", "createDept", "createTime", "updateBy", "updateTime", "updateIp", "remark", "version", "delFlag"};

    /**
     * Tree基类字段
     */
    public static final String[] TREE_ENTITY = {"parentId", "parentIds", "treeSort", "treeSorts", "treeLevel", "treeLeaf", "children"};

    /**
     * 文本框
     */
    public static final String HTML_INPUT = "input";

    /**
     * 数字框
     */
    public static final String HTML_NUMBER = "number";

    /**
     * 文本域
     */
    public static final String HTML_TEXTAREA = "textarea";

    /**
     * 下拉框
     */
    public static final String HTML_SELECT = "select";

    /**
     * 下拉框
     */
    public static final String HTML_SELECT_MULTIPLE = "selectMultiple";

    /**
     * 开关
     */
    public static final String HTML_SWITCH = "switch";

    /**
     * 单选框
     */
    public static final String HTML_RADIO = "radio";


    /**
     * 单选框
     */
    public static final String HTML_RADIO_BUTTON = "radioButton";

    /**
     * 复选框
     */
    public static final String HTML_CHECKBOX = "checkbox";


    /**
     * 日期控件
     */
    public static final String HTML_DATETIME = "datetime";

    /**
     * 时间戳控件
     */
    public static final String HTML_DATE = "date";

    /**
     * 图片上传控件
     */
    public static final String HTML_IMAGE_UPLOAD = "imageUpload";

    /**
     * 文件上传控件
     */
    public static final String HTML_FILE_UPLOAD = "fileUpload";

    /**
     * 富文本控件
     */
    public static final String HTML_EDITOR = "editor";

    /**
     * 字符串类型
     */
    public static final String TYPE_STRING = "String";

    /**
     * 整型
     */
    public static final String TYPE_INTEGER = "Integer";

    /**
     * 长整型
     */
    public static final String TYPE_LONG = "Long";

    /**
     * 浮点型
     */
    public static final String TYPE_DOUBLE = "Double";

    /**
     * 高精度计算类型
     */
    public static final String TYPE_BIGDECIMAL = "BigDecimal";

    /**
     * 时间类型
     */
    public static final String TYPE_DATE = "Date";

    /**
     * 模糊查询
     */
    public static final String QUERY_LIKE = "LIKE";

    /**
     * 精确查询
     */
    public static final String QUERY_EQ = "EQ";

    /**
     * 区间查询
     */
    public static final String QUERY_BETWEEN = "BETWEEN";

    /**
     * 需要
     */
    public static final String REQUIRE = "1";

    /**
     * 居中
     */
    public static final String ALIGH_CENTER = "center";

    /**
     * 居右
     */
    public static final String ALIGH_RIGHT = "right";

    /**
     * 居左
     */
    public static final String ALIGH_LEFT = "left";
}
