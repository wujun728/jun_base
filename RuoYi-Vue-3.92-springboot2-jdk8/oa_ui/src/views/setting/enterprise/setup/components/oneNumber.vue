<template>
  <div class="v-height-flag" style="min-height: calc(100vh - 190px)">
    <el-form ref="form" :model="form" class="mt20" label-width="150px">
      <el-row>
        <el-col :span="14">
          <el-form-item label="一号通AppId：" prop="appId">
            <el-input
              v-model="form.yihaotong_appid"
              placeholder="请输入APPID（从一号通平台申请）"
              show-password
              size="small"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-button class="ml14 copy-data" type="text"
            ><a href="https://api.crmeb.com" target="_blank">进入一号通平台 </a></el-button
          >
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="14">
          <el-form-item label="一号通AppSecret：" prop="appKey">
            <el-input
              v-model="form.yihaotong_appsecret"
              placeholder="请输入APPKEY（从一号通平台申请）"
              show-password
              size="small"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-button :loading="loading" size="small" type="primary" @click="submitForm()">立即提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { cloudFileSetupApi } from '@/api/config'
export default {
  props: {
    fromData: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      loading: false,
      form: { yihaotong_appid: '', yihaotong_appsecret: '' }
    }
  },
  watch: {
    fromData: {
      handler(val) {
        this.fromData.map((item) => {
          if (item.field == 'yihaotong_appid') {
            this.form.yihaotong_appid = item.value
          } else if (item.field == 'yihaotong_appsecret') {
            this.form.yihaotong_appsecret = item.value
          }
        })
      },
      deep: true
    }
  },
  mounted() {},

  methods: {
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.setup('yiht_config', this.form)
        }
      })
    },
    setup(id, data) {
      this.loading = true
      cloudFileSetupApi(id, data)
        .then((res) => {
          this.loading = false
        })
        .catch((err) => {
          this.loading = false
        })
    }
  }
}
</script>

<style lang="scss" scoped></style>
