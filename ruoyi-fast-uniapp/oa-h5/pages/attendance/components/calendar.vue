<template>
	<view ref="calContainer" class="cal_con">
		<view class="cal_month">
			<!--日历表头  周一 周二 周三 周四 周五 周六 周日-->
			<view class="cal_m_weeks">
				<view v-for="w in weeks" :key="w" class="cal_m_weeks_cell">
					{{ w }}
				</view>
			</view>
			<!--日历表内容 -->
			<view class="cal_m_days">
				<!-- 每行内容 -->
				<view v-for="(ds, index) in monthData" :key="index" class="cal_m_day_line">
					<template v-if="weekIndex == index || isOneWeek">
						<view v-for="d in ds" :key="d.day" class="cal_m_day_cell" :class="[
                getCellColor(d),
                { today: sel === `${d.month}-${d.day}` },
              ]" @click="mouseenter(d, $event)">

							<view class="itemDay">{{ isToday(d) ? "今" : d.day }}</view>
							<!-- 正常卡 -->
							<view v-if="d.type == 0 && setDataList(d.date).status == 1" class="suc">
							</view>
							<!-- 异常卡 -->
							<view v-if="d.type == 0 && setDataList(d.date).status == 2" class="error">
							</view>
						</view>
					</template>
				</view>
			</view>
		</view>
		<view class="tab" @click="changeShowStatus"></view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		toRefs,
		onMounted,
		defineEmits
	} from "vue";
	// #ifdef APP-PLUS
	const calContainer = ref(null); // 创建一个 ref
	// #endif
	const props = defineProps({
		dateList: {
			type: Array,
			default: () => {
				return [];
			},
		},
	});
	const state = reactive({
		currentMonth: new Date(),
	});
	const {
		dateList
	} = toRefs(props);
	const $emit = defineEmits(["enter", "leave", "changeMonth"]);
	// const dataList = reactive({
	//   datas: [
	//     {
	//       time: "2023-08-10",
	//       typeName: "正常卡",
	//       jobTime: 8,
	//       dType: 1,
	//     },
	//   ],
	// });

	let now = ref(new Date()); //当前时间：Fri Jul 29 2022 09:57:33 GMT+0800 (中国标准时间)
	let year = ref(0);
	let month = ref(0);
	let jobTime = ref([]);
	let sel = ref();
	let weekIndex = ref();
	let isOneWeek = ref(false);
	const weeks = ["日", "一", "二", "三", "四", "五", "六"];
	let monthData = ref([]); //月数据容器
	let currentYear = ref(new Date().getFullYear()); //当前年：2022
	let currentMonth = ref(new Date().getMonth() + 1); //当前月：7
	let currentDay = ref(new Date().getDate()); //当前天：29
	let newYear = ref()
	let newMonth = ref()
	onMounted(async () => {
		await nextTick();

		// #ifdef APP-PLUS
		const container = calContainer.value;
		// #endif
		// #ifndef APP-PLUS
		const container = document.querySelector('.cal_con');
		// #endif
		let startX = 0;
		let endX = 0;

		container.addEventListener('touchstart', (e) => {
			startX = e.touches[0].clientX;
		});

		container.addEventListener('touchend', (e) => {
			endX = e.changedTouches[0].clientX;
			const diff = startX - endX;

			if (diff > 50) {
				// 向左滑动，切换到下一个月
				nextMonth();

			} else if (diff < -50) {
				// 向右滑动，切换到上一个月
				previousMonth();
			}
		});

		setYearMonth(now.value);
		generateMonth(now.value);
		//   getJobTime();
		getThisWeek();
	});

	function nextMonth() {
		state.currentMonth.setMonth(state.currentMonth.getMonth() + 1);
		newMonth.value = state.currentMonth.getMonth() + 1;
		newYear.value = state.currentMonth.getFullYear();
		generateMonth(state.currentMonth);
		sendValueToParent()
		const data = newYear.value + '-' + newMonth.value
		$emit('getMonthReport', data)
	}

	function sendValueToParent() {
		$emit('changeMonth', newYear.value, newMonth.value, );
	}

	function previousMonth() {
		state.currentMonth.setMonth(state.currentMonth.getMonth() - 1);
		newMonth.value = state.currentMonth.getMonth() + 1;
		newYear.value = state.currentMonth.getFullYear();
		generateMonth(state.currentMonth);
		sendValueToParent()
		const data = newYear.value + '-' + newMonth.value
		$emit('getMonthReport', data)
	}

	function getJobTime() {
		dataList.forEach((item) => {


			// check if animal type has already been added to newObj
			if (!jobTime.value[item.typeName]) {
				// If it is the first time seeing this animal type
				// we need to add title and points to prevent errors
				jobTime.value[item.typeName] = {};
				jobTime.value[item.typeName] = 0;
			}
			// add animal points to newObj for that animal type.
			jobTime.value[item.typeName] += item.jobTime;
		});
	}

	function changeShowStatus() {
		isOneWeek.value = !isOneWeek.value;
	}

	function getThisWeek() {
		monthData.value.map((week, i) => {
			week.map((d) => {
				if (d.month == currentMonth.value && d.day == currentDay.value) {
					weekIndex.value = i;
				}
			});
		});
	}
	// 通过输入日期，匹配当天的所有数据
	// 入参格式 value：'2023-07-09'
	function setDataList(value) {
		let object = {};
		const date = dateFormat("YYYY-mm-dd", value);
		dateList.value.forEach((element) => {
			if (element.date == date) {
				object = element;
			}
		});
		return object;
	}

	function setYearMonth(now) {
		year.value = now.getFullYear();
		month.value = now.getMonth() + 1;
	}

	function preYear() {
		let n = now.value;
		let date = new Date(
			n.getFullYear() - 1,
			n.getMonth(),
			n.getDate(),
			n.getHours(),
			n.getMinutes(),
			n.getSeconds(),
			n.getMilliseconds()
		);

		setYearMonthInfos(date);
	}

	function preMonth() {
		let n = now.value;
		let date = new Date(
			n.getFullYear(),
			n.getMonth() - 1,
			n.getDate(),
			n.getHours(),
			n.getMinutes(),
			n.getSeconds(),
			n.getMilliseconds()
		);

		setYearMonthInfos(date);
	}

	function nextYear() {
		let n = now.value;
		let date = new Date(
			n.getFullYear() + 1,
			n.getMonth(),
			n.getDate(),
			n.getHours(),
			n.getMinutes(),
			n.getSeconds(),
			n.getMilliseconds()
		);

		setYearMonthInfos(date);
	}

	// function nextMonth() {
	//   let n = now.value;
	//   let date = new Date(
	//     n.getFullYear(),
	//     n.getMonth() + 1,
	//     n.getDate(),
	//     n.getHours(),
	//     n.getMinutes(),
	//     n.getSeconds(),
	//     n.getMilliseconds()
	//   );

	//   setYearMonthInfos(date);
	// }

	function setYearMonthInfos(date) {
		setYearMonth(date);
		generateMonth(date);
		now.value = date;
		dateChange();
	}

	function generateMonth(date) {
		date.setDate(1);
		// 星期 0 - 6， 星期天 - 星期6
		let weekStart = date.getDay() + 1;
		let endDate = new Date(date.getFullYear(), date.getMonth() + 1, 0);
		let dayEnd = endDate.getDate();
		// 星期 0 - 6， 星期天 - 星期6
		let weeEnd = endDate.getDay();
		let milsStart = date.getTime();
		let dayMils = 24 * 60 * 60 * 1000;
		let milsEnd = endDate.getTime() + dayMils;

		let monthDatas = [];
		let current;
		// 上个月的几天
		for (let i = 1; i < weekStart; i++) {
			current = new Date(milsStart - (weekStart - i) * dayMils);
			monthDatas.push({
				type: -1,
				date: current,
				fullYear: current.getFullYear(),
				month: current.getMonth() + 1,
				day: current.getDate(),
			});
		}
		// 当前月
		for (let i = 0; i < dayEnd; i++) {
			current = new Date(milsStart + i * dayMils);
			monthDatas.push({
				type: 0,
				date: current,
				fullYear: current.getFullYear(),
				month: current.getMonth() + 1,
				day: current.getDate(),
			});
		}
		// 下个月的几天

		for (let i = 0; i < 6 - weeEnd; i++) {
			current = new Date(milsEnd + i * dayMils);
			monthDatas.push({
				type: 1,
				date: current,
				fullYear: current.getFullYear(),
				month: current.getMonth() + 1,
				day: current.getDate(),
			});
		}



		monthData.value = [];
		for (let i = 0; i < monthDatas.length; i++) {
			let mi = i % 7;
			if (mi == 0) {
				monthData.value.push([]);
			}

			monthData.value[Math.floor(i / 7)].push(monthDatas[i]);
		}

		// 少于6行，补足6行
		if (monthData.value.length <= 5) {
			milsStart = current.getTime();
			let lastLine = [];
			for (let i = 1; i <= 7; i++) {
				current = new Date(milsStart + i * dayMils);
				lastLine.push({
					type: 1,
					date: current,
					fullYear: current.getFullYear(),
					month: current.getMonth() + 1,
					day: current.getDate(),
				});
			}
			monthData.value.push(lastLine);
		}

	}

	function getCellColor(d) {
		if (
			d.fullYear == currentYear.value &&
			d.month == currentMonth.value &&
			d.day == currentDay.value
		) {
			return "today";
		}
		let color = d.type == -1 ? "bef-day" : d.type == 1 ? "after-day" : "this-day";
		return color;
	}

	function isToday(d) {
		if (
			d.fullYear == currentYear.value &&
			d.month == currentMonth.value &&
			d.day == currentDay.value
		) {
			return true;
		}
		return false;
	}

	function mouseenter(d, event) {
		sel.value = `${d.month}-${d.day}`;
		$emit("enter", event, d);
		// document.getElementsByClassName('cal_m_day_cell').style('background-color','#000000')
	}

	function mouseleave(d, event) {
		$emit("leave", event, d);
	}

	function dateChange() {
		let fullYear = now.value.getFullYear();

		let month = now.value.getMonth();
		let startDay = new Date(fullYear, month, 1);
		let endDay = new Date(fullYear, month + 1, 0, 23, 59, 59);
		$emit("changeMonth", startDay, endDay);
	}

	function dateFormat(fmt, date) {
		let ret;
		const opt = {
			"Y+": date.getFullYear().toString(), // 年
			"m+": (date.getMonth() + 1).toString(), // 月
			"d+": date.getDate().toString(), // 日
			"H+": date.getHours().toString(), // 时
			"M+": date.getMinutes().toString(), // 分
			"S+": date.getSeconds().toString(), // 秒
			// 有其他格式化字符需求可以继续添加，必须转化成字符串
		};
		for (let k in opt) {
			ret = new RegExp("(" + k + ")").exec(fmt);
			if (ret) {
				fmt = fmt.replace(
					ret[1],
					ret[1].length == 1 ? opt[k] : opt[k].padStart(ret[1].length, "0")
				);
			}
		}
		return fmt;
	}
