<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统监控', '定时任务']"></Breadcrumb>
    <a-card
        class="general-card r-nav-card"
        :body-style="{ padding: '15px' }"
        :header-style="{ padding: '15px' }"
    >
      <a-row>
        <!-- 搜索区块 -->
        <a-col :flex="1">
          <a-form
              :model="state.queryParams"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }"
              label-align="left"
              auto-label-width
          >
            <a-row :gutter="16">
              <a-col :span="8">
                <a-form-item field="jobName" :label="'任务名称'">
                  <a-input
                      v-model="state.queryParams.jobName"
                      :placeholder="'请输入任务名称'">
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="jobGroup" :label="'任务组名'">
                  <a-select v-model="state.queryParams.jobGroup" placeholder="请选择">
                    <a-option
                        v-for="dict in state.jobGroupOptions"
                        :key="dict.dictValue"
                        :value="dict.dictValue"
                        :label="dict.dictLabel"
                    >{{ dict.dictLabel }}
                    </a-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="status" :label="'任务状态'">
                  <a-select v-model="state.queryParams.status" placeholder="请选择">
                    <a-option
                        v-for="dict in state.jobStatusOptions"
                        :key="dict.dictValue"
                        :value="dict.dictValue"
                        :label="dict.dictLabel"
                    >{{ dict.dictLabel }}
                    </a-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 42px" direction="vertical"/>
        <a-col :flex="'86px'" style="text-align: right">
          <a-space :size="18">
            <a-button type="primary" @click="fetchJobList">
              <template #icon>
                <icon-search/>
              </template>
              查询
            </a-button>
            <a-button @click="resetQueryForm">
              <template #icon>
                <icon-refresh/>
              </template>
              重置
            </a-button>
          </a-space>
        </a-col>

        <a-divider style="margin-top: 0"/>
        <!-- 工具栏 -->
        <a-row style="margin-bottom: 16px">
          <!-- 前置工具栏部分 -->
          <a-col :span="12">
            <a-space>
              <a-button v-permission="['monitor:job:add']" type="primary" @click="handleAdd">
                <template #icon>
                  <icon-plus/>
                </template>
                新建
              </a-button>
              <a-button v-permission="['monitor:job:query']" @click="handleJobLog(0)">
                <template #icon>
                  <icon-file/>
                </template>
                日志
              </a-button>
            </a-space>
          </a-col>
        </a-row>
      </a-row>

      <!-- 表格数据-->
      <a-table
          ref="tableRef"
          row-key="jobId"
          :loading="loading"
          :pagination="pagination"
          :columns="state.columns"
          :data="state.jobList"
          :bordered="false"
          :size="'medium'"
          @page-change="onPageChange"
      >
        <template #jobGroup="{ record }">
          <dict-tag :options="state.jobGroupOptions" :value="record.jobGroup"/>
        </template>
        <template #status="{ record }">
          <a-switch
              :model-value="record.status" checked-value="0" unchecked-value="1"
              @change="handleStatusChange(record)"
          />
        </template>
        <template #optional="{ record }">
          <a-space>
            <a-button-group>
              <a-button v-permission="['monitor:job:edit']" type="text" size="mini" @click="handleUpdate(record.jobId)">
                <template #icon>
                  <icon-edit/>
                </template>
                修改
              </a-button>
              <a-button v-permission="['monitor:job:remove']" type="text" size="mini" @click="handleDelete(record)">
                <template #icon>
                  <icon-delete/>
                </template>
                删除
              </a-button>
              <a-dropdown trigger="hover">
                <a-button v-permission="['monitor:job:changeStatus', 'monitor:job:query']" type="text" size="mini">
                  <template #icon>
                    <icon-double-right/>
                  </template>
                  更多
                </a-button>
                <template #content>
                  <a-doption>
                    <a-button
                        v-permission="['monitor:job:changeStatus']"
                        type="text" size="mini"
                        @click="handleRun(record)">
                      <template #icon>
                        <icon-play-arrow/>
                      </template>
                      执行一次
                    </a-button>
                  </a-doption>
                  <a-doption>
                    <a-button
                        v-permission="['monitor:job:query']"
                        type="text" size="mini"
                        @click="handleView(record)">
                      <template #icon>
                        <icon-eye/>
                      </template>
                      任务详情
                    </a-button>
                  </a-doption>
                  <a-doption>
                    <a-button
                        v-permission="['monitor:job:query']"
                        type="text" size="mini"
                        @click="handleJobLog(record.jobId)">
                      <template #icon>
                        <icon-file/>
                      </template>
                      调度日志
                    </a-button>
                  </a-doption>
                </template>
              </a-dropdown>
            </a-button-group>
          </a-space>
        </template>
      </a-table>
    </a-card>
    <edit-job
        v-model:id="state.currentId"
        v-model:visible="state.editVisible"
        @refresh-data-list="fetchJobList">
    </edit-job>
    <view-job
        v-model:id="state.currentId"
        v-model:visible="state.viewVisible">
    </view-job>
  </div>
