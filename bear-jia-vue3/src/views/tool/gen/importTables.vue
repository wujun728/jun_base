<template>
	<div>
		<a-modal v-model:open="pageDataObj.visible" width="60%" :title="pageDataObj.title" :destroyOnClose="true"
			@ok="clickModalOk" @cancel="handleModalCancel">
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
					<a-col :span="8" style="text-align: right">
						<a-button type="primary" @click="queryTableData()">
							<BearJiaIcon icon="SearchOutlined" />查询
						</a-button>
						<a-button @click="resetQueryForm()">
							<BearJiaIcon icon="redo-outlined" />重置
						</a-button>
					</a-col>
				</a-row>
			</a-form>
			<!-- tableLayout设为 fixed 表示内容不会影响列的布局 	- | 'auto' | 'fixed' -->
			<a-table rowKey="tableName" :columns="tableObj.columns" :data-source="tableObj.dataSource"
				:loading="tableObj.loading" :pagination="tablePagination" @change="tableHandChangePage"
				:row-selection="{ selectedRowKeys: tableObj.selectedRowKeys, onChange: onTableSelectChange }" bordered
				size="small" tableLayout="fixed">
				<template #bodyCell="{ index, column, record }">
					<template v-if="column.key === 'pageIndex'">
						{{ index + 1 }}
					</template>
				</template>
			</a-table>
		</a-modal>
	</div>
</template>

<script setup>
	import {
		listDbTable,
		importTable
	} from '@/api/tool/gen';
	import {
		reactive,
		ref,
		computed
	} from 'vue';
	import BearJiaUtil from '@/utils/BearJiaUtil.js';
	import { BearJiaIcon } from '@/utils/BearJiaIcon.js';

	// 引入父页面方法
	const pageEmit = defineEmits(["refreshFatherPageTable"]);

	// 当前页面使用的数据
	const pageDataObj = reactive({
		title: '导入表',
		visible: false,
		operateType: '',
		sexDict: [],
		deptTreeData: [],
		statusDict: [],
		postDict: [],
		roleDict: [],
	});
	BearJiaUtil.getDictsByType('sys_user_sex').then((res) => {
		pageDataObj.sexDict = res;
	});
	BearJiaUtil.getDeptTreeData().then((res) => {
		pageDataObj.deptTreeData = res.data;
	});
	BearJiaUtil.getDictsByType('sys_normal_disable').then((res) => {
		pageDataObj.statusDict = res;
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
	const resetQueryForm = () => {
		queryFormObj.data = {
			pageNum: 1,
			pageSize: 10,
			params: {}
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
				title: '创建时间',
				dataIndex: 'createTime',
				key: 'createTime',
			},
			{
				title: '更新时间',
				dataIndex: 'updateTime',
				key: 'updateTime',
			},
		],
	});

	// 列表选中方法
	const onTableSelectChange = (selectedRowKeys) => {
		tableObj.selectedRowKeys = selectedRowKeys;
	};


	// 查询列表数据
	const queryTableData = () => {
		// 调用后端列表查询方法，通过返回结果设置operlogTableObj.total，operlogTableObj.dataSource，operlogTableObj.loading
		BearJiaUtil.getTableDataByQueryFunc(listDbTable(queryFormObj.data), tableObj);
	}

	// 默认查询列表数据
	queryTableData();


	// 用户列表翻页工具条：必须通过计算函数每次重新生成
	const tablePagination = computed(() => BearJiaUtil.createTablePagination(tableObj, queryFormObj));
	// 手动翻页方法
	const tableHandChangePage = (page, filters, sorter) => {
		queryFormObj.data.pageSize = page.pageSize;
		queryFormObj.data.pageNum = page.current;
		queryTableData();
	};

	// 打开窗口
	const openModal = () => {
		resetQueryForm();
		pageDataObj.visible = true;
	};

	// 点击窗口确认
	const clickModalOk = (e) => {
		const tableNames = tableObj.selectedRowKeys.join(',');
		if (tableNames == '') {
			BearJiaUtil.messageWarn('请选择要导入的表！');
			return;
		}
		importTable({
			tables: tableNames
		}).then((res) => {
			BearJiaUtil.messageSuccess('导入表操作成功。');
			if (res.code === 200) {
				pageDataObj.visible = false;
				resetQueryForm();
				// 调用父页面刷新方法
				pageEmit("refreshFatherPageTable");
			}
		});
	};

	// 点击窗口取消
	const handleModalCancel = () => {};

	// 对外暴露出去
	defineExpose({
		openModal,
	});
</script>

<style lang="less"></style>
