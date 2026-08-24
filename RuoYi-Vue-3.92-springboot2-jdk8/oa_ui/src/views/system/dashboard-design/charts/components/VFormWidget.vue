<template>
  <div class="form-widget-container">
    <el-form
      class="full-height-width widget-form custom-class layoutType-layout"
      label-position="top"
      size="medium"
      :validate-on-rule-change="false"
    >
      <div class="no-widget-hint" v-if="designer.widgetList.length && designer.widgetList[0].widgetList.length === 0">
        <img src="@/assets/images/no-widget-hint.png" alt="" />
        <div class="trip">暂无图表,选择左侧图表组件，鼠标点击即可加入</div>
      </div>
      <div class="form-widget-canvas" v-else>
        <draggable
          :list="designer.widgetList"
          item-key="id"
          group="dragGroup"
          ghost-class="ghost"
          animation="400"
          tag="div"
          :component-data="{ name: 'fade', class: 'canvas-drag-drop-zone' }"
          handle=".drag-handler"
          @end="onDragEnd"
          @add="onDragAdd"
          @update="onDragUpdate"
          :move="checkMove"
        >
          <template class="transition-group-el" v-for="(g, y) in designer.widgetList">
            <component
              v-if="g.category === 'container'"
              :is="getWidgetName(g)"
              :widget="g"
              :designer="designer"
              :parent-list="designer.widgetList"
              :index-of-parent-list="y"
              :parent-widget="null"
            ></component>
            <component
              v-else
              :is="getWidgetName(g)"
              :field="g"
              :designer="designer"
              :parent-list="designer.widgetList"
              :index-of-parent-list="y"
              :parent-widget="null"
              design-state="true"
            ></component>
          </template>
        </draggable>
      </div>
    </el-form>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
// import FieldComponents from '../charts-widget/index'
import i18n, { changeLocale } from '@/utils/i18n'

