<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="待办" name="1">
        <todo-list />
      </el-tab-pane>
      <el-tab-pane label="待阅" name="2">
        <read-list />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import TodoList from "./todo-list.vue";
import ReadList from "./read-list.vue";
export default {
  name: "Todo",
  components: { TodoList, ReadList },
  data() {
    return {
      activeName: this.$route.query.type || "1",
      // 全部待办总数
      allTotal: 0,
    };
  },
  methods: {
    handleClick() {},
  },
};
</script>

<style scoped lang="scss">
::v-deep .el-tabs__nav-wrap::after {
  height: 1px;
}
::v-deep .el-tabs > .el-tabs__content {
  background-color: #fff;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.08);
  padding: 10px 16px;
}
::v-deep .el-tabs__item.is-active {
  font-size: 15px;
  font-weight: 600;
  color: #409eff;
}
</style>