<template>
	<div>
		<a-modal v-model:open="pageData.visible" width="60%" :title="pageData.title" :destroyOnClose="true"
			@ok="clickModalOk" @cancel="handleModalCancel">
			<a-form ref="typeAddUpdateFormRef" name="typeAddUpdateForm" :model="typeAddUpdateForm.data"
				:labelCol="{ span: 4 }" :wrapperCol="{ span: 18 }">
				<a-form-item name="dictType" label="字典类型" :rules="[{ required: true, message: '字典类型不能为空！' }]">
					<a-input v-model:value="typeAddUpdateForm.data.dictType" :maxlength="100"
						placeholder="请填写字典类型"></a-input>
				</a-form-item>
				<a-form-item name="dictName" label="字典类型名称" :rules="[{ required: true, message: '字典名称不能为空！' }]">
					<a-input v-model:value="typeAddUpdateForm.data.dictName" :maxlength="100"
						placeholder="请填写字典名称"></a-input>
				</a-form-item>
				<a-form-item name="status" label="状态" :rules="[{ required: true, message: '状态不能为空！' }]">
					<a-radio-group v-model:value="typeAddUpdateForm.data.status"
						:options="fatherPageData.sysNormalDisableDict" optionType="button" button-style="solid">
					</a-radio-group>
				</a-form-item>
				<a-form-item name="remark" label="备注" :rules="[{}]">
					<a-textarea v-model:value="typeAddUpdateForm.data.remark" :maxlength="500" :rows="3"
						placeholder="请填写备注" />
				</a-form-item>
			</a-form>
		</a-modal>
	</div>
</template>
<script setup>
	import {
		reactive,
		ref
	} from 'vue';
	import BearJiaUtil from '@/utils/BearJiaUtil.js';
	import {
		getType,
		addType,
		updateType
	} from '@/api/system/dict/type';
	import {
		message
	} from 'ant-design-vue';

	// 父页面公用数据
	const fatherPageData = defineProps({
		dictTypeDict: Array,
		sysNormalDisableDict: Array,
	});

	// 引入父页面方法
	const pageEmit = defineEmits(["refreshFatherTypeTableData"]);

	// 当前页面使用的数据
	const pageData = reactive({
		title: '新增页面',
		visible: false,
		operateType: '',
	});

	// 新增修改Form
	const typeAddUpdateFormRef = ref();
	const typeAddUpdateForm = reactive({
		data: {}
	});
	// 重置Form
	const resetTypeAddUpdateForm = () => {
		BearJiaUtil.resetFormFieldsToEmpty(typeAddUpdateForm.data);
	};

	// 打开新增窗口
	const openAddModal = () => {
		pageData.operateType = 'add';
		pageData.title = '新增';
		pageData.visible = true;
	};

	// 打开修改窗口
	const openUpdateModal = (record) => {
		pageData.operateType = 'update';
		pageData.title = '修改';
		getType(record.dictId).then((response) => {
			typeAddUpdateForm.data = response.data;
			pageData.visible = true;
		});
	};

	// 点击窗口确认
	const clickModalOk = (e) => {
		typeAddUpdateFormRef.value
			.validateFields()
			.then((values) => {
				if (pageData.operateType === 'add') {
					addType(typeAddUpdateForm.data).then((res) => {
						pageData.visible = false;
						resetTypeAddUpdateForm();
						// 调用父页面刷新方法
						pageEmit("refreshFatherTypeTableData");
						BearJiaUtil.messageSuccess('新增操作成功。');
					});
				} else if (pageData.operateType === 'update') {
					updateType(typeAddUpdateForm.data).then((res) => {
						pageData.visible = false;
						resetTypeAddUpdateForm();
						// 调用父页面刷新方法
						pageEmit("refreshFatherTypeTableData");
						BearJiaUtil.messageSuccess('修改操作成功。');
					});
				}
			})
			.catch((info) => {
				console.log('Validate Failed:', info);
			});
	};

	// 点击窗口取消
	const handleModalCancel = () => {
		resetTypeAddUpdateForm();
	};

	// 对外暴露出去
	defineExpose({
		openAddModal,
		openUpdateModal,
	});
</script>

<style lang="less"></style>
