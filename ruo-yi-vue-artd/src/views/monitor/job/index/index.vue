<template>
  <div>
    <ArtCrud
      ref="crudRef"
      :config="crudConfig"
      v-model:search="queryParams"
      v-model:edit-form="form"
      @edit-row="handleUpdate"
      @add-row="handleAdd"
      @selection-change="handleSelectionChange">
      <template #toolbar>
        <el-button
          v-auth="'monitor:job:edit'"
          type="success"
          plain
          :icon="Edit"
          :disabled="single"
          @click="() => handleUpdate()">
          修改
        </el-button>
        <el-button
          v-auth="'monitor:job:remove'"
          type="danger"
          plain
          :icon="Delete"
          :disabled="multiple"
          @click="() => handleDelete()">
          删除
        </el-button>
        <el-button
          v-auth="'monitor:job:export'"
          type="warning"
          plain
          :icon="Download"
          @click="handleExport">
          导出
        </el-button>
        <el-button
          v-auth="'monitor:job:query'"
          type="info"
          plain
          :icon="Operation"
          @click="() => handleJobLog()">
          日志
        </el-button>
      </template>

      <template #col-jobName="{ row }">
        <el-link type="primary" @click="handleView(row)">
          {{ row.jobName }}
        </el-link>
      </template>

      <template #col-jobGroup="{ row }">
        <dict-tag :options="sys_job_group" :value="row.jobGroup" />
      </template>

      <template #col-status="{ row }">
        <el-switch
          v-model="row.status"
          active-value="0"
          inactive-value="1"
          @change="handleStatusChange(row)" />
      </template>

      <template #col-operationTemplate="{ row }">
        <ArtButtonTable
          v-auth="'monitor:job:edit'"
          type="edit"
          @click="handleUpdate(row)" />
        <ArtButtonTable
          v-auth="'monitor:job:remove'"
          type="delete"
          @click="handleDelete(row)" />
        <ArtButtonTable
          v-auth="'monitor:job:changeStatus'"
          icon="ri:play-fill"
          tooltip="执行一次"
          icon-class="bg-success/12 text-success"
          @click="handleRun(row)" />
        <ArtButtonTable
          v-auth="'monitor:job:query'"
          icon="ri:file-list-3-line"
          tooltip="调度日志"
          icon-class="bg-info/12 text-info"
          @click="handleJobLog(row)" />
      </template>

      <!-- <template #form-invokeTarget="{ modelValue }">
        <el-input
          v-model="modelValue.invokeTarget"
          placeholder="请输入调用目标字符串" />
      </template> -->

      <template #form-cronExpression="{ modelValue }">
        <el-input
          v-model="modelValue.cronExpression"
          placeholder="请输入cron执行表达式">
          <template #append>
            <el-button type="primary" @click="handleShowCron">
              生成表达式
            </el-button>
          </template>
        </el-input>
      </template>
    </ArtCrud>

    <el-dialog
      title="Cron表达式生成器"
      v-model="openCron"
      append-to-body
      destroy-on-close>
      <Crontab
        :expression="expression"
        @hide="openCron = false"
        @fill="crontabFill" />
    </el-dialog>

    <JobDetail v-model:visible="openView" :row="detailRow" type="job" />
  </div>
</template>

<script setup lang="ts">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type { JobQueryParams, SysJob } from '@/types/api/monitor/job'