</template>

<script lang="ts" setup>

import useLoading from "@/hooks/loading";
import {Pagination} from "@/types/global";
import {computed, reactive} from "vue";
import {DictDataRecord, getDicts} from "@/api/system/dict-data";
import {changeJobStatus, delJob, JobRecord, listJob, runJob} from "@/api/monitor/job";
import {Message, Modal} from "@arco-design/web-vue";
import {useRouter} from "vue-router";
import {TableColumnData} from "@arco-design/web-vue/es/table/interface";
import DictTag from "@/components/dict-tag/index.vue";
import EditJob from "./components/edit-job.vue";
import ViewJob from "./components/view-job.vue";

const basePagination: Pagination = {
  current: 1,
  pageSize: 10,
};
const pagination = reactive({
  ...basePagination,
});
const state = reactive({
  // 列表显示列
  columns: computed<TableColumnData[]>(() =>[
    {
      title: '任务编号',
      dataIndex: 'jobId',
      slotName: 'jobId',
      width: 80,
    },
    {
      title: '任务名称',
      dataIndex: 'jobName',
      slotName: 'jobName',
      ellipsis: true,
      tooltip: {position: 'right'},
      width: 200,
    },
    {
      title: '任务组名',
      dataIndex: 'jobGroup',
      slotName: 'jobGroup',
      width: 100,
    },
    {
      title: '调用目标字符串',
      dataIndex: 'invokeTarget',
      slotName: 'invokeTarget',
      ellipsis: true,
      tooltip: {position: 'right'},
      width: 300,
    },
    {
      title: 'cron执行表达式',
      dataIndex: 'cronExpression',
      slotName: 'cronExpression',
      ellipsis: true,
      tooltip: {position: 'right'},
      width: 150,
    },
    {
      title: '状态',
      dataIndex: 'status',
      slotName: 'status',
      width: 100,
    },
    {
      title: '操作',
      dataIndex: 'optional',
      slotName: 'optional',
      fixed: 'right',
      width: 200,
    },
  ]),
  // 查询参数
  queryParams: {
    jobName: undefined,
    jobGroup: undefined,
    status: undefined,
  },
  // 定时任务表格数据
  jobList: [],
  // 选中行ID
  currentId: 0,
  // 显示编辑页面
  editVisible: false,
  viewVisible: false,
  // 字典
  jobGroupOptions: [] as DictDataRecord[],
  jobStatusOptions: [] as DictDataRecord[],
});

// 初始化字典
const initData = async () => {
  const {data: jobGroup} = await getDicts('sys_job_group');
  state.jobGroupOptions = jobGroup;
  const {data: jobStatus} = await getDicts('sys_job_status');
  state.jobStatusOptions = jobStatus;
};
initData();

// 重置查询表单
const resetQueryForm = () => {
  state.queryParams = {
    jobName: undefined,
    jobGroup: undefined,
    status: undefined,
  };
  state.currentId = 0;
};

const {loading, setLoading} = useLoading(true);
// 获取列表数据
const fetchJobList = async () => {
  setLoading(true);
  const {rows, total} = await listJob({
    ...pagination,
    ...state.queryParams,
  })
  state.jobList = rows;
  pagination.total = total;
  setLoading(false);
};
fetchJobList();

// 分页
const onPageChange = (current: number) => {
  pagination.current = current;
  fetchJobList();
};

// 打开新增
const handleAdd = () => {
  state.currentId = 0;
  state.editVisible = true;
};

// 打开编辑
const handleUpdate = (jobId: number) => {
  state.currentId = jobId;
  state.editVisible = true;
};

// 删除
const handleDelete = (row: JobRecord) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认删除定时任务编号为"${row.jobId}"的数据项？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      delJob(row.jobId as number).then(() => {
        Message.success("删除成功");
        fetchJobList();
      });
    },
  });
};

// 启用禁用
const handleStatusChange = (record: JobRecord) => {
  const text = record.status === '0' ? '停用' : '启用';
  const value = record.status === '0' ? '1' : '0';
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `确认要"${text}""${record.jobName}"任务吗？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      changeJobStatus(record.jobId as number, value).then(() => {
        Message.success(`${text}成功`);
        fetchJobList();
      }).catch(error => {
        record.status = record.status === '0' ? '1' : '0';
      });
    },
  })
};

// 立即执行一次
const handleRun = (record: JobRecord) => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `确认要立即执行一次"${record.jobName}"任务吗？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      runJob(record.jobId as number, record.jobGroup as string).then(() => {
        Message.success("执行成功");
      });
    },
  });
};

// 任务详细信息
const handleView = (record: JobRecord) => {
  state.currentId = record.jobId as number;
  state.viewVisible = true;
};

const router = useRouter();
// 任务日志列表查询
const handleJobLog = (jobId: number) => {
  router.push({name: 'JobLog', params: {jobId},});
};

</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
