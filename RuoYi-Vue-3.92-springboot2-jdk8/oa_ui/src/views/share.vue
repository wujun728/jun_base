<template>
  <div class="content">
    <template v-if="!submitSuccess">
      <div class="title">{{ info.crud ? info.crud.table_name : '' }}新增 {{ role_type }}</div>
      <div class="form-box">
        <VFormRender
          ref="preForm"
          v-if="isShow"
          :form-json="importTemplate"
          :form-data="testFormData"
          :preview-state="true"
          :option-data="testOptionData"
          :global-dsv="designerDsv"
        >
        </VFormRender>
      </div>
      <div class="footer">
        <el-button size="small" type="primary" :loading="loading" @click="saveFn">提交</el-button>
      </div>
    </template>
    <default-page v-else :index="20"> </default-page>

    <!-- 滑块验证 -->
    <Verify
      @success="success"
      captchaType="clickWord"
      :imgSize="{ width: '330px', height: '155px' }"
      ref="verify"
    ></Verify>
  </div>
</template>
<script>
import store from '@/store'
import { mapMutations } from 'vuex'
import Verify from '@/components/verifition/Verify'
import VFormRender from '@/components/form-render/index'
import { getModuleQuestionnaireInfoApi, saveModuleFormApi } from '@/api/develop'
import DefaultPage from '@/components/common/defaultPage.vue'
export default {
  name: '',
  components: { VFormRender, Verify, DefaultPage },
  props: {},
  data() {
    return {
      importTemplate: {
        formConfig: {},
        widgetList: []
      },
      isShow: false,
      submitSuccess: false,
      testOptionData: {
        select62173: [
          { label: '01', value: 1 },
          { label: '22', value: 2 },
          { label: '333', value: 3 }
        ],

        select001: [
          { label: '辣椒', value: 1 },
          { label: '菠萝', value: 2 },
          { label: '丑橘子', value: 3 }
        ]
      },
      loading: false,
      unique: '',
      token: '',
      keyName: '',
      role_type: 0,
      info: {},
      testFormData: {}
    }
  },
  computed: {
    designerDsv() {
      return this.globalDsv
    }
  },

  created() {
    this.token = store.getters.token
    this.unique = this.$route.params.id
    this.role_type = this.$route.query.role_type
    this.$store.commit('user/SET_UNIQUE', this.unique)
  },
  mounted() {
    this.getInfo(this.unique)
  },
  methods: {
    ...mapMutations('user', ['SET_UNIQUE']),
    getInfo(id) {
      getModuleQuestionnaireInfoApi(id).then((res) => {
        this.info = res.data
        this.keyName = res.data.crud.table_name_en
        if (res.data.form_info && res.data.form_info.global_options) {
          this.$set(this.importTemplate, 'formConfig', res.data.form_info.global_options)
          this.importTemplate.formConfig.roleType = this.token ? 0 : 1
        }
        this.$set(this.importTemplate, 'widgetList', res.data.form_info.options)
      })

      // 表单样式数据延迟
      setTimeout(() => {
        this.isShow = true
      }, 500)
    },
    // 验证通过
    success(data) {
      let obj = {
        captchaVerification: data.captchaVerification,
        captchaType: 'clickWord'
      }
      this.savaData(obj)
    },
    savaData(obj) {
      this.$refs['preForm']
        .getFormData()
        .then((formData) => {
          if (obj) {
            formData = Object.assign(formData, obj)
          }
          this.loading = true
          saveModuleFormApi(this.keyName, this.unique, formData).then((res) => {
            if (res.status == 200) {
              this.submitSuccess = true
            }
            this.loading = false
          })
        })
        .catch((error) => {
          this.$message.error(error)
          this.loading = false
        })
    },
    saveFn() {
      if (this.token) {
        this.savaData()
      } else {
        this.$refs.verify.show()
      }
    }
  }
}
</script>
<style scoped lang="scss">
.content {
  width: 1000px;
  margin: 0 auto;
  padding: 40px 20px;
  background: #ffffff;
  min-height: 100vh;
  .title {
    text-align: center;
    font-family: PingFang SC, PingFang SC;
    font-weight: 500;
    font-size: 20px;
    color: #1e2128;
  }
  .form-box {
    margin-top: 30px;
  }
  .footer {
    margin-top: 30px;
    display: flex;
    justify-content: center;
  }
  .p10 {
    width: 100%;
    padding: 0 10px;
  }
}
</style>
