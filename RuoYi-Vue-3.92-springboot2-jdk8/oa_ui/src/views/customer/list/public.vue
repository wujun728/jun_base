<template>
  <!-- 公海池客户 -->
  <div>
    <index activeName="1" :types="3"></index>
  </div>
</template>

<script>
import index from './index'
export default {
  name: 'public',
  components: {
    index
  }
}
</script>

<style lang="scss" scoped></style>
