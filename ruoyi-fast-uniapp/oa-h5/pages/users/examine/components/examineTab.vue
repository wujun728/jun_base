<template>
	<view class="examine-search plr10">
		<view class="examine-search-list">
			<view class="examine-search-list-item" :class="index === data.tabIndex ? 'active': ''" v-for="(item,index) in examineTabData" :key="'tab'+item.id"
				@click="examineSearchItem(item, index)">
				<view class="name">{{item.name}}</view>
				<!-- <uni-badge class="examine-search-badge" :custom-style="data.customStyle" :text="item.number" type="error" /> -->
			</view>
		</view>
	</view>
</template>

<script setup>
	import { reactive, toRefs, watch } from "vue"
	const props = defineProps( {
		examineTabData: {
			type: Array,
			default: () => {
				return []
			}
		},
		index: {
			type: Number,
			default: -1
		}
	} )
	const { examineTabData, index } = toRefs( props )

	const data = reactive( {
		tabIndex: 0
	} )

	const emit = defineEmits( [ 'change' ] )

	const examineSearchItem = ( item, index ) => {
		data.tabIndex = index
		item.index = index
		emit( 'change', item )
	}
	// 数据监听
	watch( () => index, ( newvalue, oldvalue ) => {
		if ( newvalue.value > -1 ) {
			data.tabIndex = newvalue.value
		}
	}, { immediate: true } )
</script>

<style scoped lang="scss">
	.examine-search {
		height: 80rpx;
		line-height: 80rpx;
		border-bottom: 1px solid #F0F1F5;

		.examine-search-list {
			display: flex;
			justify-content: space-around;
			align-items: center;

			.examine-search-list-item {
				position: relative;
				text-align: center;
				color: $nui-text-color-four;
				border-bottom: 2px solid rgba(0, 0, 0, 0);

				&.active {
					color: $uni-text-color;
					font-size: 600;
					font-weight: $uni-default-font-weight;
					border-bottom: 2px solid $uni-color-primary;
				}

				.name {
					font-size: 28rpx;
				}

				.examine-search-badge {
					position: absolute;
					top: -10px;
					right: 0;
				}
			}
		}
	}
</style>
