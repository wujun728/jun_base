<template>
  <div class="tree-box">
    <div class="v-height-flag">
      <div class="clearfix el-card__header">
        <el-input
          v-model="filterText"
          placeholder="请输入应用，实体，接口名称搜索"
          clearable
          size="small"
          suffix-icon="el-icon-search"
        />
      </div>
      <div v-height>
        <el-scrollbar style="height: 100%">
          <div class="tree-box-con">
            <el-tree
              ref="tree"
              :data="list"
              highlight-current
              :default-expanded-keys="defaultFrame"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              :highlight-current="true"
              node-key="id"
              @node-click="handleClick"
            >
              <div slot-scope="{ node, data }" class="custom-tree-node">
                <div v-if="data.children">
                  <span class="iconfont iconjiekouwendang-01"></span> {{ data.name }}
                  <span v-if="data.crud_id == 0" class="color-doc">(系)</span>
                </div>
                <div v-else>
                  <span class="mr10" :style="getColor(data.method)">{{ data.method }}</span
                  >{{ data.name }}
                </div>
              </div>
            </el-tree>
          </div>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: '',
  components: {},
  props: {
    list: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      filterText: '',
      defaultFrame: []
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    },
    list: {
      handler(nVal, oVal) {
        this.$nextTick(() => {
          if (nVal.length) {
            const value = nVal[0].children[0].id
            this.defaultFrame = [value]
            this.$refs.tree.setCurrentKey(value)
          }
        })
      },
      deep: true
    }
  },
  mounted() {
    // this.$refs.tree.setCurrentKey(this.list[0].children[0])
  },

  methods: {
    // tree搜索过滤
    filterNode(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },
    handleClick(data, node, one) {
      if (data.children) return false
      this.$emit('getJson', data, node.parent.data.name)
    },
    getColor(type) {
      let str = {}
      if (type == 'POST') {
        str = { color: '#19BE6B' }
      } else if (type == 'PUT') {
        str = { color: '#ff9900' }
      } else if (type == 'DELETE') {
        str = { color: '#ED4014' }
      } else if (type == 'GET') {
        str = { color: '#1890FF' }
      }
      return str
    }
  }
}
</script>
<style scoped lang="scss">
.mr10 {
  display: inline-block;
  margin-right: 10px;
}
.tree-icon {
  margin-right: 5px;
  font-size: 15px;
  color: #ffca28;
}
.el-card__header ::v-deep.el-input--small .el-input__inner {
  cursor: pointer;
}

::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background: rgba(240, 250, 254, 0.6);
  border-color: #1890ff;
  .flex-box {
    color: #1890ff;
    font-weight: 600;
  }
}
::v-deep.tree-box-con .el-tree-node__content {
  width: 100%;
  height: 40px;
  padding-right: 15px;
  border-right: 2px solid transparent;
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .custom-tree-node,
.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .right-icon {
  display: inline-block;
  color: #1890ff;
  font-weight: 600;
}

::v-deep.el-tree-node > .el-tree-node__children {
  overflow: inherit;
}

.custom-tree-node {
  position: relative;
  width: calc(100% - 20px);
  .flex-box {
    display: flex;
    align-items: center;
    width: 100%;
    font-size: 14px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #303133;

    span:first-of-type {
      display: inline-block;
      max-width: 66%;
    }
    .right-icon {
      display: none;
      position: absolute;
      right: -6px;
      top: 0px;
    }
  }

  .edit-box {
    z-index: 200;
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

.tree-box-con {
  padding-bottom: 12px;
}

.tree-box {
  background: #fff;
}
</style>
