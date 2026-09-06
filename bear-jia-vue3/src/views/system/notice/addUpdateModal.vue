<template>
	<div>
		<a-modal v-model:open="pageDataObj.visible" width="80%" :title="pageDataObj.title" :destroyOnClose="true" @ok="clickModalOk" @cancel="handleModalCancel">
			<a-form ref="addUpdateFormRef" name="addUpdateFormObj" :model="addUpdateFormObj.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
				<a-row :gutter="24">
					<a-col span="12">
                        <a-form-item name="noticeTitle" label="公告标题" :rules="[{ required: true, message: '公告标题不能为空！' }]">
						    <a-input v-model:value="addUpdateFormObj.data.noticeTitle" :maxlength="50" placeholder="请填写公告标题"></a-input>
					    </a-form-item>
					</a-col>
					<a-col span="12">
                        <a-form-item name="noticeType" label="公告类型" :rules="[{ required: true, message: '公告类型不能为空！' }]">
						    <a-select v-model:value="addUpdateFormObj.data.noticeType" :options="fatherPageDataObj.sysNoticeTypeDict" placeholder="请选择公告类型"></a-select>
					    </a-form-item>
					</a-col>
					<a-col span="24">
                        <a-form-item name="noticeContent" label="公告内容" :labelCol="{ span: 4 }" :wrapperCol="{ span: 20 }" :rules="[{required: true, message: '公告内容不能为空！'}]">
						    <WangEditor
							    v-model:value="addUpdateFormObj.data.noticeContent"
							    :height="300"
							    :imageSize="5"
							    :videoSize="50"
							    placeholder="请输入公告内容"
						    />
					    </a-form-item>
					</a-col>
					<a-col span="12">
                        <a-form-item name="status" label="公告状态" :rules="[{required: false}]">
						    <a-radio-group v-model:value="addUpdateFormObj.data.status" :options="fatherPageDataObj.sysNoticeStatusDict" optionType="button" button-style="solid"></a-radio-group>
					    </a-form-item>
					</a-col>
					<a-col span="12">
                        <a-form-item name="remark" label="备注" :rules="[{required: false}]">
						    <a-input v-model:value="addUpdateFormObj.data.remark" :maxlength="255" placeholder="请填写备注"></a-input>
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
	import {getNotice, addNotice, updateNotice} from "@/api/system/notice";
		import WangEditor from '@/components/editor/WangEditor.vue';

	// 父页面公用数据
	const fatherPageDataObj = defineProps({
        sysNoticeTypeDict: Array,
        sysNoticeStatusDict: Array,
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
	    getNotice(record.noticeId).then((response) => {
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
					addNotice(addUpdateFormObj.data).then((res) => {
						pageDataObj.visible = false;
						resetAddUpdateForm();
						// 调用父页面刷新方法
						pageEmit("refreshFatherPageTable");
            BearJiaUtil.messageSuccess('新增操作成功。');
					});
				} else if (pageDataObj.operateType === 'update') {
					updateNotice(addUpdateFormObj.data).then((res) => {
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
