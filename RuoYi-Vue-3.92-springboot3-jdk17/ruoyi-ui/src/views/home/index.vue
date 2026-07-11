<template>
  <!-- 首页 -->
  <div class="home">
    <!-- 左侧 -->
    <div class="left-position">
      <div class="left-header">
        <static ref="static" />
      </div>
      <div class="left-body">
        <todo ref="todo" />
      </div>
      <div class="left-foot">
        <fast-entrance @reflesh="reflesh" />
        <news />
      </div>
    </div>
    <!-- 右侧 -->
    <div class="right-position">
      <welcome />
      <notice />
      <schedule ref="schedule" />
    </div>
  </div>
</template>
  
<script>
import Todo from "./components/Todo/Collapse.vue";
import FastEntrance from "./components/FastEntrance";
import Welcome from "./components/Welcome";
import Notice from "./components/Notice";
import News from "./components/News";
import Static from "./components/Static";
import Schedule from "./components/Schedule";
import eventType from "@/utils/socket/eventType.js";

export default {
  name: "HomeIndex",
  components: { Todo, Notice, News, Welcome, FastEntrance, Static, Schedule },
  data() {
    return {};
  },
  mounted() {
    this.$eventBus.$on(eventType[1], (payload) => {
      console.log("收到消息:", payload.text);
      this.refleshTodo();
    });
  },
  computed: {},
  methods: {
    reflesh() {
      this.$refs.schedule.getScheduleData();
    },
    refleshTodo() {
      this.$refs.static.initData();
      this.$refs.todo.getList();
    },
  },
};
</script>
  
<style scoped lang="scss">
.home {
  display: flex;
  justify-content: space-between;
  height: 88vh;
  padding: 12px;
}

.left-position {
  display: flex;
  flex-direction: column;
  width: 75%;
}
.left-foot {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}
.left-foot > div {
  flex: 1 1 48%;
  min-width: 0;
}
.right-position {
  display: flex;
  flex-direction: column;
  width: 24.5%;
}
</style>
  
  