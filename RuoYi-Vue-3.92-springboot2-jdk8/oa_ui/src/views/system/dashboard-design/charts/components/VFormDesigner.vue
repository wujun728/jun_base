<template>
  <div class="form-designer-container">
    <el-header class="toolbar-header">
      <slot></slot>
    </el-header>
    <el-container class="main full-height">
      <el-container>
        <el-aside class="side-panel" :class="!leftAsideVisible ? 'aside-hidden' : 'aside-visible'">
          <FieldPanel :designer="designer"></FieldPanel>
        </el-aside>
        <div class="left-aside-toggle-bar" :class="{ 'aside-hidden': !leftAsideVisible }" @click="toggleLeftAside">
          <i class="el-icon" :class="!leftAsideVisible ? 'el-icon-arrow-right' : 'el-icon-arrow-left'"></i>
        </div>
        <el-container class="center-layout-container">
          <el-main class="form-widget-main">
            <el-scrollbar class="container-scroll-bar">
              <VFormWidget
                :designer="designer"
                :form-config="designer.formConfig"
                :global-dsv="globalDsv"
                ref="formRef"
              ></VFormWidget>
            </el-scrollbar>
          </el-main>
        </el-container>

        <el-aside class="setting-panel">
          <SettingPanel
            v-show="rightAsideVisible"
            :designer="designer"
            :selected-widget="designer.selectedWidget"
            :form-config="designer.formConfig"
            :global-dsv="globalDsv"
          ></SettingPanel>
          <div class="right-aside-toggle-bar" :class="{ 'aside-hidden': !rightAsideVisible }" @click="toggleRightAside">
            <i class="el-icon" :class="!rightAsideVisible ? 'el-icon-arrow-left' : 'el-icon-arrow-right'"></i>
          </div>
        </el-aside>
      </el-container>
    </el-container>
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
      :fullscreen="true"
    >
      <div class="form-render-wrapper">
        <VFormRender
          ref="preForm"
          :form-json="designer"
          :preview-state="true"
          :option-data="testOptionData"
          :global-dsv="globalDsv"
        >
          <!--
            <div slot="testSlot">aaaa</div>
            -->
        </VFormRender>
      </div>
      <!--      <div slot="footer" class="dialog-footer">-->
      <!--        <el-button type="" @click="showPreviewDialogFlag = false">关闭</el-button>-->
      <!--      </div>-->
    </el-dialog>
    <oa-dialog
      ref="oaDialog"
      :fromData="fromData"
      :formConfig="formConfig"
      :formRules="formRules"
      :formDataInit="formDataInit"
      @submit="submit"
    ></oa-dialog>
  </div>
</template>

<script>
import i18n, { changeLocale } from '@/utils/i18n'
import SvgIcon from '@/components/svg-icon-nc'
import { createDesigner } from '../designer'
import VFormRender from '@/components/form-render/index'

import {
  addWindowResizeHandler,
  deepClone,
  getAllContainerWidgets,
  getAllFieldWidgets,
  getQueryParam,
  traverseAllWidgets,
  generateId
} from '@/utils/util'
import FieldPanel from './FieldPanel'
import VFormWidget from './VFormWidget'
import SettingPanel from './SettingPanel'
import oaDialog from '@/components/form-common/dialog-form'
import { changeDashboard } from '@/api/chart'

