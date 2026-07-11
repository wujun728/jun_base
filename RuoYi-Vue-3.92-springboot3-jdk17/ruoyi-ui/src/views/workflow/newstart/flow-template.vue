<template>
  <div class="container" ref="container">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: "FlowTemplate",
  props: {
    show: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      height: 0,
      heightItem: 87,
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.height = this.$refs.container.offsetHeight;
      this.$refs.container.style.height = this.show ? `${this.height}px` : 0;
      this.$refs.container.style.transition = `height ${Math.floor(this.height / this.heightItem) / 10}s linear`;
    });
  },
  watch: {
    show: {
      handler(val) {
        this.$refs.container.style.height = val ? `${this.height}px` : 0;
      },
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  overflow: hidden;
  margin-bottom: 32px;
}
</style>
