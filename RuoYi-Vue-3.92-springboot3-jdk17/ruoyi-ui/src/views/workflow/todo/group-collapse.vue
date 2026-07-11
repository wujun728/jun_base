<template>
  <div class="table-body">
    <el-collapse v-model="activeNames">
      <el-collapse-item v-for="item in collapseList" :key="item.templateId" :name="item.templateId">
        <template slot="title">
          <i :class="['header-icon', activeNames.includes(item.templateId) ? 'el-icon-caret-bottom' : 'el-icon-caret-top']" class="title-icon"></i>
          <span class="title-text">{{ item.templateName}}</span>
          <span class="count">({{ item.count }})</span>
        </template>
        <el-table
          v-loading="loading"
          ref="todoTable"
          :data="item.todoList"
          :row-class-name="tableRowClassName"
          :show-header="false"
          stripe
          class="pointer"
          @row-click="handleRowClick"
          :element-loading-text="loadingText"
          element-loading-spinner="el-icon-loading"
        >
          <el-table-column label="标题" align="left" prop="title" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-if="scope.row.urgeFlag === '1'" class="urge">【催】</span>
              <span v-if="scope.row.urgencyStatus && scope.row.urgencyStatus !== '0'" class="urgeStatus">{{ urgency(scope.row.urgencyStatus) }}</span>
              <span v-if="scope.row.handleType === '0'" class="draft">【草稿】</span>
              <span v-if="scope.row.handleType === '2'" class="hanleType">【驳回】</span>
              <span v-if="scope.row.handleType === '3'" class="hanleType">【退回】</span>
              <span v-if="['4','5'].includes(scope.row.handleType)" class="hanleType">【取回】</span>
              <span v-if="scope.row.handleType === '6'" class="hanleType">【抄送】</span>
              <span v-if="scope.row.handleType === '7'" class="hanleType">【委派】</span>
              <span v-if="scope.row.handleType === '8'" class="hanleType">【转办】</span>
              <span v-if="scope.row.handleType === '9'" class="hanleType">【加签】</span>
              <span v-if="scope.row.handleType === '10'" class="hanleType">【委托】</span>
              <span v-if="scope.row.handleType === '11'" class="hanleType">【秘书办理】</span>
              <span>{{ scope.row.title }}</span>
            </template>
          </el-table-column>
          <el-table-column label="当前环节" align="left" prop="curNode" width="150" show-overflow-tooltip />
          <el-table-column label="发送人" align="left" prop="senderName" width="160" show-overflow-tooltip />
          <el-table-column label="发送时间" align="left" prop="sendTime" width="240">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-collapse-item>
      <!-- <el-empty v-if="!loading && !collapseList.length" :image-size="50"></el-empty> -->
    </el-collapse>
  </div>
</template>

<script>
import { listTodoCollapse, readTodo, noRead, readCopyTodo } from "@/api/workflow/todo";

