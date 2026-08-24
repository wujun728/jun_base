<!-- @FileDescription: 低代码-触发器更新规则-动态内容组件-->
<template>
  <div>
    <div>
      <el-form-item v-if="type === 'group_aggregate'" label="分组字段关联" class="mt14">
        <div v-for="(item, index) in groupList" :key="index">
          <div class="flex">
            <div>
              <div class="prompt">目标字段</div>
              <el-select
                size="small"
                v-model="item.form_field_uniqid"
                placeholder="请搜索选择目标字段"
                style="width: 300px"
                filterable
              >
                <el-option
                  v-for="item in groupField"
                  :key="item.id"
                  :label="item.field_name"
                  :value="item.form_field_uniqid"
                  :disabled="groupIds.includes(item.form_field_uniqid)"
                >
                </el-option>
              </el-select>
            </div>

            <div class="ml14">
              <div class="prompt">源字段</div>
              <!-- 源字段选择 -->
              <el-select
                size="small"
                v-model="item.to_form_field_uniqid"
                placeholder="请搜索选择源字段"
                style="width: 300px"
                filterable
              >
                <el-option v-for="(item, index) in field" :key="index" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </div>
        <span @click.stop="addNewGroup()" class="pointer default-color fz-12"
          ><span class="el-icon-plus mr5"></span>添加字段</span
        >
      </el-form-item>

      <!-- 字段聚合/字段更新 -->
      <el-form-item :label="getLabel(type)" class="mt14">
        <div v-for="(item, index) in updateList" :key="index">
          <el-col :span="8"><div class="grid-content bg-purple"></div></el-col>
          <el-col :span="4"><div class="grid-content bg-purple"></div></el-col>

          <el-row :gutter="0">
            <el-col :span="type == 'push_data' ? 10 : 5">
              <div>
                <div class="prompt">目标字段</div>
                <el-select
                  size="small"
                  v-model="item.form_field_uniqid"
                  placeholder="请搜索选择目标字段"
                  style="width: 100%"
                  filterable
                  @change="changeTargetField($event, item)"
                >
                  <el-option
                    v-for="item in targetField"
                    :key="item.id"
                    :label="item.field_name"
                    :disabled="listIds.includes(item.form_field_uniqid)"
                    :value="item.form_field_uniqid"
                  >
                    <span>{{ item.field_name }} <span v-if="item.is_uniqid == 1"> (唯一)</span></span>
                  </el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="5" v-if="type !== 'push_data'">
              <div class="mr14 ml14" v-if="!isUniqidFn(item.form_field_uniqid, targetField)">
                <div class="prompt">
                  {{ type == 'field_aggregate' || type == 'group_aggregate' ? '聚合方式' : '更新方式' }}
                </div>

                <el-select
                  size="small"
                  v-model="item.operator"
                  placeholder="请选择更新方式"
                  style="width: 100%"
                  filterable
                >
                  <el-option
                    v-for="el in options"
                    :key="el.value"
                    :label="el.label"
                    :value="el.value"
                    :disabled="el.value == 'formula_value' && !numBerList.includes(getType(item.form_field_uniqid))"
                  >
                  </el-option>
                </el-select>
              </div>
              <div class="mr14 ml14" v-else>
                <div class="prompt">更新方式</div>
                <el-select
                  size="small"
                  v-model="item.operator"
                  placeholder="请选择更新方式"
                  style="width: 100%"
                  filterable
                >
                  <el-option v-for="item in uniqidOptions" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="10" v-if="type !== 'push_data'">
              <div v-if="item.operator !== 'null_value'">
                <div class="prompt">{{ getName(type, item.operator) }}</div>

                <!-- 源字段选择 -->
                <el-select
                  v-if="
                    ['skip_value', 'field_value', 'cover_value'].includes(item.operator) || type == 'field_aggregate'
                  "
                  size="small"
                  v-model="item.to_form_field_uniqid"
                  placeholder="请搜索选择源字段"
                  style="width: 100%"
                  filterable
                >
                  <el-option v-for="(item, index) in field" :key="index" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>

                <!-- 计算公式 -->
                <div v-else-if="item.operator === 'formula_value'" class="textPosition">
                  <template v-if="numBerList.includes(getType(item.form_field_uniqid))">
                    <el-input
                      placeholder="请输计算公式"
                      size="small"
                      @focus="openDialog(index, item.value)"
                      v-model="item.value"
                    >
                    </el-input>
                  </template>
                </div>
                <!-- 固定值选择 -->
                <div v-else>
                  <!-- 下拉选择 -->
                  <el-select
                    size="small"
                    v-model="item.value"
                    :multiple="item.type == 'checkbox'"
                    v-if="['select', 'checkbox', 'radio'].includes(getType(item.form_field_uniqid))"
                    style="width: 100%"
                    filterable
                  >
                    <el-option
                      v-for="items in getOptions(item.form_field_uniqid)"
                      :key="items.value"
                      :value="items.value"
                      :label="items.name"
                    ></el-option>
                  </el-select>
                  <el-select
                    v-model="item.value"
                    style="width: 100%"
                    v-if="getType(item.form_field_uniqid) === 'switch'"
                  >
                    <el-option value="1" label="是"></el-option>
                    <el-option value="0" label="否"></el-option>
                  </el-select>

                  <!-- 输入框类型 -->
                  <el-input
                    v-model="item.value"
                    style="width: 100%"
                    size="small"
                    v-if="
                      getType(item.form_field_uniqid) === 'input' ||
                      !item.form_field_uniqid ||
                      getType(item.form_field_uniqid) === 'textarea'
                    "
                  ></el-input>

                  <!-- 数字类型 -->
                  <template v-if="numBerList.includes(getType(item.form_field_uniqid))">
                    <el-input-number v-model="item.value" :controls="false" style="width: 100%"></el-input-number>
                  </template>

                  <!-- 级联选择 -->
                  <el-cascader
                    v-model="item.value"
                    :options="
                      getType(item.form_field_uniqid) == 'cascader_address'
                        ? addressList
                        : getOptions(item.form_field_uniqid)
                    "
                    :props="{
                      checkStrictly: true,
                      label: 'name',
                      value: 'value',
                      expandTrigger: 'hover',
                      multiple: ['cascader'].includes(getType(item.form_field_uniqid)) ? true : false
                    }"
                    style="width: 100%"
                    v-if="['cascader', 'cascader_address', 'cascader_radio'].includes(getType(item.form_field_uniqid))"
                    filterable
                  ></el-cascader>

                  <!-- 日期选择 -->
                  <div v-if="getType(item.form_field_uniqid) === 'date_picker'">
                    <el-date-picker style="width: 100%" v-model="item.value" type="date" placeholder="选择日期时间">
                    </el-date-picker>
                  </div>

                  <!-- 日期时间选择 -->
                  <div v-if="getType(item.form_field_uniqid) === 'date_time_picker'">
                    <el-date-picker style="width: 100%" v-model="item.value" type="datetime" placeholder="选择日期时间">
                    </el-date-picker>
                  </div>
                  <!-- 人员 -->
                  <select-member
                    v-if="['user_id', 'update_user_id', 'owner_user_id'].includes(getValue(item.form_field_uniqid))"
                    :onlyOne="true"
                    :value="item.options || []"
                    @getSelectList="getSelectList($event, item)"
                    style="width: 100%"
                  >
                  </select-member>

                  <select-department
                    v-if="['frame_id'].includes(getValue(item.form_field_uniqid))"
                    :onlyOne="true"
                    :value="item.options || []"
                    @changeMastart="changeMastart($event, item)"
                    style="width: 100%"
                  ></select-department>

                  <!-- 一对一关联字段 -->
                  <select-one
                    v-if="
                      getType(item.form_field_uniqid) === 'input_select' &&
                      !['frame_id', 'user_id', 'update_user_id', 'owner_user_id'].includes(
                        getValue(item.form_field_uniqid)
                      )
                    "
                    :value="item.value || {}"
                    :id="getId(item)"
                    style="width: 100%"
                    @getSelection="getSelection($event, item)"
                  >
                  </select-one>

                  <!-- 标签组 -->
                  <select-label
                    v-if="getType(item.form_field_uniqid) === 'tag'"
                    :list="getOptions(item.form_field_uniqid)"
                    :value="item.options || []"
                    style="width: 100%"
                    :props="{ children: 'children', label: 'name' }"
                    @handleLabelConf="handleLabelConf($event, item)"
                  ></select-label>
                </div>
              </div>
            </el-col>
            <el-col :span="2">
              <div class="el-icon-delete" @click="removeItems(item, index)"></div>
            </el-col>
          </el-row>
        </div>
        <span @click.stop="addNewLine()" class="pointer default-color fz-12"
          ><span class="el-icon-plus mr5"></span>添加字段</span
        >
        <template v-if="type == 'push_data'">
          <div class="prompt">推送字段示例</div>
          <div class="up-box">
            {"action":"{{ action[action.length - 1] || '' }}","data":{{ getUpdateObj(updateList) }}}
          </div>
        </template>
      </el-form-item>
    </div>
    <!-- 计算公式 -->
    <oa-dialog :fromData="fromData" ref="oaDialog" @submit="submit">
      <el-input class="textPosition" type="textarea" :rows="5" placeholder="请输入计算公式" v-model="value"> </el-input>
      <el-popover placement="left" trigger="hover">
        <div class="field-box">
          <div class="field-text over-text" v-for="(val, index) in numfieldList" :key="index" @click="handleClick(val)">
            {{ val.label }}
          </div>
        </div>
        <span class="el-icon-chat-dot-square icon" slot="reference"></span>
      </el-popover>
    </oa-dialog>
  </div>
