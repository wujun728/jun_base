<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="监控设备管理"
      permission="business:deviceCamera"
      id-key="id"
      :fields="fields"
      :list-fn="fetchSqDeviceCameraList"
      :get-fn="fetchSqDeviceCamera"
      :add-fn="addSqDeviceCamera"
      :update-fn="updateSqDeviceCamera"
      :remove-fn="removeSqDeviceCamera"
      :defaults="{ status: 'start', isDeleted: '0', safeAlert: true }"
      export-url="/business/sq/device-camera/export"
      drawer-size="960px"
    />
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqDeviceCamera,
    fetchSqDeviceCamera,
    fetchSqDeviceCameraList,
    removeSqDeviceCamera,
    updateSqDeviceCamera
  } from '@/api/business/sqDevice'
  import { fetchDeptTree } from '@/api/system/user'

  defineOptions({ name: 'BusinessSqDeviceCamera' })

  const deptOptions = ref<any[]>([])
  const statusOptions = [
    { label: '运行', value: 'start' },
    { label: '离线', value: 'stop' }
  ]

  const fields = computed(() => [
    { prop: 'lname', label: '设备名称', required: true, search: true, minWidth: 150 },
    { prop: 'carmname', label: '摄像机名称', search: true, minWidth: 140 },
    { prop: 'carmip', label: '摄像机IP', search: true, minWidth: 130 },
    {
      prop: 'type',
      label: '视频类型',
      type: 'select',
      dictType: 'camera_type',
      search: true,
      minWidth: 120
    },
    { prop: 'company', label: '所属中段', required: true, search: true, minWidth: 120 },
    {
      prop: 'tname',
      label: '所属单位',
      type: 'treeselect',
      required: true,
      search: true,
      table: false,
      props: deptTreeProps()
    },
    { prop: 'tnameLabel', label: '所属单位', form: false, minWidth: 130 },
    {
      prop: 'mine',
      label: '所属工区',
      type: 'treeselect',
      table: false,
      props: deptTreeProps()
    },
    { prop: 'mineLabel', label: '所属工区', form: false, minWidth: 130 },
    { prop: 'shitypename', label: '矿种种类', minWidth: 130 },
    {
      prop: 'shitype',
      label: '矿种名称',
      type: 'select',
      dictType: 'shitype',
      search: true,
      minWidth: 130
    },
    { prop: 'username', label: '用户名', table: false },
    { prop: 'password', label: '密码', table: false, props: { showPassword: true } },
    { prop: 'channel', label: '通道号', minWidth: 100 },
    { prop: 'carmstar', label: '星级', minWidth: 90 },
    { prop: 'px', label: '排序', minWidth: 90 },
    { prop: 'dppx', label: '大屏排序', minWidth: 100 },
    {
      prop: 'safeAlert',
      label: '安全预警',
      type: 'switch',
      table: false,
      props: { activeValue: true, inactiveValue: false }
    },
    {
      prop: 'serverUrl',
      label: '直播流通道',
      table: false,
      span: 24,
      props: { type: 'textarea', rows: 3 }
    },
    {
      prop: 'serverUrl2',
      label: '直播流通道2',
      table: false,
      span: 24,
      props: { type: 'textarea', rows: 3 }
    },
    {
      prop: 'status',
      label: '状态',
      type: 'select',
      options: statusOptions,
      required: true,
      search: true,
      width: 100
    },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 160 },
    { prop: 'updateTime', label: '更新时间', form: false, minWidth: 160 }
  ])

  onMounted(async () => {
    deptOptions.value = await fetchDeptTree()
  })

  function deptTreeProps() {
    return {
      data: deptOptions.value,
      nodeKey: 'id',
      valueKey: 'id',
      props: { value: 'id', label: 'label', children: 'children' },
      checkStrictly: true
    }
  }
</script>
