<template>
	<view class="l-drag l-class" :style="[areaStyles]" ref="dragRef" @touchstart="setDisabled">
		<movable-area class="l-drag__inner" v-if="isReset" :style="[innerStyles]">
			<slot></slot>
			<movable-view class="l-drag__ghost" v-if="isDrag && props.ghost" :animation="true" :style="[viewStyles]" direction="all" :x="ghostEl.x" :y="ghostEl.y" key="l-drag-clone">
				<slot name="ghost"></slot>
			</movable-view>
			<movable-view v-if="props.before" class="l-drag__before" disabled :animation="false" :style="[viewStyles]" :x="beforeEl.x" :y="beforeEl.y">
				<slot name="before"></slot>
			</movable-view>
			<movable-view
				v-for="(item, oindex) in cloneList" :key="item.id"
				direction="all" 
				:data-oindex="oindex" 
				:style="[viewStyles]"
				class="l-drag__view"
				:class="[{'l-is-active': oindex == active, 'l-is-hidden': !item.show}, item.class]"
				:x="item.x"
				:y="item.y"
				:friction="friction"
				:damping="damping"
				:animation="animation"
				:disabled="isDisabled || props.disabled"
				@touchstart="touchStart" 
				@change="touchMove" 
				@touchend="touchEnd"
				@touchcancel="touchEnd"
				@longpress="setDisabled"
				>
				<!-- <view v-if="props.remove" class="l-drag__remove" :style="removeStyle" data-remove="true">
					<slot name="remove" :oindex="oindex" data-remove="true" />
				</view> -->
				<!-- <view v-if="props.handle" class="l-drag__handle" :style="handleStyle" data-handle="true">
					<slot name="handle" :oindex="oindex" :active="!isDisabled && !isDisabled && oindex == active" />
				</view> -->
				<slot name="grid" :oindex="oindex" :index="item.index" :oldindex="item.oldindex" :content="item.content" :active="!isDisabled && !isDisabled && oindex == active" />
				<view class="mask" v-if="!(isDisabled || props.disabled) && props.longpress"></view>
			</movable-view>
			
			
			<movable-view v-if="props.after" class="l-drag__after" disabled :animation="true"  direction="all" :style="[viewStyles]" :x="afterEl.x" :y="afterEl.y">
				<slot name="after"></slot>
			</movable-view>
		</movable-area>
	</view>
