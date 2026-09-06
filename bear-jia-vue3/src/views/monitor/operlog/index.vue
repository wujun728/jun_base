<template>
  <div>
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :exportConfig="exportConfig"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        rowKey="operId"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, delete: deleteRows }">
        <a-button v-hasPermi="['monitor:operlog:remove']" :disabled="selectedRowKeys.length <= 0" danger type="primary"
                  @click="() => deleteRows()">
          <BearJiaIcon icon="delete-outlined"/>删除
        </a-button>
        <a-button v-hasPermi="['monitor:operlog:remove']" danger type="primary" @click="clickCleanOperlog">
          <BearJiaIcon icon="delete-outlined"/>清空
        </a-button>
        <a-button v-hasPermi="['monitor:operlog:export']" @click="() => proTableRef.export()">
          <BearJiaIcon icon="export-outlined"/>导出
        </a-button>
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'businessType'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_oper_type" :value="record.businessType"/>
        </template>
        <template v-else-if="column.key === 'status'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sys_common_status" :value="record.status"/>
        </template>
        <template v-else-if="column.key === 'operlogTableOperateCol'">
          <TableActionBar
              :hasDelete="false"
              :hasEdit="false"
              :hasView="true"
              :record="record"
              @view="openOperlogDetailModal"
          />
        </template>
      </template>
    </ProTable>

    <!-- Modals -->
    <OperlogDetailModal ref="operlogDetailModalRef"
                        :sysCommonStatusDict="sys_common_status"
                        :sysOperTypeDict="sys_oper_type"/>
  </div>
</template>

<script name="Operlog" setup>
import {computed, getCurrentInstance, ref} from 'vue';
import {cleanOperlog, delOperlog, list} from '@/api/monitor/operlog';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import OperlogDetailModal from './detailModal.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const {proxy} = getCurrentInstance();
const proTableRef = ref();

// 使用 useDict 获取字典
const {sys_oper_type, sys_common_status} = proxy.useDict("sys_oper_type", "sys_common_status");


// --- ProTable 配置 ---
const tableApi = {list: list, delete: delOperlog};
const initialSearchParams = {
  title: null,
  businessType: null,
  operName: null,
  status: null,
  operTimeRange: null,
};
const exportConfig = {url: 'monitor/operlog/export', fileName: '操作日志'};

const searchFields = computed(() => [
  {name: 'title', label: '系统模块', type: 'input'},
  {name: 'businessType', label: '操作类型', type: 'select', options: sys_oper_type.value},
  {name: 'operName', label: '操作人员', type: 'input'},
  {name: 'status', label: '操作状态', type: 'select', options: sys_common_status.value},
  {name: 'operTimeRange', label: '操作时间', type: 'daterange'},
]);

const columns = [
  {title: '系统模块', dataIndex: 'title', key: 'title', width: 120},
  {title: '操作类型', dataIndex: 'businessType', key: 'businessType', width: 100},
  {title: '请求方式', dataIndex: 'requestMethod', key: 'requestMethod', width: 100},
  {title: '操作人员', dataIndex: 'operName', key: 'operName', width: 100},
  {title: '操作地址', dataIndex: 'operIp', key: 'operIp', width: 140},
  {title: '操作地点', dataIndex: 'operLocation', key: 'operLocation', width: 150},
  {title: '操作状态', dataIndex: 'status', key: 'status', width: 100},
  {title: '操作时间', dataIndex: 'operTime', key: 'operTime', width: 180},
  {title: '操作', key: 'operlogTableOperateCol', width: 120, fixed: 'right'},
];

// --- 页面特有逻辑 ---
const operlogDetailModalRef = ref();
const openOperlogDetailModal = (record) => operlogDetailModalRef.value.openModal(record);

// 清空所有日志
const clickCleanOperlog = () => {
  BearJiaUtil.confirmCleanAllData(() => {
    cleanOperlog().then((res) => {
      BearJiaUtil.messageSuccess('清空操作成功。');
      proTableRef.value?.refresh();
    });
  });
};
</script>
<style lang="less"></style>
