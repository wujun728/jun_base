<template>
  <el-card class="mt14">
    <div class="v-height-flag">
      <div class="plan-tabs-content">
        <el-tabs v-model="tabCur" @tab-click="handleClick">
          <el-tab-pane :label="$t('access.selfassessment01')" name="0" />
          <el-tab-pane :label="$t('access.mydepartment')" name="1" />
        </el-tabs>
      </div>
      <div class="mt20">
        <assess-statistics ref="assessStatistics" />
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'WorkStatistics',
  components: {
    assessStatistics: () => import('./components/assessStatistics')
  },
  data() {
    return {
      tabCur: 0,
      checkBtn: true
    }
  },
  mounted() {},
  methods: {
    handleClick(number) {
      this.tabCur = number.index
      this.$refs.assessStatistics.type = number.name
      this.$refs.assessStatistics.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-tabs__header {
  margin-bottom: 0;
}
::v-deep .el-tabs__nav-wrap::after {
  display: none;
}
.plan-tabs-content {
  margin: 0 -20px 14px -20px;
  border-bottom: 2px solid #f0f2f5;
  ::v-deep .el-tabs {
    padding: 0 20px;
  }
  ::v-deep .el-tabs__item {
    line-height: 26px;
  }
}
.right-box {
  position: absolute;
  right: 0;
  top: 50%;
  width: 300px;
  transform: translateY(-50%);
}
.tips-box {
  padding: 0 20px;
  background: #fffbe6;
  border-radius: 2px;
  border: 1px solid #ffe58f;
  font-size: 13px;
  color: rgba(0, 0, 0, 0.65);
  position: relative;
  padding-top: 10px;
  i {
    position: absolute;
    font-size: 14px;
    color: #000000;
    top: 6px;
    right: 6px;
    cursor: pointer;
  }
}
.tips-box.tActive {
  background-color: #fffafa;
  border-color: #f5c0c0;
}
.process-left-list {
  display: flex;
  align-items: center;
  .process-list {
    display: flex;
    align-items: center;
  }
  .el-button--medium {
    padding: 8px 0;
    width: 100px;
    font-size: 13px;
  }
  i {
    font-size: 22px;
    display: inline-block;
    color: #999999;
    margin: 0 10px;
    background: linear-gradient(-180deg, #dedede, #999999);
    background: -webkit-linear-gradient(-180deg, #dedede, #999999);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
}
</style>
