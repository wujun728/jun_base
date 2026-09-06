<template>
  <el-drawer
    v-model="drawerVisible"
    title="已授权用户"
    size="70%"
    @close="handleClose"
    @opened="getList">
    <template #default>
      <!-- 搜索区域 -->
      <ArtSearchBar
        v-model="queryParams"
        :items="searchItems"
        :show-border="false"
        :show-expand="false"
        :button-left-limit="2"
        @search="handleQuery"
        @reset="resetQuery" />

      <el-row :gutter="10" class="mb-5">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            :icon="Plus"
            @click="openSelectUser"
            v-auth="'system:role:add'">
            添加用户
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            :icon="CircleClose"
            :disabled="multiple"
            @click="cancelAuthUserAll"
            v-auth="'system:role:remove'">
            批量取消授权
          </el-button>
        </el-col>
      </el-row>

      <el-table
        v-loading="loading"
        :data="userList"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          label="用户名称"
          prop="userName"
          :show-overflow-tooltip="true" />
        <el-table-column
          label="用户昵称"
          prop="nickName"
          :show-overflow-tooltip="true" />
        <el-table-column
          label="邮箱"
          prop="email"
          :show-overflow-tooltip="true" />
        <el-table-column
          label="手机"
          prop="phonenumber"
          :show-overflow-tooltip="true" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              link
              type="primary"
              :icon="CircleClose"
              @click="cancelAuthUser(scope.row)"
              v-auth="'system:role:remove'">
              取消授权
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList" />
      <select-user
        ref="selectRef"
        :roleId="queryParams.roleId"
        @ok="handleQuery" />
    </template>
  </el-drawer>
</template>

<script setup lang="ts" name="AuthUser">
import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'
import type { SysUser, AuthUserQueryParams } from '@/types/api/system/user'

import { Plus, CircleClose } from '@element-plus/icons-vue'

import ArtSearchBar from '@/components/core/forms/art-search-bar/index.vue'
import DictTag from '@/components/core/ruoyi/dict-tag/index.vue'
import { useDict } from '@/hooks/core/useDict.js'
import { parseTime } from '@/utils/sys/ruoyi'

import { allocatedUserList, authUserCancel, authUserCancelAll } from '../api'
import selectUser from './selectUser.vue'

const route = useRoute()
const { sys_normal_disable } = useDict('sys_normal_disable')

const selectRef = ref<typeof selectUser>()

const drawerVisible = ref<boolean>(false)
const userList = ref<SysUser[]>([])
const loading = ref<boolean>(true)
const multiple = ref<boolean>(true)
const total = ref<number>(0)
const userIds = ref<number[]>([])

const queryParams = reactive<AuthUserQueryParams>({
  pageNum: 1,
  pageSize: 10,
  roleId: Number(route.params.roleId),
  userName: undefined,
  phonenumber: undefined
})

const searchItems: SearchFormItem[] = [
  {
    key: 'userName',
    label: '用户名称',
    type: 'input',
    props: { clearable: true, placeholder: '请输入用户名称' }
  },
  {
    key: 'phonenumber',
    label: '手机号码',
    type: 'input',
    props: { clearable: true, placeholder: '请输入手机号码' }
  }
]

/** 打开抽屉 */
function open(roleId?: number) {
  if (roleId !== undefined) {
    queryParams.roleId = roleId
  }
  drawerVisible.value = true
}

/** 关闭抽屉 */
function handleClose() {
  drawerVisible.value = false
}

/** 查询授权用户列表 */
async function getList() {
  loading.value = true
  const res = await allocatedUserList(queryParams)
  userList.value = res.rows
  total.value = res.total
  loading.value = false
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection: SysUser[]) {
  userIds.value = selection.map((item) => item.userId!)
  multiple.value = !selection.length
}

/** 打开授权用户表弹窗 */
function openSelectUser() {
  if (!selectRef.value) return

  selectRef.value.show()
}

/** 取消授权按钮操作 */
function cancelAuthUser(row: SysUser) {
  ElMessageBox.confirm('确认要取消该用户"' + row.userName + '"角色吗？')
    .then(function () {
      return authUserCancel({
        userId: row.userId!,
        roleId: queryParams.roleId!
      })
    })
    .then(() => {
      getList()
      ElMessage.success('取消授权成功')
    })
    .catch(() => {})
}

/** 批量取消授权按钮操作 */
async function cancelAuthUserAll() {
  const roleId = queryParams.roleId
  const uIds = userIds.value.join(',')
  ElMessageBox.confirm('是否取消选中用户授权数据项?').then(async () => {
    await authUserCancelAll({ roleId: roleId!, userIds: uIds })
    getList()
    ElMessage.success('取消授权成功')
  })
}

defineExpose({ open })
</script>
