<!-- @FileDescription: 低代码-高级筛选组件-条件设置组件-->
<template>
  <div>
    <el-form ref="form" label-width="auto">
      <div class="demo-drawer__content">
        <div
          class="condition_content"
          :class="[conditionConfig.conditionList.length > 5 ? 'pb30' : '', max !== 9 ? 'p20' : '']"
        >
          <el-row :gutter="20" v-if="eventStr !== 'event'">
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
            <el-row class="list" v-for="(item, index) in conditionConfig.conditionList" :key="index">
              <el-col :span="10" class="mr10 flex">
                <span class="number">{{ index + 1 }}</span>
                <el-select
                  v-model="item.field"
                  size="small"
                  :style="noRule ? 'width: 50%;' : 'width: 100%;'"
                  filterable
                  @change="itemConditions(item.field, index, item)"
                >
                  <el-option
                    v-for="items in conditions"
                    :key="items.title"
                    :value="items.field"
                    :label="items.title"
                    :disabled="listIds.includes(items.field)"
                  ></el-option>
                </el-select>

                <el-select
                  v-if="noRule"
                  v-model="item.value"
                  placeholder="选择条件"
                  @change="changeValue(item, index)"
                  style="width: 50%"
                  size="small"
                  class="ml10"
                >
                  <el-option
                    v-for="val in list[item.form_value]"
                    :key="val.value"
                    :label="val.label"
                    :value="val.value"
                  >
                  </el-option>
                </el-select>
              </el-col>

              <el-col
                :span="13"
                class="center-item-from"
                v-if="
                  !['is_empty', 'not_empty', 'today', 'week', 'month', 'quarter', 'year'].includes(item.value) ||
                  !noRule
                "
              >
                <!-- 下拉选择 -->

                <el-select
                  v-model="item.option"
                  size="small"
                  multiple="true"
                  v-if="
                    item.form_value === 'select' &&
                    (item.type == 'radio' || item.type == 'checkbox' || item.type == 'select')
                  "
                  style="width: 100%"
                  filterable
                >
                  <el-option
                    v-for="items in item.options"
                    :key="items.value"
                    :value="items.value"
                    :label="items.name"
                  ></el-option>
                </el-select>
                <el-select style="width: 100%" v-model="item.option" v-if="item.form_value === 'switch'" size="small">
                  <el-option value="1" label="是"></el-option>
                  <el-option value="0" label="否"></el-option>
                </el-select>

                <!-- 输入框类型 -->
                <el-input
                  v-model="item.option"
                  size="small"
                  v-if="item.form_value == 'input' && ['input', 'textarea'].includes(item.type)"
                ></el-input>

                <!-- 数字类型 -->
                <template v-if="item.form_value == 'number'">
                  <el-input v-model="item.option" size="small" style="width: 100%" v-if="item.value == 'regex'">
                  </el-input>
                  <el-input-number
                    v-model="item.option"
                    :controls="false"
                    size="small"
                    :precision="item.type == 'input_number' ? 0 : undefined"
                    style="width: 100%"
                    v-if="!['between', 'regex'].includes(item.value)"
                  ></el-input-number>
                  <!-- 区间 -->
                  <div v-if="item.value == 'between'" class="flex">
                    <el-input-number
                      v-model="item.min"
                      :controls="false"
                      style="width: 50%"
                      size="small"
                      :precision="item.type == 'input_number' ? 0 : undefined"
                    ></el-input-number>
                    <el-input-number
                      v-model="item.max"
                      :controls="false"
                      style="width: 50%"
                      class="ml10"
                      size="small"
                      :precision="item.type == 'input_number' ? 0 : undefined"
                    ></el-input-number>
                  </div>
                </template>

                <!-- 级联选择 -->

                <el-cascader
                  v-model="item.option"
                  :options="item.options"
                  :props="{
                    label: 'name',
                    value: 'value',
                    expandTrigger: 'hover',
                    multiple: true
                  }"
                  style="width: 100%"
                  size="small"
                  filterable
                  v-if="['cascader', 'cascader_radio'].includes(item.type)"
                ></el-cascader>
                <!-- 管理范围 -->
                <!-- {{ item.input_type }} -->
                <el-cascader
                  v-if="!noRule && item.type == 'scope_frame'"
                  style="width: 100%"
                  size="small"
                  v-model="item.option"
                  :options="frameTreeData"
                  :props="{ checkStrictly: true, value: 'id', label: 'label' }"
                  placeholder="管理范围"
                  filterable
                  clearable
                  :show-all-levels="false"
                ></el-cascader>
                <!-- 级联选择省市区 -->
                <el-cascader
                  v-model="item.option"
                  :options="item.options"
                  :props="{
                    checkStrictly: true,
                    label: 'name',
                    value: 'value',
                    multiple: true
                  }"
                  style="width: 100%"
                  filterable
                  size="small"
                  v-if="['cascader_address'].includes(item.type)"
                ></el-cascader>

                <!-- 日期选择 -->
                <div
                  v-if="
                    item.type == 'date_picker' && !['today', 'week', 'month', 'quarter', 'year'].includes(item.value)
                  "
                >
                  <div v-if="item.value == 'between' || !noRule" class="flex">
                    <el-date-picker
                      v-model="item.option"
                      type="daterange"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      style="width: 100%"
                      size="small"
                      format=" yyyy/MM/dd"
                      value-format="yyyy/MM/dd"
                    >
                    </el-date-picker>
                  </div>
                  <el-input-number
                    v-else-if="['n_day', 'last_day', 'next_day'].includes(item.value)"
                    v-model="item.option"
                    :controls="false"
                    :min="0"
                    style="width: 100%"
                    size="small"
                  ></el-input-number>
                  <el-date-picker
                    v-else
                    style="width: 100%"
                    v-model="item.option"
                    type="date"
                    placeholder="选择日期"
                    format=" yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    size="small"
                  >
                  </el-date-picker>
                </div>

                <!-- 日期时间选择 -->
                <div
                  v-if="
                    item.type == 'date_time_picker' &&
                    !['today', 'week', 'month', 'quarter', 'year', 'last_year'].includes(item.value)
                  "
                >
                  <div v-if="item.value == 'between'" class="flex">
                    <el-date-picker
                      v-model="item.option"
                      type="datetimerange"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      style="width: 100%"
                      size="small"
                      format=" yyyy/MM/dd HH:mm:ss"
                      value-format="yyyy/MM/dd HH:mm:ss"
                    >
                    </el-date-picker>
                  </div>
                  <el-input-number
                    v-else-if="['n_day', 'last_day', 'next_day'].includes(item.value)"
                    v-model="item.option"
                    :controls="false"
                    :min="0"
                    style="width: 100%"
                    size="small"
                  ></el-input-number>
                  <el-date-picker
                    v-else
                    style="width: 100%"
                    v-model="item.option"
                    type="datetime"
                    format=" yyyy/MM/dd HH:mm:ss"
                    value-format="yyyy/MM/dd HH:mm:ss"
                    placeholder="选择日期时间"
                    size="small"
                  >
                  </el-date-picker>
                </div>

                <!-- 一对一关联字段 -->
                <select-one
                  v-if="item.type == 'input_select' && !item.category"
                  :value="item.options[0] || {}"
                  :id="item.id"
                  style="width: 100%"
                  @getSelection="getSelection($event, item)"
                ></select-one>

                <!-- 选择标签 -->
                <select-label
                  v-if="item.type == 'tag'"
                  :list="item.options || []"
                  :value="item.optionsList || []"
                  style="width: 100%"
                  :props="{ children: 'children', label: 'name' }"
                  @handleLabelConf="handleLabelConf($event, item)"
                ></select-label>

                <!-- 选择成员 -->
                <select-member
                  v-if="item.category == 2"
                  :onlyOne="['in', 'not_in'].includes(item.value) ? false : true"
                  :value="item.options.userList || []"
                  @getSelectList="getSelectList($event, item)"
                  style="width: 100%"
                ></select-member>

                <!-- 选择部门 -->
                <select-department
                  v-if="item.category == 1"
                  :onlyOne="['in', 'not_in'].includes(item.value) ? false : true"
                  :value="item.options.depList || []"
                  @changeMastart="changeMastart($event, item)"
                  style="width: 100%"
                ></select-department>
              </el-col>
              <el-col :span="1">
                <i class="el-icon-delete ml10" @click="removeItems(item, index)"></i>
              </el-col>
            </el-row>
          </div>
          <div class="conditions mb20">
            <el-button @click="addCondition" type="text"> <span class="el-icon-circle-plus"></span>添加条件</el-button>
            <div class="el-popover conditions-popover" v-show="conditionsPopover">
              <el-button
                type="text"
                v-for="(item, index) in conditions"
                :key="index"
                v-show="!item.disabled"
                @click.stop="itemConditions(item, index)"
              >
                {{ item.title }}
              </el-button>
            </div>
          </div>

          <template v-if="eventStr == 'event' && noRule">
            <el-divider v-if="max !== 9">条件规则设置</el-divider>
            <el-divider v-else></el-divider>
            <el-form-item label="条件规则: ">
              <el-radio v-model="additional_search_boolean" label="1">符合全部</el-radio>
              <el-radio v-model="additional_search_boolean" label="0">符合任一</el-radio>
            </el-form-item>
          </template>
        </div>
        <div class="button from-foot-btn fix btn-shadow" v-if="max !== 9">
          <el-button @click="closeCondition">{{ $t('public.cancel') }}</el-button>
          <el-button type="primary" @click="saveCondition">{{ $t('public.ok') }}</el-button>
        </div>
        <div v-else class="flex-end fix">
          <el-button size="small" @click="close">清空数据</el-button>
          <el-button size="small" type="primary" @click="saveCondition">确定</el-button>
        </div>
      </div>
    </el-form>
  </div>
