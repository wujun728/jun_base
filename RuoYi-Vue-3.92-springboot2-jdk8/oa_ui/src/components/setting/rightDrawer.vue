<!-- @FileDescription: 系统-新增菜单动态表单 -->
<template>
  <div>
    <el-drawer
      :title="config.title"
      :visible.sync="drawer"
      :direction="direction"
      size="700px"
      :destroy-on-close="true"
      :before-close="handleClose"
    >
      <div class="p14">
        <form-create v-if="FromData" ref="fc" :option="option" :rule="rules" class="mt14" />
      </div>

      <div class="from-foot-btn fix btn-shadow">
        <el-button size="small" @click="drawer = false">取消</el-button>
        <el-button size="small" type="primary" @click="onSubmit">保存</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import formCreate from '@form-create/element-ui'
import request from '@/api/request'
export default {
  name: 'RightDrawer',
  components: { formCreate: formCreate.$form() },
  props: {
    config: {
      type: Object,
      default: function () {
        return {}
      }
    },
    modal: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      rules: [],
      option: {
        form: {
          labelWidth: '125px',
          labelSuffix: '：'
        },
        submitBtn: false,
        global: {
          upload: {
            props: {
              onSuccess(res, file) {
                if (res.status === 200) {
                  file.url = res.data.src
                } else {
                  this.$Message.error(res.msg)
                }
              }
            }
          },
          frame: {
            props: {
              closeBtn: false,
              okBtn: false,
              onLoad: (e) => {
                e.fApi = this.$refs.fc.$f
              }
            }
          }
        }
      },
      FromData: null
    }
  },
  mounted() {},
  methods: {
    handelOpen() {
      this.drawer = true
      this.getFormApi()
    },
    handleClose(done) {
      done()
    },
    getFormApi() {
      const data = {}
      if (this.config.type == 1) {
        data.entid = 1
      }
      request[this.config.method || 'get'](this.config.api, data)
        .then((res) => {
          this.FromData = res.data
          this.rules = res.data.rule
        })
        .catch((err) => {
          this.$message.error(err.message || '获取失败')
        })
    },
    onSubmit() {
      this.$refs.fc.$f.submit((formData) => {
        request[this.FromData.method.toLowerCase()](this.FromData.action.slice(7), formData).then(() => {
          this.$store.dispatch('user/getMenus')
          this.$emit('changge')
          this.drawer = false
        })
      })
    }
  }
}
</script>

<style scoped lang="scss">
.p14 {
  padding-right: 14px;
}
</style>
