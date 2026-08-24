<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card :class="tabCur == 0 ? 'employees-card-bottom' : 'employees-card'">
        <div class="form-wrapper">
          <!--考核计划-->
          <plan v-if="tabCur == 0" />
          <!--考核评分-->
          <explain v-if="tabCur == 1" />
          <!--考核流程-->
          <process v-if="tabCur == 2" />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Index',
  components: {
    explain: () => import('./components/explain'),
    process: () => import('./components/process'),
    plan: () => import('./components/plan')
  },
  data() {
    return {
      totalPage: 0,
      tableData: [],
      tabCur: 0
    }
  },
  created() {
    if (this.$route.path == `${roterPre}/hr/staff/config/assessmentPlan`) {
      this.tabCur = 0
    } else if (this.$route.path == `${roterPre}/hr/staff/config/assessmentScore`) {
      this.tabCur = 1
    } else {
      this.tabCur = 2
    }
  },
  mounted() {},
  methods: {
    handleClick(number) {
      this.tabCur = number.index
    }
  }
}
</script>

<style lang="scss" scoped>
.divBox {
  padding-bottom: 0;
  margin-bottom: 0;
}
::v-deep .el-tabs__header {
  margin-bottom: 0;
}
.form-wrapper {
  position: relative;
}
::v-deep .el-tabs__nav-wrap::after {
  display: none;
}
.right-box {
  position: absolute;
  right: 0;
  top: 50%;
  width: 300px;
  transform: translateY(-50%);
}
</style>
