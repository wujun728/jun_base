<template>
  <div style="width: 100%">
    <video ref="{videoRef}" controls muted controlsList="nodownload" style="width: 1200px; height: 650px">
      <track kind="captions" />
      <source :src="fileUrl" type="video/mp4" />
    </video>
  </div>
</template>
<script>
export default {
  name: '',
  props: {
    url: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      fileUrl: this.url
    }
  },
  watch: {
    url(val) {
      this.$nextTick(() => {
        this.fileUrl = val
      })
    }
  }
}
</script>
<style scoped lang="scss"></style>
