<style scoped>
</style>

<template>
	<div class="vue-box">
		<!-- 参数栏 -->
		<div class="c-panel">
			<div class="c-title">服务器所有设置</div>
			<el-form size="mini" v-if="m" label-width="120px">
				<el-form-item label="系统名称：">
					<el-input v-model="m.app_name"></el-input>
				</el-form-item>
				<el-form-item label="当前版本：">
					<el-input v-model="m.app_version_no"></el-input>
				</el-form-item>
				<el-form-item label="联系Q群：">
					<el-input v-model="m.qq_group"></el-input>
				</el-form-item>
				<el-form-item label="开放注册：">
					<el-switch v-model="m.xcx_share_btn" :active-value="1" :inactive-value="2"></el-switch>
					<span class="c-remark" v-if="m.open_reg==1">开启</span>
					<span class="c-remark" v-else>关闭</span>
				</el-form-item>
				<el-form-item label="显示分享按钮：">
					<el-switch v-model="m.gzhxc" :active-value="1" :inactive-value="2"></el-switch>
					<span class="ps" v-if="m.show_share==1">开启</span>
					<span class="ps" v-else>关闭</span>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" size="mini" icon="el-icon-check" @click="ok">保存修改</el-button>
					<el-button type="primary" size="mini" icon="el-icon-close" @click="f5">重置</el-button>
				</el-form-item>
			</el-form>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				m: null
			}
		},
		methods: {
			// 创建一个初始化的 model
			create_m: function() {
				return {	
					app_name: 'sa-admin模板',
					app_version_no: 'v1.0.0',
					qq_group: '782974737',
					open_reg: 1,
					show_share: 1,
				}
			},
			// f5
			f5: function() {
				var value = localStorage.getItem('system-cfg');
				if(value != null) {
					value = this.sa.JSONParse(value, this.create_m());
				} else {
					value = this.create_m();
				}
				this.m = value;
			},
			// ok
			ok: function() {
				var value = JSON.stringify(this.m);
				localStorage.setItem('system-cfg', value);
				this.sa.ok('保存成功');
			},
		},
		created() {
			this.f5();
		}
	}
</script>
