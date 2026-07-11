<template>
  <div class="mybatis-editor">
    <div class="editor-header">
      <span class="editor-tip">支持 MyBatis 动态SQL语法, 参数使用 #{paramName} 格式</span>
    </div>
    <div class="editor-container">
      <div class="editor-line-numbers" ref="lineNumbers"></div>
      <div class="editor-area">
        <pre class="editor-highlight" v-html="highlightedCode"></pre>
        <textarea
          ref="textarea"
          class="editor-textarea"
          v-model="sqlText"
          @scroll="syncScroll"
          @keydown.tab.prevent="insertTab"
          @input="updateHighlight"
          placeholder="请输入SQL语句..."
          spellcheck="false"
        ></textarea>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MybatisInput",
  props: {
    value: { type: String, default: '' }
  },
  data() {
    return {
      sqlText: this.value,
      highlightedCode: ''
    }
  },
  watch: {
    value(val) {
      this.sqlText = val
      this.updateHighlight()
    },
    sqlText(val) {
      this.$emit('input', val)
    }
  },
  mounted() {
    this.updateHighlight()
    this.updateLineNumbers()
    this.$refs.textarea.addEventListener('scroll', this.syncScroll)
  },
  beforeDestroy() {
    this.$refs.textarea.removeEventListener('scroll', this.syncScroll)
  },
  methods: {
    updateHighlight() {
      let html = this.escapeHtml(this.sqlText)
      // SQL关键字
      html = html.replace(/\b(SELECT|FROM|WHERE|AND|OR|INSERT|INTO|VALUES|UPDATE|SET|DELETE|JOIN|LEFT|RIGHT|INNER|OUTER|ON|AS|ORDER|BY|GROUP|HAVING|LIMIT|OFFSET|UNION|ALL|DISTINCT|CASE|WHEN|THEN|ELSE|END|IS|NOT|NULL|IN|LIKE|BETWEEN|EXISTS|COUNT|SUM|AVG|MAX|MIN|ASC|DESC|CREATE|ALTER|DROP|TABLE|INDEX|PRIMARY|KEY|FOREIGN|REFERENCES|DEFAULT|VARCHAR|INT|BIGINT|DECIMAL|TEXT|DATE|DATETIME|TIMESTAMP|CHAR|AUTO_INCREMENT|COMMENT)\b/gi,
        '<span class="sql-keyword">$1</span>')
      // MyBatis标签
      html = html.replace(/(&lt;\/?)(select|insert|update|delete|sql|if|foreach|where|set|trim|choose|when|otherwise|include|bind|resultMap)([\s&gt;])/gi,
        '$1<span class="mybatis-tag">$2</span>$3')
      // 参数占位符 #{xxx} 和 ${xxx}
      html = html.replace(/([#$]\{[^}]+\})/g, '<span class="sql-param">$1</span>')
      // SQL函数
      html = html.replace(/\b(count|sum|avg|max|min|date_format|now|concat|substring|ifnull|coalesce|group_concat)\(/gi,
        '<span class="sql-function">$1</span>(')
      // 字符串
      html = html.replace(/('(?:[^'\\]|\\.)*')/g, '<span class="sql-string">$1</span>')

      this.highlightedCode = html
      this.$nextTick(() => this.updateLineNumbers())
    },
    updateLineNumbers() {
      const lines = (this.sqlText || '').split('\n').length
      let numbers = ''
      for (let i = 1; i <= Math.max(lines, 1); i++) {
        numbers += i + '\n'
      }
      this.$refs.lineNumbers.innerText = numbers
    },
    syncScroll() {
      const pre = this.$refs.textarea.previousElementSibling
      pre.scrollTop = this.$refs.textarea.scrollTop
      pre.scrollLeft = this.$refs.textarea.scrollLeft
      this.$refs.lineNumbers.scrollTop = this.$refs.textarea.scrollTop
    },
    insertTab() {
      const ta = this.$refs.textarea
      const start = ta.selectionStart
      const end = ta.selectionEnd
      this.sqlText = this.sqlText.substring(0, start) + '    ' + this.sqlText.substring(end)
      this.$nextTick(() => {
        ta.selectionStart = ta.selectionEnd = start + 4
        this.updateHighlight()
      })
    },
    escapeHtml(text) {
      if (!text) return ''
      return text
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
    }
  }
}
</script>

<style scoped>
.mybatis-editor {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  width: 100%;
}
.editor-header {
  background: #f5f7fa;
  padding: 4px 10px;
  border-bottom: 1px solid #dcdfe6;
  font-size: 12px;
  color: #909399;
}
.editor-container {
  display: flex;
  min-height: 200px;
  max-height: 350px;
  overflow: hidden;
}
.editor-line-numbers {
  width: 36px;
  background: #f5f7fa;
  color: #909399;
  font-size: 13px;
  line-height: 20px;
  padding: 8px 4px;
  text-align: right;
  overflow: hidden;
  user-select: none;
  border-right: 1px solid #ebeef5;
  white-space: pre;
}
.editor-area {
  flex: 1;
  position: relative;
  overflow: hidden;
}
.editor-highlight {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: 0;
  padding: 8px;
  font-size: 13px;
  line-height: 20px;
  font-family: Consolas, Monaco, 'Courier New', monospace;
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow: hidden;
  pointer-events: none;
  color: transparent;
  z-index: 1;
}
.editor-textarea {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  padding: 8px;
  font-size: 13px;
  line-height: 20px;
  font-family: Consolas, Monaco, 'Courier New', monospace;
  color: #303133;
  background: transparent;
  border: none;
  outline: none;
  resize: none;
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow: auto;
  z-index: 2;
  -webkit-text-fill-color: #303133;
}
.editor-highlight >>> .sql-keyword { color: #409EFF; font-weight: bold; }
.editor-highlight >>> .mybatis-tag { color: #67C23A; font-weight: bold; }
.editor-highlight >>> .sql-param { color: #E6A23C; }
.editor-highlight >>> .sql-function { color: #E6A23C; }
.editor-highlight >>> .sql-string { color: #F56C6C; }
</style>
