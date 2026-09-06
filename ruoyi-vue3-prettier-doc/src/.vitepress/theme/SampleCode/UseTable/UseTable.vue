<template>
  <div class="demo-content vp-raw">
    <BaseTable
      :dataList="tableData"
      :listCount="listCount"
      v-bind="tableConfig"
    >
      <template #header>
        <BaseForm v-bind="formConfig" :data="tableConfig">
          <template #alignCustom>
            <el-segmented v-model="tableConfig.align" :options="alignOption" />
          </template>
        </BaseForm>
      </template>
      <template #expand="{ backData }">
        <div>
          <p>姓名：{{ backData.name }}</p>
          <p>年龄：{{ backData.age }}</p>
          <p>地址：{{ backData.address }}</p>
        </div>
      </template>
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
  },
  border: true,
  pagination: true,
  showIndex: true,
  align: 'center',
  showExpand: false,
})

const formConfig = {
  formItems: [
    {
      label: '边框',
      field: 'border',
      type: 'switch',
    },
    {
      label: '分页',
      field: 'pagination',
      type: 'switch',
    },
    {
      label: '序号',
      field: 'showIndex',
      type: 'switch',
    },
    {
      label: '可选中',
      field: 'showChoose',
      type: 'switch',
    },
    {
      label: '可展开',
      field: 'showExpand',
      type: 'switch',
    },
    {
      label: '对齐',
      field: 'align',
      type: 'custom',
      layout: {
        span: 24,
      },
    },
  ],
  colLayout: {
    xl: 4,
    lg: 4,
    md: 6,
    sm: 6,
    xs: 12,
  },
}
const alignOption = ref([
  {
    label: '左对齐',
    value: 'left',
  },
  {
    label: '居中',
    value: 'center',
  },
  {
    label: '右对齐',
    value: 'right',
  },
])
</script>
