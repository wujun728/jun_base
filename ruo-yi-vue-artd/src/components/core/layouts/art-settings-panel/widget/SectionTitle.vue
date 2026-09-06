<template>
  <p
    class="relative mt-7.5 mb-5.5 text-center text-sm text-g-800 before:absolute before:top-[10px] before:left-0 before:m-auto before:w-[50px] before:border-b before:border-[var(--art-gray-300)] before:content-[''] after:absolute after:top-[10px] after:right-0 after:m-auto after:w-[50px] after:border-b after:border-g-300 after:content-['']"
    :style="style">
    {{ title }}
  </p>
</template>

<script setup lang="ts">
interface Props {
  title: string
  style?: Record<string, any>
}

defineProps<Props>()
</script>
