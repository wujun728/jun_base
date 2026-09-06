<script setup name="Job">
import Review from './components/Review.vue'
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getDialogConfig from './config/dialogConfig.js'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import Crontab from '@/components/Crontab/index.vue'
import to from '@/utils/to'
import { runJob, changeJobStatus } from '@/api/monitor/job'
import { monitorBaseUrl } from '@/api/config/base.js'
import { job } from '@/views/pageName.js'

const proxy = inject('proxy')
const { sys_job_group, sys_job_status } = proxy.useDict(
  'sys_job_group',
  'sys_job_status'
)
const router = useRouter()
const isSmall = window.isSmallScreen
const pageName = job
const requestBaseUrl = monitorBaseUrl
const pageSearchRef = useTemplateRef('pageSearchRef')
const pageContentRef = useTemplateRef('pageContentRef')
const dialogHideItems = ref([])
const tableHideItems = ref([])

const dictMap = {
  status: sys_job_status,
  jobGroup: sys_job_group,
}

const searchConfig = getSearchConfig()
const searchConfigComputed = computed(() => {
  return getComputedConfig(searchConfig, dictMap)
})

const tableSelected = ref([])
const tableListener = {
  selectionChange: (selected) => {
    tableSelected.value = selected
  },
}

const contentConfig = getContentConfig()
const contentConfigComputed = computed(() => {
  contentConfig.hideItems = tableHideItems
  return contentConfig
})

const dialogConfig = getDialogConfig()
const dialogConfigComputed = computed(() => {
  dialogConfig.hideItems = dialogHideItems
  return getComputedConfig(dialogConfig, dictMap)
})

const addCallBack = () => {
  dialogHideItems.value.length = 0
}

const editCallBack = (item) => {
  expression.value = item.cronExpression
}

const [dialogRef, infoInit, addClick, editBtnClick] = useDialog(
  addCallBack,
  editCallBack,
  '添加'
)

const dialogWidth = ref('600px')
const searchData = computed(() => pageContentRef.value?.finalSearchData)

const search = () => {
  pageSearchRef.value?.search()
}

const permission = ref({
  add: 'monitor:job:add',
  edit: 'monitor:job:edit',
  del: 'monitor:job:remove',
})

const editClick = (row) => {
  pageContentRef.value?.editClick(row)
}

const deleteRow = (row) => {
  pageContentRef.value?.deleteRow(row)
}

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const handleExport = () => {
  proxy.download(
    'monitor/job/export',
    { ...searchData.value },
    `monitor_${new Date().getTime()}.xlsx`
  )
}

const handleStatusChange = async (row) => {
  row.statusLoading = true
  const text = row.status === '0' ? '启用' : '停用'
  const [res] = await to(changeJobStatus(row.jobId, row.status))
  if (res) {
    proxy.$modal.msgSuccess(`${text}成功`)
  } else {
    row.status = row.status === '0' ? '1' : '0'
  }
  row.statusLoading = false
}

const cronOpen = ref(false)
const crontabRef = useTemplateRef('crontabRef')
const expression = ref('')

const crontabFill = (value) => {
  dialogRef.value?.setFormData('cronExpression', value)
}

const handleShowCron = () => {
  expression.value = dialogRef.value?.formData?.cronExpression ?? ''
  cronOpen.value = true
}

const clearCron = () => {
  crontabRef.value?.clearCron()
}

const submitFill = () => {
  crontabRef.value?.submitFill()
}

const viewFormData = ref({})
const viewOpen = ref(false)

const handleView = (backData) => {
  viewFormData.value = backData
  viewOpen.value = true
}

const jobGroupFormat = (row) => {
  return proxy.selectDictLabel(sys_job_group.value, row.jobGroup)
}

const handleRun = async (row) => {
  const [res] = await to(runJob(row.jobId, row.jobGroup))
  if (res) {
    proxy.$modal.msgSuccess('执行成功')
  }
}

const handleJobLog = (row) => {
  const jobId = row?.jobId || 0
  router.push(`/monitor/job-log/index/${jobId}`)
}
</script>

