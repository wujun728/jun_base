<template>
	<div>
		<a-modal v-model:visible="pageDataObj.visible" width="60%" :title="pageDataObj.title">
			<template #footer>
				<a-button @click="pageDataObj.visible = false">关闭</a-button>
			</template>
			<a-form class="autoee-detail-page" ref="detailFormRef" name="detailFormObj" :model="detailFormObj.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
				<a-row :gutter="24">
					<a-col span="12">
						<a-form-item name="postCode" label="岗位编码">
							<span>{{ detailFormObj.data.postCode }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="postName" label="岗位名称">
							<span>{{ detailFormObj.data.postName }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="postSort" label="显示顺序">
							<span>{{ detailFormObj.data.postSort }}</span>
						</a-form-item>
					</a-col>
					<a-col span="12">
						<a-form-item name="status" label="岗位状态">
							<span>{{ AutoeeUtil.getDictLabelByKey(detailFormObj.data.status, fatherPageDataObj.sysNormalDisableDict) }}</span>
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
	import {getPost} from "@/api/system/post";
	import {reactive, ref} from 'vue';
	import AutoeeUtil from '@/utils/AutoeeUtil.js';

	// 父页面公用数据
	const fatherPageDataObj = defineProps({
    sysNormalDisableDict: Array,
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
	    getPost(record.postId).then((response) => {
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
