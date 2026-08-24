<template>
  <div class="left">
    <div class="el-card__header clearfix">物资分类</div>
    <div class="tree-box-con">
      <div v-height>
        <el-scrollbar style="height: 100%">
          <el-tree
            ref="tree"
            :data="parentTree"
            :props="defaultProps"
            :highlight-current="true"
            :filter-node-method="filterNode"
            :default-expand-all="true"
            :expand-on-click-node="false"
            icon-class="el-icon-arrow-right"
            node-key="value"
            @node-click="handleClick"
          >
            <div slot-scope="{ node }" class="custom-tree-node">
              <div class="flex-box">
                <span class="over-text">{{ node.label }}</span>
              </div>
            </div>
          </el-tree>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TreeVue',
  props: {
    parentTree: {
      type: Array,
      default: () => {
        return []
      }
    },
    frameId: {
      type: Number,
      default: () => {
        return 0
      }
    },
    types: {
      type: Number,
      default: () => {
        return 0
      }
    }
  },
  data() {
    return {
      drawerConfig: {},
      filterText: '',
      treeData: [],
      isShowEdit: '',
      multipleSelection: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      focusShow: true,
      frame_id: 0,
      personConfig: {},
      parentId: [],
      topId: '', // 顶级id
      activeValue: '' // 记录当前那个框展开
    }
  },
  watch: {
    parentTree: {
      handler(nVal) {
        this.$nextTick(() => {
          if (!this.isShowEdit && nVal.length) {
            const value = this.treeData[0]
            this.$refs.tree.setCurrentKey(nVal[0].value)
          }
          this.topId = nVal.length ? nVal[0].value : ''
        })
      },
      deep: true
    },
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  methods: {
    // 编辑窗口显示
    handleShow(value) {
      this.activeValue = value
    },
    // 编辑窗口隐藏
    handleHide() {
      this.activeValue = ''
    },
    // tree节点点击
    handleClick(node) {
      if (node == 1) {
        this.focusShow = true
        this.frame_id = 0
        this.isShowEdit = ''
        this.$emit('frameId', 0)
        this.$refs.tree.setCurrentKey()
      } else {
        this.focusShow = false
        if (this.activeValue) {
          this.$refs[`pop-${this.activeValue}`].doClose()
        }
        this.frame_id = node.value
        this.isShowEdit = ''
        this.$emit('frameId', node.value)
      }
    },
    // 显示tree编辑
    showTreeEdit(node, data) {
      this.isShowEdit = data.value
    },
    // tree搜索过滤
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    }
  }
}
</script>

<style lang="scss" scoped>
.left {
  border-right: 1px solid #eeeeee;
  margin: -20px 0;
  // height: 100vh;
}
.tree-icon {
  margin-right: 5px;
  font-size: 18px;
  color: #ffca28;
}
::v-deep .el-card__header {
  border-bottom: none;
  padding-left: 0;
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 14px;
  color: #303133;
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background: rgba(24, 144, 255, 0.08);
  border-color: #1890ff;
}
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
::v-deep .tree-box-con .el-tree-node__content {
  height: 40px;
  padding-right: 15px;
  border-right: 2px solid transparent;
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .custom-tree-node,
.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .right-icon {
  display: inline-block;
  color: #1890ff;
  font-weight: 800;
}

::v-deep.el-tree-node > .el-tree-node__children {
  overflow: inherit;
}
.custom-tree-node {
  position: relative;
  width: 90%;
  font-size: 14px;
  font-weight: 400;
  .right-icon {
    display: none;
    position: absolute;
    right: -30px;
    top: 50%;
    transform: translateY(-50%);
  }
}
.flex-box {
  display: flex;
  align-items: center;
}
.tree-box-con {
  padding-bottom: 12px;
  margin-left: -20px;
}

.main-box {
  cursor: pointer;
  height: 40px;
  display: flex;
  align-items: center;
  padding-left: 20px;

  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
  span {
    font-size: 15px;
  }
}
.focus {
  background: rgba(24, 144, 255, 0.08) !important;
  color: #1890ff !important;
  font-weight: 800;
}
</style>
