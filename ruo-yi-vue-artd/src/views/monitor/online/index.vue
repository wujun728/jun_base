<template>
  <ArtCrud ref="crudRef" :config="crudConfig" v-model:search="queryParams">
    <template #col-loginTime="{ row }">
      <span>{{ parseTime(row.loginTime) }}</span>
    </template>

    <template #col-operationTemplate="{ row }">
      <ArtButtonTable
        v-auth="'monitor:online:forceLogout'"
        type="delete"
        tooltip="强退"
        @click="handleForceLogout(row)">
        强退
      </ArtButtonTable>
    </template>
  </ArtCrud>
</template>

<script setup lang="ts" name="Online">
import type { ArtCrudConfig } from '@/components/business/art-crud/index.vue'
import type { SysUserOnline } from '@/types/api/monitor/online'

import { ElMessage, ElMessageBox } from 'element-plus'

import ArtCrud from '@/components/business/art-crud/index.vue'
import { parseTime } from '@/utils/sys/ruoyi'

import { forceLogout, list } from './api'

const crudRef = useTemplateRef<InstanceType<typeof ArtCrud>>('crudRef')

const queryParams = ref<Record<string, any>>({
  ipaddr: undefined,
  userName: undefined
})

function handleForceLogout(row: SysUserOnline) {
  ElMessageBox.confirm(`是否确认强退名称为"${row.userName}"的用户?`)
    .then(() => forceLogout(row.tokenId!))
    .then(() => {
      crudRef.value?.handleRefresh()
      ElMessage.success('强退成功')
    })
    .catch(() => {})
}

const crudConfig = computed<ArtCrudConfig<SysUserOnline>>(() => ({
  api: {
    list: (params) =>
      list({
        ...queryParams.value
      })
  },
  searchConfig: {
    items: [
      {
        label: '登录地址',
        key: 'ipaddr',
        type: 'input',
        placeholder: '请输入登录地址'
      },
      {
        label: '用户名称',
        key: 'userName',
        type: 'input',
        placeholder: '请输入用户名称'
      }
    ]
  },
  columns: [
    {
      label: '会话编号',
      prop: 'tokenId',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '登录名称',
      prop: 'userName',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '所属部门',
      prop: 'deptName',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '主机',
      prop: 'ipaddr',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '登录地点',
      prop: 'loginLocation',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '操作系统',
      prop: 'os',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '浏览器',
      prop: 'browser',
      align: 'center',
      showOverflowTooltip: true
    },
    {
      label: '登录时间',
      prop: 'loginTime',
      width: 180,
      align: 'center',
      useSlot: true,
      slotName: 'loginTime'
    },
    {
      label: '操作',
      prop: 'operation',
      width: 100,
      align: 'center',
      fixed: 'right',
      useSlot: true,
      slotName: 'operationTemplate'
    }
  ],
  btnConfig: {
    showAddOperation: () => false,
    showEditOperation: () => false,
    showDeleteOperation: () => false
  }
}))
</script>
