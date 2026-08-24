<template>
  <!-- 表格多选批量操作 -->
  <div class="batch-action-wrapper" v-if="selectCount">
    <el-checkbox
      :true-value="true"
      :false-value="false"
      :indeterminate="selectCount < listCount"
      :value="selectCount === listCount"
      @change="$emit('change')"
    />
    <el-button size="small" @click="$emit('delete')">批量删除</el-button>
    <el-button size="small" @click="$emit('share')">共享协作</el-button>
    <el-button size="small" @click="$emit('transfer')">批量移交</el-button>
  </div>
</template>

<script>
export default {
  props: {
    listCount: Number,
    selectCount: Number
  }
}
</script>

<style scoped lang="scss">
.batch-action-wrapper {
  position: absolute;
  bottom: -10px;
  left: 0;
  right: 0;
  height: 82px;
  background: rgba(255, 255, 255, 0.8);
  box-shadow: inset 0px 1px 0px 0px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  padding-left: 54px;

  .el-checkbox {
    margin-right: 10px;
  }

  .el-button {
    width: 74px;
    height: 32px;
    padding: 0;

    &:focus {
      background: #fff;
      border: 1px solid #dcdfe6;
      color: #606266;
    }

    &:hover {
      color: #1890ff;
      border-color: #badeff;
      background-color: #e8f4ff;
    }
  }
}
</style>
