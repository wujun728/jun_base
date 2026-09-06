<template>
  <div class="demo-content vp-raw">
    <div class="font">数据列表</div>
    <BaseTable
      :dataList="tableData"
      :listCount="listCount"
      v-bind="tableConfig"
      :tableListener="tableListener"
    >
    </BaseTable>
    <div class="mt12">
      <div class="font">已选中列表</div>
      <BaseTable
        :dataList="tableSelect"
        :listCount="tableSelect.length"
        v-bind="tableConfig"
      >
      </BaseTable>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
const tableSelect = ref([])
const tableListener = {
  selectionChange: (row) => {
    tableSelect.value = row
    console.log(row)
  },
}
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
  showChoose: true,
  pagination: false,
})
</script>
