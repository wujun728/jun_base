<template>
  <div class="table-box">
    <el-dialog
      :visible.sync="dialogVisible"
      :width="title == '调薪弹窗' ? '680px' : '470px'"
      :show-close="status == 'add' ? false : true"
      append-to-body
    >
      <div class="header" slot="title">
        <span>{{ title }}</span>
        <el-link :underline="false" icon="el-icon-edit-outline" @click="editFn" v-if="status == 'add'"
          >编辑字段</el-link
        >
      </div>
      <div class="line" />

      <div class="from" v-if="status == 'add'">
        <el-form
          ref="formName"
          label-width="100px"
          label-position="right"
          class="demo-dynamic"
          v-if="title !== '调薪弹窗'"
        >
          <el-form-item
            label="生效时间:"
            :rules="[{ type: 'date', required: true, message: '请选择日期', trigger: 'change' }]"
          >
            <el-date-picker v-model="take_date" value-format="yyyy-MM-dd" type="date" placeholder="选择日期">
            </el-date-picker>
          </el-form-item>

          <div v-for="(item, index) in fromItem" :key="index">
            <el-form-item :label="item.label">
              <el-input
                :placeholder="item.placeholder"
                type="number"
                maxLength="9"
                @input="handleInput(item.value, index)"
                v-model="item.value"
                @blur="blurVal(item.value, index)"
              ></el-input>
            </el-form-item>
          </div>
          <el-form-item label="调薪备注：" prop="desc">
            <el-input
              type="textarea"
              style="width: 260px"
              placeholder="请输入调薪备注，最多可输入200字"
              v-model="mark"
              maxlength="200"
            ></el-input>
          </el-form-item>
          <el-form-item class="footer">
            <el-button @click="restFn" class="btn">取消</el-button>
            <el-button type="primary" :loading="loading" @click="okFn('formName')" class="btn">确定</el-button>
          </el-form-item>
        </el-form>

        <el-form
          ref="formName"
          label-width="120px"
          label-position="right"
          class="demo-dynamic"
          v-if="title == '调薪弹窗'"
        >
          <el-form-item
            label="生效时间:"
            :rules="[{ type: 'date', required: true, message: '请选择日期', trigger: 'change' }]"
          >
            <div class="change-item">
              <el-date-picker v-model="take_date" value-format="yyyy-MM-dd" type="date" placeholder="选择日期">
              </el-date-picker>
            </div>
          </el-form-item>

          <div v-for="(item, index) in fromItem" :key="index">
            <el-form-item :label="item.label">
              <div class="change-item">
                <el-input v-model="item.num" :disabled="true" class="disabled-item"> </el-input>
                <i class="el-icon-d-arrow-right"></i>
                <el-input
                  :placeholder="item.placeholder"
                  @input="handleInput(item.value, index)"
                  type="number"
                  maxLength="9"
                  v-model="item.value"
                ></el-input>
              </div>
            </el-form-item>
          </div>
          <el-form-item label="调薪备注：" prop="desc">
            <el-input
              type="textarea"
              placeholder="请输入调薪备注信息，最多可输入200字"
              v-model="mark"
              maxlength="200"
            ></el-input>
          </el-form-item>
          <el-form-item class="footer">
            <el-button @click="restFn" class="btn">取消</el-button>
            <el-button type="primary" @click="okFn('formName')" class="btn">确定</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 添加表单 -->

      <div class="addForm" v-if="status === 'edit'">
        <div class="assess-right v-height-flag">
          <div>
            <draggable
              v-model="fromItem"
              chosen-class="chosen"
              force-fallback="true"
              group="people"
              animation="1000"
              @start="onStart"
              @end="onEnd"
            >
              <transition-group>
                <div v-for="(item, index) in fromItem" :key="index" class="item-list">
                  <i class="icon iconfont icontuodong item-drag"></i>
                  <el-input v-model="item.label" clearables show-word-limit placeholder="请输入表单字段" />
                  <i
                    class="el-icon-remove item-remove"
                    @click="handleDelete(index)"
                    v-if="item.sort !== 1 && item.sort !== 2"
                  ></i>
                </div>
              </transition-group>
            </draggable>
            <el-button class="add-type mt14" type="text" @click="handleAddType()">
              <i class="el-icon-plus"></i> 添加</el-button
            >

            <div class="footer">
              <el-button @click="handleResetFn" class="btn">取消</el-button>
              <el-button type="primary" @click="handleConfirm" class="btn">确定</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import draggable from 'vuedraggable'
