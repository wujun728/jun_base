<template>
  <div class="divBox">
    <!-- 我的考核 -->
    <div v-if="checkBtn">
      <el-card class="employees-card-bottom">
        <div>
          <div class="plan-tabs-content mb20">
            <el-tabs v-model="tabCur">
              <el-tab-pane label="当前考核" name="0" />
              <el-tab-pane label="我的自评" name="1" />
              <el-tab-pane label="考核统计" name="2" />
            </el-tabs>
          </div>
          <!-- 当前考核   -->
          <current v-if="tabCur == 0" :id="id" />
          <!-- 我的自评   -->
          <self v-if="tabCur == 1" ref="self" @selfHandleCheck="selfHandleCheck" />
          <!-- 自评统计   -->
          <assess-statistics v-if="tabCur == 2" ref="assessStatistics" :is-mine="true" />
        </div>
      </el-card>
    </div>

    <!-- 绩效审核详情 -->
    <audit-process
      v-if="!checkBtn"
      :id="id"
      ref="auditProcess"
      :is-tips="isTips"
      :rowData="rowData"
      :process-index="status"
      :strType="strType"
      @saveOk="saveOk"
      @auditProcess="auditProcess"
    />
  </div>
</template>
<script>
export default {
  name: '',
  components: {
    current: () => import('./components/current'),
    self: () => import('./components/self'),
    assessStatistics: () => import('@/views/user/workStatistics/components/assessStatistics'),
    auditProcess: () => import('./components/auditProcess')
  },
  props: {},
  data() {
    return {
      tabCur: 0,
      id: 0,
      checkBtn: true,
      isTips: true,
      status: 0,
      strType: '',
      rowData: {}
    }
  },
  watch: {
    tabCur: function (newVal, old) {
      this.getList()
    }
  },

  methods: {
    selfHandleCheck(data, str, row) {
      if (row) {
        this.rowData = row
      }
      this.checkBtn = false
      this.status = data.status
      this.id = data.id
      this.tabCur = data.tabIndex
      this.strType = str
    },
    auditProcess(data) {
      if (data.return == 1) {
        this.checkBtn = true
        this.getList()
      }
    },
    saveOk() {
      this.checkBtn = true
      this.getList()
    },
    getList() {
      this.$nextTick(() => {
        switch (parseInt(this.tabCur)) {
          case 1:
            setTimeout(() => {
              this.$refs.self.getTableData()
            }, 300)
            break
          case 2:
            setTimeout(() => {
              this.$refs.assessStatistics.type = 0
              this.$refs.assessStatistics.getTableData()
            }, 300)
            break
        }
      })
    }
  }
}
</script>
<style scoped lang="scss">
.plan-tabs-content {
  ::v-deep .el-tabs__header {
    margin-bottom: 0;
    .el-tabs__nav-wrap::after {
      height: 1px;
      background-color: #eee;
    }
  }
}
</style>
