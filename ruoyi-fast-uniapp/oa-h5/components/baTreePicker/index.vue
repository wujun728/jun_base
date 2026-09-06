<template>
	<view>
		<!-- 树形层级选择器-->
		<!-- 1、支持单选、多选 -->
		<uni-popup ref="popupRef" type="bottom" :mask-click="false">
			<view class="tree-dialog">
				<view class="tree-bar">
					<view class="tree-bar-cancel" :style="{'color':cancelColor}" hover-class="hover-c" @tap="cancel">取消</view>
					<view class="tree-bar-title" :style="{'color':titleColor}">{{title}}</view>
					<view class="tree-bar-confirm" :style="{'color':confirmColor}" hover-class="hover-c" @tap="confirm">确定</view>
				</view>
				<view class="tree-view">
					<scroll-view class="tree-list" :scroll-y="true">
						<view class="tree-list-content">
							<block v-for="(item, index) in data.treeList" :key="index">
								<view class="tree-item" :style="[{
								paddingLeft: item.level*30 + 'rpx'
							}]" :class="{
								itemBorder: border === true,
								show: item.isShow
							}">
									<view class="item-label">
										<view class="item-icon" @tap="onItemSwitch(item, index)">
											<view v-if="!item.isLastLevel" class="iconfont icon-jinru-copy"
												:style="{transform: !item.isLastLevel&&item.isShowChild ? 'rotate(90deg)' : 'rotate(0)'}"></view>
										</view>
										<view class="uni-flex-item" @tap="onItemSelect(item, index)">
											<view class="item-name" :class="item.id === data.selectedId ? 'active' : ''"> {{item.name}}</view>
											<view class="item-check" v-if="selectShow">
												<view class="item-check-yes" v-if="item.checkStatus==1" :class="{'radio':!multiple}" :style="{'border-color':confirmColor}">
													<view class="item-check-yes-part" :style="{'background-color':confirmColor}">
													</view>
												</view>
												<view class="item-check-yes" v-else-if="item.checkStatus==2" :class="{'radio':!multiple}" :style="{'border-color':confirmColor}">
													<view class="item-check-yes-all" :style="{'background-color':confirmColor}">
													</view>
												</view>
												<view class="item-check-no" v-else :class="{'radio':!multiple}" :style="{'border-color':confirmColor}"></view>
											</view>
										</view>
									</view>

								</view>
							</block>
						</view>
					</scroll-view>
				</view>
			</view>
		</uni-popup>

	</view>
</template>

