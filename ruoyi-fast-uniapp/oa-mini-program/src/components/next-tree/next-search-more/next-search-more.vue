<template>
	<view class="next-search-more">
		<view class="search" :style="{ backgroundColor: backgroundColor }">
			<view class="content" :style="{ 'border-radius': radius + 'px', border: border }">
				<view class="content-box" :class="{ center: mode === 'center' }">
					<text class="icon icon-search">&#xe66f;</text>
					<input v-model="inputVal" class="input" :class="{ center: !active && mode === 'center' }" :focus="isFocus" :placeholder="placeholder" @input="input" @focus="focus" @blur="blur" />
					<text v-if="isDelShow" class="icon icon-del" @click="clear">&#xe61c;</text>
				</view>
				<view v-show="(active && isFixedSearchBtn && button === 'inside') || (isDelShow && button === 'inside')" class="searchBtn" @click="search">搜索</view>
			</view>
			<template v-if="mode === 'common' || mode ==='center'">
				<view v-if="button === 'outside'" class="button" :class="{ active: isFixedSearchBtn || active }" @click="search">
					<view class="button-item">{{ !isFixedSearchBtn ? searchName : '搜索' }}</view>
				</view>
			</template>
			<template v-else-if="mode === 'more'">
				<view class="button active" @click="selectMore">
					<view class="button-item"><text class="icon icon-more">&#xe61a;</text></view>
				</view>
			</template>
		</view>
		<view class="more-container-parent">
			<view v-if="mode === 'more' && showMore" class="more-container">
				<slot name="more"></slot>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		mode: {
			type: String,
			default: 'common'
		},
		button: {
			type: String,
			default: 'outside'
		},
		isFixedSearchBtn: {
			type: Boolean,
			default: true
		},
		radius: {
			type: String,
			default: '60'
		},
		placeholder: {
			type: String,
			default: '请输入搜索内容'
		},
		backgroundColor: {
			type: String,
			default: '#fff'
		},
		showMore: {
			type: Boolean,
			default: false
		},
		border: { type: String, default: '1px #f5f5f5 solid' }
		
	},
	data() {
		return {
			active: false,
			inputVal: '',
			searchName: '取消',
			isDelShow: false,
			isFocus: false,
			timer: 0
		};
	},
	created() {
		this.$watch(() => this.inputVal, (newVal) => {
			if (newVal) {
				this.searchName = '搜索';
				this.isDelShow = true;
			} else {
				this.searchName = '取消';
				this.isDelShow = false;
			}
		})
	},
	methods: {
		focus() {
			this.active = true;
		},
		blur() {
			this.isFocus = false;
			if (!this.inputVal) {
				this.active = false;
			}
		},
		input() {
			clearInterval(this.timer)
			this.timer = setTimeout(() => {
				this.$emit('input', this.inputVal);
			}, 500)
		},
		clear() {
			this.inputVal = '';
			this.active = false;
			this.$emit('input', this.inputVal);
			this.$emit('search', '');
		},
		getFocus() {
			this.isFocus = true;
		},
		search() {
			if (!this.inputVal) return;
			this.$emit('search', this.inputVal);
		},
		selectMore() {
			this.$emit('moreClick')
		}
	}
};
</script>

<style lang="scss" scoped>
.next-search-more {
	.search {
		display: flex;
		width: 100%;
		border-bottom: 1px #f5f5f5 solid;
		box-sizing: border-box;
		padding: 15upx;
		font-size: $uni-font-size-base;
		background: #fff;
		.content {
			display: flex;
			align-items: center;
			width: 100%;
			height: 60upx;
			border: 1px #ccc solid;
			background: #fff;
			overflow: hidden;
			transition: all 0.2s linear;
			border-radius: 30px;
	
			.content-box {
				width: 100%;
				display: flex;
				align-items: center;
				&.center {
					justify-content: center;
				}
				.icon {
					padding: 0 15upx;
					&.icon-del {
						font-size: 38upx;
					}
				}
				.input {
					width: 100%;
					max-width: 100%;
					line-height: 60upx;
					height: 60upx;
					transition: all 0.2s linear;
					&.center {
						width: 200upx;
					}
					&.sub {
						// position: absolute;
						width: auto;
						color: grey;
					}
				}
			}
			.searchBtn {
				height: 100%;
				flex-shrink: 0;
				padding: 0 30upx;
				background: $uni-color-success;
				line-height: 60upx;
				color: #fff;
				border-left: 1px #ccc solid;
				transition: all 0.3s;
			}
		}
	
		.button {
			display: flex;
			align-items: center;
			justify-content: center;
			position: relative;
			flex-shrink: 0;
			width: 0;
			transition: all 0.2s linear;
			white-space: nowrap;
			overflow: hidden;
			&.active {
				padding-left: 15upx;
				width: 100upx;
			}
			.icon-more {
				font-size: 48upx;
			}
		}
	}
	.more-container-parent {
		flex-shrink: 0;
		width: 100%;
		// position: fixed;
		// position: sticky;
		z-index: 997;
		flex-wrap: nowrap;
		display: flex;
		flex-direction: row;
		position: relative;
		flex-direction: column;
		.more-container {
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: auto;
			background-color: #ffffff;
			padding: 20rpx;
			border-radius: 0 0 30rpx 30rpx;
			box-sizing: border-box;
			overflow: hidden;
		}
	}
}

@font-face {
	font-family: 'iconfont';
	src: url('https://at.alicdn.com/t/c/font_4110624_nikfg21uyk8.ttf?t=1686190660183') format('truetype');
}
.icon {
	font-family: iconfont;
	font-size: 32upx;
	font-style: normal;
	color: #999;
}
</style>
