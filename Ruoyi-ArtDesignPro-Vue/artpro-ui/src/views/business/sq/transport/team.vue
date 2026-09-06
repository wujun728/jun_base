<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="运输管理"
      permission="business:transportTeam"
      id-key="id"
      :fields="fields"
      :list-fn="fetchSqTransportTeamList"
      :get-fn="fetchSqTransportTeam"
      :add-fn="addSqTransportTeam"
      :update-fn="updateSqTransportTeam"
      :remove-fn="removeSqTransportTeam"
      :defaults="{ status: '0', isDeleted: '0' }"
      export-url="/business/sq/transport-team/export"
      drawer-size="760px"
    />
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqTransportTeam,
    fetchSqTransportTeam,
    fetchSqTransportTeamList,
    removeSqTransportTeam,
    updateSqTransportTeam
  } from '@/api/business/sqTransport'
  import { fetchDeptTree } from '@/api/system/user'

  defineOptions({ name: 'BusinessSqTransportTeam' })

  const deptOptions = ref<any[]>([])

  const fields = computed(() => [
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
      search: true,
      table: false,
      props: deptTreeProps()
    },
    { prop: 'mineLabel', label: '所属工区', form: false, minWidth: 130 },
    { prop: 'tnameuser', label: '所属中段', search: true, minWidth: 120 },
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
