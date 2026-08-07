<style>
	.vue-4123213,.vue-4123213 .el-tree{background-color: #F5F5F5;}
	.vue-4123213 .el-tree-node{margin: 0.15em 0 !important;}
	/* 悬浮时颜色更深一点 */
	.vue-4123213 .el-tree-node__content:hover{background-color: #CFE8FC !important;}
</style>

<template>
	<div class="vue-box vue-4123213">
		<div style="padding: 2em;">
			<div class="c-title">菜单预览</div>
			<!-- 树插件 -->
			<el-tree
				ref="tree"
				:data="dataList"
				node-key="id"
				:default-expand-all="true"
				>
				<span class="custom-tree-node" slot-scope="s">
					<span style="color: #2D8CF0;" v-if="s.data.is_show == undefined || s.data.is_show == true">{{ s.data.name }}</span>
					<span style="color: #999;" v-if="s.data.is_show == false">{{ s.data.name }} (隐藏)</span>
					<span style="color: #999;" v-if="s.data.info">&emsp;———— {{s.data.info}} </span>
				</span>
			</el-tree>
		</div>
	</div>
</template>

<script>
	import menuList from './../../sa-resources/sa-menu-list.js';
	import sa_admin_code_util from './../../sa-resources/index/admin-util.js';	// admin代码util
	export default {
		data() {
			return {
				dataList: [],	// 数据集合 
			}
		},
		methods: {
			
		},
		created: function(){
			// 全部
			this.sa.ajax2('/SysMenu/getList', function(){
				let menuList2 = menuList;
				menuList2 = sa_admin_code_util.arrayToTree(menuList2);	// 一维转tree 
				menuList2 = sa_admin_code_util.refMenuList(menuList2);	// 属性处理 
				this.dataList = menuList2;	// 数据  
			}.bind(this));
		}
	}
</script>
