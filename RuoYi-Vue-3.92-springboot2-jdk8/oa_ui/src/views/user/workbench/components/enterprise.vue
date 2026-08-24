<template>
  <div>
    <el-dialog
      :visible.sync="dialogFormVisible"
      :width="formData.width"
      :modal="true"
      :close-on-click-modal="false"
      custom-class="person"
      :show-close="false"
      :before-close="handleClose"
    >
      <div class="container">
        <div class="logo"><img :src="rolesConfig.url" alt="" /></div>
        <div class="logo-text">{{ rolesConfig.name }}</div>
        <div class="logo-reason">{{ rolesConfig.reason }}</div>
        <div v-if="rolesConfig.tab" class="w60" @click="handleTab">
          <el-button type="success">{{ $t('navbar.tabenterprise') }}</el-button>
        </div>
        <div class="w60">
          <el-button type="primary" @click="handleConfirm">{{ $t('setting.enterprise07') }}</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'DialogForm',
  components: {},
  props: {
    title: {
      type: String,
      default: '',
    },
    rolesConfig: {
      type: Object,
      default: () => {
        return {};
      },
    },
    formData: {
      type: Object,
      default: () => {
        return {};
      },
    },
    isRequest: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      dialogFormVisible: false,
      tableFrom: {
        type: '',
        id: '',
      },
      option: {
        form: {
          labelWidth: '75px',
        },
        submitBtn: false,
        global: {
          frame: {
            props: {
              closeBtn: false,
              okBtn: false,
              onLoad: (e) => {
                e.fApi = this.$refs.fc.$f;
              },
            },
          },
        },
      }, // 表单配置
      rules: [],
    };
  },
  watch: {
    rolesConfig: {
      handler(nVal, oVal) {
        this.rules = this.rolesConfig;
      },
      deep: true,
    },
  },
  methods: {
    handleClose() {
      this.dialogFormVisible = false;
    },
    open() {
      this.dialogFormVisible = true;
    },
    // 提交
    handleConfirm() {
      this.tableFrom.type = 1;
      this.tableFrom.id = this.rolesConfig.entId;
      this.$emit('isEnterprise', this.tableFrom);
      this.handleClose();
    },
    handleTab() {
      this.tableFrom.type = 2;
      this.tableFrom.id = this.rolesConfig.entId;
      this.$emit('isEnterprise', this.tableFrom);
      this.handleClose();
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep.tips {
  font-size: 13px;
  color: #999;
  margin-bottom: 20px;
  margin-left: 75px;
}
.container {
  div {
    margin-bottom: 15px;
  }
  .logo {
    text-align: center;
    img {
      width: 96px;
      height: 96px;
      border-radius: 50%;
    }
  }
  .logo-text {
    font-size: 19px;
    color: #000000;
    text-align: center;
  }
  .logo-reason {
    margin: 15px 0;
    font-size: 15px;
    color: #999;
    text-align: center;
  }
  .w60 {
    width: 60%;
    margin: 0 auto 10px auto;
    text-align: center;
    ::v-deep .el-button--medium {
      width: 100%;
    }
  }
}
::v-deep .el-dialog__wrapper {
  background-color: rgba(0, 0, 0, 0.6);
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
