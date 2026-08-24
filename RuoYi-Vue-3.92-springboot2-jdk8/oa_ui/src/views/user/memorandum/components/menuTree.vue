<template>
  <div class="tree-box">
    <div class="tree-title">
      <el-dropdown placement="bottom" @command="handleCommand">
        <el-button type="primary" size="small" class="dropdownbutton"> <i class="el-icon-plus"></i> 新 建 </el-button>

        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="note" icon="el-icon-circle-plus">新建笔记</el-dropdown-item>
          <el-dropdown-item command="folder" icon="iconfont iconjishiben-xinjianwenjianjia1"
            >新建文件夹</el-dropdown-item
          >
        </el-dropdown-menu>
      </el-dropdown>
      <div class="recently" :class="recentlyShow ? 'activeColor' : ''" @click="onRecently">
        <span class="iconfont iconshijian"></span><span>最近使用</span>
      </div>
    </div>
    <div class="" v-height>
      <el-scrollbar style="height: 100%">
        <div class="main-box" tabindex="1" :class="focusShow ? 'focus' : ''" @click="handleClick(1)">
          <span class="iconfont iconwenjianjia1"></span> <span>我的文件夹</span>
        </div>
        <el-tree
          ref="tree"
          :data="treeData"
          :props="defaultProps"
          :highlight-current="true"
          :default-expand-all="true"
          :expand-on-click-node="false"
          current-node-key="id"
          node-key="id"
          :indent="10"
          @node-click="handleClick"
        >
          <div slot-scope="{ node, data }" class="custom-tree">
            <div class="custom-tree-node">
              <el-popover v-if="data.name.length > 11" :content="data.name" placement="top" trigger="hover">
                <div class="flex-box" slot="reference">
                  <i class="tree-icon icon iconfont iconwenjianjia color-file" />
                  <span class="over-text flex-box-title">{{ data.name }}</span>
                  <span class="flex-box-title">({{ data.count }})</span>
                </div>
              </el-popover>
              <div class="flex-box" v-else>
                <i class="tree-icon icon iconfont iconwenjianjia color-file" />
                <span class="over-text flex-box-title">{{ data.name }}</span>
                <span class="flex-box-title">({{ data.count }})</span>
              </div>
            </div>
            <el-popover
              :ref="`pop-${data.id}`"
              placement="bottom-end"
              trigger="click"
              :offset="10"
              @after-enter="handleShow(data.id)"
              @hide="handleHide"
            >
              <div class="right-item-list">
                <div class="right-item" @click.stop="addCate(1, data)">{{ $t('calendar.addtype') }}</div>
                <div v-if="data.id != ''" class="right-item" @click.stop="addEdit(data)">{{ $t('public.edit') }}</div>
                <div v-if="data.id != ''" class="right-item" @click.stop="handleDelete(data)">
                  {{ $t('public.delete') }}
                </div>
              </div>

              <div slot="reference" class="icon iconfont icongengduo right-icon ml10" />
            </el-popover>
          </div>
        </el-tree>
      </el-scrollbar>
    </div>
    <!-- 通用弹窗表单   -->
    <dialogForm ref="dialogForm" :roles-config="rolesConfig" :form-data="formBoxConfig" @isOk="menuList()" />
  </div>
</template>

