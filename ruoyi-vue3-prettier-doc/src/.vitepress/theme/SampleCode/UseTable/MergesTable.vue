<template>
  <div class="demo-content vp-raw">
    <BaseTable :dataList="tableData" v-bind="tableConfig">
      <template #provinceSlot="{ backData }">
        自定义省份内容： {{ backData.province }}
      </template>
      <template #provinceSlotHeader> 省份自定义header </template>
      <template #nameSlotHeader> 姓名header </template>
    </BaseTable>
  </div>
</template>

<script setup>
const tableConfig = {
  tableItem: [
    {
      label: '姓名',
      prop: 'name',
      slotName: 'nameSlot',
    },
    {
      label: '个人信息',
      prop: 'info',
      merges: [
        {
          label: '年龄',
          prop: 'age',
        },
        {
          label: '地址',
          prop: 'address',
          merges: [
            {
              prop: 'province',
              label: '省',
              width: 200,
              slotName: 'provinceSlot',
            },
            {
              prop: 'city',
              label: '市',
              width: 105,
              slotName: 'citySlot',
            },
            {
              prop: 'district',
              label: '区',
              width: 105,
            },
          ],
        },
      ],
    },
  ],
  pagination: false,
}
const tableData = [
  {
    name: '张三',
    age: 18,
    province: '浙江省',
    city: '杭州市',
    district: '西湖区',
  },
  {
    name: '李四',
    age: 20,
    province: '江苏省',
    city: '南京市',
    district: '玄武区',
  },
  {
    name: '王五',
    age: 22,
    province: '广东省',
    city: '广州市',
    district: '天河区',
  },
]
</script>
