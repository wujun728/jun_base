<template>
  <div class="tree-box mt14">
    <el-scrollbar style="height: 100%">
      <el-tree
        ref="tree"
        :data="treeData"
        :props="defaultProps"
        :highlight-current="true"
        :default-expanded-keys="[0]"
        :expand-on-click-node="false"
        icon-class="el-icon-arrow-right"
        node-key="value"
        @node-click="handleClick"
      >
        <div slot-scope="{ node, data }" class="custom-tree-node">
          <span class="flex-box">
            {{ node.label }}
          </span>
        </div>
      </el-tree>
    </el-scrollbar>
  </div>
</template>

<script>
export default {
  name: 'MenuTree',
  props: {
    treeData: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'label',
      },
    };
  },
  watch: {
    treeData: {
      handler(nVal, oVal) {
        this.$nextTick(() => {
          if (nVal.length) {
            const value = nVal[0].value;
            this.$refs.tree.setCurrentKey(value);
          }
        });
      },
      deep: true,
    },
  },
  methods: {
    // tree节点点击
    handleClick(node, data) {
      this.$emit('frameId', node.value);
    },
  },
};
</script>

<style lang="scss" scoped>
.tree-icon {
  margin-right: 5px;
  font-size: 18px;
  color: #ffca28;
}
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background: rgba(24, 144, 255, 0.08);
  border-color: #1890ff;
}
::v-deep.el-tree-node__content {
  height: 40px;
  padding-right: 15px;
  border-right: 2px solid transparent;
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .custom-tree-node,
.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .right-icon {
  display: inline-block;
  font-weight: 800;
  color: #1890ff;
}

::v-deep.el-tree-node > .el-tree-node__children {
  overflow: inherit;
}
.custom-tree-node {
  position: relative;
  width: 100%;
  .right-icon {
    display: none;
    position: absolute;
    right: 10px;
  }
  .edit-box {
    z-index: 9999;
    position: absolute;
    right: 0;
    bottom: -124px;
    width: 120px;
    padding: 7px 0;
    box-shadow: 0px 0px 8px 0px rgba(0, 0, 0, 0.06);
    border-radius: 6px;
    color: #000000;
    background: #fff;
    .edit-item {
      height: 40px;
      line-height: 40px;
      padding-left: 19px;
      font-size: 13px;
      &:active {
        background: #f5f5f5;
      }
    }
  }
}
.tree-box {
  margin-left: -20px;
  height: calc(100% - 14px);
  background: #fff;
}
</style>
