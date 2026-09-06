<template>
	<div>
		<a-modal v-model:visible="pageData.visible" width="60%" :title="pageData.title" :destroyOnClose="true"
			@ok="clickModalOk" @cancel="handleModalCancel">
			<a-form ref="dataAddUpdateFormRef" name="dataAddUpdateForm" :model="dataAddUpdateForm.data"
				:labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
				<a-row :gutter="24">
					<a-col span="12">
						<a-form-item name="dictType" label="字典类型" :rules="[{ required: true, message: '字典类型不能为空！' }]">
							<a-input v-model:value="dataAddUpdateForm.data.dictType" :disabled="true" :maxlength="100"
								placeholder="请填写字典类型"></a-input>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="dictValue" label="字典值" :rules="[{ required: true, message: '字典键值不能为空！' }]">
							<a-input v-model:value="dataAddUpdateForm.data.dictValue" :maxlength="100"
								placeholder="请填写字典键值"></a-input>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="dictLabel" label="字典名称" :rules="[{ required: true, message: '字典标签不能为空！' }]">
							<a-input v-model:value="dataAddUpdateForm.data.dictLabel" :maxlength="100"
								placeholder="请填写字典标签"></a-input>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="dictSort" label="字典排序" :rules="[{ required: true, message: '字典排序不能为空！' }]">
							<a-input v-model:value="dataAddUpdateForm.data.dictSort" :maxlength="4"
								placeholder="请填写字典排序"></a-input>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="status" label="状态" :rules="[{ required: true, message: '状态不能为空！' }]">
							<a-radio-group v-model:value="dataAddUpdateForm.data.status"
								:options="fatherPageData.sysNormalDisableDict" optionType="button" button-style="solid">
							</a-radio-group>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="cssClass" label="样式属性" :rules="[{}]">
							<a-input v-model:value="dataAddUpdateForm.data.cssClass" :maxlength="100"
								placeholder="请填写样式属性"></a-input>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="listClass" label="表格回显样式" :rules="[{}]">
							<a-input v-model:value="dataAddUpdateForm.data.listClass" :maxlength="100"
								placeholder="请填写表格回显样式"></a-input>
						</a-form-item>
					</a-col>
					<!-- <a-col span="12">
            <a-form-item name="isDefault" label="是否默认" :rules="[{}]">
              <a-select v-model:value="dataAddUpdateForm.data.isDefault" :options="fatherPageData.sysYesNoDict" placeholder="请选择是否默认"> </a-select>
            </a-form-item>
          </a-col> -->
					<a-col span="12">
						<a-form-item name="remark" label="备注" :rules="[{}]">
							<a-textarea v-model:value="dataAddUpdateForm.data.remark" :maxlength="500" :rows="3"
								placeholder="请填写备注" />
						</a-form-item>
					</a-col>
				</a-row>
			</a-form>
		</a-modal>
	</div>
</template>
<script setup>
	import {
		reactive,
		ref
	} from 'vue';
	import AutoeeUtil from '@/utils/AutoeeUtil.js';
	import {
		getData,
		addData,
		updateData
	} from '@/api/system/dict/data';
	import {
		message
	} from 'ant-design-vue';

	// 父页面公用数据
	const fatherPageData = defineProps({
		dictTypeDict: Array,
		sysYesNoDict: Array,
		sysNormalDisableDict: Array,
	});

	// 引入父页面方法
	const pageEmit = defineEmits(["refreshFatherDataTableData"]);

	// 当前页面使用的数据
	const pageData = reactive({
		title: '新增页面',
		visible: false,
		operateType: '',
	});

	// 新增修改Form
	const dataAddUpdateFormRef = ref();
	const dataAddUpdateForm = reactive({
		data: {
			dictType: ''
		}
	});
	// 重置Form
	const resetDataAddUpdateForm = () => {
		AutoeeUtil.resetFormFieldsToEmpty(dataAddUpdateForm.data);
	};

	// 打开新增窗口
	const openAddModal = (pDictType) => {
		dataAddUpdateForm.data.dictType = pDictType;
		pageData.operateType = 'add';
		pageData.title = '新增字典数据';
		pageData.visible = true;
	};

	// 打开修改窗口
	const openUpdateModal = (record) => {
		pageData.operateType = 'update';
		pageData.title = '修改';
		getData(record.dictCode).then((response) => {
			dataAddUpdateForm.data = response.data;
			pageData.visible = true;
		});
	};

	// 点击窗口确认
	const clickModalOk = (e) => {
		dataAddUpdateFormRef.value
			.validateFields()
			.then((values) => {
				if (pageData.operateType === 'add') {
					addData(dataAddUpdateForm.data).then((res) => {
						pageData.visible = false;
						resetDataAddUpdateForm();
						// 调用父页面刷新方法
						pageEmit("refreshFatherDataTableData");
						AutoeeUtil.messageSuccess('新增操作成功。');
					});
				} else if (pageData.operateType === 'update') {
					updateData(dataAddUpdateForm.data).then((res) => {
						pageData.visible = false;
						resetDataAddUpdateForm();
						// 调用父页面刷新方法
						pageEmit("refreshFatherDataTableData");
						AutoeeUtil.messageSuccess('修改操作成功。');
					});
				}
			})
			.catch((info) => {
				console.log('Validate Failed:', info);
			});
	};

	// 点击窗口取消
	const handleModalCancel = () => {
		resetDataAddUpdateForm();
	};

	// 对外暴露出去
	defineExpose({
		openAddModal,
		openUpdateModal,
	});
</script>

<style lang="less"></style>
