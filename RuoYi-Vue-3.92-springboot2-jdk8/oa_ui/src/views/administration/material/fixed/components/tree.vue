<template>
  <div class="left">
    <div class="el-card__header clearfix">
      <el-row class="title">
        <el-col :span="12" class="material"> 物资分类 </el-col>
        <el-col :span="12" class="text-right">
          <el-tooltip effect="dark" content="添加物资分类" placement="top">
            <span class="iconfont icontianjia pointer" @click="addDivsion()"></span>
          </el-tooltip>
        </el-col>
      </el-row>
    </div>
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
            <div slot-scope="{ node, data }" class="custom-tree-node">
              <div class="flex-box">
                <span class="over-text">{{ node.label }}</span>
                <el-popover
                  :ref="`pop-${data.value}`"
                  placement="bottom-end"
                  trigger="click"
                  :offset="10"
                  @after-enter="handleShow(data.value)"
                  @hide="handleHide"
                >
                  <div class="right-item-list">
                    <div class="right-item" @click.stop="addDivsion(data)">添加子类</div>
                    <div class="right-item" v-if="data.value !== topId" @click.stop="editDivsion(data.value)">
                      {{ $t('public.edit') }}
                    </div>
                    <div v-if="data.value !== topId" class="right-item" @click.stop="handleDelete(data)">
                      {{ $t('public.delete') }}
                    </div>
                  </div>
                  <div slot="reference" v-if="data.value > 0" class="iconfont icongengduo right-icon" />
                </el-popover>
              </div>
            </div>
          </el-tree>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<script>
import { storageCateCreateApi, storageCateDeleteApi, storageCateEditApi } from '@/api/administration'

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
      filterText: '',
      isShowEdit: '',
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      focusShow: true,
      frame_id: 0,
      topId: 0, // 顶级id
      activeValue: '' // 记录当前那个框展开
    }
  },
  watch: {
    parentTree: {
      handler(nVal) {
        this.$nextTick(() => {
          if (!this.isShowEdit && nVal.length) {
            const value = this.frame_id
            this.$refs.tree.setCurrentKey(nVal[0].value)
          }
          this.topId = nVal.length ? nVal[0].value : ''
        })
      },
      deep: true,
      immediate: true
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
        if (this.activeValue !== '') {
          this.$refs[`pop-${this.activeValue}`].doClose()
        }
        this.frame_id = node.value
        this.isShowEdit = ''
        this.$emit('frameId', node.value)
      }
    },

    // tree搜索过滤
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    // 删除部门
    async handleDelete(data) {
      await this.$modalSure('你确定要删除该分类吗')
      await storageCateDeleteApi(data.value)

      await this.$emit('getList')
      this.$emit('frameId', data.pid)
    },
    // 添加子类
    addDivsion(data) {
      console.log(data, '添加子类')
      let path = null
      if (data) {
        path = data.path
        if (path && path.length <= 0) {
          path.unshift(0)
          if (!path.includes(data.id)) {
            path.push(data.id) // 添加当前分类
          }
        } else if (path && path.length > 0) {
          if (!path.includes(data.id)) {
            path.push(data.id) // 添加当前分类
          }
        }
      } else {
        path = [0]
      }
      path = path.filter((i) => {
        return i != 0
      })

      this.$modalForm(storageCateCreateApi({ type: this.types, path: path })).then(({ message }) => {
        this.$emit('getList')
      })
    },
    // 编辑子类
    editDivsion(value) {
      this.$modalForm(storageCateEditApi(value, { type: this.types })).then(({ message }) => {
        this.$emit('getList')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.left {
  border-right: 1px solid #eeeeee;
  margin: -20px 0;
}
.icongengduo {
  margin-right: 15px;
}
.material {
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 14px;
  color: #303133;
}
.icontianjia {
  font-size: 14px;
  color: #1890ff;
}
.tree-icon {
  margin-right: 5px;
  font-size: 18px;
  color: #ffca28;
}
::v-deep .el-card__header {
  border-bottom: none;
  padding: 20px 16px 15px 0;
  font-weight: 600;
}
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background: rgba(24, 144, 255, 0.08);
  border-color: #1890ff;
  .flex-box {
    max-width: 100%;
    font-size: 14px;
    font-weight: 600;
    color: #1890ff;
  }
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
  font-weight: 600;
}

::v-deep.el-tree-node > .el-tree-node__children {
  overflow: inherit;
}
::v-deep.el-tree-node__content {
  // padding-left: 14px !important;
}
.custom-tree-node {
  position: relative;
  width: 80%;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 14px;
  color: #303133;
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
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
}
.title {
  display: flex;
  justify-content: center;
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
  font-weight: 600;
}
</style>
