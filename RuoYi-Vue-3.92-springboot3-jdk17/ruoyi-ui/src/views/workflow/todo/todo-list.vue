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
              <el-button :type="showType === 'all' ? 'primary' : ''" @click="getAll">全部（{{ allTotal }}）</el-button>
              <el-button :type="showType === 'noRead' ? 'primary' : ''" @click="getNoReadList">
                <i class="el-icon-time" />
                未读（{{ totalNoRead }}）
              </el-button>
              <el-button plain icon="el-icon-refresh" @click="resetQuery">刷新</el-button>
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
    <!-- 表格 -->
    <group-table v-if="groupLable == 0" ref="table" :todoType="1" @resetAllToal="resetAllToal" @resetNoReadTotal="resetNoReadTotal" />
    <!-- 折叠面板 -->
    <group-collapse v-if="groupLable == 1" ref="collapse" @resetAllToal="resetAllToal" @resetNoReadTotal="resetNoReadTotal" />
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
      activeName: ["1"],
      // 显示搜索条件
      showSearch: true,
      // 展示方式（存储在localstore中，如果没有就默认0）
      groupLable: localStorage.getItem("todo_show_sroup_type") || 0,
      // 展示类型，默认全部-all，未读-noRead
      showType: "all",
      // 全部待办总数
      allTotal: 0,
      // 未读总数
      totalNoRead: 0,
      // 展开状态
      isSpread: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
      },
    };
  },
  mounted() {
    //监听待办更新事件
    this.$eventBus.$on(eventType[1], (payload) => {
      console.log("收到消息:", payload.text);
      this.initData();
    });
    this.initData();
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
    handleClick() {},
    /** 初始化 */
    initData() {
      this.getAll();
    },
    /** 展示方式切换 */
    handleGroupModelChange(label) {
      if (this.groupLable == label) return;
      this.groupLable = label;
      localStorage.setItem("todo_show_sroup_type", label);
      this.showType = "all";
      this.getChildData();
    },
    /** 获取全部待办 */
    getAll() {
      this.showType = "all";
      this.handleQuery();
    },
    /** 获取未读待办 */
    getNoReadList() {
      this.showType = "noRead";
      this.queryParams.pageNum = 1;
      // 列表展示
      if (this.groupLable == 0) {
        this.$refs.table.getNoReadData(this.queryParams);
        return;
      }
      // 聚合展示
      this.$refs.collapse.getNoReadData(this.queryParams);
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getChildData();
    },
    /** 刷新 */
    resetQuery() {
      this.resetForm("queryForm");
      if (this.showType === "all") {
        this.handleQuery();
      } else {
        this.getNoReadList();
      }
    },
    /** 获取子组件数据 */
    getChildData() {
      this.$nextTick(() => {
        // 列表展示
        if (this.groupLable == 0) {
          this.$refs.table.getAllData(this.queryParams);
          this.$refs.table.getNoRead();
          return;
        }
        // 聚合展示
        this.$refs.collapse.getAllData(this.queryParams);
        this.$refs.collapse.getNoRead();
      });
    },
    /** 重新设置总数 */
    resetAllToal(total) {
      this.allTotal = total;
    },
    /** 重新设置未读数 */
    resetNoReadTotal(val) {
      this.totalNoRead = val;
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
