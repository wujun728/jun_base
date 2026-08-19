<template xlang="wxml">
	<view class="tki-tree">
		<view class="tki-tree-mask" :class="{'show':showTree}" @tap="_cancel"></view>
		<view class="tki-tree-cnt" :class="{'show':showTree}">
			<view class="tki-tree-bar">
				<view class="tki-tree-bar-cancel" :style="{'color':cancelColor}" hover-class="hover-c" @tap="_cancel">取消</view>
				<view class="tki-tree-bar-title" :style="{'color':titleColor}">{{title}}</view>
				<view class="tki-tree-bar-confirm" :style="{'color':confirmColor}" hover-class="hover-c" @tap="_confirm">确定</view>
			</view>
			<view class="tki-tree-view">
				<scroll-view class="tki-tree-view-sc" :scroll-y="true">
					<block v-for="(item, index) in treeList" :key="index">
						<view class="tki-tree-item" :style="[{
							paddingLeft: item.rank*15 + 'px',
							zIndex: item.rank*-1 +50
						}]"
						 :class="{
							border: border === true,
							show: item.show,
							last: item.lastRank,
							showchild: item.showChild,
							open: item.open,
						}">
							<view class="tki-tree-label" @tap.stop="_treeItemTap(item, index)">
								<image class="tki-tree-icon" :src="item.lastRank ? lastIcon : item.showChild ? currentIcon : defaultIcon"></image>
								{{item.name}}
							</view>
							<view class="tki-tree-check" @tap.stop="_treeItemSelect(item, index)" v-if="selectParent?true:item.lastRank">
								<view class="tki-tree-check-yes" v-if="item.checked" :class="{'radio':!multiple}" :style="{'border-color':confirmColor}">
									<view class="tki-tree-check-yes-b" :style="{'background-color':confirmColor}"></view>
								</view>
								<view class="tki-tree-check-no" v-else :class="{'radio':!multiple}" :style="{'border-color':confirmColor}"></view>
							</view>
						</view>
					</block>
				</scroll-view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "tki-tree",
		props: {
			lazy: {
				type: Boolean,
				default: false
			},
			range: {
				type: Array,
				default: function() {
					return []
				}
			},
			idKey: {
				type: String,
				default: 'id'
			},
			rangeKey: {
				type: String,
				default: 'label'
			},
			title: {
				type: String,
				default: ''
			},
			multiple: { // 是否可以多选
				type: Boolean,
				default: false
				// default: true
			},
			selectParent: { //是否可以选父级
				type: Boolean,
				default: false
			},
			foldAll: { //折叠时关闭所有已经打开的子集，再次打开时需要一级一级打开
				type: Boolean,
				default: false
			},
			confirmColor: { // 确定按钮颜色
				type: String,
				default: '' // #07bb07
			},
			cancelColor: { // 取消按钮颜色
				type: String,
				default: '' // #757575
			},
			titleColor: { // 标题颜色
				type: String,
				default: '' // #757575
			},
			currentIcon: { // 展开时候的ic
				type: String,
				default: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFEAAABRCAYAAACqj0o2AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MEQ0QTM0MzQ1Q0RBMTFFOUE0MjY4NzI1Njc1RjI1ODIiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MEQ0QTM0MzU1Q0RBMTFFOUE0MjY4NzI1Njc1RjI1ODIiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDowRDRBMzQzMjVDREExMUU5QTQyNjg3MjU2NzVGMjU4MiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowRDRBMzQzMzVDREExMUU5QTQyNjg3MjU2NzVGMjU4MiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PidwepsAAAK0SURBVHja7JxbTsJAFIYHww7ciStgCeoGvGxAiOsgURegoL5720AXYLiIr0aJviq3Zx3PhIEnKG3ndtr+f3KixrSUj/ZjzjClIqUUiFm2gAAQAREQEUAEREAERAQQAREQAREBREAEREBEEqa67h9RFDWllDv0awWYlqlQHmu1WjMRRMoV1QFttA12y3xRtdNczq8EsE4/f8FumX2q77ROvNXk8UGMEKdUz6tYJHljaZAbuyUH+UR1to5BEohTuqwPCeS4pAA/qY6o/kyHOAMCeRK3owJnj+rH1jjxhqpVsstaebCz6TmnHWyXyY+xHjSBWBY/bvSgadtXBj9u9KCN3rnIfkzkQVsTEEX0Y2IP2oKo/HhMICcFAThUcwVZNGU6FdbX/XURzkbVF4+ybGhjPrFdgP66QdXNurGtSdk6Xdb9nAJ8oDo3OQlsQZzkdPw41ONBo6vI5scDefRjZg+6gpg3Pxp50CXEvPjR2IOuIXL3oxUPuobI3Y9WPOgDIlc/WvOgL4iL/vqFCcD7LH0xB4hj7cfQ/fWH9qCT+FhG0tN+DBk1PzjOM0SVllixcsBT1AvYc/kAPhc0hRg/3uvxoCgKRN9+dOrBUBB9+9GpB0NC9OVH5x4MDdG1H714kANEV3705kEOEBf9dcPi/lQnsuvLg1wgSu3Ha0v7Uh4MMgUXeuG71H407a+VBy9CPQkOdw+MtB+nGbd/D+FBbhBNxo9SjwcngJjNj0E9yBFiFj8G9SBXiGn8GNyDnCEm8SMLD3KHGOdHNh7kDjHOj2w8mAeIi/5arX+c6b/fxHz9oADEdGdjR/fXCw/OOB5oVfCOgnepz8IB14PMw03jCmTE+QBx5z0gAmKSqK9OUF+hcAeIhu/QYr4Qie8rjW83hhMBERARQAREQAREBBABERCLnH8BBgA+TQI7U4t53AAAAABJRU5ErkJggg=='
			},
			defaultIcon: { // 折叠时候的ic
				type: String,
				default: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFEAAABRCAYAAACqj0o2AAACE0lEQVR4Xu3c200DMRCF4XEltJAOkEugA+ggpUAHoQMqiFMCdEAJUMEiS4mEELlIO7bPOeN9i6K1rG/952myyea1WiCtXmEuYBPR4RBMxInoIOCwhOtJLKVszWyXc/5y2BvNEq6I+/3+kFK6M7OHnPM7jcLKjbZAvD/uaZtzflm5P4rbWyJWgDcze1LPuzVihfxUz7sH4ilJ2bx7Isrm3RtRMu8RiHJ5j0SUyXs0okTeCIj0eSMh0uaNhkiZNyIiXd7IiDR5oyNS5M2ACJ83EyJs3myIkHkzIsLlzYwIkzc7IkTeCojD81ZCHJa3GuKQvBURu+etjNgtb3XELnlHQGyedyTEZnlHQ2ySd0RE97wjI7rlHR3RJe+JeIrbLOecD6ePpZQ6W1kn2epo4MUrPOKyLN8ppYq1+y1VStncOjIdGnFZlo+U0uOtWOeOY2TE12Ouq//pEA7xXL7XfvcufR8K0Svfv6CREN3yDYfYIt9QiK3yjYTYLF95xB75SiP2ylcZsVu+cogj8pVCHJWvEuKwfOkREfKlRkTJlxkRJl86RMR8qRBR82VChM0XHpEhX2hElnyREWnyhUNkzBcKkTVfJETafIcjKuQ7FFEl35GIMvl2R1TMtyuiar49EWXzbY5oZpv/hibXTF2h3+s60FRKeT6+3TjMS3nrA3ZFRD8xrfY3ER1kJ+JEdBBwWGKeRAfEH1wS5WFZSDB/AAAAAElFTkSuQmCC'
			},
			lastIcon: { // 没有子集的ic
				type: String,
				default: ''
			},
			border: { // 是否有分割线
				type: Boolean,
				default: false
			},
		},
		data() {
			return {
				showTree: false,
				treeList: [],
				selectIndex: -1,
				returnedItem: [] ,//定义一个空数组
				pids: [],
				ancestorsIds: [],
				childNums: [],
			}
		},
		computed: {},
		methods: {
			_show() {
				this.showTree = true
			},
			_hide() {
				this.showTree = false
			},
			_cancel() {
				this._hide()
				this.$emit("cancel", '');
			},
			_confirm() {
				// 处理所选数据
				let rt = [],
					obj = {};
				this.treeList.forEach((v, i) => {
					if (this.treeList[i].checked) {
						rt.push(this.treeList[i].id)
					}
				})
				this._hide()
				this.$emit("confirm", rt);
			},
			//扁平化树结构
			_renderTreeList(list = [], rank = 0, parentId = [], parents = []) {
				list.forEach(item => {
					this.treeList.push({
						id: item[this.idKey],
						name: item[this.rangeKey],
						source: item,
						parentId, // 父级id数组
						parents, // 父级id数组
						rank, // 层级
						showChild: false, //子级是否显示
						open: false, //是否打开
						show: rank === 0, // 自身是否显示
						hideArr: [],
						orChecked: item.checked ? item.checked : false,
						checked: item.checked ? item.checked : false,
						childNum: 0
					})
					
					if (Array.isArray(item.children) && item.children.length > 0) {
						// console.log(item)
						let parentid = [...parentId],
							parentArr = [...parents];
						delete parentArr.children
						parentid.push(item[this.idKey]);
						parentArr.push({
							[this.idKey]: item[this.idKey],
							[this.rangeKey]: item[this.rangeKey]
						})
						// lazy
						if(!this.lazy) {
							this._renderTreeList(item.children, rank + 1, parentid, parentArr)
						}
					} else {
						this.treeList[this.treeList.length - 1].lastRank = true;
					}
				})
			},
			// 处理默认选择
			_defaultSelect() {
				this.treeList.forEach((v, i) => {
					if (v.checked) {
						this.treeList.forEach((v2, i2) => {
							if (v.parentId.toString().indexOf(v2.parentId.toString()) >= 0) {
								v2.show = true
								if (v.parentId.includes(v2.id)) {
									v2.showChild = true;
									v2.open = true;
								}
							}
						})
					}
				})
			},
			getOwn(id, arr){
			  //利用foreach循环遍历
			  arr.forEach((item) => {
				//判断递归结束条件
				if(item[this.idKey] == id)
				{       
				  // 存储数据到空数组
				  this.returnedItem = item
				}
				  else if(item.children != null) //判断chlidren是否有数据
				{
				  //递归调用
				  this.getOwn(id, item.children);                 
				}                   
			  })
			  return this.returnedItem
			},
			setShow (id, arr, isShow) {
				arr.forEach((item, index) => {
					if(item.parentId.includes(id)) {
						this.treeList[index].showChild = isShow
						this.treeList[index].show = isShow
					} else if (item.children !== undefined) {
						this.setShow(id, item.children, isShow)
					}
				})
			},
			// 点击
			_treeItemTap(item, index) {
				// console.log(item)
				if (item.lastRank === true) {
					//点击最后一级时触发事件
					this.treeList[index].checked = !this.treeList[index].checked
					this._fixMultiple(index)
					return;
				}
				let id = item.id;
				item.showChild = !item.showChild;
				// qingqian
				if(item.showChild) {
					const range = this.range
					const parentIdArr = item.parentId
					// 找到当前元素
					const own = this.getOwn(id, range)
					const checkedChildren = own.children
					// 子元素插入的索引位置
					const nextIndex = this.treeList.findIndex(itemT => itemT.id === item.id)
					console.log(checkedChildren);
					if(checkedChildren === undefined || checkedChildren.length < 1) {
						return
					}
					// 子节点数量
					this.treeList[index].childNum = checkedChildren.length
					const newRank = item.rank + 1
					checkedChildren.forEach(itemC => {
						const childObj = {
							id: itemC[this.idKey],
							name: itemC[this.rangeKey],
							source: {},
							parentId: [item.id], // 父级id数组
							parents: [item], // 父级id数组
							rank: newRank, // 层级
							showChild: false, //子级是否显示
							open: false, //是否打开
							show: 1, // 自身是否显示
							hideArr: [],
							orChecked:  this.treeList[index].checked,
							checked:  this.treeList[index].checked,
						}
						if(!this.treeList.some(itemT => itemT.id === itemC[this.idKey])) {
							this.treeList.splice(nextIndex+1,0,childObj)
						}
					})
				}
				// 展开/隐藏子级/孙级
				let list = this.treeList
				item.open = item.showChild ? true : !item.open;
				list.forEach((childItem, i) => {
					if (item.showChild === false) {
						//隐藏所有子级
						if (!childItem.parentId.includes(id)) {
							return;
						}
						//TODO: 修改
						if (!this.foldAll) {
							if (childItem.lastRank !== true && !childItem.open) {
								childItem.showChild = false;
								this.setShow(childItem.id, this.treeList, false)
							}
							// 为隐藏的内容添加一个标记
							if (childItem.show) {
								childItem.hideArr[item.rank] = id
							}
						} else {
							if (childItem.lastRank !== true) {
								childItem.showChild = false;
								// 继续隐藏子级的的子级
								this.setShow(childItem.id, this.treeList, false)
							}
						}
						if(childItem.children !== undefined) {
							childItem.children.forEach((childItem1, i1) => {
								if(!childItem1.parentId.includes(childItem.id)) {
									return
								}
								childItem.children[i1].showChild = false
								childItem.children[i1].show = false
							})
						}
						childItem.show = false;
					} else {
						// 打开子集
						if (childItem.parentId[childItem.parentId.length - 1] === id) {
							childItem.show = true;
						}
						// 打开被隐藏的子集
						if (childItem.parentId.includes(id) && !this.foldAll) {
							// console.log(childItem.hideArr)
							if (childItem.hideArr[item.rank] === id) {
								childItem.show = true;
								if (childItem.open && childItem.showChild) {
									childItem.showChild = true
								} else {
									childItem.showChild = false
								}
								childItem.hideArr[item.rank] = null
							}
						}
					}
				})
			},
			// 通过父id处理子级
			syncChecked (trees, pid, checked) {
				trees.forEach((item,index) => {
					if(item.parentId.includes(pid)) {
						this.treeList[index].checked = checked
						this.syncChecked(trees, item.id, checked)
					} else if(item.children !== undefined) {
						this.syncChecked(item.children, pid, checked)
					}
				})
			},
			// 获取父级往上所有层级的id 并同步状态
			setAncestors (pids, checked) {
				this.treeList.forEach((item,index) => {
					if(pids.includes(item.id)) {
						if(checked && this.childNums[item.id] !== undefined && item.childNum === this.childNums[item.id]) {
						// 子级全部选中, 父级才选中
							this.treeList[index].checked = true
						} else {
							this.treeList[index].checked = false
						}
						this.setAncestors(item.parentId, checked)
					} 
				})
			},
			_treeItemSelect(item, index) {
				this.treeList[index].checked = !this.treeList[index].checked
				// 选父级, 子级自动全选
				this.syncChecked(this.treeList, item.id, this.treeList[index].checked)
			
				if(item.rank > 0) {
					item.parentId.forEach((pid, indexP) => {
						const parent = this.treeList.filter(i => i.id === pid)
						const childNum = parent.length > 0 ? parent[0].childNum : 0
						if(this.childNums[pid] === undefined) {
							this.childNums[pid] = 1
						} else if(this.childNums[pid] < childNum) {
							this.childNums[pid]++
						}
					})
					//子级选择/选满/取消选择, 父级往上同步状态
					this.setAncestors(item.parentId, this.treeList[index].checked)
				}
				this._fixMultiple(index)
			},
			// 处理单选多选
			_fixMultiple(index) {
				if (!this.multiple) {
					// 如果是单选
					this.treeList.forEach((v, i) => {
						if (i != index) {
							this.treeList[i].checked = false
						} else {
							this.treeList[i].checked = true
						}
					})
				}
			},
			// 重置数据
			_reTreeList() {
				this.treeList.forEach((v, i) => {
					this.treeList[i].checked = v.orChecked
				})
			},
			_initTree(range = this.range){
				this.treeList = [];
				this._renderTreeList(range);
				this.$nextTick(() => {
					this._defaultSelect(range)
				})
			}
		},
		watch: {
			range(list) {
				this._initTree(list);
			},
			multiple() {
				if (this.range.length) {
					this._reTreeList();
				}
			},
			selectParent() {
				if (this.range.length) {
					this._reTreeList();
				}
			},
		},
		mounted() {
			this._initTree();
		}
	}
</script>

<style scoped>
	@import "./style.css";
</style>
