<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统监控', '定时任务', '调度日志']"></Breadcrumb>
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
                <a-form-item field="status" :label="'执行状态'">
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
              <a-col :span="8">
                <a-form-item field="dateRange" :label="'执行时间'">
                  <a-range-picker v-model="state.dateRange" style="width: 100%;"></a-range-picker>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 84px" direction="vertical"/>
        <a-col :flex="'86px'" style="text-align: right">
          <a-space direction="vertical" :size="18">
            <a-button type="primary" @click="fetchJobLogList">
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
              <a-space>
                <a-button status="danger" :disabled="state.selectedKeys.length <= 0" @click="handleDelete">
                  <template #icon>
                    <icon-delete/>
                  </template>
                  删除
                </a-button>
                <a-button status="danger" @click="handleClean">
                  <template #icon>
                    <icon-delete/>
                  </template>
                  清空
                </a-button>
                <a-button status="warning" @click="handleExport">
                  <template #icon>
                    <icon-download/>
                  </template>
                  导出
                </a-button>
                <a-button size="small" @click="handleClose">
                  <template #icon>
                    <icon-close/>
                  </template>
                  关闭
                </a-button>
              </a-space>
            </a-space>
          </a-col>
        </a-row>
      </a-row>

      <!-- 表格数据-->
      <a-table
          ref="tableRef"
          v-model:selectedKeys="state.selectedKeys"
          row-key="jobLogId"
          :loading="loading"
          :pagination="pagination"
          :columns="state.columns"
          :data="state.operlogList"
          :row-selection="state.rowSelection"
          :bordered="false"
          :size="'medium'"
          @sorter-change="handleSorterChange"
          @page-change="onPageChange"
      >
        <template #jobGroup="{ record }">
          <dict-tag :options="state.jobGroupOptions" :value="record.jobGroup"/>
        </template>
        <template #status="{ record }">
          <dict-tag :options="state.jobStatusOptions" :value="record.status"/>
        </template>
        <template #optional="{ record }">
          <a-space>
            <a-button-group>
              <a-button v-permission="['admin']" type="text" size="mini" @click="handleView(record)">
                <template #icon>
                  <icon-eye/>
                </template>
                详细
              </a-button>
            </a-button-group>
          </a-space>
        </template>
      </a-table>
    </a-card>
    <view-job-log
        v-model:job-log="state.currentRow"
        v-model:visible="state.editVisible">
    </view-job-log>
  </div>
</template>

<script lang="ts" setup>
// 定义页面加载中效果变量
import useLoading from "@/hooks/loading";
import {computed, reactive} from "vue";
import {DictDataRecord, getDicts} from "@/api/system/dict-data";
import {Message, Modal} from "@arco-design/web-vue";
import {Pagination} from "@/types/global";
import addDateRange from "@/utils/common";
import {cleanJobLog, delJobLog, listJobLog} from "@/api/monitor/job-log";
import {download} from "@/api/download";
import {useRouter} from "vue-router";
import {getJob} from "@/api/monitor/job";
import {useTabBarStore} from "@/store";
import {TagProps} from "@/store/modules/tab-bar/types";
import {TableColumnData} from "@arco-design/web-vue/es/table/interface";
import DictTag from "@/components/dict-tag/index.vue";
import ViewJobLog from "./view-job-log.vue";

const basePagination: Pagination = {
  current: 1,
  pageSize: 10,
};
const pagination = reactive({
  ...basePagination,
});

const state = reactive({
  // 列表显示列
  columns: computed<TableColumnData[]>(() => [
    {
      title: '日志编号',
      dataIndex: 'jobLogId',
      slotName: 'jobLogId',
      width: 80,
    },
    {
      title: '任务名称',
      dataIndex: 'jobName',
      slotName: 'jobName',
      ellipsis: true,
      tooltip: {position: 'right'},
      width: 150,
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
      title: '日志信息',
      dataIndex: 'jobMessage',
      slotName: 'jobMessage',
      ellipsis: true,
      tooltip: {position: 'right'},
      width: 200,
    },
    {
      title: '操作状态',
      dataIndex: 'status',
      slotName: 'status',
      width: 100,
    },
    {
      title: '执行时间',
      dataIndex: 'createTime',
      slotName: 'createTime',
      sortable: {
        sortDirections: ['ascend', 'descend']
      },
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'optional',
      slotName: 'optional',
      fixed: 'right',
      width: 100,
    },
  ]),
  queryParams: {
    orderByColumn: '',
    isAsc: '',
    jobName: undefined,
    jobGroup: undefined,
    status: undefined
  },
  rowSelection: {
    type: 'checkbox',
    showCheckedAll: true,
    onlyCurrent: false,
  } as unknown,
  // 表格选中值
  selectedKeys: [],
  // 时间选择
  dateRange: [],
  // 列表数据
  operlogList: [],
  // 字典
  jobGroupOptions: [] as DictDataRecord[],
  jobStatusOptions: [] as DictDataRecord[],
  // 选中行ID
  currentId: 0,
  currentRow: undefined,
  // 显示编辑页面
  editVisible: false,
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
    orderByColumn: '',
    isAsc: '',
    jobName: undefined,
    jobGroup: undefined,
    status: undefined
  };
  state.dateRange = [];
  state.currentRow = undefined;
};
const router = useRouter();
const {loading, setLoading} = useLoading(true);
// 获取列表数据
const fetchJobLogList = async () => {
  setLoading(true);
  const {jobId} = router.currentRoute.value.params;
  if (jobId && jobId !== '0') {
    const {data} = await getJob(jobId as string);
    state.queryParams.jobName = data.jobName;
    state.queryParams.jobGroup = data.jobGroup;
  }
  const {rows, total} = await listJobLog({
    ...pagination,
    ...addDateRange(state.queryParams, state.dateRange)
  })
  state.operlogList = rows;
  pagination.total = total;
  setLoading(false);
};
fetchJobLogList();

// 分页
const onPageChange = (current: number) => {
  pagination.current = current;
  fetchJobLogList();
};

// 排序触发事件
const handleSorterChange = (dataIndex: string, direction: string) => {
  state.queryParams.orderByColumn = dataIndex;
  state.queryParams.isAsc = direction;
  fetchJobLogList();
}

// 打开详情
const handleView = (currentRow: any) => {
  state.editVisible = true;
  state.currentRow = currentRow;
};

// 删除
const handleDelete = () => {
  const ids = state.selectedKeys.join(',');
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: `是否确认删除调度日志编号为"${ids}"的数据项？`,
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      delJobLog(ids).then(() => {
        Message.success("删除成功");
        fetchJobLogList();
      });
    },
  });
};

// 清空按钮操作
const handleClean = () => {
  Modal.confirm({
    titleAlign: 'start',
    title: '系统提示',
    content: '是否确认清空所有调度日志数据项？',
    okText: '确定',
    cancelText: '取消',
    onOk: () => {
      cleanJobLog().then(() => {
        Message.success("清空成功");
        fetchJobLogList();
      });
    },
  });
};

// 导出按钮操作
const handleExport = () => {
  download('/monitor/jobLog/export', state.queryParams, `log_${new Date().getTime()}.xlsx`)
};


const tabBarStore = useTabBarStore();
// 关闭按钮
const handleClose = () => {
  const tabList = tabBarStore.getTabList;
  const tag = tabList.find((x) => x.name === router.currentRoute.value.name) as TagProps
  const idx = tabList.findIndex((x) => x.name === tag.name)
  tabBarStore.deleteTag(idx, tag);
  const latest = tabList[idx - 1]; // 获取队列的前一个tab
  router.push({name: latest.name});
};

</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}
</style>
