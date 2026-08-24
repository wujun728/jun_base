<template>
  <div>
    <el-dialog :title="title" :visible.sync="dialogFormVisible" width="45%" v-bind="$attrs">
      <!-- 工作经历 -->
      <el-form :model="workForm" ref="workForm" :rules="workRules" label-width="90px" v-if="add === 'work'">
        <div class="form-box">
          <div class="form-item" v-for="(item, index) in workList" :key="index">
            <el-form-item :prop="item.value">
              <span slot="label">{{ item.label }}</span>
              <!-- date -->
              <el-date-picker
                v-if="item.type === 'date'"
                v-model="workForm[item.value]"
                type="date"
                :placeholder="item.placeholder"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
              <!-- input -->
              <el-input
                v-if="item.type === 'input'"
                v-model="workForm[item.value]"
                :size="item.size || 'small'"
                clearable
                :placeholder="item.placeholder"
              />
              <el-input v-if="item.type === 'textarea'" type="textarea" v-model="workForm[item.value]"></el-input>
            </el-form-item>
          </div>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitFnn()">确 定</el-button>
        </div>
      </el-form>
      <!-- 教育经历 -->
      <el-form
        :model="educationForm"
        ref="educationForm"
        :rules="educationRules"
        label-width="90px"
        v-if="add === 'education'"
      >
        <div class="form-box">
          <div class="form-item" v-for="(item, index) in educationList" :key="index">
            <el-form-item :prop="item.value">
              <span slot="label">{{ item.label }}</span>
              <!-- date -->
              <el-date-picker
                v-if="item.type === 'date'"
                v-model="educationForm[item.value]"
                type="date"
                :placeholder="item.placeholder"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
              <!-- input -->
              <el-input
                v-if="item.type === 'input'"
                v-model="educationForm[item.value]"
                :size="item.size || 'small'"
                clearable
                :placeholder="item.placeholder"
              />
              <el-input v-if="item.type === 'textarea'" type="textarea" v-model="educationForm[item.value]"></el-input>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFnn()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'addWork',
  components: {},
  props: {
    add: {
      type: String,
      default: ''
    },
    edit: {
      type: String,
      default: ''
    }
  },
  data() {
    const startTime = (rules, value, callback) => {
      if (!value) {
        callback(new Error('请选择开始时间'))
      } else {
        if (this.workForm.end_time) {
          this.$refs.workForm.validateField('endTime')
        }
        callback()
      }
    }
    const endTime = (rules, value, callback) => {
      if (!value) {
        callback(new Error('请选择结束时间'))
      } else {
        if (!this.workForm.start_time) {
          callback(new Error('请选择开始时间！'))
        } else if (Date.parse(this.workForm.start_time) >= Date.parse(value)) {
          callback(new Error('结束时间必须大于开始时间！'))
        } else {
          callback()
        }
      }
    }
    const start_Time = (rules, value, callback) => {
      if (!value) {
        callback(new Error('请选择开始时间'))
      } else {
        if (this.educationForm.end_time) {
          this.$refs.educationForm.validateField('endTime')
        }
        callback()
      }
    }
    const end_Time = (rules, value, callback) => {
      if (!value) {
        callback(new Error('请选择结束时间'))
      } else {
        if (!this.educationForm.start_time) {
          callback(new Error('请选择开始时间！'))
        } else if (Date.parse(this.educationForm.start_time) >= Date.parse(value)) {
          callback(new Error('结束时间必须大于开始时间！'))
        } else {
          callback()
        }
      }
    }
    return {
      dialogFormVisible: false,
      title: '添加工作经历',
      workForm: {
        start_time: '',
        end_time: '',
        company: '',
        position: '',
        describe: '',
        quit_reason: ''
      },

      workList: [
        {
          type: 'date',
          label: '开始时间：',
          value: 'start_time',
          placeholder: '请选择开始时间'
        },
        {
          type: 'date',
          label: '结束时间：',
          value: 'end_time',
          placeholder: '请选择结束时间'
        },
        {
          type: 'input',
          label: '所在公司：',
          value: 'company',
          placeholder: '请输入所在公司名称'
        },
        {
          type: 'input',
          label: '职位：',
          value: 'position',
          placeholder: '请输入职位'
        },
        {
          type: 'input',
          label: '工作描述：',
          value: 'describe',
          placeholder: '请输入工作描述'
        },
        {
          type: 'textarea',
          label: '离职原因：',
          value: 'quit_reason',
          placeholder: '请输入离职原因...'
        }
      ],
      educationForm: {
        start_time: '',
        end_time: '',
        school_name: '',
        major: '',
        education: '',
        academic: '',
        remark: ''
      },
      educationList: [
        {
          type: 'date',
          label: '入学时间：',
          value: 'start_time',
          placeholder: '请选择开始时间'
        },
        {
          type: 'date',
          label: '毕业时间：',
          value: 'end_time',
          placeholder: '请选择结束时间'
        },
        {
          type: 'input',
          label: '学校名称：',
          value: 'school_name',
          placeholder: '请输入学校名称'
        },
        {
          type: 'input',
          label: '所学专业：',
          value: 'major',
          placeholder: '请输入所学专业'
        },
        {
          type: 'input',
          label: '学历：',
          value: 'education',
          placeholder: '请输入学历'
        },
        {
          type: 'input',
          label: '学位：',
          value: 'academic',
          placeholder: '请输入学位'
        },
        {
          type: 'textarea',
          label: '备注：',
          value: 'remark',
          placeholder: '请输入备注...'
        }
      ],
      workRules: {
        start_time: [{ required: true, validator: startTime, trigger: 'blur' }],
        end_time: [{ required: true, validator: endTime, trigger: 'blur' }],
        company: [{ required: true, message: '公司名称不能为空', trigger: 'blur' }],
        position: [{ required: true, message: '职位不能为空', trigger: 'blur' }],
        describe: [{ required: true, message: '工作描述不能为空', trigger: 'blur' }]
      },
      educationRules: {
        start_time: [{ required: true, validator: start_Time, trigger: 'blur' }],
        end_time: [{ required: true, validator: end_Time, trigger: 'blur' }],
        school_name: [{ required: true, message: '学校名称不能为空', trigger: 'blur' }],
        major: [{ required: true, message: '专业不能为空', trigger: 'blur' }],
        education: [{ required: true, message: '学历不能为空', trigger: 'blur' }]
      }
    }
  },

  methods: {
    submitFn() {
      if (+this.workForm.end_time < +this.workForm.start_time) {
        alert('结束日期需大于开始日期')
      }
      if (this.add == 'work') {
        this.$refs.workForm.validate((valid) => {
          if (valid) {
            setTimeout(() => {
              this.$emit('addWorkFn', this.workForm, 'work')
            }, 1000)
            this.dialogFormVisible = false
          }
        })
      } else {
        this.$refs.educationForm.validate((valid) => {
          if (valid) {
            setTimeout(() => {
              this.$emit('addWorkFn', this.educationForm, 'education')
            }, 1000)

            this.dialogFormVisible = false
          }
        })
      }
    },

    submitFnn() {
      this.debounce(this.submitFn())
    },

    debounce(func, wait = 1000) {
      var timer //计时器
      return function () {
        var args = arguments
        var that = this

        clearTimeout(timer)
        timer = setTimeout(() => {
          func.apply(that, args)
        }, wait)
      }
    },
    // 重置
    resetForm() {
      if (this.add == 'work') {
        this.$refs.workForm.resetFields()
      } else {
        this.$refs.educationForm.resetFields()
      }
    }
  }
}
</script>
<style scoped lang="scss"></style>
