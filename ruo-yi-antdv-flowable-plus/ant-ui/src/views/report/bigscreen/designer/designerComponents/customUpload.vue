<template>
  <div>
    <el-input
      clearable
      v-model.trim="uploadImgUrl"
      size="mini"
      @change="changeInput"
    >
      <template slot="append">
        <i class="iconfont iconfolder-o"></i>
        <input type="file" class="file" ref="files" @change="getImages" />
      </template>
    </el-input>
  </div>
</template>
<script>
import axios from 'axios'
import { getToken } from '@/utils/auth'
export default {
  model: {
    prop: 'value',
    event: 'input'
  },
  props: {
    value: {
      type: undefined,
      default: ''
    }
  },
  data() {
    return {
      requestUrl: process.env.BASE_API + '/file/upload',
      headers: {
        Authorization: getToken()
      },
      fileList: [],
      uploadImgUrl: ''
    }
  },
  created() {
    this.uploadImgUrl = this.value
  },
  methods: {
    getImages(el) {
      var file = el.target.files[0]
      var type = file.type.split('/')[0]
      if (type === 'image') {
        this.upload(file)
      } else {
        this.$message.warn('只能上次图片格式')
      }
    },
    upload(imgUrl) {
      var that = this
      console.log(that.headers)
      var formdata = new FormData()
      formdata.append('file', imgUrl)
      axios
        .post(this.requestUrl, formdata, {
          headers: that.headers
        })
        .then(response => {
          const res = response.data
          if (res.code == '200') {
            that.uploadImgUrl = res.data.urlPath
            that.$emit('input', that.uploadImgUrl)
            that.$emit('change', that.uploadImgUrl)
          }
        })
    },
    changeInput(e) {
      if (e) {
        this.uploadImgUrl = e
      } else {
        this.$refs.files.value = ''
        this.uploadImgUrl = ''
      }
      this.$emit('input', this.uploadImgUrl)
      this.$emit('change', this.uploadImgUrl)
    }
  }
}
</script>
<style lang="scss" scoped>
.file {
  position: absolute;
  width: 100%;
  padding: 100%;
  right: 0;
  top: 0;
  opacity: 0;
}
/deep/.el-input-group__append,
/deep/.el-input-group__prepend {
  padding: 0 10px !important;
  overflow: hidden;
}
.iconfont {
  font-size: 12px;
}
</style>
