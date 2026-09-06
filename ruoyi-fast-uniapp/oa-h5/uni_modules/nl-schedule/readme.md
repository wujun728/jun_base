# nl-schedule
日历日程显示组件，用于显示每日行程/会议的组件
- 支持多日程
- 可配置日程颜色
- 多端兼容

## 安装方式
本组件符合easycom规范，HBuilderX 2.5.5起，只需将本组件导入项目，在页面template中即可直接使用，无需在页面中import和注册components。

## 基本使用
通过`list`参数传入日程列表值，该值为一个数组，
- `title` 日程标题
- `startHour` 日程开始时间（必填）
- `endHour` 日程结束时间（必填）
- `color` 日程卡片颜色，16进制色号
- `isShowOverlay` 当该日程已过期，配置改属性为true使日程有一层遮罩，暗一点显示
```vue
<template>
	<view class="schedule-container">
		<nl-schedule :list="scheduleList" @detail="handleDetail" @add="handleAdd"></nl-schedule>
	</view>
</template>
<script>
	export default {
		data() {
			return {
				scheduleList: [
					{ id: 0, title: '日程1', startHour: '0:00', endHour: '2:00', color: '#ff0000', isShowOverlay:true },
					{ id: 1, title: '日程2', startHour: '0:00', endHour: '2:10', color: '#ff0000' , isShowOverlay:true},
					{ id: 2, title: '日程3', startHour: '4:00', endHour: '5:30' },
					{ id: 3, title: '日程4', startHour: '0:00', endHour: '5:30' },
					{ id: 4, title: '日程5', startHour: '4:00', endHour: '5:30' },
					{ id: 5, title: '日程6', startHour: '1:10', endHour: '5:50' },
					{ id: 6, title: '日程7', startHour: '9:00', endHour: '10:00' },
					{ id: 7, title: '日程8', startHour: '9:00', endHour: '10:00' },
					{ id: 8, title: '日程9', startHour: '10:00', endHour: '11:00' },
				],
			}
		},
		methods: {
			handleDetail(item) {
				console.log('点击的历史日程：', item)
			},
			handleAdd(item) {
				console.log('新建日程：', item) // {startHour: '07:15', endHour: '07:45'}
			},
		},
	}
</script>
```

## API
| 属性					| 说明									| 类型				|默认值						|
|----------				|-----------							|-------------------|-----------------			|
| hourHeight			| 一小时的单元格高度(单位px)				|Nubmer				| 50						|
| hourColor				| 小时的字体颜色							|String				|'#999999'					|
| list					| 日程列表数据，见上方"基本使用"说明		|Array				|							|
| showCurrentTimeLine	| 是否显示当前时间线						|Boolean			| true						|
| currentTimeLineColor	| 当前时间线颜色							|String				| '#ff0000'					|
| overlayOpacity		| 过期日程遮罩透明度(取值0-1之间的数字)	|Number				| 0.4						|
| isAllowAddSchedule	| 是否允许新建日程						|Boolean			| true						|
| newScheduleColor		| 新建日程颜色							|String				| '#1678ff'（16进制色号）	|
| newScheduleOpacity	| 新增日程暗淡程度(取值0-1之间的数字)		|Number				| 0.9						|

| 事件名	| 说明			| 回调参数																	|
|--------	|-----------	|-------------------														|
| detail	| 点击显示的日程	| 日程数据																	|
| add		| 点击新增日程	| 日程的开始时间和结束时间对象	 `{startHour: '07:15', endHour: '07:45'}`	|


