<template>
  <div class="demo-content">
    <BaseForm :data="formData" v-bind="formConfig">
      <template #urlPrepend> Http:// </template>
      <template #urlAppend>.com</template>
      <template #colorDefaultOption="{ backData }">
        <div class="items-center">
          <el-tag
            class="tag"
            :color="backData.slotData.option.value"
            style="margin-right: 8px"
            size="small"
          />
          <span :style="{ color: backData.slotData.option.value }">{{
            backData.slotData.option.label
          }}</span>
        </div>
      </template>
      <template #colorTag="{ backData }">
        <el-tag
          class="tag"
          v-for="color in backData.formData.color"
          :key="color"
          :color="color"
        />
      </template>
    </BaseForm>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const formConfig = {
  formItems: [
    {
      field: 'url',
      label: '网址',
      type: 'input',
      slotNames: ['prepend', 'append'],
    },
    {
      field: 'color',
      label: '颜色',
      type: 'select',
      optionSlots: ['default'],
      slotNames: ['tag'],
      config: {
        placeholder: '颜色可多选',
        multiple: true,
      },
      options: [
        {
          value: '#E63415',
          label: 'red',
        },
        {
          value: '#FF6600',
          label: 'orange',
        },
        {
          value: '#1EC79D',
          label: 'green',
        },
        {
          value: '#14CCCC',
          label: 'cyan',
        },
        {
          value: '#4167F0',
          label: 'blue',
        },
        {
          value: '#6222C9',
          label: 'purple',
        },
      ],
    },
  ],
  colLayout: {
    xl: 24,
    lg: 24,
    md: 24,
    sm: 24,
    xs: 24,
  },
}
const formData = ref({
  url: '',
  color: [],
})
</script>
<style lang="scss" scoped>
.items-center {
  display: flex;
  align-items: center;
}
.tag {
  border: none;
  aspect-ratio: 1;
}
</style>
