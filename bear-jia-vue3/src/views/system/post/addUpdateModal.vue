<template>
	<div>
		<a-modal v-model:open="pageDataObj.visible" width="60%" :title="pageDataObj.title" :destroyOnClose="true" @ok="clickModalOk" @cancel="handleModalCancel">
			<a-form ref="addUpdateFormRef" name="addUpdateFormObj" :model="addUpdateFormObj.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
				<a-row :gutter="24">
					<a-col span="12">
                        <a-form-item name="postCode" label="岗位编码" :rules="[{ required: true, message: '岗位编码不能为空！' }]">
						    <a-input v-model:value="addUpdateFormObj.data.postCode" :maxlength="64" placeholder="请填写岗位编码"></a-input>
					    </a-form-item>
					</a-col>
					<a-col span="12">
                        <a-form-item name="postName" label="岗位名称" :rules="[{ required: true, message: '岗位名称不能为空！' }]">
						    <a-input v-model:value="addUpdateFormObj.data.postName" :maxlength="50" placeholder="请填写岗位名称"></a-input>
					    </a-form-item>
					</a-col>
					<a-col span="12">
                        <a-form-item name="postSort" label="显示顺序" :rules="[{ required: true, message: '显示顺序不能为空！' }]">
						    <a-input v-model:value="addUpdateFormObj.data.postSort" :maxlength="4" placeholder="请填写显示顺序"></a-input>
					    </a-form-item>
					</a-col>
					<a-col span="12">
                        <a-form-item name="status" label="岗位状态" :rules="[{ required: true, message: '岗位状态不能为空！' }]">
						    <a-radio-group v-model:value="addUpdateFormObj.data.status" :options="fatherPageDataObj.sysNormalDisableDict" optionType="button" button-style="solid"></a-radio-group>
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
	import {getPost, addPost, updatePost} from "@/api/system/post";
	import {message} from 'ant-design-vue';

	// 父页面公用数据
	const fatherPageDataObj = defineProps({
        sysNormalDisableDict: Array,
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
	    getPost(record.postId).then((response) => {
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
					addPost(addUpdateFormObj.data).then((res) => {
						pageDataObj.visible = false;
						resetAddUpdateForm();
						// 调用父页面刷新方法
						pageEmit("refreshFatherPageTable");
						BearJiaUtil.messageSuccess('新增操作成功。');
					});
				} else if (pageDataObj.operateType === 'update') {
					updatePost(addUpdateFormObj.data).then((res) => {
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
