<template>
	<view class="upload-time-picker">
		<uni-popup ref="popupRef" type="bottom" :mask-click="true">
			<view class="uni-picker-header">
				<view class="uni-picker-action uni-picker-action-cancel" @click="cancel">取消</view>
				<view class="uni-picker-action uni-picker-action-confirm" @click="confirm">完成</view>
			</view>
			<picker-view :indicator-style="data.indicatorStyle" :value="data.newValue" @change="changePicker" class="picker-view">
				<picker-view-column>
					<view class="item" v-for="(item,index) in data.years" :key="index">{{item}}年</view>
				</picker-view-column>
				<picker-view-column>
					<view class="item" v-for="(item,index) in data.months" :key="index">{{item}}月</view>
				</picker-view-column>
				<picker-view-column>
					<view class="item" v-for="(item,index) in data.days" :key="index">{{item}}日</view>
				</picker-view-column>
				<picker-view-column>
					<view class="item" v-for="(item,index) in data.options" :key="index">{{item.label}}</view>
				</picker-view-column>
			</picker-view>
		</uni-popup>

	</view>
</template>

<script setup>
	import { ref, reactive, toRefs, onMounted, watch } from 'vue'
	import moment from 'moment'

	const props = defineProps( {
		dataTime: {
			type: String,
			default: '',
		},
		type: {
			type: String,
			default: 'time',
		},
	} )
	const { dataTime, type } = toRefs( props )

	const popupRef = ref( null )

	// 打开弹出
	const popupOpen = () => {
		popupRef.value.open()
	}

	// 关闭
	const cancel = () => {
		popupRef.value.close()
	}

	const data = reactive( {
		years: [],
		months: [],
		days: [],
		newValue: [],
		dataTime: moment( new Date() ).format( 'YYYY/MM/DD' ),
		newTime: moment( new Date() ).format( 'YYYY/MM/DD' ) + '/1',
		options: [
			{ value: 1, label: '上午' },
			{ value: 0, label: '下午' },
		],
		monthDays: [ 4, 6, 9, 11 ],
		month: moment( new Date() ).format( 'M' ),
		indicatorStyle: `height: 50px;`
	} )

	const pickerInit = async () => {
		for ( let i = 2014; i <= 2050; i++ ) {
			data.years.push( i )
		}
		for ( let i = 1; i <= 12; i++ ) {
			data.months.push( i )
		}
		await getMonthDays( moment( new Date() ).format( 'YYYY' ), data.month )
		getChangeTime()
	}

	let emit = defineEmits( [ 'change' ] )

	const changePicker = ( e ) => {
		let current = e.detail.value
		const len1 = current[ 0 ]
		const len2 = current[ 1 ]
		const year = data.years[ len1 ]
		const month = data.months[ len2 ]
		data.month = Number( month )
		getMonthDays( year, data.month )
		if ( type.value === 'time' ) {
			const len3 = current[ 2 ]
			const len4 = current[ 3 ]
			data.newTime = year + '/' + month + '/' + data.days[ len3 ] + '/' + data.options[ len4 ].value
		}
	}

	const getMonthDays = ( year, month ) => {
		let days = 31
		data.days = []
		if ( data.monthDays.includes( month ) ) {
			days = 30
		} else if ( data.month === 2 ) {
			if ( year % 4 === 0 ) {
				days = 29
			} else {
				days = 28
			}
		}
		for ( let i = 1; i <= days; i++ ) {
			data.days.push( i )
		}
	}

	const confirm = () => {
		emit( 'change', data.newTime )
		cancel()
	}

	onMounted( () => {
		pickerInit()
	} )

	const getChangeTime = () => {
		data.newValue = []
		if ( type.value === 'time' ) {
			const arrTime = data.dataTime.split( '/' )
			let yIndex = data.years.findIndex( ( item ) => { return item === Number( arrTime[ 0 ] ) } )
			let mIndex = data.months.findIndex( ( item ) => { return item === Number( arrTime[ 1 ] ) } )
			let dIndex = data.days.findIndex( ( item ) => { return item === Number( arrTime[ 2 ] ) } )
			let time = 0
			if ( arrTime.length === 4 ) {
				time = data.options.findIndex( ( item ) => { return item.value === Number( arrTime[ 3 ] ) } )
			}
			data.newValue = ( yIndex > -1 && mIndex > -1 && dIndex > -1 ) ? [ yIndex, mIndex, dIndex, time ] : []
		}
	}

	// 数据监听
	watch( dataTime, ( newvalue, oldvalue ) => {
		if ( newvalue ) {
			data.dataTime = newvalue
			getChangeTime()
		}
	}, { immediate: true, deep: true } );

	defineExpose( { popupOpen } )
</script>

<style lang="scss" scoped>
	.upload-time-picker {

		.uni-picker-header {
			border-bottom: 1px solid #e5e5e5;
			width: 100%;
			height: 90rpx;
			background-color: #fff;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.uni-picker-action {
				max-width: 50%;
				top: 0;
				height: 100%;
				box-sizing: border-box;
				padding: 0 14px;
				font-size: 30rpx;
				line-height: 90rpx;
				cursor: pointer;

				&.uni-picker-action-cancel {
					color: #888;
				}

				&.uni-picker-action-confirm {
					color: #007aff;
				}
			}
		}


		.picker-view {
			width: 750rpx;
			height: 480rpx;
			background-color: #fff;
		}

		.item {
			display: flex;
			justify-content: center;
			align-items: center;
		}
	}
</style>
