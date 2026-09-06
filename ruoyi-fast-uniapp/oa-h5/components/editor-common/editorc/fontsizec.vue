<template>

	<view class="fontsizec-warp">
		<text class="editor-ul_label">文字格式</text>
		<view class="editor-ul_font">
			<view class="editor-ul_fontOne faj bg">
				<text :class="[{'bgColor': item.check}, 'iconfont', item.ico]" v-for="(item) in fontList" :key="item.name" @tap="formatFn(item, 'fontOne')"></text>
			</view>
			<view class="editor-ul_fontTwo faj bg">
				<text :class="{'bgColor': (isFontTwoClass==item.value)}" v-for="(item) in fontTwoList" :key="item.value" :style="`fontSize:${item.value.replace('px', '')*2}rpx`"
					@tap="formatFn(item, 'fontTwo')">{{item.ico}}</text>
			</view>
		</view>
		<text class="editor-ul_label">文字颜色</text>
		<view class="editor-ul_color faj bg">
			<text class="text" :class="{'selectFontColor': (isFontColorClass==item.value)}" v-for="item in fontColorList" :key="item.value" :style="{backgroundColor:item.value}"
				@tap="formatFn(item, 'color')"></text>
		</view>
		<text class="editor-ul_label">高亮颜色</text>
		<view class="editor-ul_color editor-ul_bgcolor faj bg">
			<text class="text" :class="{'selectFontColor': (isBackgroundColorClass==item.value)}" v-for="item in backgroundColorList" :key="item.value"
				:style="{backgroundColor:item.value}" @tap="formatFn(item, 'bgcolor')"></text>
		</view>
		<text class="editor-ul_label">对齐方式</text>
		<view class="editor-ul_align faj bg">
			<text :class="[{'bgColor': (isAligenListClass==item.value)}, 'iconfont', item.ico]" v-for="item in aligenList" :key="item.value" @tap="formatFn(item, 'align')"></text>

		</view>
	</view>

</template>

<script>
	export default {
		props: [ 'editorCtx' ],
		data () {
			return {
				// 字体数据
				fontList: [
					{ check: false, name: 'bold', value: '', ico: 'icon-jiacu' },
					{ check: false, name: 'italic', value: '', ico: 'icon-bianji-qingxie' },
					{ check: false, name: 'underline', value: '', ico: 'icon-ziyuan' },
					{ check: false, name: 'strike', value: '', ico: 'icon-strikethrough' },
				],
				fontTwoList: [
					{ check: false, name: 'fontSize', value: '12px', ico: '小' },
					{ check: false, name: 'fontSize', value: '16px', ico: '标准' },
					{ check: false, name: 'fontSize', value: '20px', ico: '大' },
					{ check: false, name: 'fontSize', value: '24px', ico: '超大' },
				],
				// 文字颜色
				fontColorList: [
					{ check: false, name: 'color', value: '#000000' },
					{ check: false, name: 'color', value: '#ff0000' },
					{ check: false, name: 'color', value: '#00ff00' },
					{ check: false, name: 'color', value: '#0000ff' },
					{ check: false, name: 'color', value: '#ffaa00' },
					{ check: false, name: 'color', value: '#9e9e9e' },
					{ check: false, name: 'color', value: '#aa00ff' },
				],
				// 高亮颜色
				backgroundColorList: [
					{ check: false, name: 'backgroundColor', value: '#ffffff' },
					{ check: false, name: 'backgroundColor', value: '#f44336' },
					{ check: false, name: 'backgroundColor', value: '#8bc34a' },
					{ check: false, name: 'backgroundColor', value: '#00bcd4' },
					{ check: false, name: 'backgroundColor', value: '#ffeb3b' },
					{ check: false, name: 'backgroundColor', value: '#d9d6d6' },
					{ check: false, name: 'backgroundColor', value: '#9c27b0' },
				],
				// 对齐
				aligenList: [
					{ check: false, name: 'align', value: 'left', ico: 'icon-juzuoduiqi' },
					{ check: false, name: 'align', value: 'center', ico: 'icon-juzhong' },
					{ check: false, name: 'align', value: 'right', ico: 'icon-juyouduiqi' },
				],
				styleList: [],
				styleListOld: [],
				// 选中
				isFontTwoClass: '16px',
				isFontColorClass: '#000000',
				isBackgroundColorClass: '#ffffff',
				isAligenListClass: 'left',
			}
		},
		methods: {
			forFn ( arr, value ) {
				arr.forEach( L => {
					if ( L.value == value ) {
						L.check = true
					} else {
						L.check = false
					}
				} )
			},
			iterationFn ( value, type ) {
				if ( type == 'fontTwo' ) {
					this.forFn( this.fontTwoList, value )
				} else if ( type == 'color' ) {
					this.forFn( this.fontColorList, value )
				} else if ( type == 'bgcolor' ) {
					this.forFn( this.backgroundColorList, value )
				} else if ( type == 'align' ) {
					this.forFn( this.aligenList, value )
				}
			},

			formatFn ( item, type, i ) {
				// this.onClickFn()
				let { name, value, ico } = item
				if ( type == 'fontOne' ) {
					item.check = !item.check
				} else if ( type == 'fontTwo' ) {
					this.isFontTwoClass = item.value
					this.iterationFn( item.value, type )
				} else if ( type == 'color' ) {
					this.isFontColorClass = item.value
					this.iterationFn( item.value, type )
				} else if ( type == 'bgcolor' ) {
					this.isBackgroundColorClass = item.value
					this.iterationFn( item.value, type )
				} else if ( type == 'align' ) {
					this.isAligenListClass = item.value
					this.iterationFn( item.value, type )
				}
				if ( type !== 'fontOne' ) {
					this.editorCtx.format( name, value )
				} else {
					this.editorCtx.format( name )
				}
				// onClickFn(e) {
				document.activeElement.blur()
				requestAnimationFrame( function () {
					document.activeElement.blur() // 失去焦点
				} )
				// },
				// this.styleList.push({name, value, ico, type})
			}
		}
	}
</script>

<style>
	@import url("../static/ico/iconfont.css");
	@import url("../static/editor.css");

	.selectFontColor {
		position: relative;
	}

	.selectFontColor::after {
		position: absolute;
		content: '√';
		top: 50%;
		left: 50%;
		color: #fff;
		font-weight: bolder;
		transform: translate(-50%, -50%);
	}
</style>