<template>
  <div class="default-main page">
    <PageSearch
      ref="pageSearchRef"
      :pageName="pageName"
      :searchConfig="searchConfigComputed"
    />
    <PageContent
      ref="pageContentRef"
      :pageName="pageName"
      :contentConfig="contentConfigComputed"
      :autoDesc="false"
      :dictMap="dictMap"
      :tableListener="tableListener"
      :tableSelected="tableSelected"
      :permission="permission"
      :requestBaseUrl="requestBaseUrl"
      :tableHideItems="tableHideItems"
      :showEdit="false"
      :showDelete="false"
      @addClick="addClick"
      @editBtnClick="editBtnClick"
      @onChangeShowColumn="onChangeShowColumn"
    >
      <template #handleLeft>
        <a-button
          class="order16"
          type="dashed"
          v-hasPermi="['monitor:job:export']"
          @click="handleExport"
        >
          <template #icon>
            <SvgIcon size="14" iconClass="export" />
          </template>
          <span class="ml6">导出</span>
        </a-button>
        <a-button
          class="order17"
          type="dashed"
          v-hasPermi="['monitor:job:query']"
          @click="handleJobLog()"
        >
          <template #icon>
            <SvgIcon size="14" iconClass="log" />
          </template>
          <span class="ml6">日志</span>
        </a-button>
      </template>

      <template #jobGroupSlot="{ backData }">
        <DictTag :options="sys_job_group" :value="backData.jobGroup" />
      </template>
      <template #statusSlot="{ backData }">
        <a-switch
          v-model:checked="backData.status"
          checked-value="0"
          un-checked-value="1"
          :loading="backData.statusLoading"
          @change="() => handleStatusChange(backData)"
        />
      </template>
      <template #toSth="{ backData }">
        <div class="do-sth">
          <a-tooltip v-if="hasPermi(permission.edit)" title="编辑">
            <a-button type="primary" size="small" @click="editClick(backData)">
              <template #icon>
                <SvgIcon size="12" iconClass="pencil" />
              </template>
            </a-button>
          </a-tooltip>
          <a-tooltip v-if="hasPermi('monitor:job:query')" title="任务详情">
            <a-button
              type="primary"
              size="small"
              class="ml6"
              @click="handleView(backData)"
            >
              <template #icon>
                <SvgIcon size="12" iconClass="eye-open" />
              </template>
            </a-button>
          </a-tooltip>
          <a-popconfirm
            v-if="hasPermi('monitor:job:changeStatus')"
            title="是否执行一次?"
            ok-text="确认"
            cancel-text="取消"
            @confirm="handleRun(backData)"
          >
            <a-tooltip title="执行一次">
              <a-button type="primary" size="small" class="ml6">
                <template #icon>
                  <SvgIcon size="12" iconClass="caret-square-right" />
                </template>
              </a-button>
            </a-tooltip>
          </a-popconfirm>
          <a-tooltip v-if="hasPermi('monitor:job:query')" title="调度日志">
            <a-button
              type="default"
              size="small"
              class="ml6"
              @click="handleJobLog(backData)"
            >
              <template #icon>
                <SvgIcon size="12" iconClass="log" />
              </template>
            </a-button>
          </a-tooltip>
          <a-popconfirm
            v-if="hasPermi(permission.del)"
            title="确定删除选中记录？"
            ok-text="确认"
            cancel-text="取消"
            @confirm="deleteRow(backData)"
          >
            <a-tooltip title="删除">
              <a-button danger type="primary" size="small" class="ml6">
                <template #icon>
                  <SvgIcon size="12" iconClass="trash" />
                </template>
              </a-button>
            </a-tooltip>
          </a-popconfirm>
        </div>
      </template>
    </PageContent>

    <PageDialog
      ref="dialogRef"
      :width="getWidth(dialogWidth)"
      :pageName="pageName"
      :dialogConfig="dialogConfigComputed"
      :infoInit="infoInit"
      :search="search"
      :requestBaseUrl="requestBaseUrl"
    >
      <template #cronExpressionAddonAfter>
        <a-button type="primary" @click="handleShowCron">
          <template #icon>
            <SvgIcon size="14" iconClass="clock" />
          </template>
          生成表达式
        </a-button>
      </template>
    </PageDialog>

    <a-modal
      v-model:open="cronOpen"
      title="Cron表达式生成器"
      :width="isSmall ? '100%' : 720"
      :wrap-class-name="isSmall ? 'full-modal' : ''"
      destroy-on-close
      @ok="submitFill"
    >
      <Crontab
        ref="crontabRef"
        :expression="expression"
        @fill="crontabFill"
        @hide="cronOpen = false"
      />
      <template #footer>
        <a-button @click="cronOpen = false">取消</a-button>
        <a-button @click="clearCron">重置</a-button>
        <a-button type="primary" @click="submitFill">确认</a-button>
      </template>
    </a-modal>

    <Review
      v-model:open="viewOpen"
      :view-form-data="viewFormData"
      :job-group-format="jobGroupFormat"
    />
  </div>
</template>

<style scoped lang="scss">
.do-sth {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: flex-start;
  gap: 4px;
}
</style>
