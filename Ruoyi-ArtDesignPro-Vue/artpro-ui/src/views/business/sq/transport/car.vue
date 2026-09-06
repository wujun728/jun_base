<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="车辆管理"
      permission="business:transportCar"
      id-key="id"
      :fields="fields"
      :list-fn="fetchSqTransportCarList"
      :get-fn="fetchSqTransportCar"
      :add-fn="addSqTransportCar"
      :update-fn="updateSqTransportCar"
      :remove-fn="removeSqTransportCar"
      :defaults="{ status: '0', isDeleted: '0' }"
      export-url="/business/sq/transport-car/export"
      drawer-size="860px"
    />
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqTransportCar,
    fetchSqTransportCar,
    fetchSqTransportCarList,
    fetchSqTransportTeamOptions,
    removeSqTransportCar,
    updateSqTransportCar
  } from '@/api/business/sqTransport'
  import { fetchDeptTree } from '@/api/system/user'

  defineOptions({ name: 'BusinessSqTransportCar' })

  const deptOptions = ref<any[]>([])
  const teamOptions = ref<Array<{ label: string; value: string }>>([])

  const fields = computed(() => [
    { prop: 'carsNum', label: '车辆编号', required: true, search: true, minWidth: 120 },
    {
      prop: 'ctype',
      label: '车辆类型',
      type: 'select',
      dictType: 'ctype',
      search: true,
      minWidth: 110
    },
    { prop: 'rfid', label: 'RFID标签', search: true, minWidth: 130 },
    { prop: 'carsXh', label: '车辆型号', search: true, minWidth: 140 },
    {
      prop: 'carsComp',
      label: '所属单位',
      type: 'treeselect',
      search: true,
      table: false,
      props: deptTreeProps()
    },
    { prop: 'carsCompLabel', label: '所属单位', form: false, minWidth: 130 },
    {
      prop: 'mine',
      label: '所属工区',
      type: 'treeselect',
      search: true,
      table: false,
      props: deptTreeProps()
    },
    { prop: 'mineLabel', label: '所属工区', form: false, minWidth: 130 },
    {
      prop: 'teamid',
      label: '运输管理',
      type: 'select',
      options: teamOptions.value,
      required: true,
      search: true,
      table: false
    },
    { prop: 'teamName', label: '运输管理', form: false, minWidth: 180 },
    { prop: 'carsW', label: '车辆载重(t)', minWidth: 120 },
    { prop: 'volume', label: '满载体积(m³)', minWidth: 130 },
    { prop: 'color', label: '色条', minWidth: 100 },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 160 },
    { prop: 'updateTime', label: '更新时间', form: false, minWidth: 160 }
  ])

  onMounted(async () => {
    const [deptRows, teamRows] = await Promise.all([fetchDeptTree(), fetchSqTransportTeamOptions()])
    deptOptions.value = deptRows
    teamOptions.value = teamRows.map((team) => ({
      label: [team.tnameLabel, team.mineLabel, team.tnameuser].filter(Boolean).join('/'),
      value: String(team.id)
    }))
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
