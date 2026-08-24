<template>
  <div class="station">
    <el-drawer
      :title="formData.title"
      :visible.sync="drawer"
      :direction="direction"
      :modal="true"
      :wrapper-closable="true"
      :before-close="handleClose"
      :size="formData.width"
    >
      <formCreate v-if="rules.length > 0" ref="fc" :option="option" :rule="rules" />
      <div class="button from-foot-btn fix btn-shadow">
        <el-button @click="drawer = false">{{ $t('public.cancel') }}</el-button>
        <el-button type="primary" @click="handleConfirm('ruleForm')">{{ $t('public.ok') }}</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import formCreate from '@form-create/element-ui'
import request from '@/api/request'
import ueditorFrom from '@/components/form-common/oa-wangeditor'
formCreate.component('ueditorFrom', ueditorFrom)
export default {
  name: 'Index',
  components: { formCreate: formCreate.$form() },
  props: {
    title: {
      type: String,
      default: ''
    },
    rolesConfig: {
      type: Array,
      default: () => {
        return []
      }
    },
    formData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    isRequest: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      option: {
        form: {
          labelWidth: '90px',
          labelSuffix: '：'
        },
        submitBtn: false,
        global: {
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
      }, // 表单配置
      rules: []
    }
  },
  watch: {
    rolesConfig: {
      handler(nVal, oVal) {
        this.rules = this.rolesConfig
      },
      deep: true
    }
  },
  methods: {
    handleClose() {
      this.drawer = false
    },
    openBox() {
      this.drawer = true
    },
    // 提交
    handleConfirm() {
      this.$refs.fc.$f.submit((formDatas, fApi) => {
        if (this.formData.hasOwnProperty('options')) {
          formDatas = Object.assign(formDatas, this.formData.options)
        }
        if (this.isRequest) {
          request[this.formData.method.toLowerCase()](this.formData.action, formDatas).then((res) => {
            this.$emit('isOk')
            this.drawer = false
          })
        } else {
          this.$emit('changData', formDatas)
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.station ::v-deep.edui-editor-iframeholder {
  height: 300px !important;
}
.station ::v-deep .el-textarea__inner {
  height: 120px;
  resize: none;
}
.station ::v-deep.el-drawer__body {
  padding: 20px 20px 50px 20px;
}
.button ::v-deep.el-button {
  line-height: 0.3;
}
::v-deep.tips {
  font-size: 13px;
  color: #999;
  margin-bottom: 20px;
  margin-left: 75px;
}
.dialog-footer {
  padding-top: 20px;
  border-top: 1px solid #e6ebf5;
  text-align: right;
}
::v-deep.el-cascader {
  width: 100%;
}
</style>
