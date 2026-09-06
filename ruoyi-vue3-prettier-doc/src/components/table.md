<script setup>
import BasicTable from '../.vitepress/theme/SampleCode/UseTable/BasicTable.vue'
import UseTable from '../.vitepress/theme/SampleCode/UseTable/UseTable.vue'
import ElementTable from '../.vitepress/theme/SampleCode/UseTable/ElementTable.vue'
import ElTableEvent from '../.vitepress/theme/SampleCode/UseTable/ElTableEvent.vue'
import MergesTable from '../.vitepress/theme/SampleCode/UseTable/MergesTable.vue'
</script>

# 表格组件

# BaseTable

## 组件路径

组件路径：`src/BaseComponent/BaseTable`

基于 element-plus 的 [el-table](https://element-plus.org/zh-CN/component/table.html) 组件封装，提供了更便捷的配置式表格。

可以使用`el-table`的所有功能。

## 基础表格

基础的表格展示用法。<br>
向`BaseTabel`传入`dataList`对象数组后，再传入`tableItem`对象数组，`tableItem`中的对象`prop`属性来对应对象中的键名即可填入数据，用`label`属性来定义表格的列名。

<BasicTable></BasicTable>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseTable/BasicTable.vue{25-41}
:::

## 使用 BaseTable 的属性

<UseTable></UseTable>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseTable/UseTable.vue{15-21,48-71}
:::

::: tip 默认配置

- 斑马纹效果可通过`elTableConfig.stripe`控制，默认`true`
- 分页可通过 `pagination` 控制，默认分页为每页 50 条数据
- 列表选择可通过`showChoose`控制，默认`false`
- 序号可通过`showIndex`控制，默认`false`
- 对齐方式可通过`align`控制，默认`center`
- 列表可展开可通过`showExpand`控制，`showExpand`为`true`时可以使用`expand`插槽，默认`false`

:::

## 使用 el-table 和 el-table-column 的属性

<ElementTable></ElementTable>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseTable/ElementTable.vue{36-42,52-55}
:::

::: tip

- 可以在`elTableConfig`中使用 el-table 的属性
- 可以在每个`item`中使用 el-table-column 的属性

:::

## 使用 el-table 的事件

<ElTableEvent></ElTableEvent>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseTable/ElTableEvent.vue{26-31}
:::

::: tip

- 可以在`tableListener`中使用 el-table 的的事件

:::

## 合并表头和自定义表头

<MergesTable></MergesTable>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseTable/MergesTable.vue{4-8,19,24-52}
:::

::: tip

- 可以使用`merges`属性让表头合并

:::

## 属性

| 属性名           | 说明                                                      | 类型             | 默认值                                    |
| ---------------- | --------------------------------------------------------- | ---------------- | ----------------------------------------- |
| dataList         | 表数据                                                    | `Array`          | []                                        |
| tableItem        | 参考下方的 tableItem                                      | `Array`          | []                                        |
| tableListener    | table 的监听事件参考 element-plus 的 table 组件的监听事件 | `Object`         | {}                                        |
| showChoose       | 是否展示复选框                                            | `Boolean`        | false                                     |
| showIndex        | 是否展示序号                                              | `Boolean`        | false                                     |
| pagination       | 是否显示分页                                              | `Boolean`        | true                                      |
| listCount        | 总条数                                                    | `Number`         | 0                                         |
| paginationInfo   | 分页的页码和偏移                                          | `Object`         | { }                                       |
| elTableConfig    | element-plus 的 table 组件的参数配置                      | `Object`         | {}                                        |
| showExpand       | 列表是否可展开                                            | `Boolean`        | false                                     |
| align            | 列布局，参考 element-plus 的 align                        | `String`         | center                                    |
| paginationLayout | 参考 element-plus 中 pagination 的 layout 属性            | `String`         | 'total, sizes, prev, pager, next, jumper' |
| hideItems        | 需要隐藏的列(需要用 ref 包裹)                             | `ref(Array)`     | []                                        |
| maxHeight        | 参考 element-plus 的 table 的 max-height                  | `Number, String` |                                           |
| selectionConfig  | element-plus 的 table 复选框的配置                        | `Object`         | {}                                        |

## tableItem

| 属性名            | 说明                                                                                                            |
| ----------------- | --------------------------------------------------------------------------------------------------------------- |
| 参考 element-plus | 与 element-plus 的 table-column 参数一致                                                                        |
| slotName          | 对应 element-plus 中 Table-column 的 default 插槽                                                               |
| useOwn            | 是否使用 element-plus 的 Table-column 的 header 插槽，如果为 true 时会向外暴露一个名称为 slotName+Header 的插槽 |
