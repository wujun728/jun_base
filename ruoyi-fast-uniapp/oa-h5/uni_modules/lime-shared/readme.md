# lime-shared 工具库
- 本人插件的几个公共函数

### 引入
```js
// 按需引入
// 这种只会引入相关的方法
import {getRect} from '@/uni_modules/lime-shared/getRect'

// 全量引入
// 这种引入方式，会全量打包
import {getRect} from '@/uni_modules/lime-shared'
```


#### getRect
- 返回节点尺寸信息

```js
// 组件内需要传入上下文
// 如果是nvue 则需要在节点上加与id或class同名的ref
getRect('#id',{context: this}).then(res => {})
```

#### addUnit 
- 将未带单位的数值添加px，如果有单位则返回原值

```js
addUnit(10)
// 10px
```

#### unitConvert
- 将带有rpx|px的字符转成number,若本身是number则直接返回

```js
unitConvert('10rpx') 
// 5 设备不同 返回的值也不同
unitConvert('10px') 
// 10
unitConvert(10) 
// 10
```

#### canIUseCanvas2d
- 环境是否支持使用 canvas 2d

```js
canIUseCanvas2d()
// 若支持返回 true 否则 false
```


#### getCurrentPage
- 获取当前页

```js
const page = getCurrentPage()
```


#### base64ToPath
- 把base64的图片转成临时路径

```js
base64ToPath(`xxxxx`).then(res => {})
```

#### pathToBase64
- 把图片的临时路径转成base64

```js
pathToBase64(`xxxxx/xxx.png`).then(res => {})
```

#### sleep
- 睡眠，让 async 内部程序等待一定时间后再执行

```js
async next () => {
	await sleep(300)
	console.log('limeui');
}
```

#### isBase64
- 判断字符串是否为base64

```js
isBase64('xxxxx')
```

#### throttle
- 节流

```js
throttle((nama) => {console.log(nama)}, 200)('limeui');
```

#### debounce
- 防抖

```js
debounce((nama) => {console.log(nama)}, 200)('limeui');
```

#### random
- 返回指定范围的随机数

```js
random(1, 5);
```

#### range
- 生成区间数组

```js
range(0, 5)
// [0,1,2,3,4,5]
```

### clamp
- 夹在min和max之间的数值，如小于min，返回min, 如大于max，返回max，否侧原值返回

```js
clamp(0, 10, -1)
// 0
clamp(0, 10, 11)
// 10
clamp(0, 10, 9)
// 9
```

#### fillZero
- 补零，如果传入的是`个位数`则在前面补0

```js
fillZero(9);
// 09
```

#### exif
- 获取图片exif
- 支持临时路径、base64

```js
uni.chooseImage({
	count: 1, //最多可以选择的图片张数
	sizeType: "original",
	success: (res) => {
		exif.getData(res.tempFiles[0], function() {
			let tagj = exif.getTag(this, "GPSLongitude");
			let	Orientation = exif.getTag(this, 'Orientation');  
			console.log(tagj, Orientation)
		})
	}
})
```

#### selectComponent
- 获取页面或当前实例的指定组件，会在页面或实例向所有的节点查找（包括子组件或子子组件）
- 仅vue3，vue2没有测试过

```js
// 当前页面
const page = getCurrentPage()
selectComponent('.custom', {context: page}).then(res => {
})
```


### createAnimation
- 创建动画，与uni.createAnimation使用方法一致，只为了抹平nvue

```html
<view ref="ball" :animation="animationData"></view>
```
```js
const ball = ref(null)
const animation = createAnimation({
  transformOrigin: "50% 50%",
  duration: 1000,
  timingFunction: "ease",
  delay: 0
})

animation.scale(2,2).rotate(45).step()
// nvue 无导出数据，这样写只为了平台一致，
// nvue 需要把 ref 传入，其它平台不需要
const animationData = animation.export(ball.value)
```