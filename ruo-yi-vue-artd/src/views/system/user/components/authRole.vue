<template>
  <el-drawer
    v-model="drawerVisible"
    title="分配角色"
    size="70%"
    @close="handleClose"
    @opened="handleOpened">
    <template #default>
      <div class="mb-4 text-base font-medium text-g-800">基本信息</div>
      <el-form :model="form" label-width="80px">
        <el-row>
          <el-col :span="10" :offset="1">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="10" :offset="1">
            <el-form-item label="登录账号" prop="userName">
              <el-input v-model="form.userName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div class="mt-2 mb-4 text-base font-medium text-g-800">角色信息</div>
      <el-table
        v-loading="loading"
        :row-key="getRowKey"
        @row-click="clickRow"
        ref="roleRef"
        @selection-change="handleSelectionChange"
        :data="roles.slice((pageNum - 1) * pageSize, pageNum * pageSize)">
        <el-table-column label="序号" width="55" type="index" align="center">
          <template #default="scope">
            <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          type="selection"
          :reserve-selection="true"
          :selectable="checkSelectable"
          width="55"></el-table-column>
        <el-table-column label="角色编号" align="center" prop="roleId" />
        <el-table-column label="角色名称" align="center" prop="roleName" />
        <el-table-column label="权限字符" align="center" prop="roleKey" />
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="pageNum"
        v-model:limit="pageSize" />
    </template>

    <template #footer>
      <div style="text-align: center">
        <el-button type="primary" @click="submitForm()">提交</el-button>
        <el-button @click="handleClose">取消</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import type { SysRole } from '@/types/api/system/role'
import type { SysUser } from '@/types/api/system/user'

import { ElMessage } from 'element-plus'

import { parseTime } from '@/utils/sys/ruoyi'

import { getAuthRole, updateAuthRole } from '../api'

interface SysRoleWithFlag extends SysRole {
  /** 用户是否存在此角色标识 */
  flag: boolean
}

const roleRef = useTemplateRef('roleRef')

const emit = defineEmits<{
  success: []
}>()

const drawerVisible = ref(false)
const loading = ref<boolean>(true)

const total = ref<number>(0)
const pageNum = ref<number>(1)
const pageSize = ref<number>(10)

const currentUserId = ref<number>(0)
const roleIds = ref<number[]>([])
const roles = ref<SysRoleWithFlag[]>([])
const form = ref<SysUser>({
  nickName: undefined,
  userName: undefined,
  userId: undefined
})

/** 打开抽屉并加载用户角色数据 */
async function open(userId: number) {
  drawerVisible.value = true
  loading.value = true
  roleIds.value = []
  roles.value = []
  pageNum.value = 1
  currentUserId.value = userId
  roleRef.value?.clearSelection()
}

/** 打开抽屉时调用 */
async function handleOpened() {
  const response = await getAuthRole(currentUserId.value)
  form.value = response.user
  roles.value = response.roles as SysRoleWithFlag[]
  total.value = roles.value.length
  nextTick(() => {
    roleRef.value?.clearSelection()
    roles.value.forEach((row: SysRoleWithFlag) => {
      if (row.flag && roleRef.value) {
        roleRef.value.toggleRowSelection(row, true)
      }
    })
    loading.value = false
  })
}

/** 单击选中行数据 */
function clickRow(row: SysRole) {
  if (checkSelectable(row) && roleRef.value) {
    roleRef.value.toggleRowSelection(row)
  }
}

/** 多选框选中数据 */
function handleSelectionChange(selection: SysRole[]) {
  roleIds.value = selection.map((item) => item.roleId!)
}

/** 保存选中的数据编号 */
function getRowKey(row: SysRole): string {
  return String(row.roleId!)
}

/** 检查角色状态 */
function checkSelectable(row: SysRole): boolean {
  return row.status === '0'
}

/** 关闭抽屉 */
function handleClose() {
  drawerVisible.value = false
}

/** 提交按钮 */
function submitForm() {
  const userId = form.value.userId
  const rIds = roleIds.value.join(',') as any
  updateAuthRole({ userId: userId!, roleIds: rIds }).then(() => {
    ElMessage.success('授权成功')
    emit('success')
    handleClose()
  })
}

defineExpose({ open })
</script>
