<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable ref="proTable" row-key="jobLogId" :columns="columns" :request-api="listJobLog" :request-auto="false">
        <template #tableHeader="scope">
          <el-button
            type="danger"
            :icon="Delete"
            plain
            v-hasPermi="['monitor:job:remove']"
            :disabled="!scope.isSelected"
            @click="batchDelete(scope.selectedListIds)"
          >
            批量删除
          </el-button>
          <el-button type="danger" plain icon="Delete" @click="handleClean" v-hasPermi="['monitor:job:remove']">清空</el-button>
          <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['monitor:job:export']">
            导出
          </el-button>
          <el-button type="warning" plain icon="Close" @click="tabRemove">关闭</el-button>
        </template>
        <template #operation="scope">
          <el-button type="primary" link :icon="View" v-hasPermi="['monitor:job:query']" @click="handleView(scope.row)">
            详细
          </el-button>
        </template>
      </ProTable>
    </div>
    <el-dialog title="调度日志详细" v-model="open" width="700px" append-to-body>
      <el-form :model="form" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="日志序号：">{{ form.jobLogId }}</el-form-item>
            <el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组：">{{ form.jobGroup }}</el-form-item>
            <el-form-item label="执行时间：">{{ form.createTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用方法：">{{ form.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="日志信息：">{{ form.jobMessage }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行状态：">
              <div v-if="form.status == 0">正常</div>
              <div v-else-if="form.status == 1">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="异常信息：" v-if="form.status == 1">{{ form.exceptionInfo }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="open = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="tsx" name="JobLog">
import { Delete, View } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { cleanJobLog, delJobLog, exportJobLogInfo, getJob, listJobLog } from "@/api/ruoyimodule/monitor/job";
import { ColumnProps } from "@/components/ProTable/interface";
import { JobLog } from "@/api/interface";
import { nextTick, onMounted, reactive, ref } from "vue";
import { getEnum } from "@/api/ruoyimodule";
import { useDict } from "@/hooks/useDict.js";
import DictTag from "@/components/DictTag/index.vue";
import { useHandleData } from "@/hooks/useHandleData";
import { ElMessage, ElMessageBox } from "element-plus";
import { useDownload } from "@/hooks/useDownload";
import { useRoute } from "vue-router";
import { useTabsStore } from "@/stores/modules/tabs";

const { sys_job_group, sys_common_status } = useDict("sys_job_group", "sys_common_status");
// ProTable 实例
const proTable = ref<any>();

const tabStore = useTabsStore();
const route = useRoute();
const columns = reactive<ColumnProps<JobLog.ResJobLogInfo>[]>([
  { type: "selection", fixed: "left", width: 70 },
  { prop: "jobLogId", label: "日志编号" },
  { prop: "jobName", label: "任务名称", search: { el: "input" } },
  {
    prop: "jobGroup",
    label: "任务组名",
    enum: getEnum("sys_job_group"),
    search: { el: "select", props: { filterable: true } } as any,
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return <DictTag options={sys_job_group.value} value={scope.row.jobGroup} />;
    }
  },
  { prop: "invokeTarget", label: "调用目标字符串" },
  { prop: "jobMessage", label: "日志信息" },
  {
    prop: "status",
    label: "执行状态",
    enum: getEnum("sys_common_status"),
    search: { el: "select", props: { filterable: true } } as any,
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return <DictTag options={sys_common_status.value} value={scope.row.status} />;
    }
  },
  {
    prop: "createTime",
    label: "执行时间",
    width: 150,
    search: {
      el: "date-picker",
      props: { type: "daterange", valueFormat: "YYYY-MM-DD" }
    },
    sortable: true
  },
  { prop: "operation", label: "操作", width: 160, fixed: "right" }
]);
const batchDelete = async (id: string[]) => {
  console.log(id);
  await useHandleData(delJobLog, id.join(), "删除所选操作信息");
  proTable.value?.clearSelection();
  proTable.value?.getTableList();
};
/** 清空按钮操作 */
function handleClean() {
  ElMessageBox.confirm("是否确认清空所有调度日志数据项?")
    .then(function () {
      return cleanJobLog();
    })
    .then(() => {
      proTable.value?.getTableList();
      ElMessage.success("清空成功");
    });
}
/** 导出按钮操作 */
function handleExport() {
  ElMessageBox.confirm("确认导出用户调度日志数据?", "温馨提示", { type: "warning" }).then(() =>
    useDownload(exportJobLogInfo, "调度日志", proTable.value?.searchParam)
  );
}

const open = ref(false);
const form = ref<JobLog.ResJobLogInfo>();
/** 详细按钮操作 */
function handleView(row) {
  open.value = true;
  form.value = row;
}
onMounted(() => {
  console.log(route.fullPath);
  const jobId = route.params && route.params.jobId;
  console.log(jobId);
  if (jobId !== undefined && jobId != 0) {
    getJob(jobId).then(response => {
      console.log(response);
      proTable.value.searchParam.jobName = response.data.jobName;
      proTable.value.searchParam.jobGroup = response.data.jobGroup;
      proTable.value?.search();
    });
  } else {
    nextTick(() => {
      proTable.value?.search();
    });
  }
});
// Remove Tab
const tabRemove = () => {
  tabStore.removeTabs(route.fullPath as string, true);
};
</script>

<style scoped lang="scss"></style>
