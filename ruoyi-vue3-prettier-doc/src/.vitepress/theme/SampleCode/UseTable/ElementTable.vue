<template>
  <div class="demo-content vp-raw">
    <BaseTable
      :dataList="tableData"
      :listCount="listCount"
      v-bind="tableConfig"
    >
    </BaseTable>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
const tableData = ref([
  {
    name: '张三',
    age: 18,
    address: '北京',
  },
  {
    name: '李四',
    age: 20,
    address: '上海',
  },
  {
    name: '王五',
    age: 22,
    address: '广州',
  },
])
const listCount = computed(() => {
  return tableData.value.length
})
const tableConfig = reactive({
  tableItem: [
    {
      label: '姓名',
      prop: 'name',
      align: 'left',
      width: 100,
      headerAlign: 'right',
    },
    {
      label: '年龄',
      prop: 'age',
    },
    {
      label: '地址',
      prop: 'address',
    },
  ],
  elTableConfig: {
    stripe: false,
    highlightCurrentRow: true,
  },
  pagination: false,
})
</script>
