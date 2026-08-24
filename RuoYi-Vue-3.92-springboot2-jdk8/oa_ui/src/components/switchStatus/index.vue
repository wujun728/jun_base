<!-- 状态切换开关组件 -->
<template>
  <div class="composite-selector">
    <!-- 显示名称 -->
    <span>{{name}}：</span>
    <!-- 开关组件 -->
    <el-switch
      v-model="internalValue"
      :active-text="activeText"
      :inactive-text="inactiveText">
    </el-switch>
  </div>
</template>

<script>
export default {
  name: 'switchStatus',
  props: {
    // 开关状态值
    value: {
      default: true,
      type: Boolean
    },
    // 开启状态显示文本
    activeText: {
      default: undefined,
      type: String
    },
    // 关闭状态显示文本
    inactiveText: {
      default: undefined,
      type: String
    },
    // 开关名称
    name: {
      default: undefined,
      type: String
    }
  },
  data() {
    return {
      // 内部维护的开关状态
      internalValue: this.value
    };
  },
  // 可以添加watch监听internalValue变化并触发事件
  watch: {
    internalValue(newVal) {
      this.$emit('input', newVal);
      this.$emit('change', newVal);
    }
  }
};
</script>

<style scoped lang="scss">
/* 选择器样式 */
.el-select .el-input {
  width: 130px;
}
/* 输入框前置选择器样式 */
.el-input-group__prepend .el-select {
  width: 100px;
}
/* 复合选择器容器样式 */
.composite-selector {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
