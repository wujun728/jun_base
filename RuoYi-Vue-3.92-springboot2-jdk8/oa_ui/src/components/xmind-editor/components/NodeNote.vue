<template>
  <el-dialog class="nodeNoteDialog" title="备注" :visible.sync="dialogVisible" :width="isMobile ? '90%' : '50%'"
    :top="isMobile ? '20px' : '15vh'">
    <div class="noteEditor" ref="noteEditor" @keyup.stop @keydown.stop></div>
    <!-- <div class="tip">换行请使用：Enter+Shift</div> -->
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import Editor from '@toast-ui/editor'
import '@toast-ui/editor/dist/toastui-editor.css'
import { isMobile } from 'simple-mind-map/src/utils/index'
import { END_TEXT_EDIT, NODE_ACTIVE, SHOW_NODE_NOTE, START_TEXT_EDIT } from '../event-constant';

export default {
  name: 'NodeNote',
  data() {
    return {
      dialogVisible: false,
      note: '',
      activeNodes: [],
      editor: null,
      isMobile: isMobile()
    }
  },
  watch: {
    dialogVisible(val, oldVal) {
      if (!val && oldVal) {
        this.$bus.$emit(END_TEXT_EDIT)
      }
    }
  },
  created() {
    this.$bus.$on(NODE_ACTIVE, this.handleNodeActive)
    this.$bus.$on(SHOW_NODE_NOTE, this.handleShowNodeNote)
  },
  beforeDestroy() {
    this.$bus.$off(NODE_ACTIVE, this.handleNodeActive)
    this.$bus.$off(SHOW_NODE_NOTE, this.handleShowNodeNote)
  },
  methods: {
    handleNodeActive(...args) {
      this.activeNodes = [...args[1]]
      if (this.activeNodes.length > 0) {
        let firstNode = this.activeNodes[0]
        this.note = firstNode.getData('note') || ''
      } else {
        this.note = ''
      }
    },

    handleShowNodeNote() {
      this.$bus.$emit(START_TEXT_EDIT)
      this.dialogVisible = true
      this.$nextTick(() => {
        this.initEditor()
      })
    },

    initEditor() {
      if (!this.editor) {
        this.editor = new Editor({
          el: this.$refs.noteEditor,
          height: '500px',
          initialEditType: 'markdown',
          previewStyle: 'vertical'
        })
      }
      this.editor.setMarkdown(this.note)
    },

    cancel() {
      this.dialogVisible = false
    },

    confirm() {
      this.note = this.editor.getMarkdown()
      this.activeNodes.forEach(node => {
        node.setNote(this.note)
      })
      this.cancel()
    }
  }
}
</script>

<style lang="scss" scoped>
.nodeNoteDialog {
  .tip {
    margin-top: 5px;
    color: #dcdfe6;
  }
}
</style>
