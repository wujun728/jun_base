<template>
  <div>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label prop="title">
            <el-input
              v-model="queryParams.title"
              prefix-icon="el-icon-search"
              placeholder="请输入标题关键字，按回车查询"
              clearable
              @clear="resetQuery"
              @keyup.enter.native="handleQuery"
              style="width: 300px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button-group>
              <el-button type="primary" @click="getList">全部（{{ allTotal }}）</el-button>
            </el-button-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item style="display: flex; justify-content: flex-end">
            <span v-if="groupLable == 1">
              <el-button v-if="isSpread" type="text" @click="collapseList(0)">
                全部收起
                <i class="el-icon-arrow-up"></i>
              </el-button>
              <el-button v-else type="text" @click="collapseList(1)">
                全部展开
                <i class="el-icon-arrow-down"></i>
              </el-button>
            </span>
            <el-tooltip content="按类型聚合展示">
              <svg-icon icon-class="tile" class="model-icon" :class="{ active: groupLable == 1 }" @click="handleGroupModelChange(1)"></svg-icon>
            </el-tooltip>
            <el-tooltip content="按列表分页展示">
              <svg-icon icon-class="list" class="model-icon" :class="{ active: groupLable == 0 }" tooltip="列表模式" @click="handleGroupModelChange(0)"></svg-icon>
            </el-tooltip>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <GroupTable v-if="groupLable == 0" ref="table" :todoType="2" @resetAllToal="resetAllToal" @resetNoReadTotal="resetNoReadTotal" />
    <GroupCollapse v-if="groupLable == 1" ref="collapse" @resetAllToal="resetAllToal" @resetNoReadTotal="resetNoReadTotal" />
  </div>
</template>

<script>
import GroupTable from "./group-table.vue";
import GroupCollapse from "./group-collapse.vue";
import eventType from "@/utils/socket/eventType.js";

export default {
  name: "Todo",
  components: { GroupTable, GroupCollapse },
  data() {
    return {
      // 显示搜索条件
      showSearch: true,
      // 展示方式（存储在localstore中，如果没有就默认0）
      groupLable: localStorage.getItem("todo_show_sroup_type") || 0,
      // 全部待办总数
      allTotal: 0,
      // 展开状态
      isSpread: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: "2",
        title: null,
      },
    };
  },
  mounted() {
    //监听待办更新事件
    this.$eventBus.$on(eventType[1], (payload) => {
      console.log("收到消息:", payload.text);
      this.getList();
    });
    this.getList();
  },
  computed: {
    // 接收人
    urgency() {
      return (val) => {
        if (!val || val === "0") return "";
        let desc = "";
        switch (val) {
          case "1":
            desc = "(加急)";
            break;
          case "2":
            desc = "(紧急)";
            break;
          case "3":
            desc = "(特急)";
            break;
        }
        return desc;
      };
    },
  },
  beforeDestroy() {
    // 组件销毁时移除监听
    this.$eventBus.$off(eventType[1]);
  },
  methods: {
    /** 展示方式切换 */
    handleGroupModelChange(label) {
      if (this.groupLable == label) return;
      this.groupLable = label;
      localStorage.setItem("todo_show_sroup_type", label);
      this.getChildData();
    },
    /** 获取全部待办 */
    getList() {
      this.handleQuery();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getChildData();
    },
    /** 刷新 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 获取子组件数据 */
    getChildData() {
      this.$nextTick(() => {
        // 列表展示
        if (this.groupLable == 0) {
          this.$refs.table.getAllData(this.queryParams);
          return;
        }
        // 聚合展示
        this.$refs.collapse.getAllData(this.queryParams);
      });
    },
    /** 重新设置总数 */
    resetAllToal(total) {
      this.allTotal = total;
    },
    /** 重新设置未读数 */
    resetNoReadTotal(total) {
      this.totalNoRead = total;
    },
    /** 全部展开、收起 */
    collapseList(type) {
      this.isSpread = type == 0 ? false : true;
      this.$refs.collapse.handleCollapse(type);
    },
  },
};
</script>

<style lang="scss" scoped>
.model-icon {
  margin-left: 12px;
  padding-bottom: 0;
  font-size: 20px;
  cursor: pointer;
  color: #909399;

  &:hover {
    color: #409eff;
  }
}

.model-icon.active {
  color: #409eff;
}
</style>
