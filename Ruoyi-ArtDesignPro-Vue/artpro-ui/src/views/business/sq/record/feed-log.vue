<template>
  <div class="art-full-height">
    <SystemCrudPage
      title="入料口记录"
      permission="business:feedLog"
      id-key="id"
      :fields="fields"
      :list-fn="fetchSqFeedLogList"
      :get-fn="fetchSqFeedLog"
      :add-fn="addSqFeedLog"
      :update-fn="updateSqFeedLog"
      :remove-fn="removeSqFeedLog"
      :defaults="{ isDeleted: '0', yiwu: '无', bigs: '无', cars: '自卸车' }"
      export-url="/business/sq/feed-log/export"
      drawer-size="900px"
    />
  </div>
</template>

<script setup lang="ts">
  import SystemCrudPage from '@/components/business/system-crud-page/index.vue'
  import {
    addSqFeedLog,
    fetchSqChuteOptions,
    fetchSqFeedLog,
    fetchSqFeedLogList,
    removeSqFeedLog,
    updateSqFeedLog
  } from '@/api/business/sqRecord'

  defineOptions({ name: 'BusinessSqFeedLog' })

  const chuteOptions = ref<Array<{ label: string; value: string }>>([])

  const fields = computed(() => [
    {
      prop: 'name',
      label: '溜井',
      type: 'select',
      options: chuteOptions.value,
      search: true,
      table: false
    },
    { prop: 'chuteName', label: '溜井名称', form: false, minWidth: 150 },
    { prop: 'company', label: '所属公司', search: true, minWidth: 120 },
    { prop: 'persons', label: '值班人', minWidth: 110 },
    {
      prop: 'shitype',
      label: '矿种',
      type: 'select',
      dictType: 'shitype',
      search: true,
      minWidth: 110
    },
    { prop: 'cars', label: '车辆编号', search: true, minWidth: 120 },
    { prop: 'fulls', label: '满车', type: 'select', dictType: 'flag', search: true, width: 90 },
    { prop: 'bigs', label: '巨石', type: 'select', dictType: 'bigs', search: true, width: 90 },
    { prop: 'yiwu', label: '异物', type: 'select', dictType: 'yiwu', search: true, width: 90 },
    { prop: 'forkcar', label: '卡车', minWidth: 100 },
    { prop: 'rate', label: '装载率', minWidth: 100 },
    { prop: 'timecha', label: '逗留时间', minWidth: 120 },
    { prop: 'videopath', label: '视频地址', span: 24, minWidth: 160 },
    { prop: 'imgpath', label: '图片地址', span: 24, minWidth: 160 },
    {
      prop: 'beginTime',
      label: '开始时间',
      type: 'datetime',
      form: false,
      search: true,
      table: false,
      props: dateTimeProps()
    },
    {
      prop: 'endTime',
      label: '结束时间',
      type: 'datetime',
      form: false,
      search: true,
      table: false,
      props: dateTimeProps()
    },
    { prop: 'createTime', label: '创建时间', form: false, minWidth: 160 }
  ])

  onMounted(loadChutes)

  async function loadChutes() {
    const rows = await fetchSqChuteOptions()
    chuteOptions.value = rows.map((item) => ({
      label: item.lname || String(item.id),
      value: String(item.id)
    }))
  }

  function dateTimeProps() {
    return { type: 'datetime', valueFormat: 'YYYY-MM-DD HH:mm:ss' }
  }
</script>
