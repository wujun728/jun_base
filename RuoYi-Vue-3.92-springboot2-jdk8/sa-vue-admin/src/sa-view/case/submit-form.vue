<style>
	.view-9812321 .c-panel .el-form .el-input{width: 250px;}
</style>

<template>
	<div class="vue-box view-9812321">
		<div class="c-panel">
			<!-- 参数栏 -->
			<div class="c-title">表单提交</div>
			<el-form size="mini" v-if="m">
				<el-form-item label="普通参数：">
					<el-input v-model="m.name"></el-input>
				</el-form-item>
				<el-form-item label="数值输入：">
					<el-input v-model="m.count" type="number"></el-input>
				</el-form-item>
				<el-form-item label="下拉输入：">
					<el-select size="mini" v-model="m.type_id">
						<el-option label="请选择" :value="0" disabled></el-option>
						<el-option v-for="type in typeList" :label="type.name" :value="type.id" :key="type.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="级联选择：">
					<el-cascader
						v-model="m.jvalue"
						:options="joptions"
						:props="{ expandTrigger: 'hover' }" >
					</el-cascader>
				</el-form-item>
				<el-form-item label="选择时间：">
					<el-date-picker v-model="m.create_time" type="date" value-format="yyyy-MM-dd"></el-date-picker>
				</el-form-item>
				<el-form-item label="滑块参数：">
					<div style="display: inline-block; height: 0px; vertical-align: top; width: 250px;">
						<el-slider v-model="m.hValue" style="position: relative; top: -5px;"></el-slider>
					</div>
				</el-form-item>
				<el-form-item label="单选参数：">
					<el-radio-group v-model="m.single">
						<el-radio :label="1">参数1</el-radio>
						<el-radio :label="2">参数2</el-radio>
						<el-radio :label="3">参数3</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="多选参数：">
					<el-checkbox v-model="m.checkbox_1">苹果</el-checkbox>
					<el-checkbox v-model="m.checkbox_2">鸭梨</el-checkbox>
					<el-checkbox v-model="m.checkbox_3">香蕉</el-checkbox>
					<el-checkbox v-model="m.checkbox_4">葡萄</el-checkbox>
				</el-form-item>
				<el-form-item label="开关参数：">
					<el-switch v-model="m.status" :active-value="1" :inactive-value="2" inactive-color="#ff4949"></el-switch>
					<span style="color: #999;" v-if="m.status==1">正常</span>
					<span style="color: #999;" v-else>禁用</span>
				</el-form-item>
				<el-form-item label="颜色选择：">
					<el-color-picker v-model="m.color" size="mini"></el-color-picker>
					<span class="c-remark" style="vertical-align: top;">{{m.color}}</span>
				</el-form-item>
				<el-form-item label="&emsp;">
					<el-button type="primary" icon="el-icon-plus" @click="ok()">确定</el-button>
				</el-form-item>
			</el-form>
		</div>
	</div>
</template>

<script>
	import mockJoptions from './mock-joptions.js';
	export default {
		data() {
			return {
				m: {	// 查询参数 
					name: '',
					count: '',
					create_time: '',
					type_id: 0,
					jvalue: [],
					single: 1,
					checkbox_1: true,
					checkbox_2: true,
					checkbox_3: false,
					checkbox_4: false,
					status: 1,
					hValue: 1,
					color: '#409EFF'
				}, 
				typeList: [		// 类型数组  
					{id: 1, name: '类型1'}, 
					{id: 2, name: '类型2'}, 
					{id: 3, name: '类型3'}, 
					{id: 4, name: '类型4'}, 
				],
				joptions: mockJoptions
			}
		},
		methods: {
			// 表单验证  
			submit_check: function() {
				var m = this.m;
				if(this.sa.isNull(m.name)) {
					return this.sa.error('请输入普通参数');
				}
				if(this.sa.isNull(m.count)) {
					return this.sa.error('请输入数值参数');
				}
				if(m.type_id == 0) {
					return this.sa.error('请选择下拉框');
				}
				return 'ok';
			},
			// 提交 
			ok: function() {
				if(this.submit_check() != 'ok') {
					return;
				}
				this.sa.alert('提交参数为：' + JSON.stringify(this.m));
			},
		},
		created: function(){
			this.$notify.info({
				title: '消息',
				message: '本页面展示提交表单时各种常见的各种姿势'
			});
		}
	}
</script>
