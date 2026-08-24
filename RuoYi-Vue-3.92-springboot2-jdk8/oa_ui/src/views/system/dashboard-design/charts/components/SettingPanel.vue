<template>
  <el-scrollbar class="setting-scrollbar" :style="{ height: scrollerHeight }">
    <template v-if="designer.selectedWidget && designer.selectedWidget.type">
      <el-form
        :model="optionModel"
        size="mini"
        label-position="left"
        label-width="120px"
        class="setting-form"
        @submit.native.prevent
      >
        <el-collapse v-model="widgetActiveCollapseNames" class="setting-collapse">
          <el-collapse-item name="1" v-if="showCollapse(commonProps)" :title="i18nt('designer.setting.commonSetting')">
            <template v-for="(A, R) in commonProps">
              <!-- {{ A }} -->

              <component
                v-if="hasPropEditor(R, A)"
                :is="getPropEditor(R, A)"
                :designer="designer"
                :selected-widget="selectedWidget"
                :option-model.sync="optionModel"
                @clearSearch="clearSearch"
                @optionModelChange="optionModelChange"
              />
            </template>
          </el-collapse-item>
        </el-collapse>
      </el-form>
    </template>
    <div v-else-if="!designer.selectedWidget">
      <div class="no-widget-selected">
        <img class="no-widget-selected-img" src="@/assets/images/defd-da.png" alt="" />
        <div class="no-widget-selected-text">当前没有选中设置的组件</div>
      </div>
    </div>
  </el-scrollbar>
</template>

