<template>
  <div class="flex">
    <template v-if="type">
      <el-input
        v-model="ruleForm.keyword_default"
        :placeholder="`请输入关键字`"
        class="search-form search-width"
        clearable
        prefix-icon="el-icon-search"
        size="small"
        @change="handleEmit()"
      >
      </el-input>
      <manage-range
        v-if="type == 0"
        ref="manageRange"
        :all="`all`"
        :scopeFrames="scopeFrames"
        class="search-form"
        @change="changeFrame"
      ></manage-range>
    </template>
    <div v-for="(val, index) in list" :key="index" class="form-box">
      <!-- 表单 -->
      <div>
        <manage-range
          v-if="['manage'].includes(val.form_value)"
          ref="manageRange"
          :all="`all`"
          class="search-form"
          @change="changeFrame"
        ></manage-range>
        <el-input
          v-if="
            ['input', 'input_number', 'textarea', 'input_float', 'input_price', 'input_percentage'].includes(
              val.form_value
            )
          "
          v-model="ruleForm[val.field_name_en]"
          :placeholder="`请输入${val.field_name}`"
          class="mr10"
          clearable
          prefix-icon="el-icon-search"
          size="small"
          style="width: 200px"
          @change="handleEmit(val)"
        >
        </el-input>

        <!-- 下拉选择类型 -->
        <el-select
          v-if="['radio', 'select', 'checkbox'].includes(val.form_value)"
          v-model="ruleForm[val.field_name_en]"
          :clearable="!val.hasOwnProperty('value')"
          :multiple="val.multiple"
          :placeholder="`${val.field_name}`"
          class="mr10"
          collapse-tags
          filterable
          size="small"
          style="min-width: 120px"
          v-bind="val.props || {}"
          @change="handleEmit(val)"
        >
          <el-option
            v-for="items in val.data_dict"
            :key="items.value || items.id"
            :label="items.name || items.label"
            :value="items.value || items.id"
          ></el-option>
        </el-select>
        <el-select
          v-if="val.form_value === 'switch'"
          v-model="ruleForm[val.field_name_en]"
          :placeholder="`${val.field_name}`"
          class="mr10"
          clearable
          filterable
          size="small"
          @change="handleEmit(val)"
        >
          <el-option label="是" value="1"></el-option>
          <el-option label="否" value="0"></el-option>
        </el-select>

        <!-- 数字类型 -->
        <el-input-number
          v-if="val.form_value == 'number'"
          v-model="ruleForm[val.field_name_en]"
          :controls="false"
          :min="0"
          :placeholder="`${val.field_name}`"
          class="mr10"
          @change="handleEmit(val)"
        ></el-input-number>

        <!-- 低代码选择实体单选 -->
        <div v-if="val.form_value == 'cascaderSelect'" class="mr10">
          <el-cascader
            v-model="ruleForm[val.field_name_en]"
            :options="val.data_dict"
            :show-all-levels="false"
            clearable
            filterable
            placeholder="请选择实体"
            size="small"
            style="width: 100%"
            @change="handleEmit(val)"
          >
          </el-cascader>
        </div>

        <!-- 地区选择 -->

        <el-cascader
          v-if="['cascader_address'].includes(val.form_value)"
          v-model="ruleForm[val.field_name_en]"
          :options="addressList"
          :placeholder="`${val.field_name}`"
          :props="{
            checkStrictly: false,
            label: 'name',
            value: 'id',
            multiple: true,
            emitPath: false
          }"
          class="mr10"
          clearable
          collapse-tags
          filterable
          size="small"
          v-bind="val.props || {}"
          @change="handleEmit(val)"
        ></el-cascader>
        <!-- 级联选择-多选 -->
        <el-cascader
          v-if="val.form_value == 'cascader' || val.form_value == 'cascader_radio'"
          v-model="ruleForm[val.field_name_en]"
          :options="val.data_dict"
          :placeholder="`${val.field_name}`"
          :props="{
            checkStrictly: false,
            label: 'name',
            value: 'value',
            multiple: true
          }"
          class="mr10"
          clearable
          collapse-tags
          size="small"
          @change="handleEmit(ruleForm[val.field_name_en], val)"
        ></el-cascader>

        <!-- 日期选择 -->
        <el-date-picker
          v-if="val.form_value === 'date_time_picker' || val.form_value === 'date_picker'"
          v-model="val.data_dict"
          :clearable="val.data_dict && !val.data_dict.length > 0"
          :end-placeholder="`${val.field_name_end ? val.field_name_end : val.field_name}`"
          :picker-options="pickerOptions"
          :range-separator="$t('toptable.to')"
          :start-placeholder="`${val.field_name}`"
          class="time mr10"
          format="yyyy/MM/dd"
          size="small"
          style="width: 250px"
          type="daterange"
          value-format="yyyy/MM/dd"
          @change="onchangeTime($event, val)"
        />

        <!-- 月份选择 -->
        <el-date-picker
          v-if="val.form_value === 'month'"
          v-model="val.data_dict"
          :placeholder="val.field_name"
          class="time mr10"
          format="yyyy-MM"
          size="small"
          type="month"
          value-format="yyyy-MM"
          @change="onchangeTime($event, val)"
        >
        </el-date-picker>
        <!-- 月份选择区间 -->
        <el-date-picker
          v-if="val.form_value === 'monthrange'"
          v-model="val.data_dict"
          :end-placeholder="`${val.field_name}`"
          :start-placeholder="`${val.field_name}`"
          class="time mr10"
          format="yyyy/MM"
          range-separator="至"
          size="small"
          type="monthrange"
          value-format="yyyy/MM"
          @change="onchangeTime($event, val)"
        >
        </el-date-picker>

        <!-- 一对一 -->
        <div
          v-if="
            val.form_value == 'input_select' && !member.includes(val.field_name_en) && val.field_name_en !== 'frame_id'
          "
          class="mr10"
          style="width: 200px"
        >
          <select-one
            v-if="
              val.form_value == 'input_select' &&
              !member.includes(val.field_name_en) &&
              val.field_name_en !== 'frame_id'
            "
            :id="val.id"
            :placeholder="val.field_name"
            :value="val.data_dict || {}"
            class="mr10"
            style="width: 200px"
            @getSelection="getSelection($event, val)"
          ></select-one>
        </div>

        <!-- 选择人员 -->
        <div v-if="member.includes(val.field_name_en)" class="mr10" style="width: 200px">
          <select-member
            v-if="member.includes(val.field_name_en)"
            :only-one="!val.onlyOne ? false : true"
            :placeholder="val.field_name"
            :isSearch="true"
            :value="val.data_dict || []"
            class="mr10"
            style="width: 200px"
            @getSelectList="getSelectList($event, val)"
          ></select-member>
        </div>

        <!-- 选择部门 -->
        <div v-if="val.field_name_en === 'frame_id'" class="mr10" style="width: 200px">
          <select-department
            :isSearch="true"
            :only-one="true"
            :placeholder="val.field_name"
            :value="val.data_dict || []"
            @changeMastart="changeMastart($event, val)"
          ></select-department>
        </div>

        <!-- 选择标签 -->
        <div v-if="val.form_value === 'tag'" class="mr10" style="width: 200px">
          <select-label
            ref="selectLabel"
            :isSearch="true"
            :labelList="labelList"
            :list="val.data_dict || []"
            :placeholder="val.field_name"
            :props="{ children: 'children', label: 'name' }"
            class="mr10"
            style="width: 200px"
            @handleLabelConf="handleLabelConf($event, val)"
          ></select-label>
        </div>
      </div>
    </div>
    <div>
      <el-tooltip content="重置搜索条件" effect="dark" placement="top">
        <div class="reset" @click="resetSearch()"><i class="iconfont iconqingchu"></i></div>
      </el-tooltip>
    </div>
  </div>
