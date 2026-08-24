<template>
  <el-drawer
    :append-to-body="true"
    title="条件设置"
    :visible.sync="$store.state.business.conditionDrawer"
    direction="rtl"
    class="condition_copyer"
    size="650px"
    :before-close="saveCondition"
  >
    <el-form ref="form" label-width="110px">
      <div class="demo-drawer__content">
        <div class="condition_content">
          <el-row :gutter="20">
            <el-col :span="16">
              <el-form-item label="条件设置 : ">
                <el-input type="text" v-model="conditionConfig.nodeName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-select v-model="conditionConfig.priorityLevel">
                <el-option
                  v-for="item in conditionLen"
                  :value="item.toString()"
                  :label="'优先级' + item"
                  :key="item"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
          <div class="drawer-content">
            <el-row class="list" :gutter="20" v-for="(item, index) in conditionConfig.conditionList" :key="index">
              <el-col :span="item.type === 'departmentTree' && item.category == 1 ? 22 : 10">
                <el-form-item :label="item.title + ' : '">
                  <select-member
                    v-if="item.type === 'departmentTree' && item.category == 1"
                    :value="item.options.userList || []"
                    :placeholder="`选择申请人`"
                    @getSelectList="getSelectList($event, item)"
                    style="width: 100%"
                  >
                  </select-member>

                  <el-select v-model="item.value" v-if="item.type === 'radio'" placeholder="请选择条件">
                    <el-option value="0" label="属于"></el-option>
                  </el-select>
                  <el-select v-model="item.value" v-if="item.type === 'datePicker'" placeholder="请选择条件">
                    <el-option value="0" label="小于"></el-option>
                    <el-option value="1" label="等于"></el-option>
                    <el-option value="2" label="小于等于"></el-option>
                    <el-option value="3" label="大于等于"></el-option>
                  </el-select>
                  <el-select
                    v-model="item.value"
                    v-if="item.type === 'select' || item.type === 'checkbox'"
                    placeholder="请选择条件"
                  >
                    <el-option value="0" label="完全等于"></el-option>
                    <el-option value="1" label="包含任意"></el-option>
                  </el-select>
                  <el-select
                    v-model="item.value"
                    v-if="item.type === 'inputNumber' || item.type === 'moneyFrom' || item.type === 'timeFrom'"
                    placeholder="请选择条件"
                  >
                    <el-option value="0" label="小于"></el-option>
                    <el-option value="1" label="等于"></el-option>
                    <el-option value="2" label="小于等于"></el-option>
                    <el-option value="3" label="大于等于"></el-option>
                    <el-option value="4" label="介于（两个数字之间）"></el-option>
                  </el-select>
                  <el-select
                    v-model="item.value"
                    v-if="item.type === 'departmentTree' && item.category != 1"
                    placeholder="请选择条件"
                  >
                    <el-option value="0" label="完全属于"></el-option>
                    <el-option value="1" label="其一属于"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col
                :span="12"
                class="center-item-from"
                v-if="(item.type === 'departmentTree' && item.category != 1) || item.type !== 'departmentTree'"
              >
                <!-- datePicker -->
                <el-date-picker
                  v-if="item.type === 'datePicker'"
                  v-model="item.option"
                  type="date"
                  placeholder="选择时间"
                  style="width: 100%"
                  format=" yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  size="small"
                >
                </el-date-picker>
                <el-select
                  v-model="item.option"
                  :multiple="item.type !== 'radio'"
                  v-if="item.type === 'radio' || item.type === 'select' || item.type === 'checkbox'"
                >
                  <el-option
                    v-for="items in item.options"
                    :key="items.value"
                    :value="items.value"
                    :label="items.label"
                  ></el-option>
                </el-select>
                <el-input-number
                  v-model="item.option"
                  :controls="false"
                  :min="0"
                  v-if="['inputNumber', 'moneyFrom', 'timeFrom'].includes(item.type) && item.value < 4"
                ></el-input-number>
                <span class="time-from-tip" v-if="item.type === 'timeFrom'">
                  {{ item.timeType === 'day' ? '天' : '小时' }}
                </span>
                <el-row
                  class="display-flex"
                  v-if="['inputNumber', 'moneyFrom', 'timeFrom'].includes(item.type) && item.value == 4"
                >
                  <el-col :span="11" class="center-item-from">
                    <el-input-number
                      v-model="item.min"
                      :controls="false"
                      placeholder="输入最小值"
                      :min="0"
                    ></el-input-number>
                    <span class="time-from-tip" v-if="item.type === 'timeFrom'">
                      {{ item.timeType === 'day' ? '天' : '小时' }}
                    </span>
                  </el-col>
                  <el-col class="text-center" :span="2">-</el-col>
                  <el-col :span="11" class="center-item-from">
                    <el-input-number
                      v-model="item.max"
                      :controls="false"
                      placeholder="输入最大值"
                      :min="item.min"
                    ></el-input-number>
                    <span class="time-from-tip" v-if="item.type === 'timeFrom'">
                      {{ item.timeType === 'day' ? '天' : '小时' }}
                    </span>
                  </el-col>
                </el-row>

                <select-member
                  v-if="item.type === 'departmentTree' && item.category == 2"
                  :value="item.options.userList || []"
                  @getSelectList="getSelectList($event, item)"
                  style="width: 100%"
                >
                </select-member>

                <select-department
                  v-if="item.type === 'departmentTree' && item.category == 3"
                  :value="item.options.depList || []"
                  @changeMastart="changeMastart($event, item)"
                  style="width: 100%"
                ></select-department>
              </el-col>
              <el-col :span="2">
                <i class="el-icon-close pointer condition-icon" @click="removeItems(item, index)"></i>
              </el-col>
            </el-row>
          </div>
          <div class="conditions">
            <el-button @click="addCondition" type="text" :disabled="conditionButton" icon="el-icon-plus"
              >添加条件</el-button
            >
            <div class="el-popover conditions-popover" v-show="conditionsPopover">
              <el-button
                type="text"
                v-for="(item, index) in conditions"
                :key="index"
                v-show="!item.disabled"
                :class="item.disabled ? 'active' : ''"
                @click.stop="itemConditions(item, index)"
              >
                {{ item.title || item.props.titleIpt }}
              </el-button>
            </div>
          </div>
        </div>
        <div class="button from-foot-btn fix btn-shadow">
          <el-button @click="closeCondition">{{ $t('public.cancel') }}</el-button>
          <el-button type="primary" @click="saveCondition">{{ $t('public.ok') }}</el-button>
        </div>
      </div>
    </el-form>
  </el-drawer>
