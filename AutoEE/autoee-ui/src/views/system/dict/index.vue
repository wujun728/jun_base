<template>
	<div>
		<a-collapse v-model:activeKey="pageData.activeKey">
			<a-collapse-panel key="1" header="1.字典类型管理">
				<a-row :gutter="24" bo>
					<a-col>
						<a-form ref="queryTypeFormRef" name="queryTypeForm" :model="queryTypeForm.data"
							:labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
							<a-row :gutter="24">
								<a-col span="8">
									<a-form-item name="dictType" label="字典类型">
										<a-input v-model:value="queryTypeForm.data.dictType" allowClear></a-input>
									</a-form-item>
								</a-col>
								<a-col span="8">
									<a-form-item name="dictName" label="字典类型名称">
										<a-input v-model:value="queryTypeForm.data.dictName" allowClear></a-input>
									</a-form-item>
								</a-col>
								<a-col span="8">
									<a-form-item name="status" label="字典类型状态">
										<a-select v-model:value="queryTypeForm.data.status"
											:options="pageData.sysNormalDisableDict" allowClear> </a-select>
									</a-form-item>
								</a-col>
							</a-row>
							<a-row :gutter="24" class="autoee-button-row">
								<a-col :span="12" style="text-align: left">
									<a-button type="primary" @click="openTypeAddModal()"
										v-hasPermi="['system:dict:add']">
										<AutoeeIcon icon="plus-outlined" />新增
									</a-button>
									<a-button type="primary" danger @click="clickDeleteType()"
										:disabled="typeTableData.selectedRowKeys.length <= 0"
										v-hasPermi="['system:dict:remove']">
										<AutoeeIcon icon="delete-outlined" />删除
									</a-button>
									<a-button type="primary" @click="clickExportType()"
										v-hasPermi="['system:dict:export']">
										<AutoeeIcon icon="export-outlined" />导出
									</a-button>
								</a-col>
								<a-col :span="12" style="text-align: right">
									<a-button type="primary" @click="queryTypeTableData()">
										<AutoeeIcon icon="SearchOutlined" />查询
									</a-button>
									<a-button @click="resetTypeQueryForm()">
										<AutoeeIcon icon="redo-outlined" />重置
									</a-button>

								</a-col>
							</a-row>
						</a-form>
						<!-- 单选列表 type: 'radio' -->
						<a-table rowKey="dictId" :columns="typeTableData.columns"
							:data-source="typeTableData.dataSource" :loading="typeTableData.loading"
							:pagination="typeTablePagination" @change="typeTableHandChangePage"
							:row-selection="{ selectedRowKeys: typeTableData.selectedRowKeys, onChange: onTypeTableSelectChange, type: 'radio' }"
							bordered size="small" tableLayout="fixed">
							<template #bodyCell="{ index, column, record }">
								<template v-if="column.key === 'pageIndex'">
									{{ index + 1 }}
								</template>
								<template v-else-if="column.key === 'status'">
									<span>
										{{ AutoeeUtil.getDictLabelByKey(record.status, pageData.sysNormalDisableDict) }}
									</span>
								</template>
								<template v-else-if="column.key === 'typeTableOperateCol'">
									<span>
										<a @click="openTypeDetailModal(record)"> 查看 </a>
										<a-divider type="vertical" v-hasPermi="['system:dict:edit']" />
										<a @click="openTypeUpdateModal(record)" v-hasPermi="['system:dict:edit']"> 修改
										</a>
									</span>
								</template>
							</template>
						</a-table>

						<TypeAddUpdateModal ref="typeAddUpdateModalRef"
							:sysNormalDisableDict="pageData.sysNormalDisableDict"
							@refreshFatherTypeTableData="queryTypeTableData" />
						<TypeDetailModal ref="typeDetailModalRef" :sysNormalDisableDict="pageData.sysNormalDisableDict"
							@refreshFatherTypeTableData="queryTypeTableData" />
					</a-col>

				</a-row>
			</a-collapse-panel>
			<a-collapse-panel key="2" header="2.字典值管理">
				<a-row :gutter="24">
					<a-col>
						<a-form ref="queryDataFormRef" name="queryDataForm" :model="queryDataForm.data">
							<a-row :gutter="24">
								<a-col span="8">
									<a-form-item name="dictValue" label="字典值">
										<a-input v-model:value="queryDataForm.data.dictValue" allowClear></a-input>
									</a-form-item>
								</a-col>
								<a-col span="8">
									<a-form-item name="dictLabel" label="字典名称">
										<a-input v-model:value="queryDataForm.data.dictLabel" allowClear></a-input>
									</a-form-item>
								</a-col>
								<a-col span="8">
									<a-form-item name="status" label="状态">
										<a-select v-model:value="queryDataForm.data.status"
											:options="pageData.sysNormalDisableDict" allowClear> </a-select>
									</a-form-item>
								</a-col>
							</a-row>

							<a-row :gutter="24" class="autoee-button-row">
								<a-col :span="12" style="text-align: left">
									<a-button type="primary" @click="openDataAddModal()"
										v-hasPermi="['system:dict:add']">
										<AutoeeIcon icon="plus-outlined" />新增
									</a-button>
									<a-button type="primary" danger @click="clickDeleteData()"
										:disabled="dataTableData.selectedRowKeys.length <= 0"
										v-hasPermi="['system:dict:remove']">
										<AutoeeIcon icon="delete-outlined" />删除
									</a-button>
									<a-button type="primary" @click="clickExportData()"
										v-hasPermi="['system:dict:export']">
										<AutoeeIcon icon="export-outlined" />导出
									</a-button>
								</a-col>
								<a-col :span="12" style="text-align: right">
									<a-button type="primary" @click="queryDataTableData()">
										<AutoeeIcon icon="SearchOutlined" />查询
									</a-button>
									<a-button @click="resetDataQueryForm()">
										<AutoeeIcon icon="redo-outlined" />重置
									</a-button>
								</a-col>
							</a-row>
						</a-form>

						<a-table rowKey="dictCode" :columns="dataTableData.columns"
							:data-source="dataTableData.dataSource" :loading="dataTableData.loading"
							:pagination="dataTablePagination" @change="dataTableHandChangePage"
							:row-selection="{ selectedRowKeys: dataTableData.selectedRowKeys, onChange: onDataTableSelectChange }"
							bordered size="small" tableLayout="fixed">
							<template #bodyCell="{ index, column, record }">
								<template v-if="column.key === 'pageIndex'">
									{{ index + 1 }}
								</template>
								<template v-else-if="column.key === 'status'">
									<span>
										{{ AutoeeUtil.getDictLabelByKey(record.status, pageData.sysNormalDisableDict) }}
									</span>
								</template>
								<template v-else-if="column.key === 'dataTableOperateCol'">
									<span>
										<a @click="openDataDetailModal(record)"> 查看 </a>
										<a-divider type="vertical" v-hasPermi="['system:dict:edit']" />
										<a @click="openDataUpdateModal(record)" v-hasPermi="['system:dict:edit']"> 修改
										</a>
									</span>
								</template>
							</template>
						</a-table>

						<DataAddUpdateModal ref="dataAddUpdateModalRef" :sysYesNoDict="pageData.sysYesNoDict"
							:sysNormalDisableDict="pageData.sysNormalDisableDict"
							@refreshFatherDataTableData="queryDataTableData" />
						<DataDetailModal ref="dataDetailModalRef" :sysYesNoDict="pageData.sysYesNoDict"
							:sysNormalDisableDict="pageData.sysNormalDisableDict"
							@refreshFatherDataTableData="queryDataTableData" />
					</a-col>
				</a-row>
			</a-collapse-panel>
		</a-collapse>
	</div>
