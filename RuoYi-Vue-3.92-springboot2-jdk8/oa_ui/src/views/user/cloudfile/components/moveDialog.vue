<template>
  <div>
    <!-- 移动弹窗 -->
    <el-dialog :visible.sync="dialogVisible" width="480px" :before-close="handleClose">
      <div slot="title">
        <el-tabs v-model="name" class="tab-move" @tab-click="handleTabClick">
          <!--<el-tab-pane :label="$t('file.myfiles')" name="1" />-->
          <el-tab-pane :label="$t('file.enterprisespace')" name="2" />
        </el-tabs>
      </div>
      <div class="tree-box">
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
        <el-button size="small" type="primary" @click="handleSave">{{ $t('public.ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  folderCopyApi,
  folderDirListApi,
  folderMoveAllApi,
  folderMoveApi,
  folderSpaceEntAllMoveApi,
  folderSpaceEntCopyApi,
  folderSpaceEntDirApi,
  folderSpaceEntMoveApi,
  folderSpaceListApi
} from '@/api/cloud'

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
      }
    }
  },
  methods: {
    menuList() {
      if (this.moveData.type <= 2 || this.moveData.type === 5) {
        this.name = '1'
        this.activeIndex = '1'
        this.getFolderDirList()
      } else {
        // 企业空间
        this.name = '2'
        this.activeIndex = '2'
        this.getFolderSpaceEntDir()
      }
    },
    // 我的空间
    getFolderDirList() {
      folderDirListApi().then((res) => {
        if (res.data !== undefined) {
          this.treeData = res.data
        }
      })
    },
    // 企业空间
    getFolderSpaceEntDir() {
      folderSpaceEntDirApi().then((res) => {
        this.treeData = res.data
      })
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
    handleTabClick(e) {
      if (this.activeIndex !== this.name) {
        if (this.name == 1) {
          this.getFolderDirList()
        } else {
          this.getFolderSpaceEntDir()
        }
      }
      this.activeIndex = e.name
    },

    handlerEmit() {
      this.$emit('handlerMove', this.formData)
    },
    handleSave() {
      if (this.fileId === null) {
        let str = ''
        if (this.moveData.type <= 4) {
          str = '选择移动的文件夹'
        } else {
          str = '选择复制的文件夹'
        }
        this.$message.error(str)
      } else if (this.fileId === this.id) {
        let str = ''
        if (this.moveData.type <= 4) {
          str = '移动的文件夹不能一样'
        } else {
          str = '复制的文件夹不能一样'
        }
        this.$message.error(str)
      } else {
        if (this.moveData.type === 1) {
          // 我的空间单个移动
          folderMoveApi(this.moveData.id, { to_id: this.fileId }).then((res) => {
            this.formData.type = 1
            this.handlerEmit()
            this.handleClose()
          })
        } else if (this.moveData.type === 2) {
          // 我的空间批量移动
          const data = {
            id: this.moveData.id,
            to_id: this.fileId
          }
          folderMoveAllApi(data).then((res) => {
            this.formData.type = 2
            this.handlerEmit()
            this.handleClose()
          })
        } else if (this.moveData.type === 3) {
          // 企业空间单个移动
          folderSpaceEntMoveApi(this.moveData.fid, this.moveData.id, { to_id: this.fileId }).then((res) => {
            this.formData.type = 3
            this.handlerEmit()
            this.handleClose()
          })
        } else if (this.moveData.type === 4) {
          // 企业空间批量修改
          const data = {
            id: this.moveData.id,
            to_id: this.fileId
          }
          folderSpaceEntAllMoveApi(this.moveData.fid, data).then((res) => {
            this.formData.type = 4
            this.handlerEmit()
            this.handleClose()
          })
        } else if (this.moveData.type === 5) {
          // 我的空间复制文件

          const data = {
            to_id: this.fileId,
            fid: this.moveData.fid
          }
          folderCopyApi(this.moveData.id, data).then((res) => {
            this.formData.type = 5
            this.handlerEmit()
            this.handleClose()
          })
        } else if (this.moveData.type === 6) {
          // 企业空间复制文件
          const data = {
            to_id: this.fileId,
            fid: this.moveData.fid
          }
          folderSpaceEntCopyApi(this.moveData.fid, this.moveData.id, data).then((res) => {
            this.formData.type = 6
            this.handlerEmit()
            this.handleClose()
          })
        }
      }
    }
  }
}
</script>

<style scoped lang="scss">
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
}
.iconfont {
  font-size: 13px;
}
.el-icon-s-home {
  color: #ffca28;
  font-size: 14px;
}
</style>
