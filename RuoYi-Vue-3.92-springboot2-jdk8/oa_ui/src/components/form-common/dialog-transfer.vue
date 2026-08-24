<!-- @FileDescription: 穿梭框弹窗页面 例：客户管理设置列表头部 -->
<template>
  <div class="delete-info-dialog">
    <el-dialog
      :title="dialogTitle"
      :visible.sync="tableItemDialogVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      width="700px"
      :append-to-body="true"
      :before-close="handleClose"
    >
      <div class="info-body">
        <el-transfer
          ref="transfer"
          v-model="visibleValue"
          :data="transferData"
          :titles="transferTitle"
          target-order="push"
          :props="transferProps"
          @left-check-change="leftCheckChange"
          @right-check-change="rightCheckChange"
        >
          <span slot-scope="{ option }" draggable="!option.disabled" @dragstart="drag($event, option)">
            {{ option.name }}
          </span>
        </el-transfer>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Sortable from 'sortablejs'
import { cloneDeep } from 'lodash' //避免修改父组件传过来的数据
export default {
  name: 'visible-item-table-dialog',
  props: {
    dialogTitle: {
      type: String,
      default: '显示字段设置'
    },
    transferProps: {
      type: Object,
      default: {
        key: 'key',
        label: 'label'
      }
    },
    transferTitle: {
      type: Array,
      default: () => ['隐藏字段', '显示字段']
    },
    // 穿梭框总数据
    transferDataList: {
      type: Array,
      default: () => []
    },
    // 右侧显示列，用户已经选好保存过的数据，从父组件传过来回显
    visibleTableItemList: {
      type: Array,
      default: () => []
    },
    // 默认显示列，如果用户还没设置过则取这个
    defaultTableItemList: {
      type: Array,
      default: () => []
    },
    // 控制弹框显隐
    tableItemDialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      visibleValue: [], //显示列表头数据
      transferData: [], // 穿梭框数据
      transferLeftCheckData: [], // 左侧选中数据
      transferRightCheckData: [], // 右侧选中数据
      draggingKey: '' // 当前拖拽项
    }
  },
  mounted() {
    this.transferData = this.transferDataList
    this.visibleValue =
      this.visibleTableItemList.length > 0 ? cloneDeep(this.visibleTableItemList) : cloneDeep(this.defaultTableItemList)
    // 此处加nextTick 为了保证顺利取到refs
    this.$nextTick(() => {
      const transfer = this.$refs.transfer.$el
      const leftPanel = transfer.getElementsByClassName('el-transfer-panel')[0]
        ? transfer.getElementsByClassName('el-transfer-panel')[0].getElementsByClassName('el-transfer-panel__body')[0]
        : []
      const rightPanel = transfer.getElementsByClassName('el-transfer-panel')[1]
        ? transfer.getElementsByClassName('el-transfer-panel')[1].getElementsByClassName('el-transfer-panel__body')[0]
        : []
      const rightEl = rightPanel.getElementsByClassName('el-transfer-panel__list')[0]
        ? rightPanel.getElementsByClassName('el-transfer-panel__list')[0]
        : []

      Sortable.create(rightEl, {
        onEnd: (evt) => {
          const { oldIndex, newIndex } = evt
          const temp = this.visibleValue[oldIndex]
          if (!temp || temp === 'undefined') {
            return
          } // 解决右边最后一项从右边拖左边，有undefined的问题
          // this.$set(this.visibleValue, oldIndex, this.visibleValue[newIndex]) //这种赋值方法会导致数据更新视图未更新，排序后顺序展示错乱，强制更新也无效,原博主是这么赋值的，仅供参考
          // this.$set(this.visibleValue, newIndex, temp)
          let _arr = this.visibleValue.splice(oldIndex, 1)
          this.visibleValue.splice(newIndex, 0, _arr[0])
        }
      })
      // 目前只让右侧支持拖拽顺序即可，左侧暂时注释
      const leftEl = leftPanel.getElementsByClassName('el-transfer-panel__list')[0]
      Sortable.create(leftEl, {
        onEnd: (evt) => {
          const { oldIndex, newIndex } = evt
          const temp = this.transferData[oldIndex]
          if (!temp || temp === 'undefined') {
            return
          } // 解决右边最后一项从左边拖右边，有undefined的问题
          let _arr = this.transferData.splice(oldIndex, 1)
          this.transferData.splice(newIndex, 0, _arr[0])
        }
      })

      // 关于左侧拖拽至右侧的功能，在本项目中暂时无法实现
      leftPanel.ondragover = (ev) => ev.preventDefault()
      leftPanel.ondrop = (ev) => {
        ev.preventDefault()
        // 往左拉
        const index = this.visibleValue.indexOf(this.draggingKey)
        if (index !== -1) {
          // 如果当前拉取的是选中数据就将所有选中的数据拉到左边选中框内
          if (this.transferRightCheckData.indexOf(this.draggingKey) !== -1) {
            // 此处为多选执行
            this.transferRightCheckData.reduce((arr, item) => {
              if (arr.indexOf(item) !== -1) {
                // 每次计算将相同的删掉
                arr.splice(arr.indexOf(item), 1)
              }
              return arr
            }, this.visibleValue)
            this.transferRightCheckData = [] // 清除右侧选中的 不然下次向左拉取时会有缓存
            // 否则就只拉取当前一个
          } else {
            this.visibleValue.splice(index, 1)
          }
        }
      }
      rightPanel.ondragover = (ev) => ev.preventDefault()
      rightPanel.ondrop = (ev) => {
        ev.preventDefault()
        if (!this.draggingKey || this.draggingKey === 'undefined') {
          return
        } // 解决右边最后一项从左边拖右边，有undefined的问题
        // 右边框里没有当前key值的时候 向右拉
        if (this.visibleValue.indexOf(this.draggingKey) === -1) {
          // 此处为多选执行
          // 如果当前拉取的是选中数据就将所有选中的数据拉到右边选中框内
          if (this.transferLeftCheckData.indexOf(this.draggingKey) !== -1) {
            this.visibleValue = this.visibleValue.concat(this.transferLeftCheckData)
            this.transferLeftCheckData = [] // 清除左侧选中的  不然下次向右拉取时会有缓存
          } else {
            // 否则就只拉取当前一个
            this.visibleValue.push(this.draggingKey)
          }
        }
      }
    })
  },
  watch: {
    visibleTableItemList: {
      immediate: true, // 立即执行一次
      handler(newVal) {
        this.visibleValue = cloneDeep(newVal)
      }
    }
  },

  methods: {
    //关闭弹框
    handleClose() {
      this.$emit('handleCloseTableItem')
    },
    //点击确定按钮
    handleConfirm() {
      if (this.visibleValue.length <= 0) {
        this.$message({
          message: '未选中任何需要显示的数据',
          type: 'warning'
        })
        return
      }
      this.$emit('handleConfirmVisible', this.visibleValue)
    },
    drag(ev, option) {
      // 赋值当前拖拽的唯一标识
      this.draggingKey = option[this.transferProps.key]
    },
    leftCheckChange(val) {
      // 穿梭框左侧多选选中
      this.transferLeftCheckData = [...val]
    },
    rightCheckChange(val) {
      // 穿梭框右侧多选选中
      this.transferRightCheckData = [...val]
    }
  }
}
</script>

<style lang="scss" scoped>
.delete-info-dialog {
  ::v-deep .el-dialog__body {
    max-height: 500px;
    overflow-y: auto;
  }
  .info-body {
    color: #909399;
    padding-top: 20px;
    padding-bottom: 20px;
    .success-info {
      height: 20px;
      line-height: 20px;
    }
    .info-list {
      .info {
        padding: 0 10px;
        line-height: 20px;
        word-break: break-all;
      }
    }
  }
}

::v-deep.el-transfer {
  display: flex;
  align-items: center;

  .el-transfer-panel {
    width: 40%; // 设置左右穿梭面板的宽度
    height: 400px;
  }

  // 调整穿梭框中列表项的样式
  .el-transfer-panel__list {
    height: 350px;
  }

  .el-transfer__buttons {
    width: 100px !important;
  }
  .el-transfer__button:nth-child(2) {
    margin-left: 0;
    margin-bottom: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .el-transfer__button:first-child {
    display: flex;
    justify-content: center;
    align-items: center;
  }
}

::v-deep.el-transfer__buttons {
  width: 100px;
  display: flex;
  flex-direction: column-reverse; // 使按钮垂直反向排列
}
.el-transfer-panel__list {
  height: auto;
}
</style>
