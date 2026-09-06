<template>
	<view class="titlep-warp">
		<text class="editor-ul_label">标题格式</text>
		<view class="editor-ul_header faj bg">
			<text :class="{'bgColor':(isHeaderClass == item.value)}" v-for="item in headerList" :key="item.value" @tap="headerListFn(item)">{{item.ioc}}</text>
		</view>
		<text class="editor-ul_label">段落</text>
		<view class="editor-ul_header faj bg">
			<text :class="[{'bgColor':(isHeaderClass == item.value)}, 'iconfont', item.ioc]" v-for="item in pUllist" :key="item.value" @tap="pUllistFn(item)"></text>
		</view>
	</view>
</template>

<script>
	export default {
		data () {
			return {
				headerList: [
					{ name: 'header', value: 'h1', ioc: 'H1' },
					{ name: 'header', value: 'h2', ioc: 'H2' },
					{ name: 'header', value: 'h3', ioc: 'H3' },
					{ name: 'header', value: 'h4', ioc: 'H4' },
					{ name: 'header', value: 'h5', ioc: 'H5' },
					{ name: 'header', value: 'h6', ioc: 'H6' },
				],
				pUllist: [
					{ name: 'list', value: 'ordered', ioc: 'icon-24gl-ol' },
					{ name: 'list', value: 'bullet', ioc: 'icon-24gf-ul' },
					{ name: 'list', value: 'check', ioc: 'icon-liebiao5' },
				],
				isHeaderClass: ''
			}
		},
		methods: {
			headerListFn ( item ) {
				this.isHeaderClass = item.value
				this.$emit( 'headerListFn_emit', item )
			},
			pUllistFn ( item ) {
				this.isHeaderClass = item.value
				this.$emit( 'pUllistFn_emit', item )
			}
		}
	}
</script>

<style>
	@import url("../static/ico/iconfont.css");
	@import url("../static/editor.css");
</style>
