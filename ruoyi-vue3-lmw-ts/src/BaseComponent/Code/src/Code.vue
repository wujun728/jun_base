<template>
  <div class="hy-code">
    <pre class="bg">
      <code :class="'language'+language" v-html="highlightedCode" />
    </pre>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from 'vue'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
type Props = {
  language: string
  code: ''
}
const { language = 'html', code = '' } = defineProps<Props>()
const highlightedCode = ref('')
watchEffect(() => {
  highlightedCode.value = hljs.highlight(code, {
    language: language,
  }).value
})
</script>

<style scoped>
.bg {
  padding: 10px;
  text-align: left;
  background: #f0f0f0;
}
</style>
