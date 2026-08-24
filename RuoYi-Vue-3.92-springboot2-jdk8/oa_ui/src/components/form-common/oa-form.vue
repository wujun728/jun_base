<!-- @FileDescription: 动态表单页面 -->
<template>
  <div>
    <el-form ref="form" :model="form" :rules="formRules" label-width="auto">
      <el-form-item
        v-for="(item, key, index) in formConfig"
        :key="key"
        :label="item.label"
        :prop="item.key"
        v-if="form[item.isShow] != 0"
      >
        <!-- 单选 -->
        <el-radio-group v-if="item.type == 'radio'" v-model="form[item.key]" class="vertical">
          <el-radio v-for="(itemOption, index) in item.options" :key="index" :label="itemOption.label">{{
            itemOption.value
          }}</el-radio>
        </el-radio-group>

        <!-- 选择日期 -->
        <el-date-picker
          v-if="item.type == 'date'"
          size="small"
          v-model="form[item.key]"
          :placeholder="item.placeholder"
          type="date"
          :format="item.format"
          :value-format="item.format"
          style="width: 100%"
        >
        </el-date-picker>
        <!-- 输入框 -->
        <el-input
          v-if="item.type == 'input'"
          v-model="form[item.key]"
          :placeholder="item.placeholder"
          :maxlength="item.maxlength"
          size="small"
          style="width: 100%"
          :disabled="item.disabled"
          :show-word-limit="item.showWordLimit"
        ></el-input>
        <!-- 刷新生成英文输入框 -->
        <el-input
          v-if="item.type == 'inputEn'"
          :disabled="fromData.type == 'edit' ? true : false"
          v-model="form[item.key]"
          :placeholder="item.placeholder"
          size="small"
          class="refresh-input"
          @focus="refreshFn(item.refresh, item.key)"
        >
          <el-button
            type="primary"
            class="refresh"
            :disabled="fromData.type == 'edit' ? true : false"
            slot="suffix"
            size="small"
            @click.stop="refreshFn(item.refresh, item.key)"
          >
            刷新生成</el-button
          >
        </el-input>
        <!-- 文本域 -->
        <el-input
          class="textarea"
          v-if="item.type == 'textarea'"
          type="textarea"
          placeholder="请输入内容"
          :maxlength="item.maxlength"
          v-model="form[item.key]"
          :style="{ height: item.height }"
          :placeholder="item.placeholder"
          :show-word-limit="item.showWordLimit"
          size="small"
        >
        </el-input>

        <!-- 富文本 -->
        <ueditor-from
          v-if="item.type === 'richText'"
          ref="ueditorFrom"
          :border="true"
          :content="form[item.key]"
          :height="`400px`"
          @input="ueditorEdit($event, item)"
        >
        </ueditor-from>
        <!-- 密码输入框 -->
        <el-input
          type="password"
          v-if="item.type == 'password'"
          prefix-icon="el-icon-lock"
          v-model="form[item.key]"
          :placeholder="item.placeholder"
          show-password
          style="width: 100%"
          size="small"
        ></el-input>
        <!-- 计数器输入框 -->
        <div v-if="item.type == 'number'">
          <el-input-number
            style="width: 100%"
            size="small"
            v-model="form[item.key]"
            :min="item.min"
            :max="item.max"
          ></el-input-number>
          <!-- <span class="tips">{{ item.tips }}</span> -->
        </div>
        <!-- 数字输入框 -->
        <el-input
          v-if="item.type == 'inputNumber'"
          type="number"
          size="small"
          v-model="form[item.key]"
          :placeholder="item.placeholder"
        ></el-input>
        <!-- 开关组件 -->
        <el-switch
          v-if="item.type == 'switch'"
          v-model="form[item.key]"
          size="small"
          :active-value="item.activeValue"
          :inactive-value="item.inactiveValue"
          :active-text="item.activeText"
          :inactive-text="item.inactiveText"
        >
        </el-switch>
        <!-- 选择时间（月） -->
        <el-date-picker
          v-if="item.type == 'month'"
          style="width: 100%"
          v-model="form[item.key]"
          type="month"
          size="small"
          :picker-options="pickerOptions"
          placeholder="选择月"
        >
        </el-date-picker>
        <!-- 选择（单选） -->
        <div v-if="item.type == 'select'">
          <div class="flex">
            <el-select
              style="width: 100%"
              v-model="form[item.key]"
              :disabled="item.disabled"
              filterable
              size="small"
              @change="selectChange"
              :placeholder="item.placeholder"
            >
              <el-option
                v-for="(v, index) in item.options"
                :key="v.id"
                :label="v.label || v.name || v.table_name"
                :value="v.id || v.value"
              >
              </el-option>
            </el-select>
            <div v-if="item.sign == 'dict'" class="fang">
              <el-button size="small" @click="goDict(item)">添加</el-button>
            </div>
          </div>
          <!-- <span v-if="item.tips" class="tips">{{ item.tips }}</span> -->
        </div>

        <!-- 选择人员 -->
        <template v-if="item.type == 'user_id'">
          <select-member
            :onlyOne="item.only_one"
            :value="userList"
            :disabled="item.disabled"
            :disabledList="item.disabledList"
            @getSelectList="getSelectList($event, item)"
            style="width: 100%"
          >
          </select-member>

          <!-- <span v-if="item.tips" class="tips">{{ item.tips }}</span> -->
        </template>

        <!-- 一对一引用 -->
        <div
          v-if="item.type == 'input_select'"
          class="el-input__inner select plan-footer-on flex-between h32"
          @click="checkboxDialogOpen()"
        >
          <div class="over-text1" @click="checkboxDialogOpen()">
            <span @click="checkboxDialogOpen()" v-for="(items, indexs) in fieldList" :key="indexs" @click.stop="">
              {{ items.field_name }},
            </span>
          </div>
          <i class="el-tag__close el-icon-arrow-down" />
        </div>

        <!-- 选择（多选） -->
        <el-select
          style="width: 100%"
          v-if="item.type == 'multipleSelect'"
          v-model="form[item.key]"
          :disabled="fromData.type == 'edit'"
          multiple
          size="small"
          filterable
          :placeholder="item.placeholder ? item.placeholder : '请选择'"
        >
          <el-option v-for="(v, index) in item.options" :key="v.id" :label="v.name" :value="v.id"> </el-option>
        </el-select>

        <!--低代码选择应用-实体 -->
        <div v-if="item.type == 'cascaderSelect'">
          <el-cascader
            v-model="form[item.key]"
            :options="item.options"
            :show-all-levels="false"
            filterable
            size="small"
            style="width: 100%"
            clearable
            :placeholder="item.placeholder ? item.placeholder : '请选择'"
          >
          </el-cascader>
          <!-- <span v-if="item.tips" class="tips">{{ item.tips }}</span> -->
        </div>

        <!-- 级联选择器(自定义) -->
        <div v-if="item.type == 'cascader'">
          <el-cascader
            v-model="form[item.key]"
            :options="item.options"
            filterable
            size="small"
            style="width: 100%"
            :show-all-levels="false"
            :props="item.props"
            clearable
            :placeholder="item.placeholder ? item.placeholder : '请选择'"
          >
            <template slot-scope="{ node, data }">
              <span>{{ data.table_name }}</span>
              <span> （{{ data.table_name_en }}）</span>
            </template>
          </el-cascader>
          <!-- <span class="tips">{{ item.tips }}</span> -->
        </div>

        <div v-if="item.type == 'cascaderNew'">
          <el-cascader
            v-model="form[item.key]"
            :options="item.options"
            :props="{ checkStrictly: true }"
            clearable
            filterable
            style="width: 100%"
          ></el-cascader>
        </div>
        <div class="tips" v-if="item.tips">{{ item.tips }}</div>
      </el-form-item>
    </el-form>
    <!-- 引用实体弹窗 -->
    <checkboxDialog ref="checkboxDialog" @getData="getDataFn" :type="`field`" :showCrud="true"></checkboxDialog>
  </div>
