<template>
	<div>
		<a-form ref="queryFormRef" name="queryFormObj" :model="queryFormObj.data" :labelCol="{ span: 8 }"
			:wrapperCol="{ span: 14 }">
			<a-row :gutter="24">
				<a-col span="8">
					<a-form-item name="tableName" label="表名称">
						<a-input v-model:value="queryFormObj.data.tableName"></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="tableComment" label="表描述">
						<a-input v-model:value="queryFormObj.data.tableComment"></a-input>
					</a-form-item>
				</a-col>

				<a-col :span="8">
					<a-form-item name="beginTime" label="创建起期">
						<a-date-picker v-model:value="queryFormObj.data.params.beginTime" format="YYYY-MM-DD"
							valueFormat="YYYY-MM-DD" />
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item name="endTime" label="创建止期">
						<a-date-picker v-model:value="queryFormObj.data.params.endTime" format="YYYY-MM-DD"
							valueFormat="YYYY-MM-DD" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="24" class="autoee-button-row">
				<a-col :span="12" style="text-align: left">
					<a-button type="primary" @click="openImportTablesModal()">
						<AutoeeIcon icon="plus-outlined" />导入表
					</a-button>
					<a-button type="primary" danger @click="clickDelete()"
						:disabled="tableObj.selectedRowKeys.length <= 0">
						<AutoeeIcon icon="delete-outlined" />删除
					</a-button>
				</a-col>
				<a-col :span="12" style="text-align: right">
					<a-button type="primary" @click="queryTableData()">
						<AutoeeIcon icon="SearchOutlined" />查询
					</a-button>
					<a-button @click="resetQueryForm()">
						<AutoeeIcon icon="redo-outlined" />重置
					</a-button>
				</a-col>
			</a-row>
		</a-form>
		<!-- tableLayout设为 fixed 表示内容不会影响列的布局 	- | 'auto' | 'fixed' -->
		<a-table rowKey="tableId" :columns="tableObj.columns" :data-source="tableObj.dataSource"
			:loading="tableObj.loading" :pagination="tablePagination" @change="tableHandChangePage"
			:row-selection="{ selectedRowKeys: tableObj.selectedRowKeys, onChange: onTableSelectChange }" bordered
			size="small" tableLayout="fixed">
			<template #bodyCell="{ index, column, record }">
				<template v-if="column.key === 'pageIndex'">
					{{ index + 1 }}
				</template>
				<template v-else-if="column.key === 'status'">
					<span>
						<a-tag v-if="record.status === '0'" color="green" style="width: 50px; text-align: center">
							{{ AutoeeUtil.getDictLabelByKey(record.status, pageDataObj.statusDict) }}
						</a-tag>
						<a-tag v-else-if="record.status === '1'" color="red" style="width: 50px; text-align: center">
							{{ AutoeeUtil.getDictLabelByKey(record.status, pageDataObj.statusDict) }}
						</a-tag>
					</span>
				</template>
				<template v-else-if="column.key === 'sex'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.sex, pageDataObj.sexDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'operateCol'">
					<span>
						<!-- <a @click="openUpdateModal(record)"> 预览 </a>
            <a-divider type="vertical" /> -->
						<a @click="openUpdateModal(record)"> 修改 </a>
						<a-divider type="vertical" />
						<a @click="openPreviewModal(record)"> 预览 </a>
						<a-divider type="vertical" />
						<!-- <a @click="openUpdateModal(record)"> 同步 </a>
            <a-divider type="vertical" /> -->
						<a @click="generateCode(record)"> 生成代码 </a>
					</span>
				</template>
			</template>
		</a-table>

		<ImportTables ref="importTablesRef" @refreshFatherPageTable="queryTableData" />
		<GenCodeConfigUpdate ref="genCodeConfigUpdateRef" @refreshFatherPageTable="queryTableData" />
		<GenCodePreview ref="genCodePreviewRef" />
	</div>
</template>

<script setup>
	import {
		listTable,
		previewTable,
		delTable,
		genCode,
		synchDb,
		batchGenCode
	} from '@/api/tool/gen';
	import GenCodeConfigUpdate from './genCodeConfigUpdate.vue';
	import GenCodePreview from './genCodePreview.vue';
	import ImportTables from './importTables.vue';
	import {
		computed,
		reactive,
		ref
	} from 'vue';
	import AutoeeUtil from '@/utils/AutoeeUtil.js';
	import {
		AutoeeIcon
	} from '@/utils/AutoeeIcon.js';
	import {
		message
	} from 'ant-design-vue';
