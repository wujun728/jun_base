<template>
	<div>
		<a-form ref="queryFormRef" name="queryOperlogForm" :model="queryOperlogForm.data" :labelCol="{ span: 8 }"
			:wrapperCol="{ span: 14 }">
			<a-row :gutter="24">
				<a-col span="8">
					<a-form-item name="title" label="系统模块">
						<a-input v-model:value="queryOperlogForm.data.title" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="businessType" label="操作类型">
						<a-select v-model:value="queryOperlogForm.data.businessType" :options="pageData.sysOperTypeDict"
							allowClear> </a-select>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="operName" label="操作人员">
						<a-input v-model:value="queryOperlogForm.data.operName" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="status" label="操作状态">
						<a-select v-model:value="queryOperlogForm.data.status" :options="pageData.sysCommonStatusDict"
							allowClear> </a-select>
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item name="operTime" label="操作时间起期">
						<a-date-picker v-model:value="queryOperlogForm.data.params.beginTime" format="YYYY-MM-DD"
							valueFormat="YYYY-MM-DD" />
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item name="operTime" label="操作时间止期">
						<a-date-picker v-model:value="queryOperlogForm.data.params.endTime" format="YYYY-MM-DD"
							valueFormat="YYYY-MM-DD" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="24" class="autoee-button-row">
				<a-col :span="12" style="text-align: left">
					<!-- <a-button type="primary" @click="openOperlogAddModal()" v-hasPermi="['monitor:operlog:add']"><AutoeeIcon icon="plus-outlined" />新增</a-button> -->
					<a-button type="primary" danger @click="clickDeleteOperlog()"
						:disabled="operlogTableObj.selectedRowKeys.length <= 0" v-hasPermi="['monitor:operlog:remove']">
						<AutoeeIcon icon="delete-outlined" />删除
					</a-button>
					<a-button type="primary" danger @click="clickCleanOperlog()"
						v-hasPermi="['monitor:operlog:remove']">
						<AutoeeIcon icon="delete-outlined" />清空
					</a-button>
					<a-button type="primary" @click="clickExport()" v-hasPermi="['monitor:operlog:export']">
						<AutoeeIcon icon="export-outlined" />导出
					</a-button>
				</a-col>
				<a-col :span="12" style="text-align: right">
					<a-button type="primary" @click="queryOperlogList()">
						<AutoeeIcon icon="SearchOutlined" />查询
					</a-button>
					<a-button @click="resetOperlogQueryForm()">
						<AutoeeIcon icon="redo-outlined" />重置
					</a-button>
				</a-col>
			</a-row>
		</a-form>

		<a-table rowKey="operId" :columns="operlogTableObj.columns" :data-source="operlogTableObj.dataSource"
			:loading="operlogTableObj.loading" :pagination="operlogTablePagination" @change="operlogTableHandChangePage"
			:row-selection="{ selectedRowKeys: operlogTableObj.selectedRowKeys, onChange: onOperlogTableSelectChange }"
			bordered size="small" tableLayout="fixed">
			<template #bodyCell="{ index, column, record }">
				<template v-if="column.key === 'pageIndex'">
					{{ index + 1 }}
				</template>
				<template v-else-if="column.key === 'businessType'">
					<span> {{ AutoeeUtil.getDictLabelByKey(record.businessType, pageData.sysOperTypeDict) }} </span>
				</template>
				<template v-else-if="column.key === 'status'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.status, pageData.sysCommonStatusDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'operlogTableOperateCol'">
					<span>
						<a @click="openOperlogDetailModal(record)"> 查看 </a>
						<!-- <a-divider type="vertical" v-hasPermi="['monitor:operlog:edit']" />
            <a @click="openOperlogUpdateModal(record)" v-hasPermi="['monitor:operlog:edit']"> 修改 </a> -->
					</span>
				</template>
			</template>
		</a-table>

		<!-- <OperlogAddUpdateModal ref="operlogAddUpdateModalRef" :sysOperTypeDict="pageData.sysOperTypeDict" :sysOperTypeDict="pageData.sysOperTypeDict" :sysCommonStatusDict="pageData.sysCommonStatusDict" /> -->
		<OperlogDetailModal ref="operlogDetailModalRef" :sysOperTypeDict="pageData.sysOperTypeDict"
			:sysCommonStatusDict="pageData.sysCommonStatusDict" />
	</div>
</template>