</template>
<script>
import { getDictCreateApi, getDictListApi } from '@/api/form'
import { pinyin } from 'pinyin-pro'
import checkboxDialog from '@/components/develop/checkboxDialog'
import selectMember from '@/components/form-common/select-member'
import ueditorFrom from '@/components/form-common/oa-wangeditor'

export default {
  name: '',
  components: { checkboxDialog, selectMember, ueditorFrom },
  props: {
    // 弹窗样式
    fromData: {
      type: Object,
      default: () => {}
    },
    // 表单内容
    formConfig: {
      type: Array,
      default: () => []
    },
    // 表单绑定值
    formDataInit: {
      type: Object,
      default: () => {}
    },
    // 表单规则
    formRules: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      form: this.formDataInit,
      strData: '',
      onlyOne: false,
      fieldList: [],
      dictList: [],
      itemIndex: 0,
      itemData: {},
      userList: [], // 人员数据
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now()
        }
      }
    }
  },
  watch: {
    formDataInit(val) {
      if (val.association_field_names_list && val.association_field_names_list.length > 0) {
        this.fieldList = val.association_field_names_list
      }
      this.form = val
    }
  },

  methods: {
    getDataFn(data) {
      this.formDataInit.association_crud_id = data.id
      this.form.association_field_names = []
      this.fieldList = data.selectList
      data.selectList.map((item) => {
        this.form.association_field_names.push(item.field_name_en)
      })
    },
    submit() {
      if (this.fromData.type == 'slot') {
        this.$emit('submit')
      } else {
        this.$refs.form.validate((valid) => {
          if (valid) {
            this.$emit('submit', this.form, this.fromData.type)
          }
        })
      }
    },

    // 获取字典列表
    async getDictList(item) {
      let data = {
        page: 1,
        limit: '',
        form_value: item.form_value
      }
      const result = await getDictListApi(data)
      if (result.data.list.length > 0) {
        this.dictList = result.data.list.filter((item) => {
          return item.status == 1
        })
      } else {
        this.dictList = []
      }
    },

    // 刷新转拼音小写
    refreshFn(refresh, key) {
      if (this.form[refresh] == '') {
        return false
      }
      var regex = /^[\u4e00-\u9fa5a-zA-Z][\u4e00-\u9fa5a-zA-Z_]{0,15}$/
      if (!regex.test(this.form[refresh])) {
        return false
      }
      this.strData = pinyin(this.form[refresh], { toneType: 'none' })
      var reg = /[\t\r\f\n\s]*/g
      if (typeof this.strData === 'string') {
        this.strData = this.strData.replace(reg, '')
      }
      this.form[key] = this.strData
    }, // 首字母转成大写
    titleCase(str) {
      const newStr = str.slice(0, 1).toUpperCase() + str.slice(1).toLowerCase()
      return newStr
    },

    selectChange(e) {
      this.$emit('selectChange', e)
    },
    ueditorEdit(val, item) {
      this.form[item.key] = val
    },

    // 选择人员回调
    getSelectList(data, item) {
      this.userList = data
      let ids = []
      data.map((item) => {
        ids.push(item.value)
      })
      this.form[item.key] = ids
    },
    goDict(item) {
      this.$modalForm(getDictCreateApi()).then(({ message }) => {
        this.getDictList(item)
        setTimeout(() => {
          item.options = this.dictList
        }, 500)
      })
      // window.open(`${roterPre}/develop/dictionary`, '_blank')
    },
    closeFn() {
      this.userList = []
      this.fieldList = []
      this.$refs.form.resetFields()
    },
    checkboxDialogOpen() {
      if (this.form.association_field_names && this.form.association_field_names.length > 0) {
        let ids = []
        this.fieldList.map((item) => {
          ids.push(item.id)
        })
        let data = {
          type: this.fromData.type,
          id: this.form.association_crud_id,
          ids,
          selectList: this.fieldList
        }

        this.$refs.checkboxDialog.openBox(data)
      } else {
        this.$refs.checkboxDialog.openBox()
      }
    }
  }
}
</script>
<style scoped lang="scss">
.refresh-input {
  ::v-deep .el-input__suffix {
    position: absolute;
    right: 0;
  }
}

.fang {
  margin-left: 12px;
  z-index: 155;
}
.iconxitong-xitongshezhi-cebian {
  font-size: 18px;
  color: #c0c4cc;
}
.tips {
  font-size: 12px;
  color: #909399;
}

::v-deep.textarea .el-textarea__inner {
  // font-size: 12px;
  height: 120px;
}

::v-deep .invite .el-button--small {
  border-radius: 0 3px 3px 0;
}
::v-deep .el-textarea__inner {
  resize: none;
}
</style>
