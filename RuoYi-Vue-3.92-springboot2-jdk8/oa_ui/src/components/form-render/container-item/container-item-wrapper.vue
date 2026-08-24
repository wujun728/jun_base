<template>
  <div class="container-wrapper" :class="[customClass]">
    <slot></slot>
  </div>
</template>

<script>
  export default {
    name: "container-item-wrapper",
    props: {
      widget: {
        type: Object,
        required: true
      },
    },
    computed: {
      customClass() {
        return !!this.widget.options.customClass ? this.widget.options.customClass.join(' ') : ''
      },

    }
  }
</script>

<style lang="scss" scoped>

</style>
