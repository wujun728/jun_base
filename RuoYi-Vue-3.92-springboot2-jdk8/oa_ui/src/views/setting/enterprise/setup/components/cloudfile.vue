<template>
  <div class="v-height-flag ml20 mr20" style="min-height: calc(100vh - 200px)">
    <div v-height>
      <el-alert
        class="cr-alert"
        show-icon
        title="「云文件配置」利用WPS平台开放功能实现文档在线预览编辑功能，接入WPS平台服务，获取APPID和APPKEY，填入即可。"
        type="info"
      ></el-alert>
      <el-form ref="form" :model="form" :rules="rules" class="mt20" label-width="150px">
        <el-row>
          <el-col :span="14">
            <el-form-item label="编辑类型：" prop="wps_type">
              <el-radio-group v-model="form.wps_type">
                <el-radio :label="0">WPS</el-radio>
                <el-radio :label="1">系统自带</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <template v-if="form.wps_type == 0">
          <el-row>
            <el-col :span="14">
              <el-form-item label="APPID：" prop="wps_appid">
                <el-input v-model="form.wps_appid" placeholder="请输入APPID（从WPS平台申请）" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-button class="ml14 copy-data" type="text"
                ><a href="https://developer.kdocs.cn/" target="_blank">进入WPS平台 </a></el-button
              >
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="14">
              <el-form-item label="APPKEY：" prop="wps_appkey">
                <el-input v-model="form.wps_appkey" placeholder="请输入APPKEY（从WPS平台申请）" size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="form-item">
            <el-col :span="14">
              <el-form-item label="应用地址：" prop="address">
                <el-input v-model="form.address" :disabled="true" placeholder="请输入网站地址" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-button :data-clipboard-text="form.address" class="ml14 copy-data" type="text" @click="copy"
                >复制地址</el-button
              >
            </el-col>
          </el-row>

          <el-form-item>
            请将应用地址粘贴至 WPS在线平台编辑应用服务 [数据回调地址] 中，操作详情请参考帮助文档
          </el-form-item>
        </template>
        <el-form-item>
          <el-button :loading="loading" size="small" type="primary" @click="submitForm()">立即提交</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import ClipboardJS from 'clipboard'
import { cloudFileSetupApi } from '@/api/config'
import { configUpdateDataApi } from '@/api/setting'
import { appConfigApi } from '@/api/public'

export default {
  name: 'CloudFile',
  components: {},
  props: {
    rule: {
      type: Array,
      default: () => {
        ;[]
      }
    }
  },
  data() {
    return {
      form: {
        wps_type: 0,
        wps_appid: '',
        wps_appkey: '',
        address: location.protocol + '//' + location.host
      },
      rules: {
        wps_type: [{ required: true, message: '请输入wps_type', trigger: 'change' }],
        wps_appid: [{ required: true, message: '请输入APPID', trigger: 'blur' }],
        wps_appkey: [{ required: true, message: '请输入APPKEY', trigger: 'blur' }],
        address: [{ required: true, message: '请输入网站地址', trigger: 'blur' }]
      },
      loading: false
    }
  },
  watch: {
    rule: {
      handler(val) {
        if (val.length) {
          val.map((e) => {
            this.form[e.field] = e.value
          })
        }
      }
    }
  },
  mounted() {
    this.getConfigData()
  },
  methods: {
    submitForm() {
      let data
      if (this.form.wps_type == 1) {
        data = {
          wps_type: this.form.wps_type
        }
      } else {
        this.$refs.form.validate((valid) => {
          if (valid) {
            data = {
              wps_type: this.form.wps_type,
              wps_appid: this.form.wps_appid,
              wps_appkey: this.form.wps_appkey
            }
          }
        })
      }
      this.setup('wps_config', data)
    },
    copy() {
      this.$nextTick(() => {
        const clipboard = new ClipboardJS('.copy-data')
        clipboard.on('success', () => {
          this.$message.success(this.$t('setting.copytitle'))
          clipboard.destroy()
        })
      })
    },
    getConfigData() {
      // configUpdateDataApi({ cate_id: 4 }).then((res) => {
      //   const data = res.data.rule
      //   this.form.appId = data[0].value
      //   this.form.appKey = data[1].value
      // })
    },
    setup(id, data) {
      this.loading = true
      cloudFileSetupApi(id, data)
        .then((res) => {
          this.getConfig()
          this.loading = false
          // this.fromData = res.data;
        })
        .catch((err) => {
          this.loading = false
        })
    },
    getConfig() {
      appConfigApi().then((res) => {
        localStorage.setItem('isWebConfig', res.data.global_watermark_status)
        localStorage.setItem('webConfig', JSON.stringify(res.data))
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.form-item {
  ::v-deep .el-form-item {
    margin-bottom: 5px;
  }
}
</style>
