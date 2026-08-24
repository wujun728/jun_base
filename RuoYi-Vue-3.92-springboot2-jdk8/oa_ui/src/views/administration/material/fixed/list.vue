<!-- 行政-办公物资-固定物资-物资记录 -->
<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card class="card-box">
        <log ref="fixed"></log>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FixedList',
  components: {
    log: () => import('./components/log')
  }
}
</script>

<style lang="scss" scoped>
.card-box {
  min-height: calc(100vh - 77px);
}
.table-box {
  ::v-deep .el-table th {
    background-color: rgba(247, 251, 255, 1);
  }
}
</style>