export default {
  name: "Todo",
  data() {
    return {
      // 当前展开的面板
      activeNames: [],
      // 遮罩层
      loading: true,
      // loading文本内容
      loadingText: "正在加载中...",
      // 全部待办总数
      allTotal: 0,
      // 未读总数
      totalNoRead: 0,
      // 待办数据
      collapseList: [],
      // 查询参数
      queryParams: {},
    };
  },
  computed: {
    // 接收人
    urgency() {
      return (val) => {
        if (!val || val === "0") return "";
        let desc = "";
        switch (val) {
          case "1":
            desc = "【加急】";
            break;
          case "2":
            desc = "【紧急】";
            break;
          case "3":
            desc = "【特急】";
            break;
        }
        return desc;
      };
    },
  },
  methods: {
    /** 行样式控制 */
    tableRowClassName({ row, rowIndex }) {
      if (row.readFlag === "0") {
        return "no-read";
      }
      return "";
    },
    /** 行点击 */
    handleRowClick(row, column, event) {
      let pageType = "1";
      if (row.handleType === "0") {
        pageType = "0"; // 草稿
      }
      if (row.handleType === "6") {
        pageType = "2";
        readCopyTodo(row.id).then((res) => {
          if (res.code === 200) {
            this.totalNoRead -= 1;
            this.updateNoReadNum(this.totalNoRead);
          }
        });
      } else if (row.readFlag === "0") {
        // 设置已读
        row.readFlag = "1";
        readTodo(row.id).then((res) => {
          if (res.code === 200) {
            this.totalNoRead -= 1;
            this.updateNoReadNum(this.totalNoRead);
          }
        });
      }
      this.$router.push({
        path: "/workflow/flowForm/" + new Date().getTime(),
        query: {
          title: row.title,
          pageType: pageType,
          todoId: row.id,
          businessId: row.businessId,
          procInsId: row.procInstId,
          templateId: row.templateId,
          taskId: row.taskId,
          userId: row.curHandler,
          draft: row.handleType === "0" ? "1" : "0",
          type: row.type,
          handleType: row.handleType,
        },
      });
    },
    /** 更新未读数 */
    updateNoReadNum(num) {
      num = num <= 0 ? 0 : num;
      this.$emit("resetNoReadTotal", num);
    },
    /** 初始化数据 */
    async getAllData(params) {
      this.queryParams = params;
      await this.getList();
      // 默认展开全部
      this.collapseAll();
      this.$emit("resetAllToal", this.allTotal);
    },
    /** 获取未读数 */
    getNoRead() {
      noRead().then((res) => {
        this.totalNoRead = res.data;
        this.updateNoReadNum(this.totalNoRead);
      });
    },
    /** 获取未读待办 */
    getNoReadData() {
      this.loading = true;
      this.queryParams.pageNum = 1;
      listTodoCollapse({ ...this.queryParams, readFlag: "0" }).then((response) => {
        this.collapseList = response.data;
        // 默认展开全部
        this.collapseAll();
        this.handleTotal(1);
        this.loading = false;
      });
    },
    /** 查询待办列表 */
    async getList() {
      this.loading = true;
      const response = await listTodoCollapse(this.queryParams);
      this.collapseList = response.data;
      this.handleTotal(0);
      this.loading = false;
    },
    /** 处理全部、未读总数 */
    handleTotal(type) {
      if (type == 0) {
        this.allTotal = 0;
        if (this.collapseList && this.collapseList.length > 0) {
          this.collapseList.forEach((item) => {
            this.allTotal += item.count;
          });
        }
        this.$emit("resetAllToal", this.total);
      } else {
        this.totalNoRead = 0;
        if (this.collapseList && this.collapseList.length > 0) {
          this.collapseList.forEach((item) => {
            this.totalNoRead += item.count;
          });
        }
        this.$emit("resetNoReadTotal", this.totalNoRead);
      }
    },
    /** 处理展开、收起 */
    handleCollapse(type) {
      if (type == 1) {
        // 展开
        this.collapseAll();
      } else {
        this.activeNames = [];
      }
    },
    /** 展开全部 */
    collapseAll() {
      this.activeNames = [];
      if (this.collapseList && this.collapseList.length > 0) {
        this.collapseList.forEach((item) => {
          this.activeNames.push(item.templateId);
        });
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.table-body {
  min-height: calc(100vh - 270px);
}
.header-icon {
  margin-right: 3px;
  font-weight: bold;
}

.title-icon {
  color: #c0c4cc;
  font-weight: 500;
  font-size: 12px;
}
.title-text {
  font-size: 14px;
  font-weight: bold;
  margin-left: 3px;
}
.count {
  color: #1c84c6;
  margin-left: 4px;
}
.draft {
  font-size: 14px;
  font-weight: 500;
  margin-left: -6px;
}
.urge {
  font-size: 14px;
  font-weight: 600;
  color: #f19f00;
  margin-left: -6px;
}

.urgeStatus {
  font-size: 14px;
  font-weight: 600;
  color: #fd0505;
  margin-left: -6px;
}

.hanleType {
  font-size: 14px;
  font-weight: 600;
  color: #fd0505;
  margin-left: -6px;
}

::v-deep .el-collapse-item__header .el-icon-arrow-right {
  display: none;
}
::v-deep .el-collapse {
  border-top: none;
  border-bottom: none;
}
::v-deep .el-collapse-item {
  border-top: none !important;
}
::v-deep .el-collapse-item .el-collapse-item__header {
  font-size: 14px;
  font-weight: bold;
  // color: #1c84c6;
  border-top: none !important;
  background-color: transparent !important;
}
::v-deep .el-collapse-item__wrap {
  color: #303133;
  border-bottom: none;
}
::v-deep .el-collapse-item__header {
  border-bottom: none;
}
::v-deep .el-table .no-read {
  font-weight: 600;
}

::v-deep .el-table__row .cell {
  color: #303133;
  font-size: 14px;
  padding-left: 54px;
}
::v-deep .el-table__body tr td,
::v-deep .el-table__body tr th {
  border-bottom: none !important;
}
::v-deep .el-table {
  border: none !important;
}

::v-deep .el-table::before {
  height: 0px !important;
}

::v-deep .el-table__body tr {
  border-bottom: none !important;
}

::v-deep .el-table__body tr:last-child td {
  border-bottom: none !important;
}
</style>
