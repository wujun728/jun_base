<!-- @FileDescription: 公共-全局-新增/编辑表单dialog弹窗页面 -->
<template>
  <div class="oa-dialog">
    <el-dialog
      top="10%"
      :visible.sync="show"
      :width="fromData.width || '500px'"
      :show-close="false"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <div slot="title" class="header">
        <span class="title">{{ fromData.title }} </span>
        <span class="el-icon-close" @click="handleClose"></span>
      </div>
      <div v-if="fromData.type == 'slot'">
        <slot></slot>
      </div>

      <div class="invite" v-else>
        <oaForm
          ref="oaForm"
          :fromData="fromData"
          :formConfig="formConfig"
          :formDataInit="formDataInit"
          :formRules="formRules"
          @submit="submitOk"
        ></oaForm>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleClose">取消</el-button>
        <el-button size="small" type="primary" @click="submit">{{ fromData.btnText || '确定' }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import oaForm from '@/components/form-common/oa-form'
import selectMember from '@/components/form-common/select-member'

export default {
  name: 'CrmebOaEntIndex',
  components: {
    oaForm,
    selectMember
  },
  props: {
    // 弹窗样式
    fromData: {
      type: Object,
      default: () => {}
    },
    // 表单内容
    formConfig: {
      type: Array,
      default: () => []
    },
    // 表单绑定值
    formDataInit: {
      type: Object,
      default: () => {}
    },
    // 表单规则
    formRules: {
      type: Object,
      default: () => {}
    }
  },

  data() {
    return {
      show: false,
      loading: '',
      form: this.formDataInit,
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now()
        }
      }
    }
  },

  methods: {
    openBox() {
      this.show = true
    },

    submitOk(form) {
      this.$emit('submit', form, this.fromData.type)
    },

    submit() {
      if (this.fromData.type == 'slot') {
        this.$emit('submit')
      } else {
        this.$refs.oaForm.submit()
      }
    },

    handleClose() {
      if (this.fromData.type !== 'slot') {
        this.$refs.oaForm.closeFn()
      }
      this.$emit('handleClose')

      this.show = false
    }
  }
}
</script>

<style lang="scss" scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between !important;
  font-size: 14px !important;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #303133;
}
.oa-dialog {
  .title {
    font-size: 14px !important;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #303133;
  }
  .el-icon-close {
    color: #c0c4cc;
    font-weight: 500;
    font-size: 14px;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
  }
  ::v-deep.el-dialog {
    border-radius: 6px;
  }
  ::v-deep .el-dialog__footer {
    padding-top: 0;
  }
  ::v-deep .el-button--medium {
    padding: 10px 15px;
  }
}

::v-deep.el-dialog__body {
  padding: 30px 20px 10px;
}
</style>
