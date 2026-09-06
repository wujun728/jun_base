<template>
	<div>
		<a-form ref="queryFormRef" name="queryFormObj" :model="queryFormObj.data" :labelCol="{ span: 8 }"
			:wrapperCol="{ span: 14 }">
			<a-row :gutter="24">
				<a-col span="8">
					<a-form-item name="deptName" label="部门名称">
						<a-input v-model:value="queryFormObj.data.deptName" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="status" label="部门状态">
						<a-select v-model:value="queryFormObj.data.status" :options="pageData.statusDict" allowClear>
						</a-select>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="24" class="autoee-button-row">
				<a-col :span="12" style="text-align: left">
					<a-button type="primary" @click="openDeptAddModal()" v-hasPermi="['system:dept:add']">
						<AutoeeIcon icon="plus-outlined" />新增
					</a-button>
					<a-button type="primary" danger @click="clickDeleteDept()"
						:disabled="tableObj.selectedRowKeys.length <= 0" v-hasPermi="['system:dept:remove']">
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

		<a-table rowKey="deptId" :columns="tableObj.columns" :data-source="tableObj.dataSource"
			:loading="tableObj.loading" :pagination="tablePagination" @change="tableHandChangePage"
			:row-selection="{ selectedRowKeys: tableObj.selectedRowKeys, onChange: onTableSelectChange, type: 'radio' }"
			bordered size="small" tableLayout="fixed">
			<template #bodyCell="{ index, column, record }">
				<template v-if="column.key === 'pageIndex'">
					{{ index + 1 }}
				</template>
				<template v-else-if="column.key === 'status'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.status, pageData.statusDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'deptTableOperateCol'">
					<span>
						<a @click="openDeptDetailModal(record)"> 查看 </a>
						<a-divider type="vertical" v-hasPermi="['system:dept:edit']" />
						<a @click="openDeptUpdateModal(record)" v-hasPermi="['system:dept:edit']"> 修改 </a>
					</span>
				</template>
			</template>
		</a-table>

		<DeptAddUpdateModal ref="deptAddUpdateModalRef" @querDeptTreeData="querDeptTreeData"
			:statusDict="pageData.statusDict" :deptTreeData="pageData.deptTreeData"
			@refreshFatherPageTable="queryTableData" />
		<DeptDetailModal ref="deptDetailModalRef" :statusDict="pageData.statusDict"
			:deptTreeData="pageData.deptTreeData" />
	</div>
</template>

<script setup name="Dept">
	import {
		listDept,
		getDept,
		delDept,
		addDept,
		updateDept
	} from '@/api/system/dept';
	import DeptAddUpdateModal from './addUpdateModal.vue';
	import DeptDetailModal from './detailModal.vue';
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
	import {
		usePagination
	} from 'vue-request';

	// 当前页面使用的数据
	const pageData = reactive({
		statusDict: [],
		deptTreeData: [],
		deptTreeData: [],
	});

	AutoeeUtil.getDictsByType('sys_normal_disable').then((res) => {
		pageData.statusDict = res;
	});
	const querDeptTreeData = () => {
		AutoeeUtil.getDeptTreeData().then((res) => {
			pageData.deptTreeData = res.data;
			// pageData.deptTreeData.push({ id: 0, label: '无' });
		});
	};
	querDeptTreeData();

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
	const resetQueryForm = () => {
		queryFormObj.data = {
			pageNum: 1,
			pageSize: 10,
			params: {}
		}
		queryTableData();
	};

	// 打开新增窗口
	const deptAddUpdateModalRef = ref();
	const openDeptAddModal = () => {
		deptAddUpdateModalRef.value.openAddModal();
	};

	// 打开修改窗口
	const openDeptUpdateModal = (record) => {
		deptAddUpdateModalRef.value.openUpdateModal(record);
	};

	// 打开详细窗口
	const deptDetailModalRef = ref();
	const openDeptDetailModal = (record) => {
		deptDetailModalRef.value.openModal(record);
	};

	// 点击删除
	const clickDeleteDept = () => {
		AutoeeUtil.confirmDeleteSelectedData(() => {
			delDept(tableObj.selectedRowKeys).then((res) => {
				tableObj.selectedRowKeys = [];
				AutoeeUtil.messageSuccess('删除操作成功。');
				// 操作后，更新部门下拉列表数据
				AutoeeUtil.getDeptTreeData().then((res) => {
					pageData.deptTreeData = res.data;
				});
				deptTablePage.reload();
			});
		});
	};

	// 列表数据
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
			// {
			//   title: '序号',
			//   dataIndex: 'pageIndex',
			//   key: 'pageIndex',
			//   width: 50,
			//   align: 'center',
			// },
			{
				title: '部门id',
				key: 'deptId',
				dataIndex: 'deptId',
			},
			{
				title: '部门名称',
				key: 'deptName',
				dataIndex: 'deptName',
			},
			{
				title: '显示顺序',
				key: 'orderNum',
				dataIndex: 'orderNum',
			},
			{
				title: '负责人',
				key: 'leader',
				dataIndex: 'leader',
			},
			{
				title: '部门状态',
				key: 'status',
				dataIndex: 'status',
			},
			{
				title: '操作',
				key: 'deptTableOperateCol',
				width: 120,
			},
		],
	});



	// 查询列表数据方法
	const queryTableData = () => {
		// 调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
		AutoeeUtil.getTableTreeByQueryFunc(listDept(queryFormObj.data), tableObj, 'deptId');
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

	// 列表选中方法
	const onTableSelectChange = (selectedRowKeys) => {
		tableObj.selectedRowKeys = selectedRowKeys;
	};
</script>
<style lang="less"></style>
