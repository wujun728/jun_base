<template>
  <div class="form-widget-container">
    <el-form
      class="full-height-width widget-form"
      :label-position="labelPosition"
      :class="[customClass, layoutType + '-layout']"
      :size="size"
      :validate-on-rule-change="false"
    >
      <div v-if="designer.widgetList.length === 0" class="no-widget-hint">
        <i class="el-icon-rank"></i> {{ i18nt('designer.noWidgetHint') }}
      </div>

      <draggable
        :list="designer.widgetList"
        v-bind="{ group: 'dragGroup', ghostClass: 'ghost', animation: 300 }"
        handle=".drag-handler"
        @end="onDragEnd"
        @add="onDragAdd"
        @update="onDragUpdate"
        :move="checkMove"
      >
        <transition-group name="fade" tag="div" class="form-widget-list">
          <template v-for="(widget, index) in designer.widgetList">
            <template v-if="'container' === widget.category">
              <component
                :is="getWidgetName(widget)"
                :widget="widget"
                :designer="designer"
                :key="widget.id"
                :parent-list="designer.widgetList"
                :index-of-parent-list="index"
                :parent-widget="null"
              ></component>
            </template>
            <template v-else>
              <component
                :is="getWidgetName(widget)"
                :field="widget"
                :designer="designer"
                :key="widget.id"
                :parent-list="designer.widgetList"
                :index-of-parent-list="index"
                :parent-widget="null"
                :design-state="true"
              ></component>
            </template>
          </template>
        </transition-group>
      </draggable>
    </el-form>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import '@/components/form-designer/form-widget/container-widget/index'
import FieldComponents from '@/components/form-designer/form-widget/field-widget/index'
import i18n from '@/utils/i18n'
import { containers } from '@/components/form-designer/widget-panel/widgetsConfig'
import { el } from '@fullcalendar/core/internal-common'

