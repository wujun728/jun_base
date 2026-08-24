<!-- @FileDescription: 低代码-表单设计页面 -->
<template>
  <el-container class="main full-height">
    <el-container>
      <el-aside class="side-panel">
        <widget-panel :designer="designer" />
      </el-aside>
      <el-container class="center-layout-container">
        <el-header class="toolbar-header">
          <toolbar-panel :designer="designer" :global-dsv="globalDsv" ref="toolbarRef">
            <template v-for="(idx, slotName) in $slots" #[slotName]>
              <slot :name="slotName"></slot>
            </template>
          </toolbar-panel>
        </el-header>
        <el-main class="form-widget-main">
          <el-scrollbar class="container-scroll-bar" :style="{ height: scrollerHeight }">
            <v-form-widget
              :designer="designer"
              :form-config="designer.formConfig"
              :global-dsv="globalDsv"
              ref="formRef"
              @end="onEnd"
            >
            </v-form-widget>
          </el-scrollbar>
        </el-main>
      </el-container>

      <el-aside class="setting-panel">
        <setting-panel
          :designer="designer"
          :selected-widget="designer.selectedWidget"
          :form-config="designer.formConfig"
          :global-dsv="globalDsv"
        />
      </el-aside>
    </el-container>
  </el-container>
</template>

<script>
import WidgetPanel from './widget-panel/index'
import ToolbarPanel from './toolbar-panel/index'
import SettingPanel from './setting-panel/index'
import VFormWidget from './form-widget/index'
import { createDesigner } from '@/components/form-designer/designer'
import {
  addWindowResizeHandler,
  deepClone,
  getAllContainerWidgets,
  getAllFieldWidgets,
  getQueryParam,
  traverseAllWidgets
} from '@/utils/util'
import i18n, { changeLocale } from '@/utils/i18n'
import axios from 'axios'
// import SvgIcon from '@/components/svg-icon'

