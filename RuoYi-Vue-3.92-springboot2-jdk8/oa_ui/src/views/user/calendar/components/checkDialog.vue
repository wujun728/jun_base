<template>
  <div>
    <el-dialog
      :title="repeatData.title"
      :visible.sync="dialogVisible"
      :width="repeatData.width"
      :before-close="handleClose"
    >
      <div v-if="repeatData.data !== undefined" class="body mt15">
        <el-row>
          <el-col :span="12" class="repeat-title">待办类型：{{ getScheduleName(repeatData.data.types) }}</el-col>
          <el-col :span="12" class="text-right repeat-left">
            {{ repeatData.data.remind_day }} {{ repeatData.data.remind_time }}
          </el-col>
        </el-row>
        <p>待办内容：{{ repeatData.data.content }}</p>
        <p v-if="['client_renew', 'client_track', 'client_return'].includes(repeatData.data.types)">
          <span>{{ repeatData.data.types === 'client_track' ? '客户名称' : '合同名称' }}：</span>
          <span @click="toRouterLink()" class="default-color pointer">{{ repeatData.data.title }}</span>
        </p>
        <p v-if="repeatData.data.types === 'personal'">待办备注：{{ repeatData.data.mark }}</p>
      </div>
      <div slot="footer" class="dialog-footer" v-if="repeatData.data !== undefined">
        <el-button
          size="small"
          @click="handleEdit"
          v-if="repeatData.data.types === 'personal' || repeatData.data.types === 'report_renew'"
          >{{ $t('public.edit') }}</el-button
        >
        <el-button
          size="small"
          v-if="(!deleteBtn && repeatData.data.types === 'personal') || repeatData.data.types === 'report_renew'"
          type="primary"
          @click="handleConfirm"
          >{{ $t('public.delete') }}</el-button
        >
        <el-button size="small" v-if="deleteBtn" type="primary" @click="handleOk">{{ $t('login.ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { roterPre } from '@/settings'
import { dealtScheduleDeleteApi, dealtScheduleTypesApi } from '@/api/user'
export default {
  name: 'CheckDialog',
  props: {
    repeatData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    deleteBtn: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      rules: {
        type: 1
      },
      scheduleTypes: []
    }
  },

  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    handleOpen() {
      this.dialogVisible = true
      if (this.scheduleTypes <= 0) {
        this.getTypes()
      }
    },
    confirmData() {
      this.$emit('handleRepeatData', this.rules)
    },
    async getTypes() {
      const result = await dealtScheduleTypesApi()
      this.scheduleTypes = result.data
    },
    getScheduleName(type) {
      var str = ''
      this.scheduleTypes.map((value) => {
        if (value.key === type) {
          str = value.name
        }
      })
      return str
    },
    async handleConfirm() {
      const result = await dealtScheduleDeleteApi(this.repeatData.data.id)
      this.rules.type = 1
      await this.confirmData()
      this.dialogVisible = false
      this.calendarData = result.data
    },
    handleEdit() {
      this.dialogVisible = false
      this.rules.type = 2
      this.confirmData()
    },
    handleOk() {
      this.dialogVisible = false
      this.rules.type = 3
      this.confirmData()
    },
    toRouterLink() {
      this.$router.push({
        path: roterPre + this.repeatData.data.link,
        query: {
          id: this.repeatData.data.link_id,
          name: this.repeatData.data.title
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.body {
  .repeat-title {
    font-size: 13px;
    font-weight: bold;
  }
  .repeat-left {
    font-size: 13px;
  }
  p {
    margin: 15px 0 6px 0;
    font-size: 13px;
  }
}
.dialog-footer {
  padding-top: 20px;
  border-top: 1px solid #e6ebf5;
}
</style>
