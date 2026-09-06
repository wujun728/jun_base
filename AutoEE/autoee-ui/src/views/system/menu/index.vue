<template>
	<div>
		<a-form ref="queryFormRef" name="queryFormObj" :model="queryFormObj.data" :labelCol="{ span: 8 }"
			:wrapperCol="{ span: 14 }">
			<a-row :gutter="24">
				<a-col span="8">
					<a-form-item name="menuName" label="菜单名称">
						<a-input v-model:value="queryFormObj.data.menuName" allowClear></a-input>
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="24" class="autoee-button-row">
				<a-col :span="12" style="text-align: left">
					<a-button type="primary" @click="openMenuAddModal()" v-hasPermi="['system:menu:add']">
						<AutoeeIcon icon="plus-outlined" />新增
					</a-button>
					<a-button type="primary" danger @click="clickDeleteMenu()"
						:disabled="tableObj.selectedRowKeys.length <= 0" v-hasPermi="['system:menu:remove']">
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
		<!-- 单选列表 type: 'radio' -->
		<a-table rowKey="menuId" :columns="tableObj.columns" :data-source="tableObj.dataSource"
			:loading="tableObj.loading" :pagination="tablePagination" @change="tableHandChangePage"
			:row-selection="{ selectedRowKeys: tableObj.selectedRowKeys, onChange: onTableSelectChange, type: 'radio'}"
			bordered size="small" tableLayout="fixed">
			<template #bodyCell="{ index, column, record }">
				<template v-if="column.key === 'pageIndex'">
					{{ index + 1 }}
				</template>
				<template v-else-if="column.key === 'visible'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.visible, pageData.sysShowHideDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'menuType'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.menuType, pageData.menuTypeDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'menuName'">
					<span>
						<AutoeeIcon :icon="record.icon" style="color: blue; padding-right: 10px" />{{ record.menuName }}
					</span>
				</template>
				<template v-else-if="column.key === 'status'">
					<span>
						{{ AutoeeUtil.getDictLabelByKey(record.status, pageData.sysNormalDisableDict) }}
					</span>
				</template>
				<template v-else-if="column.key === 'menuTableOperateCol'">
					<span>
						<a @click="openMenuDetailModal(record)"> 查看 </a>
						<a-divider type="vertical" v-hasPermi="['system:menu:edit']" />
						<a @click="openMenuUpdateModal(record)" v-hasPermi="['system:menu:edit']"> 修改 </a>
					</span>
				</template>
			</template>
		</a-table>

		<MenuAddUpdateModal ref="menuAddUpdateModalRef" :sysYesNoDict="pageData.sysYesNoDict"
			:sysShowHideDict="pageData.sysShowHideDict" :menuTypeDict="pageData.menuTypeDict"
			:sysNormalDisableDict="pageData.sysNormalDisableDict"
			@refreshFatherPageTable="queryTableData"/>
		<MenuDetailModal ref="menuDetailModalRef" :sysYesNoDict="pageData.sysYesNoDict"
			:sysShowHideDict="pageData.sysShowHideDict" :menuTypeDict="pageData.menuTypeDict"
			:sysNormalDisableDict="pageData.sysNormalDisableDict"
			@refreshFatherPageTable="queryTableData"/>
	</div>
</template>

<script setup name="Menu">
	import {
		listMenu,
		getMenu,
		delMenu,
		addMenu,
		updateMenu,
		treeselect
	} from '@/api/system/menu';
	import MenuAddUpdateModal from './addUpdateModal.vue';
	import MenuDetailModal from './detailModal.vue';
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
		sysYesNoDict: [],
		sysShowHideDict: [],
		sysNormalDisableDict: [],
		menuTypeDict: [],
	});

	// AutoeeUtil.getDictsByType('sys_yes_no').then((res) => {
	//   pageData.sysYesNoDict = res;
	// });
	pageData.sysYesNoDict = [{
			value: '0',
			label: '是'
		},
		{
			value: '1',
			label: '否'
		},
	];
	AutoeeUtil.getDictsByType('sys_show_hide').then((res) => {
		pageData.sysShowHideDict = res;
	});
	AutoeeUtil.getDictsByType('sys_normal_disable').then((res) => {
		pageData.sysNormalDisableDict = res;
	});
	pageData.menuTypeDict = [{
			value: 'M',
			label: '目录'
		},
		{
			value: 'C',
			label: '菜单'
		},
		{
			value: 'F',
			label: '按钮'
		},
	];

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
	const menuAddUpdateModalRef = ref();
	const openMenuAddModal = () => {
		menuAddUpdateModalRef.value.openAddModal();
	};

	// 打开修改窗口
	const openMenuUpdateModal = (record) => {
		menuAddUpdateModalRef.value.openUpdateModal(record);
	};

	// 打开详细窗口
	const menuDetailModalRef = ref();
	const openMenuDetailModal = (record) => {
		menuDetailModalRef.value.openModal(record);
	};

	// 点击删除
	const clickDeleteMenu = () => {
		AutoeeUtil.confirmDeleteSelectedData(() => {
			delMenu(tableObj.selectedRowKeys).then((res) => {
				tableObj.selectedRowKeys = [];
				AutoeeUtil.messageSuccess('删除操作成功。');
				queryTableData();
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
			// {
			//   title: '菜单ID',
			//   key: 'menuId',
			//   dataIndex: 'menuId',
			// },
			{
				title: '菜单名称',
				key: 'menuName',
				dataIndex: 'menuName',
			},
			{
				title: '路由地址',
				key: 'path',
				dataIndex: 'path',
			},
			{
				title: '权限标识',
				key: 'perms',
				dataIndex: 'perms',
			},
			{
				title: '菜单类型',
				key: 'menuType',
				dataIndex: 'menuType',
			},
			{
				title: '是否显示',
				key: 'visible',
				dataIndex: 'visible',
			},
			{
				title: '菜单状态',
				key: 'status',
				dataIndex: 'status',
			},
			// {
			//   title: '菜单图标',
			//   key: 'icon',
			//   dataIndex: 'icon',
			// },
			{
				title: '操作',
				key: 'menuTableOperateCol',
				width: 120,
			},
		],
	});

	// 查询列表数据方法
	// 表格支持树形数据的展示，当数据中有 children 字段时会自动展示为树形表格，如果不需要或配置为其他字段可以用 childrenColumnName 进行配置。 可以通过设置 indentSize 以控制每一层的缩进宽度。
	const queryTableData = () => {
		// 转为树形结构：调用后端列表查询方法，通过返回结果设置tableObj.total，tableObj.dataSource，tableObj.loading
		AutoeeUtil.getTableTreeByQueryFunc(listMenu(queryFormObj.data), tableObj, "menuId");
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
