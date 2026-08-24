<template>
  <div>
    <div class="card-box">
      <el-card :body-style="{ padding: '20px 20px 0' }" class="box-card">
        <div class="setting-container">
          <el-form ref="elForm" :model="formData" :rules="rules" size="medium">
            <div class="card-list">
              <div class="head">
                <span class="line">|</span>
                <span class="title">客户跟进规则</span>
              </div>
              <el-form-item label="客户跟进提醒：" prop="follow_up_switch">
                <el-switch
                  v-model="formData.follow_up_switch"
                  :active-value="1"
                  :inactive-value="0"
                  active-color="#1890ff"
                  active-text="开启"
                  inactive-color="#909399"
                  inactive-text="关闭"
                >
                </el-switch>
              </el-form-item>
              <template v-if="formData.follow_up_switch">
                <el-form-item label="客户状态：" prop="follow_up_status">
                  <el-checkbox-group v-model="formData.follow_up_status" @change="handleCheckChange">
                    <el-checkbox :label="2">未成交</el-checkbox>
                    <el-checkbox :label="1">已成交</el-checkbox>
                    <div class="info">用于设置需要跟进提醒的客户状态</div>
                  </el-checkbox-group>
                </el-form-item>
                <el-form-item v-if="getVal(2)" label="未成交提醒：" prop="follow_up_unsettled">
                  <el-input
                    v-model="formData.follow_up_unsettled"
                    clearable
                    placeholder="请输入正整数"
                    show-word-limit
                    size="small"
                    style="width: 350px"
                    type="number"
                  >
                    <template slot="suffix">天</template>
                  </el-input>
                  <div class="info">用于设置暂未成交状态的客户多少天需要跟进一次</div>
                </el-form-item>
                <el-form-item v-if="getVal(1)" label="已成交提醒：" prop="follow_up_traded">
                  <el-input
                    v-model="formData.follow_up_traded"
                    clearable
                    placeholder="请输入正整数"
                    show-word-limit
                    size="small"
                    style="width: 350px"
                  >
                    <template slot="suffix">天</template>
                  </el-input>
                  <div class="info">用于设置已成交状态的客户多少天需要跟进一次</div>
                </el-form-item>
              </template>
              <el-form-item>
                <el-button size="small" type="primary" @click="saveEvt">保存</el-button>
              </el-form-item>
            </div>
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
        follow_up_unsettled: [{ required: true, message: '请输入客户跟进提醒周期' }],
        follow_up_traded: [{ required: true, message: '请输入客户跟进提醒周期' }]
      }
    }
  },
  watch: {
    'formData.follow_up_unsettled'(newValue, oldValue) {
      if (newValue === '') {
        return
      }
      const numericValue = parseInt(newValue, 10)
      if (isNaN(numericValue)) {
        this.formData.follow_up_unsettled = oldValue || ''
      } else {
        this.formData.follow_up_unsettled = Math.abs(numericValue).toString()
      }
    },
    'formData.follow_up_traded'(newValue, oldValue) {
      if (newValue === '') {
        return
      }
      const numericValue = parseInt(newValue, 10)
      if (isNaN(numericValue)) {
        this.formData.follow_up_traded = oldValue || ''
      } else {
        this.formData.follow_up_traded = Math.abs(numericValue).toString()
      }
    }
  },
  methods: {
    handleCheckChange(value) {
      this.formData.follow_up_status = value
    },
    sendFollowData() {
      const data = this.formData
      this.$emit('followData', data)
    },
    saveEvt() {
      this.$emit('saveEvt')
    },
    getVal(type) {
      return (
        typeof this.formData.follow_up_status != 'undefined' && this.formData.follow_up_status.includes(type)
      )
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
