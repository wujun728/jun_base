<template>
  <div style="padding: 16px;">
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :exportConfig="exportConfig"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        rowKey="dictCode"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, delete: deleteRows }">
        <a-button v-hasPermi="['system:dict:add']" type="primary" @click="openDataAddModal">
          <BearJiaIcon icon="plus-outlined"/>新增
        </a-button>
        <a-button v-hasPermi="['system:dict:remove']" :disabled="selectedRowKeys.length <= 0" danger type="primary"
                  @click="() => deleteRows()">
          <BearJiaIcon icon="delete-outlined"/>删除
        </a-button>
        <a-button v-hasPermi="['system:dict:export']" @click="() => proTableRef.export()">
          <BearJiaIcon icon="export-outlined"/>导出
        </a-button>
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_normal_disable" :value="record.status"/>
        </template>
        <template v-else-if="column.key === 'dataTableOperateCol'">
          <TableActionBar
              :hasDelete="false"
              :hasEdit="true"
              :hasView="true"
              :record="record"
              @edit="openDataUpdateModal"
              @view="openDataDetailModal"
          />
        </template>
      </template>
    </ProTable>

    <!-- Modals -->
    <DataAddUpdateModal
        ref="dataAddUpdateModalRef"
        :sys-yes-no-dict="sysYesNoDict"
        :sys-normal-disable-dict="sys_normal_disable"
        @refresh-father-data-table-data="() => proTableRef.refresh()"
    />
    <DataDetailModal
        ref="dataDetailModalRef"
        :sys-yes-no-dict="sysYesNoDict"
        :sys-normal-disable-dict="sys_normal_disable"
        @refresh-father-data-table-data="() => proTableRef.refresh()"
    />
  </div>
</template>

<script setup>
import { computed, getCurrentInstance, reactive, ref } from 'vue';
import { listData, delData } from '@/api/system/dict/data';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import DataAddUpdateModal from './dataAddUpdateModal.vue';
import DataDetailModal from './dataDetailModal.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import { BearJiaIcon } from '@/utils/BearJiaIcon.js';

const { proxy } = getCurrentInstance();
const proTableRef = ref();

// 使用 useDict 获取字典
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

// 定义 Props
const props = defineProps({
  dictType: {
    type: String,
    required: true,
  },
  dictId: {
    type: Number,
    required: true,
  },
});

// 自定义字典数据
const sysYesNoDict = reactive([
  { value: 'Y', label: '是' },
  { value: 'N', label: '否' },
]);

// --- ProTable 配置 ---
const tableApi = {
  list: (params) => listData({ ...params, dictType: props.dictType }),
  delete: delData
};
const initialSearchParams = reactive({
  dictValue: null,
  dictLabel: null,
  status: null,
  dictType: props.dictType,
});
const exportConfig = { url: 'system/dict/data/export', fileName: '字典数据' };

const searchFields = computed(() => [
  { name: 'dictValue', label: '字典值', type: 'input' },
  { name: 'dictLabel', label: '字典名称', type: 'input' },
  { name: 'status', label: '状态', type: 'select', options: sys_normal_disable.value },
]);

const columns = [
  { title: '字典编码', dataIndex: 'dictCode', key: 'dictCode', width: 100 },
  { title: '字典标签', dataIndex: 'dictLabel', key: 'dictLabel', width: 120 },
  { title: '字典键值', dataIndex: 'dictValue', key: 'dictValue', width: 120 },
  { title: '字典排序', dataIndex: 'dictSort', key: 'dictSort', width: 100 },
  { title: '状态', dataIndex: 'status', key: 'status', width: 80 },
  { title: '备注', dataIndex: 'remark', key: 'remark', width: 150 },
  { title: '创建时间', dataIndex: 'createTime', key: 'createTime', width: 180 },
  { title: '操作', key: 'dataTableOperateCol', width: 200, fixed: 'right' },
];

// --- 页面特有逻辑 ---
const dataAddUpdateModalRef = ref();
const dataDetailModalRef = ref();

const openDataAddModal = () => dataAddUpdateModalRef.value.openAddModal();
const openDataUpdateModal = (record) => dataAddUpdateModalRef.value.openUpdateModal(record);
const openDataDetailModal = (record) => dataDetailModalRef.value.openModal(record);
</script>

<style lang="less" scoped>
.bearjia-button-row {
  margin-top: 16px;
}
</style>