import { getSalary, getSalaryContent, putSalaryContent, latelySalaryContent } from '@/api/enterprise'

export default {
  name: '',
  components: {
    draggable
  },
  props: {
    id: {
      type: [String, Number, Boolean],
      default: () => false
    },
    editForm: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      unmodifiedFormItem: [],
      dialogVisible: false,
      mark: '',
      title: '定薪',
      status: 'add',
      loading: false,
      take_date: '',
      fromItem: [
        {
          label: '基本工资(元):',
          value: '0',
          placeholder: '请输入基本工资',
          sort: 1
        },
        {
          label: '绩效工资(元):',
          value: 0,
          placeholder: '请输入绩效工资',
          sort: 2
        },
        {
          label: '岗位工资(元):',
          value: 0,
          placeholder: '请输入岗位工资',
          sort: 3
        },

        {
          label: '管理津贴(元):',
          value: 0,
          placeholder: '请输入管理津贴',
          sort: 4
        },
        {
          label: '技能补贴(元):',
          value: 0,
          placeholder: '请输入基本工资',
          sort: 5
        },

        {
          label: '其他补贴(元):',
          value: 0,
          placeholder: '请输入餐饮，交通，话费，电脑等其他补贴',
          sort: 6
        }
      ],
      time: '',
      rules: [],
      total: 0,
      salaryId: '',
      type: '',
      nowtype: ''
    }
  },
  created() {
    var nowDate = new Date()
    var date = {
      year: nowDate.getFullYear(),
      month: nowDate.getMonth() + 1,
      day: nowDate.getDate()
    }
    const dayDate = date.year + '-' + (date.month >= 10 ? date.month : '0' + date.month) + '-' + date.day
    this.take_date = dayDate
  },

  methods: {
    show() {
      this.dialogVisible = true
    },
    handleResetFn() {
      this.fromItem = JSON.parse(JSON.stringify(this.unmodifiedFormItem))
      if (this.nowtype == '调薪弹窗') {
        this.title = '调薪弹窗'
        this.status = 'add'
        this.type = '调薪弹窗'
      } else {
        // this.title = '编辑薪资'
        this.status = 'add'
      }
    },

    restFn() {
      this.dialogVisible = false
      this.fromItem.forEach((item) => {
        item.value = 0
      })
      this.nowtype = ''
    },

    blurVal(data, index) {
      this.fromItem[index].value = data.replace(/^\D*([0-9]\d*\.?\d{0,2})?.*$/, '$1')
    },
    handleInput(data, index) {
      this.fromItem[index].value = data.replace(/\D/g, '').replace(/^0{1,}/g, '')
    },

    // 单独编辑
    async editId(val) {
      this.status = 'add'
      this.title = val.title
      this.salaryId = val.id
      const result = await getSalaryContent(val.id)
      this.mark = result.data.mark
      this.take_date = result.data.take_date
      this.fromItem = result.data.content
      this.dialogVisible = true
    },

    // 调薪弹窗
    async changeSalary(val) {
      this.title = val.title
      this.status = 'add'
      this.type = '调薪弹窗'
      this.nowtype = '调薪弹窗'
      const result = await latelySalaryContent(this.id)
      this.mark = result.data.mark
      this.take_date = result.data.take_date
      this.fromItem = result.data.content
      // 添加新属性
      for (let i = 0; i < this.fromItem.length; i++) {
        this.fromItem[i].num = this.fromItem[i].value
      }
      //  绑定值置空
      this.fromItem.forEach((item) => {
        item.value = ''
      })
      this.dialogVisible = true
    },

    // 编辑
    editFn() {
      this.unmodifiedFormItem = JSON.parse(JSON.stringify(this.fromItem))
      this.title = '编辑表单字段'
      this.status = 'edit'
    },

    // 拖动
    onStart() {
      this.drag = true
    },
    onEnd() {
      this.drag = false
    },

    // 删除动态表单
    handleDelete(index) {
      this.fromItem.splice(index, 1)
    },

    // 添加动态表单
    handleAddType() {
      if (this.fromItem.length > 0) {
        const status = this.fromItem.some((el, index) => {
          return el.label === ''
        })
        if (status) {
          this.$message.warning(this.$t('customer.message05'))
        } else {
          this.fromItem.push({
            sort: '',
            value: 0,
            label: '',
            placeholder: '请输入'
          })
        }
      } else {
        this.fromItem.push({
          sort: '',
          value: 0,
          label: '',
          placeholder: '请输入'
        })
      }
    },

    // 提交表单
    async okFn() {
      let totalArr = []
      this.fromItem.forEach((item) => {
        totalArr.push(item.value)
      })
      let newtol = totalArr.map(Number)
      this.total = newtol.reduce((x, y) => x + y)

      let data = {
        take_date: this.take_date,
        card_id: this.id,
        total: this.total,
        content: this.fromItem,
        mark: this.mark
      }
      this.loading = true
      if (this.title == '编辑薪资') {
        await putSalaryContent(this.salaryId, data)
        await this.$emit('getSalaryList')
        this.dialogVisible = false
        this.loading = false
      } else {
        await getSalary(data)
        this.dialogVisible = false
        await this.$emit('getSalaryList')
        this.loading = false
        this.fromItem.forEach((item) => {
          item.value = '0'
        })
      }
    },

    // 确定动态表单
    handleConfirm() {
      var data = []
      if (this.fromItem.length <= 0) {
        this.$message.warning(this.$t('customer.message05'))
      } else {
        const status = this.fromItem.some((el, index) => {
          return el.label === ''
        })
        if (status) {
          this.$message.warning(this.$t('customer.message05'))
        } else {
          const len = this.fromItem.length
          this.fromItem.map((value, index) => {
            if (this.type == '调薪弹窗') {
              data.push({
                label: value.label,
                value: '',
                num: 0,
                sort: len - index + 1,
                placeholder: value.placeholder + value.label
              })
            } else {
              data.push({
                label: value.label,
                value: value.value,
                sort: len - index + 1,
                placeholder: value.placeholder + value.label
              })
            }
          })
          this.status = 'add'
          if (this.type == '调薪弹窗') {
            this.title = '调薪弹窗'
          }
        }
      }
    }
  }
}
</script>

