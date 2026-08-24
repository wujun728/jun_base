<template>
  <div class="divBox">
    <el-card class="normal-page">
      <el-row>
        <el-col :span="4" class="tree-left">
          <div class="tree-box-content">
            <div class="text">协议类型</div>
            <el-tree
              ref="tree"
              class="blue-theme"
              :data="treeData"
              :show-checkbox="false"
              node-key="id"
              :default-expand-all="true"
              :highlight-current="true"
              @node-click="handleClick"
              :check-on-click-node="false"
              :current-node-key="currentNodeKey"
              icon-class="el-icon-arrow-right"
              :expand-on-click-node="false"
              :props="defaultProps"
            >
              <div slot-scope="{ node, data }" class="custom-tree-node">
                <span class="flex-box">{{ node.label }}</span>
              </div>
            </el-tree>
          </div>
        </el-col>
        <el-col :span="20" class="right">
          <div class="btn-box">
            <el-button type="primary" :loading="loadding" size="small" @click="saveCurrent">保存</el-button>
          </div>
          <el-row class="mt20">
            <ueditorFrom :border="true" :height="height" type="notepad" :content="content" ref="ueditorFrom" />
          </el-row>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { agreementListApi, agreementInfoApi, agreementUpdateApi } from '@/api/setting'
import ueditorFrom from '@/components/form-common/oa-wangeditor'

export default {
  name: 'agreement',
  components: {
    ueditorFrom
  },
  data() {
    return {
      treeData: null,
      defaultProps: {
        children: 'children',
        label: 'title',
        value: 'id'
      },
      height: 'calc(100vh - 200px)',
      activeValue: '', // 记录当前那个框展开
      currentNodeKey: 0,
      content: '',
      contents: '',
      id: null,
      loadding: false,
      currentData: {}
    }
  },
  mounted() {
    this.getRoleCreate()
  },
  methods: {
    getRoleCreate() {
      agreementListApi().then((res) => {
        this.treeData = res.data.list
        const nVal = this.treeData[0].id
        this.$nextTick(() => {
          this.$refs.tree.setCurrentKey(nVal)
        })
        this.getDocEdit(nVal)
      })
    },
    // tree节点点击
    handleClick(node, data) {
      this.$refs.ueditorFrom.clear()
      this.isShowEdit = ''
      this.getDocEdit(node.id)
    },

    ueditorEdit(e) {
      this.content = e
    },
    getDocEdit(id) {
      agreementInfoApi(id).then((res) => {
        let data = res.data
        this.currentData = data
        this.$nextTick(() => {
          setTimeout(() => {
            this.content = data.content
          }, 100)
        })
        this.id = data.id
      })
    },
    // 保存当前
    saveCurrent() {
      this.content = this.$refs.ueditorFrom.getValue()
      if (!this.content) {
        this.$message.error('内容为空')
      } else {
        this.loadding = true
        const content = this.content
        agreementUpdateApi(this.id, { content })
          .then((res) => {
            this.loadding = false
          })
          .catch((error) => {
            this.loadding = false
          })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-card__body {
  padding: 0;
}
.right {
  padding: 20px;
  padding-top: 15px;
  min-height: calc(100vh - 77px);
}
.tree-left {
  min-height: calc(100vh - 77px);
  border-right: 1px solid #eee;
  ::v-deep .el-card__body {
    padding: 0;
  }
  .tree-box-content {
    .text {
      padding-left: 24px;
      padding-top: 20px;
      padding-bottom: 16px;
      font-size: 14px;
      font-family: PingFang SC-Regular, PingFang SC;
      font-weight: 500;
      color: #303133;
    }
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
}
.header {
  display: flex;
  align-items: center;
  .title {
    font-size: 12px;
    color: #1890ff;
    height: 30px;
    line-height: 30px;
    background: rgba(24, 144, 255, 0.06);
  }
}
::v-deep .el-card__header {
  border: none;
  padding-bottom: 0;
  .info {
    color: #999999;
    cursor: default;
  }
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
  background: rgba(240, 250, 254, 0.6);
  border-color: #1890ff;
}
::v-deep .blue-theme .el-tree-node__content {
  height: 40px;
  padding-right: 15px;
  border-right: 2px solid transparent;
}
.custom-tree-node {
  position: relative;
  width: 100%;
  .flex-box {
    width: 90%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    display: inline-block;
  }
  .right-icon {
    display: none;
    position: absolute;
    right: 0;
    transform: rotate(-90deg);
  }
}
::v-deep .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .custom-tree-node,
.el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .right-icon {
  display: inline-block;
  color: #1890ff;
}

::v-deep .el-form-item__content {
  position: relative;
}
.keyword {
  position: absolute;
  top: 0;
  left: 10px;
  span {
    margin-right: 8px;
    &:last-of-type {
      margin-right: 0;
    }
  }
}
.edit-content {
  width: 100%;
}
.btn-box {
  display: flex;
  justify-content: flex-end;
}
</style>