</template>
<script lang="ts">
	// @ts-nocheck
	import { computed, onMounted, ref, getCurrentInstance, watch, nextTick, reactive , triggerRef, onUnmounted, defineComponent} from "./vue";
	import DragProps from './props';
	import type {GridRect, Grid, Position} from  './type'
	// #ifdef APP-NVUE
	const dom = weex.requireModule('dom')
	// #endif
	
	export default defineComponent({
		name: 'l-drag',
		externalClasses: ['l-class'],
		options: {
			addGlobalClass: true,
			virtualHost: true,
		},
		props: DragProps,
		emits: ['change'],
		setup(props, {emit, expose}) {
			// #ifdef APP-NVUE
			const dragRef = ref(null)
			// #endif
			const app = getCurrentInstance()
			const isDrag = ref(false)
			const isInit = ref(false)
			const isReset = ref(true)
			const colmunId = ref(-1)
			/** 选中项原始下标 */
			const active = ref(-1)
			const maxIndex = ref(-1)
			const animation = ref(true)
			const isDisabled = ref(props.handle || props.longpress ? true: false)
			
			const dragEl  = reactive({
				content: null,
				/** 当前视图下标*/
				index: 0,
				/** 旧视图下标 */
				oldindex: -1,
				/** 上次原始下标 */
				lastindex: -1
			})
			
			const ghostEl = reactive({
				content: null,
				x: 0,
				y: 0
			})
			const beforeEl = reactive({
				x: 0,
				y: 0
			})
			const afterEl = reactive({
				x: 0,
				y: 0
			})
			
			let gridRects = [] //ref<GridRect[]>([])
			const areaWidth = ref(0)
			const cloneList = ref<Grid[]>([])
			// 删除项时可能会减少行数影响到删除过渡动画，故增加此值在删除时保持高度不变，等动画完成后再归零
			const leaveRow = ref(0)
			const extra = computed(() => (props.before ? 1 :0) + (props.after ? 1 : 0))
			const rows = computed(() => Math.ceil( ((isInit.value ? cloneList.value.length : props.list.length) + extra.value) / props.column ))
			const gridHeight = computed(() => props.aspectRatio ? girdWidth.value / props.aspectRatio : (/rpx$/.test(`${props.gridHeight}`) ? uni.upx2px(parseInt(`${props.gridHeight}`)) : parseInt(`${props.gridHeight}`)))
			const girdWidth = computed(() => areaWidth.value / props.column)
			const viewStyles = computed(() => ({width: girdWidth.value + 'px',height: gridHeight.value + 'px'}))
			const areaStyles = computed(() => ({height: (rows.value + leaveRow.value ) * gridHeight.value + 'px'}))
			const innerStyles = computed(() => ({
				// #ifdef APP-NVUE
				width: areaWidth.value + 'px', 
				// #endif
				height: (rows.value + props.extraRow + leaveRow.value) * gridHeight.value + 'px'}))
			
			const sleep = (cb: Function, time = 1000/60) => setTimeout(cb, time)
			const createGrid = (content: any, position?:Position|null): Grid => {
				colmunId.value++
				maxIndex.value++
				const index = maxIndex.value
				const colmun = gridRects[index]
				
				let x = 0
				let y = 0
				if(colmun) {
					if(props.after) {
						let nxet = gridRects[index + 1]
						if(!nxet) {
							nxet = createGridRect(gridRects.length + (props.before ? 1 : 0))
							gridRects.push(nxet)
						} 
						setReset(() => setAfter(nxet))
					} else {
						setReset()
					}
					x = colmun.x
					y = colmun.y
				} else {
					const nxet = createGridRect(gridRects.length + (props.before ? 1 : 0))
					gridRects.push(nxet)
					setReset()
					x = nxet.x
					y = nxet.y
				}
				if(position) {
					x = position.x
					y = position.y
				}
				return {id: `l-drag-item-${colmunId.value}`, index, oldindex: index, content, x, y, class: '', show: true}
			}
			const setReset = (cb?: any) => {
				// const newRow = (cloneList.value.length + extra.value) % (props.column)
				if(isInit.value) {
					cb&&sleep(cb)
				}
			}
			const setAfter = ({x, y} = {x: 0, y: 0}) => {
				if(props.after) {
					afterEl.x = x
					afterEl.y = y
				}
			}
			const setDisabled = (e: any, flag?: boolean= false) => {
				 // e?.preventDefault() 
				const type = `${e.type}`.toLowerCase()
				const {handle = props.touchHandle} = e.target.dataset
				if(props.handle && !handle) {
					isDisabled.value = true
				} else if(props.handle && handle && !props.longpress) {
					isDisabled.value = flag
				} else if(props.handle && handle && props.longpress && type.includes('longpress')) {
					isDisabled.value = false
				} else if(props.longpress && type.includes('longpress') && !props.handle) {
					isDisabled.value = false
				}
				if(type.includes('touchend') && props.longpress) {
					isDisabled.value = true
				}
			}
			const createGridRect = (i: number, last?: GridRect): GridRect => {
				let { row } = last || gridRects[gridRects.length - 1] || { row: 0 }
				const col = i % (props.column)
				const grid = (row: number, x: number, y: number):GridRect => {
					return {row, x, y, x1: x +  girdWidth.value, y1: y + gridHeight.value}
				}
				if(col == 0 && i != 0) {row++} 
				return grid(row, col * girdWidth.value, row * gridHeight.value)
			}
			const createGridRects = () => {
				let rects: GridRect[] = []
				const length = rows.value * props.column + extra.value
				gridRects = []
				for (var i = 0; i < length; i++) {
					const item = createGridRect(i, rects[rects.length - 1])
					rects.push(item)
				}
				if(props.before) {
					const {x, y} = rects.shift()
					beforeEl.x = x
					beforeEl.y = y
				}
				setAfter(rects[props.list.length])
				gridRects = rects as GridRect[]
			}
			const updateList = (v: any[]) => {
				cloneList.value = v.map((content) => createGrid(content))
			}
			
			const touchStart = (e: any) => {
				if(e.target.dataset.remove) return
				// 选中项原始下标
				const {oindex} = e.currentTarget?.dataset || e.target?.dataset || {}
				if(typeof oindex !== 'number') return
				const target = cloneList.value[oindex]
				isDrag.value = true
				// 选中项原始下标
				active.value = oindex 
				// 选中项的当前下标
				dragEl.index = dragEl.oldindex = target.index
				ghostEl.x = target.x||0
				ghostEl.y = target.y||0
				dragEl.content = ghostEl.content = target.content
			}
			
			const touchEnd = (e: any) => {
				setTimeout(() => {
					if(e.target.dataset.remove || active.value==-1) return
					setDisabled(e, true)
					isDrag.value = false
					const isEmit = dragEl.index !== dragEl.oldindex && dragEl.oldindex > -1 // active.value !== dragEl.index
					dragEl.lastindex = active.value
					dragEl.oldindex = active.value = -1
					const last = cloneList.value[dragEl.lastindex]
					const position = gridRects[dragEl.index]
					nextTick(() => {
						last.x = position.x + 0.001
						last.y = position.y + 0.001
						sleep(() => {
							last.x = position.x
							last.y = position.y
							isEmit && emitting()
						})
					})
				},80)
				
			}
			const emitting = () => {
				const clone = [...cloneList.value].sort((a, b) => a.index - b.index)//.map(item => ref(item.content))
				emit('change', clone)
			}
			
			const touchMove = (e: any) => {
				if(!isDrag.value) return
				// #ifndef APP-NVUE
				let {oindex} = e.currentTarget.dataset
				// #endif
				// #ifdef APP-NVUE
				oindex = e.currentTarget.dataset['-oindex']
				// #endif
				if(oindex != active.value) return
				const {x, y} = e.detail
				const centerX = x + girdWidth.value / 2
				const centerY = y + gridHeight.value / 2
				for (let i = 0; i < cloneList.value.length; i++) {
					const item = gridRects[i]
					if(centerX > item.x && centerX < item.x1 && centerY > item.y && centerY < item.y1) {
						ghostEl.x = item.x
						ghostEl.y = item.y
						if(dragEl.index != i) {
							_move(active.value, i)
						}
						break;
					}
				}
			}
			const getDragEl = (oindex: number) => {
				if(isDrag.value) {return dragEl}
				return cloneList.value[oindex]
			}
			
			/**
			 * 把原始数据中排序为index的项 移动到 toIndex
			 * @param oindex 原始数据的下标
			 * @param toIndex 视图中的下标
			 * @param position 指定坐标
			 */
			const _move = (oindex: number, toIndex: number, position?: Position|null, emit: boolean = true) => {
				const length = cloneList.value.length - 1
				if(toIndex > length || toIndex < 0) return
				// 获取oIdnex在视图中的项目
				const dragEl = getDragEl(oindex)
				let speed = 0
				let start = dragEl.index
				// 比较开始index和终点index，设置方向
				if(start < toIndex) {speed = 1} 
				if(start > toIndex) {speed = -1}
				if(!speed) return
				// 距离
				let distance = start - toIndex
				// 找到区间所有的项
				while(distance) {
					distance += speed
					// 目标
					const target = isDrag.value ? (dragEl.index += speed)  : (start += speed) 
					let targetOindex = cloneList.value.findIndex(item => item.index == target && item.content != dragEl.content)
					if (targetOindex == oindex) return
					if (targetOindex < 0) {targetOindex = cloneList.value.length - 1}
					let targetEl = cloneList.value[targetOindex]
					if(!targetEl) return;
					// 上一个index
					const lastIndex = target - speed
					const activeEl = cloneList.value[oindex]
					const rect = gridRects[lastIndex]
					targetEl.x = rect.x
					targetEl.y = rect.y
					targetEl.oldindex = targetEl.index
					targetEl.index = lastIndex
					activeEl.oldindex = activeEl.index //oIndex
					activeEl.index = toIndex
					// 到达终点，如果是拖拽则不处理
					if(!distance && !isDrag.value) {
						const rect = gridRects[toIndex]
						const {x, y} = position||rect
						activeEl.x = dragEl.x = x
						activeEl.y = dragEl.y = y
						// triggerRef(cloneList)
						if(emit) {
							emitting()
						}
					}
				}
			}
			/**
			 * 为区分是主动调用还是内部方法
			 */
			const move = (oindex: number, toIndex: number) => {
				active.value = -1
				isDrag.value = false
				_move(oindex, toIndex)
			}
			// 临时处理 待有空再完善
			const REMOVE_TIME = 400
			let removeTimer = null
			const remove = (oindex: number) => {
				active.value = -1
				isDrag.value = false
				
				clearTimeout(removeTimer)
				const item = cloneList.value[oindex]
				if(props.disabled || !item) return
				item.show = false
				const after = cloneList.value.length - 1
				_move(oindex, after, item, false)
				setAfter(gridRects[after])
				maxIndex.value--
				const _remove = (_index = oindex) => {
					// 小程序 删除会闪一下 所以先关闭动画再开启
					// animation.value = false
					const row = Math.ceil((cloneList.value.length - 1 + extra.value) / props.column)
					if( row < rows.value) {
						leaveRow.value = (rows.value - row)
					}
					cloneList.value.splice(_index, 1)[0]
					emitting()
					removeTimer = setTimeout(() => {
						leaveRow.value = 0
					},  REMOVE_TIME)
				} 
				_remove()
			}
			const push = (...args: any) => {
				if(props.disabled) return
				if(Array.isArray(args)) {
					Promise.all(args.map(async item => await add(item, true))).then(emitting)
				}
			}
			const add = (content: any, after: boolean) => {
				return new Promise((resolve) => {
					const item = createGrid(content, after ? null : {x: -100, y:0})
					item.class = 'l-drag-enter'
					cloneList.value.push(item)
					const length = cloneList.value.length - 1
					nextTick(() => {
						sleep(() => {
							item.class = 'l-drag-leave'
							_move(length, (after ? length : 0), null, false)
							triggerRef(cloneList)
							resolve(true)
						})
					})
					
				})
			}
			const unshift = (...args: any) => {
				if(props.disabled) return
				if(Array.isArray(args)) {
					Promise.all(args.map(async (item) => await add(item))).then(emitting)
				}
			}
			
			// 暂时先简单处理，待有空再完善
			const shift = () => {
				if(!cloneList.value.length) return
				remove(cloneList.value.findIndex(item => item.index == 0) || 0)
			}
			const pop = () => {
				const length = cloneList.value.length-1
				if(length < 0 ) return
				remove(cloneList.value.findIndex(item => item.index == length) || length)
			}
			// const splice = (start, count, ...context) => {
			// 	// 暂未实现
			// }
			const clear = () => {
				isInit.value = isDrag.value = false
				maxIndex.value = colmunId.value = active.value = -1
				cloneList.value = []
				gridRects = []
			}
			const init = () => {
				clear()
				createGridRects()
				nextTick(() => {
					updateList(props.list)
					isInit.value = true
				})
			}
			let count = 0
			const getRect = () => {
				count++
				// #ifndef APP-NVUE
				uni.createSelectorQuery().in(app.proxy).select('.l-drag').boundingClientRect((res: UniNamespace.NodeInfo) => {
					if(res) {
						areaWidth.value = res.width || 0
						// 小程序居然无法响应式？
						init()
					}
				}).exec()
				// #endif
				// #ifdef APP-NVUE
				sleep(() => {
					nextTick(() => {
						dom.getComponentRect(dragRef.value, (res) => {
							if(!res.size.width && count < 5) {
								getRect()
							} else {
								areaWidth.value = res.size.width || 0
								init()
							}
						})
					})
				})
				// #endif
			}
			onMounted(getRect)
			onUnmounted(clear)
			watch(() => props.list, init)
			
			// #ifdef VUE3
			expose({
				remove,
				// add,
				move,
				push,
				unshift,
				shift,
				pop
			})
			// #endif
			return {
				// #ifdef APP-NVUE
				dragRef,
				// #endif
				cloneList,
				
				areaStyles,
				innerStyles,
				viewStyles,
				
				setDisabled,
				isDisabled,
				isReset,
				isDrag,
				
				active,
				animation,
				
				afterEl,
				ghostEl,
				beforeEl,
				
				touchStart,
				touchMove,
				touchEnd,
				
				remove,
				// add,
				move,
				push,
				unshift,
				// shift,
				// pop,
				props
				// isDelete: props.delete,
				// ...toRefs(props)
			}
		}
	})
	
	
	
	
	
	
	
</script>
<style lang="scss">
	@import './index';
</style>
