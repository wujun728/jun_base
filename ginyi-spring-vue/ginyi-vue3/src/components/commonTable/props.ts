import {tableActionEnums} from "@/enums/tableActionEnums";

export const definedProps = {
    /**
     * 表格的列
     */
    columns: {
        require: true,
        type: Array<any>
    },
    /**
     * 表格的数据
     */
    dataList: {
        type: Array<any>
    },
    /**
     * 整个组件的尺寸
     */
    size: {
        type: String,
        default: "small"
    },
    /**
     * 表格的列显示中文的字段，如 {label: 年龄, value: age}，则传 label
     */
    labelField: {
        type: String
    },
    /**
     * 每一行数据都要有唯一的 key
     */
    rowKey: {
        require: true,
        type: Function
    },
    /**
     * 操作列宽度
     */
    actionWidth: {
        type: Number,
        default: 250
    },
    /**
     * 操作列的数据
     */
    actionColData: {
        default: () => [],
        type: Array<any>
    },
    /**
     * 分页参数对象
     */
    pagination: {
        type: [Object, Boolean],
        default: () => false,
    },
    /**
     * 是否启用loading加载
     */
    loading: {
        type: Boolean
    },
    /**
     * 横屏滚动区域，启用固定列时必须设置值
     */
    scrollX: {
        type: Number,
        default: 2000
    },
    /**
     * 是否显示操作列
     */
    showActionCol: {
      type: Boolean,
      default: true
    },
    /**
     * 按钮配置
     */
    buttonConfig: {
        type: Object,
        default: () => {
            return {
                addButton: {
                    type: "add",
                    title: "新增数据",
                    actionType: tableActionEnums.ADD,
                    show: true,
                    colorType: "primary",
                },
                editButton: {
                    type: "edit",
                    title: "编辑",
                    actionType: tableActionEnums.EDIT,
                    show: true,
                    colorType: "primary",
                },
                deleteButton: {
                    type: "delete",
                    title: "删除",
                    actionType: tableActionEnums.DELETE,
                    show: true,
                    colorType: "error",
                },
                batchDeleteButton: {
                    type: "batchDelete",
                    colorType: "error",
                    actionType: tableActionEnums.BATCH_DELETE,
                    show: true,
                    title: "批量删除",
                }
            }
        }
    }
}