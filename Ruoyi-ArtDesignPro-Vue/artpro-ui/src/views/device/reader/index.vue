<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="阅读器"
      permission="device:reader"
      id-key="readerId"
      :fields="fields"
      :list-fn="fetchReaderList"
      :get-fn="fetchReader"
      :add-fn="addReader"
      :update-fn="updateReader"
      :remove-fn="removeReader"
      :defaults="{ status: '0' }"
      export-url="/device/reader/export"
      drawer-size="820px"
    >
      <template #toolbar>
        <QuickChute
          permission="device:reader"
          :dept-options="deptOptions"
          @created="refreshChutes"
        />
      </template>
    </SystemCrudPage>
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import QuickChute from '../quick-chute.vue'
  import { addReader, fetchReader, fetchReaderList, removeReader, updateReader } from '@/api/device'
  import { useDeviceOptions } from '../useDeviceOptions'

  defineOptions({ name: 'DeviceReader' })

  const { dict, chuteOptions, deptOptions, refreshChutes } = useDeviceOptions()
  const fields = computed(() => [
    { prop: 'readerName', label: '阅读器名称', required: true, search: true, minWidth: 150 },
    { prop: 'readerCode', label: '阅读器编码', required: true, search: true, minWidth: 140 },
    { prop: 'readerType', label: '阅读器类型', minWidth: 130 },
    { prop: 'ipAddr', label: 'IP地址', required: true, search: true, minWidth: 140 },
    { prop: 'installPosition', label: '安装位置', minWidth: 160 },
    {
      prop: 'chuteId',
      label: '对应溜井',
      type: 'select',
      options: chuteOptions.value,
      required: true,
      search: true,
      table: false
    },
    { prop: 'chuteName', label: '溜井名称', form: false, minWidth: 140 },
    {
      prop: 'deptId',
      label: '所属部门',
      type: 'treeselect',
      required: true,
      table: false,
      props: {
        data: deptOptions.value,
        nodeKey: 'deptId',
        valueKey: 'deptId',
        props: { value: 'deptId', label: 'deptName', children: 'children' },
        checkStrictly: true
      }
    },
    { prop: 'deptName', label: '部门', form: false, minWidth: 120 },
    {
      prop: 'status',
      label: '状态',
      type: 'select',
      options: dict.sys_normal_disable,
      dict: dict.sys_normal_disable,
      required: true,
      search: true,
      width: 100
    },
    { prop: 'createTime', label: '添加时间', form: false, minWidth: 160 },
    { prop: 'updateTime', label: '修改时间', form: false, minWidth: 160 },
    { prop: 'remark', label: '备注', table: false, props: { type: 'textarea', rows: 3 }, span: 24 }
  ])
</script>