export default {
  name: 'VFormWidget',
  componentName: 'VFormWidget',
  mixins: [i18n],
  components: {
    Draggable,

    ...FieldComponents
  },
  props: {
    designer: Object,
    formConfig: Object,
    optionData: {
      //prop传入的选项数据
      type: Object,
      default: () => ({})
    },
    globalDsv: {
      type: Object,
      default: () => ({})
    }
  },
  provide() {
    return {
      refList: this.widgetRefList,
      formConfig: this.formConfig,
      getGlobalDsv: () => this.globalDsv, // 全局数据源变量
      globalOptionData: this.optionData,
      getOptionData: () => this.optionData,
      globalModel: {
        formModel: this.formModel
      }
    }
  },
  inject: ['getDesignerConfig'],
  data() {
    return {
      formModel: {},
      widgetRefList: {},
      containersNames: containers.map((con) => con.type)
    }
  },
  computed: {
    labelPosition() {
      if (!!this.designer.formConfig && !!this.designer.formConfig.labelPosition) {
        return this.designer.formConfig.labelPosition
      }

      return 'left'
    },

    size() {
      if (!!this.designer.formConfig && !!this.designer.formConfig.size) {
        return this.designer.formConfig.size
      }

      return 'medium'
    },

    customClass() {
      return this.designer.formConfig.customClass || ''
    },

    layoutType() {
      return this.designer.getLayoutType()
    }
  },
  watch: {
    'designer.widgetList': {
      deep: true,
      handler(val) {
        //
      }
    },

    'designer.formConfig': {
      deep: true,
      handler(val) {
        //
      }
    }
  },
  created() {
    this.designer.initDesigner(!!this.getDesignerConfig().resetFormJson)
    this.designer.loadPresetCssCode(this.getDesignerConfig().presetCssCode)
  },
  mounted() {
    this.disableFirefoxDefaultDrop()
    this.designer.registerFormWidget(this)
  },
  methods: {
    getWidgetName(widget) {
      return widget.type + '-widget'
    },
    /* 禁用Firefox默认拖拽搜索功能!! */
    disableFirefoxDefaultDrop() {
      let isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') !== -1
      if (isFirefox) {
        document.body.ondrop = function (event) {
          event.stopPropagation()
          event.preventDefault()
        }
      }
    },

    onDragEnd(evt) {
      const newIndex = evt.newIndex
      if (!!this.designer.widgetList[newIndex]) {
        this.designer.setSelected(this.designer.widgetList[newIndex])
      }
    },

    onDragAdd(evt) {
      const newIndex = evt.newIndex
      let widget = this.designer.widgetList[newIndex]
      let name = this.widgetNameFilter(widget)
      if (!!this.designer.widgetList[newIndex]) {
        this.designer.setNamesList(this.designer.widgetList[newIndex])
        this.designer.setSelected(this.designer.widgetList[newIndex])
      }
      this.designer.emitHistoryChange(name)
      this.designer.emitEvent('field-selected', null)
    },

    onDragUpdate() {
      /* 在VueDraggable内拖拽组件发生位置变化时会触发update，未发生组件位置变化不会触发！！ */
      this.designer.emitHistoryChange()
    },

    checkMove(evt) {
      return this.designer.checkWidgetMove(evt)
    },

    getFormData() {
      return this.formModel
    },

    getWidgetRef(widgetName, showError = false) {
      let foundRef = this.widgetRefList[widgetName]
      if (!foundRef && !!showError) {
        this.$message.error(this.i18nt('render.hint.refNotFound') + widgetName)
      }
      return foundRef
    },

    getSelectedWidgetRef() {
      let wName = this.designer.selectedWidgetName
      return this.getWidgetRef(wName)
    },

    clearWidgetRefList() {
      Object.keys(this.widgetRefList).forEach((key) => {
        delete this.widgetRefList[key]
      })
    },
    widgetNameFilter(obj) {
      let type = containers.map((con) => con.type)
      if (!type.includes(obj.type)) {
        return obj.options.name
      } else {
        return ''
      }
    },
    crudStatusFilter(obj, nameArr) {
      let names = nameArr

      if (obj.type === 'grid') {
        this.crudStatusFilter(obj.cols, names)
      } else if (obj.type === 'grid-col' && obj.widgetList) {
        obj.widgetList.forEach((item) => {
          if (this.containersNames.includes(item.type)) {
            this.crudStatusFilter(item, names)
          } else {
            names.push(item.options.name)
          }
        })
      } else if (obj.type === 'card' && obj.widgetList.length) {
        obj.widgetList.forEach((item) => {
          if (this.containersNames.includes(item.type)) {
            this.crudStatusFilter(item, names)
          } else {
            names.push(item.options.name)
          }
        })
      } else if (obj.type === 'tab' && obj.tabs.length) {
        obj.tabs.forEach((item) => {
          if (item.type === 'tab-pane') {
            item.widgetList.forEach((item2) => {
              if (this.containersNames.includes(item2.type)) {
                this.crudStatusFilter(item2, names)
              } else {
                names.push(item2.options.name)
              }
            })
          }
        })
      } else if (Array.isArray(obj) && obj.length) {
        obj.forEach((item) => {
          this.crudStatusFilter(item, names)
        })
        // if (!this.containersNames.includes(obj.type)) return [obj.options.name]
      }

      return names
    },
    deleteWidgetRef(widgetRefName, removeWidget) {
      if (widgetRefName) {
        let index = this.designer.selectNames.findIndex((name) => {
          return name === widgetRefName
        })

        this.designer.selectNames.splice(index, 1)
      } else if (removeWidget) {
        let arr = []
        let names = this.crudStatusFilter(removeWidget, arr)
        names.forEach((name) => {
          let index = this.designer.selectNames.findIndex((n) => {
            return n === name
          })
          this.designer.selectNames.splice(index, 1)
        })
      }

      // delete this.widgetRefList[widgetRefName]
    }
  }
}
</script>

<style lang="scss" scoped>
.container-scroll-bar {
  ::v-deep .el-scrollbar__wrap,
  ::v-deep .el-scrollbar__view {
    overflow-x: hidden;
  }
}

.form-widget-container {
  padding: 10px;
  background: #f1f2f3;

  overflow-x: hidden;
  overflow-y: auto;

  .el-form.full-height-width {
    height: 100%;
    padding: 5px;
    background: #ffffff;

    .no-widget-hint {
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      bottom: 0;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      text-align: center;
      font-size: 16px;
      color: #555;
      background-color: #fff;
    }

    .form-widget-list {
      min-height: calc(100vh - 276px); // from 视口高度
      padding: 3px;
    }
  }

  .el-form.Pad-layout {
    margin: 0 auto;
    max-width: 960px;
    border-radius: 15px;
    box-shadow: 0 0 1px 10px #495060;
  }

  .el-form.H5-layout {
    margin: 0 auto;
    width: 420px;
    border-radius: 15px;
    //border-width: 10px;
    box-shadow: 0 0 1px 10px #495060;
  }

  .el-form.widget-form ::v-deep .el-row {
    padding: 2px;
    border: 1px dashed #dcdfe6;
  }
}

.grid-cell {
  min-height: 30px;
  border-right: 1px dotted #dcdfe6;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
@/utils/i18ns
