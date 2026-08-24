<template>
  <div class="divBox">
    <div class="v-height-flag">
      <el-card>
        <consume ref="consume"></consume>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConsumeManage',
  components: {
    consume: () => import('../fixed/components/consume')
  }
}
</script>

<style lang="scss" scoped>
.list-header {
  padding: 0;
}

.table-box {
  ::v-deep .el-table th {
    background-color: rgba(247, 251, 255, 1);
  }
}
</style>
