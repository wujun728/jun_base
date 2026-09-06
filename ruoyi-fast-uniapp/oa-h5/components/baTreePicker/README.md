## 树形层级选择器
### 简介
为统一样式而生，树形层级选择器，picker弹窗形式的，样式和比例参照uniapp的picker和uni-data-picker组件
* 支持单选、多选、父级选择，当然也支持单层选择
* 支持Object对象属性自定义映射
* 支持显示全部选中、部分选中、未选中三种状态
* 支持快速自定义简单样式（分割线、按钮、标题、对齐等），深入样式可复写css

### 使用方法
在 `script` 中引入组件
``` javascript
	import baTreePicker from "@/components/ba-tree-picker/ba-tree-picker.vue"
	export default {
		components: {
			baTreePicker
		}
```
在 `template` 中使用组件
``` javascript
	<ba-tree-picker ref="treePicker" :multiple='false' @select-change="selectChange" title="选择城市"
		:localdata="listData" valueKey="value" textKey="label" childrenKey="children" />
```
在 `script` 中定义打开方法，和选择监听
``` javascript
		methods: {
			// 显示选择器
			showPicker() {
				this.$refs.treePicker._show();
			},
			//监听选择（ids为数组）
			selectChange(ids, names) {
				console.log(ids, names)
			}
		}
```
在 `template` 中调用打开
``` javascript
	<view @click="showPicker">调用选择器</view>
```

### 属性
|属性名|类型|默认值|说明|
|:-|:-:|:--:|-:|
|localdata|Array|[]|源数据，目前支持tree结构，后续会考虑支持扁平化结构|
|valueKey|String|id|指定 Object 中 key 的值作为节点数据id|
|textKey|String|name|指定 Object 中 key 的值作为节点显示内容|
|childrenKey|String|children|指定 Object 中 key 的值作为节点子集|
|multiple|Boolean|false|是否多选，默认单选|
|selectParent|Boolean|true|是否可以选父级，默认可以|
|title|String| |标题|
|titleColor|String||标题颜色|
|confirmColor|String|#0055ff|确定按钮颜色|
|cancelColor|String|#757575|取消按钮颜色|
|switchColor|String|#666|节点切换图标颜色|
|border|Boolean|false|是否有分割线，默认无|



###  数据格式

注意：必须有id、name(id可通过valueKey来配置为其它键值，如value)字段，且唯一

``` json
[
    {
        id: 1,
        name: '公司1',
        children: [{
            id: 11,
            name: '研发部',
            children: [{
                id: 111,
                name: '张三',
                
            },{
                id: 112,
                name: '李四',
                
            }]
        },{
            id: 12,
            name: '综合部',
            
        } ]
    },
    {
        id: 2,
        name: '公司2',
        children: [{
            id: 21,
            name: '研发部',
            
        },{
            id: 22,
            name: '综合部',
            
        },{
            id: 23,
            name: '财务部',
            
        }, ]
    },
    {
        id: 3,
        name: '公司3'
    },
    {
        id: 4,
        name: '公司4',
        children: [{
            id: 41,
            name: '研发部',
            
        }]
    }
]
```
</details>

### 方法
|方法名|参数|默认值|说明|
|:-|:-:|:--:|-:|
|_show()| | |显示选择器|
|_hide()| | |隐藏选择器|