<script setup name="Operlog">
	import {
		list,
		delOperlog,
		cleanOperlog,
		exportOperlog
	} from '@/api/monitor/operlog';
	// import OperlogAddUpdateModal from './addUpdateModal.vue';
	import OperlogDetailModal from './detailModal.vue';
	import {
		computed,
		reactive,
		ref
	} from 'vue';
	import {
		AutoeeIcon
	} from '@/utils/AutoeeIcon.js';
	import AutoeeUtil from '@/utils/AutoeeUtil.js';
	import moment from 'moment';

	// 当前页面使用的数据
	const pageData = reactive({
		sysOperTypeDict: [],
		sysCommonStatusDict: [],
	});

	AutoeeUtil.getDictsByType('sys_oper_type').then((res) => {
		pageData.sysOperTypeDict = res;
	});
	AutoeeUtil.getDictsByType('sys_common_status').then((res) => {
		pageData.sysCommonStatusDict = res;
	});


	// 查询Form
	const queryFormRef = ref();
	const queryOperlogForm = reactive({
		data: {
			pageNum: 1,
			pageSize: 10,
			params: {}
		}
	});

	// 重置查询Form
	const resetOperlogQueryForm = () => {
		queryOperlogForm.data = {
			pageNum: 1,
			pageSize: 10,
			params: {},
		}
		queryOperlogList()
	};
	// 列表数据
	const operlogTableObj = reactive({
		// 列表数据集
		dataSource: [],
		// 列表总记录数
		total: 0,
		// 列表加载是否加载中
		loading: false,
		// 列表选中行数组
		selectedRowKeys: [],
		// 列表列定义
		columns: [{
				title: '序号',
				dataIndex: 'pageIndex',
				key: 'pageIndex',
				width: 50,
				align: 'center',
			},
			{
				title: '系统模块',
				key: 'title',
				dataIndex: 'title',
			},
			{
				title: '操作类型',
				key: 'businessType',
				dataIndex: 'businessType',
			},
			// {
			//   title: '操作类别',
			//   key: 'operatorType',
			//   dataIndex: 'operatorType',
			// },
			{
				title: '方法名称',
				key: 'method',
				dataIndex: 'method',
			},
			{
				title: '请求方式',
				key: 'requestMethod',
				dataIndex: 'requestMethod',
			},
			{
				title: '操作人员',
				key: 'operName',
				dataIndex: 'operName',
			},
			{
				title: '请求URL',
				key: 'operUrl',
				dataIndex: 'operUrl',
			},
			{
				title: '操作地点',
				key: 'operLocation',
				dataIndex: 'operLocation',
			},
			{
				title: '操作状态',
				key: 'status',
				dataIndex: 'status',
			},
			{
				title: '操作时间',
				key: 'operTime',
				dataIndex: 'operTime',
			},
			{
				title: '操作',
				key: 'operlogTableOperateCol',
				width: 100,
			},
		],
	});


	// 查询列表数据
	const queryOperlogList = () => {
		// 调用后端列表查询方法，通过返回结果设置operlogTableObj.total，operlogTableObj.dataSource，operlogTableObj.loading
		AutoeeUtil.getTableDataByQueryFunc(list(queryOperlogForm.data), operlogTableObj);
	}

	// 默认查询列表数据
	queryOperlogList();

	// 用户列表翻页工具条：必须通过计算函数每次重新生成
	const operlogTablePagination = computed(() => AutoeeUtil.createTablePagination(operlogTableObj, queryOperlogForm));

	// 手动翻页方法
	const operlogTableHandChangePage = (page, filters, sorter) => {
		queryOperlogForm.data.pageSize = page.pageSize;
		queryOperlogForm.data.pageNum = page.current;
		queryOperlogList();
	};

	// 列表选中方法
	const onOperlogTableSelectChange = (selectedRowKeys, selectedRows) => {
		operlogTableObj.selectedRowKeys = selectedRowKeys;
		// console.log('selectedRows[0].dictType=' + JSON.stringify(selectedRows[0].dictType));
	};


	// // 打开新增窗口
	// const operlogAddUpdateModalRef = ref();
	// const openOperlogAddModal = () => {
	//   operlogAddUpdateModalRef.value.openAddModal(operlogTablePage);
	// };

	// // 打开修改窗口
	// const openOperlogUpdateModal = (record) => {
	//   operlogAddUpdateModalRef.value.openUpdateModal(record, operlogTablePage);
	// };

	// 打开详细窗口
	const operlogDetailModalRef = ref();
	const openOperlogDetailModal = (record) => {
		operlogDetailModalRef.value.openModal(record);
	};

	// 点击删除
	const clickDeleteOperlog = () => {
		AutoeeUtil.confirmDeleteSelectedData(() => {
			delOperlog(operlogTableObj.selectedRowKeys).then((res) => {
				operlogTableObj.selectedRowKeys = [];
				AutoeeUtil.messageSuccess('删除操作成功。');
				operlogTablePage.reload();
			});
		});
	};

	// 点击清空
	const clickCleanOperlog = () => {
		AutoeeUtil.confirmCleanAllData(() => {
			cleanOperlog().then((res) => {
				AutoeeUtil.messageSuccess('清空操作成功。');
				operlogTablePage.reload();
			});
		});
	};

	// 点击导出
	const clickExport = () => {
		let exportUrl = 'monitor/operlog/export';
		let moduleName = "操作日志";
		let exportFileName = moduleName + "_" + moment().format('YYYY-MM-DD_HH-mm-ss') + ".xlsx";
		AutoeeUtil.download(exportUrl, queryOperlogForm.data, exportFileName);
	};
</script>
<style lang="less"></style>
