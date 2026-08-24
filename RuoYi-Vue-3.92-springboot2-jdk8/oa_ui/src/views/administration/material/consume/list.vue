<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card class="card-box">
        <consume ref="consume"></consume>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConsumeList',
  components: {
    consume: () => import('./components/log')
  }
}
</script>

<style lang="scss" scoped>
.card-box {
  min-height: calc(100vh - 77px);
}
.list-header {
  padding: 0;
}

.table-box {
  ::v-deep .el-table th {
    background-color: rgba(247, 251, 255, 1);
  }
}
</style>