import {
  Delete,
  Download,
  Edit,
  Operation,
  QuestionFilled
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import ArtCrud from '@/components/business/art-crud/index.vue'
import Crontab from '@/components/Crontab/index.vue'
import { useAuth } from '@/hooks/core/useAuth'
import { useDict } from '@/hooks/core/useDict'
import download from '@/utils/http/download'

import JobDetail from '../components/detail.vue'
import {
  addJob,
  changeJobStatus,
  delJob,
  getJob,
  listJob,
  runJob,
  updateJob
} from './api'

defineOptions({ name: 'Job' })

const router = useRouter()
const { hasAuth } = useAuth()
const { sys_job_group, sys_job_status } = useDict(
  'sys_job_group',
  'sys_job_status'
)

const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')

const ids = ref<number[]>([])
const single = ref<boolean>(true)
const multiple = ref<boolean>(true)
const openView = ref<boolean>(false)
const detailRow = ref<SysJob>({})
const openCron = ref<boolean>(false)
const expression = ref<string>('')

/**
 * 判断当前 ArtCrud 是否处于编辑状态
 * @returns 是否为编辑状态
 */
const crudIsEdit = computed(() => crudRef.value?.editType === 'edit')

const queryParams = ref<JobQueryParams>({
  jobName: undefined,
  jobGroup: undefined,
  status: undefined
})
const form = ref<SysJob>({
  jobId: undefined,
  jobName: undefined,
  jobGroup: undefined,
  invokeTarget: '',
  cronExpression: '',
  misfirePolicy: '1',
  concurrent: '1',
  status: '0'
})

function handleAdd() {
  form.value = {
    jobId: undefined,
    jobName: undefined,
    jobGroup: undefined,
    invokeTarget: '',
    cronExpression: '',
    misfirePolicy: '1',
    concurrent: '1',
    status: '0'
  }
}

async function handleUpdate(row?: SysJob) {
  const jobId = row?.jobId || ids.value[0]
  if (!jobId) return
  form.value = await getJob(jobId)
  crudRef.value?.openEdit(form.value)
}
// 多选框选中数据
function handleSelectionChange(selection: SysJob[]) {
  ids.value = selection.map((item) => item.jobId!)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

// 任务状态修改
function handleStatusChange(row: SysJob) {
  const text = row.status === '0' ? '启用' : '停用'
  ElMessageBox.confirm(`确认要"${text}""${row.jobName}"任务吗?`)
    .then(() => changeJobStatus(row.jobId!, row.status!))
    .then(() => {
      ElMessage.success(`${text}成功`)
    })
    .catch(() => {
      row.status = row.status === '0' ? '1' : '0'
    })
}

/* 立即执行一次 */
function handleRun(row: SysJob) {
  ElMessageBox.confirm(`确认要立即执行一次"${row.jobName}"任务吗?`)
    .then(() => runJob(row.jobId!, row.jobGroup!))
    .then(() => {
      ElMessage.success('执行成功')
    })
    .catch(() => {})
}

/** 任务详细信息 */
async function handleView(row: SysJob) {
  detailRow.value = await getJob(row.jobId!)
  openView.value = true
}

/** cron表达式按钮操作 */
async function handleShowCron() {
  expression.value = form.value.cronExpression || ''
  await nextTick()
  openCron.value = true
}

/** 确定后回传值 */
function crontabFill(value: string) {
  form.value.cronExpression = value
}

/** 任务日志列表查询 */
function handleJobLog(row?: SysJob) {
  const jobId = row?.jobId || 0
  router.push(`/monitor/job-log/index/${jobId}`)
}

// 删除定时任务调度
function handleDelete(row?: SysJob) {
  const jobIds = row?.jobId || ids.value
  ElMessageBox.confirm(`是否确认删除定时任务编号为"${jobIds}"的数据项?`)
    .then(() => delJob(jobIds))
    .then(() => {
      refreshTable()
      ElMessage.success('删除成功')
    })
    .catch(() => {})
}

// 导出定时任务调度列表
function handleExport() {
  download(
    'monitor/job/export',
    {
      ...queryParams.value
    },
    `job_${new Date().getTime()}.xlsx`
  )
}

// 刷新表格
function refreshTable() {
  crudRef.value?.handleRefresh()
}

/** 稳定的表单校验规则引用，避免 computed 重新求值时产生新引用导致 Element Plus 自动重新校验 */
const editFormRules = {
  jobName: [{ required: true, message: '任务名称不能为空', trigger: 'blur' }],
  invokeTarget: [
    { required: true, message: '调用目标字符串不能为空', trigger: 'blur' }
  ],
  cronExpression: [
    { required: true, message: 'cron执行表达式不能为空', trigger: 'change' }
  ]
}

const crudConfig: any = computed<ArtCrudConfig<SysJob>>(() => ({
  api: {
    list: (params) =>
      listJob({
        ...queryParams.value,
        pageNum: params.pageNum,
        pageSize: params.pageSize
      }),
    add: addJob,
    update: updateJob,
    delete: (row: SysJob) => delJob(row.jobId!)
  },
  searchConfig: {
    items: [
      {
        label: '任务名称',
        key: 'jobName',
        type: 'input',
        placeholder: '请输入任务名称'
      },
      {
        label: '任务组名',
        key: 'jobGroup',
        type: 'select',
        placeholder: '请选择任务组名',
        options: sys_job_group.value
      },
      {
        label: '任务状态',
        key: 'status',
        type: 'select',
        placeholder: '请选择任务状态',
        options: sys_job_status.value
      }
    ]
  },
  columns: [
    { type: 'selection', width: 55, align: 'center' },
    { label: '任务编号', prop: 'jobId', width: 100, align: 'center' },
    {
      label: '任务名称',
      prop: 'jobName',
      align: 'center',
      showOverflowTooltip: true,
      useSlot: true,
      slotName: 'jobName'
    },
    {
      label: '任务组名',
      prop: 'jobGroup',
      align: 'center',
      useSlot: true,
      slotName: 'jobGroup'
    },
    {
      label: '调用目标字符串',
      prop: 'invokeTarget',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: 'cron执行表达式',
      prop: 'cronExpression',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '状态',
      prop: 'status',
      align: 'center',
      useSlot: true,
      slotName: 'status'
    },
    {
      label: '操作',
      prop: 'operation',
      width: 200,
      align: 'center',
      useSlot: true,
      slotName: 'operationTemplate'
    }
  ],
  editConfig: {
    span: 12,
    labelWidth: 120,
    items: [
      {
        label: '任务名称',
        key: 'jobName',
        type: 'input',
        props: { placeholder: '请输入任务名称' }
      },
      {
        label: '任务分组',
        key: 'jobGroup',
        type: 'select',
        props: { placeholder: '请选择', options: sys_job_group.value }
      },
      {
        label: '调用方法',
        tooltip:
          "Bean调用示例：ryTask.ryParams('ry')\nClass类调用示例：com.ruoyi.quartz.task.RyTask.ryParams('ry')\n参数说明：支持字符串，布尔类型，长整型，浮点型，整型",
        key: 'invokeTarget',
        type: 'input',
        props: { placeholder: '请输入调用目标字符串' },
        span: 24
      },
      {
        label: 'cron表达式',
        key: 'cronExpression',
        span: 24,
        useSlot: true,
        slotName: 'cronExpression'
      },
      {
        label: '执行策略',
        key: 'misfirePolicy',
        type: 'radiogroupbutton',
        span: 24,
        props: {
          options: [
            { label: '立即执行', value: '1' },
            { label: '执行一次', value: '2' },
            { label: '放弃执行', value: '3' }
          ]
        }
      },
      {
        label: '状态',
        key: 'status',
        type: 'radiogroup',
        hidden: !crudIsEdit.value,
        props: { options: sys_job_status.value }
      },
      {
        label: '是否并发',
        key: 'concurrent',
        type: 'radiogroup',
        props: {
          options: [
            { label: '允许', value: '0' },
            { label: '禁止', value: '1' }
          ]
        }
      }
    ],
    // 抽取到外部常量，保证引用稳定，避免 computed 重新求值时 Element Plus 自动重新校验
    rules: editFormRules
  },
  btnConfig: {
    showAddOperation: () => hasAuth('monitor:job:add'),
    showEditOperation: () => false,
    showDeleteOperation: () => false
  }
}))
</script>