</template>

<script setup name="Type">
	import {
		listType,
		getType,
		delType,
		addType,
		updateType
	} from '@/api/system/dict/type';
	import TypeAddUpdateModal from './addUpdateModal.vue';
	import TypeDetailModal from './detailModal.vue';
	import {
		listData,
		getData,
		delData,
		addData,
		updateData
	} from '@/api/system/dict/data';
	import DataAddUpdateModal from './dataAddUpdateModal.vue';
	import DataDetailModal from './dataDetailModal.vue';
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
		sysNormalDisableDict: [],
		activeKey: ['1', '2']
	});

	AutoeeUtil.getDictsByType('sys_normal_disable').then((res) => {
		pageData.sysNormalDisableDict = res;
	});

	// 查询Form
	const queryTypeFormRef = ref();
	const queryTypeForm = reactive({
		data: {
			pageNum: 1,
			pageSize: 10,
			// params: {}
		}
	});

	// 重置查询Form
	const resetTypeQueryForm = () => {
		queryTypeForm.data = {
			pageNum: 1,
			pageSize: 10,
			// params: {}
		}
		queryTypeTableData();
	};

	// 打开新增窗口
	const typeAddUpdateModalRef = ref();
	const openTypeAddModal = () => {
		typeAddUpdateModalRef.value.openAddModal();
	};

	// 打开修改窗口
	const openTypeUpdateModal = (record) => {
		typeAddUpdateModalRef.value.openUpdateModal(record);
	};

	// 打开详细窗口
	const typeDetailModalRef = ref();
	const openTypeDetailModal = (record) => {
		typeDetailModalRef.value.openModal(record);
	};

	// 点击删除
	const clickDeleteType = () => {
		AutoeeUtil.confirmDeleteSelectedData(() => {
			delType(typeTableData.selectedRowKeys).then((res) => {
				typeTableData.selectedRowKeys = [];
				AutoeeUtil.messageSuccess('删除操作成功。');
				queryTypeTableData();
			});
		});
	};

	// 点击导出
	const clickExportType = () => {
		AutoeeUtil.download('system/dict/type/export', queryTypeForm.data, "字典类型_" + moment().format('YYYY-MM-DD_HH-mm-ss') + ".xlsx");
	};

	// 列表数据
	const typeTableData = reactive({
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
			// {
			//   title: '序号',
			//   dataIndex: 'pageIndex',
			//   key: 'pageIndex',
			//   width: 50,
			//   align: 'center',
			// },
			{
				title: '字典类型',
				key: 'dictType',
				dataIndex: 'dictType',
				width: 160,
			},
			{
				title: '字典类型名称',
				key: 'dictName',
				dataIndex: 'dictName',
			},
			{
				title: '状态',
				key: 'status',
				dataIndex: 'status',
			},
			{
				title: '操作',
				key: 'typeTableOperateCol',
				width: 100,
			},
		],
	});
	// 查询列表数据方法
	const queryTypeTableData = () => {
		// 调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
		AutoeeUtil.getTableDataByQueryFunc(listType(queryTypeForm.data), typeTableData);
	}

	// 初始化查询列表数据
	queryTypeTableData();

	// 列表翻页工具条：必须通过计算函数每次重新生成
	const typeTablePagination = computed(() => AutoeeUtil.createTablePagination(typeTableData, queryTypeForm));

	// 手动翻页方法
	const typeTableHandChangePage = (page, filters, sorter) => {
		queryTypeForm.data.pageSize = page.pageSize;
		queryTypeForm.data.pageNum = page.current;
		queryTypeTableData();
	};

	// 列表选中方法
	const onTypeTableSelectChange = (selectedRowKeys, selectedRows) => {
		typeTableData.selectedRowKeys = selectedRowKeys;
		queryDataForm.data.dictType = selectedRows[0].dictType;
		queryDataTableData();
	};

	// 查询Form
	const queryDataFormRef = ref();
	const queryDataForm = reactive({
		data: {
			pageNum: 1,
			pageSize: 10,
			params: {},
			dictType: ''
		}

	});

	// 重置查询Form
	const resetDataQueryForm = () => {
		typeTableData.selectedRowKeys = [];
		queryDataForm.data = {
			pageNum: 1,
			pageSize: 10,
			params: {},
			dictType: ''
		}
		queryDataTableData();
	};

	// 打开新增窗口
	const dataAddUpdateModalRef = ref();
	const openDataAddModal = () => {
		if (queryDataForm.data.dictType) {
			dataAddUpdateModalRef.value.openAddModal(queryDataForm.data.dictType);
		} else {
			AutoeeUtil.messageWarn('请先选择左侧字典类型，再进行新增字典数据！');
		}
	};

	// 打开修改窗口
	const openDataUpdateModal = (record) => {
		dataAddUpdateModalRef.value.openUpdateModal(record);
	};

	// 打开详细窗口
	const dataDetailModalRef = ref();
	const openDataDetailModal = (record) => {
		dataDetailModalRef.value.openModal(record);
	};

	// 点击删除
	const clickDeleteData = () => {
		AutoeeUtil.confirmDeleteSelectedData(() => {
			delData(dataTableData.selectedRowKeys).then((res) => {
				dataTableData.selectedRowKeys = [];
				AutoeeUtil.messageSuccess('删除操作成功。');
				queryDataTableData();
			});
		});
	};

	// 点击导出
	const clickExportData = () => {
		AutoeeUtil.download('system/dict/data/export', queryDataForm.data, "字典值_" + moment().format('YYYY-MM-DD_HH-mm-ss') + ".xlsx");
	};

	// 列表数据
	const dataTableData = reactive({
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
			// {
			//   title: '序号',
			//   dataIndex: 'pageIndex',
			//   key: 'pageIndex',
			//   width: 50,
			//   align: 'center',
			// },
			{
				title: '字典类型',
				key: 'dictType',
				dataIndex: 'dictType',
				width: 160,
			},
			{
				title: '字典值',
				key: 'dictValue',
				dataIndex: 'dictValue',
			},
			{
				title: '字典名称',
				key: 'dictLabel',
				dataIndex: 'dictLabel',
			},
			{
				title: '字典排序',
				key: 'dictSort',
				dataIndex: 'dictSort',
			},
			{
				title: '状态',
				key: 'status',
				dataIndex: 'status',
			},
			{
				title: '操作',
				key: 'dataTableOperateCol',
				width: 100,
			},
		],
	});

	// 查询列表数据方法
	const queryDataTableData = () => {
		// 调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
		AutoeeUtil.getTableDataByQueryFunc(listData(queryDataForm.data), dataTableData);
	}

	// 初始化查询列表数据
	queryDataTableData();

	// 列表翻页工具条：必须通过计算函数每次重新生成
	const dataTablePagination = computed(() => AutoeeUtil.createTablePagination(dataTableData, queryDataForm));

	// 手动翻页方法
	const dataTableHandChangePage = (page, filters, sorter) => {
		queryDataForm.data.pageSize = page.pageSize;
		queryDataForm.data.pageNum = page.current;
		queryDataTableData();
	};

	// 列表选中方法
	const onDataTableSelectChange = (selectedRowKeys, selectedRows) => {
		dataTableData.selectedRowKeys = selectedRowKeys;

	};
</script>
<style lang="less"></style>
