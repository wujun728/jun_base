<template>
	<div>
		<a-modal v-model:visible="pageDataObj.visible" width="95%" style="top: 10px; height: 95%" title="修改配置信息"
			:destroyOnClose="true" :maskClosable="false" @ok="clickModalOk" @cancel="handleModalCancel">
			<a-tabs size="default" hide-add>
				<a-tab-pane key="panekey1" tab="【1】生成信息">
					<a-form ref="updateFormRef" name="updateFormObj" :model="updateFormObj.data" :labelCol="{ span: 8 }"
						:wrapperCol="{ span: 14 }">
						<a-row :gutter="24">
							<a-col span="12">
								<a-form-item name="tableName" label="表名"
									:rules="[{ required: true, message: '表名不能为空！' }]">
									<a-input v-model:value="updateFormObj.data.tableName" placeholder="请填写表名"
										:disabled="true"></a-input>
								</a-form-item>
							</a-col>
							<a-col span="12">
								<a-form-item name="tableComment" label="表描述"
									:rules="[{ required: true, message: '表描述不能为空！' }]">
									<a-input v-model:value="updateFormObj.data.tableComment" placeholder="请填写表描述">
									</a-input>
								</a-form-item>
							</a-col>
							<a-col span="12">
								<a-form-item name="className" label="实体类名"
									:rules="[{ required: true, message: '实体类名不能为空！' }]">
									<a-input v-model:value="updateFormObj.data.className" placeholder="请填写实体类名">
									</a-input>
								</a-form-item>
							</a-col>
							<a-col span="12">
								<a-form-item name="functionAuthor" label="作者">
									<a-input v-model:value="updateFormObj.data.functionAuthor" placeholder="请填写作者">
									</a-input>
								</a-form-item>
							</a-col>
							<a-col span="12">
								<a-form-item name="remark" label="备注">
									<a-input v-model:value="updateFormObj.data.remark"></a-input>
								</a-form-item>
							</a-col>
							<a-col span="12">
								<a-form-item name="tplCategory" label="生成模板"
									:rules="[{ required: true, message: '生成模板不能为空！' }]">
									<a-select v-model:value="updateFormObj.data.tplCategory"
										:options="pageDataObj.tplCategoryDict" placeholder="请选择生成模板"> </a-select>
								</a-form-item>
							</a-col>
							<a-col span="12">
								<a-form-item name="packageName" label="生成包路径"
									:rules="[{ required: true, message: '生成包路径不能为空！' }]">
									<a-input v-model:value="updateFormObj.data.packageName" placeholder="请填写生成包路径">
									</a-input>
								</a-form-item>
							</a-col>
							<a-col span="12">
								<a-form-item name="moduleName" label="生成模块名"
									:rules="[{ required: true, message: '生成模块名不能为空！' }]">
									<a-input v-model:value="updateFormObj.data.moduleName" placeholder="请填写生成模块名">
									</a-input>
								</a-form-item>
							</a-col>
							<a-col span="12">
								<a-form-item name="businessName" label="生成业务名"
									:rules="[{ required: true, message: '生成业务名不能为空！' }]">
									<a-input v-model:value="updateFormObj.data.businessName" placeholder="请填写生成业务名">
									</a-input>
								</a-form-item>
							</a-col>
							<a-col span="12">
								<a-form-item name="functionName" label="生成功能名"
									:rules="[{ required: true, message: '生成功能名不能为空！' }]">
									<a-input v-model:value="updateFormObj.data.functionName" placeholder="请填写生成功能名">
									</a-input>
								</a-form-item>
							</a-col>

							<a-col span="12">
								<a-form-item name="parentMenuId" label="上级菜单">
									<a-tree-select v-model:value="updateFormObj.data.parentMenuId" placeholder="请选择上级菜单"
										:tree-data="pageDataObj.menusDict"
										:fieldNames="{ children: 'children', label: 'menuName', key: 'menuId', value: 'menuId' }">
									</a-tree-select>
								</a-form-item>
							</a-col>
							<a-col span="12">
								<a-form-item name="genType" label="生成代码方式"
									:rules="[{ required: true, message: '生成代码方式不能为空！' }]">
									<a-radio-group v-model:value="updateFormObj.data.genType" button-style="solid">
										<a-radio-button value="0">zip压缩包</a-radio-button>
										<a-radio-button value="1">自定义路径</a-radio-button>
									</a-radio-group>
								</a-form-item>
							</a-col>
							<a-col span="12" v-if="updateFormObj.data.genType == 1">
								<a-form-item name="genPath" label="自定义路径">
									<a-input v-model:value="updateFormObj.data.genPath" defaultValue="sss"></a-input>
								</a-form-item>
							</a-col>
						</a-row>
					</a-form>
				</a-tab-pane>
				<a-tab-pane key="panekey2" tab="【2】字段信息">
					<!-- :pagination="tablePagination" -->
					<!-- @change="tableHandChangePage" -->
					<!-- :loading="tablePage.loading.value" -->
					<a-table rowKey="userId" :pagination="false" :scroll="{ y: 450 }" @resizeColumn="handleResizeColumn"
						:columns="tableData.columns" :data-source="tableData.dataSource" bordered size="small"
						tableLayout="fixed">
						<template #bodyCell="{ column, record }">
							<template v-if="column.key === 'columnComment'">
								<a-input v-model:value="record.columnComment"></a-input>
							</template>
							<template v-else-if="column.key === 'javaType'">
								<a-select v-model:value="record.javaType" :options="pageDataObj.javaTypeDict"
									style="width: 100%"> </a-select>
							</template>
							<template v-else-if="column.key === 'javaField'">
								<a-input v-model:value="record.javaField"></a-input>
							</template>
							<template v-else-if="column.key === 'isInsert'">
								<a-switch v-model:checked="record.isInsert" checkedValue="1" unCheckedValue="0"
									checked-children="是" un-checked-children="否" />
							</template>
							<template v-else-if="column.key === 'isEdit'">
								<a-switch v-model:checked="record.isEdit" checkedValue="1" unCheckedValue="0"
									checked-children="是" un-checked-children="否" />
							</template>
							<template v-else-if="column.key === 'isList'">
								<a-switch v-model:checked="record.isList" checkedValue="1" unCheckedValue="0"
									checked-children="是" un-checked-children="否" />
							</template>
							<template v-else-if="column.key === 'isQuery'">
								<a-switch v-model:checked="record.isQuery" checkedValue="1" unCheckedValue="0"
									checked-children="是" un-checked-children="否" />
							</template>
							<template v-else-if="column.key === 'queryType'">
								<a-select v-model:value="record.queryType" :options="pageDataObj.queryTypeDict"
									style="width: 100%"> </a-select>
							</template>
							<template v-else-if="column.key === 'isRequired'">
								<a-switch v-model:checked="record.isRequired" checkedValue="1" unCheckedValue="0"
									checked-children="是" un-checked-children="否" />
							</template>
							<template v-else-if="column.key === 'htmlType'">
								<a-select v-model:value="record.htmlType" :options="pageDataObj.htmlTypeDict"
									style="width: 100%"> </a-select>
							</template>
							<template v-else-if="column.key === 'dictType'">
								<a-select v-model:value="record.dictType" :options="pageDataObj.optionselectDict"
									style="width: 100%"> </a-select>
							</template>
							<template v-else-if="column.key === 'operateCol'">
								<span>
									<a @click="openUpdateModal(record)"> 预览 </a>
									<a-divider type="vertical" />
									<a @click="openUpdateModal(record)"> 修改 </a>
									<a-divider type="vertical" />
									<a @click="openUpdateModal(record)"> 同步 </a>
									<a-divider type="vertical" />
									<a @click="generateCode(record)"> 生成代码 </a>
								</span>
							</template>
						</template>
					</a-table>
				</a-tab-pane>
			</a-tabs>
		</a-modal>
	</div>
