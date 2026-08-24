<template>
  <div>
    <el-dialog
      :visible.sync="dialogFormVisible"
      :width="formData.width"
      :modal="true"
      :close-on-click-modal="false"
      custom-class="person"
      :before-close="handleClose"
    >
      <div class="container">
        <div class="logo"><img :src="rolesConfig.logo" alt="" /></div>
        <div class="logo-text">{{ rolesConfig.name }} {{ $t('setting.enterprise04') }}</div>
        <div class="w60">
          <el-button type="primary" @click="handleConfirm">{{ $t('setting.enterprise05') }}</el-button>
        </div>
        <div class="w60">
          <el-button type="text" @click="handleClose">{{ $t('setting.enterprise06') }}</el-button>
        </div>
      </div>
    </el-dialog>
    <el-dialog title="提示" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
      <span>是否需要完善信息？</span>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="goNow">立即前往</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { enterpriseUserJoinApi } from '@/api/user';
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
      dialogVisible: false,
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
      localStorage.removeItem('invitationStorage');
      this.dialogFormVisible = false;
    },
    open() {
      this.dialogFormVisible = true;
    },
    goNow() {
      this.dialogVisible = false;
      this.$router.push({
        name: 'resume',
        path: '/',
      });
    },
    // 提交
    handleConfirm() {
      enterpriseUserJoinApi({
        invitation: this.formData.invitation,
      })
        .then((res) => {
          this.handleClose();
          localStorage.removeItem('enterprise');
          localStorage.setItem('enterprise', JSON.stringify({ entid: this.formData.entId }));
          localStorage.removeItem('invitationStorage');
          if (res.data.perfect == 1) {
            this.dialogVisible = true;
          }
        })
        .catch((error) => {
          localStorage.removeItem('invitationStorage');
          this.handleClose();
        });
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
    margin-top: 10px;
    margin-bottom: 15px;
  }
  .logo {
    text-align: center;

    img {
      background-color: #f2f6fc;
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