</template>

<script>
export default {
  components: {
    selectMember: () => import('@/components/form-common/select-member'),
    selectDepartment: () => import('@/components/form-common/select-department')
  },
  data() {
    return {
      conditionVisible: false,
      conditionsConfig: {
        conditionNodes: []
      },

      conditionLen: 0,
      conditionsPopover: false,
      conditionConfig: {},
      newConfig: [],
      PriorityLevel: '',
      conditions: [],
      conditionList: [],

      type: 1,
      activeIndex: -1,
      title: '',
      conditionsFields: [],
      conditionButton: true
    }
  },
  computed: {
    conditionsConfig1() {
      return this.$store.state.business.conditionsConfig
    },
    conditions1() {
      return this.$store.state.business.formSettingProps
    }
  },

  watch: {
    conditionsConfig1(val) {
      this.conditionsConfig = val.value
      this.PriorityLevel = val.priorityLevel
      this.newConfig = val.priorityLevel
        ? this.conditionsConfig.conditionNodes[val.priorityLevel - 1]
        : { nodeUserList: [], conditionList: [] }
      this.conditionConfig = val.priorityLevel
        ? this.conditionsConfig.conditionNodes[val.priorityLevel - 1]
        : { nodeUserList: [], conditionList: [] }
      this.conditionLen = this.conditionsConfig.conditionNodes.length - 1
    },
    conditions1(val) {
      this.newConfig = val.filter((item) => item.type !== 'input')
      this.conditions = val.filter((item) => item.type !== 'input')
    },

    conditions: {
      deep: true,
      handler: function (newV, oldV) {
        if (newV.length <= 0) {
          this.conditionButton = true
        } else {
          this.conditionButton = true
          for (let i = 0; i < newV.length; i++) {
            if (!newV[i].disabled) {
              this.conditionButton = false
              break
            }
          }
        }
      }
    }
  },

  methods: {
    addCondition() {
      // 根据条件不同筛选选中框
      if (this.conditions.length > 0) {
        this.conditionList = []
        if (this.conditionConfig.conditionList.length <= 0) {
          this.conditions.forEach((value) => {
            if (value.title === '异常日期') {
              value.disabled = true
            } else if (value.title === '异常记录') {
              value.disabled = true
            } else {
              value.disabled = false
            }
          })
        } else {
          this.conditions.forEach((value) => {
            this.conditionConfig.conditionList.forEach((val) => {
              if (val.field === value.field) {
                value.disabled = true
              }
            })
          })
        }
        this.conditionsPopover = this.conditionsPopover !== true
      } else {
        this.$message.warning('表单配置为空')
      }
    },
    itemConditions(row, index) {
      this.conditions[index].disabled = true
      let data = {}
      if (row.type === 'departmentTree') {
        if (row.props && row.props.member) {
          // 成员
          data = {
            field: row.field,
            title: row.title || row.props.titleIpt,
            type: row.type,
            options: {
              depList: [],
              userList: []
            },
            value: '',
            category: 2
          }
        } else if (row.props && row.props.member === false) {
          // 申请人
          data = {
            field: row.field,
            title: row.title,
            type: row.type,
            options: {
              depList: [],
              userList: []
            },
            value: '',
            category: 1
          }
        } else {
          data = {
            field: row.field,
            title: row.title || row.props.titleIpt,
            type: row.type,
            options: {
              depList: [],
              userList: []
            },
            value: '',
            category: 3
          }
        }
      } else {
        data = {
          field: row.field,
          title: row.title || row.props.titleIpt,
          type: row.type,
          options: row.options ? row.options : [],
          value: '',
          min: 0,
          max: 0,
          option: row.type === 'select' || row.type === 'checkbox' ? [] : null
        }
        if (row.type === 'timeFrom') {
          data.timeType = row.timeType
          data.title = row.title || row.props.titleIpt
        }
      }
      this.conditionConfig.conditionList.push(data)

      this.conditionsPopover = false
    },
    removeItems(row, index) {
      if (this.conditions.length > 0) {
        this.conditions.forEach((value) => {
          if (value.field === this.conditionConfig.conditionList[index].field) {
            value.disabled = false
          }
        })
      }
      this.conditionsPopover = false
      this.conditionConfig.conditionList.splice(index, 1)
    },

    // 获取条件信息
    getConditionsFields(field, type) {
      if (field && this.conditionsFields.length <= 0) {
        this.conditionsFields.push({
          field: field,
          number: 1
        })
      } else {
        // 添加条件
        if (type === 1) {
          this.conditionsFields.map((value) => {
            if (field === value.field) {
              value.number++
            }
          })
          // 删除单个条件
        } else if (type === 2) {
          this.conditionsFields.map((value) => {
            if (field === value.field) {
              value.number--
            }
          })
        }
      }
    },
    // 选择成员回调
    getSelectList(data, item) {
      if (data.length > 0) {
        data.forEach((item) => {
          item.id = item.value
        })
      }
      if (this.type == 1) {
        item.options.userList = data
        // this.conditionConfig.conditionList[this.activeIndex].options = {
        //   userList: data
        // }
      } else if (this.type == 2) {
        item.options.userList = data
        // this.conditionConfig.conditionList[this.activeIndex].options = {
        //   userList: data
        // }
      }
    },
    // 选择成员完成回调
    changeMastart(data, item) {
      item.options.depList = data
      // this.conditionConfig.conditionList[this.activeIndex].options = {
      //   depList: data
      // }

      this.activeIndex = -1
    },

    cardTag(index, active, type) {
      if (type === 1) {
        this.conditionConfig.conditionList[index].options.userList.splice(active, 1)
      } else if (type === 2) {
        this.conditionConfig.conditionList[index].options.depList.splice(active, 1)
      }
    },
    saveCondition() {
      var condition = this.conditionConfig.conditionList

      if (condition.length > 0) {
        for (let i = 0; i < condition.length; i++) {
          const value = condition[i]
          if (value.type === 'departmentTree' && value.category == 1 && value.options.userList.length <= 0) {
            this.$message.error('请选择' + value.title)
            return
          } else if (value.type === 'departmentTree' && value.category == 2 && !value.value) {
            this.$message.error('请选择' + value.title + '条件')
            return
          } else if (value.type === 'departmentTree' && value.category == 2 && value.options.userList.length <= 0) {
            this.$message.error('请选择' + value.title)
            return
          } else if (value.type === 'departmentTree' && value.category == 3 && !value.value) {
            this.$message.error('请选择' + value.title + '条件')
            return
          } else if (
            value.type === 'departmentTree' &&
            value.category == 3 &&
            value.options.depList &&
            value.options.depList.length <= 0
          ) {
            this.$message.error('请选择' + value.title)
            return
          } else if (value.type !== 'departmentTree' && !value.value) {
            this.$message.error('请选择' + value.title + '条件')
            return
          } else if (
            (value.type === 'moneyFrom' || value.type === 'inputNumber' || value.type === 'timeFrom') &&
            (!value.option || value.option <= 0) &&
            value.min >= value.max
          ) {
            this.$message.error(value.title + '不能为负数或0')
            return
          } else if ((value.type === 'select' || value.type === 'checkbox') && value.option.length <= 0) {
            this.$message.error('请选择' + value.title + '内容')
            return
          } else if (value.type === 'radio' && !value.option) {
            this.$message.error('请选择' + value.title + '内容')
            return
          }
        }
      }
      this.$store.commit('updateCondition', false)
      var a = this.conditionsConfig.conditionNodes.splice(this.PriorityLevel - 1, 1) // 截取旧下标
      this.conditionsConfig.conditionNodes.splice(this.conditionConfig.priorityLevel - 1, 0, a[0]) // 填充新下标
      this.conditionsConfig.conditionNodes.map((item, index) => {
        item.priorityLevel = (index + 1).toString()
      })
      for (var i = 0; i < this.conditionsConfig.conditionNodes.length; i++) {
        this.conditionsConfig.conditionNodes[i].error =
          this.$func.conditionStr(this.conditionsConfig, i) == '请设置条件' &&
          i != this.conditionsConfig.conditionNodes.length - 1
      }
      this.conditionsPopover = false
      this.$store.commit('updateConditionsConfig', {
        value: this.conditionsConfig,
        flag: true,
        id: this.$store.state.business.conditionsConfig.id
      })
    },
    closeCondition() {
      this.conditionsPopover = false
      this.conditions.forEach((item) => {
        item.disabled = false
      })
      this.$store.commit('updateCondition', false)
    }
  }
}
</script>

