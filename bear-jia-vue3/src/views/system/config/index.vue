<template>
  <div>
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :exportConfig="exportConfig"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        rowKey="configId"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, delete: deleteRows }">
        <a-button v-hasPermi="['system:config:add']" type="primary" @click="openAddModal">
          <BearJiaIcon icon="plus-outlined"/>新增
        </a-button>
        <a-button v-hasPermi="['system:config:remove']" :disabled="selectedRowKeys.length <= 0" danger type="primary"
                  @click="() => deleteRows()">
          <BearJiaIcon icon="delete-outlined"/>删除
        </a-button>
        <a-button v-hasPermi="['system:config:export']" @click="() => proTableRef.export()">
          <BearJiaIcon icon="export-outlined"/>导出
        </a-button>
        <!-- 保留原有的搜索按钮 -->
        <a-button :icon="h(SearchOutlined)" href="https://www.google.com" />
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'configType'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_yes_no" :value="record.configType"/>
        </template>
        <template v-else-if="column.key === 'configTableOperateCol'">
          <TableActionBar
              :hasDelete="false"
              :hasEdit="true"
              :hasView="true"
              :record="record"
              @edit="openUpdateModal"
              @view="openDetailModal"
          />
        </template>
      </template>
    </ProTable>

    <!-- Modals -->
    <AddUpdateModal ref="addUpdateModalRef" :sysYesNoDict="sys_yes_no" @refreshFatherPageTable="() => proTableRef.refresh()"/>
    <DetailModal ref="detailModalRef" :sysYesNoDict="sys_yes_no"/>
  </div>
</template>

<script setup name="Config">
import {computed, getCurrentInstance, ref} from 'vue';
import {h} from 'vue';
import {SearchOutlined} from '@ant-design/icons-vue';
import {listConfig, delConfig} from "@/api/system/config";

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import AddUpdateModal from './addUpdateModal.vue';
import DetailModal from './detailModal.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const {proxy} = getCurrentInstance();
const proTableRef = ref();

// 使用 useDict 获取字典
const {sys_yes_no} = proxy.useDict("sys_yes_no");

// --- ProTable 配置 ---
const tableApi = {list: listConfig, delete: delConfig};
const initialSearchParams = {configName: null, configKey: null, configValue: null, configType: null};
const exportConfig = {url: 'system/config/export', fileName: '参数配置'};

const searchFields = computed(() => [
  {name: 'configName', label: '参数名称', type: 'input'},
  {name: 'configKey', label: '参数键名', type: 'input'},
  {name: 'configValue', label: '参数键值', type: 'input'},
  {name: 'configType', label: '系统内置', type: 'select', options: sys_yes_no.value},
]);

const columns = [
  {title: '参数名称', dataIndex: 'configName', key: 'configName', width: 180},
  {title: '参数键名', dataIndex: 'configKey', key: 'configKey', width: 200},
  {title: '参数键值', dataIndex: 'configValue', key: 'configValue', width: 200},
  {title: '系统内置', dataIndex: 'configType', key: 'configType', width: 100},
  {title: '操作', key: 'configTableOperateCol', width: 200, fixed: 'right'},
];

// --- 页面特有逻辑 ---
const addUpdateModalRef = ref();
const detailModalRef = ref();
const openAddModal = () => addUpdateModalRef.value.openAddModal();
const openUpdateModal = (record) => addUpdateModalRef.value.openUpdateModal(record);
const openDetailModal = (record) => detailModalRef.value.openModal(record);
</script>
<style lang="less"></style>
