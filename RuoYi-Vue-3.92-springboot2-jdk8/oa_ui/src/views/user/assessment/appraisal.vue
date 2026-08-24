<template>
  <!-- 绩效考核 -->
  <div class="divBox">
    <div v-if="checkBtn">
      <el-card class="employees-card-bottom">
        <div class="plan-tabs-content mb20">
          <el-tabs v-model="tabCur" @tab-click="tapClick">
            <el-tab-pane label="待处理" name="6" />
            <el-tab-pane label="考核记录" name="7" />
          </el-tabs>
        </div>
        <merits ref="merits" :tabCur="tabCur" :handle="handle" @meritsHandleCheck="meritsHandleCheck" />
      </el-card>
    </div>

    <audit-process
      v-if="!checkBtn"
      :id="id"
      ref="auditProcess"
      :add-button="addBtn"
      :is-tips="isTips"
      :process-index="status"
      :strType="strType"
      @auditProcess="auditProcess"
      @saveOk="saveOk"
    />
  </div>
</template>
<script>
export default {
  name: 'DepartmentAssessment',
  components: {
    merits: () => import('./components/merits'),
    auditProcess: () => import('./components/auditProcess')
  },

  data() {
    return {
      tabCur: '6',
      id: 0,
      checkBtn: true,
      isTips: true,
      addBtn: false,
      status: 0,
      strType: '',
      tabButton: true,
      handle: 1
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    // 评价成功
    saveOk() {
      this.checkBtn = true

      this.getList()
    },
    meritsHandleCheck(res, str) {
      this.strType = str
      this.checkBtn = false
      this.status = res.status
      this.id = res.id
      if (res.addBtn > 1) {
        this.isTips = false
        this.addBtn = true
      } else {
        this.isTips = true
        this.addBtn = false
      }
    },
    auditProcess(data) {
      if (data.return == 1) {
        this.checkBtn = true
        this.tabButton = true
        this.getList()
      }
    },
    tapClick() {
      this.handle = this.tabCur == 6 ? 1 : ''
      this.getList()
    },
    getList() {
      const tabCur = parseInt(this.tabCur)
      setTimeout(() => {
        this.handle = tabCur == 6 ? 1 : ''
        this.$refs.merits.getFormBoxFrame()
        if (this.checkBtn) {
          this.$refs.merits.getTableData()
        }
      }, 300)

      this.tabButton = false
    }
  }
}
</script>

<style lang="scss" scoped>
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
