<template>
  <div style="width: 100%;">
    <a-row :gutter="24">
      <a-col :span="24">
        <ProTable
            ref="proTableRef"
            :api="tableApi"
            :columns="columns"
            :exportConfig="exportConfig"
            :initialSearchParams="initialSearchParams"
            :searchFields="searchFields"
            :expandable="{ expandedRowRender: expandedRowRender }"
            :rowSelection="{ type: 'checkbox', onSelect: onRowSelect }"
            rowKey="dictId"
        >
          <!-- 1. 自定义操作按钮 -->
          <template #actions="{ selectedRowKeys, delete: deleteRows }">
            <a-button v-hasPermi="['system:dict:add']" type="primary" @click="openTypeAddModal">
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
            <template v-else-if="column.key === 'typeTableOperateCol'">
              <TableActionBar
                  :hasDelete="false"
                  :hasEdit="true"
                  :hasView="true"
                  :record="record"
                  @edit="openTypeUpdateModal"
                  @view="openTypeDetailModal"
              />
            </template>
          </template>
        </ProTable>

        <!-- Modals -->
        <TypeAddUpdateModal ref="typeAddUpdateModalRef"
                            :sysNormalDisableDict="sys_normal_disable"
                            @refreshFatherTypeTableData="() => proTableRef.refresh()" />
        <TypeDetailModal ref="typeDetailModalRef"
                         :sysNormalDisableDict="sys_normal_disable"
                         @refreshFatherTypeTableData="() => proTableRef.refresh()" />
      </a-col>
    </a-row>
  </div>
</template>

<script setup name="Type">
import {computed, getCurrentInstance, h, reactive, ref} from 'vue';
import {listType, delType} from '@/api/system/dict/type';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import TypeAddUpdateModal from './addUpdateModal.vue';
import TypeDetailModal from './detailModal.vue';
import DictData from './DictData.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const {proxy} = getCurrentInstance();
const proTableRef = ref();

// 使用 useDict 获取字典
const {sys_normal_disable} = proxy.useDict("sys_normal_disable");

// 当前选中的字典类型（用于字典数据查询）
const selectedDictType = ref(null);

// --- ProTable 配置 ---
const tableApi = {list: listType, delete: delType};
const initialSearchParams = {dictType: null, dictName: null, status: null};
const exportConfig = {url: 'system/dict/type/export', fileName: '字典类型'};

const searchFields = computed(() => [
  {name: 'dictType', label: '字典类型', type: 'input'},
  {name: 'dictName', label: '字典类型名称', type: 'input'},
  {name: 'status', label: '字典类型状态', type: 'select', options: sys_normal_disable.value},
]);

const columns = [
  {title: '字典类型', dataIndex: 'dictType', key: 'dictType', width: 200},
  {title: '字典类型名称', dataIndex: 'dictName', key: 'dictName', width: 180},
  {title: '状态', dataIndex: 'status', key: 'status', width: 100},
  {title: '操作', key: 'typeTableOperateCol', width: 200, fixed: 'right'},
];

// 可展开行渲染函数
const expandedRowRender = (record) => {
  return h(DictData, {
    dictId: record.dictId,
    dictType: record.dictType,
  });
};

// 行选择回调（用于字典数据联动）
const onRowSelect = (record, selected) => {
  if (selected) {
    selectedDictType.value = record.dictType;
  } else {
    selectedDictType.value = null;
  }
};

// --- 页面特有逻辑 ---
const typeAddUpdateModalRef = ref();
const typeDetailModalRef = ref();

const openTypeAddModal = () => typeAddUpdateModalRef.value.openAddModal();
const openTypeUpdateModal = (record) => typeAddUpdateModalRef.value.openUpdateModal(record);
const openTypeDetailModal = (record) => typeDetailModalRef.value.openModal(record);


</script>
<style lang="less"></style>
