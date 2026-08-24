<template>
  <div class="divBox">
    <div class="main employees-card-bottom">
      <div class="title">组织架构图</div>
      <div v-html="content" @click="replayImgShow($event)"></div>
    </div>
    <image-viewer ref="imageViewer" :srcList="srcList"></image-viewer>
  </div>
</template>
<script>
import { getEmployeeTrainApi } from '@/api/config.js'
import imageViewer from '@/components/common/imageViewer'
export default {
  components: { imageViewer },
  name: '',
  data() {
    return {
      content: '',
      srcList: []
    }
  },

  mounted() {
    this.getConent()
  },
  methods: {
    // 富文本查看图片
    replayImgShow(e) {
      if (e.target.tagName === 'IMG') {
        this.srcList = [e.target.currentSrc]
        this.$refs.imageViewer.openImageViewer(e.target.currentSrc)
      }
    },
    async getConent() {
      let type = 'organization_chart'
      const result = await getEmployeeTrainApi(type)
      this.content = result.data.content
    }
  }
}
</script>
<style scoped lang="scss">
.main {
  background: #fff;
  max-width: 1000px;
  margin: 0 auto;
  padding: 40px 86px;
  .title {
    text-align: center;
    font-size: 30px;
    font-family: PingFang SC-Medium, PingFang SC;
    font-weight: 500;
    color: rgba(0, 0, 0, 0.85);
    margin-bottom: 16px;
  }
  ::v-deep table {
    border: 1px solid #ccc;
  }

  ::v-deep table th {
    border: 1px solid #ccc;
  }
  ::v-deep table td {
    padding: 10px 5px;
    border: 1px solid #ccc;
  }

  ::v-deep p img {
    max-width: 800px;
  }
}
::v-deep .wang-editor.w-e-toolbar {
  display: none !important;
}
</style>
