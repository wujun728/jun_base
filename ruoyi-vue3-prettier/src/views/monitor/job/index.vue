<script setup name="Job">
import Review from './cpns/Review.vue'
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getDialogConfig from './config/dialogConfig.js'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import { monitorBaseUrl } from '@/api/config/base.js'
import Crontab from '@/components/Crontab/index.vue'
import to from '@/utils/to'
import { runJob, changeJobStatus } from '@/api/monitor/job'
import { job } from '@/views/pageName.js'
import { inject } from 'vue'

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
const editCallBack = (item, type, res) => {
  expression.value = item.cronExpression
  isEditMore.value = type
}
const isEditMore = ref(false)
const editMoreClick = () => {
  if (tableSelected.value.length > 0) {
    const data = tableSelected.value[0]
    pageContentRef.value?.editClick(data, true)
    nextTick(() => {
      const newArray = tableSelected.value.slice(1)
      dialogRef.value?.changeSelected(newArray)
    })
  }
}

const editNext = (data) => {
  pageContentRef.value?.editClick(data, true)
}

const [dialogRef, infoInit, addClick, editBtnClick] = useDialog(
  addCallBack,
  editCallBack,
  '添加'
)

const dialogWidth = ref('600px')
const searchData = computed(() => {
  return pageContentRef.value?.finalSearchData
})

const search = () => {
  pageSearchRef.value?.search()
}

const beforeSend = (queryInfo) => {}

const permission = ref({
  add: 'monitor:job:add',
  edit: 'monitor:job:edit',
  del: 'monitor:job:remove',
})

const editClick = (row) => {
  pageContentRef.value.editClick(row)
}
const deleteRow = (row) => {
  pageContentRef.value.deleteRow(row)
}

const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy.download(
    'monitor/job/export',
    {
      ...searchData.value,
    },
    `monitor_${new Date().getTime()}.xlsx`
  )
}
const handleStatusChange = async (row) => {
  let text = row.status === '0' ? '启用' : '停用'
  const [res, err] = await to(changeJobStatus(row.jobId, row.status))
  if (res) {
    proxy.$modal.notifySuccess(text + '成功')
  }
  if (err) {
    row.status = row.status === '0' ? '1' : '0'
  }
}
const cronDialog = ref(false)
const cronLoading = ref(false)
const crontabRef = useTemplateRef('crontabRef')
const expression = ref('')
const crontabFill = (value) => {
  dialogRef.value?.setFormData('cronExpression', value)
}
const handleShowCron = () => {
  cronDialog.value = true
}
const clearCron = () => {
  crontabRef.value?.clearCron()
}
const submitFill = () => {
  crontabRef.value?.submitFill()
}

