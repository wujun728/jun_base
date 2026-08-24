<template>
  <div>
    <!-- 编辑审批 -->
    <div class="ex-content">
      <el-scrollbar style="height: 100%">
        <div class="ex-content-con">
          <div class="acea-row mb20">
            <div class="shu mr10"></div>
            <span class="title">提交审批</span>
          </div>
          <form-create
            class="edit-examine-form"
            ref="fc"
            v-model="fapi"
            :option="option"
            size="small"
            :rule="rules"
            @change="handleChange"
          />
          <el-divider v-if="configData && configData.examine != 0"></el-divider>
          <process-from
            v-if="configData && configData.examine != 0"
            :examine-data="examineData"
            :edit-user="editUser"
          ></process-from>
        </div>
      </el-scrollbar>
    </div>
    <div class="button from-foot-btn fix btn-shadow">
      <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
      <el-button size="small" type="primary" :loading="loading" @click="handleConfirm('ruleForm')">
        {{ $t('public.ok') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import request from '@/api/request'
import {
  approveApplyEditApi,
  approveApplyFormApi,
  approveApplyListApi,
  attendanceAbnormalDateApi,
  attendanceAbnormalRecordApi
} from '@/api/business'

export default {
  name: 'EditExamine',
  components: {
    processFrom: () => import('@/views/user/examine/components/process')
  },
  props: {
    parameterData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    commandId: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      fapi: null,
      abnormal: '', //异常日期
      record: '', //异常记录
      holiday: '', //假期类型
      abnormal_id: '', //异常日期id
      record_id: 0, //异常记录id
      holiday_id: 0,
      abnormal_label: '',
      record_label: '',
      calculateType: '',
      option: {
        form: {
          labelWidth: '120px'
        },
        submitBtn: false,
        global: {
          frame: {
            props: {
              closeBtn: false,
              okBtn: false,
              onLoad: (e) => {
                e.fApi = this.$refs.fc.$f
              }
            }
          }
        }
      }, // 表单配置
      rules: [],
      configData: null,
      formData: {},
      isRequest: true,
      examineData: {},
      id: 0,
      isEdit: false,
      loading: false,
      editUser: false, // 审批人添加按钮
      approverDelete: true,
      copyerDelete: true,
      tryDateOptions: null, // 异常时间
      tryYiChangOptions: null, // 异常记录
      configData: null
    }
  },
  created() {
    this.attendanceAbnormalDate()
  },
  mounted() {
    this.approveApply()
  },

  methods: {
    handleClose() {
      this.close()
    },
    close() {
      this.drawer = false
      this.id = 0
    },

    // 获取表单配置
    approveApply() {
      approveApplyFormApi(this.commandId, this.parameterData).then((res) => {
        this.configData = res.data.info
        res.data.forms.forEach((item) => {
          if ((item.type === 'moneyFrom' || item.type === 'inputNumber') && item.props) {
            if (item.props.max) {
              item.props.max = Number(item.props.max)
            }
            if (item.props.min) {
              item.props.min = Number(item.props.min)
            }
            if (item.props.precision) {
              item.props.precision = Number(item.props.precision)
            }
          }
          if (item.children) {
            item.children.forEach((it) => {
              if (it.title == '异常日期') {
                this.abnormal = it.field
              }
              if (it.title == '异常记录') {
                this.record = it.field
              }
            })
          }
        })
        this.rules = []
        res.data.forms.map((item) => {
          if (item.children) {
            item.children.map((per) => {
              per.calculateType = item.type
              this.calculateType = item.type
              if (per.title === '异常日期' && per.calculateType === 'refillFrom') {
                per.options = this.tryDateOptions
              }
              this.rules.push(per)
            })
          } else {
            item.calculateType = item.type
            this.rules.push(item)
          }
        })

        if (!this.isEdit) {
          this.drawer = true
          this.$nextTick(() => {
            const data = this.handleFromObj()
            this.approveApplyList(this.commandId, data)
          })
        } else {
          this.approveApplyEdit(this.id, { types: 0 })
        }
      })
    },
    // 获取人员配置表单
    approveApplyList(id, data) {
      approveApplyListApi(id, data).then((res) => {
        this.examineData = res.data
        var rules = res.data.rules
        if (rules.edit) {
          var rulesEdit = rules.edit + ''
          this.approverDelete = rulesEdit.includes('1')
          this.copyerDelete = rulesEdit.includes('2')
        }

        if (rules.edit && rulesEdit.length > 0) {
          // 是否可删除审批人与抄送人
          if (this.examineData.list.length > 0) {
            this.examineData.list.map((value) => {
              if (value.types == 1) {
                // 判断审批人是否为指定审批人
                if (rules.abnormal > 0 && (value.settype === 2 || value.settype === 7) && value.users.length <= 0) {
                  value.users.push(rules)
                }
                if (rulesEdit.includes('1')) {
                  value.users.map((val) => {
                    val.isDelete = true
                  })
                } else {
                  this.editUser = true
                }
              }
              if (value.types == 2 && rulesEdit.includes('2')) {
                value.users.map((val) => {
                  val.isDelete = true
                })
              }
            })
          }
        } else {
          this.editUser = true
        }
      })
    },
    handleChange(e) {
      // V1.4动态显示隐藏申请开票的邮寄字段
      let expressList = ['liaisonMan', 'telephone', 'mailingAddress']
      let timeType
      const data = this.handleFromObj()

      this.rules.forEach((item) => {
        if (data[e] == 'express') {
          if (expressList.includes(item.symbol)) {
            item.hidden = false
          }
          if (item.symbol == 'invoicingEmail') {
            item.hidden = true
          }
        } else if (data[e] == 'mail') {
          if (expressList.includes(item.symbol)) {
            item.hidden = true
          }
          if (item.symbol == 'invoicingEmail') {
            item.hidden = false
          }
        }
        if (this.fapi.rule[9].value == 1) {
          if (item.symbol == 'dutyParagraph') {
            item.hidden = true
            item.effect.required = true
          }
        } else {
          if (item.symbol == 'dutyParagraph') {
            item.hidden = false
            item.effect.required = false
          }
        }

        if (!!JSON.stringify(timeType) && item.type == 'timeFrom') {
          item.props.timeType = timeType == 0 ? 'day' : 'time'
        }
      })
      if (data[e].timeType == 'time') {
        const dateStart = data[e].dateStart
        const dateEnd = data[e].dateEnd

        // 将日期时间字符串转换为 Date 对象
        const startDate = new Date(dateStart)
        const endDate = new Date(dateEnd)

        // 计算时间差
        const timeDifference = endDate - startDate

        const millisecondsInHour = 60 * 60 * 1000
        const hours = timeDifference / millisecondsInHour
        data[e].duration = hours ? hours.toFixed(2) : ''
      }

      if (this.tryDateOptions) {
        this.tryDateOptions.map((item) => {
          if (item.value == data[e]) {
            this.abnormal_label = item.label
          }
        })
      }
      if (this.tryYiChangOptions) {
        this.tryYiChangOptions.map((item) => {
          if (item.value == data[e]) {
            this.record_label = item.label
          }
        })
      }

      let label
      for (let i in data) {
        if (data[i] == data[e]) {
          label = i
        }
        if (i == this.abnormal) {
          this.abnormal_id = data[i]
        }
        if (i == this.record) {
          this.record_id = data[i]
        }
        if (i == this.holiday) {
          this.holiday_id = data[i]
        }
      }
      // 赋值异常记录
      if (label == this.abnormal) {
        this.attendanceAbnormalRecord(data[e])
      }
      if (!this.isEdit) {
        this.approveApplyList(this.commandId, data)
      } else {
        this.approveApplyList(this.configData.approve_id, data)
      }
    },
    // 编辑
    approveApplyEdit(id, data) {
      approveApplyEditApi(id, data).then((res) => {
        this.drawer = true
        var option = res.data
        this.rules.map((value) => {
          if (option[value.field]) {
            value.value = option[value.field]
          }
        })
        this.rules.map((item) => {
          if (item.title == '异常日期') {
            this.attendanceAbnormalDate()
            if (item.value.abnormal_id) {
              this.abnormal_id = item.value.abnormal_id.value
              this.abnormal_label = item.value.abnormal_id.label
            }
            item.value = this.abnormal_label
            this.attendanceAbnormalRecord(this.abnormal_id)
          }
        })
        this.$nextTick(() => {
          const data = this.handleFromObj()
          this.approveApplyList(this.configData.approve_id, data)
        })
      })
    },
    handleFromData() {
      const attr = []
      this.fapi.rule.map((item) => {
        const condition = {}
        condition[item.field] = item.value ? item.value : ''
        attr.push(condition)
      })
      return attr
    },
    handleFromObj() {
      const obj = {}
      var data = this.fapi.rule
      for (let i = 0; i < data.length; i++) {
        if (Object.prototype.toString.call(data[i].value) === '[object Object]') {
          data[i].value.type = data[i].calculateType
        }
        const keys = data[i]['field']
        obj[keys] = data[i].value ? data[i].value : ''
      }

      return obj
    },
    // 提交
    handleConfirm() {
      let id = !this.isEdit ? this.id : this.configData.approve_id
      if (id == 0) {
        id = this.commandId
      }
      this.approveApplySave(id)
    },
    // 保存
    approveApplySave(id) {
      const FromData = {
        title: this.$t('hr.addposition'),
        method: 'post',
        action: `approve/apply/save/${id}`
      }
      this.$refs.fc.$f.submit((formData) => {
        Object.keys(formData).forEach((k) => {
          if (formData[k] === undefined) formData[k] = ''
        })

        const processInfo = this.examineData.list
        if (processInfo && processInfo.length) {
          let len = 0
          for (let i = 0; i < processInfo.length; i++) {
            const value = processInfo[i]
            if (!this.approverDelete && value.types == 1 && value.users.length <= 0) {
              this.$message.error('自选节点不能为空')
              return
            }
            if (!this.copyerDelete && value.types == 2 && value.users.length <= 0) {
              this.$message.error('自选节点不能为空')
              return
            }
            if (
              this.approverDelete &&
              value.types == 1 &&
              (value.settype == 4 || value.settype == 1) &&
              value.users.length <= 0
            ) {
              this.$message.error('自选节点不能为空')
              return
            }
            if (value.users.length <= 0) {
              len++
            }
          }
          if (len === processInfo.length) {
            this.$message.error('自选节点不能为空')
            return
          }
          processInfo.forEach((value, index) => {
            if (value.users.length <= 0) {
              processInfo.splice(index, 1)
            }
          })
        }
        const data = {
          formInfo: formData,
          processInfo: processInfo
        }

        if (this.isEdit) {
          data.apply_id = this.id
        }
        this.loading = true
        request[FromData.method.toLowerCase()](FromData.action, data)
          .then((res) => {
            this.loading = false
            this.close()
            this.$emit('isOk')
          })
          .catch((err) => {
            this.loading = false
          })
      })
    },
    // 调用接口赋值 异常日期
    attendanceAbnormalDate() {
      attendanceAbnormalDateApi().then((res) => {
        if (res.status == 200) {
          this.tryDateOptions = res.data
          this.tryDateOptions = this.tryDateOptions.map((it) => {
            it.label = it.date
            it.value = it.id
            return it
          })
        }
      })
    },
    // 异常记录
    attendanceAbnormalRecord(id) {
      attendanceAbnormalRecordApi(id).then((res) => {
        if (res.status == 200) {
          this.tryYiChangOptions = res.data
          this.tryYiChangOptions = this.tryYiChangOptions.map((it) => {
            it.value = it.shift_number
            it.label = it.title + ' : ' + it.time
            return it
          })
          // 赋值
          for (let i = 0; i < this.rules.length; i++) {
            if (this.rules[i].title === '异常记录' && this.rules[i].calculateType === 'refillFrom') {
              this.rules[i].options = this.tryYiChangOptions
              break
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.headerBox {
  .portrait {
    width: 36px;
    height: 36px;
    border-radius: 5px;
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
    i {
      font-size: 20px;
      color: #fff;
    }
  }
  .nameBox {
    span {
      display: block;
    }
    .st1 {
      font-size: 15px;
      font-weight: 600;
      color: rgba(0, 0, 0, 0.85);
    }
    .st2 {
      font-size: 13px;
      color: #999999;
    }
  }
}
.iconBox {
  width: 46px;
  height: 46px;
  background: #1890ff;
  border-radius: 6px;
  color: #fff;
  font-size: 26px;
  line-height: 46px;
  text-align: center;
}

.iBox {
  background: #cccccc;
  i {
    color: #fff;
  }
}

.titleBox {
  color: #000000;
  font-size: 16px;
  line-height: 46px;
  font-weight: 600;
}

::v-deep .el-drawer__body {
  padding-bottom: 40px;
}
.ex-content {
  padding: 20px 0 20px 20px;
  height: 100%;
  .ex-content-con {
    padding-right: 24px;
  }
  ::v-deep.select-item {
    margin-top: 0 !important;
  }
  ::v-deep .el-divider--horizontal {
    margin-top: 0;
    margin-bottom: 30px;
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .shu {
    width: 3px;
    height: 16px;
    background: #1890ff;
    display: inline-block;
  }
  .title {
    font-size: 14px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.85);
  }
}
::v-deep .el-input--medium {
  font-size: 12px;
}
::v-deep.el-cascader--medium {
  width: 100%;
}
</style>