</script>

<style scoped lang="scss">
	.cal_con {
		.cal_m_weeks {
			display: flex;
			justify-content: space-between;
			margin-bottom: 20rpx;

			.cal_m_weeks_cell {
				width: calc(100% / 7);
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 20rpx;
				font-weight: 400;
				color: #ffffff;
				line-height: 20rpx;
			}
		}

		.cal_m_days {
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;
			flex-direction: column;

			// height: max-content;
			.cal_m_day_line {
				display: flex;
				justify-content: space-between;

				.cal_m_day_cell {
					width: calc(100% / 7);
					height: 80rpx;
					display: flex;
					align-items: center;
					flex-direction: column;
					padding-top: 20rpx;
					line-height: 40rpx;
					transition: all 1s;
				}
			}

			.cal_m_days {}
		}

		.tab {
			width: 52rpx;
			height: 6rpx;
			background: #ffffff;
			border-radius: 3rpx;
			opacity: 0.5;
			text-align: center;
			margin: 40rpx auto 0 auto;
		}
	}

	.bef-day {
		color: #c0c4cc;
	}

	.this-day {
		color: #fff;
	}

	.after-day {
		color: #c0c4cc;
	}

	.today {
		.itemDay {
			width: 40rpx;
			height: 40rpx;
			background-color: #fff;
			font-size: 28rpx;
			font-weight: 500;
			color: #308bf8;
			line-height: 40rpx;
			border-radius: 50%;
			text-align: center;
		}
	}

	.error {
		width: 8rpx;
		height: 8rpx;
		background: #ed4014;
		margin-top: 10rpx;
		border-radius: 50%;
	}

	.suc {
		width: 8rpx;
		height: 8rpx;
		background: #fff;
		margin-top: 10rpx;
		border-radius: 50%;
	}

	.transition {
		width: 100%;
		display: flex;
		justify-content: space-between;
	}
</style>