<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="摄像头"
      permission="device:camera"
      id-key="cameraId"
      :fields="fields"
      :list-fn="fetchCameraList"
      :get-fn="fetchCamera"
      :add-fn="addCamera"
      :update-fn="updateCamera"
      :remove-fn="removeCamera"
      :defaults="{ status: '0' }"
      export-url="/device/camera/export"
      drawer-size="860px"
    >
      <template #toolbar>
        <QuickChute
          permission="device:camera"
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
  import { addCamera, fetchCamera, fetchCameraList, removeCamera, updateCamera } from '@/api/device'
  import { useDeviceOptions } from '../useDeviceOptions'

  defineOptions({ name: 'DeviceCamera' })

  const { dict, chuteOptions, deptOptions, refreshChutes } = useDeviceOptions()
  const fields = computed(() => [
    { prop: 'cameraName', label: '摄像头名称', required: true, search: true, minWidth: 150 },
    { prop: 'ipAddr', label: 'IP地址', required: true, search: true, minWidth: 140 },
    { prop: 'username', label: '用户名', required: true, minWidth: 120 },
    {
      prop: 'password',
      label: '密码',
      table: false,
      props: { type: 'password', showPassword: true, autocomplete: 'new-password' }
    },
    { prop: 'rtspUrl', label: 'RTSP流地址', required: true, minWidth: 220 },
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
