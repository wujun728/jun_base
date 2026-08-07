<style scoped>
</style>

<template>
	<el-dialog
		:title="title"
		:visible.sync="isShow"
		width="800px"
		top="5vh"
		:before-close="handleClose"
		:append-to-body="true"
		:destroy-on-close="true"
		:close-on-click-modal="false"
		custom-class="full-dialog"
		>
		<div class="vue-box" style="height: 65vh;">
			<div style="padding: 1em 2em;">
				<el-form>
					<div class="c-title">所有权限</div>
					<!-- 此扩展能递归渲染一个权限树，点击深层次节点，父级节点中没有被选中的节点会被自动选中，单独点击父节点，子节点会全部 选中/去选中 -->
					<el-tree
						ref="tree"
						:data="dataList"
						show-checkbox 
						node-key="id"
						:default-expand-all="true"
						:default-checked-keys="select_list" 
						:expand-on-click-node="false"
						:check-on-click-node="true"
						:check-strictly="true"
						@node-click="node_click"
						@check="node_click"
						>
						<span class="custom-tree-node" slot-scope="s">
							<span style="color: #2D8CF0;" v-if="s.data.is_show == undefined || s.data.is_show == true">{{ s.data.name }}</span>
							<span style="color: #999;" v-if="s.data.is_show == false">{{ s.data.name }} (隐藏)</span>
							<span style="color: #999;" v-if="s.data.info">&emsp;———— {{s.data.info}} </span>
						</span>
					</el-tree>
				</el-form>
			</div>
		</div>
		<span slot="footer" class="dialog-footer">
			<el-button size="small" @click="isShow = false">取 消</el-button>
			<el-button size="small" type="primary" @click="ok()">确 定</el-button>
		</span>
	</el-dialog>
</template>

<script>
	import menuList from './../../sa-resources/sa-menu-list.js';
	import sa_admin_code_util from './../../sa-resources/index/admin-util.js';	// admin代码util
	export default {
		data() {
			return {
				isShow: false,
				role_id: 0,
				title: '',
				dataList: [],	// 数据集合 
				select_list: [],	// 默认选中
			}
		},
		methods: {
			// 打开
			show: function(role_id, role_name) {
				this.isShow = true;
				this.role_id = role_id;
				this.title = '为[' + role_name + ']分配权限';
				
				// 加载 
				this.sa.ajax2('/SysMenu/getList?role_id=' + role_id, function(){
					let menuList2 = menuList;
					menuList2 = sa_admin_code_util.arrayToTree(menuList2);	// 一维转tree 
					menuList2 = sa_admin_code_util.refMenuList(menuList2);	// 属性处理 
					this.dataList = menuList2;	// 数据  
					this.select_list = ['11','1', '1-1', '1-2', '1-3', '1-3-1', '2', '2-1', '3', '3-1', '3-2'];	// 选中的列表 
				}.bind(this));
			},
			// 关闭
			handleClose: function(done) {
				done();
			},
			// 保存
			ok: function(){
				var str = '';
				this.$refs.tree.getCheckedKeys().forEach(function(ts){
					str += '&ids=' + ts;
				})
				var url = '/MRM/updateRoleMenu?role_id=' + this.role_id + str;
				this.sa.ajax2(url,{},function () {
					this.sa.alert('成功', function(){
						this.isShow = false;
					}.bind(this));
				}.bind(this))
			},
			// 点击回调, 处理其子节点跟随父节点的选中
			node_click: function(node) {
				var is_select = this.$refs.tree.getCheckedKeys().indexOf(node.id) != -1;	// 此节点现在是否被选中 
				if(node.children){
					node.children.forEach(function(item) {
						this.$refs.tree.setChecked(item.id, is_select);
						// 递归
						if(item.children) {
							this.node_click(item);
						}
					}.bind(this))
				}
			}
		},
		created() {
			
		}
	}
</script>
