<template>
  <div class="tree-box">
    <div class="v-height-flag">
      <div class="clearfix el-card__header">
        <el-input
          v-model="filterText"
          :placeholder="$t('public.searchdepartment')"
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
              :data="parentTree"
              :default-expanded-keys="defaultFrame"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              :highlight-current="true"
              :props="defaultProps"
              node-key="value"
              @node-click="handleClick"
            >
              <div slot-scope="{ node, data }" class="custom-tree-node">
                <div class="flex-box">
                  <i class="tree-icon iconfont iconwenjianjia" />
                  <span class="over-text">{{ node.label }}</span>
                  <span>（{{ data.user_count }}）</span>
                  <div>
                    <el-popover
                      :ref="`pop-${data.value}`"
                      :offset="10"
                      placement="bottom-end"
                      trigger="click"
                      @hide="handleHide"
                      @after-enter="handleShow(data.value)"
                    >
                      <div class="right-item-list">
                        <div class="right-item" @click.stop="addDivsion(0)">{{ $t('public.Adddepartment') }}</div>
                        <div v-if="data.pid !== 0" class="right-item" @click.stop="addDivsion(1)">
                          {{ $t('public.edit') }}
                        </div>
                        <div v-if="data.pid !== 0" class="right-item" @click.stop="hanleDelete(data)">
                          {{ $t('public.delete') }}
                        </div>
                      </div>
                      <div slot="reference" class="iconfont icongengduo right-icon" />
                    </el-popover>
                  </div>
                </div>
              </div>
            </el-tree>
          </div>
        </el-scrollbar>
      </div>
    </div>
    <rightRole
      ref="roleBox"
      :config="drawerConfig"
      :frame-id="frameId"
      :parent-id="parentId"
      :parent-tree="parentTree"
    />
  </div>
</template>

<script>
import { configFrameDeleteApi } from '@/api/setting'
export default {
  name: 'TreeVue',
  components: {
    rightRole: () => import('./rightRole')
  },
  props: {
    parentTree: {
      type: Array,
      default: () => {
        return []
      }
    },
    frameId: {
      type: Number | String,
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
      frame_id: '',
      personConfig: {},
      parentId: [],
      topId: '', // 顶级id
      defaultFrame: [],
      activeValue: '' // 记录当前那个框展开
    }
  },
  watch: {
    parentTree: {
      handler(nVal, oVal) {
        this.$nextTick(() => {
          if (!this.isShowEdit && nVal.length) {
            const value = nVal[0].value

            this.defaultFrame = [value]
            this.$refs.tree.setCurrentKey(this.frame_id ? this.frame_id : value)
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
    handleClick(node, data) {
      if (this.activeValue) {
        this.$refs[`pop-${this.activeValue}`].doClose()
      }
      this.frame_id = node.value
      this.isShowEdit = ''
      this.$emit('frameId', node.pid === 0 ? 0 : node.value)
    },
    // 显示tree编辑
    showTreeEdit(node, data) {
      this.isShowEdit = data.value
    },
    // tree搜索过滤
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    // 删除部门
    async hanleDelete(data) {
      await this.$modalSure('确定删除改部门吗')
      await configFrameDeleteApi(data.value)
      this.$bus.$emit('getList')
    },
    // 添加子部门
    addDivsion(type) {
      const node = this.$refs.tree.getCurrentNode()
      // node.path.push(node.value)
      if (node.pid === 0) {
        // 顶级判断
        node.path = []
      }
      this.parentId = [...new Set(node.path)]
      if (!type) {
        this.parentId.push(node.value)
      }
      this.drawerConfig.title = type
        ? this.$t('setting.group.editorialdepartment')
        : this.$t('setting.group.adddepartment')
      this.drawerConfig.type = type
      this.drawerConfig.id = node.value
      this.drawerConfig.disabled = !!(this.topId === node.value && type)
      this.$refs.roleBox.handelOpen()
      this.$refs.roleBox.getDepartmentHead()
    }
  }
}
</script>

<style lang="scss" scoped>
.tree-icon {
  margin-right: 5px;
  font-size: 15px;
  color: #ffca28;
}
// .el-card__header ::v-deep.el-input--small .el-input__inner {
//   cursor: pointer;
// }

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
  margin: -20px 0 -20px -20px;
}
</style>
