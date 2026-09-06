<template>
	<div>
		<a-form ref="queryFormRef" name="queryJobForm" :model="queryJobForm.data" :labelCol="{ span: 8 }"
			:wrapperCol="{ span: 14 }">
			<a-row :gutter="24">
				<a-col span="8">
					<a-form-item name="jobName" label="任务名称">
						<a-input v-model:value="queryJobForm.data.jobName" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="jobGroup" label="任务组名">
						<a-select v-model:value="queryJobForm.data.jobGroup" :options="pageData.jobGroupDict"
							allowClear> </a-select>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="status" label="状态">
						<a-select v-model:value="queryJobForm.data.status" :options="pageData.sysJobStatusDict"
							allowClear> </a-select>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="24" class="autoee-button-row">
				<a-col :span="12" style="text-align: left">
					<a-button type="primary" @click="openJobAddModal()" v-hasPermi="['monitor:job:add']">
						<AutoeeIcon icon="plus-outlined" />新增
					</a-button>
					<a-button type="primary" danger @click="clickDeleteJob()"
						:disabled="jobTableData.selectedRowKeys.length <= 0" v-hasPermi="['monitor:job:remove']">
						<AutoeeIcon icon="delete-outlined" />删除
					</a-button>
					<a-button type="primary" @click="clickExport()" v-hasPermi="['monitor:job:export']">
						<AutoeeIcon icon="export-outlined" />导出
					</a-button>
				</a-col>
				<a-col :span="12" style="text-align: right">
					<a-button type="primary" @click="jobTablePage.reload()">
						<AutoeeIcon icon="SearchOutlined" />查询
					</a-button>
					<a-button @click="resetJobQueryForm()">
						<AutoeeIcon icon="redo-outlined" />重置
					</a-button>
				</a-col>
			</a-row>
		</a-form>

		<a-table rowKey="jobId" :columns="jobTableData.columns" :data-source="jobTableData.dataSource"
			:loading="jobTablePage.loading.value" :pagination="jobTablePagination" @change="jobTableHandChangePage"
			:row-selection="{ selectedRowKeys: jobTableData.selectedRowKeys, onChange: onJobTableSelectChange }"
			bordered size="small" tableLayout="fixed">
			<template #bodyCell="{ index, column, record }">
				<template v-if="column.key === 'pageIndex'">
					{{ index + 1 }}
				</template>
				<template v-else-if="column.key === 'jobGroup'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.jobGroup, pageData.jobGroupDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'misfirePolicy'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.misfirePolicy, pageData.misfirePolicyDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'concurrent'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.concurrent, pageData.sysYesNoDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'status'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.status, pageData.sysJobStatusDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'jobTableOperateCol'">
					<span>
						<a @click="openJobDetailModal(record)"> 查看 </a>
						<a-divider type="vertical" v-hasPermi="['monitor:job:edit']" />
						<a @click="openJobUpdateModal(record)" v-hasPermi="['monitor:job:edit']"> 修改 </a>
					</span>
				</template>
			</template>
		</a-table>

		<JobAddUpdateModal ref="jobAddUpdateModalRef" :jobGroupDict="pageData.jobGroupDict"
			:misfirePolicyDict="pageData.misfirePolicyDict" :sysYesNoDict="pageData.sysYesNoDict"
			:sysJobStatusDict="pageData.sysJobStatusDict" />
		<JobDetailModal ref="jobDetailModalRef" :jobGroupDict="pageData.jobGroupDict"
			:misfirePolicyDict="pageData.misfirePolicyDict" :sysYesNoDict="pageData.sysYesNoDict"
			:sysJobStatusDict="pageData.sysJobStatusDict" />
	</div>
</template>

