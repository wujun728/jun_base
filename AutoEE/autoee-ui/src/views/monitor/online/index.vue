<template>
	<div>
		<a-form ref="queryFormRef" name="queryFormObj" :model="queryFormObj.data" :labelCol="{ span: 8 }"
			:wrapperCol="{ span: 14 }">
			<a-row :gutter="24">
				<a-col span="8">
					<a-form-item name="title" label="登录地址">
						<a-input v-model:value="queryFormObj.data.ipaddr" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="title" label="用户名称">
						<a-input v-model:value="queryFormObj.data.userName" allowClear></a-input>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="24" class="autoee-button-row">
				<a-col :span="24" style="text-align: right">
					<a-button type="primary" @click="queryTableData()">
						<AutoeeIcon icon="SearchOutlined" />查询
					</a-button>
					<a-button @click="resetQueryForm()">
						<AutoeeIcon icon="redo-outlined" />重置
					</a-button>
				</a-col>
			</a-row>
		</a-form>

		<a-table rowKey="operId" :columns="tableObj.columns" :data-source="tableObj.dataSource"
			:loading="tableObj.loading" :pagination="onlineTablePagination" @change="onlineTableHandChangePage"
			:row-selection="{ selectedRowKeys: tableObj.selectedRowKeys, onChange: onOnlineTableSelectChange }" bordered
			size="small" tableLayout="fixed">
			<template #bodyCell="{ index, column, record }">
				<template v-if="column.key === 'pageIndex'">
					{{ index + 1 }}
				</template>
				<template v-else-if="column.key === 'loginTime'">
					<span> {{ AutoeeUtil.parseDateTime(record.loginTime) }} </span>
				</template>
				<template v-else-if="column.key === 'onlineTableOperateCol'">
					<span>
						<a @click="clickForceLogout(record)"> 强退 </a>
					</span>
				</template>
			</template>
		</a-table>
	</div>
</template>

<script setup name="Online">
	import {
		list,
		forceLogout
	} from '@/api/monitor/online';
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
	// 点击强退
	const clickForceLogout = (row) => {
		AutoeeUtil.confirmOperate('强退', () => {
			forceLogout(row.tokenId)
				.then(() => {
					AutoeeUtil.messageSuccess('强退操作成功。');
					queryTableData();
				})
				.catch(() => {});
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
		columns: [{
				title: '序号',
				dataIndex: 'pageIndex',
				key: 'pageIndex',
				width: 50,
				align: 'center',
			},
			{
				title: '会话编号',
				key: 'tokenId',
				dataIndex: 'tokenId',
				ellipsis: true,
			},
			{
				title: '登录名称',
				key: 'userName',
				dataIndex: 'userName',
			},
			{
				title: '部门名称',
				key: 'deptName',
				dataIndex: 'deptName',
			},
			{
				title: '登录地址',
				key: 'ipaddr',
				dataIndex: 'ipaddr',
			},
			{
				title: '登录地点',
				key: 'loginLocation',
				dataIndex: 'loginLocation',
			},
			{
				title: '浏览器',
				key: 'browser',
				dataIndex: 'browser',
			},
			{
				title: '操作系统',
				key: 'os',
				dataIndex: 'os',
			},
			{
				title: '登录时间',
				key: 'loginTime',
				dataIndex: 'loginTime',
			},
			{
				title: '操作',
				key: 'onlineTableOperateCol',
				width: 100,
			},
		],
	});

	// 查询列表数据方法
	const queryTableData = () => {
		// 调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
		AutoeeUtil.getTableDataByQueryFunc(list(queryFormObj.data), tableObj);
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
	const onOnlineTableSelectChange = (selectedRowKeys, selectedRows) => {
		tableObj.selectedRowKeys = selectedRowKeys;
	};
</script>
<style lang="less"></style>
