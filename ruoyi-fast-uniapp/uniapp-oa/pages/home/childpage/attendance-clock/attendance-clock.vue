<template>
	<view class="container">
			<view class="attendance">
				<uni-segmented-control :current="current" :values="approvalList" @clickItem="onClickItem" styleType="button" activeColor="#007AFF"></uni-segmented-control>
				<view v-show="current === 0" class="attendance_content">
					<view class="attendance_time">
						<view class="attendance_morning">
							<view class="morning_state">
								
							</view>
							<view class="">
								<ul>
									<li>已打卡</li>
									<li class="clock_time">09:00 上班</li>
								</ul>
							</view>
						</view>
						<view class="attendance_afternoon">
							<view class="afternoon_state">
								
							</view>
							<view class="">
								<ul>
									<li>未打卡</li>
									<li class="clock_time">18:00 下班</li>
								</ul>
							</view>
						</view>
						
					</view>
					<view class="location">
						当前定位：{{location}}
					</view>
					<view class="punsh_in">
						<view class="punsh_op">
							{{punchinText}}
						</view>
						<view class="punshin_time">
							{{time}}
						</view>
					</view>
				</view>
				<view v-show="current === 1" class="attendance_content">
					<uni-calendar
					:insert="true"
					:start-date="'2019-3-2'"
					:end-date="'2019-5-20'"
					@change="change"
					:selected="selectDate"
					 />
				</view>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				current:0,
				approvalList:['考勤打卡','打卡记录'],
				selectDate:[{date: '2021-10-08'},{date: '2021-10-09'},{date: '2021-10-11'},{date: '2021-10-12'},{date: '2021-10-13'},{date: '2021-10-14'},{date: '2021-10-15'},{date: '2021-10-18'}],
				location:'获取地理位置失败',
				punchin_state:1,
				time:'',
				timer:null
			}
		},
		onLoad(){
			this.getLocation()
			
		},
		onShow() {
			this.getTime()
			this.timer = setInterval(() =>{
				this.getTime()
			},1000)
		},
		onHide() {
			if(this.timer) clearInterval(this.timer)
		},
		computed:{
			punchinText:function(){
				return this.punchin_state === 0? '上班打卡':'下班打卡'
			}
		},
		methods: {
			onClickItem(e) {
				this.current = e.currentIndex
			},
			change(event) {
				console.log(event)
			},
			getLocation(){
				// uni.getLocation({
				// 	type: 'gcj02',
				// 	geocode:true,
				//     success: function (res) {
				// 		console.log(res)
				//         console.log('当前位置的经度：' + res.longitude);
				//         console.log('当前位置的纬度：' + res.latitude);
				//     },
				// 	fail:function (err) {
				// 		console.log(err)
				// 	}
				// });
			},
			getTime(){
				let date = new Date()
				let h = date.getHours()
				h = h < 10 ? ('0' + h) : h
				let minute = date.getMinutes()
				minute = minute < 10 ? ('0' + minute) : minute; 
				var second = date.getSeconds();		
				second = second < 10 ? ('0' + second) : second
				this.time = h +':'+ minute +':' + second; 
			}
		}
	}
</script>

<style lang="scss">
	.container{
		width: 100%;
		height: 100%;
		background: #f5f6f7;
		.attendance{
			padding: 10px 20px;		
			.attendance_content{
				margin-top: 10px;
				.attendance_time{
					padding: 10px 0;
					font-size: 12px;
					display: flex;
					background: #ffffff;
					border-radius: 8rpx;
					.attendance_morning, {
						flex:1;
						display: flex;
						justify-content: center;
						align-items: center;
						.morning_state{
							margin-right: 10px;
							width:8px;
							height: 8px;
							border-radius: 50%;
							background:#4CD964;
						}				
						
					}
					.attendance_afternoon{
						flex:1;
						display: flex;
						justify-content: center;
						align-items: center;
						.afternoon_state{
							margin-right: 10px;
							width:8px;
							height: 8px;
							border-radius: 50%;
							background:#999999;
						}
					}
				}
				.location{
					padding: 10px;
					height: 20px;
					line-height: 20px;
					margin-top: 10px;
					background: #ffffff;
					font-size: 13px;
					border-radius: 8rpx;
				}
				.punsh_in{
					width:120px;
					height: 120px;
					margin: 80px auto;
					border-radius: 50%;
					background: #007aff;
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: center;
					.punsh_op{
						color: #ffffff;
						font-size: 16px;
						font-weight: bold;
					}
					.punshin_time{
						margin-top: 4px;
						color: #ffffff;
						opacity: 0.8;
					}
				}
			}
		}
		/deep/ .uni-calendar-item__weeks-box-circle{
			background: #4CD964;
		}
		/deep/ .uni-calendar{
			justify-content: center;
			align-items: center;
		}
		.clock_time{
			color: #999999;
		}
	}
</style>