<script setup name="Job">
	import {
		listJob,
		getJob,
		delJob,
		addJob,
		updateJob
	} from '@/api/monitor/job';
	import JobAddUpdateModal from './addUpdateModal.vue';
	import JobDetailModal from './detailModal.vue';
	import {
		computed,
		reactive,
		ref
	} from 'vue';
	import {
		AutoeeIcon
	} from '@/utils/AutoeeIcon.js';
	import AutoeeUtil from '@/utils/AutoeeUtil.js';
	import {
		message
	} from 'ant-design-vue';
	import moment from 'moment';

	// 当前页面使用的数据
	const pageData = reactive({
		jobGroupDict: [],
		misfirePolicyDict: [],
		sysYesNoDict: [],
		sysJobStatusDict: [],
	});

	// 自定义转码字典
	pageData.misfirePolicyDict = [{
			value: '1',
			label: '立即执行'
		},
		{
			value: '2',
			label: '执行一次'
		},
		{
			value: '3',
			label: '放弃执行'
		},
	];
	// 自定义转码字典
	pageData.sysYesNoDict = [{
			value: '0',
			label: '允许'
		},
		{
			value: '1',
			label: '禁止'
		},
	];
	AutoeeUtil.getDictsByType('sys_job_group').then((res) => {
		pageData.jobGroupDict = res;
	});
	AutoeeUtil.getDictsByType('sys_job_status').then((res) => {
		pageData.sysJobStatusDict = res;
	});

	// 查询Form
	const queryFormRef = ref();
	const queryJobForm = reactive({
		data: {
			params: {}
		}
	});

	// 重置查询Form
	const resetJobQueryForm = () => {
		AutoeeUtil.resetFormFieldsToEmpty(queryJobForm.data);
		jobTablePage.reload();
	};

	// 列表数据
	const jobTableData = reactive({
		// 列表数据
		dataSource: [],
		// 列表记录数
		total: 0,
		selectedRowKeys: [],
		// 列表列定义
		columns: [
			// {
			//   title: '序号',
			//   dataIndex: 'pageIndex',
			//   key: 'pageIndex',
			//   width: 50,
			//   align: 'center',
			// },
			{
				title: '任务ID',
				key: 'jobId',
				dataIndex: 'jobId',
			},
			{
				title: '任务名称',
				key: 'jobName',
				dataIndex: 'jobName',
			},
			{
				title: '任务组名',
				key: 'jobGroup',
				dataIndex: 'jobGroup',
			},
			{
				title: '调用目标字符串',
				key: 'invokeTarget',
				dataIndex: 'invokeTarget',
			},
			{
				title: 'cron执行表达式',
				key: 'cronExpression',
				dataIndex: 'cronExpression',
			},
			{
				title: '计划执行错误策略',
				key: 'misfirePolicy',
				dataIndex: 'misfirePolicy',
			},
			{
				title: '是否并发执行',
				key: 'concurrent',
				dataIndex: 'concurrent',
			},
			{
				title: '状态',
				key: 'status',
				dataIndex: 'status',
			},
			{
				title: '操作',
				key: 'jobTableOperateCol',
				width: 100,
			},
		],
	});

	// 查询用户列表
	const queryJobList = () => {
		return listJob(queryJobForm.data);
	};

	// 用户列表翻页数据：使用vue-request中分页方法，返回参数包含{ run, reload, loading, current, pageSize }
	const jobTablePage = AutoeeUtil.usePagination(queryJobList, jobTableData);

	// 用户列表翻页工具条：必须通过计算函数每次重新生成
	const jobTablePagination = computed(() => AutoeeUtil.getTablePagination(jobTablePage));

	// 手动翻页方法
	const jobTableHandChangePage = (page, filters, sorter) => {
		AutoeeUtil.tableHandChangePage(jobTablePage, page, filters, sorter);
	};

	// 列表选中方法
	const onJobTableSelectChange = (selectedRowKeys, selectedRows) => {
		jobTableData.selectedRowKeys = selectedRowKeys;
		// console.log('selectedRows[0].dictType=' + JSON.stringify(selectedRows[0].dictType));
	};
	
	// 打开新增窗口
	const jobAddUpdateModalRef = ref();
	const openJobAddModal = () => {
		jobAddUpdateModalRef.value.openAddModal(jobTablePage);
	};
	
	// 打开修改窗口
	const openJobUpdateModal = (record) => {
		jobAddUpdateModalRef.value.openUpdateModal(record, jobTablePage);
	};
	
	// 打开详细窗口
	const jobDetailModalRef = ref();
	const openJobDetailModal = (record) => {
		jobDetailModalRef.value.openModal(record);
	};
	
	// 点击删除
	const clickDeleteJob = () => {
		AutoeeUtil.confirmDeleteSelectedData(() => {
			delJob(jobTableData.selectedRowKeys).then((res) => {
				AutoeeUtil.messageSuccess('删除操作成功。');
				jobTablePage.reload();
			});
		});
	};
	
	// 点击导出
	const clickExport = () => {
		AutoeeUtil.download('monitor/job/export', queryJobForm.data, "定时任务_" + moment().format('YYYY-MM-DD_HH-mm-ss') +
			".xlsx");
	};
</script>
<style lang="less"></style>
