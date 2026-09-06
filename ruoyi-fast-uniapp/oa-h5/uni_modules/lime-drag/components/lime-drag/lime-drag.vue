<template>
	<demo-block type="ultra" title="拖拽">
		<demo-block title="基础">
			<l-drag :list="list">
				<template #grid="{active, index}">
					<view class="inner" :class="{active}">
						<text class="text" :class="{'text-active': active}">{{index}}</text>
					</view>
				</template>
			</l-drag> 
		</demo-block>
		<demo-block title="多列 长按">
			<!-- 列后 删除 幽灵 长按 -->
			<l-drag ref="dragRef2" :list="list" @change="change2" :aspectRatio="1"  :column="4" after ghost longpress>
				<template #grid="{oindex, content, active}">
					<view class="grid">
						<view class="remove" @click="onRemove2(oindex)"></view>
						<view class="inner" :class="{active}">
							<text class="text" :class="{'text-active': active}">{{content}}</text>
						</view>
					</view>
				</template>
				
				<template #ghost>
					<view class="grid">
						<!-- 幽灵样式 -->
						<view class="inner ghost"></view>
					</view>
				</template>
				<!-- 示例未设置 before -->
				<template #before>
					<view class="grid">
						<view class="inner extra" @click="onAdd2">
							增加
						</view>
					</view>
				</template>
				<template #after>
					<view class="grid">
						<view class="inner extra" @click="onAdd2">
							增加
						</view>
					</view>
				</template>
			</l-drag>
			<button @click="refresh2">更新列表</button>
			<button @click="onUnshift2">向前增加</button>
			<button @click="onPush2">向后增加</button>
			<button @click="onShift2">shift</button>
			<button @click="onPop2">pop</button>
		</demo-block>
		<demo-block title="单列 按手柄">
			<!-- 幽灵 手柄 -->
			<view class="inputs">
				<text>把原始下标为</text>
				<input type="text" v-model="move3.index">
				<text>的项移动到</text>
				<input type="text" v-model="move3.nindex">
			</view>
			<view style="height: 600rpx; overflow: auto;">
				<l-drag ref="dragRef3" :list="list3"  @change="change3" grid-height="200rpx" :column="1" :touchHandle="touchHandle3" ghost handle>
					<template #grid="{active, content, index, oindex}">
						<view class="grid">
							<view class="mover" @touchstart="touchHandle3 = true" @touchend="touchHandle3 = false" style="left: 100rpx; top: 50%; transform: translateY(-50%); position: absolute; z-index: 1;"></view>
							<view class="inner" :class="{active}">
								<text class="text" :class="{'text-active': active}">{{content}}</text>
								<view @click.stop.prevent="to(oindex, index - 1)">上移</view>
								<view @click.stop.prevent="to(oindex, index + 1)">下移</view>
							</view>
						</view>
					</template>
					<template #ghost>
						<view class="grid">
							<!-- 幽灵样式 -->
							<view class="inner ghost"></view>
						</view>
					</template>
				</l-drag>
			</view>
		</demo-block>
	</demo-block>
</template>
<script>
	import {ref, watch, defineComponent, reactive} from '../l-drag/vue'
	export default defineComponent({
		setup() {
			// 示例1和2共用list
			const list = ref(new Array(7).fill(0).map((v,i) => i));
			// 示例2 多列 长按
			const dragRef2 = ref(null)
			const newList2 = ref([])
			const change2 = v => {
				console.log('示例2数据发生变化：：', v)
				newList2.value = [...v]
			}
			const onRemove2 = (index) => {
				if(dragRef2.value && index >= 0) {
					dragRef2.value.remove(index)
				}
			}
			const onAdd2 = () => {
				dragRef2.value.push(Math.round(Math.random() * 1000))
			}
			const onShift2 = () => {
				dragRef2.value.shift()
			}
			const onPop2 = () => {
				dragRef2.value.pop()
			}
			const onUnshift2  = () => {
				dragRef2.value.unshift(Math.round(Math.random() * 1000), Math.round(Math.random() * 1000))
			}
			const onPush2  = () => {
				dragRef2.value.push(Math.round(Math.random() * 1000), Math.round(Math.random() * 1000))
			}
			const refresh2 = () => {
				list.value = new Array(10).fill(0).map((v,i) => i);
			}
			
			
			// 示例3
			const list3 = new Array(5).fill(0).map((v,i) => i);
			const dragRef3 = ref(null)
			const newList3 = ref([])
			const touchHandle3 = ref(false)
			
			const change3 = v => {
				newList3.value = v
			}
			const move3 = reactive({
				index: 0,
				nindex: 0
			})
			watch(() => move3.nindex, (v, o) => {
				dragRef3.value.move(move3.index*1, move3.nindex*1)
			})
			
			const to = (oindex, index) => {
				dragRef3.value.move(oindex, index)
			}
			
			return {
				list,
				// 示例2
				dragRef2,
				change2,
				onRemove2,
				onAdd2,
				refresh2,
				onUnshift2,
				onPush2,
				onShift2,
				onPop2,
				// 示例3
				list3,
				dragRef3,
				change3,
				move3,
				touchHandle3,
				to
			}
		}
	})
	
	
</script>
<style lang="scss">
	
	.grid {
		height: 100%;
		/* #ifdef APP-NVUE */
		flex: 1;
		/*  #endif */
		/* #ifndef APP-NVUE */
		width: 100%;
		/*  #endif */
		padding: 16rpx;
		box-sizing: border-box;
		position: relative;
	}
	.remove {
		width: 32rpx; 
		height: 32rpx;
		background-color: red;
		border-radius: 50%;
		font-size: 16rpx;
		color: white;
		display: flex;
		text-align: center;
		justify-content: center;
		z-index: 10;
		position: absolute;
		right: 0;
		top: 0;
	}
	.inner {
		/* #ifdef APP-NVUE */
		flex: 1;
		/*  #endif */
		/* #ifndef APP-NVUE */
		height: 100%;
		width: 100%;
		/*  #endif */
		
		border: 1rpx solid #eee;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: white;
		font-size: 50rpx;
		font-weight: bold;
		color: blue;
		transition: all 300ms ease;
		position: relative;
	}
	.extra {
		color: #ddd
	}
	.mover {
		position: relative;
		width: 50rpx;
		margin-top: 10rpx;
		height: 30rpx;
		border: 2px solid #ddd;
		border-left: none;
		border-right: none;
		/* background-color: #ddd; */
	}
	.mover::before {
		content: '';
		position: absolute;
		width: 50rpx;
		top: 15rpx;
		border-top: 2px solid #ddd;
	}
	.active {
		box-shadow: 0 2rpx 16rpx rgba(0, 0, 0, 0.1);
		background-color: blue;
		color: white;
		border: 1rpx solid blue;
		transform: scale(1.1);
	}
	/* #ifdef APP-NVUE */
	.text {
		font-size: 50rpx;
		font-weight: bold;
	}
	.text-active {
		font-size: 50rpx;
		font-weight: bold;
		color: white;
	}
	/*  #endif */
	.ghost {
		background-color: rgba(0, 0, 255, 0.1);
	}
	.inputs {
		display: flex;
		align-items: center;
		input {
			background-color: #fff;
			width: 80rpx;
			border: 1rpx solid #ddd;
			padding: 5rpx 10rpx;
			margin: 0 8rpx;
		}
	}
</style>
