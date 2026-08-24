<template>
  <div class="box">
    <div v-if="!editBtn" class="textarea" style="white-space: pre-wrap">
      {{ htmlMD }}
    </div>
    <el-input
      v-else
      v-model="htmlMD"
      type="textarea"
      :rows="30"
      placeholder="请输入内容"
      style="width: 100%"
    ></el-input>
  </div>
</template>

<script>
import { fileUpload } from '@/api/public'
export default {
  name: '',
  props: {
    url: {
      type: String,
      default: ''
    },
    editBtn: {
      type: Boolean,
      default: false
    },
    fid: {
      type: String,
      default: ''
    },
    file: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      htmlMD: ''
    }
  },

  mounted() {
    this.fetchMarkdownAndConvertToHtml()
  },
  methods: {
    async fetchMarkdownAndConvertToHtml() {
      const response = await fetch(this.$processResourceUrl(this.url));
      this.htmlMD = await response.text()
    },

    save() {
      let data = {
        content: this.htmlMD,
        is_file: 1
      }
      fileUpload(this.fid, this.file.id, data).then((res) => {
        if (res.status === 200) {
          let path = this.$router.history.current.path
          this.$router.push({ path, query: { ...this.$route.query, word_url: res.data.file_url } })
        }
        this.$emit('closeLoading')
      })
    }
  }
}
</script>
<style scoped lang="scss">
.box {
  width: 1000px;
  margin: 0 auto;
  min-height: calc(100vh - 60px);
  padding-top: 30px;
  ::v-deep .el-textarea__inner {
    padding: 30px;
    height: calc(100vh - 95px);
    font-size: 14px;
  }
}
.textarea {
  height: calc(100vh - 95px);
  padding: 20px;
  background: #fff;
  line-height: 48px;
}
</style>
