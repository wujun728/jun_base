<template>
  <div>
    <index activeName="2" :types="1"></index>
  </div>
</template>

<script>
export default {
  name: 'IChecked',
  components: {
    index: () => import('./index')
  }
}
</script>

<style lang="scss" scoped></style>