export default {
  name: 'VFormDesigner',
  componentName: 'VFormDesigner',
  mixins: [i18n],
  components: {
    SvgIcon,
    FieldPanel,
    VFormWidget,
    SettingPanel,
    VFormRender,
    oaDialog
  },
  props: {
    fieldListApi: { type: Object, default: null },
    fieldListData: { type: Object, default: null },
    bannedWidgets: { type: Array, default: () => [] },
    designerConfig: {
      type: Object,
      default: () => ({
        languageMenu: !0,
        externalLink: !0,
        formTemplates: !0,
        componentLib: !0,
        chartLib: !1,
        metadataLib: !1,
        layoutTypeButton: !0,
        eventCollapse: !0,
        widgetNameReadonly: !1,
        clearDesignerButton: !0,
        previewFormButton: !0,
        importJsonButton: !0,
        exportJsonButton: !0,
        exportCodeButton: !0,
        generateSFCButton: !0,
        logoHeader: !0,
        toolbarMaxWidth: 450,
        toolbarMinWidth: 300,
        productName: '',
        productTitle: '',
        presetCssCode: '',
        languageName: 'zh-CN',
        resetFormJson: !1
      })
    },
    globalDsv: { type: Object, default: () => ({}) },
    formTemplates: { type: Array, default: null },
    testOptionData: { type: Object, default: null }
  },
  data() {
    return {
      vFormVersion: '1.0.0',
      curLangName: '',
      curLocale: '',
      vsCodeFlag: !1,
      showPreviewDialogFlag: !1,
      caseName: '',
      docUrl: '',
      gitUrl: '',
      chatUrl: '',
      subScribeUrl: '',
      designer: createDesigner(this),
      fieldList: [],
      subFormList: [],
      optionData: this.testOptionData,
      externalComponents: {},
      leftAsideVisible: !0,
      rightAsideVisible: !0,
      widgetPanelKey: 'widgetPanel' + generateId(),
      scrollerHeight: 0,
      fromData: {
        width: '600px',
        title: '编辑名称',
        btnText: '确定',
        labelWidth: '100px',
        type: ''
      },
      formConfig: [
        {
          type: 'input',
          label: '图表名称：',
          placeholder: '请输入图表名称',
          key: 'name',
          options: []
        }
      ],
      id: 0,
      formRules: {
        name: [
          {
            required: true,
            message: '请输入图表名称',
            trigger: 'blur'
          }
        ]
      },
      formDataInit: {
        name: ''
      },
      screenWidth: null
    }
  },
  provide() {
    return {
      getServerFieldList: () => this.fieldList,
      getServerSubFormList: () => this.subFormList,
      getDesignerConfig: () => this.designerConfig,
      getBannedWidgets: () => this.bannedWidgets,
      getTestOptionData: () => this.optionData,
      previewState: false,
      getScreenWidth: () => this.screenWidth
    }
  },
  computed: {
    vfProductName() {
      return (this.designerConfig && this.designerConfig.productName) || ''
    },
    vfProductTitle() {
      return (this.designerConfig && this.designerConfig.productTitle) || this.i18nt('application.productTitle')
    }
  },
  created() {},
  mounted() {
    this.initLocale()
    this.initFormTemplates()
    this.loadCase()
    this.initServerFields()
    this.scrollerHeight = window.innerHeight - 60 + 'px'
    addWindowResizeHandler(() => {
      this.$nextTick(() => {
        this.screenWidth = window.innerWidth
        this.scrollerHeight = window.innerHeight - 60 + 'px'
      })
    })
  },
  methods: {
    submit(data) {
      changeDashboard(this.id, data).then((res) => {
        this.$emit('changeTitle', data.name)
        this.$refs.oaDialog.handleClose()
      })
    },
    // 编辑
    editFn(row) {
      this.id = row.id
      this.formDataInit.name = row.name
      this.$refs.oaDialog.openBox()
    },
    testEEH(o, e) {},
    showLink(o) {
      return this.designerConfig[o] === void 0 ? !0 : !!this.designerConfig[o]
    },
    openUrl(o, e) {
      if (this.vsCodeFlag) {
        const n = { cmd: 'openUrl', data: { url: e } }
        window.parent.postMessage(n, '*')
      } else {
        let n = o.currentTarget
        n.href = e
      }
    },
    loadCase() {
      !this.caseName ||
        axios
          .get(MOCK_CASE_URL + this.caseName + '.txt')
          .then((o) => {
            if (o.data.code) {
              this.$message.error(this.i18nt('designer.hint.sampleLoadedFail'))
              return
            }
            this.setFormJson(o.data), this.$message.success(this.i18nt('designer.hint.sampleLoadedSuccess'))
          })
          .catch((o) => {
            this.$message.error(this.i18nt('designer.hint.sampleLoadedFail') + ':' + o)
          })
    },
    initLocale() {
      ;(this.curLocale = localStorage.getItem('form_cache')),
        this.vsCodeFlag ? (this.curLocale = this.curLocale || 'en-US') : (this.curLocale = this.curLocale || 'zh-CN'),
        (this.curLangName = this.i18nt('application.' + this.curLocale)),
        this.changeLanguage(this.curLocale)
    },
    initFormTemplates() {
      this.formTemplates &&
        (clearFormTemplates(),
        this.formTemplates.length > 0 &&
          this.formTemplates.forEach((o) => {
            addFormTemplate(o)
          }))
    },
    loadFieldListFromServer() {
      if (!this.fieldListApi) return
      let o = this.fieldListApi.headers || {}
      axios
        .get(this.fieldListApi.URL, { headers: o })
        .then((e) => {
          let n = this.fieldListApi.labelKey || 'label',
            l = this.fieldListApi.nameKey || 'name',
            s = this.fieldListApi.resultDataName || '',
            c = s ? e.data[s] : e.data
          this.fieldList.splice(0, this.fieldList.length),
            c.forEach((u) => {
              this.fieldList.push({
                label: u[n],
                name: u[l]
              })
            })
        })
        .catch((e) => {
          this.$message.error(e)
        })
    },
    initServerFields() {
      !!this.fieldListData && !!this.fieldListData.fieldList
        ? (this.fieldList.splice(0, this.fieldList.length, ...this.fieldListData.fieldList),
          this.fieldListData.subFormList &&
            this.subFormList.splice(0, this.subFormList.length, ...this.fieldListData.subFormList))
        : this.loadFieldListFromServer()
    },
    setFieldListData(o) {
      !!o &&
        !!o.fieldList &&
        (this.fieldList.splice(0, this.fieldList.length, ...o.fieldList),
        o.subFormList && this.subFormList.splice(0, this.subFormList.length, ...o.subFormList))
    },
    handleLanguageChanged(o) {
      this.changeLanguage(o), (this.curLangName = this.i18nt('application.' + o))
    },
    changeLanguage(o) {
      changeLocale(o)
    },
    setFormJson(o) {
      let e = !1
      o &&
        (typeof o == 'string'
          ? (e = this.designer.loadFormJson(JSON.parse(o)))
          : o.constructor === Object && (e = this.designer.loadFormJson(o)),
        e && this.designer.emitHistoryChange())
    },
    getFormJson() {
      return {
        widgetList: deepClone(this.designer.widgetList),
        formConfig: deepClone(this.designer.formConfig)
      }
    },
    clearDesigner() {
      this.$refs.toolbarRef.clearFormWidget()
    },
    refreshDesigner() {
      let o = this.getFormJson()
      this.designer.clearDesigner(!0), this.designer.loadFormJson(o)
    },
    previewForm() {
      this.showPreviewDialogFlag = true
    },
    importJson() {
      this.$refs.toolbarRef.importJson()
    },
    exportJson() {
      this.$refs.toolbarRef.exportJson()
    },
    exportCode() {
      this.$refs.toolbarRef.exportCode()
    },
    generateSFC() {
      this.$refs.toolbarRef.generateSFC()
    },
    getFieldWidgets(o = null, e = !1) {
      return getAllFieldWidgets(o || this.designer.widgetList, e)
    },
    getContainerWidgets(o = null) {
      return getAllContainerWidgets(o || this.designer.widgetList)
    },
    upgradeFormJson(o) {
      if (!o.widgetList || !o.formConfig) {
        this.$message.error('Invalid form json!')
        return
      }
      return (
        traverseAllWidgets(o.widgetList, (e) => {
          this.designer.upgradeWidgetConfig(e)
        }),
        this.designer.upgradeFormConfig(o.formConfig),
        o
      )
    },
    getWidgetRef(o, e = !1) {
      return this.$refs.formRef.getWidgetRef(o, e)
    },
    getSelectedWidgetRef() {
      return this.$refs.formRef.getSelectedWidgetRef()
    },
    addDataSource(o) {
      this.designer.formConfig.dataSources.push(o)
    },
    addEC(o, e) {
      this.externalComponents[o] = e
    },
    hasEC(o) {
      return this.externalComponents.hasOwnProperty(o)
    },
    getEC(o) {
      return this.externalComponents[o]
    },
    buildFormDataSchema() {
      let o = {},
        e = getAllContainerWidgets(this.designer.widgetList),
        n = [],
        l = []
      e.forEach(($) => {
        $.type === 'sub-form' || $.type === 'grid-sub-form'
          ? n.push($.container)
          : $.type === 'object-group' && l.push($.container)
      })
      let s = []
      n.forEach(($) => {
        let g = {}
        traverseFieldWidgetsOfContainer($, (y) => {
          y.formItemFlag && ((g[y.options.name] = y.type), s.push(y.options.name))
        }),
          (o[$.options.name] = g)
      })
      let c = []
      return (
        l.forEach(($) => {
          let g = {}
          traverseFieldWidgetsOfContainer($, (f) => {
            f.formItemFlag && ((g[f.options.name] = f.type), c.push(f.options.name))
          })
          let y = $.options.objectName
          setObjectValue(o, y, g)
        }),
        getAllFieldWidgets(this.designer.widgetList).forEach(($) => {
          s.indexOf($.name) === -1 && c.indexOf($.name) === -1 && (o[$.name] = $.type)
        }),
        o
      )
    },
    getFormTemplates() {
      return getAllFormTemplates()
    },
    clearFormTemplates() {
      clearFormTemplates()
    },
    clearSelected() {
      this.designer.setSelected(0)
    },
    addFormTemplate(o) {
      addFormTemplate(o), this.designer.emitEvent('refresh-form-templates')
    },
    deleteFormTemplate(o) {
      deleteFormTemplate(o), this.designer.emitEvent('refresh-form-templates')
    },
    toggleLeftAside() {
      this.leftAsideVisible = !this.leftAsideVisible
    },
    toggleRightAside() {
      this.rightAsideVisible = !this.rightAsideVisible
    },
    changePrimaryColor(o) {
      document.documentElement.style.setProperty('--el-color-primary', o),
        document.documentElement.style.setProperty('--vf-color-primary', o)
    },
    setMetaFields(o) {
      this.$refs.widgetPanelRef.setMetaFields(o)
    },
    setTestOptionData(o) {
      this.optionData = o
    },
    refreshWidgetPanel() {
      this.widgetPanelKey = 'widgetPanel' + generateId()
    }
  }
}
</script>
<style lang="scss" scoped>
.el-container.main {
  background: #fff !important;

  ::v-deep aside {
    /* 防止aside样式被外部样式覆盖！！ */
    margin: 0;
    padding: 0;
    background: #fff;
  }
}
.main-container {
  margin-top: 0 !important;
}