<script>
import i18n, { changeLocale } from '@/utils/i18n'
import emitter from '@/utils/emitter'
import { COMMON_PROPERTIES$1, ADVANCED_PROPERTIES$1, EVENT_PROPERTIES$1 } from '../configData'
import CodeEditor from '@/components/code-editor/index'
import FormSetting from '@/components/form-designer/setting-panel/form-setting.vue'
import { propertyRegistered } from '../../utils'
import { addWindowResizeHandler } from '@/utils/util'
import comp from '../property-editor/export'
import PropertyEditors from '@/components/form-designer/setting-panel/property-editor/index'
export default {
  name: 'SettingPanel',
  componentName: 'SettingPanel',
  mixins: [i18n, emitter],
  components: { CodeEditor, FormSetting, ...PropertyEditors, ...comp },
  props: {
    designer: Object,
    selectedWidget: Object,
    formConfig: Object,
    globalDsv: { type: Object, default: () => ({}) }
  },
  provide() {
    return {
      isSubFormChildWidget: () => this.subFormChildWidgetFlag,
      getGlobalDsv: () => this.globalDsv
    }
  },
  inject: ['getDesignerConfig'],
  data() {
    return {
      designerConfig: this.getDesignerConfig(),
      activeTab: '2',
      widgetActiveCollapseNames: ['1', '3'],
      formActiveCollapseNames: ['1', '2'],
      commonProps: COMMON_PROPERTIES$1,
      advProps: ADVANCED_PROPERTIES$1,
      eventProps: EVENT_PROPERTIES$1,
      showWidgetEventDialogFlag: !1,
      eventHandlerCode: '',
      curEventName: '',
      eventHeader: '',
      subFormChildWidgetFlag: !1,
      scrollerHeight: 0
    }
  },
  computed: {
    optionModel: {
      get() {
        return this.selectedWidget.options
      },
      set(o) {
        this.selectedWidget.options = o
      }
    }
  },
  watch: {
    'designer.selectedWidget': {
      handler(o) {
        o && (this.activeTab = '1')
      }
    },
    'selectedWidget.options': {
      deep: !0,
      handler() {
        this.designer.saveCurrentHistoryStep()
      }
    },
    formConfig: {
      deep: !0,
      handler() {
        this.designer.saveCurrentHistoryStep()
      }
    }
  },
  created() {
    this.on$('editEventHandler', (o) => {
      this.editEventHandler(o[0], o[1])
    }),
      this.designer.handleEvent('form-css-updated', (o) => {
        this.designer.setCssClassList(o)
      }),
      this.designer.handleEvent('field-selected', (o) => {
        this.subFormChildWidgetFlag = !!o && o.type === 'sub-form'
      })
  },
  mounted() {
    this.designer.selectedWidget ? (this.activeTab = '1') : (this.activeTab = '2')
    this.scrollerHeight = window.innerHeight - 54 + 'px'
    addWindowResizeHandler(() => {
      this.$nextTick(() => {
        this.scrollerHeight = window.innerHeight - 54 + 'px'
      })
    })
  },
  methods: {
    optionModelChange(o) {
      this.optionModel = o
    },
    // 图表实体切换事件
    clearSearch(o) {
      o.setDimensional = {
        dimension: [],
        metrics: [],
        targetValue: 1,
        showFields: [],
        dimensionRow: [],
        dimensionCol: []
      }
      o.setChartFilter = {
        equation: '',
        list: []
      }
      this.optionModel = o
    },
    getEventHandled(o) {
      return !!this.optionModel[o] && this.optionModel[o].length > 0
    },
    showEventCollapse() {
      return this.designerConfig.eventCollapse === void 0 ? !0 : !!this.designerConfig.eventCollapse
    },
    hasPropEditor(o, e) {
      if (!e) return !1
      let type = false
      for (let n in this.selectedWidget.options) {
        let name = e.split('-')[0]
        if (name === n) {
          type = true
        }
      }
      return type
    },
    getPropEditor(o, e) {
      for (let n in this.selectedWidget.options) {
        let name = e.split('-')[0]
        if (name === n) {
          return this.$options.components[e]
        }
      }
    },
    showCollapse(o) {
      let e = !1
      for (let n in o)
        if (!!o.hasOwnProperty(n) && this.hasPropEditor(n, o[n])) {
          e = !0
          break
        }
      return e
    },
    editEventHandler(o, e) {
      ;(this.curEventName = o),
        (this.eventHeader = `${this.optionModel.name}.${o}(${e.join(', ')}) {`),
        (this.eventHandlerCode = this.selectedWidget.options[o] || ''),
        o === 'onValidate' &&
          !this.optionModel.onValidate &&
          (this.eventHandlerCode = `  /* sample code */
  /*
  if ((value > 100) || (value < 0)) {
    callback(new Error('error message'))  //fail
  } else {
    callback();  //pass
  }
  */`),
        (this.showWidgetEventDialogFlag = !0)
    },
    saveEventHandler() {
      const o = this.$refs.ecEditor.getEditorAnnotations()
      let e = !1
      if (
        !!o &&
        o.length > 0 &&
        (o.forEach((n) => {
          n.type === 'error' && (e = !0)
        }),
        e)
      ) {
        this.$confirm(this.i18nt('designer.setting.syntaxCheckWarning'), this.i18nt('render.hint.prompt'), {
          confirmButtonText: this.i18nt('render.hint.forcedSave'),
          cancelButtonText: this.i18nt('render.hint.cancel')
        })
          .then(() => {
            ;(this.selectedWidget.options[this.curEventName] = this.eventHandlerCode),
              (this.showWidgetEventDialogFlag = !1)
          })
          .catch((n) => {})
        return
      }
      ;(this.selectedWidget.options[this.curEventName] = this.eventHandlerCode), (this.showWidgetEventDialogFlag = !1)
    }
  }
}
</script>
<style lang="scss" scoped>
.setting-scrollbar {
  border-left: 1px solid #f2f6fc;
  background-color: #fff;
  width: 310px;
  ::v-deep.el-switch__core {
    width: 40px !important;
    min-width: auto;
  }
}
.el-collapse {
  border: none;
}
.setting-form {
  padding: 0px 20px 20px 20px;
}
.el-form-item--mini.el-form-item {
  margin-bottom: 14px;
}
.no-widget-selected {
  width: 100%;
  margin: 20px auto;
  .no-widget-selected-img {
    width: 120px;
    margin: 30px auto;
    display: block;
  }
  .no-widget-selected-text {
    text-align: center;
    font-size: 12px;
    color: #999;
  }
}
</style>
