<script setup>
import LocalIcon from '../.vitepress/theme/SampleCode/UseIcon/LocalIcon.vue'
import ElementIcon from '../.vitepress/theme/SampleCode/UseIcon/ElementIcon.vue'
</script>

# SvgIcon 组件

## 组件路径

组件路径：`src/components/SvgIcon/index.vue`,已全局注册,组件名为`SvgIcon`

## 使用本地 icon 图标

::: tip
如果使用本地 icon 图标，请先在`src/assets/icons/svg`目录下创建图标文件，文件名格式为`**.svg`，然后使用`<SvgIcon iconClass="fileName" />`即可使用。
:::

- size 属性可以调整 icon 的大小
- color 属性可以调整 icon 的颜色
- className 属性可以给 svg 添加类名

<LocalIcon />

::: details 查看源代码

<<< @/.vitepress/theme/SampleCode/UseIcon/LocalIcon.vue

:::

::: tip
ruoyi-vue3-prettier 项目使用了部分[fontawesome](https://fontawesome.com/search?ic=free)的 icon 图,如果需要配套的 icon 可以自行下载。
:::

## 使用 Element Plus 的 icon 图标

使用 element-plus 的 icon 图标只需要在`iconClass`中加入前缀`el-icon-`即可
<ElementIcon />
::: details 查看源代码

<<< @/.vitepress/theme/SampleCode/UseIcon/ElementIcon.vue

:::

## 属性

| 属性名    | 说明        | 类型                       | 默认值 |
| --------- | ----------- | -------------------------- | ------ |
| iconClass | Icon 名称   | `string`                   | 必填   |
| size      | icon 的大小 | ` string` &nbsp; `number ` | 16     |
| color     | icon 的颜色 | `string`                   | ''     |
| className | svg 类名    | `string`                   | ''     |
