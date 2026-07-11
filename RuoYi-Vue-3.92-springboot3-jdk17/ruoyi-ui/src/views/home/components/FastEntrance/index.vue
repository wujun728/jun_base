<template>
  <!-- 快捷入口 -->
  <div class="fast-container">
    <div class="position-title">
      <span>
        <svg-icon icon-class="fast" class="fast-flag" />常用功能
      </span>
    </div>
    <div class="fast-body">
      <div class="fast-item pointer" @click="goApply">
        <svg-icon icon-class="apply" class="apply-icon" />
        <div class="item-name">发起申请</div>
      </div>
      <div class="fast-item pointer" @click="addSchedule">
        <svg-icon icon-class="fast-schedule" class="schedule-icon" />
        <div class="item-name">创建日程</div>
      </div>
      <div class="fast-item pointer" @click="goKbs">
        <svg-icon icon-class="learn" class="fast-item-icon" />
        <div class="item-name">知识库</div>
      </div>
      <div class="fast-item pointer" @click="goContact">
        <svg-icon icon-class="contact-book" class="fast-item-icon" />
        <div class="item-name">通讯录</div>
      </div>
    </div>
    <!-- 新增日程组件 -->
    <schedule-handler :open="open" :formData="formData" title="新增日程" @close="close" @submit="submit" />
  </div>
</template>

<script>
import ScheduleHandler from "@/views/schedule/components/schedule/save.vue";

export default {
  name: "FastEntranceModule",
  components: { ScheduleHandler },
  data() {
    return {
      // 日程控制开关
      open: false,
      formData: {},
    };
  },
  methods: {
    /** 发起申请 */
    goApply() {
      this.$router.push("/my/newstart");
    },
    /** 知识库 */
    goKbs() {
      this.$router.push("/kbs/topic");
    },
    /** 通讯录 */
    goContact() {
      this.$router.push("/contact/book");
    },
    /** 新增日程 */
    addSchedule() {
      this.open = true;
    },
    /** 成功创建日程，刷新 */
    submit() {
      this.open = false;
      this.$emit("reflesh");
    },
    close() {
      this.open = false;
    },
  },
};
</script>

<style scoped lang="scss">
.position-title {
  font-size: 14px;
  font-weight: bold;
  border-bottom: 1px solid #f1f1f1;
  padding: 12px;
}

.fast-container {
  display: flex;
  flex-direction: column;
  height: calc(24vh - 10px);
  border-radius: 4px;
  border: 1px solid #f5f5f5;
  box-sizing: border-box;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  margin-bottom: 10px;
}
.fast-flag {
  margin-right: 4px;
}
.fast-body {
  flex: 1;
  display: flex;
  align-items: center;
  padding-left: 12px;
  gap: 24px;
}
.fast-item {
  width: 80px;
  height: 80px;
  padding: 5px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
}
.apply-icon {
  width: 40px;
  height: 40px;
  margin-bottom: 7px;
  padding-top: 3px;
}

.fast-item-icon {
  width: 42px;
  height: 42px;
  margin-bottom: 5px;
}
.schedule-icon {
  width: 38px;
  height: 38px;
  margin-bottom: 9px;
  padding-top: 3px;
}
.item-name {
  font-size: 14px;
  color: #606266;
}

.add-box {
  width: 40px;
  height: 40px;
  border: 1px dashed #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: #409eff;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: #fff;
}

.add-box:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
  color: #409eff;
}
</style>

