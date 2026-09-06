<template>
  <div class="main-box">
    <div class="table-box">
      <ProTable ref="proTable" row-key="deptId" :columns="columns" :request-api="listJob" :request-auto="true">
        <template #tableHeader>
          <el-button type="primary" :icon="CirclePlus" v-hasPermi="['monitor:job:add']" @click="handleAdd">
            新增定时任务
          </el-button>
          <el-button type="primary" :icon="Download" plain v-hasPermi="['monitor:job:export']" @click="downloadFile">
            导出定时任务数据
          </el-button>
          <el-button plain :icon="Operation" @click="handleJobLog" v-hasPermi="['monitor:job:query']"> 日志 </el-button>
        </template>
        <template #operation="scope">
          <el-tooltip content="修改" placement="top">
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['monitor:job:edit']"
            ></el-button>
          </el-tooltip>
          <el-tooltip content="删除" placement="top">
            <el-button
              link
              type="primary"
              icon="Delete"
              @click="deleteAccount(scope.row)"
              v-hasPermi="['monitor:job:remove']"
            ></el-button>
          </el-tooltip>
          <el-tooltip content="执行一次" placement="top">
            <el-button
              link
              type="primary"
              icon="CaretRight"
              @click="handleRun(scope.row)"
              v-hasPermi="['monitor:job:changeStatus']"
            ></el-button>
          </el-tooltip>
          <el-tooltip content="任务详细" placement="top">
            <el-button
              link
              type="primary"
              icon="View"
              @click="handleView(scope.row)"
              v-hasPermi="['monitor:job:query']"
            ></el-button>
          </el-tooltip>
          <el-tooltip content="调度日志" placement="top">
            <el-button
              link
              type="primary"
              icon="Operation"
              @click="handleJobLog(scope.row)"
              v-hasPermi="['monitor:job:query']"
            ></el-button>
          </el-tooltip>
        </template>
      </ProTable>
    </div>
    <!-- 添加或修改定时任务对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form ref="jobRef" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务名称" prop="jobName">
              <el-input v-model="form.jobName" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组" prop="jobGroup">
              <el-select v-model="form.jobGroup" placeholder="请选择">
                <el-option v-for="dict in sys_job_group" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="invokeTarget">
              <template #label>
                <span>
                  调用方法
                  <el-tooltip placement="top">
                    <template #content>
                      <div>
                        Bean调用示例：ryTask.ryParams('ry')
                        <br />Class类调用示例：com.ruoyi.quartz.task.RyTask.ryParams('ry')
                        <br />参数说明：支持字符串，布尔类型，长整型，浮点型，整型
                      </div>
                    </template>
                    <el-icon><question-filled /></el-icon>
                  </el-tooltip>
                </span>
              </template>
              <el-input v-model="form.invokeTarget" placeholder="请输入调用目标字符串" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="cron表达式" prop="cronExpression">
              <el-input v-model="form.cronExpression" placeholder="请输入cron执行表达式">
                <template #append>
                  <el-button type="primary" @click="handleShowCron">
                    生成表达式
                    <i class="el-icon-time el-icon--right"></i>
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行策略" prop="misfirePolicy">
              <el-radio-group v-model="form.misfirePolicy">
                <el-radio-button label="1">立即执行</el-radio-button>
                <el-radio-button label="2">执行一次</el-radio-button>
                <el-radio-button label="3">放弃执行</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否并发" prop="concurrent">
              <el-radio-group v-model="form.concurrent">
                <el-radio-button label="0">允许</el-radio-button>
                <el-radio-button label="1">禁止</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in sys_job_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="Cron表达式生成器" v-model="openCron" append-to-body destroy-on-close>
      <crontab ref="crontabRef" @hide="openCronHide" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>

    <!-- 任务日志详细 -->
    <el-dialog title="任务详细" v-model="openView" width="700px" append-to-body>
      <el-form :model="form" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务编号：">{{ form.jobId }}</el-form-item>
            <el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组：">
              <DictTag :options="sys_job_group" :value="form.jobGroup"></DictTag>
            </el-form-item>
            <el-form-item label="创建时间：">{{ form.createTime }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="cron表达式：">{{ form.cronExpression }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次执行时间：">{{ form.nextValidTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用目标方法：">{{ form.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务状态：">
              <div v-if="form.status == 0">正常</div>
              <div v-else-if="form.status == 1">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否并发：">
              <div v-if="form.concurrent === 0">允许</div>
              <div v-else-if="form.concurrent == 1">禁止</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行策略：">
              <div v-if="form.misfirePolicy == 0">默认策略</div>
              <div v-else-if="form.misfirePolicy == 1">立即执行</div>
              <div v-else-if="form.misfirePolicy == 2">执行一次</div>
              <div v-else-if="form.misfirePolicy == 3">放弃执行</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="openView = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="tsx" name="Job">
import { CirclePlus, Download, Operation } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { reactive, ref } from "vue";
import { ColumnProps } from "@/components/ProTable/interface";
import DictTag from "@/components/DictTag/index.vue";
import { Job } from "@/api/interface";
import { getEnum } from "@/api/ruoyimodule";
import { useDict } from "@/hooks/useDict.js";
import Crontab from "@/components/Crontab/index.vue";
import { addJob, delJob, exportJobInfo, getJob, listJob, runJob, updateJob } from "@/api/ruoyimodule/monitor/job";
import { ElMessage, ElMessageBox, FormInstance } from "element-plus";
import { useDownload } from "@/hooks/useDownload";
import { useRouter } from "vue-router";
import { useHandleData } from "@/hooks/useHandleData";

const router = useRouter();
const { sys_job_group, sys_job_status } = useDict("sys_job_group", "sys_job_status");
// 表格配置项
const columns = reactive<ColumnProps<Job.ResJobInfo>[]>([
  { prop: "jobId", label: "任务编号" },
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
  { prop: "cronExpression", label: "cron执行表达式" },
  {
    prop: "status",
    label: "任务状态",
    enum: getEnum("sys_job_status"),
    search: { el: "select", props: { filterable: true } } as any,
    fieldNames: { label: "dictLabel", value: "dictValue" },
    render: scope => {
      return <DictTag options={sys_job_status.value} value={scope.row.status} />;
    }
  },
  { prop: "remark", label: "备注" },
  {
    prop: "createTime",
    label: "操作日期",
    width: 150,
    sortable: true
  },
  { prop: "operation", label: "操作", width: 160, fixed: "right" }
]);

const rules = {
  jobName: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
  invokeTarget: [{ required: true, message: "调用目标字符串不能为空", trigger: "blur" }],
  cronExpression: [{ required: true, message: "cron执行表达式不能为空", trigger: "change" }]
};

const open = ref(false);
const title = ref("");
const form = ref<Partial<Job.ResJobInfo>>();
const jobRef = ref<FormInstance>();
const proTable = ref();
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加任务";
}

/** 表单重置 */
function reset() {
  form.value = {
    jobId: undefined,
    jobName: undefined,
    jobGroup: undefined,
    invokeTarget: undefined,
    cronExpression: undefined,
    misfirePolicy: 1,
    concurrent: 1,
    status: "0"
  };
  jobRef.value?.resetFields();
}

/** 提交按钮 */
function submitForm() {
  jobRef.value?.validate(valid => {
    if (valid) {
      if (form.value?.jobId != undefined) {
        updateJob(form.value).then(() => {
          ElMessage.success("修改成功");
          open.value = false;
          proTable.value.getTableList();
        });
      } else {
        addJob(form.value).then(() => {
          ElMessage.success("新增成功");
          open.value = false;
          proTable.value.getTableList();
        });
      }
    }
  });
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
const expression = ref("");
const openView = ref(false);
const openCron = ref(false);
/** cron表达式按钮操作 */
function handleShowCron() {
  expression.value = form.value?.cronExpression as string;
  openCron.value = true;
}
function openCronHide() {
  openCron.value = false;
}
/** 确定后回传值 */
function crontabFill(value: string) {
  if (form.value) (form.value as any).cronExpression = value;
}

const downloadFile = async () => {
  ElMessageBox.confirm("确认导出定时任务数据?", "温馨提示", { type: "warning" }).then(() =>
    useDownload(exportJobInfo, "定时任务列表", proTable.value?.searchParam)
  );
};
/** 任务日志列表查询 */
function handleJobLog(row) {
  const jobId = row.jobId || 0;
  router.push("/monitor/job-log/index/" + jobId);
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const jobId = row.jobId;
  getJob(jobId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改任务";
  });
}
/** 删除按钮操作 */
const deleteAccount = async (params: Job.ResJobInfo) => {
  await useHandleData(delJob, params.jobId, `删除定时任务【${params.jobName}】`);
  proTable.value?.getTableList();
};
function handleRun(row) {
  ElMessageBox.confirm('确认要立即执行一次"' + row.jobName + '"任务吗?')
    .then(function () {
      return runJob(row.jobId, row.jobGroup);
    })
    .then(() => {
      ElMessage.success("执行成功");
    });
}
/** 任务详细信息 */
function handleView(row) {
  getJob(row.jobId).then(response => {
    form.value = response.data;
    openView.value = true;
  });
}
</script>

<style scoped lang="scss"></style>
