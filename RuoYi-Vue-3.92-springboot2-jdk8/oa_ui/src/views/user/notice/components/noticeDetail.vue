<template>
  <div>
    <div class="v-height-flag">
      <el-card :body-style="{ padding: '20px' }">
        <el-row>
          <el-col>
            <el-page-header content="文章详情" @click.native="handleEmit">
              <div slot="title">返回</div>
            </el-page-header>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="mt14 employees-card">
        <div class="card-box" style="margin-top: 20px">
          <div>
            <div class="main">
              <div class="header">
                <!-- 标题 -->
                <div class="futitle">{{ data.title }}</div>
                <span class="futitle-time">
                  <i class="iconfont iconriqishijian"></i>
                  {{ data.time }}
                </span>
                <span class="futitle-time">
                  <i class="iconfont iconyiyuedu"></i>
                  {{ data.visit }}
                </span>
              </div>
              <div class="content">
                <div v-html="data.content" @click="replayImgShow($event)"></div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
    <image-viewer ref="imageViewer" :srcList="srcList"></image-viewer>
  </div>
</template>

<script>
export default {
  name: 'NoticeDetail',
  components: {
    imageViewer: () => import('@/components/common/imageViewer')
  },
  props: {
    data: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      srcList: []
    }
  },
  methods: {
    // 富文本查看图片
    replayImgShow(e) {
      if (e.target.tagName === 'IMG') {
        this.srcList = [e.target.currentSrc]
        this.$refs.imageViewer.openImageViewer(e.target.currentSrc)
      }
    },
    handleEmit() {
      this.$emit('handleNoticeDetail')
    }
  }
}
</script>

<style lang="scss" scoped>
.card-box {
  height: calc(100vh - 210px);
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */
  overflow-y: auto;
}

.header {
  margin-bottom: 15px;
  .futitle {
    font-size: 30px;
    font-family: PingFang SC-中黑体, PingFang SC;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.85);
    margin-bottom: 15px;
  }
}
.main {
  max-width: 800px;
  margin: 0 auto;
}
.card-padding {
  padding-top: 40px;
  padding-bottom: 40px;
}
.futitle-time {
  font-size: 14px;
  font-family: PingFang SC-常规体, PingFang SC;
  margin-bottom: 20px;

  color: #999999;
  .iconyiyuedu {
    margin-left: 15px;
  }
}
.p60 {
  padding: 40px 60px;
}
::v-deep .el-page-header__content {
  font-size: 15px;
  font-weight: 600;
}
::v-deep .el-page-header__left::after {
  position: absolute;
  right: -10px;
}

::v-deep .el-page-header__left {
  margin-right: 20px;
}

.content {
  color: #333;
  font-size: 16px;
  line-height: 24px;
  margin-top: 30px;
  margin-bottom: 20px;
  word-break: break-all;
  ::v-deep img {
    cursor: pointer;
  }
  ::v-deep table {
    width: 100%;
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
  ::v-deep p {
    margin: 0;
    padding: 0;
  }
}
.employees-card {
  min-height: calc(100vh - 208px);
}
</style>
