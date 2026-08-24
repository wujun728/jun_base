<template>
  <div>
    <el-dialog
      :visible.sync="dialogVisible"
      title="移动到"
      width="480px"
      :append-to-body="true"
      :before-close="handleClose"
    >
      <div class="tree-box mt10">
        <el-scrollbar style="height: 100%">
          <el-tree
            ref="tree"
            :data="treeData"
            :props="defaultProps"
            :highlight-current="true"
            :default-expanded-keys="[1]"
            :expand-on-click-node="false"
            node-key="id"
            @node-click="handleClick"
          >
            <div slot-scope="{ node, data }" class="custom-tree-node">
              <span class="flex-box">
                <i v-if="data.icon" class="el-icon-s-home" :icon-class="data.icon" />
                <i v-else class="icon iconfont iconwenjianjia color-file" />
                {{ data.name }}
              </span>
            </div>
          </el-tree>
        </el-scrollbar>
      </div>
      <div slot="footer" class="dialog-footer text-right">
        <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" type="primary" :loading="saveLoading" @click="handleSave">{{
          $t('public.ok')
        }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { memorialCateListApi, memorialEditApi } from '@/api/user'

export default {
  name: 'MoveDialog',
  props: {
    moveData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      name: '1',
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      fileId: null,
      activeIndex: '1',
      formData: {
        type: null
      },
      saveLoading: false
    }
  },
  mounted() {},
  methods: {
    async menuList() {
      const result = await memorialCateListApi()
      this.treeData = result.data.tree
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleOpen() {
      this.dialogVisible = true
      this.menuList()
    },
    handleClick(node, data) {
      this.fileId = node.id
    },
    handlerEmit() {
      this.$emit('handlerMove')
    },
    handleSave() {
      if (!this.fileId) {
        this.$message.error('选择移动的文件夹')
      } else {
        const data = {
          title: this.moveData.data.title,
          content: this.moveData.data.content,
          pid: this.fileId
        }
        this.memorialEdit(this.moveData.data.id, data)
      }
    },
    // 编辑
    memorialEdit(id, data) {
      this.saveLoading = true
      memorialEditApi(id, data)
        .then((res) => {
          this.saveLoading = false
          this.handleClose()
          this.handlerEmit()
        })
        .catch((err) => {
          this.saveLoading = false
        })
    }
  }
}
</script>

<style scoped lang="scss">
::v-deep .el-dialog__body {
  padding: 0 !important;
}
.tab-move {
  ::v-deep .el-tabs__nav-wrap::after {
    height: 0;
  }
  ::v-deep .el-tabs__header {
    margin: 0;
  }
  ::v-deep .el-tabs__active-bar {
    height: 0;
  }
  ::v-deep .el-tabs__item {
    padding-bottom: 15px;
    height: 0;
    line-height: 0;
  }
}
::v-deep .el-tabs__item.is-active {
  color: #303133;
  font-weight: 600;
}
.tree-box {
  height: 340px;
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }

  .flex-box {
    display: flex;
    align-items: center;
    .iconfont {
      font-size: 14px;
      margin-right: 10px;
    }
  }

  ::v-deep .el-tree-node__content {
    height: 30px;
  }
}

.el-icon-s-home {
  color: #ffca28;
  font-size: 14px;
}
</style>