<script setup>
	import { ref, reactive, toRefs, computed, onMounted, watch } from 'vue'
	const props = defineProps( {
		valueKey: {
			type: String,
			default: 'id'
		},
		textKey: {
			type: String,
			default: 'name'
		},
		childrenKey: {
			type: String,
			default: 'children'
		},
		localdata: {
			type: Array,
			default: function () {
				return []
			}
		},
		localTreeList: { //在已经格式化好的数据
			type: Array,
			default: function () {
				return []
			}
		},
		selectedData: {
			type: Array,
			default: function () {
				return []
			}
		},
		title: {
			type: String,
			default: ''
		},
		multiple: { // 是否可以多选
			type: Boolean,
			default: true
		},
		selectParent: { //是否可以选父级
			type: Boolean,
			default: true
		},
		selectShow: { //是否显示选择按钮
			type: Boolean,
			default: false
		},
		confirmColor: { // 确定按钮颜色
			type: String,
			default: '' // #0055ff
		},
		cancelColor: { // 取消按钮颜色
			type: String,
			default: '' // #909399 
		},
		titleColor: { // 标题颜色
			type: String,
			default: '' //
		},
		switchColor: { // 节点切换图标颜色
			type: String,
			default: '' // #666
		},
		border: { // 是否有分割线
			type: Boolean,
			default: false
		},
		clearTree: { // 是否保留数据
			type: Boolean,
			default: true
		},
		allowEmpty: { // 是否允许提交空数据
			type: Boolean,
			default: false
		}
	} )

	const {
		valueKey,
		textKey,
		childrenKey,
		localdata,
		localTreeList,
		selectedData,
		title,
		multiple,
		selectParent,
		selectShow,
		confirmColor,
		cancelColor,
		titleColor,
		switchColor,
		border,
		clearTree,
		allowEmpty
	} = toRefs( props )

	const data = reactive( {
		showDialog: false,
		treeList: [],
		selectedId: 0
	} )

	const emit = defineEmits( [ 'cancel', 'select-change' ] )

	const popupRef = ref( null )
	//显示
	const show = () => {
		popupRef.value.open()
	}
	const hide = () => {
		popupRef.value.close()
	}
	const cancel = () => {
		hide()
		emit( 'cancel' )
	}

	// 确认
	const confirm = () => { //多选
		let selectedList = []; //如果子集全部选中，只返回父级 id
		let selectedNames;
		let currentLevel = -1;
		data.treeList.forEach( ( item, index ) => {
			if ( currentLevel >= 0 && item.level > currentLevel ) {

			} else {
				if ( item.checkStatus === 2 ) {
					currentLevel = item.level;
					selectedList.push( item.id );
					selectedNames = selectedNames ? selectedNames + ' / ' + item.name : item.name;
				} else {
					currentLevel = -1;
				}
			}
		} )
		if ( selectedList.length <= 0 && !allowEmpty.value ) return
		hide()
		clearTree.value && reTreeList()
		emit( 'select-change', selectedList, selectedNames)
		// this.$emit( "select-change", selectedList, selectedNames );
	}

	// 格式化原数据（原数据为tree结构）
	const formatTreeData = ( list = [], level = 0, parentItem, isShowChild = true ) => {
		let nextIndex = 0;
		let parentId = -1;
		let initCheckStatus = 0;
		if ( parentItem ) {
			nextIndex = data.treeList.findIndex( item => item.id === parentItem.id ) + 1;
			parentId = parentItem.id;
			if ( !multiple.value ) { //单选
				initCheckStatus = 0;
			} else
				initCheckStatus = parentItem.checkStatus == 2 ? 2 : 0;
		}
		list.forEach( item => {
			let isLastLevel = true;
			if ( item && item[ childrenKey.value ] ) {
				let children = item[ childrenKey.value ];
				if ( Array.isArray( children ) && children.length > 0 ) {
					isLastLevel = false;
				}
			}

			let itemT = {
				id: item[ valueKey.value ],
				name: item[ textKey.value ],
				level,
				isLastLevel,
				isShow: isShowChild,
				isShowChild: false,
				checkStatus: initCheckStatus,
				orCheckStatus: 0,
				parentId,
				children: item[ childrenKey.value ],
				childCount: item[ childrenKey.value ] ? item[ childrenKey.value ].length : 0,
				childCheckCount: 0,
				childCheckPCount: 0
			};

			if ( selectedData.value.indexOf( itemT.id ) >= 0 ) {
				itemT.checkStatus = 2;
				itemT.orCheckStatus = 2;
				itemT.childCheckCount = itemT.children ? itemT.children.length : 0;
				onItemParentSelect( itemT, nextIndex );
			}

			data.treeList.splice( nextIndex, 0, itemT );
			nextIndex++;
		} )
	}

	// 节点打开、关闭切换
	const onItemSwitch = ( item, index ) => {
		if ( item.isLastLevel === true ) {
			return;
		}
		item.isShowChild = !item.isShowChild;
		if ( item.children ) {
			formatTreeData( item.children, item.level + 1, item );
			item.children = undefined;
		} else {
			onItemChildSwitch( item, index );
		}
	}

	const onItemChildSwitch = ( item, index ) => {
		const firstChildIndex = index + 1;
		if ( firstChildIndex > 0 )
			for ( var i = firstChildIndex; i < data.treeList.length; i++ ) {
				let itemChild = data.treeList[ i ];
				if ( itemChild.level > item.level ) {
					if ( item.isShowChild ) {
						if ( itemChild.parentId === item.id ) {
							itemChild.isShow = item.isShowChild;
							if ( !itemChild.isShow ) {
								itemChild.isShowChild = false;
							}
						}
					} else {
						itemChild.isShow = item.isShowChild;
						itemChild.isShowChild = false;
					}
				} else {
					return;
				}
			}
	}

	// 节点选中、取消选中
	const onItemSelect = ( item, index ) => {
		data.selectedId = item.id
		if ( !multiple.value ) { //单选
			item.checkStatus = item.checkStatus == 0 ? 2 : 0;

			data.treeList.forEach( ( v, i ) => {
				if ( i != index ) {
					data.treeList[ i ].checkStatus = 0
				} else {
					data.treeList[ i ].checkStatus = 2
				}
			} )

			let selectedList = [];
			let selectedNames;
			selectedList.push( item.id );
			selectedNames = item.name;
			return
		}

		let oldCheckStatus = item.checkStatus;
		switch ( oldCheckStatus ) {
			case 0:
				item.checkStatus = 2;
				item.childCheckCount = item.childCount;
				item.childCheckPCount = 0;
				break;
			case 1:
			case 2:
				item.checkStatus = 0;
				item.childCheckCount = 0;
				item.childCheckPCount = 0;
				break;
			default:
				break;
		}
		//子节点 全部选中
		onItemChildSelect( item, index );
		//父节点 选中状态变化
		onItemParentSelect( item, index, oldCheckStatus );
	}

	// 子节点 全部选中
	const onItemChildSelect = ( item, index ) => {
		let allChildCount = 0;
		if ( item.childCount && item.childCount > 0 ) {
			index++;
			while ( index < data.treeList.length && data.treeList[ index ].level > item.level ) {
				let itemChild = data.treeList[ index ];
				itemChild.checkStatus = item.checkStatus;
				if ( itemChild.checkStatus == 2 ) {
					itemChild.childCheckCount = itemChild.childCount;
					itemChild.childCheckPCount = 0;
				} else if ( itemChild.checkStatus == 0 ) {
					itemChild.childCheckCount = 0;
					itemChild.childCheckPCount = 0;
				}
				index++;
			}
		}
	}
	// 父节点 选中状态变化
	const onItemParentSelect = ( item, index, oldCheckStatus ) => {
		const parentIndex = data.treeList.findIndex( itemP => itemP.id == item.parentId );
		//console.log('parentIndex：' + parentIndex)
		if ( parentIndex >= 0 ) {
			let itemParent = data.treeList[ parentIndex ];
			let count = itemParent.childCheckCount;
			let oldCheckStatusParent = itemParent.checkStatus;

			if ( oldCheckStatus == 1 ) {
				itemParent.childCheckPCount -= 1;
			} else if ( oldCheckStatus == 2 ) {
				itemParent.childCheckCount -= 1;
			}
			if ( item.checkStatus == 1 ) {
				itemParent.childCheckPCount += 1;
			} else if ( item.checkStatus == 2 ) {
				itemParent.childCheckCount += 1;
			}

			if ( itemParent.childCheckCount <= 0 && itemParent.childCheckPCount <= 0 ) {
				itemParent.childCheckCount = 0;
				itemParent.childCheckPCount = 0;
				itemParent.checkStatus = 0;
			} else if ( itemParent.childCheckCount >= itemParent.childCount ) {
				itemParent.childCheckCount = itemParent.childCount;
				itemParent.childCheckPCount = 0;
				itemParent.checkStatus = 2;
			} else {
				itemParent.checkStatus = 1;
			}

			onItemParentSelect( itemParent, parentIndex, oldCheckStatusParent );
		}
	}

	// 重置数据
	const reTreeList = () => {
		data.treeList.forEach( ( v, i ) => {
			data.treeList[ i ].checkStatus = v.orCheckStatus
		} )
		data.selectedId = 0
	}

	// 初始化
	const initTree = () => {
		data.treeList = [];
		formatTreeData( localdata.value );
	}

	onMounted( () => {
		initTree()
	} )


	watch(
		() => [ localdata, localTreeList ],
		( newvalue, oldalue ) => {
			const local = newvalue[ 0 ].value
			const treeList = newvalue[ 1 ].value
			if ( local ) {
				initTree()
			}
			if ( treeList.length > 0 ) {
				data.treeList = treeList;
			}
		}, {
			deep: true,
			immediate: true
		},
	)


	defineExpose( { show } )
