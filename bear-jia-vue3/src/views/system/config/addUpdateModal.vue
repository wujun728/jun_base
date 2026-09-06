<template>
	<div>
		<a-modal v-model:open="pageDataObj.visible" width="60%" :title="pageDataObj.title" :destroyOnClose="true" @ok="clickModalOk" @cancel="handleModalCancel">
			<a-form ref="addUpdateFormRef" name="addUpdateFormObj" :model="addUpdateFormObj.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
				<a-row :gutter="24">
					<a-col span="12">
                        <a-form-item name="configName" label="参数名称" :rules="[{required: false}]">
						    <a-input v-model:value="addUpdateFormObj.data.configName" :maxlength="100" placeholder="请填写参数名称"></a-input>
					    </a-form-item>
					</a-col>
					<a-col span="12">
                        <a-form-item name="configKey" label="参数键名" :rules="[{required: false}]">
						    <a-input v-model:value="addUpdateFormObj.data.configKey" :maxlength="100" placeholder="请填写参数键名"></a-input>
					    </a-form-item>
					</a-col>
					<a-col span="12">
                        <a-form-item name="configValue" label="参数键值" :rules="[{required: false}]">
						    <a-textarea v-model:value="addUpdateFormObj.data.configValue" :maxlength="500" :rows="3" placeholder="请填写参数键值"/>
					    </a-form-item>
					</a-col>
					<a-col span="12">
                        <a-form-item name="configType" label="系统内置" :rules="[{required: false}]">
						    <a-select v-model:value="addUpdateFormObj.data.configType" :options="fatherPageDataObj.sysYesNoDict" placeholder="请选择系统内置"></a-select>
					    </a-form-item>
					</a-col>
					<a-col span="12">
                        <a-form-item name="remark" label="备注" :rules="[{required: false}]">
						    <a-textarea v-model:value="addUpdateFormObj.data.remark" :maxlength="500" :rows="3" placeholder="请填写备注"/>
					    </a-form-item>
					</a-col>
				</a-row>
			</a-form>
		</a-modal>
	</div>
</template>
<script setup>
	import {reactive, ref} from 'vue';
	import BearJiaUtil from '@/utils/BearJiaUtil.js';
	import {getConfig, addConfig, updateConfig} from "@/api/system/config";
	import {message} from 'ant-design-vue';

	// 父页面公用数据
	const fatherPageDataObj = defineProps({
        sysYesNoDict: Array,
	});

	// 引入父页面方法
	const pageEmit = defineEmits(["refreshFatherPageTable"]);

	// 当前页面使用的数据
	const pageDataObj = reactive({
		title: '新增页面',
		visible: false,
		operateType: '',
	});

	// 新增修改Form
	const addUpdateFormRef = ref();
	const addUpdateFormObj = reactive({data: {}});
	// 重置Form
	const resetAddUpdateForm = () => {
		BearJiaUtil.resetFormFieldsToEmpty(addUpdateFormObj.data);
	};

	// 打开新增窗口
	const openAddModal = () => {
		pageDataObj.operateType = 'add';
		pageDataObj.title = '新增';
		pageDataObj.visible = true;
	};

	// 打开修改窗口
	const openUpdateModal = (record, tablePage) => {
		pageDataObj.operateType = 'update';
		pageDataObj.title = '修改';
	    getConfig(record.configId).then((response) => {
             addUpdateFormObj.data = response.data;
             pageDataObj.visible = true;
        });
	};

	// 点击窗口确认
	const clickModalOk = (e) => {
		addUpdateFormRef.value
			.validateFields()
			.then((values) => {
				if (pageDataObj.operateType === 'add') {
					addConfig(addUpdateFormObj.data).then((res) => {
						pageDataObj.visible = false;
						resetAddUpdateForm();
						// 调用父页面刷新方法
						pageEmit("refreshFatherPageTable");
						BearJiaUtil.messageSuccess('新增操作成功。');
					});
				} else if (pageDataObj.operateType === 'update') {
					updateConfig(addUpdateFormObj.data).then((res) => {
						pageDataObj.visible = false;
						resetAddUpdateForm();
						// 调用父页面刷新方法
						pageEmit("refreshFatherPageTable");
						BearJiaUtil.messageSuccess('修改操作成功。');
					});
				}
			}).catch((info) => {
			// console.log('Validate Failed:', info);
		});
	};

	// 点击窗口取消
	const handleModalCancel = () => {
		resetAddUpdateForm();
	};

	// 对外暴露出去
	defineExpose({
		openAddModal,
		openUpdateModal,
	});
</script>

<style lang="less"></style>
