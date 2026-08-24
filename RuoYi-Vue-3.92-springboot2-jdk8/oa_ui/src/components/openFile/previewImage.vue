<!-- @FileDescription: 预览图片组件 -->
<template>
  <div>
    <el-image-viewer v-if="isImage" :on-close="closeImageViewer" :url-list="srcList" />
  </div>
</template>

<script>
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
export default {
  name: 'ImageViewer',
  props: {
    srcList: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      isImage: false
    }
  },
  components: {
    ElImageViewer
  },
  methods: {
    openImageViewer(url) {
      this.isImage = true
      if (this.srcList.length > 0) {
        this.srcList.map((value, index) => {
          if (value === url) {
            this.srcList.splice(index, 1)
          }
        })
        this.srcList.unshift(url)
      }
    },
    closeImageViewer() {
      this.isImage = false
    }
  }
}
</script>

<style lang="scss" scoped></style>