</script>

<style lang="scss" scoped>
	.tree-cover {
		position: fixed;
		top: 0rpx;
		right: 0rpx;
		bottom: 0rpx;
		left: 0rpx;
		z-index: 100;
		background-color: rgba(0, 0, 0, .4);
		opacity: 0;
		transition: all 0.3s ease;
		visibility: hidden;

		&.show {
			visibility: visible;
			opacity: 1;
		}
	}

	.tree-dialog {
		width: 100%;
		background-color: #fff;
		border-top-left-radius: 10px;
		border-top-right-radius: 10px;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		z-index: 102;
		height: 80vh;
	}


	.uni-flex-item {
		display: flex;
		align-items: center;
		flex: 1;
	}

	.tree-bar {
		/* background-color: #fff; */
		height: 90rpx;
		padding: 0 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-sizing: border-box;
		font-size: 32rpx;
		line-height: 1;
	}

	.tree-bar-confirm {
		color: #1890FF;
		padding: 15rpx;
	}

	.tree-bar-title {
		font-weight: 800;
		color: #303133;
	}

	.tree-bar-cancel {
		color: #909399;
		padding: 15rpx;
	}

	.tree-view {
		flex: 1;
		padding: 30rpx 0 30rpx 30rpx;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		overflow: hidden;
		height: 100%;
	}

	.tree-list {
		flex: 1;
		height: 100%;
		overflow: hidden;
	}

	.tree-list-content {
		flex: 1;
		height: 100%;
		padding-right: 30rpx;
	}

	.tree-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		line-height: 1;
		height: 0;
		opacity: 0;
		transition: 0.2s;
		overflow: hidden;
	}

	.tree-item.show {
		height: 90rpx;
		opacity: 1;
	}

	.tree-item.showchild:before {
		transform: rotate(90deg);
	}

	.tree-item.last:before {
		opacity: 0;
	}

	.switch-on {
		width: 0;
		height: 0;
		border-left: 10rpx solid transparent;
		border-right: 10rpx solid transparent;
		border-top: 15rpx solid #666;
	}

	.switch-off {
		width: 0;
		height: 0;
		border-bottom: 10rpx solid transparent;
		border-top: 10rpx solid transparent;
		border-left: 15rpx solid #666;
	}

	.item-last-dot {
		position: absolute;
		width: 10rpx;
		height: 10rpx;
		border-radius: 100%;
		background: #666;
	}

	.item-icon {
		width: 60rpx;
		text-align: center;

		.iconfont {
			font-size: 26rpx;
			color: #909399;
			transition: transform 0.3s;
		}

	}

	.item-label {
		flex: 1;
		display: flex;
		align-items: center;
		height: 100%;
		line-height: 1.2;
	}

	.item-name {
		flex: 1;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		width: 450rpx;

		&.active {
			color: #1890FF;
		}
	}

	.item-check {
		width: 40px;
		height: 40px;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.item-check-yes,
	.item-check-no {
		width: 20px;
		height: 20px;
		border-top-left-radius: 20%;
		border-top-right-radius: 20%;
		border-bottom-right-radius: 20%;
		border-bottom-left-radius: 20%;
		border-top-width: 1rpx;
		border-left-width: 1rpx;
		border-bottom-width: 1rpx;
		border-right-width: 1rpx;
		border-style: solid;
		border-color: #1890FF;
		display: flex;
		justify-content: center;
		align-items: center;
		box-sizing: border-box;
	}

	.item-check-yes-part {
		width: 12px;
		height: 12px;
		border-top-left-radius: 20%;
		border-top-right-radius: 20%;
		border-bottom-right-radius: 20%;
		border-bottom-left-radius: 20%;
		background-color: #1890FF;
	}

	.item-check-yes-all {
		margin-bottom: 5px;
		border: 2px solid #007aff;
		border-left: 0;
		border-top: 0;
		height: 12px;
		width: 6px;
		transform-origin: center;
		/* #ifndef APP-NVUE */
		transition: all 0.3s;
		/* #endif */
		transform: rotate(45deg);
	}

	.item-check .radio {
		border-top-left-radius: 50%;
		border-top-right-radius: 50%;
		border-bottom-right-radius: 50%;
		border-bottom-left-radius: 50%;
	}

	.item-check .radio .item-check-yes-b {
		border-top-left-radius: 50%;
		border-top-right-radius: 50%;
		border-bottom-right-radius: 50%;
		border-bottom-left-radius: 50%;
	}

	.hover-c {
		opacity: 0.6;
	}

	.itemBorder {
		border-bottom: 1px solid rgba(235, 238, 245, .55);
	}
</style>
