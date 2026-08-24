<template>
  <div class="xmind-preview-container" ref="container" />
</template>

<script>
import { XMindEmbedViewer } from "xmind-embed-viewer";

export default {
  name: 'previewXmind',
  props: {
    url: String
  },
  async mounted() {
    const fetchTaskPromise = this.getXmindFileArrayBuffer();
    const viewer = new XMindEmbedViewer({
      el: this.$refs.container,
      region: 'cn',
      styles: {
        width: "100%",
        height: "100%"
      }
    });

    const xmindArrayBuffer = await fetchTaskPromise;
    viewer.load(xmindArrayBuffer);
  },
  methods: {
    async getXmindFileArrayBuffer() {
      const response = await fetch(this.$processResourceUrl(this.url))
      return await response.arrayBuffer();
    },
  }
}
</script>

<style scoped lang="scss">
.xmind-preview-container {
  height: calc(100vh - 60px);
}
</style>