import moment from 'moment';

	// 当前页面使用的数据
	const pageDataObj = reactive({
		sexDict: [],
		deptTreeData: [],
		statusDict: [],
		postDict: [],
		roleDict: [],
	});
	AutoeeUtil.getDictsByType('sys_user_sex').then((res) => {
		pageDataObj.sexDict = res;
	});
	AutoeeUtil.getDeptTreeData().then((res) => {
		pageDataObj.deptTreeData = res.data;
	});
	AutoeeUtil.getDictsByType('sys_normal_disable').then((res) => {
		pageDataObj.statusDict = res;
	});

	// 查询Form
	const queryFormRef = ref();
	const queryFormObj = reactive({
		data: {
			pageNum: 1,
			pageSize: 10,
			params: {},
			orderByColumn: "update_time",
			isAsc: "desc",
		}
	});

	// 重置查询Form
	const resetQueryForm = () => {
		queryFormObj.data = {
			pageNum: 1,
			pageSize: 10,
			params: {},
			orderByColumn: "update_time",
			isAsc: "desc",
		}
		queryTableData();
	};


	// 用户列表数据
	const tableObj = reactive({
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
				title: '表名称',
				dataIndex: 'tableName',
				key: 'tableName',
			},
			{
				title: '表描述',
				dataIndex: 'tableComment',
				key: 'tableComment',
			},
			{
				title: '实体',
				dataIndex: 'className',
				key: 'className',
			},
			{
				title: '创建时间',
				dataIndex: 'createTime',
				key: 'createTime',
			},
			{
				title: '更新时间',
				dataIndex: 'updateTime',
				key: 'updateTime',
			},
			{
				title: '操作',
				key: 'operateCol',
				width: 200,
			},
		],
	});

	// 列表选中方法
	const onTableSelectChange = (selectedRowKeys) => {
		tableObj.selectedRowKeys = selectedRowKeys;
	};



	// 查询列表数据
	const queryTableData = () => {
		// 调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
		AutoeeUtil.getTableDataByQueryFunc(listTable(queryFormObj.data),tableObj);
	}

	// 默认查询列表数据
	queryTableData();

	// 用户列表翻页工具条：必须通过计算函数每次重新生成
	const tablePagination = computed(() => AutoeeUtil.createTablePagination(tableObj, queryFormObj));
	// 手动翻页方法
	const tableHandChangePage = (page, filters, sorter) => {
		queryFormObj.data.pageSize = page.pageSize;
		queryFormObj.data.pageNum = page.current;
		queryTableData();
	};

	// 打开导入表窗口
	const importTablesRef = ref();
	const openImportTablesModal = () => {
		importTablesRef.value.openModal();
	};

	// 打开修改窗口
	const genCodeConfigUpdateRef = ref();
	const openUpdateModal = (record) => {
		genCodeConfigUpdateRef.value.openUpdateModal(record);
	};

	// 打开预览窗口
	const genCodePreviewRef = ref();
	const openPreviewModal = (record) => {
		genCodePreviewRef.value.openPreviewModal(record);
	};

	const userDetailRef = ref();
	// 打开详细窗口
	const clickUserDetail = (record) => {
		userDetailRef.value.openModal(record);
	};

	// 生成代码
	const uploadRef = ref();
	const generateCode = (record) => {
		const tableNames = record.tableName;

		if (record.genType === '1') {
			genCode(record.tableName).then((response) => {
				AutoeeUtil.messageSuccess('成功生成到自定义路径：' + record.genPath);
			});
		} else {
			AutoeeUtil.zip('/tool/gen/batchGenCode?tables=' + tableNames, '表[' + tableNames + ']代码文件.zip');
		}
	};

	// 点击删除
	const clickDelete = () => {
		AutoeeUtil.confirmDeleteSelectedData(() => {
			delTable(tableObj.selectedRowKeys).then((res) => {
				tableObj.selectedRowKeys = [];
				AutoeeUtil.messageSuccess('删除操作成功。');
				 queryTableData();
			});
		});
	};
</script>

<style lang="less"></style>
