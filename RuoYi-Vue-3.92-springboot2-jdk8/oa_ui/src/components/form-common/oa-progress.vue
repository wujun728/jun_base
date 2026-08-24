<!-- @FileDescription: 上传进度条组件 -->
<template>
  <div>
    <el-progress
      v-if="progressValue != 0"
      type="circle"
      :width="26"
      :stroke-width="3"
      :percentage="progressValue"
      :show-text="false"
      :key="1"
    ></el-progress>
  </div>
</template>
<script>
export default {
  name: 'progress-bar',

  props: {
    // 上传进度
    uploadStatus: {
      type: String,
      require: true
    },

    // 文件大小
    fileSize: {
      type: Number,
      require: true
    }
  },

  data() {
    return {
      progressValue: 0
    }
  },

  computed: {
    progressBarSetp() {
      const val = Math.ceil(this.fileSize / 1024 / 1024)

      return val < 10 ? 5 : 1
    },

    progressBarTimer() {
      return Math.ceil(this.fileSize / 1024 / 1024)
    }
  },

  methods: {
    start() {
      clearInterval(this.timer)

      this.timer = setInterval(() => {
        this.progressValue += this.progressBarSetp

        // 如果上传到了98, 接口还没完成就卡主进度条
        if (this.progressValue === 98 && this.uploadStatus !== 'success') {
          clearInterval(this.timer)
        }

        if (this.progressValue >= 100) {
          clearInterval(this.timer)
        }

        this.$emit('update', this.progressValue)
      }, this.progressBarTimer)
    },

    clear() {
      clearInterval(this.timer)
      this.progressValue = 0
    }
  },

  watch: {
    uploadStatus: {
      handler(type) {
        if (type) {
          const map = {
            ready: () => this.start(),
            pending: () => this.start(),
            success: () => this.clear(true),
            error: () => this.clear()
          }

          map[type] && map[type]()
        }
      },
      immediate: true
    }
  }
}
</script>
