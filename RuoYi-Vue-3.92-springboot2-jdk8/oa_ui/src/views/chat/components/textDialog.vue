<template>
  <div class="textDialog">
    <el-dialog
      :title="title"
      :visible.sync="show"
      width="650px"
      :close-on-click-modal="false"
      :before-close="handleClose"
      top="10vh"
    >
      <div>
        <el-input
          type="textarea"
          class="textareaBox"
          :class="type == 'tooltip_text' ? 'height540' : 'height307'"
          placeholder="请输入内容"
          v-model="value"
          :maxlength="maxlength"
          show-word-limit
        >
        </el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose" size="small">取消</el-button>
        <el-button type="primary" @click="submitFn" size="small">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  props: {},
  data() {
    return {
      show: false,
      maxlength: 200,
      value: '',
      title: '',
      type: ''
    }
  },

  mounted() {},
  methods: {
    openBox(obj) {
      if (obj.type === 'tooltip_text') {
        this.title = '提示词'
      } else if (obj.type === 'prologue_text') {
        this.title = '开场白'
      } else if (obj.type === 'data_arrange_text') {
        this.title = '整理数据规则'
      }
      this.type = obj.type
      this.maxlength = obj.max
      this.value = obj.text
      this.show = true
    },
    submitFn() {
      this.$emit('submit', this.value, this.type)
      this.handleClose()
    },
    handleClose() {
      this.show = false
      this.value = ''
    }
  }
}
</script>
<style scoped lang="scss">
.textareaBox {
  position: relative;
  ::v-deep .el-textarea__inner {
    resize: none;
    background: #f2f5f9;
    border: none;
    .el-input__count {
      background: #f2f5f9;
    }
  }
}
.height540 {
  ::v-deep .el-textarea__inner {
    height: 540px;
  }
}
.height307 {
  ::v-deep .el-textarea__inner {
    height: 307px;
  }
}
.textDialog {
  ::v-deep .el-dialog__header {
    border: none;
  }
  ::v-deep.el-textarea .el-input__count {
    background: #f2f5f9;
  }
  ::v-deep .el-dialog__body {
    padding-top: 0;
  }
}
</style>
