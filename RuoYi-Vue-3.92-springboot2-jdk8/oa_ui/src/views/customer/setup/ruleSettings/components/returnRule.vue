<template>
  <div>
    <div class="card-box">
      <el-card :body-style="{ padding: '20px 20px 0' }" class="box-card">
        <div class="setting-container">
          <el-form ref="elForm" :model="formData" :rules="rules" size="medium">
            <div class="card-list">
              <div class="head">
                <span class="line">|</span>
                <span class="title">自动退回公海</span>
              </div>
              <el-form-item label="自动退回公海：" prop="return_high_seas_switch">
                <el-switch
                  v-model="formData.return_high_seas_switch"
                  :active-value="1"
                  :inactive-value="0"
                  active-color="#1890ff"
                  active-text="开启"
                  inactive-color="#909399"
                  inactive-text="关闭"
                >
                </el-switch>
              </el-form-item>
              <template v-if="formData.return_high_seas_switch">
                <el-form-item label="未成交退回：" prop="unsettled_cycle">
                  <el-input
                    v-model="formData.unsettled_cycle"
                    clearable
                    placeholder="请输入正整数"
                    show-word-limit
                    size="small"
                    style="width: 350px"
                    type="number"
                  >
                    <template slot="suffix">天</template>
                  </el-input>
                  <div class="info">用于设置客户在未成交状态滞留多少天自动退回公海</div>
                </el-form-item>
                <el-form-item label="未跟进退回：" prop="unfollowed_cycle">
                  <el-input
                    v-model="formData.unfollowed_cycle"
                    clearable
                    placeholder="请输入正整数"
                    show-word-limit
                    size="small"
                    style="width: 350px"
                    type="number"
                  >
                    <template slot="suffix">天</template>
                  </el-input>
                  <div class="info">用于设置未成交状态的客户，多少天未跟进自动退回公海</div>
                </el-form-item>
                <el-form-item label="退回公海提醒：" prop="advance_cycle">
                  <el-input
                    v-model="formData.advance_cycle"
                    clearable
                    placeholder="请输入正整数"
                    show-word-limit
                    size="small"
                    style="width: 350px"
                    type="number"
                  >
                    <template slot="suffix">天</template>
                  </el-input>
                  <div class="info">用于客户退回公海提前多少天进行提醒</div>
                </el-form-item>
              </template>
            </div>
            <!-- <div class="dash-line"></div> -->
            <div class="card-list mt30">
              <div class="head">
                <span class="line">|</span>
                <span class="title">客户保单规则</span>
              </div>
              <el-form-item label="客户保单规则：" prop="client_policy_switch">
                <el-switch
                  v-model="formData.client_policy_switch"
                  :active-value="1"
                  :inactive-value="0"
                  active-color="#1890ff"
                  active-text="开启"
                  inactive-color="#909399"
                  inactive-text="关闭"
                >
                </el-switch>
              </el-form-item>
              <template v-if="formData.client_policy_switch">
                <el-form-item label="保单数量：" prop="unsettled_client_number">
                  <el-input
                    v-model="formData.unsettled_client_number"
                    clearable
                    placeholder="请输入正整数"
                    show-word-limit
                    size="small"
                    style="width: 350px"
                    type="number"
                  >
                    <template slot="suffix">个</template>
                  </el-input>
                  <div class="info">用于设置每个销售员最多可以拥有多少个暂未成交状态的客户</div>
                </el-form-item>
              </template>
            </div>
            <el-form-item>
              <el-button size="small" type="primary" @click="saveEvt">保存</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </div>
  </div>
</template>
<script>
export default {
  name: 'FollowRules',
  props: {
    formData: {
      default: () => {},
      type: Object
    }
  },
  data() {
    return {
      grid1: {
        xl: 2,
        lg: 4,
        md: 2,
        sm: 24,
        xs: 24
      },
      grid2: {
        xl: 20,
        lg: 18,
        md: 20,
        sm: 24,
        xs: 24
      },
      rules: {
        unsettled_cycle: [{ required: true, message: '请输入退回客户公海周期' }],
        advance_cycle: [{ required: true, validator: this.checkAdvanceCycle }],
        unsettled_client_number: [{ required: true, message: '请输入未成交客户数量' }],
        unfollowed_cycle: [{ required: true, message: '请输入未跟进退回公海数量' }]
      }
    }
  },
  watch: {
    'formData.unsettled_cycle'(newValue, oldValue) {
      if (newValue === '') {
        return
      }
      const numericValue = parseInt(newValue, 10)
      if (isNaN(numericValue)) {
        this.formData.unsettled_cycle = oldValue || ''
      } else {
        this.formData.unsettled_cycle = Math.abs(numericValue).toString()
      }
    },
    'formData.advance_cycle'(newValue, oldValue) {
      if (newValue === '') {
        return
      }
      const numericValue = parseInt(newValue, 10)
      if (isNaN(numericValue)) {
        this.formData.advance_cycle = oldValue || ''
      } else {
        this.formData.advance_cycle = Math.abs(numericValue).toString()
      }
    },
    'formData.unsettled_client_number'(newValue, oldValue) {
      if (newValue === '') {
        return
      }
      const numericValue = parseInt(newValue, 10)
      if (isNaN(numericValue)) {
        this.formData.unsettled_client_number = oldValue || ''
      } else {
        this.formData.unsettled_client_number = Math.abs(numericValue).toString()
      }
    }
  },
  methods: {
    sendReturnData() {
      const data = this.formData
      this.$emit('returnData', data)
    },
    saveEvt() {
      this.$emit('saveEvt')
    },
    checkAdvanceCycle(rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入客户退回公海提醒提前天数'))
      } else if (
        parseInt(value, 10) >= parseInt(this.formData.unsettled_cycle, 10) ||
        parseInt(value, 10) >= parseInt(this.formData.unfollowed_cycle, 10)
      ) {
        callback(new Error('退回公海提醒天数要小于未成交退回天数和未跟进退回天数'))
      } else {
        callback()
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.card-box {
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
  .box-card {
    .setting-container {
      width: 300px;
      margin: 0 auto;
      // padding-bottom: 20px;
      .head {
        display: flex;
        margin-bottom: 20px;
        margin-left: -8px;
        .title {
          padding-left: 6px;
          font-size: 14px;
          font-weight: 600;
          position: relative;
        }
        .line {
          width: 3px;
          background-color: #1890ff;
          color: #1890ff;
        }
      }
      .card-list {
        border-spacing: 2px;
        .info {
          margin-top: 10px;
          font-size: 13px;
          line-height: 18px;
          color: #909399;
        }
      }
      .dash-line {
        // width: 100%;
        // height: 1px;
        // background-image: linear-gradient(to right, #dcdfe6 0%, #dcdfe6 50%, transparent 50%);
        // background-size: 12px 0.5px; //第一个值（20px）越大线条越长间隙越大
        // background-repeat: repeat-x;
        // margin-top: 20px;
      }
    }
  }
}
::v-deep.el-card.is-always-shadow {
  border: none;
}
::v-deep.el-form-item__label {
  line-height: 36px;
}
.el-form-item::before,
.el-form-item::after {
  display: none;
}
::v-deep.el-form-item__content::before,
::v-deep.el-form-item__content::after {
  display: none;
}
</style>
