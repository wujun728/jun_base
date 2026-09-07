<template>
  <div class="p-6">
    <ImageUpload
      :api="uploadApi"
      @change="handleUpload"
      :accept="['png']"
      :multiple="false"
      :result-field="resultField"
    />
  </div>
</template>

<script setup lang="ts">
  import { ImageUpload } from '@/components/Upload';
  import { uploadApi } from '@/api/upload';
  import { useMessage } from '@/hooks/web/useMessage';
  import { ref } from 'vue';
  /**
   * 图片上传demo
   */
  const { createMessage } = useMessage();

  function handleUpload(e: string[]) {
    console.log(e);
    createMessage.success('上传成功: ' + e[0]);
  }
  /**
   * 回调参数 可选
   * newFileName
   * fileName
   * originalFilename
   * url 默认
   */
  const resultField = ref('url');
</script>
