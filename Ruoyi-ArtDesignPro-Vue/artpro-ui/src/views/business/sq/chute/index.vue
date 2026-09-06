<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="溜井基本信息"
      permission="business:chute"
      id-key="id"
      :fields="fields"
      :list-fn="fetchSqChuteList"
      :get-fn="fetchSqChute"
      :add-fn="addSqChute"
      :update-fn="updateSqChute"
      :remove-fn="removeSqChute"
      :defaults="{ status: 'start', isDeleted: '0', safeAlert: true }"
      export-url="/business/sq/chute/export"
      drawer-size="900px"
    />
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqChute,
    fetchSqChute,
    fetchSqChuteList,
    removeSqChute,
    updateSqChute
  } from '@/api/business/sqChute'
  import { fetchDeptTree } from '@/api/system/user'

  defineOptions({ name: 'BusinessSqChute' })

  const deptOptions = ref<any[]>([])
  const statusOptions = [
    { label: '运行', value: 'start' },
    { label: '离线', value: 'stop' }
  ]

  const fields = computed(() => [
    { prop: 'lname', label: '溜井名称', required: true, search: true, minWidth: 150 },
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
      required: true,
      table: false,
      props: deptTreeProps()
    },
    { prop: 'mineLabel', label: '所属工区', form: false, minWidth: 130 },
    { prop: 'shitypename', label: '矿种种类', required: true, minWidth: 130 },
    {
      prop: 'shitype',
      label: '矿种名称',
      type: 'select',
      dictType: 'shitype',
      required: true,
      search: true,
      minWidth: 130
    },
    { prop: 'allQuantity', label: '溜井高度', required: true, minWidth: 110 },
    { prop: 'rad', label: '溜井半径', required: true, minWidth: 110 },
    { prop: 'nowQuantity', label: '当前高度', minWidth: 110 },
    { prop: 'ton', label: '矿石吨数', form: false, minWidth: 110 },
    { prop: 'channel', label: '通道号', minWidth: 100 },
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
