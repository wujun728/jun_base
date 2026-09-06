<template>
	<div>
		<a-modal v-model:visible="pageDataObj.visible" width="60%" :title="pageDataObj.title">
			<template #footer>
				<a-button @click="pageDataObj.visible = false">关闭</a-button>
			</template>
			<a-form class="autoee-detail-page" ref="detailFormRef" name="detailFormObj" :model="detailFormObj.data"
				:labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
				<a-row :gutter="24">
					<a-col span="12">
						<a-form-item name="userName" label="用户账号">
							<span>{{ detailFormObj.data.userName }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="ipaddr" label="登录IP地址">
							<span>{{ detailFormObj.data.ipaddr }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="loginLocation" label="登录地点">
							<span>{{ detailFormObj.data.loginLocation }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="browser" label="浏览器类型">
							<span>{{ detailFormObj.data.browser }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="os" label="操作系统">
							<span>{{ detailFormObj.data.os }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="status" label="登录状态">
							<span>{{ AutoeeUtil.getDictLabelByKey(detailFormObj.data.status, fatherPageData.sysCommonStatusDict) }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="msg" label="提示消息">
							<span>{{ detailFormObj.data.msg }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="loginTime" label="访问时间">
							<span>{{ detailFormObj.data.loginTime }}</span>
						</a-form-item>
					</a-col>
				</a-row>
			</a-form>
		</a-modal>
	</div>
</template>

<script setup>
	// import { getLogininfor } from "@/api/monitor/logininfor";
	import {
		reactive,
		ref
	} from 'vue';
	import AutoeeUtil from '@/utils/AutoeeUtil.js';

	// 父页面公用数据
	const fatherPageData = defineProps({
		sysCommonStatusDict: Array,
	});

	// 当前页面使用的数据
	const pageDataObj = reactive({
		title: '详细页面',
		visible: false,
		operateType: '',
	});

	//详细Form
	const detailFormRef = ref();
	const detailFormObj = reactive({
		data: {}
	});
	// 打开详细窗口
	const openModal = (record) => {
		detailFormObj.data = record;
		pageDataObj.visible = true;
		// getLogininfor(record.infoId).then((response) => {
		//   detailFormObj.data = response.data;
		//   pageDataObj.visible = true;
		// });
	};

	// 对外暴露出去
	defineExpose({
		openModal,
	});
</script>

<style lang="less"></style>
