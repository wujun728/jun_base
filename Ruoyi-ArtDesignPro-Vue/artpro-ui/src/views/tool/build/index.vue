<template>
  <div class="art-full-height"
    ><ElCard
      ><template #header>表单构建</template
      ><ArtForm v-model="form" :items="items" @submit="submit" @reset="reset" /></ElCard
  ></div>
</template>
<script setup lang="ts">
  const form = reactive<Record<string, string>>({ label: '', field: '', placeholder: '' })
  const items = computed(() => [
    { label: '字段标题', key: 'label', type: 'input', props: { placeholder: '请输入字段标题' } },
    { label: '字段名称', key: 'field', type: 'input', props: { placeholder: '请输入字段名称' } },
    {
      label: '占位提示',
      key: 'placeholder',
      type: 'input',
      props: { placeholder: '请输入占位提示' }
    }
  ])
  function submit() {
    ElMessage.success('表单配置已生成')
  }
  function reset() {
    Object.assign(form, { label: '', field: '', placeholder: '' })
  }
</script>
