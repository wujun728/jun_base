<style scoped>
</style>

<template>
	<div class="vue-box">
		<!-- 参数栏 -->
		<div class="c-panel">
			<div class="c-title">用户添加</div>
			<el-form size="mini" v-if="m">
				<el-form-item label="昵称：">
					<el-input v-model="m.username"></el-input>
				</el-form-item>
				<el-form-item label="密码：">
					<el-input v-model="m.password"></el-input>
				</el-form-item>
				<el-form-item label="手机：">
					<el-input v-model="m.phone"></el-input>
				</el-form-item>
				<el-form-item label="角色：">
					<el-select v-model="m.role_id">
						<el-option label="请选择" :value="0" disabled></el-option>
						<el-option label="管理员" :value="1"></el-option>
						<el-option label="公告管理员" :value="2"></el-option>
						<el-option label="普通用户" :value="3"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="性别：">
					<el-radio-group v-model="m.sex" size="mini">
						<el-radio :label="1">男</el-radio>
						<el-radio :label="2">女</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item>
					<span class="c-label">&emsp;</span>
					<el-button type="primary" icon="el-icon-plus" size="mini" @click="ok">确定</el-button>
				</el-form-item>
			</el-form>
		</div>
	</div>
</template>

<script>
	export default {
		props: ['params'],
		data() {
			return {
				m: {	
					username: this.params.username || '',	// 从菜单配置文件里传递过来的参数 
					password: '',
					phone: '',
					role_id: 0,
					sex: 1,
				}
			}
		},
		methods: {
			// ok
			ok: function() {
				this.sa.ajax2('/user/add', function(res) {
					console.log(res);
					this.sa.alert('数据添加, 参数为：' + JSON.stringify(this.m));
				}.bind(this))
			}
		},
		created() {
			
		}
	}
</script>