.el-container.full-height {
  flex: 1;
  overflow: hidden;
}

.el-container.center-layout-container {
  min-width: 680px;
  // border-left: 2px dotted #ebeef5;
  // border-right: 2px dotted #ebeef5;
}

.el-header.main-header {
  // border-bottom: 2px dotted #ebeef5;
  height: 48px !important;
  line-height: 48px !important;
  min-width: 800px;
}

div.main-title {
  font-size: 18px;
  color: #242424;
  display: flex;
  align-items: center;
  justify-items: center;

  img {
    cursor: pointer;
    width: 36px;
    height: 36px;
  }

  span.bold {
    font-size: 20px;
    font-weight: bold;
    margin: 0 6px 0 6px;
  }

  span.version-span {
    font-size: 14px;
    color: #101f1c;
    margin-left: 6px;
  }
}

.float-left {
  float: left;
}

.float-right {
  float: right;
}

.el-dropdown-link {
  margin-right: 12px;
  cursor: pointer;
}

div.external-link a {
  font-size: 13px;
  text-decoration: none;
  margin-right: 10px;
  color: #606266;
}
.form-designer-container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.el-header.toolbar-header {
  font-size: 14px;
  // border-bottom: 1px dotted #cccccc;
  height: 54px !important;
  //line-height: 42px !important;
}

