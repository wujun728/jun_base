<template>
	<view class="container">
		<view class="leave_container">
			<view class="leave_title">
				<text>请假类型<text class="red-icon">*</text></text>
				<view class="leave_con">
					<uni-combox :candidates="leaveList" placeholder="请选择请假类型" v-model="form.leave_type"></uni-combox>
				</view>	
			</view>
			<view class="leave_title">
				<text>开始时间<text class="red-icon">*</text></text>
				<view class="leave_con">
					<uni-datetime-picker type="datetime" v-model="form.start_date" />
				</view>
			</view>
			<view class="leave_title">
				<text>结束时间<text class="red-icon">*</text></text>
				<view class="leave_con">
					<uni-datetime-picker type="datetime" v-model="form.end_date" />
				</view>
			</view>
			<view class="leave_title">
				<text>请假时长</text>
				<view class="leave_con">
					<text class="leave_all_time">{{form.leave_time}} 天</text>
				</view>
			</view>
			<view class="leave_reason">
				<text>请假事由</text>
				<textarea class="textarea" placeholder="请输入请假事由" v-model="form.leave_reason"></textarea>
			</view>
			<view class="leave_btn">
				<button type="primary" @click="submitLeave">提交</button>
			</view >
			
		</view>
		

	</view>
</template>

<script>
	export default {
		data() {
			return {
				leaveList: [
					'年假',
					'事假',
					'调休假',
					'婚假',
					'事产假',
					'陪产假',
					'其他'

				],
				form:{
					leave_type: '',
					start_date:'',
					end_date:'',
					leave_time:0,
					leave_reason:''
				}
				
			}
		},
		methods: {
			submitLeave() {
				console.log(this.form)
			}
		}
	}
</script>

<style lang="scss">
	.container {
		width: 100%;
		height: 100%;
		.leave_container {
			margin-bottom: 10px;

			.leave_title {
				display: flex;
				align-items: center;
				background:#ffffff;
				padding: 10px 20px;
				margin-bottom: 10px;
				.red-icon {
					color: red;
					padding-right: 20px;
				}
				.leave_con{
					flex:1;
					.leave_all_time{
						padding-left: 20px;
					}
				}
			}
			.leave_reason{
				padding: 10px 20px;
				background: #ffffff;
			}
			.leave_btn{
				margin-top: 10px;
				padding:0 20px;
			}
		}
		
	}
</style>
