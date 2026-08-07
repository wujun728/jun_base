<style scoped>
</style>

<template>
	<div class="vue-box">
		<div class="c-panel">
			<!-- 参数栏 -->
			<div class="c-title">检索参数</div>
			<el-form :inline="true" size="mini">
				<el-form-item label="文章编号：">
					<el-input v-model="p.id" type="number"></el-input>
				</el-form-item>
				<el-form-item label="标题：">
					<el-input v-model="p.title" placeholder="模糊查询"></el-input>
				</el-form-item>
				<el-form-item style="min-width: 10px;">
					<el-button type="primary" icon="el-icon-search" @click="f5">查询</el-button>
					<el-button type="primary" icon="el-icon-plus" @click="add">增加</el-button>
				</el-form-item>
				<br>
				<el-form-item label="综合排序：">
					<el-radio-group v-model="p.sort_type">
						<el-radio :label="0">发表时间</el-radio>
						<el-radio :label="1">喜欢数</el-radio>
						<el-radio :label="2">点击数</el-radio>
						<el-radio :label="3">分享数</el-radio>
					</el-radio-group>
				</el-form-item>
			</el-form>
			<!-- 数据列表 -->
			<!-- <h4 class="c-title">列表</h4> -->
			<el-table class="data-table" :data="dataList" size="mini">
				<el-table-column label="编号" prop="id" width="70px"> </el-table-column>
				<el-table-column label="文章标题" prop="title"></el-table-column>
				<el-table-column label="文章内容" prop="content" width="400px"></el-table-column>
				<el-table-column label="发表人" prop="create_username"></el-table-column>
				<el-table-column label="发表于" prop="create_time"></el-table-column>
				<el-table-column label="点击量" prop="see_count"></el-table-column>
				<el-table-column label="喜欢" prop="like_count"></el-table-column>
				<el-table-column label="分享" prop="share_count"></el-table-column>
				<el-table-column prop="address" label="操作" width="150px">
					<template slot-scope="s">
						<el-button class="c-btn" type="primary" icon="el-icon-edit" @click="update(s.row)">修改</el-button>
						<el-button class="c-btn" type="danger" icon="el-icon-delete" @click="del(s.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<!-- 分页 -->
			<div class="page-box">
				<el-pagination background
					layout="total, prev, pager, next, sizes, jumper" 
					:current-page.sync="p.pageNo" 
					:page-size.sync="p.pageSize" 
					:total="dataCount" 
					:page-sizes="[1, 10, 20, 30, 40, 50, 100]" 
					@current-change="f5(true)" 
					@size-change="f5(true)">
				</el-pagination>
			</div>
		</div>
		
		<!-- 给wangEditor打一波广告 -->
		<div class="c-panel" style="background-color: rgba(0,0,0,0);">
			<li>
				wangEditor：
				<el-link type="primary" href="http://www.wangeditor.com/" target="_blank">
					基于javascript和css开发的 Web富文本编辑器， 轻量、简洁、易用、开源免费
				</el-link>
			</li>
			<li>
				在vue中集成示例：
				<el-link type="primary" href="https://www.kancloud.cn/wangfupeng/wangeditor3/335789" target="_blank">
					https://www.kancloud.cn/wangfupeng/wangeditor3/335789
				</el-link>
			</li>
		</div>
					
		<!-- 增改组件 -->
		<add-or-update ref="add-or-update"></add-or-update>
		
	</div>
</template>

<script>
	import mockData from './data-list.js';
	import addOrUpdate from './art-add.vue';
	export default {
		components: {
			addOrUpdate
		},
		data() {
			return {
				p: { // 查询参数
					id: '',
					title: '',
					sort_type: 0,
					pageNo: 1,
					pageSize: 10,
				},
				dataCount: 0,	// 数据总数 
				dataList: [] // 数据集合
			}
		},
		methods: {
			// 数据刷新
			f5: function() {
				this.sa.ajax2('/VocArticle/getList', this.p, function(res){
					this.dataList = res.data;	// 数据
					this.dataCount = res.dataCount;		// 分页 
				}.bind(this), {res: mockData});
			},
			// 增加
			add: function() {
				this.$refs['add-or-update'].open(0);
			},
			// 修改
			update: function(data) {
				this.$refs['add-or-update'].open(data.id);
			},
			// 删除
			del: function(data) {
				this.sa.confirm('是否删除，此操作不可撤销', function() {
					this.sa.ajax2('/acticle/delete?id=' + data.id, function() {
						this.sa.arrayDelete(this.dataList, data);
						this.sa.ok('删除成功');
					}.bind(this))
				}.bind(this));
			}
		},
		created: function() {
			this.f5();
		}
	}
</script>
