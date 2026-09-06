<template>
  <el-dialog
    v-model="visible"
    :title="`「${noticeTitle}」已读用户`"
    width="800px"
    top="6vh"
    append-to-body
    @close="handleClose">
    <ArtSearchBar
      v-model="queryParams"
      :items="searchItems"
      :show-border="false"
      :show-expand="false"
      :button-left-limit="2"
      @search="handleQuery"
      @reset="resetQuery" />

    <div class="-mt-1 mb-2 flex justify-end">
      <span class="read-stat">
        共
        <strong>{{ total }}</strong>
        人已读
      </span>
    </div>

    <el-table v-loading="loading" :data="userList" stripe height="340px">
      <el-table-column type="index" label="序号" width="55" align="center" />
      <el-table-column
        label="登录名称"
        prop="userName"
        align="center"
        :show-overflow-tooltip="true" />
      <el-table-column
        label="用户名称"
        prop="nickName"
        align="center"
        :show-overflow-tooltip="true" />
      <el-table-column
        label="所属部门"
        prop="deptName"
        align="center"
        :show-overflow-tooltip="true" />
      <el-table-column
        label="手机号码"
        prop="phonenumber"
        align="center"
        width="120" />
      <el-table-column
        label="阅读时间"
        prop="readTime"
        align="center"
        width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.readTime) }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
      style="padding: 6px 0px" />
  </el-dialog>
</template>

<script setup lang="ts">
import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'
import type {
  NoticeReadUser,
  NoticeReadUserQueryParams,
  SysNotice
} from '@/types/api/system/notice'

import { parseTime } from '@/utils/sys/ruoyi'

import { listNoticeReadUsers } from '../api'

const visible = ref(false)
const loading = ref(false)
const noticeTitle = ref('')
const total = ref(0)
const userList = ref<NoticeReadUser[]>([])

const queryParams = reactive<NoticeReadUserQueryParams>({
  pageNum: 1,
  pageSize: 10,
  noticeId: undefined,
  searchValue: undefined
})

const searchItems: SearchFormItem[] = [
  {
    key: 'searchValue',
    label: '',
    type: 'input',
    props: { clearable: true, placeholder: '登录名称 / 用户名称' }
  }
]

function open(row: SysNotice) {
  queryParams.noticeId = row.noticeId
  noticeTitle.value = row.noticeTitle ?? ''
  queryParams.searchValue = undefined
  queryParams.pageNum = 1
  visible.value = true
  getList()
}

function getList() {
  loading.value = true
  listNoticeReadUsers(queryParams)
    .then((res) => {
      userList.value = res.rows
      total.value = res.total
    })
    .finally(() => {
      loading.value = false
    })
}

function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

function resetQuery() {
  handleQuery()
}

function handleClose() {
  userList.value = []
  total.value = 0
  queryParams.searchValue = undefined
}

defineExpose({
  open
})
</script>

<style scoped>
.read-stat {
  font-size: 13px;
  color: #606266;
  line-height: 28px;
}
.read-stat strong {
  color: #409eff;
  font-size: 15px;
  margin: 0 2px;
}
</style>
