<template>
  <div class="demo-content vp-raw">
    <BaseTable :dataList="tableData" v-bind="tableConfig"> </BaseTable>
  </div>
</template>

<script setup>
const tableData = [
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
]
const tableConfig = {
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
  pagination: false,
}
</script>
