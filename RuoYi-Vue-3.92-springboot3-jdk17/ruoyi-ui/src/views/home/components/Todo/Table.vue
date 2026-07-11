<template>
  <!-- 待办组件-table风格 -->
  <div class="todo-list-container">
    <div class="sum-container">
      <div class="sum-item pending-approval">
        <div class="content">
          <div class="count">{{ todoTotal }}</div>
          <div class="desc">待审批</div>
        </div>
        <div>
          <svg-icon icon-class="todo" class="todo-flag" />
        </div>
      </div>
      <div class="sum-item pending-review">
        <div class="content">
          <div class="count">1</div>
          <div class="desc">待查阅</div>
        </div>
        <div>
          <svg-icon icon-class="toRead" class="toread-flag" />
        </div>
      </div>
    </div>
    <div class="todo-body">
      <el-table
        v-loading="loading"
        ref="todoTable"
        :data="todoList"
        :row-class-name="tableRowClassName"
        stripe
        class="pointer"
        height="60vh"
        @row-click="handleRowClick"
        element-loading-text="正在加载中..."
        element-loading-spinner="el-icon-loading"
      >
        <el-table-column label="序号" type="index" align="center" width="55">
          <template slot-scope="scope">
            <span>{{scope.$index + 1}}</span>
          </template>
        </el-table-column>
        <el-table-column label="标题" align="left" prop="title" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.urgeFlag === '1'" class="urge">【催】</span>
            <span v-if="scope.row.urgencyStatus !== '0'" class="urgeStatus">{{ urgency(scope.row.urgencyStatus) }}</span>
            <span v-if="scope.row.type === '0'" class="draft">【草稿】</span>
            <span>{{ scope.row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column label="类型" align="center" prop="templateName" width="150" show-overflow-tooltip />
        <el-table-column label="当前环节" align="center" prop="curNode" width="150" show-overflow-tooltip />
        <el-table-column label="发送人" align="center" prop="senderName" width="120" show-overflow-tooltip />
        <el-table-column label="发送时间" align="center" prop="sendTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!hasData" :image-size="50"></el-empty>
    </div>
    <div class="todo-footer">
      <div v-if="hasMore" @click="loadMore" class="load-more">
        加载更多
        <i class="el-icon-arrow-down" />
      </div>
      <div v-else class="no-more">已加载全部数据！</div>
    </div>
  </div>
</template>

<script>
import { listTodoTable, readTodo } from "@/api/workflow/todo";
export default {
  name: "TodoTableModule",
  data() {
    return {
      // 加载状态
      loading: false,
      // 待办总数
      todoTotal: 0,
      // 待办列表
      todoList: [],
      // 是否有更多
      hasMore: false,
      // 是否有数据
      hasData: true,
      // 请求参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
    };
  },
  mounted() {
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
    /** 查询待办列表 */
    async getList() {
      if (this.loading) return;
      this.loading = true;
      const response = await listTodoTable(this.queryParams);
      if (response.code == 200) {
        this.todoList = [...this.todoList, ...response.rows];
        this.todoTotal = response.total;
        this.queryParams.pageNum += 1;
        this.hasMore = this.todoTotal > this.todoList.length ? true : false;
        this.hasData = this.todoTotal > 0 ? true : false;
        this.loading = false;
      } else {
        this.hasMore = false;
      }
    },
    /** 加载更多 */
    async loadMore() {
      await this.getList();
    },
    /** 行样式控制 */
    tableRowClassName({ row, rowIndex }) {
      if (row.readFlag === "0") {
        return "no-read";
      }
      return "";
    },
    /** 行点击 */
    handleRowClick(row, column, event) {
      if (row.readFlag === "0") {
        row.readFlag = "1";
        readTodo(row.id);
      }
      this.$router.push({
        path: "/workflow/flowForm/" + new Date().getTime(),
        query: {
          title: row.title,
          pageType: "1",
          todoId: row.id,
          businessId: row.businessId,
          procInsId: row.procInstId,
          templateId: row.templateId,
          taskId: row.taskId,
          userId: row.curHandler,
          draft: row.type === "0" ? "1" : "0",
          type: row.type,
        },
      });
    },
  },
};
</script>

<style scoped lang="scss">
.sum-container {
  height: 52px;
  display: flex;
  margin: 10px 10px 0px 10px;
}

.sum-item {
  width: 200px;
  padding: 24px;
  border-radius: 5px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-right: 24px;
  text-align: center;
}

.todo-flag,
.toread-flag {
  width: 32px;
  height: 32px;
}
.todo-flag {
  margin-right: 12px;
  color: #e1f0ff;
}

.toread-flag {
  margin-right: 12px;
  color: #ffefdc;
}
.pending-approval {
  background: linear-gradient(90deg, #e8f4ff, #f1f7ff, #f7faff);
}

.pending-review {
  background: linear-gradient(90deg, #fff6ec, #fffcf7);
}

.content {
  text-align: center;
}

.count {
  font-size: 16px;
  font-weight: bold;
  color: #606266;
}

.desc {
  font-size: 13px;
  color: #909399;
}

.todo-list-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 73vh;
  border: 1px solid #e8e8e8;
  box-sizing: border-box;
  background-color: #ffffff;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
}

.todo-body {
  flex: 1;
  padding: 10px 10px 0 10px;
  box-sizing: border-box;
}

.title {
  font-size: 14px;
  font-weight: bold;
}

.todo-item {
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.todo-footer {
  flex: 0 0 40px; // 固定高度
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ffffff;
}

.load-more,
.no-more {
  font-size: 13px;
  color: #909399;
  cursor: pointer;
  text-align: center;
}

::v-deep .el-table .no-read {
  font-weight: 600;
}
::v-deep .el-table th {
  background-color: #f3f9ff !important;
  color: #5e7fa2;
  font-size: 14px;
}

::v-deep .el-table__row .cell {
  color: #606266;
  font-size: 14px;
}

.draft {
  font-size: 14px;
  font-weight: 600;
  color: #606266;
}

.urge {
  font-size: 14px;
  font-weight: 600;
  color: #f19f00;
}

.urgeStatus {
  font-size: 14px;
  font-weight: 600;
  color: #fd0505;
}
</style>

