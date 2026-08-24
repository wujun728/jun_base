// 表单操作预览

<template>
  <div class="toolbar-container">
    <div class="left-toolbar">
      <el-button type="text" :disabled="undoDisabled" title="撤销" @click="undoHistory">
        <!-- <i class="el-icon-arrow-left"></i> -->
        <span class="iconfont iconicon_back"></span>
      </el-button>
      <el-button type="text" :disabled="redoDisabled" title="重做" @click="redoHistory">
        <!-- <i class="el-icon-arrow-right"></i> -->
        <span class="iconfont icona-icon_nextstep"></span>
      </el-button>
    </div>
    <div class="right-toolbar">
      <div class="right-toolbar-con">
        <el-button class="view-btn" @click="previewForm"> <i class="el-icon-view" />预览</el-button>
        <el-button class="clear-btn" @click="clearFormWidget"> <i class="el-icon-delete" />清空</el-button>
        <el-button type="primary" @click="exportJson">
          <i class="el-icon-collection"></i>
          保存</el-button
        >
        <template v-for="(idx, slotName) in $slots">
          <slot :name="slotName"></slot>
        </template>
      </div>
    </div>

    <el-dialog
      title="预览"
      :visible.sync="showPreviewDialogFlag"
      v-if="showPreviewDialogFlag"
      :show-close="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      center
      v-dialog-drag
      :destroy-on-close="true"
      :append-to-body="true"
      class="small-padding-dialog"
      width="75%"
      :fullscreen="false"
    >
      <div>
        <div
          class="form-render-wrapper"
          :class="[layoutType === 'H5' ? 'h5-layout' : layoutType === 'Pad' ? 'pad-layout' : '']"
        >
          <VFormRender
            ref="preForm"
            :form-json="formJson"
            :form-data="testFormData"
            :preview-state="true"
            :option-data="testOptionData"
            :global-dsv="designerDsv"
            @appendButtonClick="testOnAppendButtonClick"
            @buttonClick="testOnButtonClick"
            @formChange="handleFormChange"
          >
            <!--
            <div slot="testSlot">aaaa</div>
            -->
          </VFormRender>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="getFormData">获取数据</el-button>
        <el-button type="primary" @click="resetForm">重置表单</el-button>
        <el-button type="primary" @click="setFormDisabled">禁用编辑</el-button>
        <el-button type="primary" @click="setFormEnabled">恢复编辑</el-button>
        <el-button type="primary" @click="onlyReadMode">只读查看</el-button>
        <el-button type="" @click="showPreviewDialogFlag = false">关闭</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="导出JSON"
      :visible.sync="showExportJsonDialogFlag"
      v-if="showExportJsonDialogFlag"
      :show-close="true"
      class="small-padding-dialog"
      center
      append-to-body
      v-dialog-drag
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :destroy-on-close="true"
    >
      <code-editor :mode="'json'" :readonly="true" v-model="jsonContent"></code-editor>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" class="copy-json-btn" :data-clipboard-text="jsonRawContent" @click="copyFormJson">
          复制JSON</el-button
        >
        <!-- <el-button @click="saveFormJson">保存为文件</el-button> -->
        <el-button type="" @click="showExportJsonDialogFlag = false"> 关闭</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="表单数据"
      :visible.sync="showFormDataDialogFlag"
      v-if="showFormDataDialogFlag"
      :show-close="true"
      class="dialog-title-light-bg"
      center
      v-dialog-drag
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :destroy-on-close="true"
      :append-to-body="true"
    >
      <div style="border: 1px solid #dcdfe6">
        <code-editor :mode="'json'" :readonly="true" v-model="formCopyJson"></code-editor>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          class="copy-form-data-json-btn"
          :data-clipboard-text="formDataRawJson"
          @click="copyFormDataJson"
        >
          复制JSON</el-button
        >
        <!-- <el-button @click="saveFormData">保存为文件</el-button> -->
        <el-button type="" @click="showFormDataDialogFlag = false"> 关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import VFormRender from '@/components/form-render/index'
