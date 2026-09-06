<template>
	<div>
		<a-modal v-model:visible="pageDataObj.visible" width="60%" :title="pageDataObj.title">
			<template #footer>
				<a-button @click="pageDataObj.visible = false">关闭</a-button>
			</template>
			<a-form class="autoee-detail-page" ref="detailFormRef" name="detailFormObj" :model="detailFormObj.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
				<a-row :gutter="24">
					<a-col span="12">
						<a-form-item name="configName" label="参数名称">
							<span>{{ detailFormObj.data.configName }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="configKey" label="参数键名">
							<span>{{ detailFormObj.data.configKey }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="configValue" label="参数键值">
							<span>{{ detailFormObj.data.configValue }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="configType" label="系统内置">
							<span>{{ AutoeeUtil.getDictLabelByKey(detailFormObj.data.configType, fatherPageDataObj.sysYesNoDict) }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="remark" label="备注">
							<span>{{ detailFormObj.data.remark }}</span>
						</a-form-item>
					</a-col>
				</a-row>
			</a-form>
		</a-modal>
	</div>
</template>

<script setup>
	import {getConfig} from "@/api/system/config";
	import {reactive, ref} from 'vue';
	import AutoeeUtil from '@/utils/AutoeeUtil.js';

	// 父页面公用数据
	const fatherPageDataObj = defineProps({
    sysYesNoDict: Array,
	});

	// 当前页面使用的数据
	const pageDataObj = reactive({
		title: '详细页面',
		visible: false,
		operateType: '',
	});

	//详细Form
	const detailFormRef = ref();
	const detailFormObj = reactive({data: {}});
	// 打开详细窗口
	const openModal = (record) => {
	    getConfig(record.configId).then((response) => {
		    detailFormObj.data = response.data;
		    pageDataObj.visible = true;
	    });
	};

	// 对外暴露出去
	defineExpose({
		openModal,
	});
</script>

<style lang="less"></style>
