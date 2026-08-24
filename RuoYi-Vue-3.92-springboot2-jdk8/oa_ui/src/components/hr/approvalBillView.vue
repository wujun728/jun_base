<!-- @FileDescription: 人事-审批设置-明细新增组件 -->
<template>
  <div class="ab-container">
    <slot></slot>
    <div v-if="!formCreateInject.rule.originChildren.length" class="ab-empty">
      <div>可拖拽添加多个字段</div>
      <span>(不支持明细中添加明细字段)</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ApprovalBillView',
  props: {
    formCreateInject: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {}
  },
  mounted() {},
  methods: {}
}
</script>

<style lang="scss" scoped>
.ab-container {
  position: relative;
  z-index: 4;
  background: #f3f9ff;
  ::v-deep.drag-tool {
    padding: 0 !important;
    background: #f3f9ff;
  }
  ::v-deep .drag-box {
    min-height: 177px;
    position: relative;
    z-index: 2;
    background: #f3f9ff;
    padding: 0 20px 10px 20px;
  }
}

.ab-empty {
  position: absolute;
  top: 0;
  left: 0;
  background-color: rgb(244, 249, 254);
  text-align: center;
  padding: 61px 0;
  width: 100%;

  div {
    font-weight: 700;
    line-height: 13px;
    font-size: 14px;
    margin-bottom: 6px;
  }

  span {
    color: #999;
  }
}
</style>