</template>

<script setup>
	import {
		reactive,
		ref
	} from 'vue';
	import AutoeeUtil from '@/utils/AutoeeUtil.js';
	import {
		getGenTable,
		updateGenTable
	} from '@/api/tool/gen';
	import {
		message
	} from 'ant-design-vue';
	import {
		optionselect as getDictOptionselect
	} from '@/api/system/dict/type';
	import {
		listMenu as getMenuTreeselect
	} from '@/api/system/menu';
	import {
		handleTree
	} from '@/utils/ruoyi.js';

	// 引入父页面方法
	const pageEmit = defineEmits(["refreshFatherPageTable"]);

	// 当前页面使用的数据
	const pageDataObj = reactive({
		visible: false,
		optionselectDict: [],
		javaTypeDict: [],
		queryTypeDict: [],
		htmlTypeDict: [],
		tplCategoryDict: [],
		menusDict: [],
		deptTreeData: [],
	});
	AutoeeUtil.getDeptTreeData().then((res) => {
		pageDataObj.deptTreeData = res.data;
	});

	// 基本信息Form
	const updateFormRef = ref();
	const updateFormObj = reactive({
		visible: false,
		data: {}
	});
	// 重置updateFormObj
	const resetUpdateFormObj = () => {
		updateFormObj.data = {
			visible: false,
			data: {}
		}
	};


	pageDataObj.javaTypeDict = [{
			label: 'Long',
			value: 'Long'
		},
		{
			label: 'String',
			value: 'String'
		},
		{
			label: 'Integer',
			value: 'Integer'
		},
		{
			label: 'Double',
			value: 'Double'
		},
		{
			label: 'BigDecimal',
			value: 'BigDecimal'
		},
		{
			label: 'Date',
			value: 'Date'
		},
		{
			label: 'Boolean',
			value: 'Boolean'
		},
	];
	pageDataObj.queryTypeDict = [{
			label: '=',
			value: 'EQ'
		},
		{
			label: '!=',
			value: 'NE'
		},
		{
			label: '>',
			value: 'GT'
		},
		{
			label: '>=',
			value: 'GE'
		},
		{
			label: '<',
			value: 'LT'
		},
		{
			label: '<=',
			value: 'LE'
		},
		{
			label: 'LIKE',
			value: 'LIKE'
		},
		{
			label: 'BETWEEN',
			value: 'BETWEEN'
		},
	];
	pageDataObj.htmlTypeDict = [{
			label: '文本框',
			value: 'input'
		},
		{
			label: '数字框',
			value: 'inputNumber'
		},
		{
			label: '文本域',
			value: 'textarea'
		},
		{
			label: '下拉框',
			value: 'select'
		},
		{
			label: '树形下拉框',
			value: 'treeSelect'
		},
		{
			label: '树形控件',
			value: 'tree'
		},
		{
			label: '单选框',
			value: 'radio'
		},
		{
			label: '复选框',
			value: 'checkbox'
		},
		{
			label: '日期控件',
			value: 'datetime'
		},
		{
			label: '图片上传',
			value: 'imageUpload'
		},
		{
			label: '文件上传',
			value: 'fileUpload'
		},
		{
			label: '富文本控件',
			value: 'editor'
		},
	];
	pageDataObj.tplCategoryDict = [{
			label: '单表（增删改查）',
			value: 'crud'
		},
		// { label: '树表（增删改查）', value: 'tree' },
	];
	// 查询所有代码表列表
	getDictOptionselect().then((response) => {
		for (let d of response.data) {
			if (d) {
				pageDataObj.optionselectDict.push({
					value: d.dictType,
					label: d.dictName
				});
			}
		}
	});
	// 查询菜单下拉列表
	getMenuTreeselect().then((response) => {
		pageDataObj.menusDict = handleTree(response.data, 'menuId');
	});

	// 打开修改窗口
	const openUpdateModal = (record) => {
		pageDataObj.visible = true;
		getGenTable(record.tableId).then((res) => {
			updateFormObj.data = res.data.info;
			// console.log("updateFormObj.data=" + JSON.stringify(updateFormObj.data))
			console.log("updateFormObj.data.genPath=" + JSON.stringify(updateFormObj.data.genPath))
			if(updateFormObj.data.genPath == "/"){
				updateFormObj.data.genPath = "C:\\Users\\Fuxs\\Desktop"
			}
			tableData.dataSource = res.data.rows;
		});
	};

	// 列表数据
	const tableData = reactive({
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
				title: '字段列名',
				dataIndex: 'columnName',
				key: 'columnName',
			},
			{
				title: '字段描述',
				dataIndex: 'columnComment',
				key: 'columnComment',
				resizable: true,
				width: 100,
			},
			{
				title: '物理类型',
				dataIndex: 'columnType',
				key: 'columnType',
			},
			{
				title: 'Java类型',
				dataIndex: 'javaType',
				key: 'javaType',
			},
			{
				title: 'java属性',
				dataIndex: 'javaField',
				key: 'javaField',
			},
			{
				title: '插入',
				dataIndex: 'isInsert',
				key: 'isInsert',
			},
			{
				title: '编辑',
				dataIndex: 'isEdit',
				key: 'isEdit',
			},
			{
				title: '列表',
				dataIndex: 'isList',
				key: 'isList',
			},
			{
				title: '查询',
				dataIndex: 'isQuery',
				key: 'isQuery',
			},
			{
				title: '查询方式',
				dataIndex: 'queryType',
				key: 'queryType',
			},
			{
				title: '必填',
				dataIndex: 'isRequired',
				key: 'isRequired',
			},
			{
				title: '显示类型',
				dataIndex: 'htmlType',
				key: 'htmlType',
				resizable: true,
				width: 100,
			},
			{
				title: '字典类型',
				dataIndex: 'dictType',
				key: 'dictType',
				resizable: true,
				width: 120,
			},
		],
	});

	// 列表选中方法
	const onTableSelectChange = (selectedRowKeys) => {
		tableData.selectedRowKeys = selectedRowKeys;
	};
	// 列表拖拽列宽方法
	const handleResizeColumn = (w, col) => {
		col.width = w;
	};
	
	// 点击窗口确认
	const clickModalOk = (e) => {
		updateFormRef.value
			.validateFields()
			.then((values) => {
				const genTable = Object.assign({}, updateFormObj.data);
				genTable.columns = tableData.dataSource;
				genTable.params = {
					// treeCode: genTable.treeCode,
					// treeName: genTable.treeName,
					// treeParentCode: genTable.treeParentCode,
					parentMenuId: updateFormObj.data.parentMenuId,
				};
				// console.log('genTable=' + JSON.stringify(genTable));
				updateGenTable(genTable).then((res) => {
					AutoeeUtil.messageInfo(res.msg);
					// 调用父页面刷新方法
					pageEmit("refreshFatherPageTable");

					if (res.code === 200) {
						pageDataObj.visible = false;
						resetUpdateFormObj();
					}
				});
			})
			.catch((info) => {
				AutoeeUtil.messageWarn('请确认【1】基本信息标签页中的内容已填写完整！' + info);
			});
	};

	// 点击窗口取消
	const handleModalCancel = () => {
		resetUpdateFormObj();
	};

	// 对外暴露出去
	defineExpose({
		openUpdateModal,
	});
</script>

<style lang="less"></style>
