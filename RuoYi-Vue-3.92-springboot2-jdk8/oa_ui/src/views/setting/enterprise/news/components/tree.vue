<template>
  <div class="tree-box">
    <div class="v-height-flag">
      <div class="title">
        消息类型
        <!-- <el-tooltip class="item" effect="dark" content="新增消息类型" placement="bottom">
          <span class="el-icon-plus" v-if="type !== 2" @click="addType"></span>
        </el-tooltip> -->
      </div>
      <div v-height>
        <el-scrollbar style="height: 100%">
          <div class="tree-box-con">
            <el-tree
              ref="tree"
              :data="parentTree"
              :default-expanded-keys="defaultFrame"
              :expand-on-click-node="false"
              :highlight-current="true"
              node-key="value"
              @node-click="handleClick"
            >
              <div slot-scope="{ node, data }" class="custom-tree-node">
                <div class="flex-box">
                  <span class="over-text">{{ node.label }}</span>

                  <div>
                    <!-- <el-popover
                      :ref="`pop-${data.value}`"
                      :offset="10"
                      placement="bottom-end"
                      trigger="click"
                      @hide="handleHide"
                      @after-enter="handleShow(data.value)"
                    >
                      <div class="right-item-list">
                        <div class="right-item" @click.stop="addDivsion(1)">
                          {{ $t('public.edit') }}
                        </div>
                        <div class="right-item" @click.stop="hanleDelete(data)">
                          {{ $t('public.delete') }}
                        </div>
                      </div>
                      <div slot="reference" v-if="type !== 2" class="iconfont icongengduo right-icon" />
                    </el-popover> -->
                  </div>
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
import { configFrameDeleteApi } from '@/api/setting'
export default {
  name: 'TreeVue',
  components: {},
  props: {
    parentTree: {
      type: Array,
      default: () => {
        return []
      }
    },
    type: {
      type: Number | String,
      default: () => {
        return 0
      }
    }
  },
  data() {
    return {
      treeData: [],
      types: '',
      defaultFrame: [],
      activeValue: '' // 记录当前那个框展开
    }
  },
  watch: {
    parentTree: {
      handler(nVal, oVal) {
        this.$nextTick(() => {
          const value = nVal[0].value
          this.defaultFrame = [value]
          this.$refs.tree.setCurrentKey(value)
        })
      },
      deep: true
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
    handleClick(node, data) {
      if (this.activeValue) {
        this.$refs[`pop-${this.activeValue}`].doClose()
      }
      this.types = node.value
      this.$emit('getTypesId', node.value)
    },

    addType() {
      this.$emit('addType')
    },
    // 删除部门
    async hanleDelete(data) {
      await this.$modalSure('确定删除改部门吗')
      await configFrameDeleteApi(data.value)
      this.$bus.$emit('getList')
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
.title {
  padding: 20px;
  font-size: 14px;
  display: flex;
  justify-content: space-between;
  .el-icon-plus {
    color: #1890ff;
    cursor: pointer;
  }
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
  margin: -20px 0 -20px -20px;
  border-right: 1px solid #f5f5f5;
}
</style>
