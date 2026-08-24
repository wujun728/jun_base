<template>
  <div>
    <!-- 审批详情 -->
    <div class="ex-content">
      <div class="ex-content-con">
        <div class="acea-row mb20">
          <div class="shu mr10"></div>
          <div class="title">提交审批</div>
        </div>
        <el-form label-width="130px" class="">
          <el-form-item v-for="(item, index) in form.rule" :key="index">
            <div class="label">
              <span class="rule-label">{{ item.label || '--' }}：</span>
              <span v-if="!Array.isArray(item.value)" class="rule-value">{{ item.value || '--' }}</span>
              <div v-else>
                <upload-list :file-list="item.value"></upload-list>
              </div>
            </div>
          </el-form-item>
        </el-form>

        <detail-procecss v-if="examineData.examine != 0" :examine-data="examineData"></detail-procecss>
      </div>
    </div>
  </div>
</template>

<script>
import func from '@/utils/preload'
import { approveApplyEditApi } from '@/api/business'
import uploadList from '@/components/form-common/oa-uploadList'
export default {
  name: 'DetailExamine',
  components: {
    detailProcecss: () => import('@/views/user/examine/components/detailProcecss'),
    uploadList,
    city: () => import('@/components/hr/city')
  },
  props: {
    linkId: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      fapi: null,
      rules: [],
      form: {
        rule: [],
        formData: {},
        loaded: false,
        options: {
          submitBtn: false,
          form: {
            labelWidth: '120px'
          },
          preview: true
        }
      },
      isRequest: true,
      examineData: {}
    }
  },
  beforeCreate() {
    this.$vue.prototype.$func = func
  },
  mounted() {
    const data = { types: 1 }
    this.approveApply(this.linkId, data)
  },

  methods: {
    handleClose() {
      this.$refs.leaveAMessage.textarea = ''
      this.close()
    },
    close() {
      this.drawer = false
    },
    judge(row) {
      return row.card.avatar.includes('https')
    },
    getLabel(options, name) {
      let text = ''
      options.map((item) => {
        if (item.value == name) {
          text = item.label
        }
      })
      return text
    },

    upDate(id) {
      const data = { types: 1 }
      this.form.loaded = false
      this.approveApply(id, data)
    },
    // 获取表单配置
    approveApply(id, data) {
      approveApplyEditApi(id, data).then((res) => {
        this.examineData = res.data
        let rule = []
        const formData = {}
        // res.data.content.forEach((v) => {
        //   rule.push(v.content)
        //   if (v.uniqued) {
        //     formData[v.uniqued] = v.value
        //   }
        // })
        rule = res.data.content
        this.form.rule = rule
        this.form.formData = formData
        this.form.loaded = true
      })
    }
  }
}
</script>

<style scoped lang="scss">
.ex-content {
  width: 100%;
  padding: 20px 0 0 20px;
  height: 100%;
  .ex-content-con {
    padding-right: 30px;
    padding-bottom: 10px;
  }
  ::v-deep.select-item {
    margin-top: 0 !important;
  }
  ::v-deep .el-divider--horizontal {
    margin-top: 0;
    margin-bottom: 30px;
  }
  ::v-deep.el-form-item__label {
    font-size: 13px;
    color: #999999;
    font-weight: normal;
  }
  ::v-deep.el-form-item {
    margin-bottom: 0;
  }
  ::v-deep.el-form-item__content {
    font-size: 13px;
    color: #000000;
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .shu {
    width: 3px;
    height: 16px;
    background: #1890ff;
    display: inline-block;
  }
  .title {
    font-size: 14px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.85);
  }
}
.label {
  display: flex;
  align-items: center;
  .rule-label {
    display: inline-block;
    width: 130px;
    text-align: right;
    color: #606266;
    line-height: 36px;
    padding: 0 12px 0 0;
  }
}
::v-deep.el-form-item__content {
  margin-left: 0 !important;
}
</style>
