<template>
	<view class="wyb-table-box">
		<view v-if="loading" class="wyb-table-loading-box" :style="{
			width: width === 'auto' ? screenWidth : width,
			height: height === 'auto' ? '300rpx' : height,
			backgroundColor: loaderBgColor,
			borderTop: '1px solid' + borderColor,
			borderBottom: '1px solid' + borderColor,
			borderLeft: showLeftAndRightBorder ? '1px solid' + borderColor : 'none',
			borderRight: showLeftAndRightBorder ? '1px solid' + borderColor : 'none'}">
			<view class="loader-one" :style="{
				 width: loaderSize + 'rpx',
				 height: loaderSize + 'rpx',
				 borderTop: '3px solid ' + loadingColor.top,
				 borderRight: '3px solid ' + loadingColor.right,
				 borderBottom: '3px solid ' + loadingColor.bottom,
				 borderLeft: '3px solid ' + loadingColor.left}" />
		</view>
		<view v-if="!loading" class="wyb-table-scroll-view" :style="{
			width: width,
			height: height,
			borderTop: '1px solid' + borderColor,
			borderLeft: showLeftAndRightBorder ? '1px solid' + borderColor : 'none',
			borderRight: showLeftAndRightBorder ? '1px solid' + borderColor : 'none'}">
			<view class="wyb-table-header" :style="{borderBottom: '1px solid' + borderColor}">
				<view class="wyb-table-header-item" v-if="enableCheck" :style="{
					 minWidth: checkColWidth + 'rpx',
					 maxWidth: checkColWidth + 'rpx',
					 minHeight: minHeight[0] + 'rpx',
					 textAlign: textAlign,
					 justifyContent: textAlign === 'center' ? textAlign : (textAlign === 'left' ? 'flex-start' : 'flex-end'),
					 fontSize: fontSize[0] + 'rpx',
					 color: headerFtColor,
					 padding: padding[0] + 'rpx ' + (padding[1] || padding[0]) + 'rpx',
					 backgroundColor: headerBgColor,
					 borderRight: '1px solid' + borderColor,
					 zIndex: 30,
					 left: 0, 
					 color: headerFtColor,
					 backgroundColor: headerBgColor,
					 position: 'sticky'}">
						<view 
						 class="wyb-table-checkbox"
						 v-if="enableCheck === 'multiple'"
						 @tap.stop="onCheckAllTap"
						 :style="{
							width: checkColWidth * 0.5 + 'rpx',
							height: checkColWidth * 0.5 + 'rpx',
							backgroundColor: checkerBoxBgColor,
							border: '1px solid ' + checkerBorderColor}">
							<text 
							 class="iconfont icon-check"
							 v-show="checkAll"
							 :style="{
								color: checkerColor,
								backgroundColor: checkerBgColor,
								paddingTop: (fontSize[1] || fontSize[0]) * 0.15 + 'rpx',
								fontSize: (fontSize[1] || fontSize[0]) + 'rpx'}" />
						</view>
					 </view>
				<view ref="iosBug" class="wyb-table-header-item" v-for="(item, index) in headers" :key="item.key" @tap="onHeaderItemTap(index)"
				 :style="{
					 minWidth: (item.width || defaultColWidth) + 'rpx',
					 maxWidth: (item.width || defaultColWidth) + 'rpx',
					 minHeight: minHeight[0] + 'rpx',
					 textAlign: textAlign,
					 justifyContent: textAlign === 'center' ? textAlign : (textAlign === 'left' ? 'flex-start' : 'flex-end'),
					 fontSize: fontSize[0] + 'rpx',
					 fontWeight: headerWeight ? 'bold' : 'normal',
					 color: headerFtColor,
					 padding: padding[0] + 'rpx ' + (padding[1] || padding[0]) + 'rpx',
					 backgroundColor: headerBgColor,
					 borderRight: index === headers.length - 1 || (!showVertBorder && index !== 0) ? 'none' : '1px solid' + borderColor,
					 zIndex: index === 0 ? 20 : 0,
					 left: index === 0 && firstLineFixed ? (enableCheck ? checkColWidth + 'rpx' : 0) : 'auto', 
					 position: index === 0 ? 'sticky' : 'static'}">
					 <text :style="{marginLeft: autoSortShow(index) && textAlign !== 'left' ? fontSize[0] * 0.65 + 'rpx' : 0}">
						 {{item.label || emptyString}}
					 </text>
					 <view class="wyb-table-header-icon" v-if="autoSortShow(index)">
					 	<text class="iconfont icon-arrow-up" :style="{
							color: sortWays[sortWay] === 'asc' && sortActiveKey === item.key ? 
								headerFtColor : RGBChange(headerFtColor, 0.7, 'light'),
							fontWeight: 'normal',
							marginBottom: '-12px',
							transform: 'scale(0.4)'}" />
					 	<text class="iconfont icon-arrow-down" :style="{
							color: sortWays[sortWay] === 'inv' && sortActiveKey === item.key ? 
								headerFtColor : RGBChange(headerFtColor, 0.7, 'light'),
							fontWeight: 'normal',
							transform: 'scale(0.4)'}" />
					 </view>
				</view>
			</view>
			<view class="wyb-table-content">
				<view class="wyb-table-content-line" v-for="(content, cIndex) in contentsSort" :key="contentLineKey(content, cIndex)"
				 :style="{borderTop: cIndex === 0 ? 'none' : '1px solid' + borderColor}">
					<view class="wyb-table-content-item" v-if="enableCheck" :style="{
						 minWidth: checkColWidth + 'rpx',
						 maxWidth: checkColWidth + 'rpx',
						 textAlign: textAlign,
						 justifyContent: textAlign === 'center' ? textAlign : (textAlign === 'left' ? 'flex-start' : 'flex-end'),
						 fontSize: (fontSize[1] || fontSize[0]) + 'rpx',
						 minHeight: (minHeight[1] || minHeight[0]) + 'rpx',
						 padding: padding[0] + 'rpx ' + (padding[1] || padding[0]) + 'rpx',
						 borderRight: '1px solid' + borderColor,
						 zIndex: 21,
						 color: contentFtColor,
						 backgroundColor: checkerCellBgColor,
						 left: 0,
						 position: 'sticky'}">
							<view 
							 class="wyb-table-checkbox" 
							 @tap.stop="onCheckItemTap(cIndex)"
							 :style="{
								width: checkColWidth * 0.5 + 'rpx',
								height: checkColWidth * 0.5 + 'rpx',
								backgroundColor: checkerBoxBgColor,
								border: '1px solid ' + checkerBorderColor}">
								<text 
								 class="iconfont icon-check" 
								 v-show="contentsSort[cIndex].checked"
								 :style="{
									color: checkerColor,
									backgroundColor: checkerBgColor,
									paddingTop: (fontSize[1] || fontSize[0]) * 0.15 + 'rpx',
									fontSize: (fontSize[1] || fontSize[0]) + 'rpx'}" />
							</view>
						 </view>
					<view
					 class="wyb-table-content-item" 
					 v-for="(header, hIndex) in headers"
					 @tap.stop="onContentItemTap(cIndex, hIndex)"
					 :key="contentItemKey(header, hIndex)" 
					 :style="{
						 minWidth: (header.width || defaultColWidth) + 'rpx',
						 maxWidth: (header.width || defaultColWidth) + 'rpx',
						 textAlign: textAlign,
						 justifyContent: textAlign === 'center' ? textAlign : (textAlign === 'left' ? 'flex-start' : 'flex-end'),
						 fontSize: (fontSize[1] || fontSize[0]) + 'rpx',
						 textDecoration: autoTextDecoration(cIndex, hIndex),
						 color: autoContentColor(cIndex, hIndex),
						 backgroundColor: autoContentBgColor(cIndex, hIndex),
						 minHeight: (minHeight[1] || minHeight[0]) + 'rpx',
						 padding: padding[0] + 'rpx ' + (padding[1] || padding[0]) + 'rpx',
						 borderBottom: cIndex === contents.length - 1 ? '1px solid' + borderColor : 'none',
						 borderRight: hIndex === headers.length - 1 || (!showVertBorder && hIndex !== 0) ? 'none' : '1px solid' + borderColor,
						 zIndex: hIndex === 0 ? 20 : 0,
						 left: enableCheck ? checkColWidth + 'rpx' : 0,
						 position: hIndex === 0 && firstLineFixed ? 'sticky' : 'static'}">{{autoContentItem(cIndex, hIndex)}}</view>
				</view>
				<view v-if="computedCol.length !== 0" class="wyb-table-content-line" :style="{
					position: bottomComputedFixed ? 'sticky' : 'static',
					bottom: 0,
					zIndex: 25,
					borderTop: '1px solid' + borderColor}">
					<view class="wyb-table-content-item" v-if="enableCheck" :style="{
						 minWidth: checkColWidth + 'rpx',
						 maxWidth: checkColWidth + 'rpx',
						 textAlign: textAlign,
						 justifyContent: textAlign === 'center' ? textAlign : (textAlign === 'left' ? 'flex-start' : 'flex-end'),
						 fontSize: (fontSize[1] || fontSize[0]) + 'rpx',
						 minHeight: (minHeight[1] || minHeight[0]) + 'rpx',
						 padding: padding[0] + 'rpx ' + (padding[1] || padding[0]) + 'rpx',
						 borderBottom: '1px solid' + borderColor,
						 borderRight: '1px solid' + borderColor,
						 zIndex: 25,
						 color: contentFtColor,
						 backgroundColor: checkerCellBgColor,
						 left: 0,
						 position: 'sticky'}"></view>
					<view class="wyb-table-content-item" v-for="(header, index) in headers" :key="index"
					:style="{
						 minWidth: (header.width || defaultColWidth) + 'rpx',
						 maxWidth: (header.width || defaultColWidth) + 'rpx',
						 textAlign: textAlign,
						 justifyContent: textAlign === 'center' ? textAlign : (textAlign === 'left' ? 'flex-start' : 'flex-end'),
						 fontSize: (fontSize[1] || fontSize[0]) + 'rpx',
						 color: contentFtColor,
						 minHeight: (minHeight[1] || minHeight[0]) + 'rpx',
						 padding: padding[0] + 'rpx ' + (padding[1] || padding[0]) + 'rpx',
						 backgroundColor: index === 0 ? firstColBgColor : contentBgColor,
						 borderBottom: '1px solid' + borderColor,
						 borderRight: index === headers.length - 1 || (!showVertBorder && index !== 0) ? 'none' : '1px solid' + borderColor,
						 zIndex: index === 0 ? 20 : 0,
						 left: enableCheck ? checkColWidth + 'rpx' : 0,
						 position: index === 0 && firstLineFixed ? 'sticky' : 'static'}">
						{{autoBottomComputedItem(index)}}
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import Pinyin from '../../static/table/characterToPinyin.js'
	import isEqual  from '../../static/table/objEqual.js'
	export default {
		data() {
			return {
				bottomComputed: [],
				colorList: [],
				bgColorList: [],
				contentsSort: this.contents.slice(),
				oContentsSort: [],
				sortWay: 0,
				sortKeys: [],
				sortActiveKey: '',
				sortIsNumbers: [],
				checkAll: false,
				checkList: [],
				onload: true,
				event: {
					checkType: this.enableCheck,
					data: []
				},
				chars: '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
			}
		},
		computed: {
			loadingColor() {
				let color = this.loaderColor.slice()
				let rgbList = this.hexToRgb(color)
				let top = 'rgba(' + rgbList[0] + ',' + rgbList[1] + ',' + rgbList[2] + ', 0.3)'
				let bottom = 'rgba(' + rgbList[0] + ',' + rgbList[1] + ',' + rgbList[2] + ', 0.3)'
				let right = 'rgba(' + rgbList[0] + ',' + rgbList[1] + ',' + rgbList[2] + ', 0.3)'
				let left = 'rgb(' + rgbList[0] + ',' + rgbList[1] + ',' + rgbList[2] + ')'
				return {
					top,
					bottom,
					right,
					left
				}
			},
			contentLineKey() {
				return function(content, cIndex) {
					return this.randomString(32, this.chars)
				} 
			},
			contentItemKey() {
				return function(header, hIndex) {
					return this.randomString(16, this.chars)
				}
			},
			autoContentItem() {
				return function(cIndex, hIndex) {
					let content = this.contentsSort[cIndex]
					let header = this.headers[hIndex]
					let result = ''
					if (content[header.key] || content[header.key] === 0) {
						result = content[header.key]
						if (this.urlCol.length !== 0) {
							for (let i in this.urlCol) {
								let item = this.urlCol[i]
								if (header.key === item.key) {
									// 该单元格为链接
									result = content[header.key][0]
								}
							}
						}
						if (this.formatCol.length !== 0) {
							this.formatCol.forEach(item => {
								if (header.key === item.key) {
									let needRplace = new RegExp(`\#${item['key']}\#`, 'mg')
									result = item.template.replace(needRplace, result)
								}
							})
						}
						
					} else {
						result = this.emptyString
					}
					return result
				}
			},
			autoBottomComputedItem() {
				return function(index) {
					let bottomComputed = {}
					let needComputed = []
					this.computedCol.forEach(key => {
						let computedColData = []
						this.contentsSort.forEach(content => {
							computedColData.push(content[key] || '0')
						})
						needComputed.push(computedColData)
					})
					needComputed.forEach((item, index) => {
						let total = 0
						item.forEach(num => {
							total += parseFloat(num)
						})
						bottomComputed[this.computedCol[index]] = total
					})
					let header = this.headers[index]
					let result = this.computedCol.includes(header.key) ? 
						bottomComputed[header.key] : (index === 0 ? '总计' : this.emptyString)
					if (this.formatCol.length !== 0) {
						this.formatCol.forEach(item => {
							if (item.bottomComputedFormat) {
								if (header.key === item.key) {
									let needRplace = new RegExp(`\#${item['key']}\#`, 'mg')
									result = item.template.replace(needRplace, bottomComputed[item.key])
								}
							}
						})
					}
					return result
				}
			},
			autoTextDecoration() {
				return function(cIndex, hIndex) {
					let result = 'auto'
					let content = this.contentsSort[cIndex]
					let header = this.headers[hIndex]
					if (this.urlCol.length !== 0) {
						for (let i in this.urlCol) {
							let item = this.urlCol[i]
							if (header.key === item.key) {
								// 该单元格为链接
								if (content[header.key]) {
									result = 'underline'
								}
							}
						}
					}
					return result
				}
			},
			autoContentBgColor() {
				return function(cIndex, hIndex) {
					let result = this.contentBgColor
					let content = this.contentsSort[cIndex]
					let header = this.headers[hIndex]
					let keys = []
					// 先判断是不是首列，设置基础样式
					if (hIndex === 0) {
						result = this.firstColBgColor
					}
					// 再判断条件格式传没传值，设置条件样式
					if (this.valueFormat.length !== 0) {
						this.valueFormat.forEach(item => {
							keys.push(item.key)
						})
						if (keys.includes(header.key)) {
							// 该列开启了条件格式
							let key = header.key
							let type = this.valueFormat[keys.indexOf(key)].type
							let style = this.valueFormat[keys.indexOf(key)].style
							let range = this.valueFormat[keys.indexOf(key)].range || ''
							switch(type) {
								case 'bigger':
									if (parseFloat(content[key]) > range) {
										if (style.bgColor) result = style.bgColor
									}
									break
								case 'smaller':
									if (parseFloat(content[key]) < range) {
										if (style.bgColor) result = style.bgColor
									}
									break
								case 'equal':
									let val
									if (typeof range === 'number') val = parseFloat(content[key])
									else val = content[key]
									if (val === range) {
										if (style.bgColor) result = style.bgColor
									}
									break
								case 'range':
									if (parseFloat(content[key]) > range[0] && parseFloat(content[key]) < range[1]){
										if (style.bgColor) result = style.bgColor
									}
									break
								case 'average-bigger':
									let average = this.getAverage(key)
									if (parseFloat(content[key]) > average) {
										if (style.bgColor) result = style.bgColor
									}
									break
								case 'average-smaller':
									average = this.getAverage(key)
									if (parseFloat(content[key]) < average) {
										if (style.bgColor) result = style.bgColor
									}
									break
								case 'average-equal':
									average = this.getAverage(key)
									if (parseFloat(content[key]) === average) {
										if (style.bgColor) result = style.bgColor
									}
									break
							}
						}
					}
					return result
				}
			},
			autoContentColor() {
				return function(cIndex, hIndex) {
					let result = this.contentFtColor
					let content = this.contentsSort[cIndex]
					let header = this.headers[hIndex]
					let keys = []
					// 先判断是不是链接，设置基础样式
					if (this.urlCol.length !== 0) {
						for (let i in this.urlCol) {
							let item = this.urlCol[i]
							if (header.key === item.key) {
								// 该单元格为链接
								if (content[header.key]) {
									result = this.linkColor
								}
							}
						}
					}
					// 再判断条件格式传没传值，设置条件样式
					if (this.valueFormat.length !== 0) {
						this.valueFormat.forEach(item => {
							keys.push(item.key)
						})
						if (keys.includes(header.key)) {
							// 该列开启了条件格式
							let key = header.key
							let type = this.valueFormat[keys.indexOf(key)].type
							let style = this.valueFormat[keys.indexOf(key)].style
							let range = this.valueFormat[keys.indexOf(key)].range || ''
							switch(type) {
								case 'bigger':
									if (parseFloat(content[key]) > range) {
										if (style.color) result = style.color
									}
									break
								case 'smaller':
									if (parseFloat(content[key]) < range) {
										if (style.color) result = style.color
									}
									break
								case 'equal':
									let val
									if (typeof range === 'number') val = parseFloat(content[key])
									else val = content[key]
									if (val === range) {
										if (style.color) result = style.color
									}
									break
								case 'range':
									if (parseFloat(content[key]) > range[0] && parseFloat(content[key]) < range[1]){
										if (style.color) result = style.color
									}
									break
								case 'average-bigger':
									let average = this.getAverage(key)
									if (parseFloat(content[key]) > average) {
										if (style.color) result = style.color
									}
									break
								case 'average-smaller':
									average = this.getAverage(key)
									if (parseFloat(content[key]) < average) {
										if (style.color) result = style.color
									}
									break
								case 'average-equal':
									average = this.getAverage(key)
									if (parseFloat(content[key]) === average) {
										if (style.color) result = style.color
									}
									break
							}
						}
					}
					return result
				}
			},
			autoSortShow() {
				return function(hIndex) {
					let result = false
					let header = this.headers[hIndex]
					let keys = []
					// 判断排序是否传值
					if (this.sortCol.length !== 0 && this.sortKeys.length === 0) {
						this.sortCol.forEach(item => {
							keys.push(item.key)
						})
						this.sortKeys = keys
						if (keys.includes(header.key)) {
							result = true
						}
					} else if (this.sortCol.length !== 0) {
						if (this.sortKeys.includes(header.key)) {
							result = true
						}
					}
					return result
				}
			},
			screenWidth() {
				return `${uni.getSystemInfoSync()['screenWidth']}px`
			}
		},
		props: {
			headers: {
				type: Array,
				default() {
					return []
				}
			},
			contents: {
				type: Array,
				default() {
					return []
				}
			},
			emptyString: {
				type: String,
				default: '-'
			},
			width: {
				type: String,
				default: "100%"
			},
			height: {
				type: String,
				default: 'auto'
			},
			fontSize: {
				type: Array,
				default() {
					return [24]
				}
			},
			defaultColWidth: {
				type: Number,
				default: 176
			},
			headerWeight: {
				type: Boolean,
				default: true
			},
			minHeight: {
				type: Array,
				default() {
					return [70]
				}
			},
			headerBgColor: {
				type: String,
				default: '#24ABFD'
			},
			contentBgColor: {
				type: String,
				default: '#DADADA'
			},
			headerFtColor: {
				type: String,
				default: '#fff'
			},
			contentFtColor: {
				type: String,
				default: '#3e3e3e'
			},
			linkColor: {
				type: String,
				default: '#0024c8'
			},
			firstColBgColor: {
				type: String,
				default: '#DADADA'
			},
			firstLineFixed: {
				type: Boolean,
				default: false
			},
			textAlign: {
				type: String,
				default: 'center'
			},
			padding: {
				type: Array,
				default() {
					return [5, 10]
				}
			},
			borderColor: {
				type: String,
				default: '#fff'
			},
			urlCol: {
				type: Array,
				default() {
					return []
				}
			},
			computedCol: {
				type: Array,
				default() {
					return []
				}
			},
			bottomComputedFixed: {
				type: Boolean,
				default: true
			},
			valueFormat: {
				type: Array,
				default() {
					return []
				}
			},
			formatCol: {
				type: Array,
				default() {
					return []
				}
			},
			showLeftAndRightBorder: {
				type: Boolean,
				default: false
			},
			showVertBorder: {
				type: Boolean,
				default: true
			},
			sortCol: {
				type: Array,
				default() {
					return []
				}
			},
			sortWays: {
				type: Array,
				default() {
					return ['none', 'asc', 'inv']
				}
			},
			loading: {
				type: Boolean,
				default: false
			},
			loaderSize: {
				type: [String, Number],
				default: 50
			},
			loaderColor: {
				type: String,
				default: '#a3a3a3'
			},
			loaderBgColor: {
				type: String,
				default: '#f8f8f8'
			},
			enableCheck: {
				type: String,
				default: ''
			},
			checkColWidth: {
				type: [String, Number],
				default: '70'
			},
			checkerColor: {
				type: String,
				default: '#3e3e3e'
			},
			checkerBorderColor: {
				type: String,
				default: '#d3d3d3'
			},
			checkerBgColor: {
				type: String,
				default: 'rgba(0, 0, 0, 0)'
			},
			checkerBoxBgColor: {
				type: String,
				default: 'rgba(0, 0, 0, 0)'
			},
			checkerCellBgColor: {
				type: String,
				default: '#f1f1f1'
			}
		},
		watch: {
			headers(val) {
				this.$forceUpdate()
			},
			contents(val) {
				this.contentsSort = val.slice()
				if (this.onload) {
					this.contentsSort.forEach(item => {
						this.$set(item, 'checked', false)
					})
					this.oContentsSort = this.contentsSort.slice()
					this.onload = false
				}
				this.$forceUpdate()
			}
		},
		mounted() {
			this.contentsSort.forEach(item => {
				this.$set(item, 'checked', false)
			})
			this.oContentsSort = this.contentsSort.slice()
			if (this.sortCol.length !== 0) {
				this.sortActiveKey = this.sortCol[0].key
				uni.setStorageSync('lastSortActiveKey', this.sortActiveKey)
				this.doSort(this.sortCol[0].key, this.sortWays[this.sortWay], this.sortCol[0].isNumber)
			}
		},
		methods: {
			doSort(key, type, isNumber) {
				let arr = this.contentsSort
				if (type === 'asc') {
					// 升序
					if (isNumber) {
						arr.sort((a, b) => {
							let a1 = (parseFloat(a[key].toString().replace(/[^0-9]/ig, "")) || 0);
							let b1 = (parseFloat(b[key].toString().replace(/[^0-9]/ig, "")) || 0);
							a1 =  a[key] < 0 ? - a1 : a1;
							b1 =  b[key] < 0 ? - b1 : b1;
						    return a1 - b1
						})
					} else {
						arr.sort((a, b) => {
							let A = Pinyin.getSpell(a[key].charAt(0), function(charactor, spell) {
								return spell[1]
							}).charAt(0).charCodeAt()
							let B = Pinyin.getSpell(b[key].charAt(0), function(charactor, spell) {
								return spell[1]
							}).charAt(0).charCodeAt()
							return A - B
						})
					}
					
				} else if (type === 'inv') {
					// 倒序
					if (isNumber) {
						arr.sort((a, b) => {
						    let a1 = (parseFloat(a[key].toString().replace(/[^0-9]/ig, "")) || 0);
							let b1 = (parseFloat(b[key].toString().replace(/[^0-9]/ig, "")) || 0);
							a1 =  a[key] < 0 ? - a1 : a1;
							b1 =  b[key] < 0 ? - b1 : b1;
							return b1 - a1
						})
					} else {
						arr.sort((a, b) => {
							let A = Pinyin.getSpell(a[key].charAt(0), function(charactor, spell) {
								return spell[1]
							}).charAt(0).charCodeAt()
							let B = Pinyin.getSpell(b[key].charAt(0), function(charactor, spell) {
								return spell[1]
							}).charAt(0).charCodeAt()
							return B - A
						})
					} 
				} else {
					this.contentsSort = this.oContentsSort.slice()
				}
				if (this.enableCheck) {
					this.event.data.forEach(item => {
						this.contentsSort.forEach((content, index) => {
							if (isEqual(item.lineData, content)) {
								item.index = index
							}
						})
					})
				}
				this.$forceUpdate()
			},
			initBottomComputed() {
				let result = {}
				let needComputed = []
				this.computedCol.forEach(key => {
					let computedColData = []
					this.contentsSort.forEach(content => {
						computedColData.push(content[key] || '0')
					})
					needComputed.push(computedColData)
				})
				needComputed.forEach((item, index) => {
					let total = 0
					item.forEach(num => {
						total += parseFloat(num)
					})
					result[this.computedCol[index]] = total
				})
				this.bottomComputed = result
			},
			onHeaderItemTap(index) {
				let header = this.headers[index]
				const lastSortActiveKey = uni.getStorageSync('lastSortActiveKey') || ''
				if (this.sortCol.length !== 0) {
					if (this.sortKeys.includes(header.key)) {
						// 当前列开启了排序
						this.sortActiveKey = header.key
						uni.setStorageSync('lastSortActiveKey', this.sortActiveKey)
						if (this.sortWay < 2 && lastSortActiveKey === this.sortActiveKey) {
							this.sortWay++
						} else if (lastSortActiveKey !== this.sortActiveKey) {
							this.sortWay = 1
						} else if (this.sortWay >= 2) {
							this.sortWay = 0
						}
						let isNumber = this.sortCol[this.sortKeys.indexOf(header.key)].isNumber
						// console.log(header.key,this.sortWays[this.sortWay],isNumber)
						this.doSort(header.key, this.sortWays[this.sortWay], isNumber)
					}
				}
			},
			onContentItemTap(cIndex, hIndex) {
				let event = {}
				let content = this.contentsSort[cIndex]
				let header = this.headers[hIndex]
				let keys = []
				
				if (this.urlCol.length !== 0) {
					for (let i in this.urlCol) {
						let item = this.urlCol[i]
						keys.push(item.key)
					}
				}
				
				if (content[header.key]) {
					if (keys.includes(header.key)) {
						// 该单元格为链接
						switch(this.urlCol[keys.indexOf(header.key)].type) {
							case 'route':
								let url = content[header.key][1]
								if (content[header.key][2]) {
									url = `${url}?`
									Object.keys(content[header.key][2]).forEach(key => {
										url += `&${key}=${content[header['key']][2][key]}`
									})
								}
								uni.navigateTo({url})
								break
							case 'http':
								this.openURL(content[header.key][1])
								break
						}
					} else {
						event = {
							content: content[header.key],
							contentIndex: cIndex,
							header: header.label,
							headerIndex: hIndex,
							key: header.key,
							lineData: content
						}
						this.$emit('onCellClick', event)
					}
					
				} else {
					event = {
						content: '',
						contentIndex: cIndex,
						header: header.label,
						headerIndex: hIndex,
						key: header.key,
						lineData: content
					}
					if (keys.includes(header.key)) {
						// 该单元格为链接
						event['isLink'] = true
					}
					this.$emit('onCellClick', event)
				}
				
			},
			onCheckAllTap() {
				if (this.enableCheck === 'multiple') {
					let checkList = []
					this.contentsSort.forEach(item => {
						checkList.push(item.checked)
					})
					this.checkList = checkList
					if (!this.checkAll) {
						this.checkAll = true
						this.contentsSort.forEach(item => {
							item.checked = true
						})
						this.event.data = []
						this.contentsSort.forEach((content, index) => {
							this.event.data.push({
								index,
								lineData: content
							})
						})
						
					} else {
						this.checkAll = false
						this.event.data = []
						this.contentsSort.forEach(item => {
							item.checked = false
						})
					}
					this.$emit('onCheck', this.event)
				}
			},
			onCheckItemTap(cIndex) {
				let content = this.contentsSort[cIndex]
				if (this.enableCheck === 'single') {
					this.contentsSort.forEach((item, index) => {
						if (cIndex === index) {
							item.checked = !item.checked
						} else {
							item.checked = false
						}
					})
				} else if (this.enableCheck === 'multiple') {
					this.contentsSort[cIndex]['checked'] = !this.contentsSort[cIndex]['checked']
				}
				if (this.contentsSort[cIndex]['checked']) {
					if (this.enableCheck === 'single') {
						this.event.data = []
					}
					this.event.data.push({
						index: cIndex,
						lineData: this.contentsSort[cIndex]
					})
				} else {
					this.event.data.forEach(item => {
						if (item.index === cIndex) this.event.data.splice(this.event.data.indexOf(item), 1)
					})
					if (this.event.data.length === 0) {
						this.checkAll = false
					}
				}
				this.$forceUpdate()
				this.$emit('onCheck', this.event)
			},
			openURL(href) {
				// #ifdef APP-PLUS
				plus.runtime.openURL(href)
				// #endif
				// #ifdef H5
				window.open(href)
				// #endif
				// #ifdef MP
				uni.setClipboardData({
					data: href,
					success() {
						uni.showToast({
							title: '网址已复制，请在手机浏览器里粘贴该网址',
							icon: 'none'
						})
					}
				})
				// #endif
			},
			getAverage(key) {
				let numList = []
				this.contentsSort.forEach(content => {
					numList.push(parseFloat(content[key]) || 0)
				})
				return numList.reduce((a, b) => a + b) / numList.length
			},
			getTotal(key) {
				let numList = []
				this.contentsSort.forEach(content => {
					numList.push(parseFloat(content[key]) || 0)
				})
				return numList.reduce((a, b) => a + b)
			},
			RGBChange(color, level, type) {
				// 判断颜色类型
				let r = 0,
					g = 0,
					b = 0,
					hasAlpha = false,
					alpha = 1
				if (color.indexOf('#') !== -1) {
					// hex转rgb
					if (color.length === 4) {
						let arr = color.split('')
						color = '#' + arr[1] + arr[1] + arr[2] + arr[2] + arr[3] + arr[3]
					}
					let color16List = [color.substring(1, 3), color.substring(3, 5), color.substring(5, 7)]
					r = parseInt(color16List[0], 16)
					g = parseInt(color16List[1], 16)
					b = parseInt(color16List[2], 16)
			
				} else {
					hasAlpha = color.indexOf('a') !== -1
					let root = color.slice()
					let idx = root.indexOf('(') + 1
					root = root.substring(idx)
					let firstDotIdx = root.indexOf(',')
					r = parseFloat(root.substring(0, firstDotIdx))
					root = root.substring(firstDotIdx + 1)
					let secondDotIdx = root.indexOf(',')
					g = parseFloat(root.substring(0, secondDotIdx))
					root = root.substring(secondDotIdx + 1)
					if (hasAlpha) {
						let thirdDotIdx = root.indexOf(',')
						b = parseFloat(root.substring(0, thirdDotIdx))
						alpha = parseFloat(root.substring(thirdDotIdx + 1))
					} else {
						b = parseFloat(root)
					}
				}
			
				let rgbc = [r, g, b]
				// 减淡或加深
				for (var i = 0; i < 3; i++)
					type === 'light' ? rgbc[i] = Math.floor((255 - rgbc[i]) * level + rgbc[i]) : rgbc[i] = Math.floor(rgbc[i] * (1 -
						level))
			
				if (hasAlpha) {
					return `rgba(${rgbc[0]}, ${rgbc[1]}, ${rgbc[2]}, ${alpha})`
				} else {
					return `rgb(${rgbc[0]}, ${rgbc[1]}, ${rgbc[2]})`
				}
			},
			hexToRgb(color) {
				if (color.length === 4) {
					let arr = color.split('')
					color = '#' + arr[1] + arr[1] + arr[2] + arr[2] + arr[3] + arr[3]
				}
				let color16List = [color.substring(1, 3), color.substring(3, 5), color.substring(5, 7)]
				let r = parseInt(color16List[0], 16)
				let g = parseInt(color16List[1], 16)
				let b = parseInt(color16List[2], 16)
				return [r, g, b]
			},
			randomString(length, chars) {
			    var result = ''
			    for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)]
			    return result
			}
		}
	}
