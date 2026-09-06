<template>
	<div class="dropdown-item">
		<!-- selected -->
		<view :class="['dropdown-item__selected',listWidth!='150rpx'?'dropdown-item__right':'dropdown-item__left']" 
			  @click="changePopup" :style="{maxWidth:selectWidth}">
			<view class="selected__name">{{selectItem.text}}</view>
			<view class="selected__icon"
				:class="showClass === 'show'? 'up' : 'down'"
			>
				<li class="iconfont icon-caretdown"></li>
			</view>
		</view>
		<view class="dropdown-item__content" :style="{top: contentTop + 'rpx'}" v-if="showList">
			<!-- dropdown -->
			<view :class="['list', showClass]" :style="{left: contentLeft>0?contentLeft + 'rpx':'auto',right: contentRight>0?contentRight + 'rpx':'auto',}">
				<view class="list__option"
					v-for="(item, index) in list" :key="index"
					@click="choose(item)">
					<view>{{item.text}}</view>
					<icon v-if="item.value === value" type="success_no_circle" size="20"/>	
				</view>
			</view>	
			<!-- dropdown-mask -->
			<view :class="['dropdown-mask', showClass]" v-if="showList" @click="closePopup"></view>
		</view>
	</div>
</template>

<script>
	export default {
		components: {
		},
		props: {
			value: [Number, String, Object],
			list: {
				type: Array,
				default: ()=> {
					return []
				}
			},
			contentTop:{
				type:String,
				default:"185"
			},
			contentLeft:{
				type:String,
				default:"0"
			},
			contentRight:{
				type:String,
				default:"0"
			},
			listWidth:{
				type:String,
				default:'150rpx'
			},
			selectWidth:{
				type:String,
				default:'150rpx'
			}
		},
		data() {
			return {
				showList: "",
				showClass: '',
				selectItem: {},
			}
		},
		watch: {
			list(newVal,oldVal){
				this.selectItem = newVal[0];
			}
		},
		mounted() {
			this.showList = this.active;
			this.selectItem = this.list[0];
		},
		methods: {
			choose(item) {
				if(item.value != "auto"){
					this.selectItem = item
				}
				this.$emit('changeItem', item);
				this.closePopup();
			},
			selectAuto(){
				this.selectItem = {text: '指定日期',value: 'auto'};
			},
			changePopup() {
				if(this.showList) {
					this.closePopup()
				} else {
					this.openPopup()
				}
			},
			openPopup() {
				setTimeout(() => {
					this.showClass = 'show';
					this.showList = true;
				}, 100);
			},
			closePopup() {
				this.showClass = ''
				setTimeout(() => {
					this.showList = false
				}, 200);
			},
			close() {
				this.showClass = ''
				this.showList = false
			},
		}
	}
</script>

<style lang="scss">
	
	li{
		 list-style-type:none;
	}
	.dropdown-item__content{
		z-index: 10!important;
	}
	.dropdown-item {
		width: 100%;
		flex:1;
		position: relative;
		&__selected {
			position: relative;
			padding: 10rpx 0;
			box-sizing: border-box;
			color: #fff;
			.selected__name {
				font-size: 28rpx;
				text-overflow: ellipsis;
				white-space: nowrap;
				overflow: hidden;
			}
			.selected__icon {
				margin-left: 20rpx;
				&.down {
					transition: transform .3s;
					transform: rotateZ(0);
				}
				&.up {
					transition: transform .3s;
					transform: rotateZ(-180deg);
				}
			}
			
		}
		&__left{
			display: flex;
			flex-direction: row;
			justify-content: flex-start;
			align-items: center;
		}
		&__right{
			display: flex;
			flex-direction: row;
			justify-content: flex-end;
			align-items: center;
		}
		&__content {
			position: fixed;
			left: 0;
			right: 0;
			overflow: hidden;
			top: 0;
			bottom: 0;
			z-index: 1;
			.list {
				max-height: 400px;
				text-align: center;
				overflow-y: auto;
				position: absolute;
				z-index: 1200;
				background: #fff;
				transform: translateY(-100%);
				transition: all .3s;
				&.show {
					transform: translateY(0);
				}
				&__option {
					font-size:30rpx;
					padding: 18rpx;
					display: flex;
					justify-content: space-between;
					color: #303133;
					&:not(:last-child) {
						border-bottom: 1rpx solid #DDDDDD;
					}
				}
			}
			.dropdown-mask {
				position: absolute;
				left: 0;
				right: 0;
				top: 0;
				bottom: 0;
				transition: all .3s;
				z-index: 1100;
				&.show {
					background:rgba(0,0,0,0.5);
				}
			}
		}
		&:not(:last-child):after {
			content: ' ';
			position: absolute;
			width: 2rpx;
			top: 36rpx;
			bottom: 36rpx;
			right: 0;
			background: $uni-border-color;
		}
	}
</style>
