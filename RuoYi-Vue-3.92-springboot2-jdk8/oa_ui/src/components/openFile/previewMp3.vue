<template>
  <div>
    <audio controls style="width: 100%">
      <track kind="captions" />
      <source :src="fileUrl" type="audio/mpeg" />
    </audio>
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
      this.fileUrl = val
    }
  }
}
</script>
<style></style>
