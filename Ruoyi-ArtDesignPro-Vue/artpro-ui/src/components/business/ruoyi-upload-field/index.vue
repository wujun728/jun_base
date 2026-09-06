<template>
  <div class="flex flex-wrap items-center gap-2">
    <ElImage
      v-if="mode === 'image' && modelValue"
      :src="modelValue"
      :preview-src-list="[modelValue]"
      class="h-20 w-20 rounded border border-g-300"
      fit="cover"
      preview-teleported
    />
    <ElLink v-else-if="modelValue" :href="modelValue" target="_blank" type="primary">
      {{ fileName }}
    </ElLink>
    <ElUpload
      v-if="!disabled"
      :accept="mode === 'image' ? 'image/*' : undefined"
      :http-request="handleUpload"
      :show-file-list="false"
    >
      <ElButton :loading="uploading">{{ modelValue ? '重新上传' : '上传文件' }}</ElButton>
    </ElUpload>
    <ElButton v-if="modelValue && !disabled" type="danger" link @click="modelValue = ''">
      移除
    </ElButton>
  </div>
</template>

<script setup lang="ts">
  import type { UploadRequestOptions } from 'element-plus'
  import { uploadFile } from '@/api/common'

  const props = withDefaults(
    defineProps<{
      mode?: 'image' | 'file'
      disabled?: boolean
    }>(),
    { mode: 'file', disabled: false }
  )
  const modelValue = defineModel<string>({ default: '' })
  const uploading = ref(false)
  const fileName = computed(() => modelValue.value.split('/').pop() || '查看文件')

  async function handleUpload(options: UploadRequestOptions) {
    uploading.value = true
    try {
      const result = await uploadFile(options.file)
      modelValue.value = result.url
      options.onSuccess(result)
    } catch (error) {
      options.onError(error as any)
    } finally {
      uploading.value = false
    }
  }
</script>
