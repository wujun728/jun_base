<template>
	<div>
		<a-form ref="queryFormObj.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
			<a-row :gutter="24">
				<a-col span="8">
					<a-form-item name="roleName" label="角色名称">
						<a-input v-model:value="queryFormObj.data.roleName" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="roleKey" label="角色编码">
						<a-input v-model:value="queryFormObj.data.roleKey" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="roleSort" label="显示顺序">
						<a-input v-model:value="queryFormObj.data.roleSort" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="status" label="角色状态">
						<a-select v-model:value="queryFormObj.data.status" :options="pageDataObj.sysNormalDisableDict"
							allowClear> </a-select>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="24" class="autoee-button-row">
				<a-col :span="12" style="text-align: left">
					<a-button type="primary" @click="openAddModal()" v-hasPermi="['system:role:add']">
						<AutoeeIcon icon="plus-outlined" />新增
					</a-button>
					<a-button type="primary" danger @click="clickDelete()"
						:disabled="tableObj.selectedRowKeys.length <= 0" v-hasPermi="['system:role:remove']">
						<AutoeeIcon icon="delete-outlined" />删除
					</a-button>
					<a-button type="primary" @click="clickExport()" v-hasPermi="['system:role:export']">
						<AutoeeIcon icon="export-outlined" />导出
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

		<a-table rowKey="roleId" :columns="tableObj.columns" :data-source="tableObj.dataSource"
			:loading="tableObj.loading" :pagination="tablePagination" @change="tableHandChangePage"
			:row-selection="{ selectedRowKeys: tableObj.selectedRowKeys, onChange: onTableSelectChange }" bordered
			size="small" tableLayout="fixed">
			<template #bodyCell="{ index, column, record }">
				<template v-if="column.key === 'pageIndex'">
					{{ index + 1 }}
				</template>
				<template v-else-if="column.key === 'status'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.status , pageDataObj.sysNormalDisableDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'roleTableOperateCol'">
					<span>
						<a @click="openDetailModal(record)"> 查看 </a>
						<a-divider type="vertical" v-hasPermi="['system:role:edit']" />
						<a @click="openUpdateModal(record)" v-hasPermi="['system:role:edit']"> 修改 </a>
					</span>
				</template>
			</template>
		</a-table>

		<AddUpdateModal ref="addUpdateModalRef" @refreshFatherPageTable="queryTableData"
			:sysNormalDisableDict="pageDataObj.sysNormalDisableDict" :dataScopeDict="pageDataObj.dataScopeDict"
			:menuCheckStrictlyDict="pageDataObj.menuCheckStrictlyDict" />
		<DetailModal ref="detailModalRef" :sysNormalDisableDict="pageDataObj.sysNormalDisableDict"
			:dataScopeDict="pageDataObj.dataScopeDict" :menuCheckStrictlyDict="pageDataObj.menuCheckStrictlyDict" />
	</div>
</template>

<script setup name="Role">
	import {
		listRole,
		getRole,
		delRole,
		addRole,
		updateRole
	} from "@/api/system/role";
	import AddUpdateModal from './addUpdateModal.vue';
	import DetailModal from './detailModal.vue';
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
	const pageDataObj = reactive({
		dataScopeDict: [],
		sysNormalDisableDict: [],
		menuCheckStrictlyDict: [],
	});

	AutoeeUtil.getDictsByType('sys_normal_disable').then((res) => {
		pageDataObj.sysNormalDisableDict = res;
	});
	// 自定义转码字典
	pageDataObj.dataScopeDict = [{
			value: '1',
			label: '全部数据权限',
		},
		// 暂不支持自定数据权限
		// {
		//   value: '2',
		//   label: '自定数据权限',
		// },
		{
			value: '3',
			label: '本部门数据权限',
		},
		{
			value: '4',
			label: '本部门及以下数据权限',
		},
		{
			value: '5',
			label: '仅本人数据权限',
		},
	];

	// 自定义树形下拉框转码字典
	pageDataObj.menuCheckStrictlyDict = [];
	AutoeeUtil.getDictsByType('sys_normal_disable').then((res) => {
		pageDataObj.sysNormalDisableDict = res;
	});

	// 查询Form
	const queryFormRef = ref();
	const queryFormObj = reactive({
		data: {
			pageNum: 1,
			pageSize: 10,
			params: {}
		}
	});

	// 重置查询Form
	const resetRoleQueryForm = () => {
		queryFormObj.data = {
			pageNum: 1,
			pageSize: 10,
			params: {}
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
		columns: [{
				title: '序号',
				dataIndex: 'pageIndex',
				key: 'pageIndex',
				width: 50,
				align: 'center',
			},
			{
				title: '角色名称',
				key: 'roleName',
				dataIndex: 'roleName',
			},
			{
				title: '角色编码',
				key: 'roleKey',
				dataIndex: 'roleKey',
			},
			{
				title: '显示顺序',
				key: 'roleSort',
				dataIndex: 'roleSort',
			},
			{
				title: '角色状态',
				key: 'status',
				dataIndex: 'status',
			},
			{
				title: '操作',
				key: 'roleTableOperateCol',
				width: 100,
			},
		],
	});

	// 查询列表数据方法
	const queryTableData = () => {
		// 调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
		AutoeeUtil.getTableDataByQueryFunc(listRole(queryFormObj.data), tableObj);
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
			delRole(tableObj.selectedRowKeys).then((res) => {
				AutoeeUtil.messageSuccess('删除操作成功。');
				roleTablePage.reload();
			});
		});
	};

	// 点击导出
	const clickExport = () => {
		AutoeeUtil.download('system/role/export', queryFormObj.data, "角色信息_" + moment().format('YYYY-MM-DD_HH-mm-ss') + ".xlsx");
	};
</script>
<style lang="less"></style>