export default {
  name: 'VFormWidget',
  componentName: 'VFormWidget',
  mixins: [i18n],
  components: {
    Draggable
  },
  props: {
    designer: Object,
    formConfig: Object,
    optionData: { type: Object, default: () => ({}) },
    globalDsv: { type: Object, default: () => ({}) }
  },
  provide() {
    return {
      refList: this.widgetRefList,
      getFormConfig: () => this.formConfig,
      getGlobalDsv: () => this.globalDsv,
      globalOptionData: this.optionData,
      getOptionData: () => this.optionData,
      getReadMode: () => !1,
      globalModel: { formModel: this.formModel },
      getSubFormFieldFlag: () => !1,
      getSubFormName: () => '',
      getObjectFieldFlag: () => !1,
      getObjectName: () => '',
      getDSResultCache: () => this.dsResultCache
    }
  },
  inject: ['getDesignerConfig'],
  data() {
    return { formModel: {}, widgetRefList: {}, dsResultCache: {} }
  },
  computed: {
    labelPosition() {
      return !!this.designer.formConfig && !!this.designer.formConfig.labelPosition
        ? this.designer.formConfig.labelPosition
        : 'left'
    },
    size() {
      return !!this.designer.formConfig && !!this.designer.formConfig.size ? this.designer.formConfig.size : 'default'
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
      deep: !0,
      handler(o) {}
    },
    'designer.formConfig': { deep: !0, handler(o) {} }
  },
  created() {
    this.designer.loadPresetCssCode(this.getDesignerConfig().presetCssCode)
  },
  mounted() {
    this.disableFirefoxDefaultDrop(), this.designer.registerFormWidget(this)
  },
  methods: {
    getWidgetName(o) {
      return o.type + '-widget'
    },
    disableFirefoxDefaultDrop() {
      navigator.userAgent.toLowerCase().indexOf('firefox') !== -1 &&
        (document.body.ondrop = function (e) {
          e.stopPropagation(), e.preventDefault()
        })
    },
    onDragEnd(o) {},
    onDragAdd(o) {
      const e = o.newIndex
      this.designer.widgetList[e] && this.designer.setSelected(this.designer.widgetList[e]),
        this.designer.emitHistoryChange(),
        this.designer.emitEvent('field-selected', null)
    },
    onDragUpdate() {
      this.designer.emitHistoryChange()
    },
    checkMove(o) {
      return this.designer.checkWidgetMove(o)
    },
    getFormData() {
      return this.formModel
    },
    getWidgetRef(o, e = !1) {
      let n = this.widgetRefList[o]
      return !n && !!e && this.$message.error(this.i18nt('designer.hint.refNotFound') + o), n
    },
    getSelectedWidgetRef() {
      let o = this.designer.selectedWidgetName
      return this.getWidgetRef(o)
    },
    clearWidgetRefList() {
      Object.keys(this.widgetRefList).forEach((o) => {
        delete this.widgetRefList[o]
      })
    },
    deleteWidgetRef(o, e) {
      e ? delete this.widgetRefList[o + '@sf' + e] : delete this.widgetRefList[o]
    },
    deletedChildrenRef(o) {
      o.forEach((e) => {
        delete this.widgetRefList[e]
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.form-widget-container {
  padding: 5px;
}
.no-widget-hint {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 64px);
  img {
    width: 129px;
  }
  // 提示
  .trip {
    font-weight: 400;
    font-size: 13px;
    color: #9e9e9e;
    margin-top: 13px;
  }
}
.vue-grid-item {
  transition: all 0.2s ease;
  transition-property: left, top, right;
}
.vue-grid-item.no-touch {
  -ms-touch-action: none;
  touch-action: none;
}
.vue-grid-item.cssTransforms {
  transition-property: transform;
  left: 0;
  right: auto;
}
.vue-grid-item.cssTransforms.render-rtl {
  left: auto;
  right: 0;
}
.vue-grid-item.resizing {
  opacity: 0.6;
  z-index: 3;
}
.vue-grid-item.vue-draggable-dragging {
  transition: none;
  z-index: 3;
}
.vue-grid-item.vue-grid-placeholder {
  background: red;
  opacity: 0.2;
  transition-duration: 0.1s;
  z-index: 2;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  -o-user-select: none;
  user-select: none;
}
.vue-grid-item > .vue-resizable-handle {
  position: absolute;
  width: 20px;
  height: 20px;
  bottom: 0;
  right: 0;
  background: url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBzdGFuZGFsb25lPSJubyI/Pg08IS0tIEdlbmVyYXRvcjogQWRvYmUgRmlyZXdvcmtzIENTNiwgRXhwb3J0IFNWRyBFeHRlbnNpb24gYnkgQWFyb24gQmVhbGwgKGh0dHA6Ly9maXJld29ya3MuYWJlYWxsLmNvbSkgLiBWZXJzaW9uOiAwLjYuMSAgLS0+DTwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4xLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL0dyYXBoaWNzL1NWRy8xLjEvRFREL3N2ZzExLmR0ZCI+DTxzdmcgaWQ9IlVudGl0bGVkLVBhZ2UlMjAxIiB2aWV3Qm94PSIwIDAgNiA2IiBzdHlsZT0iYmFja2dyb3VuZC1jb2xvcjojZmZmZmZmMDAiIHZlcnNpb249IjEuMSINCXhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiIHhtbDpzcGFjZT0icHJlc2VydmUiDQl4PSIwcHgiIHk9IjBweCIgd2lkdGg9IjZweCIgaGVpZ2h0PSI2cHgiDT4NCTxnIG9wYWNpdHk9IjAuMzAyIj4NCQk8cGF0aCBkPSJNIDYgNiBMIDAgNiBMIDAgNC4yIEwgNCA0LjIgTCA0LjIgNC4yIEwgNC4yIDAgTCA2IDAgTCA2IDYgTCA2IDYgWiIgZmlsbD0iIzAwMDAwMCIvPg0JPC9nPg08L3N2Zz4=);
  background-position: bottom right;
  padding: 0 3px 3px 0;
  background-repeat: no-repeat;
  background-origin: content-box;
  box-sizing: border-box;
  cursor: se-resize;
}
.vue-grid-item > .vue-rtl-resizable-handle {
  bottom: 0;
  left: 0;
  background: url(data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAuMDAwMDAwMDAwMDAwMDAyIiBoZWlnaHQ9IjEwLjAwMDAwMDAwMDAwMDAwMiIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KIDwhLS0gQ3JlYXRlZCB3aXRoIE1ldGhvZCBEcmF3IC0gaHR0cDovL2dpdGh1Yi5jb20vZHVvcGl4ZWwvTWV0aG9kLURyYXcvIC0tPgogPGc+CiAgPHRpdGxlPmJhY2tncm91bmQ8L3RpdGxlPgogIDxyZWN0IGZpbGw9Im5vbmUiIGlkPSJjYW52YXNfYmFja2dyb3VuZCIgaGVpZ2h0PSIxMiIgd2lkdGg9IjEyIiB5PSItMSIgeD0iLTEiLz4KICA8ZyBkaXNwbGF5PSJub25lIiBvdmVyZmxvdz0idmlzaWJsZSIgeT0iMCIgeD0iMCIgaGVpZ2h0PSIxMDAlIiB3aWR0aD0iMTAwJSIgaWQ9ImNhbnZhc0dyaWQiPgogICA8cmVjdCBmaWxsPSJ1cmwoI2dyaWRwYXR0ZXJuKSIgc3Ryb2tlLXdpZHRoPSIwIiB5PSIwIiB4PSIwIiBoZWlnaHQ9IjEwMCUiIHdpZHRoPSIxMDAlIi8+CiAgPC9nPgogPC9nPgogPGc+CiAgPHRpdGxlPkxheWVyIDE8L3RpdGxlPgogIDxsaW5lIGNhbnZhcz0iI2ZmZmZmZiIgY2FudmFzLW9wYWNpdHk9IjEiIHN0cm9rZS1saW5lY2FwPSJ1bmRlZmluZWQiIHN0cm9rZS1saW5lam9pbj0idW5kZWZpbmVkIiBpZD0ic3ZnXzEiIHkyPSItNzAuMTc4NDA3IiB4Mj0iMTI0LjQ2NDE3NSIgeTE9Ii0zOC4zOTI3MzciIHgxPSIxNDQuODIxMjg5IiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlPSIjMDAwIiBmaWxsPSJub25lIi8+CiAgPGxpbmUgc3Ryb2tlPSIjNjY2NjY2IiBzdHJva2UtbGluZWNhcD0idW5kZWZpbmVkIiBzdHJva2UtbGluZWpvaW49InVuZGVmaW5lZCIgaWQ9InN2Z181IiB5Mj0iOS4xMDY5NTciIHgyPSIwLjk0NzI0NyIgeTE9Ii0wLjAxODEyOCIgeDE9IjAuOTQ3MjQ3IiBzdHJva2Utd2lkdGg9IjIiIGZpbGw9Im5vbmUiLz4KICA8bGluZSBzdHJva2UtbGluZWNhcD0idW5kZWZpbmVkIiBzdHJva2UtbGluZWpvaW49InVuZGVmaW5lZCIgaWQ9InN2Z183IiB5Mj0iOSIgeDI9IjEwLjA3MzUyOSIgeTE9IjkiIHgxPSItMC42NTU2NCIgc3Ryb2tlLXdpZHRoPSIyIiBzdHJva2U9IiM2NjY2NjYiIGZpbGw9Im5vbmUiLz4KIDwvZz4KPC9zdmc+);
  background-position: bottom left;
  padding-left: 3px;
  background-repeat: no-repeat;
  background-origin: content-box;
  cursor: sw-resize;
  right: auto;
}
.vue-grid-item.disable-userselect {
  user-select: none;
}
.vue-grid-layout {
  position: relative;
  transition: height 0.2s ease;
}
.vue-grid-layout {
  background: transparent;
}
.vue-grid-layout .smartwidget {
  height: inherit;
  width: inherit;
}
.vue-grid-layout .smartwidget.smartwidget-fullscreen {
  height: 100%;
  width: 100%;
}
.smart-widget__loading-mask {
  position: absolute;
  z-index: 2000;
  background-color: #ffffffe6;
  margin: 0;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  -webkit-transition: opacity 0.3s;
  transition: opacity 0.3s;
}
.smart-widget__loading-mask .loading-spinner {
  top: 50%;
  margin-top: -21px;
  width: 100%;
  text-align: center;
  position: absolute;
}
.smart-widget__loading-mask .circular {
  width: 42px;
  height: 42px;
  animation: loading-rotate 2s linear infinite;
}
.smart-widget__loading-mask .path {
  animation: loading-dash 1.5s ease-in-out infinite;
  stroke-dasharray: 90, 150;
  stroke-dashoffset: 0;
  stroke-width: 2;
  stroke: #5282e4;
  stroke-linecap: round;
}
@keyframes loading-rotate {
  to {
    transform: rotate(360deg);
  }
}
@keyframes loading-dash {
  0% {
    stroke-dasharray: 1, 200;
    stroke-dashoffset: 0;
  }
  50% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -40px;
  }
  to {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -120px;
  }
}
.collapse-transition[data-v-5f8fde58] {
  transition: 0.3s height ease-in-out, 0.3s padding-top ease-in-out, 0.3s padding-bottom ease-in-out;
}
.vue-grid-item {
  touch-action: none;
  box-sizing: border-box;
}
.vue-grid-item.vue-grid-placeholder {
  background: #7cbeff;
  opacity: 0.2;
  transition-duration: 0.1s;
  z-index: 2;
  user-select: none;
}
body.no-overflow {
  overflow: hidden;
  position: fixed;
  width: 100%;
}
.smartwidget {
  box-sizing: border-box;
  background: #fff;
  box-shadow: 0 1px 2px #0000000d;
  border: 1px solid #ebeef5;
  width: 100%;
  transition: 0.3s;
}
.smartwidget.is-always-shadow {
  box-shadow: 0 0 10px #e9e9e9;
}
.smartwidget.is-hover-shadow:hover {
  box-shadow: 0 0 10px #e9e9e9;
}
.smartwidget.is-never-shadow {
  box-shadow: 0 1px 2px #0000000d;
}
.smartwidget .widget-header {
  display: flex;
  border-bottom: 1px solid #ebeef5;
}
.smartwidget .widget-header .widget-header__title {
  display: inline-block;
  position: relative;
  width: auto;
  margin: 0;
  font-weight: normal;
  letter-spacing: 0;
  align-items: center;
  font-weight: 600 !important;
  font-size: 16px;
}

.smartwidget .widget-header .widget-header__subtitle {
  font-size: 12px;
  color: #777;
  margin-left: 10px;
}
.smartwidget .widget-header .ellis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.smartwidget .widget-header .widget-header__prefix {
  background: #0076db;
  width: 2px;
  height: 16px;
  margin-left: 10px;
}
.smartwidget .widget-header .widget-header__toolbar {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  flex: 1;
  padding: 0;
  margin: 0;
}
::v-deep .smartwidget .widget-header .widget-header__toolbar a {
  display: inline-block;
  text-decoration: none;
  text-align: center;
  height: 24px;
  line-height: 28px;
  padding: 0;
  margin: 0;
  color: #333;
  min-width: 35px;
  position: relative;
  font-family: Arial, Helvetica, sans-serif;
  border: none !important;
  border-left: none !important;
}
.smartwidget .widget-body-simple {
  display: flex;
  height: inherit;
  width: inherit;
}
.smartwidget .widget-body-simple .widget-body__content {
  width: 100%;
}
.smartwidget .widget-body {
  display: flex;
  flex-direction: column;
  will-change: height;
  position: relative;
  overflow: hidden;
}
.smartwidget .widget-body .widget-body__content {
  flex: 1;
}
.smartwidget .widget-body .widget-body__content.fixed-height {
  overflow-y: scroll;
}
.smartwidget .widget-body .widget-body__footer {
  position: relative;
}
.smartwidget .widget-body .widget-body__footer.has-group {
  left: 0;
  bottom: 0;
  width: 100%;
}
.smartwidget .widget-body.is-collapse {
  transition: 0.3s height ease-in-out, 0.3s padding-top ease-in-out, 0.3s padding-bottom ease-in-out;
}
.smartwidget.smartwidget-fullscreen {
  position: fixed;
  height: 100%;
  width: 100%;
  top: 0;
  left: 0;
  z-index: 6666;
}
.smartwidget.smartwidget-fullscreen .widget-header {
  cursor: default;
}
.smartwidget svg.sw-loading {
  animation: rotating 2s linear infinite;
  cursor: not-allowed;
}
.form-widget-canvas {
  height: 100%;
  box-sizing: border-box;
}
</style>
