<style scoped>
	.vue-box{background-color: #FFF;}
	/* wang富文本编辑器 */
	.editor-item{width: 100%; height: auto;}
	.editor-item .editor-box{float: left; width: 80%; margin-top: 0px; margin-left: 0px;} 
</style>

<template>
	<el-dialog
		v-if="m"
		title="文章编辑"
		:visible.sync="isShow"
		width="950px"
		top="10vh"
		:append-to-body="true"
		:destroy-on-close="true"
		:close-on-click-modal="false"
		custom-class="full-dialog"
		>
		<div class="vue-box" style="height: 60vh;">
			<div class="c-panel">
				<el-form size="mini" v-if="m">
					<el-form-item label="文章标题：">
						<el-input size="mini" v-model="m.title" style="width: 700px;" placeholder="例如：2002年的第一场雪"></el-input>
					</el-form-item>
					<el-form-item label="文章标题：" class="editor-item">
						<div class="editor-box">
							<div id="editor" ref="editor" style="text-align:left"></div>
						</div>
					</el-form-item>
					<el-form-item label="是否公开：">
						<el-switch v-model="m.is_public" :active-value="1" :inactive-value="2"></el-switch>
						<span class="c-remark">关闭后，别人将不能看到你的发表</span>
					</el-form-item>
					<div style="margin-top: 10px;"></div>
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
	import mockData from './data-list.js';
	import E from 'wangeditor';	// 富文本编辑器 
	export default {
		data() {
			return {
				isShow: false,
				m: {}
			}
		},
		methods: {
			// 打开
			open: function(id) {
				this.isShow = true;
				if(id == 0) {	// id == 0 代表 是要增加
					this.m = this.create_m();
					this.create_editor();		// 创建富文本编辑器
				} else {
					// id 非 0 代表 是要修改
					mockData.data.forEach(function(item) {
						if(item.id == id) {
							this.m = item;
						}
					}.bind(this));
					this.create_editor();		// 创建富文本编辑器
				}
			},
			// 提交 
			ok: function() {
				// 开始增加或修改
				let self = this;
				if(this.m.id == 0) {
					this.sa.ajax2('/article/add', this.m, function() {
						self.sa.alert('增加成功', function() {
							self.$parent.f5();	// 父视图刷新   
							self.isShow = false;
						}); 
					});
				} else {
					this.sa.ajax2('/article/update', this.m, function(){
						self.sa.alert('修改成功', function(){
							self.$parent.f5();	// 父视图刷新   
							self.isShow = false;
						}); 
					});
				}
			},
			// 创建编辑器 
			create_editor: function() {
				this.$nextTick(function() {
					let editor = new E(this.$refs.editor);
					editor.customConfig.menus = [	// 规定出现的菜单 
						'head', 'fontSize', 'fontName', 'italic', 'underline', 'strikeThrough', 'foreColor', 'backColor', 'link', 'list',
						'justify', 'quote', 'emoticon', 'image', 'table', 'code', 'undo', 'redo' // 重复
					]
					editor.customConfig.debug = true; // debug模式
					editor.customConfig.uploadFileName = 'file'; // 图片流name
					editor.customConfig.withCredentials = true; // 跨域携带cookie
					editor.customConfig.uploadImgShowBase64 = true   	// 使用 base64 保存图片
					editor.customConfig.onchange = (html) => {	// 创建监听，实时传入 
						this.m.content = html	
					}
					editor.create();		// 创建编辑器 
					editor.txt.html(this.m.content);	// 为编辑器赋值 
				});
			},
			// 创建一个空的model 
			create_m: function() {
				return {
					id: 0,
					title: '江雪',
					content: '<p>千山鸟飞绝，万径人踪灭。</p><p>孤舟蓑笠翁，独钓寒江雪。</p>',
					is_public: 1
				}
			}
		},
		created() {
			
		}
	}
</script>
