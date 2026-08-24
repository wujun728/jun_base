<template>
  <div class="mouseActionContainer">
    <el-tooltip
      class="item"
      effect="dark"
      :content="
        useLeftKeySelectionRightKeyDrag
          ? '当前：左键拖动画布，右键框选节点'
          : '当前：左键框选节点，右键拖动画布'
      "
      placement="top"
    >
      <div
        class="nav-btn xmind-iconfont"
        :class="[useLeftKeySelectionRightKeyDrag ? 'iconmouseR' : 'iconmouseL']"
        @click="toggleAction"
      ></div>
    </el-tooltip>
  </div>
</template>

<script>
export default {
  name: 'MouseAction',
  props: {
    mindMap: {
      type: Object
    }
  },
  data() {
    return {
      useLeftKeySelectionRightKeyDrag: false
    }
  },
  methods: {

    toggleAction() {
      let val = !this.useLeftKeySelectionRightKeyDrag
      this.useLeftKeySelectionRightKeyDrag = val
      this.mindMap.updateConfig({
        useLeftKeySelectionRightKeyDrag: val
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.mouseActionContainer {
  display: flex;
  align-items: center;

  .item {
    margin-right: 12px;

    &:last-of-type {
      margin-right: 0;
    }
  }

  .nav-btn {
    cursor: pointer;
    font-size: 18px;
  }
}
</style>
