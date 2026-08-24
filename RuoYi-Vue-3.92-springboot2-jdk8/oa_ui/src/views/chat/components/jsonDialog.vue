<template>
  <div>
    <el-dialog
      title="选择数据库"
      :visible.sync="show"
      width="65%"
      :close-on-click-modal="false"
      :before-close="handleClose"
    >
      <div class="flex flex-between mb10">
        <span class="tips"
          >数据库表
          <popover
            :tips="`请在数据库表内容下补充多张表的关联关系，以及每张表的介绍用途；支持使用#帮助智能体理解内容权重`"
            style="display: inline-block"
          ></popover
        ></span>
        <span class="add-text" @click="addTable"><span class="el-icon-plus"></span> 添加</span>
      </div>

      <mavon-editor
        :toolbars="markdownOption"
        :subfield="false"
        :editable="true"
        :ishljs="true"
        :codeStyle="type"
        :boxShadow="false"
        v-model="markdownText"
      />

      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose" size="small">取消</el-button>
        <el-button type="primary" @click="submitFn" size="small">确定</el-button>
      </span>
    </el-dialog>
    <databaseTable ref="databaseTable" :list="list" @submit="getSelectVal"></databaseTable>
  </div>
</template>
<script>
import popover from './popover'
import databaseTable from './databaseTable'
import { mavonEditor } from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import { getApplicationstoolTipApi } from '@/api/chatAi'
import Table from '@/views/hr/archives/components/table.vue'

export default {
  name: '',
  components: { mavonEditor, popover, databaseTable },
  props: {
    content: {
      type: String,
      default: ''
    },
    list: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      show: false,
      type: 'dark',
      markdownText: '',
      selectList: [],
      markdownOption: {}
    }
  },

  methods: {
    openBox(val, list) {
      this.markdownText = val
      this.show = true
    },
    getSelectVal(data) {
      if (data.length == 0) {
        return false
      }

      let str = []
      this.selectList = []
      data.map((item) => {
        str.push(item.table)
      })
      getApplicationstoolTipApi({ tables: str }).then((res) => {
        this.markdownText = res.data.tooltip_text
      })
    },
    addTable() {
      this.$refs.databaseTable.openBox()
    },
    submitFn() {
      this.$emit('submit', this.selectList, this.markdownText)
      this.handleClose()
    },
    handleClose() {
      this.show = false
      this.selectList = []
      this.markdownText = ''
    }
  }
}
</script>
<style scoped lang="scss">
.add-text {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #1890ff;
  cursor: pointer;
  .el-icon-plus {
    font-size: 12px;
  }
}
.editor-box {
  background: #1b1d20;
}
.tips {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #303133;
}
::v-deep.v-note-op {
  display: none !important;
}
::v-deep .content-input-wrapper {
  max-height: 640px !important;
  min-height: 300px !important;
  overflow-y: auto;
  background-color: #131418 !important;
  border-radius: 8px 8px 8px 8px !important;
  color: #ffffff !important;
}
::v-deep .auto-textarea-input {
  // overflow: visible !important;

  background-color: #131418 !important;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #ffffff !important;
}
</style>
