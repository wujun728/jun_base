<template>
  <div>
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        :isTreeTable="true"
        :rowSelection="{ type: 'radio' }"
        rowKey="deptId"
        :default-expand-all-rows="false"
        @expand="handleExpand"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, delete: deleteRows }">
        <a-button v-hasPermi="['system:dept:add']" type="primary" @click="openDeptAddModal">
          <BearJiaIcon icon="plus-outlined"/>新增
        </a-button>
        <a-button v-hasPermi="['system:dept:remove']" :disabled="selectedRowKeys.length <= 0" danger type="primary"
                  @click="() => deleteRows()">
          <BearJiaIcon icon="delete-outlined"/>删除
        </a-button>
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_normal_disable" :value="record.status"/>
        </template>
        <template v-else-if="column.key === 'deptTableOperateCol'">
          <TableActionBar
              :hasDelete="false"
              :hasEdit="true"
              :hasView="true"
              :record="record"
              @edit="openDeptUpdateModal"
              @view="openDeptDetailModal"
          />
        </template>
      </template>
    </ProTable>

    <!-- Modals -->
    <DeptAddUpdateModal ref="deptAddUpdateModalRef"
                        @querDeptTreeData="querDeptTreeData"
                        :statusDict="sys_normal_disable"
                        :deptTreeData="pageData.deptTreeData"
                        @refreshFatherPageTable="handleRefresh" />
    <DeptDetailModal ref="deptDetailModalRef"
                     :statusDict="sys_normal_disable"
                     :deptTreeData="pageData.deptTreeData" />
  </div>
</template>

<script setup name="Dept">
import {computed, getCurrentInstance, reactive, ref} from 'vue';
import {listDept, listDeptLazy, delDept} from '@/api/system/dept';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import DeptAddUpdateModal from './addUpdateModal.vue';
import DeptDetailModal from './detailModal.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const {proxy} = getCurrentInstance();
const proTableRef = ref();

// 使用 useDict 获取字典
const {sys_normal_disable} = proxy.useDict("sys_normal_disable");

// 当前页面使用的数据（保留部门树数据）
const pageData = reactive({
  deptTreeData: [],
});

const querDeptTreeData = () => {
  BearJiaUtil.getDeptTreeData().then((res) => {
    pageData.deptTreeData = res.data;
  });
};
querDeptTreeData();

// 是否处于搜索模式
const isSearchMode = ref(false);

// --- ProTable 配置 ---
const tableApi = {
  // 懒加载模式：初始只加载根节点
  list: async (params) => {
    // 如果有搜索条件，使用全量接口
    if (params?.deptName || params?.status) {
      isSearchMode.value = true;
      return listDept(params);
    }
    // 否则使用懒加载接口，只加载根节点
    isSearchMode.value = false;
    return listDeptLazy(0);
  },
  delete: delDept,
  // 特殊处理：根据模式决定数据处理方式
  processListData: (data) => {
    if (isSearchMode.value) {
      // 搜索模式：将扁平数据转换为树形结构
      return BearJiaUtil.handleTree(data, 'deptId', 'parentId', 'children');
    }
    // 懒加载模式：标记每个节点是否有子节点
    return data.map(item => ({
      ...item,
      children: item.hasChildren ? [] : undefined,
    }));
  }
};
const initialSearchParams = {deptName: null, status: null};

const searchFields = computed(() => [
  {name: 'deptName', label: '部门名称', type: 'input'},
  {name: 'status', label: '部门状态', type: 'select', options: sys_normal_disable.value},
]);

const columns = [
  {title: '部门名称', dataIndex: 'deptName', key: 'deptName', width: 200},
  {title: '显示顺序', dataIndex: 'orderNum', key: 'orderNum', width: 100},
  {title: '负责人', dataIndex: 'leader', key: 'leader', width: 120},
  {title: '部门状态', dataIndex: 'status', key: 'status', width: 100},
  {title: '操作', key: 'deptTableOperateCol', width: 200, fixed: 'right'},
];

// 展开节点时加载子节点（懒加载）
const handleExpand = async (expanded, record) => {
  // 搜索模式下不需要懒加载子节点
  if (isSearchMode.value) {
    return;
  }

  // 如果是折叠或者已经加载过子节点，不处理
  if (!expanded || (record.children && record.children.length > 0)) {
    return;
  }

  try {
    const res = await listDeptLazy(record.deptId);
    const children = res.data || res.rows || [];
    // 标记每个子节点是否有子节点
    record.children = children.map(item => ({
      ...item,
      children: item.hasChildren ? [] : undefined,
    }));
    // 触发 ProTable 更新
    if (proTableRef.value?.tableState?.dataSource) {
      proTableRef.value.tableState.dataSource = [...proTableRef.value.tableState.dataSource];
    }
  } catch (error) {
    console.error('加载部门子节点失败:', error);
  }
};

// 刷新表格
const handleRefresh = () => {
  proTableRef.value?.refresh();
};

// --- 页面特有逻辑 ---
const deptAddUpdateModalRef = ref();
const deptDetailModalRef = ref();

const openDeptAddModal = () => deptAddUpdateModalRef.value.openAddModal();
const openDeptUpdateModal = (record) => deptAddUpdateModalRef.value.openUpdateModal(record);
const openDeptDetailModal = (record) => deptDetailModalRef.value.openModal(record);
</script>

<style lang="less" scoped>
</style>
