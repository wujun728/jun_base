<!-- 合同-续费到期的弹窗 -->
<template>
  <el-dialog
    :title="config.title"
    :visible.sync="dialogVisible"
    :width="config.width"
    :append-to-body="true"
    :show-close="true"
    :before-close="handleClose"
  >
    <div class="table-box">
      <el-table :data="config.data" :height="420">
        <el-table-column prop="renew.title" :label="$t('customer.renewaltype')" min-width="110"> </el-table-column>
        <el-table-column prop="date" :label="$t('customer.renewaldate')" min-width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.end_date.slice(0, 4) !== '0000'">{{
              $moment(scope.row.end_date).format('YYYY-MM-DD')
            }}</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column prop="date" label="续费状态" min-width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.renew_status == 1" class="color-warning">急需续费 </span>
            <span v-if="scope.row.renew_status == 2" class="color-danger">已过期 </span>
            <span v-if="scope.row.renew_status == 0" class="color-success">正常</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'MarkDialog',
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
      loading: false,
      renewMaxDate: 30
    }
  },
  watch: {
    config: {
      handler(nVal) {
        if (nVal.mark) {
          this.rules.remarks = nVal.mark
        }
      },
      deep: true
    }
  },
  methods: {
    getRenewDays(date) {
      return this.$moment(date).diff(this.$moment(new Date()), 'days')
    },
    getRenewName(date, type) {
      let str = ''

      let days = this.getRenewDays(date)

      if (days > this.renewMaxDate) {
        str = type === 1 ? 'color-success' : '正常'
      } else if (days >= 0 && days <= this.renewMaxDate) {
        str = type === 1 ? 'color-warning' : '急需续费'
      } else {
        str = type === 1 ? 'color-danger' : '已超期'
      }
      return str
    },
    handleOpen() {
      this.dialogVisible = true
    },
    handleClose() {
      this.dialogVisible = false
      this.remarks = ''
    }
  }
}
</script>

<style scoped lang="scss">

.renewal-content {
  width: 100%;
  margin-bottom: 10px;
  p {
    width: 50%;
    margin: 12px 20px 0 0;
    padding: 0;
    font-size: 13px;
    display: inline-block;
    &:last-of-type {
      margin-right: 0;
    }
  }
}
</style>
