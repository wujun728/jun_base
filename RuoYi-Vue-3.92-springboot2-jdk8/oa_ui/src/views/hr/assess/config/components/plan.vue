<template>
  <div>
    <div class="v-height-flag">
      <el-row class="plan-title">
        <el-row class="p30">
          <el-tabs v-model="planIndex" @tab-click="handleClick">
            <el-tab-pane
              v-for="(item, index) in periodOptions"
              :key="index"
              :label="item.label"
              :name="String(item.value)"
              class="is-active"
            />
          </el-tabs>
        </el-row>
      </el-row>
      <div class="box">
        <el-row :span="24" class="mb20">
          <div class="form-list">
            <label for="" style="margin-top: 0"><span class="color-tab">*</span> {{ $t('access.openstatus') }}：</label>
            <el-col :span="12" class="form-list-con">
              <el-switch
                v-model="from.status"
                :active-text="$t('hr.open')"
                :active-value="1"
                :inactive-text="$t('hr.close')"
                :inactive-value="0"
              />
            </el-col>
          </div>
          <div class="form-list">
            <label for=""><span class="color-tab">*</span> 创建考核提醒：</label>
            <el-row :gutter="14">
              <el-col :span="12" class="form-list-con">
                <el-select
                  v-if="planIndex == 1"
                  v-model="from.remindDays"
                  placeholder="请选择创建考核提醒日期"
                  size="small"
                  style="flex: 100%"
                >
                  <el-option
                    v-for="(item, index) in remindDaysData"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
                <div v-if="planIndex == 2" class="plan-add">
                  <el-input-number
                    v-model="from.remindDays"
                    :controls="false"
                    :max="28"
                    :min="1"
                    :precision="0"
                    placeholder="请选择创建考核提醒日期"
                    size="small"
                  />
                  <p class="days">号</p>
                </div>
                <div v-if="planIndex >= 3" class="flex">
                  <!-- <el-col :span="12" > -->
                  <el-select v-model="from.remindMonth" class="plan-add" placeholder="请选择月份" size="small">
                    <el-option
                      v-for="(item, index) in months"
                      :key="'month' + index"
                      :label="'第' + item + '个月'"
                      :value="item"
                    />
                  </el-select>
                  <!-- </el-col> -->
                  <div class="plan-days">
                    <el-input-number
                      v-model="from.remindDays"
                      :controls="false"
                      :max="28"
                      :min="1"
                      :precision="0"
                      placeholder="请选择创建考核提醒日期"
                      size="small"
                    ></el-input-number>
                    <p class="days">号</p>
                  </div>
                </div>
              </el-col>
              <el-col :span="24" class="assess-info">用于设置提醒考核人，为被考核人制定考核目标的时间</el-col>
            </el-row>
          </div>
          <div class="form-list">
            <label for=""><span class="color-tab">*</span> 自我评价提醒：</label>
            <el-row :gutter="14">
              <el-col :span="12">
                <el-row class="width100">
                  <el-col :span="12">
                    <el-select
                      v-model="from.make"
                      :placeholder="$t('finance.pleaseselect')"
                      size="small"
                      style="flex: 100%"
                      @change="handleMake"
                    >
                      <el-option
                        v-for="(item, index) in makeOptions"
                        :key="index"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </el-col>
                  <el-col :span="12" class="plan-days">
                    <el-input-number
                      v-model="days.makeDays"
                      :controls="false"
                      :max="3650"
                      :min="0"
                      :placeholder="$t('access.numberofdays')"
                      :precision="0"
                      size="small"
                    >
                    </el-input-number>
                    <p class="days">{{ $t('access.day') }}</p>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="24" class="assess-info">用于设置提醒被考核人，进行考核自我评价的时间</el-col>
            </el-row>
          </div>
          <div class="form-list">
            <label for=""><span class="color-tab">*</span> 上级评价提醒：</label>
            <el-row :gutter="14">
              <el-col :span="12">
                <el-row class="width100">
                  <el-col :span="12">
                    <el-select
                      v-model="from.eval"
                      :placeholder="$t('finance.pleaseselect')"
                      size="small"
                      style="flex: 100%"
                    >
                      <el-option
                        v-for="(item, index) in evalOptions"
                        :key="index"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </el-col>
                  <el-col :span="12" class="plan-days">
                    <el-input-number
                      v-model="days.evalDays"
                      :controls="false"
                      :max="100"
                      :min="0"
                      :placeholder="$t('access.numberofdays')"
                      :precision="0"
                      size="small"
                    />
                    <p class="days">{{ $t('access.day') }}</p>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="24" class="assess-info"
                >用于设置提醒考核人，对直属下级考核完成情况进行上级评价打分的时间</el-col
              >
            </el-row>
          </div>
          <div class="form-list">
            <label for=""><span class="color-tab">*</span> 考核自动结束：</label>
            <el-row :gutter="14">
              <el-col :span="12">
                <el-row class="width100">
                  <el-col :span="12">
                    <el-input
                      v-model="from.verify"
                      :disabled="true"
                      :placeholder="$t('access.superiorevaluation')"
                      size="small"
                    />
                  </el-col>
                  <el-col :span="12" class="plan-days">
                    <el-input-number
                      v-model="days.verifyDays"
                      :controls="false"
                      :max="100"
                      :min="0"
                      :placeholder="$t('access.numberofdays')"
                      :precision="0"
                      size="small"
                    />
                    <p class="days">{{ $t('access.day') }}</p>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="24" class="assess-info">用于设置上级评价完成后，考核流转为【结束】状态的时间</el-col>
            </el-row>
          </div>
          <div class="form-list" style="align-items: normal">
            <label for=""><span class="color-tab">*</span> 被考核人：</label>
            <el-row :gutter="14">
              <el-col :span="12" class="resource-check">
                <el-radio-group v-model="from.resource">
                  <el-radio label="1">按部门添加</el-radio>
                  <el-radio label="0">按人员添加</el-radio>
                </el-radio-group>
              </el-col>

              <el-col :span="24">
                <div class="assess-info">
                  按部门添加用于本部门所有人员均需参加考核的情况；按人员添加用于特殊个例人员需要参加考核的情况
                </div>
              </el-col>
            </el-row>
          </div>
          <div class="form-list">
            <label for=""
              ><span class="color-tab">*</span> {{ from.resource == 0 ? $t('access.assessee') : '被考核部门' }}：</label
            >
            <el-col :span="22" class="form-list-con" style="width: 40%">
              <select-member
                v-if="from.resource == 0"
                :value="departmentObj.userList || []"
                :placeholder="$t('access.tips15')"
                @getSelectList="getSelectList"
                style="width: 100%"
              ></select-member>
              <select-department
                v-if="from.resource == 1"
                :value="departmentObj.frames || []"
                :placeholder="$t('access.tips15')"
                @changeMastart="changeMastart"
                style="width: 100%"
              ></select-department>
            </el-col>
          </div>
          <div class="form-list" style="margin-top: 10px">
            <label for=""></label>
            <el-button type="text" @click="handlePreserve(2)">保存查看被考核人员详情</el-button>
          </div>
        </el-row>
      </div>
      <div class="cr-bottom-button">
        <el-button :loading="loading" size="small" type="primary" @click="handlePreserve(1)">{{
          $t('public.save')
        }}</el-button>
      </div>
    </div>

    <assess-box ref="assessBox"></assess-box>
  </div>
