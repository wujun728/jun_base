<template>
  <div class="divBox">
    <div class="main employees-card-bottom">
      <div class="title">战略规划</div>
      <div v-html="content" @click="replayImgShow($event)"></div>
    </div>
    <image-viewer ref="imageViewer" :srcList="srcList"></image-viewer>
  </div>
</template>
<script>
import imageViewer from '@/components/common/imageViewer'
import { getEmployeeTrainApi } from '@/api/config.js'
export default {
  name: '',
  components: {
    imageViewer
  },
  data() {
    return {
      srcList: [],
      content: ''
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
      let type = 'strategic_plan'
      const result = await getEmployeeTrainApi(type)
      this.content = result.data.content
    }
  }
}
</script>
<style scoped lang="scss">
.title {
  text-align: center;
  font-size: 30px;
  font-family: PingFang SC-Medium, PingFang SC;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.85);
  margin-bottom: 40px;
}

.main {
  background: #fff;
  padding: 40px 86px;
  max-width: 1000px;
  margin: 0 auto;
  ::v-deep p img {
    max-width: 800px;
  }
  ::v-deep img {
    cursor: pointer;
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
}
</style>
