<template>
  <div>
    <ProTable
        ref="proTableRef"
        :api="tableApi"
        :columns="columns"
        :exportConfig="exportConfig"
        :initialSearchParams="initialSearchParams"
        :searchFields="searchFields"
        rowKey="jobId"
    >
      <!-- 1. 自定义操作按钮 -->
      <template #actions="{ selectedRowKeys, delete: deleteRows }">
        <a-button v-hasPermi="['monitor:job:add']" type="primary" @click="openJobAddModal">
          <BearJiaIcon icon="plus-outlined"/>新增
        </a-button>
        <a-button v-hasPermi="['monitor:job:remove']" :disabled="selectedRowKeys.length <= 0" danger type="primary"
                  @click="() => deleteRows()">
          <BearJiaIcon icon="delete-outlined"/>删除
        </a-button>
        <a-button v-hasPermi="['monitor:job:export']" @click="() => proTableRef.export()">
          <BearJiaIcon icon="export-outlined"/>导出
        </a-button>
      </template>

      <!-- 2. 自定义列渲染 -->
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'jobGroup'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="jobGroupDict" :value="record.jobGroup"/>
        </template>
        <template v-else-if="column.key === 'misfirePolicy'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="misfirePolicyDict" :value="record.misfirePolicy"/>
        </template>
        <template v-else-if="column.key === 'concurrent'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sysYesNoDict" :value="record.concurrent"/>
        </template>
        <template v-else-if="column.key === 'status'">
          <!-- 使用字典标签组件 -->
          <dict-tag :options="sysJobStatusDict" :value="record.status"/>
        </template>
        <template v-else-if="column.key === 'jobTableOperateCol'">
          <TableActionBar
              :hasDelete="false"
              :hasEdit="true"
              :hasView="true"
              :record="record"
              @edit="openJobUpdateModal"
              @view="openJobDetailModal"
          />
        </template>
      </template>
    </ProTable>

    <!-- Modals -->
    <JobAddUpdateModal ref="jobAddUpdateModalRef"
                       :jobGroupDict="jobGroupDict"
                       :misfirePolicyDict="misfirePolicyDict"
                       :sysYesNoDict="sysYesNoDict"
                       :sysJobStatusDict="sysJobStatusDict"
                       @refreshFatherPageTable="() => proTableRef.refresh()"/>
    <JobDetailModal ref="jobDetailModalRef"
                    :jobGroupDict="jobGroupDict"
                    :misfirePolicyDict="misfirePolicyDict"
                    :sysYesNoDict="sysYesNoDict"
                    :sysJobStatusDict="sysJobStatusDict" />
  </div>
</template>

<script setup name="Job">
import {computed, getCurrentInstance, reactive, ref} from 'vue';
import {listJob, delJob} from '@/api/monitor/job';
import BearJiaUtil from '@/utils/BearJiaUtil.js';

// 引入组件
import ProTable from '@/components/BearJiaProTable/index.vue';
import JobAddUpdateModal from './addUpdateModal.vue';
import JobDetailModal from './detailModal.vue';
import TableActionBar from '@/components/TableActionBar/index.vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';

const {proxy} = getCurrentInstance();
const proTableRef = ref();

// 使用 useDict 获取字典
const {sys_job_group, sys_job_status} = proxy.useDict("sys_job_group", "sys_job_status");

// 自定义字典数据
const misfirePolicyDict = reactive([
  {value: '1', label: '立即执行'},
  {value: '2', label: '执行一次'},
  {value: '3', label: '放弃执行'},
]);

const sysYesNoDict = reactive([
  {value: '0', label: '允许'},
  {value: '1', label: '禁止'},
]);

// 为了保持兼容性，创建别名
const jobGroupDict = sys_job_group;
const sysJobStatusDict = sys_job_status;

// --- ProTable 配置 ---
const tableApi = {list: listJob, delete: delJob};
const initialSearchParams = {jobName: null, jobGroup: null, status: null};
const exportConfig = {url: 'monitor/job/export', fileName: '定时任务'};

const searchFields = computed(() => [
  {name: 'jobName', label: '任务名称', type: 'input'},
  {name: 'jobGroup', label: '任务组名', type: 'select', options: jobGroupDict.value},
  {name: 'status', label: '状态', type: 'select', options: sysJobStatusDict.value},
]);

const columns = [
  {title: '任务ID', dataIndex: 'jobId', key: 'jobId', width: 80},
  {title: '任务名称', dataIndex: 'jobName', key: 'jobName', width: 150},
  {title: '任务组名', dataIndex: 'jobGroup', key: 'jobGroup', width: 120},
  {title: '调用目标字符串', dataIndex: 'invokeTarget', key: 'invokeTarget', width: 200},
  {title: 'cron执行表达式', dataIndex: 'cronExpression', key: 'cronExpression', width: 150},
  {title: '计划执行错误策略', dataIndex: 'misfirePolicy', key: 'misfirePolicy', width: 140},
  {title: '是否并发执行', dataIndex: 'concurrent', key: 'concurrent', width: 120},
  {title: '状态', dataIndex: 'status', key: 'status', width: 80},
  {title: '操作', key: 'jobTableOperateCol', width: 200, fixed: 'right'},
];

// --- 页面特有逻辑 ---
const jobAddUpdateModalRef = ref();
const jobDetailModalRef = ref();

const openJobAddModal = () => jobAddUpdateModalRef.value.openAddModal();
const openJobUpdateModal = (record) => jobAddUpdateModalRef.value.openUpdateModal(record);
const openJobDetailModal = (record) => jobDetailModalRef.value.openModal(record);
</script>
<style lang="less"></style>