const viewFormData = ref({})
const viewDialog = ref(false)
const handleView = (backData) => {
  viewFormData.value = backData
  viewDialog.value = true
}
const jobGroupFormat = (row) => {
  return proxy.selectDictLabel(sys_job_group.value, row)
}
/* 立即执行一次 */
const handleRun = async (row) => {
  const [res] = await to(runJob(row.jobId, row.jobGroup))
  if (res) {
    proxy.$modal.notifySuccess('执行成功')
  }
}
const handleJobLog = (row) => {
  const jobId = row.jobId || 0
  router.push('/monitor/job-log/index/' + jobId)
}
const cronMaxHeight = window.innerHeight - 140
</script>
<template>
  <div class="default-main page">
    <PageSearch
      ref="pageSearchRef"
      :pageName="pageName"
      :searchConfig="searchConfigComputed"
    ></PageSearch>
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
      @beforeSend="beforeSend"
      @addClick="addClick"
      @editBtnClick="editBtnClick"
      @onChangeShowColumn="onChangeShowColumn"
      @editMoreClick="editMoreClick"
    >
      <template #handleLeft>
        <el-button
          class="order17 ml12"
          type="warning"
          v-hasPermi="['monitor:job:export']"
          @click="handleExport"
        >
          <SvgIcon size="14" iconClass="download" />
          <span class="ml6">导出</span>
        </el-button>
        <el-button
          class="order17 ml12"
          type="info"
          v-hasPermi="['monitor:job:query']"
          @click="handleJobLog"
        >
          <SvgIcon size="14" iconClass="file-alt" />
          <span class="ml6">日志</span>
        </el-button>
      </template>

      <template #statusSlot="{ backData }">
        <el-switch
          v-model="backData.status"
          active-value="0"
          inactive-value="1"
          @click="handleStatusChange(backData)"
        ></el-switch>
      </template>
      <template #toSth="{ backData }">
        <div class="doSth">
          <el-tooltip
            v-if="hasPermi(permission.edit)"
            :hide-after="0"
            effect="dark"
            content="编辑"
            placement="top"
          >
            <el-button type="primary" size="small" @click="editClick(backData)">
              <SvgIcon size="12" iconClass="pencil" />
            </el-button>
          </el-tooltip>
          <el-tooltip
            v-if="hasPermi('monitor:job:query')"
            :hide-after="0"
            effect="dark"
            content="日志详情"
            placement="top"
          >
            <el-button
              type="primary"
              size="small"
              class="ml12"
              @click="handleView(backData)"
            >
              <SvgIcon size="12" iconClass="eye" />
            </el-button>
          </el-tooltip>
          <el-popconfirm
            v-if="hasPermi('monitor:job:changeStatus')"
            title="是否执行一次?"
            @confirm="handleRun(backData)"
          >
            <template #reference>
              <div>
                <el-tooltip
                  :hide-after="0"
                  effect="dark"
                  content="执行一次"
                  placement="top"
                >
                  <el-button class="ml12 order16" type="primary" size="small">
                    <SvgIcon size="12" iconClass="caret-square-right" />
                  </el-button>
                </el-tooltip>
              </div>
            </template>
          </el-popconfirm>
          <el-tooltip
            v-if="hasPermi('monitor:job:query')"
            :hide-after="0"
            effect="dark"
            content="调度日志"
            placement="top"
          >
            <el-button
              class="order17 ml12"
              type="info"
              size="small"
              @click="handleJobLog(backData)"
            >
              <SvgIcon size="12" iconClass="file-alt" />
            </el-button>
          </el-tooltip>
          <el-popconfirm
            v-if="hasPermi(permission.del)"
            title="确定删除选中记录？"
            confirm-button-text="确认"
            cancel-button-text="取消"
            confirmButtonType="danger"
            :hide-after="0"
            @confirm="deleteRow(backData)"
          >
            <template #reference>
              <div>
                <el-tooltip
                  :hide-after="0"
                  effect="dark"
                  content="删除"
                  placement="top"
                >
                  <el-button class="ml12" type="danger" size="small">
                    <SvgIcon :size="12" iconClass="trash"></SvgIcon>
                  </el-button>
                </el-tooltip>
              </div>
            </template>
          </el-popconfirm>
        </div>
      </template>
    </PageContent>
    <div class="pageDialog">
      <PageDialog
        ref="dialogRef"
        :width="getWidth(dialogWidth)"
        :pageName="pageName"
        :dialogConfig="dialogConfigComputed"
        :infoInit="infoInit"
        :search="search"
        :isEditMore="isEditMore"
        :requestBaseUrl="requestBaseUrl"
        @editNext="editNext"
      >
        <template #cronExpressionAppend>
          <el-button type="primary" @click="handleShowCron">
            <SvgIcon size="14" iconClass="clock" />
            <span class="ml6">生成表达式</span>
          </el-button>
        </template>
      </PageDialog>
    </div>
    <el-dialog
      v-model="cronDialog"
      :fullscreen="isSmall"
      title="Cron表达式生成器"
      top="30px"
      :close-on-click-modal="false"
      destroy-on-close
      draggable
    >
      <el-scrollbar class="pl10 pr10" :max-height="cronMaxHeight" always>
        <Crontab
          ref="crontabRef"
          @fill="crontabFill"
          @hide="cronDialog = false"
          :expression="expression"
        ></Crontab>
      </el-scrollbar>
      <template #footer>
        <div>
          <el-button @click="cronDialog = false" :loading="cronLoading">
            取消
          </el-button>
          <el-button @click="clearCron" :loading="cronLoading">
            重置
          </el-button>
          <el-button @click="submitFill" :loading="cronLoading" type="primary">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
    <Review
      v-model:dialogVisible="viewDialog"
      :viewFormData="viewFormData"
      :jobGroupFormat="jobGroupFormat"
    ></Review>
  </div>
</template>

<style scoped lang="scss">
.page {
  :deep(.el-input-group__append) {
    background-color: var(--el-color-primary);
    color: var(--el-color-white);
  }
}
.doSth {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
}
</style>
