<template>
  <!-- 统计 -->
  <div class="sum-container">
    <div class="sum-item pending-approval pointer" @click="goTodo">
      <div class="content">
        <div class="count">{{ todoTotal }}</div>
        <div class="desc">待审批</div>
      </div>
      <div>
        <svg-icon icon-class="todo" class="todo-flag" />
      </div>
    </div>
    <div class="sum-item pending-review pointer" @click="goRead">
      <div class="content">
        <div class="count">{{ toReadTotal }}</div>
        <div class="desc">待查阅</div>
      </div>
      <div>
        <svg-icon icon-class="toRead" class="toread-flag" />
      </div>
    </div>
    <div class="sum-item my-apply pointer" @click="goMyDraft">
      <div class="content">
        <div class="count">{{ myDraftTotal }}</div>
        <div class="desc">我发起的</div>
      </div>
      <div>
        <svg-icon icon-class="add" class="add-flag" />
      </div>
    </div>
  </div>
</template>

<script>
import { stat } from "@/api/workflow/todo";
import { statMyDraft } from "@/api/workflow/draft";

export default {
  name: "StaticTodoModule",
  data() {
    return {
      // 待办总数
      todoTotal: "0",
      // 待阅总数
      toReadTotal: "0",
      // 我起草的总数
      myDraftTotal: "0",
    };
  },
  mounted() {
    this.initData();
  },
  methods: {
    initData() {
      this.getTodoTotal();
      this.getToReadTotal();
      this.getMyDraftTotal();
    },
    /** 查询待办总数 */
    getTodoTotal() {
      stat("1").then((response) => {
        if (response.code == 200) {
          this.todoTotal = response.data >= 999 ? "999+" : response.data;
        }
      });
    },
    /** 查询待阅总数 */
    getToReadTotal() {
      stat("2").then((response) => {
        if (response.code == 200) {
          this.toReadTotal = response.data >= 999 ? "999+" : response.data;
        }
      });
    },
    /** 查询我起草的总数 */
    getMyDraftTotal() {
      statMyDraft().then((response) => {
        if (response.code == 200) {
          this.myDraftTotal = response.data >= 999 ? "999+" : response.data;
        }
      });
    },
    /** 待办 */
    goTodo() {
      this.$router.push("/my/todo");
    },
    /** 待阅 */
    goRead() {
      this.$router.push({ path: "/my/todo", query: { type: "2" } });
    },
    /** 我起草的 */
    goMyDraft() {
      this.$router.push({ path: "/my/apply" });
    },
  },
};
</script>

<style scoped lang="scss">
.sum-container {
  height: calc(12vh - 10px);
  display: flex;
  padding: 10px;
  border: 1px solid #f5f5f5;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  margin-bottom: 10px;
  border-radius: 4px;
}
.sum-item {
  width: 180px;
  padding: 24px;
  border-radius: 5px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-right: 24px;
  text-align: center;
}
.todo-flag,
.toread-flag,
.add-flag {
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
.add-flag {
  margin-right: 12px;
  color: #e8f4e6;
}
.pending-approval {
  background: linear-gradient(90deg, #e8f4ff, #f1f7ff, #f7faff);
}
.pending-review {
  background: linear-gradient(90deg, #fff6ec, #fffcf7);
}
.my-apply {
  background: linear-gradient(90deg, #f0ffec, #fafff7);
}
.content {
  text-align: center;
}
.count {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}
.desc {
  margin-top: 3px;
  font-size: 12px;
  color: #909399;
}
</style>