<style scoped lang="scss">
.mt15 {
  margin-top: 15px;
}
::v-deep .el-dialog {
  height: 600px;
  overflow: auto;
}
::v-deep .el-dialog::-webkit-scrollbar {
  height: 0;
  width: 0;
}

.line {
  width: 100%;
  height: 4px;
  border-bottom: 1px solid #ccc;
  margin-bottom: 20px;
}
.el-icon-remove {
  margin-top: 10px;
  margin-left: 5px;
  color: red;
}
.change-item {
  display: flex;
  align-items: center;
  .disabled-item {
    width: 50%;
  }
  ::v-deep .el-date-editor {
    width: 242px;
  }
  .el-icon-d-arrow-right {
    margin: 0 10px;
    font-size: 16px;
    color: #ccc;
  }
}
.add-text {
  color: #1890ff;
  font-weight: 500;
}
.header {
  display: flex;
  justify-content: space-between;
  span {
    font-size: 13px;
    font-weight: 700;
  }
}
::v-deep.el-scrollbar__wrap {
  margin-right: -17px !important;
  margin-bottom: -17px !important;
  margin-left: 20px;
}
.item-list {
  margin-top: 14px;
}
.item {
  display: flex;
}
::v-deep .el-dialog__body {
  padding: 0px 20px 10px 20px;
}
::v-deep .el-input {
  width: 260px;
}
::v-deep .el-form-item__label {
  font-size: 12px !important;
  font-weight: 500;
}
::v-deep .el-input__inner {
  height: 28px;
  font-size: 13px;
}
::v-deep .el-form-item {
  margin-bottom: 10px;
}
.footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 80px;
  margin-right: 10px;
  .btn {
    margin-bottom: 28px;
  }
}
</style>
<style>
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none !important;
  margin: 0;
}
</style>
