<template>
  <div class="station">
    <!--设置模板内容-->
    <el-dialog
      :title="$t('access.settemplate')"
      :visible.sync="dialogTemplate"
      width="560px"
      :modal="true"
      :close-on-click-modal="false"
      :before-close="cancelTemplate"
    >
      <el-form ref="form" :model="info" :rules="rules" label-width="90px">
        <el-form-item prop="name">
          <span slot="label">{{ $t('access.templatename') }}：</span>
          <el-input v-model="info.name" type="text" />
        </el-form-item>
        <el-form-item prop="info">
          <span slot="label">{{ $t('access.templatecontent') }}：</span>
          <el-input v-model="info.info" type="text" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelTemplate">{{ $t('public.cancel') }}</el-button>
        <el-button :loading="loading" type="primary" @click="handleConfirm">{{ $t('public.ok') }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Index',
  components: {},
  props: {
    loading: {
      type: Boolean,
      default: false
    },
    setTemplate: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  watch: {
    setTemplate: {
      handler(nVal) {
        if (nVal.success === 1) {
          this.cancelTemplate()
          nVal.success = 0
        }
      },
      deep: true
    }
  },
  data() {
    return {
      dialogTemplate: false,
      info: {
        name: '',
        info: '',
        cate_id: ''
      },
      tempLoading: false,
      rules: {
        name: [{ required: true, message: this.$t('access.placeholder05'), trigger: 'blur' }],
        info: [{ required: true, message: this.$t('access.placeholder06'), trigger: 'blur' }]
      }
    }
  },
  methods: {
    openBox() {
      this.dialogTemplate = true
    },
    cancelTemplate() {
      this.dialogTemplate = false
      this.info.name = ''
      this.info.info = ''
      this.info.cate_id = ''
    },
    // 提交
    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$emit('handleTemplate', this.info)
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.station ::v-deep.el-drawer__body {
  padding: 20px 20px 50px 20px;
}
.button ::v-deep.el-button {
  line-height: 0.3;
}
.dialog-footer {
  text-align: right;
}
</style>
