
<template>
  <div class="task-header">
    <div class="actions">
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="addSchedule">新建日程</el-button>
    </div>
    <div class="input-wrapper">
      <el-input
        v-model="title"
        prefix-icon="el-icon-search"
        placeholder="请输入关键词，按回车搜索"
        clearable
        @clear="handleQuery"
        @keyup.enter.native="handleQuery"
      />
      <el-checkbox-group v-model="checkList" @change="handleCheckChange">
        <el-checkbox label="all">全部</el-checkbox>
        <!-- <el-checkbox label="level">重要的</el-checkbox> -->
        <el-checkbox label="draft">我创建的</el-checkbox>
        <el-checkbox label="join">我参与的</el-checkbox>
      </el-checkbox-group>
    </div>

    <ScheduleHandler ref="schedule" :open="open" :formData="formData" title="新增日程" @close="close" @submit="submit" />
  </div>
</template>

<script>
import ScheduleHandler from "../schedule/save.vue";
export default {
  components: { ScheduleHandler },
  data() {
    return {
      checkList: ["all"],
      // 日程控制开关
      open: false,
      // 日程查询标题
      title: null,
      formData: {},
    };
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery() {
      this.$emit("title-change", this.title);
    },
    handleCheckChange(value) {
      if (value.length > 1) {
        // 如果选择了多个，只保留最后一个选中的
        this.checkList = [value[value.length - 1]];
      } else if (value.length === 0) {
        // 如果全不选，默认恢复“全部”
        this.checkList = ["all"];
      }
      // 触发筛选逻辑
      let checked = this.checkList[0];
      let level = checked == "level" ? "1" : null;
      let partType = ["draft", "join"].includes(checked) ? (checked == "draft" ? "0" : "1") : null;
      this.$emit("filter-change", level, partType);
    },
    /** 重置过滤条件 */
    resetFilter() {
      this.checkList = ["all"];
    },
    /** 新增日程 */
    addSchedule() {
      this.open = true;
    },
    /** 成功创建日程，刷新 */
    submit() {
      this.open = false;
      this.$emit("add-schedule");
    },
    close() {
      this.open = false;
    },
    resetType() {
      this.$refs.schedule.getTypeList();
    },
  },
};
</script>

<style scoped lang="scss">
.task-header {
  display: flex;
  align-items: center;
  flex-wrap: nowrap;
  gap: 10px;
  padding: 10px 20px 10px 15px;

  .actions {
    flex: 0 0 40%; /* 固定宽度占比 1/3 */
    min-width: 200px; /* 防止过小变形 */
    white-space: nowrap;
  }

  .input-wrapper {
    flex: 0 0 50%; /* 固定宽度占比 2/3 */
    min-width: 300px;
    display: flex;
    align-items: center;
    gap: 10px;
    white-space: nowrap;
    overflow: hidden;
  }

  ::v-deep .input-wrapper .el-input input {
    border: none;
    border-bottom: 1px solid #e4e4e4;
    border-radius: 0;
    font-size: 14px;
    // width: 100%;
    // max-width: 440px;
  }

  ::v-deep .input-wrapper .el-input__inner:focus {
    border-color: #409eff;
  }
}
</style>