<template>
  <div class="hy-code">
    <pre class="bg">
      <code :class="'language'+language" v-html="highlightedCode" />
    </pre>
  </div>
</template>

<script setup>
import { ref, watchEffect } from 'vue'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
const props = defineProps({
  language: {
    type: String,
    default: 'html',
  },
  code: {
    type: String,
    default: '',
  },
})
const highlightedCode = ref('')
watchEffect(() => {
  highlightedCode.value = hljs.highlight(props.code, {
    language: props.language,
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