<style lang="scss" scoped>
.display-flex {
  display: flex;
  align-items: center;
  justify-content: center;
}
.condition_copyer {
  .el-drawer__body {
    .priority_level {
      background: rgba(255, 255, 255, 1);
      border-radius: 4px;
      border: 1px solid rgba(217, 217, 217, 1);
    }
  }
  .condition_content {
    padding: 20px 20px 0;
    ::v-deep .el-input-number--medium,
    ::v-deep .el-select--medium {
      width: 100%;
    }
    ::v-deep .el-input__inner {
      text-align: left;
    }
  }
}
.condition_list {
  .el-dialog__body {
    padding: 16px 26px;
  }
  p {
    color: #666666;
    margin-bottom: 10px;
    & > .check_box {
      margin-bottom: 0;
      line-height: 36px;
    }
  }
}
.drawer-content {
  ::v-deep .el-form-item:last-of-type {
    margin-bottom: 0;
  }
  .plan-footer-one {
    -webkit-appearance: none;
    background-color: #fff;
    background-image: none;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    color: #606266;
    display: inline-block;
    font-size: inherit;
    min-height: 36px;
    line-height: 36px;
    outline: none;
    font-size: 12px;
    padding: 0 15px;

    -webkit-transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    width: 100%;
  }
  .list {
    margin-bottom: 22px;
    display: flex;
    align-items: center;
    &:last-of-type {
      margin-bottom: 10px;
    }
    .condition-icon {
      font-size: 20px;
      margin-top: 4px;
    }
  }
}
.conditions {
  position: relative;
  .conditions-popover {
    position: absolute;
    left: 0;
    top: 36px;
    ::v-deep .el-popover {
      min-width: 220px;
    }
    button {
      display: block;
      text-align: left;
      margin-left: 0;
      padding: 0;
      margin-bottom: 12px;
      color: rgba(0, 0, 0, 0.85);
      &.active {
        color: #bbb;
      }
      &:last-of-type {
        margin-bottom: 0;
      }
    }
  }
}
.from-foot-btn button {
  width: auto;
  height: auto;
}
.center-item-from {
  position: relative;
  .time-from-tip {
    font-size: 12px;
    color: #303133;
    position: absolute;
    right: 20px;
    top: 12px;
  }
}
</style>
