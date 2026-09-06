<template>
  <div class="box-border w-full h-full"
    ><iframe
      :src="url"
      frameborder="0"
      class="w-full h-full min-h-[calc(100vh-120px)] border-none"
    ></iframe
  ></div>
</template>
<script setup lang="ts">
  defineProps<{ url: string }>()
</script>