</template>

<script>
import { getDictTreeListApi } from '@/api/form'
export default {
  name: 'CustomerForm',
  components: {
    uploadFile: () => import('@/components/form-common/oa-upload'),
    ueditorFrom: () => import('@/components/form-common/oa-wangeditor'),
    manageRange: () => import('@/components/form-common/select-manageRange'),
    selectMember: () => import('@/components/form-common/select-member'),
    selectDepartment: () => import('@/components/form-common/select-department'),
    selectLabel: () => import('@/components/form-common/select-label'),
    selectOne: () => import('@/components/form-common/select-one')
  },
  props: {
    list: {
      type: Array,
      default: () => {
        return []
      }
    },
    scopeFrames: {
      type: Array,
      default: () => {
        return []
      }
    },
    timeValue: {
      type: [Array, String],
      default: () => []
    },

    type: {
      type: String,
      default: ''
    },
    btnShow: {
      type: Boolean,
      default: true
    },
    is_develp: {
      type: Boolean,
      default: false
    },
    isTimeArray: {
      type: Boolean,
      default: true
    }
  },

  data() {
    return {
      drawer: true,
      saveLoading: false,
      activeVal: '',
      activeItem: {},

      ruleForm: {},
      form: {},
      rule: {},
      autosize: {
        minRows: 6
      },
      pickerOptions: this.$pickerOptionsTimeEle,
      activeIndex: null,
      fileParams: {
        relation_type: 'client', // 上传类型 客户：client  合同：contract
        relation_id: 0, // 关联id合同id或者别的id
        way: 2,
        eid: 0
      },
      member: ['user_id', 'update_user_id', 'owner_user_id', 'creator', 'salesman', 'test_uid'],
      heightInputRole: 32,
      memberShow: false,
      userList: [],
      addressList: [],
      timeVal: [],
      labelList: [], // 选中客户标签
      cityList: []
    }
  },
  watch: {
    list: {
      handler(newVal) {
        newVal.forEach((item, index) => {
          if (typeof item.value != 'undefined' && !this.ruleForm[item.field_name_en]) {
            this.ruleForm[item.field_name_en] = item.value
          }
        })
      },
      deep: true,
      immediate: true
    },
    timeValue: {
      handler(newVal) {
        this.timeVal = newVal
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.getCityList()
  },
  methods: {
    reset() {
      this.timeVal = []
      this.ruleForm.scope_frame = 'all'
      this.list.map((item) => {
        if (Object.hasOwnProperty.call(item, 'value')) {
          this.ruleForm[item.field_name_en] = item.value
        }
      })
    },

    // 选择时间区域
    onchangeTime(e, val) {
      if (val.data_dict == null) {
        this.ruleForm[val.field_name_en] = ''
        this.$emit('handleEmit', this.ruleForm)
      } else {
        if (typeof val.data_dict === 'string') {
          this.ruleForm[val.field_name_en] = val.data_dict
        } else {
          this.ruleForm[val.field_name_en] = val.data_dict.join('-')
        }
        this.handleEmit(this.activeVal, this.activeItem)
      }
    },

    handleEmit(val, item) {
      this.activeVal = val
      this.activeItem = item

      // let form = JSON.parse(JSON.stringify(this.ruleForm))
      // 低代码和其他级联多选数据结构不一样
      // if (val && !this.is_develp && item) {
      //   form[item.field_name_en].forEach((value, index) => {
      //     form[item.field_name_en][index] = '[' + value.join(',') + ']'
      //   })
      // }
      this.$emit('handleEmit', this.ruleForm)
      this.$forceUpdate()
    },

    // 打开一对一表格
    handleTable(row, index) {
      this.activeIndex = index
      this.$refs.tableDialog.openBox(row.id)
    },

    oneLabel(index) {
      this.list[index].data_dict = []
      let key = this.list[index].field_name_en
      this.ruleForm[key] = ''
      this.$emit('handleEmit', this.ruleForm)
    },

    resetSearch() {
      this.labelList = []
      this.ruleForm.keyword_default = ''
      this.ruleForm = {}

      this.reset()
      if (this.$refs.selectLabel) {
        this.$refs.selectLabel.selectList = []
      }
      setTimeout(() => {
        this.$refs.manageRange &&
          (this.type && this.type == 0 ? this.$refs.manageRange.reset() : this.$refs.manageRange[0].reset())
      }, 300)

      this.list.map((item) => {
        if (
          ['input_select', 'user_id', 'frame_id', 'date_picker', 'date_time_picker', 'monthrange', 'test_uid'].includes(
            item.form_value
          )
        ) {
          item.data_dict = []
        }
      })
      this.$emit('resetSearch')
    },
    // 选择人员回调
    getSelectList(data, val) {
      val.data_dict = data || []
      if (!val.onlyOne) {
        let ids = []
        data.forEach((item) => {
          ids.push(item.value)
        })
        this.ruleForm[val.field_name_en] = ids
      } else {
        this.ruleForm[val.field_name_en] = data[0] ? data[0].value : ''
      }

      this.handleEmit(this.activeVal, this.activeItem)
    },

    // 选择部门完成回调
    changeMastart(data, val) {
      val.data_dict = data || []
      this.ruleForm[val.field_name_en] = data[0] ? data[0].id : ''
      this.handleEmit(this.activeVal, this.activeItem)
    },

    // 选择一对一回调
    getSelection(data, val) {
      val.data_dict = data || []
      this.ruleForm[val.field_name_en] = data.id
      this.handleEmit(this.activeVal, this.activeItem)
      this.activeIndex = -1
    },

    // 选中标签回调
    handleLabelConf(data, val) {
      this.ruleForm[val.field_name_en] = data.ids
      this.handleEmit(this.activeVal, this.activeItem)
      this.activeIndex = -1
    },

    getCityList(index) {
      let obj = {
        type_id: 2
      }
      getDictTreeListApi(obj)
        .then((res) => {
          const { data, status } = res
          this.addressList = data
        })
        .catch((error) => {
          reject(error)
        })
    },

    changeFrame(e) {
      this.scope_frame = e
      this.ruleForm.scope_frame = this.scope_frame
      this.handleEmit(this.activeVal, this.activeItem)
    },

    heightInput() {
      setTimeout(() => {
        const height = this.$refs.getHeight[0].clientHeight
        this.heightInputRole = height === 0 ? 36 : height
      }, 200)
    },

    // 删除客户标签
    cardTag(index, index_s, type) {
      if (type === 1) {
        // 删除人员
        let key = this.list[this.activeIndex]?.field_name_en
        this.list[index].data_dict = []
        this.ruleForm[key] = ''
      }
      this.$emit('handleEmit', this.ruleForm)
    }
  }
}
</script>

<style lang="scss" scoped>
.search-form {
  margin-right: 10px;
}
.search-width {
  width: 200px;
}

.flex {
  display: flex;
  flex-wrap: nowrap;
}
.flex-box {
  display: flex;
  align-items: center;
  line-height: 32px;
  .tag {
    background-color: #f0f2f5;
    border: none;
  }
}
.lh32 {
  margin-top: 3px;
}
.no-member {
  font-size: 13px;
  color: #c0c4cc;
}
.form-box {
  display: inline-block;
}
.plan-footer-one {
  width: 200px;
  height: 32px;
  line-height: 30px;
  .placeholder {
    font-size: 13px;
    color: #c0c4cc;
  }
  span {
    margin-right: 6px;
  }
}
.mt2 {
  margin-top: 3.5px;
}
.mt4 {
  margin-top: 4px;
}
::v-deep.el-cascader__tags .el-tag:first-child {
  max-width: 70%;
}

::v-deep.el-input__inner.select {
  white-space: nowrap;
  font-size: 0;
}
::v-deep.el-input__inner.select .el-tag {
  vertical-align: middle;
}
::v-deep.el-input__inner.select .el-tag:first-child {
  max-width: 70%;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
