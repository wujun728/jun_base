# lime-drag 拖拽排序
- 当前为初版 可能会有BUG
- 基于uniapp vue3
- Q群 1169785031


### 安装
- 在市场导入插件即可在任意页面使用，无须再`import`


### 使用
- 提供简单的使用示例，更多请查看下方的demo

```html
<l-drag :list="list" @change="change">
	<!-- // 每一项的插槽 grid 的 content 您传入的数据 -->
	<template #grid="{active, content}">
		<!-- // grid.active 是否为当前拖拽项目 根据自己需要写样式 -->
		<view class="inner" :class="{active: active}">
			<text class="text" :class="{'text-active': active}">{{grid.content}}</text>
		</view>
	</template>
</l-drag> 
```

```js
const list = new Array(7).fill(0).map((v,i) => i);
// 拖拽后新的数据
const newList = ref([])
const change = v => newList.value = v
```
#### 增删
- 不要给list赋值，这样只会重新初始化
- 增加数据 调用暴露的`push`
- 删除某条数据调用暴露的`remove`方法，需要传入`oindex`

```html
<l-drag :list="list" @change="change" ref="dragRef" after remove>
	<!-- 每一项插槽 grid 的 content 是您传入的数据 -->
	<template #grid="{active, index, oldindex, oindex}">
		<!-- active 是否为当前拖拽项目 根据自己需要写样式 -->
		<!-- index 排序后列表下标 -->
		<!-- oldindex 排序后列表旧下标 -->
		<!-- oindex 列表原始下标，输入的数据排位不会因为排序而改变 -->
		<view class="remove" @click="onRemove(oindex)"></view>
		<view class="inner" :class="{active}">
			<text class="text" :class="{'text-active': active}">{{content}}</text>
		</view>
	</template>
	<template #after>
		<view class="grid">
			<view class="inner extra" @click="onAdd">
				增加
			</view>
		</view>
	</template>
</l-drag> 
```
```js
const dragRef = ref(null)
const list = new Array(7).fill(0).map((v,i) => i);
const onAdd = () => {
	dragRef.value.push(Math.round(Math.random() * 1000))
}
const onRemove = (oindex) => {
	if(dragRef.value && oindex >= 0) {
		// 记得oindex为数组的原始index
		dragRef.value.remove(oindex)
	}
}
```


#### 插槽
```html
<l-drag :list="list">
	<!-- 每一项的插槽 -->
	<template #grid="{active, index, oldindex, oindex, content}"></template>
	<!-- 当前拖拽项幽灵插槽 设置`ghost`后使用 主要为实现拖拽时 有个影子跟着 -->
	<template #ghots></template>
	
	<!-- 前后方插槽为固定在列表前方和后方，不能拖动 -->
	<!-- 列表前方的插槽 设置`before`后使用 -->
	<template #before></template>
	<!-- 列表后方的插槽 设置`after`后使用 -->
	<template #after></template>
</l-drag> 
```


### 查看示例
- 导入后直接使用这个标签查看演示效果

```html
<!-- // 代码位于 uni_modules/lime-drag/compoents/lime-drag -->
<lime-drag />
```


### 插件标签
- 默认 l-drag 为 component
- 默认 lime-drag 为 demo

### 关于vue2的使用方式
- 插件使用了`composition-api`, 如果你希望在vue2中使用请按官方的教程[vue-composition-api](https://uniapp.dcloud.net.cn/tutorial/vue-composition-api.html)配置
- 关键代码是: 在main.js中 在vue2部分加上这一段即可,官方是把它单独成了一个文件.
```js
// vue2
import Vue from 'vue'
import VueCompositionAPI from '@vue/composition-api'
Vue.use(VueCompositionAPI)
```

- 另外插件也用到了TS，vue2可能会遇过官方的TS版本过低的问题,找到HX目录下的`compile-typescript`目录
```cmd
// \HBuilderX\plugins\compile-typescript
yarn add typescript -D
- or - 
npm install typescript -D
```

- 小程序需要在`manifest.json`启用`slotMultipleInstance`
```json
"mp-weixin" : {
    "slotMultipleInstance" : true
}
```


## API

### Props

| 参数                       | 说明                                                         | 类型             | 默认值       |
| --------------------------| ------------------------------------------------------------ | ---------------- | ------------ |
| list                      | 列表数组，不可变化，变化后会重新初始化                                                      | <em>array</em>  | `[]`     |
| column                    | 列数                  | <em>number</em>  | `2` |
| gridHeight               | 行高,宫格高度                 								| <em>string</em>  | `120rpx` |
| damping               	| 阻尼系数，用于控制x或y改变时的动画和过界回弹的动画，值越大移动越快         				| <em>string</em>  | `-` |
| friction               	| 摩擦系数，用于控制惯性滑动的动画，值越大摩擦力越大，滑动越快停止；必须大于0，否则会被设置成默认值         				| <em>number</em>  | `2` |
| extraRow               	| 额外行数        				| <em>number</em>  | `0` |
| ghost               	| 开启幽灵插槽        				| <em>boolean</em>  | `false` |
| before               	| 开启列前插槽        				| <em>boolean</em>  | `false` |
| after               	| 开启列后插槽        				| <em>boolean</em>  | `false` |
| disabled              | 是否禁用        				| <em>boolean</em>  | `false` |
| longpress              | 是否长按        				| <em>boolean</em>  | `false` |

### Events
| 参数                       | 说明                                                         | 参数             | 
| --------------------------| ------------------------------------------------------------ | ---------------- |
| change              		| 返回新数据  | list | 

### Expose
| 参数                       | 说明                                                         | 参数             | 
| --------------------------| ------------------------------------------------------------ | ---------------- |
| remove              		| 删除， 传入`oindex`,即数据列表原始的index  |  | 
| push             			| 向后增加，可以是数组或单数据  |  | 
| unshift             	    | 向前增加，可以是数组或单数据  |  | 
| move             			| 移动, 传入(`oindex`, `toindex`),将数据列表原始的index项移到视图中的目标位置  |  | 


### TODO
将来实现的功能
- splice

## 打赏

如果你觉得本插件，解决了你的问题，赠人玫瑰，手留余香。  
![](https://testingcf.jsdelivr.net/gh/liangei/image@1.9/alipay.png)
![](https://testingcf.jsdelivr.net/gh/liangei/image@1.9/wpay.png)