export default {
  name: 'VFormDesigner',
  componentName: 'VFormDesigner',
  mixins: [i18n],
  components: {
    WidgetPanel,
    ToolbarPanel,
    SettingPanel,
    VFormWidget
    // SvgIcon
  },
  props: {
    /* 后端字段列表API */
    fieldListApi: {
      type: Object,
      default: null
    },

    /* 禁止显示的组件名称数组 */
    bannedWidgets: {
      type: Array,
      default: () => []
    },

    designerConfig: {
      type: Object,
      default: () => {
        return {
          eventCollapse: true, //是否显示组件事件属性折叠面板
          widgetNameReadonly: false, //禁止修改组件名称

          clearDesignerButton: true, //是否显示清空设计器按钮
          previewFormButton: true, //是否显示预览表单按钮
          importJsonButton: true, //是否显示导入JSON按钮
          exportJsonButton: true, //是否显示导出JSON器按钮
          exportCodeButton: true, //是否显示导出代码按钮
          generateSFCButton: true, //是否显示生成SFC按钮
          toolbarMaxWidth: 200, //设计器工具按钮栏最大宽度（单位像素）
          toolbarMinWidth: 300, //设计器工具按钮栏最小宽度（单位像素）

          presetCssCode: '', //设计器预设CSS样式代码
          resetFormJson: false //是否在设计器初始化时将表单内容重置为空
        }
      }
    },

    /* 全局数据源变量 */
    globalDsv: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      curLangName: '',
      vsCodeFlag: false,
      caseName: '',
      scrollerHeight: 0,
      designer: createDesigner(this),
      fieldList: []
    }
  },
  provide() {
    return {
      serverFieldList: this.fieldList,
      getDesignerConfig: () => this.designerConfig,
      getBannedWidgets: () => this.bannedWidgets,
      getReadMode: () => false
    }
  },
  created() {
    this.vsCodeFlag = getQueryParam('vscode') == 1
    this.caseName = getQueryParam('case')
  },
  mounted() {
    this.initLocale()

    this.scrollerHeight = window.innerHeight - 250 + 'px'
    addWindowResizeHandler(() => {
      this.$nextTick(() => {
        this.scrollerHeight = window.innerHeight - 250 + 'px'
      })
    })

    this.loadFieldListFromServer()
  },
  methods: {
    onEnd(e) {
      console.log('onEnd', e)
    },
    showLink(configName) {
      if (this.designerConfig[configName] === undefined) {
        return true
      }

      return !!this.designerConfig[configName]
    },

    openUrl(event, url) {
      if (!!this.vsCodeFlag) {
        const msgObj = {
          cmd: 'openUrl',
          data: {
            url
          }
        }
        window.parent.postMessage(msgObj, '*')
      } else {
        let aDom = event.currentTarget
        aDom.href = url
        //window.open(url, '_blank') //直接打开新窗口，会被浏览器拦截
      }
    },

    loadCase() {},

    initLocale() {
      let curLocale = localStorage.getItem('form_cache')
      if (!!this.vsCodeFlag) {
        curLocale = curLocale || 'en-US'
      } else {
        curLocale = curLocale || 'zh-CN'
      }
      this.curLangName = this.i18nt('application.' + curLocale)
      this.changeLanguage(curLocale)
    },

    loadFieldListFromServer() {
      if (!this.fieldListApi) {
        return
      }

      let headers = this.fieldListApi.headers || {}
      axios
        .get(this.fieldListApi.URL, { headers: headers })
        .then((res) => {
          let labelKey = this.fieldListApi.labelKey || 'label'
          let nameKey = this.fieldListApi.nameKey || 'name'

          this.fieldList.splice(0, this.fieldList.length) //清空已有
          res.data.forEach((fieldItem) => {
            this.fieldList.push({
              label: fieldItem[labelKey],
              name: fieldItem[nameKey]
            })
          })
        })
        .catch((error) => {
          this.$message.error(error)
        })
    },

    handleLanguageChanged(command) {
      this.changeLanguage(command)
      this.curLangName = this.i18nt('application.' + command)
    },

    changeLanguage(langName) {
      changeLocale(langName)
    },

    setFormJson(formJson) {
      let modifiedFlag = false
      if (!!formJson) {
        if (typeof formJson === 'string') {
          modifiedFlag = this.designer.loadFormJson(JSON.parse(formJson))
        } else if (formJson.constructor === Object) {
          modifiedFlag = this.designer.loadFormJson(formJson)
        }

        if (modifiedFlag) {
          this.designer.emitHistoryChange()
        }
      }
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

    /**
     * 刷新表单设计器
     */
    refreshDesigner() {
      //this.designer.loadFormJson( this.getFormJson() )  //只有第一次调用生效？？

      let fJson = this.getFormJson()
      this.designer.clearDesigner(true) //不触发历史记录变更
      this.designer.loadFormJson(fJson)
    },

    /**
     * 预览表单
     */
    previewForm() {
      this.$refs.toolbarRef.previewForm()
    },

    /**
     * 导入表单JSON
     */
    importJson() {
      this.$refs.toolbarRef.importJson()
    },

    /**
     * 导出表单JSON
     */
    exportJson() {
      this.$refs.toolbarRef.exportJson()
    },

    /**
     * 导出Vue/HTML代码
     */
    exportCode() {
      this.$refs.toolbarRef.exportCode()
    },

    /**
     * 生成SFC代码
     */
    generateSFC() {
      this.$refs.toolbarRef.generateSFC()
    },

    /**
     * 获取所有字段组件
     * @returns {*[]}
     */
    getFieldWidgets(widgetList = null) {
      return !!widgetList ? getAllFieldWidgets(widgetList) : getAllFieldWidgets(this.designer.widgetList)
    },

    /**
     * 获取所有容器组件
     * @returns {*[]}
     */
    getContainerWidgets(widgetList = null) {
      return !!widgetList ? getAllContainerWidgets(widgetList) : getAllContainerWidgets(this.designer.widgetList)
    },

    /**
     * 升级表单json，以补充最新的组件属性
     * @param formJson
     */
    upgradeFormJson(formJson) {
      if (!formJson.widgetList || !formJson.formConfig) {
        this.$message.error('Invalid form json!')
        return
      }

      traverseAllWidgets(formJson.widgetList, (w) => {
        this.designer.upgradeWidgetConfig(w)
      })
      this.designer.upgradeFormConfig(formJson.formConfig)

      return formJson
    },

    getWidgetRef(widgetName, showError = false) {
      return this.$refs['formRef'].getWidgetRef(widgetName, showError)
    },

    getSelectedWidgetRef() {
      return this.$refs['formRef'].getSelectedWidgetRef()
    }

    //TODO: 增加更多方法！！
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
  height: 100%;
  overflow-y: hidden;
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

.el-header.toolbar-header {
  font-size: 14px;
  // border-bottom: 1px dotted #cccccc;
  height: 42px !important;
  //line-height: 42px !important;
}

.el-aside.side-panel {
  width: 260px !important;
  overflow-y: hidden;
}
.el-aside.setting-panel {
  width: 310px !important;
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
</style>
@/utils/i18ns