import Clipboard from 'clipboard'
import {
  deepClone,
  copyToClipboard,
  generateId,
  getQueryParam,
  traverseAllWidgets,
  addWindowResizeHandler
} from '@/utils/util'
import i18n from '@/utils/i18n'
// import { saveAs } from 'file-saver'
// import SvgIcon from '@/components/svg-icon'
import CodeEditor from '@/components/code-editor/index'
import { formCrudSave, formInfo } from '@/api/form'
export default {
  name: 'ToolbarPanel',
  mixins: [i18n],
  components: {
    VFormRender,
    Clipboard,
    // SvgIcon,
    CodeEditor
  },
  props: {
    designer: Object,
    globalDsv: {
      type: Object,
      default: () => ({})
    }
  },
  inject: ['getDesignerConfig'],
  data() {
    return {
      designerConfig: this.getDesignerConfig(),

      toolbarWidth: 220,
      showPreviewDialogFlag: false,
      showImportJsonDialogFlag: false,
      showExportJsonDialogFlag: false,
      showExportCodeDialogFlag: false,
      showFormDataDialogFlag: false,
      showExportSFCDialogFlag: false,
      showNodeTreeDrawerFlag: false,

      nodeTreeData: [],

      testFunc: '',
      importTemplate: {
        formConfig: {},
        widgetList: []
      },
      jsonContent: '',
      jsonRawContent: '',

      formDataJson: '',
      formDataRawJson: '',

      vueCode: '',
      htmlCode: '',

      sfcCode: '',
      sfcCodeV3: '',

      activeCodeTab: 'vue',
      activeSFCTab: 'vue2',

      testFormData: {
        // 'userName': '666888',
        // 'productItems': [
        //   {'pName': 'iPhone12', 'pNum': 10},
        //   {'pName': 'P50', 'pNum': 16},
        // ]

        select62173: 2
      },
      testOptionData: {
        select62173: [
          { label: '01', value: 1 },
          { label: '22', value: 2 },
          { label: '333', value: 3 }
        ],

        select001: [
          { label: '辣椒', value: 1 },
          { label: '菠萝', value: 2 },
          { label: '丑橘子', value: 3 }
        ]
      },
      id: 0,
      formCopyJson: ''
    }
  },
  computed: {
    formJson() {
      return {
        // widgetList: this.designer.widgetList,
        // formConfig: this.designer.formConfig

        widgetList: deepClone(this.designer.widgetList),
        formConfig: deepClone(this.designer.formConfig)
      }
    },

    undoDisabled() {
      return !this.designer.undoEnabled()
    },

    redoDisabled() {
      return !this.designer.redoEnabled()
    },

    layoutType() {
      return this.designer.getLayoutType()
    },

    designerDsv() {
      return this.globalDsv
    }
  },
  watch: {
    'designer.widgetList': {
      deep: true,
      handler(val) {
        //console.log('test-----', val)
        //this.refreshNodeTree()
      }
    }
  },
  created() {
    this.id = this.$route.query.id
    this.designer.initHistoryData()
    this.formInfo()
  },
  mounted() {
    let maxTBWidth = this.designerConfig.toolbarMaxWidth || 420
    let minTBWidth = this.designerConfig.toolbarMinWidth || 300
    let newTBWidth = window.innerWidth - 260 - 300 - 320 - 80
    this.toolbarWidth = newTBWidth >= maxTBWidth ? maxTBWidth : newTBWidth <= minTBWidth ? minTBWidth : newTBWidth
    addWindowResizeHandler(() => {
      this.$nextTick(() => {
        let newTBWidth2 = window.innerWidth - 260 - 300 - 320 - 80
        this.toolbarWidth =
          newTBWidth2 >= maxTBWidth ? maxTBWidth : newTBWidth2 <= minTBWidth ? minTBWidth : newTBWidth2
      })
    })
  },
  methods: {
    onlyReadMode() {
      this.$refs.preForm.setReadMode(!this.$refs.preForm.$data.readModeFlag)
    },
    formInfo() {
      formInfo(this.id).then((res) => {
        if (res.data.global_options) {
          this.importTemplate.formConfig = res.data.global_options

          this.designer.selectNames = res.data.fields
        }
        this.importTemplate.widgetList = res.data.options || []
        this.doJsonImport()
      })
    },
    showToolButton(configName) {
      if (this.designerConfig[configName] === undefined) {
        return true
      }

      return !!this.designerConfig[configName]
    },
    doJsonImport() {
      try {
        let importObj = this.importTemplate
        this.designer.loadFormJson(importObj)
        this.designer.emitHistoryChange()
        this.designer.emitEvent('form-json-imported', [])
      } catch (ex) {
        this.$message.error(ex)
      }
    },
    buildTreeNodeOfWidget(widget, treeNode) {
      let curNode = {
        id: widget.id,
        label: widget.options.label || widget.type
        //selectable: true,
      }
      treeNode.push(curNode)

      if (widget.category === undefined) {
        return
      }

      curNode.children = []
      if (widget.type === 'grid') {
        widget.cols.map((col) => {
          let colNode = {
            id: col.id,
            label: col.options.name || widget.type,
            children: []
          }
          curNode.children.push(colNode)
          col.widgetList.map((wChild) => {
            this.buildTreeNodeOfWidget(wChild, colNode.children)
          })
        })
      } else if (widget.type === 'table') {
        //TODO: 需要考虑合并单元格！！
        widget.rows.map((row) => {
          let rowNode = {
            id: row.id,
            label: 'table-row',
            selectable: false,
            children: []
          }
          curNode.children.push(rowNode)

          row.cols.map((cell) => {
            if (!!cell.merged) {
              //跳过合并单元格！！
              return
            }

            let rowChildren = rowNode.children
            let cellNode = {
              id: cell.id,
              label: 'table-cell',
              children: []
            }
            rowChildren.push(cellNode)

            cell.widgetList.map((wChild) => {
              this.buildTreeNodeOfWidget(wChild, cellNode.children)
            })
          })
        })
      } else if (widget.type === 'tab') {
        widget.tabs.map((tab) => {
          let tabNode = {
            id: tab.id,
            label: tab.options.name || widget.type,
            selectable: false,
            children: []
          }
          curNode.children.push(tabNode)
          tab.widgetList.map((wChild) => {
            this.buildTreeNodeOfWidget(wChild, tabNode.children)
          })
        })
      } else if (widget.type === 'sub-form') {
        widget.widgetList.map((wChild) => {
          this.buildTreeNodeOfWidget(wChild, curNode.children)
        })
      } else if (widget.category === 'container') {
        //自定义容器
        widget.widgetList.map((wChild) => {
          this.buildTreeNodeOfWidget(wChild, curNode.children)
        })
      }
    },

    refreshNodeTree() {
      this.nodeTreeData.length = 0
      this.designer.widgetList.forEach((wItem) => {
        this.buildTreeNodeOfWidget(wItem, this.nodeTreeData)
      })
    },

    undoHistory() {
      this.designer.undoHistoryStep()
    },

    redoHistory() {
      this.designer.redoHistoryStep()
    },

    changeLayoutType(newType) {
      this.designer.changeLayoutType(newType)
    },

    clearFormWidget() {
      this.designer.clearDesigner()
      this.designer.clearWidgetRefList()
    },
    previewForm() {
      this.showPreviewDialogFlag = true
    },

    saveAsFile(fileContent, defaultFileName) {
      this.$prompt('文件名：', '保存为文件', {
        inputValue: defaultFileName,
        closeOnClickModal: false,
        inputPlaceholder: '请输入文件名'
      })
        .then(({ value }) => {
          if (!value) {
            value = defaultFileName
          }

          if (getQueryParam('vscode') == 1) {
            this.vsSaveFile(value, fileContent)
            return
          }

          const fileBlob = new Blob([fileContent], { type: 'text/plain;charset=utf-8' })
          saveAs(fileBlob, value)
        })
        .catch(() => {
          //
        })
    },

    vsSaveFile(fileName, fileContent) {
      const msgObj = {
        cmd: 'writeFile',
        data: {
          fileName,
          code: fileContent
        }
      }
      window.parent.postMessage(msgObj, '*')
    },

    importJson() {
      this.importTemplate = JSON.stringify(this.designer.getImportTemplate(), null, '  ')
      this.showImportJsonDialogFlag = true
    },
    exportJson() {
      let widgetList = deepClone(this.designer.widgetList)
      let formConfig = deepClone(this.designer.formConfig)
      this.jsonContent = JSON.stringify({ widgetList, formConfig }, null, '  ')
      this.jsonRawContent = JSON.stringify({ widgetList, formConfig })
      // this.showExportJsonDialogFlag = true
      // 请求数据

      let saveData = {
        options: this.designer.widgetList,
        fields: this.designer.selectNames,
        global_options: this.designer.formConfig
      }
      formCrudSave(this.id, saveData).then((res) => {})
    },

    copyFormJson(e) {
      copyToClipboard(this.jsonRawContent, e, this.$message, '复制JSON成功', '复制JSON失败')
    },

    saveFormJson() {
      this.saveAsFile(this.jsonContent, `vform${generateId()}.json`)
    },
    copyVueCode(e) {
      copyToClipboard(
        this.vueCode,
        e,
        this.$message,
        this.i18nt('designer.hint.copyVueCodeSuccess'),
        this.i18nt('designer.hint.copyVueCodeFail')
      )
    },

    copyHtmlCode(e) {
      copyToClipboard(
        this.htmlCode,
        e,
        this.$message,
        this.i18nt('designer.hint.copyHtmlCodeSuccess'),
        this.i18nt('designer.hint.copyHtmlCodeFail')
      )
    },

    saveVueCode() {
      this.saveAsFile(this.vueCode, `vform${generateId()}.vue`)
    },

    saveHtmlCode() {
      this.saveAsFile(this.htmlCode, `vform${generateId()}.html`)
    },

    copyV3SFC(e) {
      copyToClipboard(
        this.sfcCodeV3,
        e,
        this.$message,
        this.i18nt('designer.hint.copySFCSuccess'),
        this.i18nt('designer.hint.copySFCFail')
      )
    },

    saveV2SFC() {
      this.saveAsFile(this.sfcCode, `vformV2-${generateId()}.vue`)
    },

    saveV3SFC() {
      this.saveAsFile(this.sfcCodeV3, `vformV3-${generateId()}.vue`)
    },

    getFormData() {
      this.$refs['preForm']
        .getFormData(false)
        .then((formData) => {
          this.formDataJson = JSON.stringify(formData, null, '  ')
          this.formDataRawJson = JSON.stringify(formData)
          let formCopyJson = JSON.parse(JSON.stringify(formData))
          Object.keys(formCopyJson).forEach((key) => {
            if (key.includes('@')) {
              const newKey = key.replace('@', '.')
              formCopyJson[newKey] = formCopyJson[key]
              delete formCopyJson[key]
            }
          })
          this.formCopyJson = JSON.stringify(formCopyJson, null, '  ')
          this.showFormDataDialogFlag = true
        })
        .catch((error) => {
          this.$message.error(error)
        })
    },

    copyFormDataJson(e) {
      copyToClipboard(
        this.formDataRawJson,
        e,
        this.$message,
        this.i18nt('designer.hint.copyJsonSuccess'),
        this.i18nt('designer.hint.copyJsonFail')
      )
    },

    saveFormData() {
      this.saveAsFile(this.htmlCode, `formData${generateId()}.json`)
    },

    resetForm() {
      this.$refs['preForm'].resetForm()
    },

    setFormDisabled() {
      this.$refs['preForm'].disableForm()
    },

    setFormEnabled() {
      if (this.$refs.preForm.$data.readModeFlag) this.$refs.preForm.setReadMode(!this.$refs.preForm.$data.readModeFlag)
      this.$refs['preForm'].enableForm()
    },

    handleFormChange(fieldName, newValue, oldValue, formModel) {
      /*
        console.log('---formChange start---')
        console.log('fieldName', fieldName)
        console.log('newValue', newValue)
        console.log('oldValue', oldValue)
        console.log('formModel', formModel)
        console.log('---formChange end---')
        */
    },

    testOnAppendButtonClick(clickedWidget) {
      console.log('test', clickedWidget)
    },

    testOnButtonClick(button) {
      console.log('test', button)
    },

    findWidgetById(wId) {
      let foundW = null
      traverseAllWidgets(this.designer.widgetList, (w) => {
        if (w.id === wId) {
          foundW = w
        }
      })

      return foundW
    },

    onNodeTreeClick(nodeData, node, nodeEl) {
      //console.log('test', JSON.stringify(nodeData))

      if (nodeData.selectable !== undefined && !nodeData.selectable) {
        this.$message.info(this.i18nt('designer.hint.currentNodeCannotBeSelected'))
      } else {
        const selectedId = nodeData.id
        const foundW = this.findWidgetById(selectedId)
        if (!!foundW) {
          this.designer.setSelected(foundW)
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
div.toolbar-container {
  //min-width: 728px;  /* 解决工具按钮栏换行的问题 */
  /* 上一行css有问题，当窗口宽度不足时会把按钮挤出到右边的属性设置区，弃之！ */
}

.left-toolbar {
  float: left;
  font-size: 16px;
}

.right-toolbar {
  float: right;
  //width: 420px;
  text-align: right;
  overflow: hidden;

  .right-toolbar-con {
    text-align: left;
    width: max-content;
  }

  ::v-deep .el-button--text {
    font-size: 14px !important;
  }
}

.el-button i {
  margin-right: 3px;
}

.small-padding-dialog {
  ::v-deep .el-dialog__header {
    //padding-top: 3px;
    //padding-bottom: 3px;
    background: #f1f2f3;
  }

  ::v-deep .el-dialog__body {
    padding: 12px 15px 12px 15px;

    .el-alert.alert-padding {
      padding: 0 10px;
    }
  }

  ::v-deep .ace-container {
    border: 1px solid #dcdfe6;
  }
}

.dialog-title-light-bg {
  ::v-deep .el-dialog__header {
    background: #f1f2f3;
  }
}

.no-box-shadow {
  box-shadow: none;
}

.no-padding.el-tabs--border-card {
  ::v-deep .el-tabs__content {
    padding: 0;
  }
}

.form-render-wrapper {
  //height: calc(100vh - 142px);
  //all: revert !important; /* 防止表单继承el-dialog等外部样式，未生效，原因不明？？ */
  padding-top: 20px;
}

.form-render-wrapper.h5-layout {
  margin: 0 auto;
  width: 420px;
  border-radius: 15px;
  //border-width: 10px;
  box-shadow: 0 0 1px 10px #495060;
  height: calc(100vh - 142px);
  overflow-y: auto;
  overflow-x: hidden;
}

.form-render-wrapper.pad-layout {
  margin: 0 auto;
  width: 960px;
  border-radius: 15px;
  //border-width: 10px;
  box-shadow: 0 0 1px 10px #495060;
  height: calc(100vh - 142px);
  overflow-y: auto;
  overflow-x: hidden;
}

.node-tree-drawer ::v-deep {
  .el-drawer {
    padding: 10px;
    overflow: auto;
  }

  .el-drawer__header {
    margin-bottom: 12px;
    padding: 5px 5px 0;
  }

  .el-drawer__body {
    padding-left: 5px;
  }
}

/*.node-tree-scroll-bar {*/
/*  height: 100%;*/
/*  overflow: auto;*/
/*}*/

.node-tree ::v-deep {
  .el-tree > .el-tree-node:after {
    border-top: none;
  }
  .el-tree-node {
    position: relative;
    padding-left: 12px;
  }

  .el-tree-node__content {
    padding-left: 0 !important;
  }

  .el-tree-node__expand-icon.is-leaf {
    display: none;
  }
  .el-tree-node__children {
    padding-left: 12px;
    overflow: visible !important; /* 加入此行让el-tree宽度自动撑开，超出宽度el-draw自动出现水平滚动条！ */
  }

  .el-tree-node :last-child:before {
    height: 38px;
  }

  .el-tree > .el-tree-node:before {
    border-left: none;
  }

  .el-tree > .el-tree-node:after {
    border-top: none;
  }

  .el-tree-node:before {
    content: '';
    left: -4px;
    position: absolute;
    right: auto;
    border-width: 1px;
  }

  .el-tree-node:after {
    content: '';
    left: -4px;
    position: absolute;
    right: auto;
    border-width: 1px;
  }

  .el-tree-node:before {
    border-left: 1px dashed #4386c6;
    bottom: 0px;
    height: 100%;
    top: -10px;
    width: 1px;
  }

  .el-tree-node:after {
    border-top: 1px dashed #4386c6;
    height: 20px;
    top: 12px;
    width: 16px;
  }

  .el-tree-node.is-current > .el-tree-node__content {
    background: #c2d6ea !important;
  }

  .el-tree-node__expand-icon {
    margin-left: -3px;
    padding: 6px 6px 6px 0px;
    font-size: 16px;
  }
}
.view-btn.el-button {
  color: #1890ff;
  border-color: #1890ff;
}
.clear-btn.el-button {
  color: red;
  border-color: red;
}
.clear-btn.el-button:hover {
  background-color: #fef0ed;
}
</style>
