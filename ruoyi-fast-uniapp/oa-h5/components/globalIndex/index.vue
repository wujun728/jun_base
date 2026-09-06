<template>
	<view class="global-content">
		<view class="global-notice" :class="data.count > 0 ? 'active' : 'active-n'" @click="globalNotice">
			<uni-row class="display-align">
				<uni-col :span="6" class="global-notice-left">
					<image class="image" src="/static/image/notice-icon.png" mode=""></image>
				</uni-col>
				<uni-col :span="18" class="global-notice-right">
					<view class="global-line">
						您有 <text class="default-color">1</text> 条新待办
					</view>
				</uni-col>
			</uni-row>
		</view>
	</view>
</template>

<script setup lang="ts">
	import { reactive } from 'vue'
	import { onLoad } from '@dcloudio/uni-app'
	import { clickNavigateTo } from '@/utils/helper'
	import mp3 from '@/static/audio/tip.mp3'

	const data = reactive({
		templateType: ['business_approval', 'business_adopt_apply', 'business_adopt_cc', 'business_fail', 'business_recall', 'assess_selt', 'assess_examine', 'assess_end',
			'contract_expend', 'dealt_person_work', 'dealt_client_work', 'dealt_money_work', 'daily_remind', 'daily_update_remind', 'daily_show_remind', 'contract_urgent_renew',
			'contract_day_remind',
			'contract_overdue_remind', 'contract_return_money', 'contract_renew', 'finance_verify_success', 'contract_soon_overdue_remind', 'contract_overdue_day_remind',
			'contract_abnormal', 'finance_verify_fail', 'finance_invoice_open', 'finance_invoice_close', 'finance_invoice_verify_fail', 'finance_invoice_verify_success'
		],
		count: 0,
		message: {},
		selectedType: ['delete', 'recall'],
	})

	onLoad(() => {
		uni.$on('message', (msg : any) => {
			data.message = msg.data
			getMessage(msg)
		})
	})

	const getMessage = (msg : any) => {
		const templateType = msg.data.template_type
		if (data.templateType.includes(templateType)) {
			data.count++
			innerAudio()
			setTimeout(() => {
				data.count = 0
			}, 5000)
		}
	}

	const globalNotice = () => {
		const res = data.message.buttons[0]
		if (res.uni_url && !data.selectedType.includes(res.action)) {
			clickNavigateTo(res.uni_url)
		}
	}

	const innerAudio = () => {
		const innerAudioContext = uni.createInnerAudioContext();
		innerAudioContext.src = mp3;
		innerAudioContext.autoplay = true;
	}
</script>

<style scoped lang="scss">
	.global-content {
		width: 100%;

		.global-notice {
			position: fixed;
			right: 0;
			// #ifndef APP-PLUS
			top: 44rpx;
			// #endif
			// #ifdef APP-PLUS
			top: calc(20rpx + var(--status-bar-height));
			// #endif

			width: 0;
			padding: 0 8rpx;
			height: 60rpx;
			z-index: 1001;
			line-height: 60rpx;
			border-radius: 30rpx 0 0 30rpx;
			background: #FFFFFF;
			opacity: 0;
			box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.4);
			border: 1px solid #EBEEF4;
			transition: all 1s ease;

			&.active {
				width: 248rpx;
				opacity: 1;
				display: block;
			}

			&.active-n {
				width: 0;
				opacity: 0;
				display: none;
			}

			.global-notice-left {
				width: 44rpx;
				height: 44rpx;

				.image {
					width: 100%;
					height: 100%;
				}

			}

			.global-notice-right {
				width: calc(100% - 44rpx);
				padding-left: 12rpx !important;
				font-size: 24rpx;
				font-weight: normal;
				color: $nui-text-color-four;

				.global-line {
					word-break: break-all;
					display: -webkit-box;
					-webkit-line-clamp: 1;
					-webkit-box-orient: vertical;
					overflow: hidden
				}
			}
		}
	}
</style>