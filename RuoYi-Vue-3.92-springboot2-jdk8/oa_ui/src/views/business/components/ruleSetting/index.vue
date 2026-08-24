<!--审批设置-规则配置 -->
<template>
  <div class="ruleSetting">
    <el-card class="box-card">
      <div class="setting-container mt14">
        <el-row>
          <el-col v-bind="grid1">&nbsp;</el-col>
          <el-col v-bind="grid2">
            <el-form ref="elForm" :model="examineFrom" :rules="rules" size="medium" label-width="150px">
              <el-form-item :label="$t('business.handle')">
                <el-radio-group v-model="examineFrom.abnormal">
                  <el-radio :label="0">{{ $t('business.handle1') }}</el-radio>
                  <el-radio :label="1">{{ $t('business.handle2') }}</el-radio>
                </el-radio-group>
                <select-member
                  v-if="examineFrom.abnormal === 1"
                  :only-one="true"
                  :value="dataList || []"
                  @getSelectList="getSelectList"
                  style="width: 250px"
                ></select-member>
              </el-form-item>
              <el-form-item :label="$t('business.automatic')">
                <div>{{ $t('business.automatic1') }}</div>
                <el-radio-group v-model="examineFrom.auto" class="shu">
                  <el-radio :label="0">{{ $t('business.automatic2') }}</el-radio>
                  <el-radio :label="1">{{ $t('business.automatic3') }}</el-radio>
                  <el-radio :label="2">{{ $t('business.automatic4') }}</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item :label="$t('business.editAuthority')" class="shu">
                <el-checkbox-group v-model="examineFrom.edit">
                  <el-checkbox :label="1">{{ $t('business.editAuthority1') }}</el-checkbox>
                  <el-checkbox :label="2">{{ $t('business.editAuthority2') }}</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item :label="$t('business.revokeAuthority')">
                <el-checkbox-group v-model="examineFrom.recall">
                  <el-checkbox :label="1">{{ $t('business.revokeAuthority1') }}</el-checkbox>
                </el-checkbox-group>
                <div class="explain">{{ $t('business.revokeAuthority2') }}</div>
              </el-form-item>
              <el-form-item label="加签权限：">
                <el-checkbox-group v-model="examineFrom.is_sign">
                  <el-checkbox :label="1">允许在审批单中增加临时审批人</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col v-bind="grid1">&nbsp;</el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'RuleSetting',
  components: {
    selectMember: () => import('@/components/form-common/select-member')
  },
  props: {
    tabName: {
      type: String,
      default: ''
    },
    conf: {
      type: Object,
      default: () => {
        return null
      }
    }
  },
  data() {
    return {
      grid1: {
        xl: 8,
        lg: 5,
        md: 24,
        sm: 24,
        xs: 24
      },
      grid2: {
        xl: 8,
        lg: 14,
        md: 24,
        sm: 24,
        xs: 24
      },
      checkList: [],
      title: '',
      type: 0,
      examineFrom: {
        abnormal: 0,
        auto: 0,
        edit: [1, 2],
        recall: 0,
        is_sign: 0
      },
      rules: {},
      showAll: true,
      checkedName: '',
      userList: [],
      dataList: []
    }
  },
  created() {
    if (typeof this.conf === 'object' && this.conf !== null) {
      if (Number(this.conf.abnormal) > 0) this.conf.abnormal = 1
      this.conf.edit = this.conf.edit.map(Number)
      Number(this.conf.recall) > 0 ? (this.conf.recall = true) : (this.conf.recall = false)
      Number(this.conf.is_sign) > 0 ? (this.conf.is_sign = true) : (this.conf.is_sign = false)
      this.conf.auto = Number(this.conf.auto)
      if (this.conf.ab_card) {
        this.conf.ab_card.value = this.conf.ab_card.id
        this.dataList = [this.conf.ab_card]
      }
      if (this.conf.ab_card) this.checkedName = this.conf.ab_card.name
      Object.assign(this.examineFrom, this.conf)
    }
  },
  methods: {
    // 选择成员完成回调
    getSelectList(data) {
      this.dataList = data
      this.checkedName = data[0].name
      data.forEach((item) => {
        item.id = item.value
      })
      this.examineFrom.ab_card = data[0]
    },

    // 给父级页面提供得获取本页数据得方法
    getData() {
      return new Promise((resolve, reject) => {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) {
            reject({ target: this.tabName })
            return
          }
          if (this.examineFrom.abnormal === 1) {
            if (!this.checkedName) {
              this.$message.warning('请选择指定人员')
              return
            }
            if (this.dataList.length > 0) {
              this.examineFrom.abnormal = this.dataList[0].value
            } else {
              this.examineFrom.abnormal = this.conf.ab_card.id
            }
          } else {
            this.examineFrom.ab_card = ''
          }
          this.examineFrom.recall ? (this.examineFrom.recall = 1) : 0
          this.examineFrom.is_sign ? (this.examineFrom.is_sign = 1) : 0
          resolve({ ruleFrom: this.examineFrom, target: this.tabName })
        })
      })
    }
  }
}
</script>

<style scoped lang="scss">
.shu {
  ::v-deep.el-radio,
  ::v-deep.el-checkbox {
    display: block;
    height: 36px;
    line-height: 36px;
  }
}
.ruleSetting {
  height: calc(100vh - 130px);
  ::v-deep .el-card {
    height: 100%;
  }
}
.explain {
  font-size: 13px;
  color: #bbbbbb;
  margin-top: -11px;
}

.plan-footer-one {
  display: flex;
  justify-content: space-between;
  min-width: 277px;
  width: 277px;
}

.select-bar {
  display: flex;
}

::v-deep .el-form-item__content {
  flex: 1;
}

.flex-box {
  span {
    margin-right: 6px;
  }
  span:last-of-type {
    margin-right: 0;
  }
}

.select-bar {
  > .el-input__inner {
    height: 36px;
    line-height: 36px;
  }
}

::v-deep .el-range-editor--medium {
  height: 32px;
}
.el-icon-arrow-down {
  line-height: 38px;
}
</style>
