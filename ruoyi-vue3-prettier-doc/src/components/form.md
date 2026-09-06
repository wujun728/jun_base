<script setup>
import BasicForm from '../.vitepress/theme/SampleCode/UseForm/BasicForm.vue'
import RuleForm from '../.vitepress/theme/SampleCode/UseForm/RuleForm.vue'
import HideForm from '../.vitepress/theme/SampleCode/UseForm/HideForm.vue'
import OriginElementForm from '../.vitepress/theme/SampleCode/UseForm/OriginElementForm.vue'
import LayoutForm from '../.vitepress/theme/SampleCode/UseForm/LayoutForm.vue'
import SlotForm from '../.vitepress/theme/SampleCode/UseForm/SlotForm.vue'
import CustomForm from '../.vitepress/theme/SampleCode/UseForm/CustomForm.vue'
import ElementSlotForm from '../.vitepress/theme/SampleCode/UseForm/ElementSlotForm.vue'
</script>

# 表单组件

## 组件路径

组件路径：`src/BaseComponent/BaseForm`

基于 element-plus 的 [el-form](https://element-plus.org/zh-CN/component/form.html) 组件封装，提供了更便捷的配置式表单。

## 基础表单

::: tip
最基础的表单包括各种输入表单项，比如`input`&nbsp;&nbsp;`select`&nbsp;&nbsp;`radio`&nbsp;&nbsp;`checkbox`&nbsp;&nbsp;`cascader`&nbsp;&nbsp;`datePicker`&nbsp;&nbsp;`inputDropdown`&nbsp;&nbsp;`inputNumber`&nbsp;&nbsp;`textarea`&nbsp;&nbsp;`tree`&nbsp;&nbsp;`treeSelect`&nbsp;&nbsp;`switch`。
:::

<BasicForm></BasicForm>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseForm/BasicForm.vue
:::

::: tip
[W3C](https://www.w3.org/TR/html52/sec-forms.html#implicit-submission) 标准定义:

> 当一个表单中只有一个单行文本输入字段时，浏览器应当将在此字段中按下 Enter（回车键）的行为视为提交表单的请求。`BaseForm`组件已阻止这一默认行为

:::

## 自定义表单

如果你所需要的表单类型不在基础表单的类型中,例如你的表单需要加上颜色选择组件,那么你可以使用`custom`自定义类型。

<CustomForm></CustomForm>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseForm/CustomForm.vue{5-7,25-29}
:::

## 表单校验

使用`rules`属性可以对表单进行校验,具体类型可以参考 el-form 的[rules](https://element-plus.org/zh-CN/component/form.html#form-attributes)属性
<RuleForm></RuleForm>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseForm/RuleForm.vue{22-26}
:::

## 隐藏和 disabled 表单项

- 使用 `hideItems` 属性可以隐藏表单项
- 使用 `allDisabled` 属性可以禁用表单项

按 Enter 回车键添加表单隐藏项
<HideForm></HideForm>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseForm/HideForm.vue{23-24,37-38}
:::

## 使用 element-plus 原本的表单属性

::: tip BaseForm 可以使用 element-plus 中 el-form 和 el-form-item 的所有属性

- 可以在`elFormConfig`属性中使用 element-plus 中[el-form](https://element-plus.org/zh-CN/component/form.html#form-attributes)的所有属性
- 可以在`formItemConfig`属性中使用 element-plus 中[el-form-item](https://element-plus.org/zh-CN/component/form.html#formitem-attributes)的所有属性
- 可以在`config`属性中使用渲染 element-plus 组件时使用的属性,例如 type 为"select"时,渲染出来的就是 el-select 组件,那么就可以在`config`属性中添加 el-select 组件的属性
- 如果使用 select 组件时`options`数组中的格式并不是{label: '',value: ''}形式,那么可以使用`setLabel`和`setValue`属性来指定 label 和 value

  :::

<OriginElementForm></OriginElementForm>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseForm/OriginElementForm.vue{19-22,28-30,36-43}
:::

## 布局

::: tip
BaseForm 中的 formItem 会被`el-row`和 `el-col`包裹, 所以支持 element-plus 中[el-row](https://element-plus.org/zh-CN/component/layout.html#row-api)和[el-col](https://element-plus.org/zh-CN/component/layout.html#col-api)的所有属性
:::

- `colLayout` 用于设置整体的网格布局
- formItem 中的`layout`属性用于设置单个 item 的网格布局

<LayoutForm></LayoutForm>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseForm/LayoutForm.vue{22-28,44-50}
:::

## 使用插槽

BaseForm 一共有五个插槽

- `field+Header`每个 formItem 的头部插槽
- `field+Before`每个 formItem 的前置插槽
- `field+After`每个 formItem 的后置插槽
- `footer`最尾部的插槽
- `field+CustomLabel`自定义 label 例如 label 上还需要加按钮可能就需要这个插槽

<SlotForm></SlotForm>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseForm/SlotForm.vue{4-24}
:::

## 使用 element-plus 组件插槽

BaseForm 中 formItem 支持使用 element-plus 组件插槽

- 在 `slotNames` 属性中加入 element-plus 组件的插槽名称,例如要使用 el-input 的 append 插槽,那么 `slotNames` 属性中就要加入 append

<ElementSlotForm></ElementSlotForm>
::: details 查看源代码
<<< @/.vitepress/theme/SampleCode/UseForm/ElementSlotForm.vue{4-26,45,46}
:::

## 属性

| 属性名       | 说明                                                                                                                                      | 类型         | 默认值                                                                                                                                                       |
| ------------ | ----------------------------------------------------------------------------------------------------------------------------------------- | ------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| elFormConfig | [element-plus 的 Form](https://element-plus.org/zh-CN/component/form.html)组件参数                                                        | `Object`     | {}                                                                                                                                                           |
| allDisabled  | 是否禁用所有操作框                                                                                                                        | `boolean`    | false                                                                                                                                                        |
| formItems    | 需要渲染的 form 项，具体参考下方的 formItem                                                                                               | `Array`      | []                                                                                                                                                           |
| data         | 表单数据                                                                                                                                  | `Object`     | 必填                                                                                                                                                         |
| itemStyle    | 每个 formItem 的样式                                                                                                                      | `Object`     | { <br>&nbsp;&nbsp; padding: '0px &nbsp;&nbsp;&nbsp;20px 0px 0px' <br>}                                                                                       |
| colLayout    | 每个 formItem 布局配置，参考 element-plus 的[Layout 组件](https://element-plus.org/zh-CN/component/layout.html#col-api)                   | `Object`     |                                                                                                                                                              |
| footerLayout | footer 插槽的布局配置，参考 element-plus 的[Layout 组件](https://element-plus.org/zh-CN/component/layout.html#col-api)                    | `Object`     | { <br>&nbsp;&nbsp;xl: 3, // 1920<br>&nbsp;&nbsp;lg: 4, // 1200<br>&nbsp;&nbsp;md: 6, // 992<br>&nbsp;&nbsp;sm: 12, // 768<br>&nbsp;&nbsp;xs: 24 // <768<br>} |
| rowConfig    | 所有 formItem 的布局配置，参考 element-plus 的 Layout 组件中[el-row 的配置](https://element-plus.org/zh-CN/component/layout.html#row-api) | `Object`     | {}                                                                                                                                                           |
| rules        | 表单正则校验，参考 element-plus 的[表单校验](https://element-plus.org/zh-CN/component/form.html#%E8%A1%A8%E5%8D%95%E6%A0%A1%E9%AA%8C)     | `Object`     | {}                                                                                                                                                           |
| hideItems    | 需要隐藏的列(需要用 ref 包裹)                                                                                                             | `ref(Array)` | []                                                                                                                                                           |

## formItem

| 属性名         | 说明                                                                                                | 类型      | 默认值 |
| -------------- | --------------------------------------------------------------------------------------------------- | --------- | ------ |
| field          | data 的键名                                                                                         | `String`  | —      |
| isHidden       | 是否隐藏该列                                                                                        | `Boolean` | false  |
| hideLabel      | 是否隐藏 label                                                                                      | `Boolean` | false  |
| label          | 标签文本                                                                                            | `String`  | —      |
| formItemConfig | 参考 element-plus 的[formItem](https://element-plus.org/zh-CN/component/form.html#formitem-api)配置 | `Object`  | —      |
| type           | 参考下方的 type                                                                                     | `String`  | —      |
| config         | 根据 type 的选择配置不同的 config，可以根据 type 的类型去查看 element-plus 的文档                   | `Object`  | —      |
| eventFunction  | 组件的监听事件，根据 type 的类型去查看 element-plus 的文档                                          | `Object`  | —      |
| slotNames      | 根据 type 类型去查看对应 element-plus 组件的插槽配置，对外暴露的插槽名称为 field+驼峰的 slotName    | `Array`   | —      |

## type

| 属性名称    | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| input       | 参考 element-plus 的[input](https://element-plus.org/zh-CN/component/input.html)，下面所有的 type 都可参考 element-plus |
| password    | [input](https://element-plus.org/zh-CN/component/input.html) |
| inputNumber | [input](https://element-plus.org/zh-CN/component/input-number.html) |
| textarea    | [Input](https://element-plus.org/zh-CN/component/input.html#%E6%96%87%E6%9C%AC%E5%9F%9F) |
| cascader    | [cascader](https://element-plus.org/zh-CN/component/cascader.html) |
| select      | [select](https://element-plus.org/zh-CN/component/select.html) |
| tree        | [Tree](https://element-plus.org/zh-CN/component/tree.html)   |
| treeSelect  | [TreeSelect](https://element-plus.org/zh-CN/component/tree-select.html) |
| datepicker  | [DatePicker](https://element-plus.org/zh-CN/component/date-picker.html) |
| switch      | [switch](https://element-plus.org/zh-CN/component/switch.html) |
| checkBox    | checkbox 组件，具体使用可以查看 BaseForm 的源代码，很容易看懂，路径：`src/BaseComponent/BaseForm/src/cpn/src/CheckBox/CheckBox.vue` |
| radio       | radio，具体使用可以查看 BaseForm 的源代码，很容易看懂，路径：`src/BaseComponent/BaseForm/src/cpn/src/Radio/Radio.vue` |
| custom      | 自定义类型，当 type='custom'时会向外部暴露一个插槽，插槽名称为 field+Custom |

## 插槽

| 插槽名            | 说明                                                     |
| ----------------- | -------------------------------------------------------- |
| field+CustomLabel | 自定义 label 例如 label 上还需要加按钮可能就需要这个插槽 |
| field+Header      | 每个 formItem 的头部插槽                                 |
| field+Before      | 每个 formItem 的前置插槽                                 |
| field+After       | 每个 formItem 的后置插槽                                 |
| footer            | 最尾部的插槽                                             |