</script>

<style>
	@import '../../static/table/iconfont.wxss';
	@import '../../static/table/loader.wxss';
	.ios-header-bug {
		height: 0;
		width: 1px;
		opacity: 0;
	}
	
	.wyb-table-scroll-view {
		overflow: scroll;
		-webkit-overflow-scrolling: touch;
	}
	
	.wyb-table-scroll-view::-webkit-scrollbar {
		display: none;
		/* #ifdef MP-WEIXIN */
		width: 0;
		height: 0;
		/* #endif */
	}
	
	.wyb-table-loading-box {
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 500;
	}
	
	.wyb-table-header {
		position: sticky;
		top: 0;
		display: grid;
		grid-auto-flow: column;
		width: max-content;
		z-index: 25;
	}
	
	.wyb-table-header-item {
		flex: 1;
		display: flex;
		align-items: center;
		box-sizing: border-box;
		position: relative;
	}
	
	.wyb-table-header-icon {
		display: flex;
		flex-direction: column;
	}
	
	.wyb-table-content-line {
		display: grid;
		grid-auto-flow: column;
		width: max-content;
		position: relative;
	}
	
	.wyb-table-content-item {
		display: flex;
		flex-direction: row;
		align-items: center;
		box-sizing: border-box;
	}
	
	.wyb-table-checkbox {
		border-radius: 3px;
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
	}
	
	.icon-check {
		width: 100%;
		height: 100%;
		position: absolute;
		border-radius: 0;
		border-radius: 3px;
		font-weight: bold;
		box-sizing: border-box;
		transform: scale(1.1);
	}
</style>