<script>
import { memorialCateCreateApi, memorialCateDeleteApi, memorialCateEditApi, memorialCateListApi } from '@/api/user'
export default {
  name: 'MenuTree',
  components: {
    dialogForm: () => import('./dialog-form')
  },
  props: {},
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      focusShow: false,
      recentlyShow: true,
      treeData: [],
      activeValue: '',
      formBoxConfig: {},
      id: 0,
      nodeIndex: null,
      rolesConfig: []
    }
  },
  watch: {
    treeData: {
      handler(nVal, oVal) {
        this.$nextTick(() => {
          const value = this.id
          this.$refs.tree.setCurrentKey(value)
        })
      },
      deep: true
    }
  },
  mounted() {
    this.onRecently()
    this.menuList()
  },
  methods: {
    //新建按钮
    handleCommand(command) {
      switch (command) {
        case 'note':
          var data = {
            title: this.$t('calendar.placeholder03'),
            content: this.$t('calendar.placeholder04'),
            pid: this.id == '' ? 0 : this.id
          }

          if (this.recentlyShow) {
            this.$emit('createNote', data)
            this.recentlyShow = false
            this.focusShow = true
            this.handleClick(1)
          } else {
            this.$emit('createNote', data)
          }

          break
        case 'folder':
          this.addCate(2)
          break
      }
    },

    // 最近使用
    onRecently() {
      this.focusShow = false
      this.recentlyShow = true
      this.$refs.tree.setCurrentKey()
      this.$emit('onRecently')
    },
    // tree节点点击
    handleClick(node, data) {
      this.recentlyShow = false
      if (node == 1) {
        this.focusShow = true
        this.id = ''
        this.$refs.tree.setCurrentKey()
      } else {
        this.focusShow = false
        this.id = node.id
      }

      this.$emit('frameId', this.id)
    },
    // 编辑窗口显示
    handleShow(value) {
      this.activeValue = value
    },
    // 编辑窗口隐藏
    handleHide() {
      this.activeValue = ''
    },
    menuList() {
      memorialCateListApi().then((res) => {
        this.treeData = []
        if (res.data !== undefined) {
          this.treeData = res.data.tree
          if (this.treeData.length > 0 && this.id === 0) {
          }
        }
      })
    },
    // 添加分类
    addCate(type, data) {
      const id = type === 1 ? this.id : 0
      memorialCateCreateApi(id).then((res) => {
        this.formBoxConfig = {
          title: res.data.title,
          width: '500px',
          method: res.data.method,
          action: res.data.action.substr(4)
        }
        if (type === 1) {
          this.closePopover(data.id)
        }
        this.rolesConfig = res.data.rule
        this.$refs.dialogForm.open()
      })
    },
    // 修改分类
    addEdit(data) {
      memorialCateEditApi(data.id).then((res) => {
        this.formBoxConfig = {
          title: res.data.title,
          width: '500px',
          method: res.data.method,
          action: res.data.action.substr(4)
        }
        this.closePopover(data.id)
        this.rolesConfig = res.data.rule
        this.$refs.dialogForm.open()
      })
    },
    // 删除
    handleDelete(item) {
      this.$modalSure(this.$t('calendar.placeholder08')).then(() => {
        memorialCateDeleteApi(item.id).then((res) => {
          this.menuList()
        })
      })
    },
    closePopover(id) {
      this.$refs[`pop-${id}`].doClose()
    },
    openPopover(id) {
      this.$refs[`pop-${id}`].doShow()
    }
  }
}
</script>

<style lang="scss" scoped>
.el-dropdown {
  width: 160px;
  .dropdownbutton {
    font-size: 14px;
    font-weight: 500;
    width: 160px;
  }
}
.main-box {
  cursor: pointer;
  height: 40px;
  display: flex;
  align-items: center;
  padding-left: 10px;
  font-size: 14px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
  .iconwenjianjia1 {
    font-size: 13px;
    margin-right: 9px;
  }
}
.main-box:hover {
  background-color: #f5f7fa;
}

.focus {
  background: rgba(24, 144, 255, 0.08) !important;
  color: #1890ff;
}
.recently {
  margin-top: 20px;
  padding-left: 10px;
  text-align: left;
  cursor: pointer;
  height: 40px;
  font-size: 14px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
  display: flex;
  align-items: center;
  .iconshijian {
    font-size: 13px;
    margin-right: 9px;
  }
}
.recently:hover {
  background-color: #f5f7fa;
}
.activeColor {
  color: #1890ff;
  background: rgba(24, 144, 255, 0.08) !important;
}
.el-dropdown-menu__item {
  font-size: 14px;
  color: #1890ff;
  line-height: 40px;
  min-width: 170px;
}

.tree-icon {
  margin-right: 8px;
  font-size: 13px;
}
.iconjishiben-xinjianwenjianjia1 {
  display: inline-block;
  font-size: 17px !important;
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background: rgba(24, 144, 255, 0.08);
  border-color: #1890ff;
}
::v-deep.el-tree-node__content {
  position: relative;
  height: 40px;
  padding-right: 15px;
  border-right: 2px solid transparent;
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .custom-tree-node,
.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .right-icon {
  display: inline-block;
  color: #1890ff;
}

::v-deep.el-tree-node > .el-tree-node__children {
  overflow: inherit;
}
.custom-tree {
  width: 96%;
  .custom-tree-node {
    width: calc(100% - 20px);
  }
}
.right-icon {
  display: none;
  position: absolute;
  right: 4px;
}
.item-list {
  display: flex !important;
  align-items: center;
}
.tree-box {
  height: 100vh;
  background-color: #f8f9fa;
  ::v-deep .el-tree {
    background: transparent;
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .tree-title {
    font-size: 14px;
    padding-top: 18px;
    // margin-bottom: 12px;
    text-align: center;
  }
  .flex-box {
    display: flex;
    align-items: center;
    width: 100%;
    .flex-box-title {
      font-size: 14px;
    }
    span:first-of-type {
      display: inline-block;
      max-width: 66%;
    }
  }
}
</style>
