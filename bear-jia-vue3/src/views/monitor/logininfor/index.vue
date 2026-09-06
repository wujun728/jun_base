<template>
  <div>
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :exportConfig="exportConfig"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        rowKey="infoId"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, delete: deleteRows }">
        <a-button v-hasPermi="['monitor:logininfor:export']" @click="() => proTableRef.export()">
          <BearJiaIcon icon="export-outlined"/>导出
        </a-button>
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_common_status" :value="record.status"/>
        </template>
        <template v-else-if="column.key === 'logininforTableOperateCol'">
          <TableActionBar
              :hasDelete="false"
              :hasEdit="false"
              :hasView="true"
              :record="record"
              @view="openDetailModal"
          />
        </template>
      </template>
    </ProTable>

    <!-- Modals -->
    <DetailModal ref="detailModalRef" :sysCommonStatusDict="sys_common_status"/>
  </div>
</template>

<script name="Logininfor" setup>
import {computed, getCurrentInstance, ref} from 'vue';
import {delLogininfor, list} from "@/api/monitor/logininfor";

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import DetailModal from './detailModal.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const {proxy} = getCurrentInstance();
const proTableRef = ref();

// 使用 useDict 获取字典
const {sys_common_status} = proxy.useDict("sys_common_status");

// --- ProTable 配置 ---
const tableApi = {list: list, delete: delLogininfor};
const initialSearchParams = {
  userName: null,
  ipaddr: null,
  loginLocation: null,
  browser: null,
  os: null,
  status: null,
  msg: null,
  loginTimeRange: null,
};
const exportConfig = {url: 'monitor/logininfor/export', fileName: '登录日志'};

const searchFields = computed(() => [
  {name: 'userName', label: '用户账号', type: 'input'},
  {name: 'ipaddr', label: '登录IP地址', type: 'input'},
  {name: 'loginLocation', label: '登录地点', type: 'input'},
  {name: 'browser', label: '浏览器类型', type: 'input'},
  {name: 'os', label: '操作系统', type: 'input'},
  {name: 'status', label: '登录状态', type: 'select', options: sys_common_status.value},
  {name: 'msg', label: '提示消息', type: 'input'},
  {name: 'loginTimeRange', label: '访问时间', type: 'daterange'},
]);

const columns = [
  {title: '用户账号', dataIndex: 'userName', key: 'userName', width: 120},
  {title: '登录IP地址', dataIndex: 'ipaddr', key: 'ipaddr', width: 140},
  {title: '登录地点', dataIndex: 'loginLocation', key: 'loginLocation', width: 150},
  {title: '浏览器类型', dataIndex: 'browser', key: 'browser', width: 120},
  {title: '操作系统', dataIndex: 'os', key: 'os', width: 150},
  {title: '登录状态', dataIndex: 'status', key: 'status', width: 100},
  {title: '提示消息', dataIndex: 'msg', key: 'msg', width: 150},
  {title: '访问时间', dataIndex: 'loginTime', key: 'loginTime', width: 180},
  {title: '操作', key: 'logininforTableOperateCol', width: 120, fixed: 'right'},
];

// --- 页面特有逻辑 ---
const detailModalRef = ref();
const openDetailModal = (record) => detailModalRef.value.openModal(record);
</script>
<style lang="less"></style>