</template>

<script>
import Common from '@/components/user/accessCommon'
import { assessPlanGetApi, assessPlanPutApi } from '@/api/enterprise'
export default {
  name: 'Plan',
  components: {
    selectMember: () => import('@/components/form-common/select-member'),
    selectDepartment: () => import('@/components/form-common/select-department'),
    assessBox: () => import('./assessBox')
  },
  data() {
    return {
      periodOptions: Common.periodOptions,
      planIndex: '2',
      from: {
        make: 'before',
        eval: 'before',
        verify: '',
        status: 0,
        remindDays: 1,
        remindMonth: 1,
        resource: '0'
      },
      months: 12,
      days: {
        makeDays: 3,
        evalDays: 3,
        verifyDays: 3
      },
      afterMaxDay: 3650,
      makeOptions: [
        { value: 'start', label: '考核开始后' },
        { value: 'before', label: '考核结束前' },
        { value: 'after', label: '考核结束后' }
      ],
      evalOptions: [
        { value: 'start', label: '考核开始后' },
        { value: 'before', label: '考核结束前' },
        { value: 'after', label: '考核结束后' }
      ],
      remindDaysData: [
        { value: 1, label: '星期一' },
        { value: 2, label: '星期二' },
        { value: 3, label: '星期三' },
        { value: 4, label: '星期四' },
        { value: 5, label: '星期五' },
        { value: 6, label: '星期六' },
        { value: 7, label: '星期七' }
      ],

      departmentObj: {
        userList: [], // 选择成员
        frames: []
      },

      heightInputRole: 36,
      id: null,
      loading: false
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    async getTableData() {
      const result = await assessPlanGetApi(this.planIndex)
      const data = result.data
      this.from.status = data.status
      this.from.make = data.make_type
      this.days.makeDays = data.make_day
      this.from.eval = data.eval_type
      this.days.evalDays = data.eval_day
      this.days.verifyDays = data.verify_day
      this.id = data.id
      this.from.resource = String(data.assess_type)
      this.from.remindDays = data.create_time
      this.from.remindMonth = data.create_month
      if (data.assess_type === 1) {
        this.departmentObj.frames = data.test_frame
        this.departmentObj.userList = []
      } else {
        let list = []
        data.test.map((item) => {
          let card = {
            value: item.id,
            name: item.name,
            avatar: item.avatar
          }
          list.push(card)
        })
        this.departmentObj.userList = list
        this.departmentObj.frames = []
      }
      this.handleMake(this.from.make)
    },
    handleClick(number) {
      this.planIndex = number.name
      if (this.planIndex == 3) {
        this.months = 12
      } else if (this.planIndex == 4) {
        this.months = 6
      } else if (this.planIndex == 5) {
        this.months = 3
      } else {
        this.months = 12
      }
      this.getTableData()
    },

    handleMake(e) {
      if (e === 'after') {
        if (this.planIndex == 1) {
          this.afterMaxDay = 7
        } else if (this.planIndex == 2) {
          this.afterMaxDay = 28
        } else if (this.planIndex == 3) {
          this.afterMaxDay = 365
        } else if (this.planIndex == 4) {
          this.afterMaxDay = 180
        } else if (this.planIndex == 5) {
          this.afterMaxDay = 90
        }
      } else {
        this.afterMaxDay = 3650
      }
    },

    // 选择部门弹窗
    changeMastart(data) {
      if (data.length > 0) {
        this.departmentObj.frames = data
      } else {
        this.departmentObj.frames = []
      }
    },
    // 选择成员弹窗
    getSelectList(data) {
      this.departmentObj.userList = data
    },

    handlePreserve(type) {
      if (this.from.name == '') {
        this.$message.error(this.$t('access.tips06'))
      } else if (this.days.makeDays == '') {
        this.$message.error(this.$t('access.tips07'))
      } else if (this.days.evalDays == '') {
        this.$message.error(this.$t('access.tips08'))
      } else if (this.days.verifyDays == '') {
        this.$message.error(this.$t('access.tips09'))
      } else if (this.days.makeDays > this.afterMaxDay) {
        this.days.makeDays = this.afterMaxDay
        this.$message.error('目标制定时间超过了最大天数')
      } else {
        let check = []
        let frame = []
        if (this.from.resource == 0) {
          if (this.departmentObj.userList.length > 0) {
            this.departmentObj.userList.forEach((item) => {
              check.push(item.value)
            })
          }
        } else {
          if (this.departmentObj.frames.length > 0) {
            this.departmentObj.frames.forEach((value) => {
              frame.push(value.id)
            })
          }
        }
        const data = {
          make_type: this.from.make,
          make_day: this.days.makeDays,
          eval_type: this.from.eval,
          eval_day: this.days.evalDays,
          verify_day: this.days.verifyDays,
          status: this.from.status,
          assess_type: this.from.resource,
          create_time: this.from.remindDays,
          create_month: this.from.remindMonth
        }
        if (this.from.resource == 0) {
          data.test = check
        } else {
          data.test_frame = frame
        }
        this.loading = true
        assessPlanPutApi(this.id, data)
          .then((res) => {
            if (res.status == 200) {
              this.loading = false
              if (type !== 1) {
                this.$refs.assessBox.id = this.id
                this.$refs.assessBox.openBox()
              }
            }
          })
          .catch((error) => {
            this.loading = false
          })
      }
    },
    cardTag(type, index) {
      type == 1 ? this.departmentObj.userList.splice(index, 1) : this.departmentObj.frames.splice(index, 1)
    }
  }
}
</script>

<style lang="scss" scoped>
.box {
  height: calc(100vh - 200px);
  overflow-y: scroll;
}
.plan-title {
  margin: 0 -20px;
  border-bottom: 2px solid #f0f2f5;
  .p30 {
    padding: 0 30px;
  }
}
.cr-bottom-button {
  position: fixed;
  left: -20px;
  right: 0;
  bottom: 0;
  width: calc(100% + 220px);
}
::v-deep .el-scrollbar__wrap {
  padding-right: 60px;
}
::v-deep .el-tabs__item {
  line-height: 24px;
}
.form-list {
  display: flex;
  margin-top: 20px;
  margin-right: 20px;

  label {
    margin-top: 8px;
    width: 20%;
    text-align: right;
    font-weight: normal;
    font-size: 13px;
  }
  ::v-deep .el-radio-group {
    display: flex;
    min-width: 500px;
    .el-radio {
      margin-bottom: 0;
    }
  }
  ::v-deep .el-row {
    width: 80%;
  }
  .resource-check {
    width: 120px;
  }
  .resource-text {
    width: calc(100% - 160px);

    .assess-info {
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 13px;
      color: #909399;
      margin-top: 10px;
      line-height: 16px;
      white-space: nowrap;
    }
  }
  .assess-info {
    white-space: nowrap;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 13px;
    color: #909399;
    margin-top: 10px;
  }
  ::v-deep .el-select {
    width: 100%;
  }
  .form-list-con {
    .plan-icon {
      color: #666666;
      padding: 0 10px;
    }
  }
  .plan-add {
    position: relative;
    width: calc(50%);
    margin-left: 0px;
    ::v-deep .el-input-number--small {
      width: 100%;
    }
    .days {
      position: absolute;
      font-size: 13px;
      right: 20px;
      z-index: 2;
      top: 8px;
    }
  }
  .plan-days {
    position: relative;
    width: calc(50% - 14px);
    margin-left: 14px;
    ::v-deep .el-input-number--small {
      width: 100%;
    }
    .days {
      position: absolute;
      font-size: 13px;
      right: 20px;
      z-index: 2;
      top: 8px;
    }
  }
  ::v-deep .el-input-number--medium {
    width: 100%;
  }
  ::v-deep .el-input__inner {
    text-align: left;
    font-size: 13px;
    width: 100%;
  }
}
.width100 {
  width: 100% !important;
}
.flex-box {
  span {
    margin-right: 6px;
  }
  span:last-of-type {
    margin-left: 0;
  }
}
::v-deep .el-radio {
  margin-bottom: 14px;
  display: block;
  &:last-of-type {
    margin-bottom: 0;
  }
}
.plan-footer-one {
  background-color: #fff;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  color: #606266;
  display: inline-block;
  font-size: inherit;
  min-height: 32px;
  line-height: 32px;
  outline: none;
  padding: 0 15px;
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
  width: 100%;
}
</style>
