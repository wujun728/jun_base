<!-- 客户-添加跟进提醒弹窗组件 -->
<template>
  <el-dialog
    :title="`${isEdit ? '编辑' : '添加'}跟进提醒`"
    top="25vh"
    class="addBox"
    :append-to-body="true"
    :visible.sync="dialogVisible"
    width="500px"
  >
    <div class="line" />
    <el-form :model="form" ref="form" :rules="rules" class="from">
      <el-form-item label=" 提醒时间：" label-width="100px" prop="time">
        <el-date-picker
          class="picker-time"
          type="datetime"
          default-time="09:00:00"
          v-model="form.time"
          placeholder="请选择日期"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label=" 提醒内容：" label-width="100px" prop="content">
        <el-input type="textarea" maxlength="200" v-model="form.content" placeholder="请输入提醒内容"></el-input>
      </el-form-item>
      <div class="dialog-footer">
        <el-button size="small" class="btn" @click="handleClose">取消</el-button>
        <el-button size="small" type="primary" @click="handleConfirm" class="btn">确定</el-button>
      </div>
    </el-form>
  </el-dialog>
</template>

<script>
import { clientFollowEditApi, clientFollowSaveApi } from '@/api/enterprise'
export default {
  name: 'LiaisonDialog',
  props: {
    config: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        time: '',
        content: ''
      },
      rules: {
        time: [{ required: true, message: '请选择提醒时间', trigger: 'change' }],
        content: [{ required: true, message: '请填写提醒内容', trigger: 'blur' }]
      },
      labelWidth: 110,
      loading: false,
      isEdit: true
    }
  },
  watch: {
    config: {
      handler(nVal) {
        if (nVal.isEdit) {
          this.form.content = nVal.data.content
          this.form.time = this.$moment(nVal.data.time).format('YYYY-MM-DD HH:mm:ss')
        }
      },
      deep: true
    }
  },
  methods: {
    handleOpen(val) {
      this.dialogVisible = true
      this.isEdit = val
    },
    handleClose() {
      this.reset()
      this.$refs.form.resetFields()
      this.dialogVisible = false
    },

    reset() {
      this.liaison = {
        time: '',
        content: ''
      }
    },

    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          let data = {
            time: this.$moment(this.form.time).format('YYYY-MM-DD HH:mm:ss'),
            content: this.form.content,
            eid: this.config.eid,
            types: 1
          }

          if (this.config.isEdit) {
            this.followEdit(this.config.data.id, data)
          } else {
            this.clientFollow(data)
          }
        }
      })
    },

    // 客户跟进记录-保存
    async clientFollow(data) {
      this.loading = true
      const res = await clientFollowSaveApi(data)
      this.loading = false
      if (res.status === 200) {
        this.handleClose()
        this.$emit('change')
        this.reset()
      }
    },

    // 客户跟进记录-编辑
    async followEdit(id, data) {
      this.loading = true
      const res = await clientFollowEditApi(id, data)
      this.loading = false
      if (res.status === 200) {
        this.reset()
        this.handleClose()
        this.$emit('change')
      }
    }
  }
}
</script>

<style scoped lang="scss">
.from {
  padding: 0 24px;
  margin-top: 20px;
}

::v-deep .el-date-editor {
  width: 100%;
}
::v-deep .el-textarea__inner {
  resize: none;
  border: 1px solid #dcdfe6 !important;
}
::v-deep .el-input-number--medium {
  width: 100%;
}
::v-deep .el-select--medium {
  width: 100%;
}
::v-deep .el-form-item:last-of-type {
  margin-bottom: 0;
}
.dialog-footer {
  padding-top: 20px;
  text-align: right;
}
</style>
