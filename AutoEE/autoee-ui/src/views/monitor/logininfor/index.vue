<template>
	<div>
		<a-form ref="queryFormObj.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
			<a-row :gutter="24">
				<a-col span="8">
					<a-form-item name="userName" label="用户账号">
						<a-input v-model:value="queryFormObj.data.userName" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="ipaddr" label="登录IP地址">
						<a-input v-model:value="queryFormObj.data.ipaddr" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="loginLocation" label="登录地点">
						<a-input v-model:value="queryFormObj.data.loginLocation" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="browser" label="浏览器类型">
						<a-input v-model:value="queryFormObj.data.browser" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="os" label="操作系统">
						<a-input v-model:value="queryFormObj.data.os" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="status" label="登录状态">
						<a-select v-model:value="queryFormObj.data.status" :options="pageDataObj.sysCommonStatusDict"
							allowClear> </a-select>
					</a-form-item>
				</a-col>
				<a-col span="8">
					<a-form-item name="msg" label="提示消息">
						<a-input v-model:value="queryFormObj.data.msg" allowClear></a-input>
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item name="loginTime" label="访问时间起期">
						<a-date-picker v-model:value="queryFormObj.data.params.beginTime" format="YYYY-MM-DD"
							valueFormat="YYYY-MM-DD" />
					</a-form-item>
				</a-col>
				<a-col :span="8">
					<a-form-item name="loginTime" label="访问时间止期">
						<a-date-picker v-model:value="queryFormObj.data.params.endTime" format="YYYY-MM-DD"
							valueFormat="YYYY-MM-DD" />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="24" class="autoee-button-row">
				<a-col :span="12" style="text-align: left">
					<!-- <a-button type="primary" @click="openAddModal()" v-hasPermi="['monitor:logininfor:add']"><AutoeeIcon icon="plus-outlined" />新增</a-button>
          <a-button type="primary" danger @click="clickDelete()" :disabled="tableObj.selectedRowKeys.length <= 0" v-hasPermi="['monitor:logininfor:remove']"><AutoeeIcon icon="delete-outlined" />删除</a-button> -->
					<a-button type="primary" @click="clickExport()" v-hasPermi="['monitor:logininfor:export']">
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

		<a-table rowKey="infoId" :columns="tableObj.columns" :data-source="tableObj.dataSource"
			:loading="tableObj.loading" :pagination="tablePagination" @change="tableHandChangePage"
			:row-selection="{ selectedRowKeys: tableObj.selectedRowKeys, onChange: onTableSelectChange }" bordered
			size="small" tableLayout="fixed">
			<template #bodyCell="{ index, column, record }">
				<template v-if="column.key === 'pageIndex'">
					{{ index + 1 }}
				</template>
				<template v-else-if="column.key === 'status'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.status , pageDataObj.sysCommonStatusDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'logininforTableOperateCol'">
					<span>
						<a @click="openDetailModal(record)"> 查看 </a>
						<!--   <a-divider type="vertical" v-hasPermi="['monitor:logininfor:edit']"/>
            <a @click="openUpdateModal(record)"  v-hasPermi="['monitor:logininfor:edit']"> 修改 </a> -->
					</span>
				</template>
			</template>
		</a-table>

		<!--    <AddUpdateModal ref="addUpdateModalRef" @refreshFatherPageTable="queryTableData"
        :sysCommonStatusDict="pageDataObj.sysCommonStatusDict"
    /> -->
		<DetailModal ref="detailModalRef" :sysCommonStatusDict="pageDataObj.sysCommonStatusDict" />
	</div>
</template>

<script setup name="Logininfor">
	import {
		list,
		delLogininfor
	} from "@/api/monitor/logininfor";
	// import AddUpdateModal from './addUpdateModal.vue';
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
		sysCommonStatusDict: [],
	});

	AutoeeUtil.getDictsByType('sys_common_status').then((res) => {
		pageDataObj.sysCommonStatusDict = res;
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
		columns: [{
				title: '序号',
				dataIndex: 'pageIndex',
				key: 'pageIndex',
				width: 50,
				align: 'center',
			},
			{
				title: '用户账号',
				key: 'userName',
				dataIndex: 'userName',
			},
			{
				title: '登录IP地址',
				key: 'ipaddr',
				dataIndex: 'ipaddr',
			},
			{
				title: '登录地点',
				key: 'loginLocation',
				dataIndex: 'loginLocation',
			},
			{
				title: '浏览器类型',
				key: 'browser',
				dataIndex: 'browser',
			},
			{
				title: '操作系统',
				key: 'os',
				dataIndex: 'os',
			},
			{
				title: '登录状态',
				key: 'status',
				dataIndex: 'status',
			},
			{
				title: '提示消息',
				key: 'msg',
				dataIndex: 'msg',
			},
			{
				title: '访问时间',
				key: 'loginTime',
				dataIndex: 'loginTime',
			},
			{
				title: '操作',
				key: 'logininforTableOperateCol',
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
			delLogininfor(tableObj.selectedRowKeys).then((res) => {
				AutoeeUtil.messageSuccess('删除操作成功。');
				queryTableData();
			});
		});
	};

	// 点击导出
	const clickExport = () => {
		let exportUrl = 'monitor/logininfor/export';
		let moduleName = "登陆日志";
		let exportFileName = moduleName + "_" + moment().format('YYYY-MM-DD_HH-mm-ss') + ".xlsx";
		AutoeeUtil.download(exportUrl, queryFormObj.data, exportFileName);
	};
</script>
<style lang="less"></style>
