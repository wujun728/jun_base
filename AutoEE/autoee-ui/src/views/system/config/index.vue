<template>
	<div>
		<a-form ref="queryFormObj.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
			<a-row :gutter="24">
				<a-col span="8">
					<a-form-item name="configName" label="参数名称">
						<a-input v-model:value="queryFormObj.data.configName" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="configKey" label="参数键名">
						<a-input v-model:value="queryFormObj.data.configKey" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="configValue" label="参数键值">
						<a-input v-model:value="queryFormObj.data.configValue" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="configType" label="系统内置">
						<a-select v-model:value="queryFormObj.data.configType" :options="pageDataObj.sysYesNoDict" allowClear></a-select>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="24" class="autoee-button-row">
				<a-col :span="12" style="text-align: left">
					<a-button type="primary" @click="openAddModal()" v-hasPermi="['system:config:add']">
						<AutoeeIcon icon="plus-outlined"/>
						新增
					</a-button>
					<a-button type="primary" danger @click="clickDelete()" :disabled="tableObj.selectedRowKeys.length <= 0" v-hasPermi="['system:config:remove']">
						<AutoeeIcon icon="delete-outlined"/>
						删除
					</a-button>
					<a-button type="primary" @click="clickExport()" v-hasPermi="['system:config:export']">
						<AutoeeIcon icon="export-outlined"/>
						导出
					</a-button>
				</a-col>
				<a-col :span="12" style="text-align: right">
					<a-button type="primary" @click="queryTableData()">
						<AutoeeIcon icon="SearchOutlined"/>
						查询
					</a-button>
					<a-button @click="resetQueryForm()">
						<AutoeeIcon icon="redo-outlined"/>
						重置
					</a-button>
				</a-col>
			</a-row>
		</a-form>

		<a-table
			rowKey="configId"
			:columns="tableObj.columns"
			:data-source="tableObj.dataSource"
			:loading="tableObj.loading"
			:pagination="tablePagination"
			@change="tableHandChangePage"
			:row-selection="{ selectedRowKeys: tableObj.selectedRowKeys, onChange: onTableSelectChange }"
			bordered
			size="small"
			tableLayout="fixed"
		>
			<template #bodyCell="{ index, column, record }">
				<template v-if="column.key === 'pageIndex'">
					{{ index + 1 }}
				</template>
				<template v-else-if="column.key === 'configType'">
          			<span>
            			{{ AutoeeUtil.getDictLabelByKey(record.configType , pageDataObj.sysYesNoDict) }}
          			</span>
				</template>
				<template v-else-if="column.key === 'configTableOperateCol'">
					<span>
						<a @click="openDetailModal(record)"> 查看 </a>
						<a-divider type="vertical" v-hasPermi="['system:config:edit']"/>
						<a @click="openUpdateModal(record)" v-hasPermi="['system:config:edit']"> 修改 </a>
          			</span>
				</template>
			</template>
		</a-table>

		<AddUpdateModal ref="addUpdateModalRef" @refreshFatherPageTable="queryTableData"
		                :sysYesNoDict="pageDataObj.sysYesNoDict"
		/>
		<DetailModal ref="detailModalRef"
		             :sysYesNoDict="pageDataObj.sysYesNoDict"
		/>
	</div>
</template>

<script setup name="Config">
	import {listConfig, getConfig, delConfig, addConfig, updateConfig} from "@/api/system/config";
	import AddUpdateModal from './addUpdateModal.vue';
	import DetailModal from './detailModal.vue';
	import {computed, reactive, ref} from 'vue';
	import {AutoeeIcon} from '@/utils/AutoeeIcon.js';
	import AutoeeUtil from '@/utils/AutoeeUtil.js';
	import {message} from 'ant-design-vue';
	import moment from 'moment';

	// 当前页面使用的数据
	const pageDataObj = reactive({
    sysYesNoDict: [],
	});

	AutoeeUtil.getDictsByType('sys_yes_no').then((res) => {
		pageDataObj.sysYesNoDict = res;
	});

	// 查询Form
	const queryFormRef = ref();
	const queryFormObj = reactive({
		data: {
			pageNum: 1,
			pageSize: 10,
			params: {},
			//orderByColumn: "update_time",
			//isAsc: "desc",
		}
	});

	// 重置查询Form
	const resetQueryForm = () => {
		queryFormObj.data = {
			pageNum: 1,
			pageSize: 10,
			params: {},
			//orderByColumn: "update_time",
			//isAsc: "desc",
		}
		queryTableData();
	};

	// 列表定义
	const tableObj = reactive({
		// 列表数据集
		dataSource: [],
		// 列表总记录数
		total: 0,
		// 列表是否加载中
		loading: false,
		// 列表选中行数组
		selectedRowKeys: [],
		// 列表列定义
		columns: [
			{
				title: '序号',
				dataIndex: 'pageIndex',
				key: 'pageIndex',
				width: 50,
				align: 'center',
			},
			{
				title: '参数名称',
				key: 'configName',
				dataIndex: 'configName',
			},
			{
				title: '参数键名',
				key: 'configKey',
				dataIndex: 'configKey',
			},
			{
				title: '参数键值',
				key: 'configValue',
				dataIndex: 'configValue',
			},
			{
				title: '系统内置',
				key: 'configType',
				dataIndex: 'configType',
			},
			{
				title: '操作',
				key: 'configTableOperateCol',
				width: 100,
			},
		],
	});

	// 查询列表数据方法
	const queryTableData = () => {
		// 调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
		AutoeeUtil.getTableDataByQueryFunc(listConfig(queryFormObj.data), tableObj);
	}

	// 初始化查询列表数据
	queryTableData();

	// 列表翻页工具条：必须通过计算函数每次重新生成
	const tablePagination = computed(() => AutoeeUtil.createTablePagination(tableObj, queryFormObj));

	// 手动翻页方法
	const tableHandChangePage = (page, filters, sorter) => {
		queryFormObj.data.pageSize = page.pageSize;
		queryFormObj.data.pageNum = page.current;
		queryTableData();
	};

	// 列表行选中方法
	const onTableSelectChange = (selectedRowKeys) => {
		tableObj.selectedRowKeys = selectedRowKeys;
	};

	// 打开新增窗口
	const addUpdateModalRef = ref();
	const openAddModal = () => {
		addUpdateModalRef.value.openAddModal();
	};

	// 打开修改窗口
	const openUpdateModal = (record) => {
		addUpdateModalRef.value.openUpdateModal(record);
	};

	// 打开详细窗口
	const detailModalRef = ref();
	const openDetailModal = (record) => {
		detailModalRef.value.openModal(record);
	};

	// 点击删除
	const clickDelete = () => {
		AutoeeUtil.confirmDeleteSelectedData(() => {
			delConfig(tableObj.selectedRowKeys).then((res) => {
				AutoeeUtil.messageSuccess('删除操作成功。');
				queryTableData();
			});
		});
	};

	// 点击导出
	const clickExport = () => {
		let exportUrl = 'system/config/export';
		let moduleName = "参数配置";
		let exportFileName = moduleName + "_" + moment().format('YYYY-MM-DD_HH-mm-ss') + ".xlsx";
		AutoeeUtil.download(exportUrl, queryFormObj.data, exportFileName);
	};

</script>
<style lang="less"></style>