</template>

<script>
import { frameTreeApi } from '@/api/public'
import Common from '@/components/develop/commonData'
import { dataDatabaseFieldsApi } from '@/api/develop'
import { getDictTreeListApi } from '@/api/form'
export default {
  components: {
    selectMember: () => import('@/components/form-common/select-member'),
    selectDepartment: () => import('@/components/form-common/select-department'),
    selectOne: () => import('@/components/form-common/select-one'),
    selectLabel: () => import('@/components/form-common/select-label')
  },
  props: {
    id: {
      type: String || Number,
      default: null
    },
    // 区分审批流程/触发器
    eventStr: {
      type: String,
      default: ''
    },
    // 高级筛选
    formArray: {
      type: Array,
      default: () => []
    },
    additionalBoolean: {
      type: String,
      default: '1'
    },
    // 最多选多少个条件
    max: {
      type: Number,
      default: null
    },
    // 不要规则设置
    noRule: {
      type: Boolean,
      default: true
    }
  },

  data() {
    return {
      additional_search_boolean: this.additionalBoolean + '',
      heightInputRole: 32,
      header: {},
      rowId: 0,
      frameTreeData: [],
      onlyOne: false,
      tableData: [],
      labelList: [],
      conditionVisible: false,
      conditionsConfig: {
        conditionNodes: []
      },
      list: Common.conditionConfig,
      conditionLen: 0,
      conditionsPopover: false,
      conditionConfig: { conditionList: [] },
      newConfig: [],
      PriorityLevel: '',
      conditions: [],
      conditionRoleVisible: false,
      type: 1,
      activeIndex: -1,
      title: '',
      conditionsFields: [],
      member: ['user_id', 'update_user_id', 'owner_user_id', 'check_uid', 'card_id', 'creator', 'salesman'],
      frame: ['frame_id', 'frame'],
      date: ['date_picker', 'date_time_picker'],
      input: ['input', 'textarea', 'tag'],
      select: ['radio', 'cascader_radio', 'cascader_address', 'checkbox', 'tag', 'cascader', 'select'],
      number: ['input_number', 'input_float', 'input_percentage', 'input_price']
    }
  },
  mounted() {
    if (!this.noRule) {
      this.getScopeFrame()
    }
    if (this.id && this.id > 0) {
      this.getList(this.id)
    }

    if (this.formArray && this.formArray.length > 0) {
      this.list.input = this.list.input.filter((item) => item.value !== 'regex')
      this.list.number = this.list.number.filter((item) => item.value !== 'regex')

      let formData = this.formArray.filter((item) => {
        return !['file', 'image'].includes(item.type)
      })

      formData.forEach((item, index) => {
        if (item.type == 'cascader_address') {
          this.getCityList(index)
        }
      })
      this.conditions = formData
    }

    if (this.eventStr === 'event' && this.$store.state.business.fieldOptions.list) {
      this.conditionConfig.conditionList = this.$store.state.business.fieldOptions.list
    } else if (this.eventStr !== 'event' && this.$store.state.business.conditionsConfig) {
      let val = this.$store.state.business.conditionsConfig
      this.conditionsConfig = val.value
      this.PriorityLevel = val.priorityLevel || ''
      this.newConfig = val.priorityLevel
        ? this.conditionsConfig.conditionNodes[val.priorityLevel - 1]
        : { nodeUserList: [], conditionList: [] }
      this.conditionConfig = val.priorityLevel
        ? this.conditionsConfig.conditionNodes[val.priorityLevel - 1]
        : { nodeUserList: [], conditionList: [] }
      this.conditionLen = this.conditionsConfig.conditionNodes.length - 1
    }
  },

  computed: {
    listIds() {
      let arr = []
      this.conditionConfig.conditionList.map((item) => {
        arr.push(item.field)
      })
      return arr
    },
    // 设置条件内容
    additional_search() {
      return this.$store.state.business.fieldOptions.list
    },
    // 设置条件内容
    additional_Type() {
      return this.$store.state.business.fieldOptions.type
    }
  },

  watch: {
    additional_search(val) {
      this.conditionConfig.conditionList = val
    },
    id(val) {
      this.getList(val)
    }
  },

  methods: {
    getScopeFrame() {
      let data = {
        scope: 1
      }
      frameTreeApi(data).then((res) => {
        this.frameTreeData = res.data
      })
    },
    changeValue(item, index) {
      if (item.category) {
        if (['in', 'not_in'].includes(item.value)) {
          this.onlyOne = false
        } else {
          this.onlyOne = true
        }
        this.conditionConfig.conditionList[index].option = ''
        if (item.category == 1) {
          this.conditionConfig.conditionList[index].options.depList = []
        }
        if (item.category == 2) {
          this.conditionConfig.conditionList[index].options.userList = []
        }
      }
      if (['is_empty', 'not_empty'].includes(item.value)) {
        this.conditionConfig.conditionList[index].option = ''
        if (item.category == 1) {
          this.conditionConfig.conditionList[index].options.depList = []
        }
        if (item.category == 2) {
          this.conditionConfig.conditionList[index].options.userList = []
        }
      }
    },

    // 获取条件
    getList(id) {
      let data = {
        approve: 1
      }

      dataDatabaseFieldsApi(id, data).then((res) => {
        let arr1 = res.data.filter((item) => {
          return !['file', 'image'].includes(item.type)
        })
        this.conditions = arr1
        this.conditions.forEach((item, index) => {
          if (item.form_value == 'cascader_address' || item.type == 'cascader_address') {
            this.getCityList(index)
          }
        })
      })
    },
    getCityList(index, item) {
      let obj = {
        type_id: 2
      }

      getDictTreeListApi(obj).then((res) => {
        this.conditions[index].options = res.data
      })
    },

    heightInput() {
      setTimeout(() => {
        const height = this.$refs.getHeight[0].clientHeight
        this.heightInputRole = height === 0 ? 36 : height
      }, 200)
    },
    // 打开一对一表格
    handleTable(row, index) {
      this.activeIndex = index
      this.$refs.tableDialog.openBox(row.id)
    },

    getSelection(data, item) {
      item.options = [data]
      item.option = data.id
      this.activeIndex = -1
    },

    // 选中客户标签成功回调
    handleLabelConf(res, item) {
      item.optionsList = res.list
      item.option = res.ids
    },

    addCondition() {
      if (this.max && this.conditionConfig.conditionList.length > this.max - 1) {
        this.$message.error('最多只能添加9个条件')
        return false
      }
      this.conditionConfig.conditionList.push({ field: '', value: '', type: 'input', form_value: 'input' })
    },

    close() {
      let arr = JSON.parse(JSON.stringify(this.conditionConfig.conditionList))
      this.$store.commit('uadatefieldOptions', {
        list: [],
        resetList: arr,
        additional_search_boolean: ''
      })
      this.conditionConfig.conditionList = []
      if (this.max) {
        this.$emit('saveCondition', {
          list: this.conditionConfig.conditionList,
          additional_search_boolean: this.additional_search_boolean,
          type: this.additional_Type
        })
        // this.$store.commit('updateConditionDialog', false)
      }
    },

    itemConditions(id, index) {
      let row = {}
      this.conditions.map((item) => {
        if (item.field === id) {
          row = item
        }
      })

      let data = {}
      if (this.frame.includes(row.field) || row.is_frame) {
        // 部门
        data = {
          field: row.field,
          title: row.title,
          type: row.type,
          form_value: 'input',
          options: {
            depList: []
          },
          option: '',
          value: 'in',
          category: 1
        }
      } else if (this.member.includes(row.field) || row.is_user) {
        // 申请人
        data = {
          field: row.field,
          title: row.title,
          type: row.type,
          form_value: 'input',
          options: {
            userList: []
          },
          option: '',
          value: 'in',
          category: 2
        }
      } else {
        data = {
          field: row.field,
          optionsList: [],
          id: row.id,
          title: row.title,
          type: row.type,
          options: row.options,
          form_value: '',
          value: '',
          option: '',
          min: '',
          max: ''
        }

        if (data.type == 'switch') {
          data.form_value = 'switch'
        } else if (data.type == 'input_select') {
          data.form_value = 'input_select'
          this.rowId = row.id
        } else if (this.input.includes(row.type)) {
          data.form_value = 'input'
        } else if (this.select.includes(row.type)) {
          data.form_value = 'select'
        } else if (this.date.includes(row.type)) {
          data.form_value = 'date'
        } else if (this.number.includes(row.type)) {
          data.form_value = 'number'
        }
      }

      this.$nextTick(() => {
        this.$set(this.conditionConfig.conditionList, index, data)
      })
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

    // 选择成员回调
    getSelectList(data, item) {
      if (data.length > 0) {
        data.forEach((item) => {
          item.id = item.value
        })
      }
      item.options.userList = data
      let arr = []
      data.map((item) => {
        arr.push(item.id)
      })
      item.option = arr
      this.activeIndex = -1
    },

    // 选择部门完成回调
    changeMastart(data, item) {
      item.options.depList = data
      let arr = []
      data.map((item) => {
        arr.push(item.id)
      })
      item.option = arr
      this.activeIndex = -1
    },

    cardTag(index, type, itemIndex) {
      let arr = []
      if (type == 1) {
        this.conditionConfig.conditionList[itemIndex].optionsList.splice(index, 1)
        this.conditionConfig.conditionList[itemIndex].optionsList.map((item) => {
          arr.push(item.id)
        })
        this.conditionConfig.conditionList[itemIndex].option = arr
      } else if (type == 2) {
        this.conditionConfig.conditionList[itemIndex].options.userList.splice(index, 1)
      } else {
        this.conditionConfig.conditionList[itemIndex].options.depList.splice(index, 1)
      }
    },

    // 保存设置
    saveCondition() {
      var condition = this.conditionConfig.conditionList
      if (condition.length > 0 && this.noRule) {
        for (let i = 0; i < condition.length; i++) {
          const value = condition[i]
          if (!value.value) {
            this.$message.error(value.title + '条件不能为空')
            return
          }
          if (value.value == 'is_empty' || value.value == 'not_empty') {
          } else if (value.value == 'between' && value.type !== 'date_time_picker') {
            if (value.max == '' || value.min == '') {
              this.$message.error(value.title + '不能为空')
              return
            }
          } else if (['n_day', 'last_day', 'next_day'].includes(value.value)) {
            if (value.option == '' || !value.option) {
              this.$message.error(value.title + '不能为空')
              return
            }
          } else if (['today', 'week', 'month', 'quarter', 'year'].includes(value.value)) {
          } else {
            if (value.category == 2 && value.options.userList.length == 0) {
              this.$message.error(value.title + '不能为空')
              return
            } else if (value.category == 1 && value.options.depList.length == 0) {
              this.$message.error(value.title + '不能为空')
              return
            } else if ((value.value !== 'is_empty' || value.value !== 'not_empty') && value.option == '') {
              this.$message.error(value.title + '不能为空')
              return
            }
          }
        }
      }

      this.$store.commit('updateConditionDialog', false)
      if (this.eventStr !== 'event') {
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
      } else {
        this.$store.commit('uadatefieldOptions', {
          list: this.conditionConfig.conditionList,
          additional_search_boolean: this.additional_search_boolean,
          type: this.additional_Type
        })
        this.$emit('saveCondition', {
          list: this.conditionConfig.conditionList,
          additional_search_boolean: this.additional_search_boolean,
          type: this.additional_Type
        })
      }
    },

    closeCondition() {
      this.conditionsPopover = false
      this.conditions.forEach((item) => {
        item.disabled = false
      })
      this.$emit('updateConditionDialog')
      this.$store.commit('updateConditionDialog', false)
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

.flex {
  display: flex;
  align-items: center;
}
.flex-end {
  padding: 10px 20px 10px 0;

  display: flex;
  align-items: flex-end;
  justify-content: flex-end;
  .el-button {
    padding: 9px 16px;
    font-size: 13px;
  }
}

.el-icon-circle-plus {
  font-size: 16px;
  margin-right: 5px;
}
.condition_copyer {
  .el-drawer__body {
    .priority_level {
      background: rgba(255, 255, 255, 1);
      border-radius: 4px;
      border: 1px solid rgba(217, 217, 217, 1);
    }
  }
}
.condition_content {
  padding: 10px 0;

  ::v-deep .el-input-number--medium,
  ::v-deep .el-select--medium {
    width: 100%;
  }
  ::v-deep .el-input__inner {
    text-align: left;
  }
}
.pb30 {
  padding-bottom: 30px;
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
    min-height: 32px;
    line-height: 32px;
    outline: none;
    font-size: 12px;
    padding: 0 15px;

    -webkit-transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    width: 100%;
  }
  .list {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    &:last-of-type {
      margin-bottom: 10px;
    }
    .condition-icon {
      font-size: 18px;
      margin-top: 4px;
    }
  }
}
.conditions {
  position: relative;
  .conditions-popover {
    max-height: 350px;
    overflow-y: scroll;
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
  .conditions-popover::-webkit-scrollbar {
    height: 0;
    width: 0;
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
    top: 9px;
  }
}
.mr10 {
  margin-right: 10px;
}
.number {
  flex-shrink: 0; // flex布局下图片挤压变形
  margin-right: 10px;
  border: 1px solid #2c7ef8;
  border-radius: 50%;
  color: #2c7ef8;
  display: block;
  font-size: 12px;
  height: 20px;
  line-height: 18px;
  font-weight: 500;
  text-align: center;
  width: 20px;
  border-color: #2c7ef8;
}
.el-icon-delete {
  font-size: 16px;
  color: #ccc;
}
::v-deep .el-divider {
  background: hsl(223, 13%, 89%);
}
::v-deep .el-divider--horizontal {
  margin: 10px 0 20px 0;
}
.p20 {
  padding: 0 20px;
  padding-top: 20px;
}
</style>
