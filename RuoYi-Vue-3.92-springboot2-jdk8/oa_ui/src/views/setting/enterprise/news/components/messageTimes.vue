<template>
  <div>
    <el-dialog
      :visible.sync="dialogVisible"
      :width="messageData.width"
      :title="messageData.title"
      :before-close="handleClose"
      :append-to-body="true"
    >
      <el-form ref="form" :model="form" :rules="rules" class="mt20" label-width="90px">
        <el-form-item label="提醒时间：" prop="date" v-if="messageData.type !== 1">
          <el-time-picker
            format="HH:mm"
            value-format="HH:mm"
            v-model="form.date"
            size="small"
            clearable
            placeholder="选择提醒时间"
          >
          </el-time-picker>
        </el-form-item>
        <el-form-item label="提醒时间：" v-if="messageData.type == 1">
          <div class="item">
            {{ messageData.data.template_type == 'clock_remind' ? '上班前' : '下班后' }}

            <div class="mo-input--number">
              <el-input-number v-model="hour" controls-position="right" size="small" :min="0"> </el-input-number>
              <div class="define-append">小时</div>
            </div>
            <div class="mo-input--number">
              <el-input-number v-model="minute" controls-position="right" size="small" :min="0"></el-input-number>
              <div class="define-append">分钟</div>
            </div>
            {{ messageData.data.template_type == 'clock_remind' ? '进行上班打卡' : '进行下班打卡' }}
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleClose()">{{ $t('public.cancel') }}</el-button>
        <el-button type="primary" :loading="loading" size="small" @click="handleConfirm()">{{
          $t('public.ok')
        }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { messageUpdateApi, upDateMessageApi } from '@/api/setting'

export default {
  name: 'MessageTimes',
  components: {},
  props: {
    messageData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      hour: 0,
      minute: 0,
      form: {
        date: ''
      },
      rules: {
        date: [{ required: true, message: '请选择提醒时间', trigger: 'change' }]
      }
    }
  },
  watch: {
    messageData: {
      handler(nVal, oVal) {
        if (nVal.data.remind_time) {
          this.form.date = nVal.data.remind_time
          this.hour = Math.floor(this.form.date / 3600)
          this.minute = Math.floor((this.form.date % 3600) / 60)
        } else {
          this.form.date = ''
        }
      },
      deep: true
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },
    handleOpen() {
      this.dialogVisible = true
    },
    handleConfirm() {
      if (this.messageData.type == 1) {
        this.form.date = parseInt(this.hour * 3600) + parseInt(this.minute * 60)
      }

      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true
          upDateMessageApi(this.messageData.data.id, { remind_time: this.form.date })
            .then((res) => {
              this.loading = false
              this.handleClose()
              this.$emit('isOk')
            })
            .catch((error) => {
              this.loading = false
            })
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
}
.mo-input--number {
  margin-left: 6px;
  width: 130px;
  border: 1px solid #dcdfe6;
  display: flex;
  border-radius: 4px;

  ::v-deep .el-input__inner {
    border: none !important;
  }
}

::v-deep .el-date-editor.el-input {
  width: 100%;
}
.define-append {
  font-size: 12px;
  width: 40px;
  display: inline-block;
  background: #f5f7fa;
  padding: 0px 3px;
  border-left: none;
  height: 32px;
  line-height: 32px;
  color: #909399;
  font-size: 12px;
  text-align: center;
}
</style>