.el-aside.side-panel {
  width: 310px !important;
  overflow-y: hidden;
}
.el-aside.side-panel.aside-hidden {
  width: 0 !important;
  animation: aside-slide-hidden 0.3s ease-in-out;
}
.el-aside.side-panel.aside-visible {
  animation: aside-slide-show 0.3s ease-in-out;
}
// 收起动画
@keyframes aside-slide-hidden {
  from {
    width: 310px;
  }
  to {
    width: 0;
  }
}
// 展开动画
@keyframes aside-slide-show {
  from {
    width: 0;
  }
  to {
    width: 310px;
  }
}
.el-aside {
  overflow: inherit;
}
.el-aside.setting-panel {
  position: relative;
  width: max-content !important;
}

.el-main.form-widget-main {
  padding: 0;

  position: relative;
  overflow-x: hidden;
}

.container-scroll-bar {
  ::v-deep .el-scrollbar__wrap,
  ::v-deep .el-scrollbar__view {
    overflow-x: hidden;
    overflow-y: auto;
  }
}
.left-aside-toggle-bar {
  display: block;
  cursor: pointer;
  height: 36px;
  width: 12px;
  position: absolute;
  top: calc(50% - 18px);
  left: 310px;
  border-radius: 0 4px 4px 0;
  background: #f0f2f5;
  z-index: 8;
  display: flex;
  align-items: center;
  justify-content: center;
}
.left-aside-toggle-bar i {
  font-size: 14px;
  color: #909399;
  margin-left: 0px;
}
.left-aside-toggle-bar:hover i {
  color: var(--vf-color-primary, #409eff);
}
.left-aside-toggle-bar.aside-hidden {
  left: -2px;
}

.right-aside-toggle-bar {
  display: block;
  cursor: pointer;
  height: 36px;
  width: 12px;
  position: absolute;
  top: calc(50% - 45px);
  right: 310px;
  border-radius: 4px 0 0 4px;
  background: #f0f2f5;
  z-index: 8;
  display: flex;
  align-items: center;
  justify-content: center;
}
.right-aside-toggle-bar i {
  font-size: 14px;
  color: #909399;
  position: relative;
  top: 0px;
  left: 0px;
}
.right-aside-toggle-bar:hover i {
  color: var(--vf-color-primary, #409eff);
}
.right-aside-toggle-bar.aside-hidden {
  right: -2px;
}
.toolbar-header {
  width: 100%;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #f2f6fc;
  ::v-deep.title {
    font-weight: 500;
    font-size: 18px;
    color: #303133;
    line-height: 18px;
  }
}
</style>