</template>

<script>
import { getDictTreeListApi } from '@/api/form'
export default {
  props: {
    type: {
      type: String,
      default: ''
    },
    groupList: {
      type: Array,
      default: () => []
    },
    list: {
      type: Array,
      default: []
    },
    options: {
      // 更新方式
      type: Array,
      default: []
    },
    action: {
      // 执行动作
      type: Array,
      default: []
    },
    uniqidOptions: {
      // 字段唯一值的更新方式
      type: Array,
      default: []
    },
    targetField: {
      // 目标字段选项
      type: Array,
      default: () => []
    },
    groupField: {
      // 分组聚合目标字段选项
      type: Array,
      default: () => []
    },
    field: {
      // 源字段
      type: Array,
      default: []
    }
  },
  components: {
    tableDialog: () => import('@/components/develop/tableDialog'),
    selectMember: () => import('@/components/form-common/select-member'),
    selectDepartment: () => import('@/components/form-common/select-department'),
    oaDialog: () => import('@/components/form-common/dialog-form'),
    selectOne: () => import('@/components/form-common/select-one'),
    selectLabel: () => import('@/components/form-common/select-label')
  },

  data() {
    return {
      fromData: {
        width: '550px',
        title: '计算公式',
        btnText: '确定',
        labelWidth: '90px',
        type: 'slot'
      },
      activeIndex: 0,
      value: '',
      updateList: [],
      numBerList: ['input_number', 'input_float', 'input_percentage', 'input_price'],
      labelList: [],
      oneList: [],
      addressList: [],
      activeIndex: 0,
      heightInputRole: 32
    }
  },
  watch: {},
  created() {
    if (this.list.length > 0) {
      this.updateList = this.list
    }
    this.getCityList()
  },
  computed: {
    numfieldList() {
      return this.field.filter((item) => this.numBerList.includes(item.form_value))
    },
    listIds() {
      const ids = []
      this.updateList.map((item) => {
        ids.push(item.form_field_uniqid)
      })
      return ids
    },
    groupIds() {
      const ids = []
      this.groupList.map((item) => {
        ids.push(item.form_field_uniqid)
      })
      return ids
    }
  },
  methods: {
    changeTargetField(val, item) {
      let index = this.targetField.findIndex((el) => el.form_field_uniqid === val)
      item.field_name = this.targetField[index].field_name
    },
    submit(data) {
      var reg = new RegExp('[\\u4E00-\\u9FFF]+', 'g')
      if (reg.test(this.value)) {
        this.$message.error('请输入正确格式的计算公式')
        return false
      }
      this.updateList[this.activeIndex].value = this.value
      this.$forceUpdate()
      this.$refs.oaDialog.handleClose()
      this.value = ''
    },
    openDialog(index, val) {
      if (val) {
        this.value = val
      }
      this.activeIndex = index
      this.$refs.oaDialog.openBox()
    },
    getName(type, val) {
      let str = ''
      if (type == 'field_aggregate') {
        str = '聚合字段'
      } else if (val == 'field_value') {
        str = '源字段'
      } else if (val == 'formula_value') {
        str = '计算公式'
      } else {
        str = '固定值'
      }
      return str
    },
    handleClick(val) {
      this.value = this.value + '{' + val.value + '}'
    },

    addNewLine() {
      let obj = { form_field_uniqid: '', operator: this.options[0].value, to_form_field_uniqid: '' }
      this.updateList.push(obj)
    },

    isUniqidFn(val, list) {
      let obj = list.filter((item) => {
        if (item.field_name_en == val) {
          return item
        }
      })
      if (obj[0] && obj[0].is_uniqid) {
        return obj[0].is_uniqid == '1' ? true : false
      } else {
        return false
      }
    },
    getUpdateObj(list) {
      let obj = {}
      for (let key in list) {
        obj[list[key].form_field_uniqid] = ''
      }
      return obj
    },

    removeItems(item, index) {
      this.updateList.splice(index, 1)
    },
    getCityList() {
      let obj = {
        type_id: 2
      }
      getDictTreeListApi(obj).then((res) => {
        this.addressList = res.data
      })
    },
    // 选择成员回调
    getSelectList(data, item) {
      let arr = []
      data.map((item) => {
        arr.push(item.value)
      })
      item.options = data
      item.value = arr
    },

    // 选择部门回调
    changeMastart(data, item) {
      let arr = []
      data.map((item) => {
        arr.push(item.id)
      })
      item.options = data
      item.value = arr
    },

    cleanUp() {
      this.updateList = []
    },

    addNewGroup() {
      let obj = { form_field_uniqid: '', to_form_field_uniqid: '' }
      this.groupList.push(obj)
    },
    // 打开一对一表格
    getId(item) {
      let id = null
      this.targetField.map((val) => {
        if (val.form_field_uniqid == item.form_field_uniqid) {
          id = val.id
        }
      })

      return id
    },

    getSelection(data, item) {
      item.value = data
      // this.$set(this.updateList[this.activeIndex], 'value', data)
    },

    getLabel(val) {
      let obj = {
        field_update: '更新规则',
        field_aggregate: '聚合规则',
        group_aggregate: '聚合规则',
        auto_create: '创建规则'
      }
      return obj[val]
    },

    // 选中客户标签成功回调
    handleLabelConf(res, item) {
      item.options = res.list
      item.value = res.ids
    },

    heightInput() {
      setTimeout(() => {
        const height = this.$refs.getHeight[0].clientHeight
        this.heightInputRole = height === 0 ? 36 : height
      }, 200)
    },

    getType(id) {
      let index = this.targetField.findIndex((item) => item.form_field_uniqid === id)
      if (index >= 0) {
        return this.targetField[index].form_value
      }
    },

    getOptions(id) {
      let index = this.targetField.findIndex((item) => item.form_field_uniqid === id)
      return this.targetField[index].data_dict
    },
    getValue(id) {
      let index = this.targetField.findIndex((item) => item.form_field_uniqid === id)
      return this.targetField[index].field_name_en
    },

    updateFn() {
      return () => this.updateList
    }
  }
}
</script>
<style scoped lang="scss">
.plan-footer-one {
  width: 100%;
  height: 32px;
  line-height: 32px;
  .placeholder {
    font-size: 12px;
    color: #ccc;
  }
  span {
    margin-right: 6px;
  }
}
.prompt {
  font-size: 13px;
  color: #909399;
}
.fz-12 {
  font-size: 13px;
}
.el-icon-delete {
  cursor: pointer;
  margin: 0 10px;
  margin-top: 45px;
}
.up-box {
  border: 1px solid #e4e8ef;
  background: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
}
.textPosition {
  position: relative;
}
.icon {
  font-size: 16px;
  position: absolute;
  right: 30px;
  bottom: 80px;
}
::v-deep .el-textarea__inner {
  resize: none;
  font-size: 13px;
}

.line {
  border-bottom: 1px solid #e8ebf2;
  margin: 10px 0;
}
.field-text {
  cursor: pointer;
  height: 32px;
  line-height: 32px;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
}
.field-text:hover {
  background: #f7fbff;
  color: #1890ff;
}
.field-box {
  height: 350px;
  overflow-y: auto;
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */
}
.field-box::-webkit-scrollbar {
  height: 0;
  width: 0;
}
</style>
