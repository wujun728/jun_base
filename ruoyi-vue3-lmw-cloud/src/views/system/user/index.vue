<script setup name="User">
import { nextTick } from 'vue'
import ImportDialog from '@/components/HsjComponent/importDialog/index'
import getSearchConfig from './config/searchConfig'
import getContentConfig from './config/contentConfig.js'
import getDialogConfig from './config/dialogConfig.js'
import useDialog from '@/hooks/useDialog'
import getComputedConfig from '@/hooks/getPageConfig'
import {
  changeUserStatus,
  resetUserPwd,
  getUser,
  deptTreeSelect,
} from '@/api/system/user'
import to from '@/utils/to'
import { getToken } from '@/utils/auth'
import { systemBaseUrl } from '@/api/config/base.js'
import { user } from '@/views/pageName.js'

const proxy = inject('proxy')
const { sys_normal_disable, sys_user_sex } = proxy.useDict(
  'sys_normal_disable',
  'sys_user_sex'
)
const pageName = user
const requestBaseUrl = systemBaseUrl
const pageSearchRef = ref(null)
const pageContentRef = ref(null)
const roleOptions = ref([])
const deptOptions = ref([])
const postOptions = ref([])
/** 查询部门下拉树结构 */
const getDeptTree = async () => {
  const [res] = await to(deptTreeSelect())
  if (res) {
    deptOptions.value = res.data ?? []
  }
}
const getPostAndRole = async () => {
  const [res] = await to(getUser())
  if (res) {
    postOptions.value = res.posts ?? []
    roleOptions.value = res.roles ?? []
  }
}

const dialogHideItems = ref([])
const tableHideItems = ref([])
const dictMap = {
  status: sys_normal_disable,
  sex: sys_user_sex,
  deptId: deptOptions,
  postIds: postOptions,
  roleIds: roleOptions,
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
  getPostAndRole()
  dialogHideItems.value.length = 0
}
const editCallBack = (item, type, res) => {
  infoInit.value.roleIds = res.roleIds
  roleOptions.value = res.roles
  infoInit.value.postIds = res.postIds
  postOptions.value = res.posts
  dialogHideItems.value = ['password', 'userName']
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

const beforeSend = (queryInfo) => {
  if (queryInfo.dateRange && Array.isArray(queryInfo.dateRange)) {
    const dateRange = queryInfo.dateRange
    queryInfo['params[beginTime]'] = dateRange[0]
    queryInfo['params[endTime]'] = dateRange[1]
    delete queryInfo.dateRange
  }
}

const permission = ref({
  add: 'system:user:add',
  edit: 'system:user:edit',
  del: 'system:user:remove',
})
const onChangeShowColumn = (filterArr) => {
  tableHideItems.value = filterArr
}

const handleStatusChange = async (row) => {
  row.statusLoading = true
  let text = row.status === '0' ? '启用' : '停用'
  const [res, err] = await to(changeUserStatus(row.userId, row.status))
  if (res) {
    proxy.$modal.notifySuccess(text + '成功')
  } else {
    row.status = row.status === '0' ? '1' : '0'
  }
  row.statusLoading = false
}

const handleResetPwd = (row) => {
  proxy.$modal
    .prompt('请输入"' + row.userName + '"的新密码', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      closeOnClickModal: false,
      inputPattern: /^(?!.*[<>\"'\\|])[^\s]{5,20}$/,
      inputErrorMessage:
        '用户密码长度必须介于 5 和 20 之间且不能包含非法字符：< > " \' \\\ |',
    })
    .then(({ value }) => {
      resetUserPwd(row.userId, value).then((response) => {
        proxy.$modal.notifySuccess('修改成功，新密码是：' + value)
      })
    })
    .catch(() => {})
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy.download(
    'system/user/export',
    {
      ...searchData.value,
    },
    `user_${new Date().getTime()}.xlsx`
  )
}

/*** 用户导入参数 */
const uploadConfig = ref({
  open: false,
  // 弹出层标题（用户导入）
  title: '',
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: 'Bearer ' + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + '/system/user/importData',
})

const handleImport = () => {
  uploadConfig.value.title = '用户导入'
  uploadConfig.value.open = true
}
/** 下载模板操作 */
const downloadTemplate = () => {
  proxy.download(
    'system/user/importTemplate',
    {},
    `user_template_${new Date().getTime()}.xlsx`
  )
}
/**文件上传中处理 */
const handleFileUploadProgress = () => {
  uploadConfig.value.isUploading = true
}

/** 文件上传成功处理 */
const handleFileSuccess = ({ response }) => {
  uploadConfig.value.open = false
  uploadConfig.value.isUploading = false
  proxy.$modal.alert(
    `<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>${response.msg}</div>`,
    '导入结果',
    { dangerouslyUseHTMLString: true }
  )
  search()
}
const handleEditShow = (row) => {
  return row.userId !== 1
}
const handleDeleteShow = (row) => {
  return row.userId !== 1
}
const init = () => {
  getDeptTree()
}

init()
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
      :handleEditShow="handleEditShow"
      :handleDeleteShow="handleDeleteShow"
      :tableHideItems="tableHideItems"
      @beforeSend="beforeSend"
      @addClick="addClick"
      @editBtnClick="editBtnClick"
      @onChangeShowColumn="onChangeShowColumn"
      @editMoreClick="editMoreClick"
    >
      <template #handleLeft>
        <el-button
          type="warning"
          class="ml12 order16"
          v-hasPermi="['system:user:import']"
          @click="handleImport"
        >
          <SvgIcon size="14" iconClass="upload" />
          <span class="ml6">导入</span>
        </el-button>
        <el-button
          class="order17"
          type="warning"
          v-hasPermi="['system:user:export']"
          @click="handleExport"
        >
          <SvgIcon size="14" iconClass="download" />
          <span class="ml6">导出</span>
        </el-button>
      </template>
      <template #statusSlot="{ backData }">
        <el-switch
          v-if="backData.userId !== 1"
          v-model="backData.status"
          active-value="0"
          inactive-value="1"
          @click="handleStatusChange(backData)"
          :loading="backData.statusLoading"
        ></el-switch>
        <span v-else></span>
      </template>
      <template #deptSlot="{ backData }">
        <span> {{ backData.dept?.deptName }}</span>
      </template>
      <template #todoSlot="{ backData }">
        <el-button
          class="ml12 order11"
          size="small"
          type="primary"
          @click="handleResetPwd(backData)"
          v-hasPermi="['system:user:resetPwd']"
          v-if="backData.userId !== 1"
        >
          <SvgIcon size="12" iconClass="key" />
          <span class="ml6">重置密码</span>
        </el-button>
      </template>
    </PageContent>
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
    </PageDialog>
    <ImportDialog
      v-model:upload="uploadConfig"
      @progress="handleFileUploadProgress"
      @success="handleFileSuccess"
      @downloadTemplate="downloadTemplate"
    ></ImportDialog>
  </div>
</template>

<style scoped lang="scss">
.page {
  :deep(.statusClass .el-radio-group) {
    width: 100%;
    justify-content: space-between;
    .el-radio {
      margin-right: 0;
    }
  }
}
</style>
