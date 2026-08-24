<template>
  <el-col
    class="grid-cell"
    :class="[customClass]"
    v-bind="layoutProps"
    :style="colHeightStyle"
    :key="widget.id"
    v-show="!widget.options.hidden"
  >
    <template v-if="!!widget.widgetList && widget.widgetList.length > 0">
      <template v-for="(subWidget, swIdx) in widget.widgetList">
        <template v-if="'container' === subWidget.category">
          <component
            :is="getComponentByContainer(subWidget)"
            :widget="subWidget"
            :key="swIdx"
            :developData="developData"
            :parent-list="widget.widgetList"
            :index-of-parent-list="swIdx"
            :parent-widget="widget"
          >
            <!-- 递归传递插槽！！！ -->
            <template v-for="slot in Object.keys($scopedSlots)" v-slot:[slot]="scope">
              <slot :name="slot" v-bind="scope" />
            </template>
          </component>
        </template>
        <template v-else>
          <component
            :is="subWidget.type + '-widget'"
            :field="{ ...developData, ...subWidget }"
            :developData="developData"
            :designer="null"
            :key="swIdx"
            :parent-list="widget.widgetList"
            :index-of-parent-list="swIdx"
            :parent-widget="widget"
          >
            <!-- 递归传递插槽！！！ -->
            <template v-for="slot in Object.keys($scopedSlots)" v-slot:[slot]="scope">
              <slot :name="slot" v-bind="scope" />
            </template>
          </component>
        </template>
      </template>
    </template>
    <template v-else>
      <el-col>
        <div class="blank-cell">
          <span class="invisible-content">{{ i18nt('render.hint.blankCellContent') }}</span>
        </div>
      </el-col>
    </template>
  </el-col>
</template>

<script>
import emitter from '@/utils/emitter'
import i18n from '../../../utils/i18n'
import refMixin from '../../../components/form-render/refMixin'
import FieldComponents from '@/components/form-designer/form-widget/field-widget/index'

export default {
  name: 'GridColItem',
  componentName: 'ContainerItem',
  mixins: [emitter, i18n, refMixin],
  components: {
    ...FieldComponents
  },
  props: {
    widget: Object,
    parentWidget: Object,
    developData: Object,
    parentList: Array,
    indexOfParentList: Number,
    colHeight: {
      type: String,
      default: null
    }
  },
  inject: ['refList', 'globalModel', 'formConfig', 'previewState'],
  data() {
    return {
      layoutProps: {
        span: Number(this.widget.options.span),
        md: Number(this.widget.options.md) || 12,
        sm: Number(this.widget.options.sm) || 12,
        xs: Number(this.widget.options.xs) || 12,
        offset: Number(this.widget.options.offset) || 0,
        push: Number(this.widget.options.push) || 0,
        pull: Number(this.widget.options.pull) || 0
      }
    }
  },
  watch: {
    '$store.state.user.activeField': {
      deep: true,
      handler: function (newVal, oldVal) {
        this.fieldShowFn(newVal)
      }
    }
  },

  computed: {
    customClass() {
      return this.widget.options.customClass || ''
    },

    colHeightStyle() {
      return !!this.colHeight ? { height: this.colHeight + 'px' } : {}
    }
  },
  created() {
    this.initLayoutProps()
    this.initRefList()
  },
  methods: {
    fieldShowFn(val) {
      this.widget.widgetList.forEach((item) => {
        if (item.id == val.id) {
          item.isShow = true
        } else {
          item.isShow = false
        }
      })
    },
    initLayoutProps() {
      if (!!this.widget.options.responsive) {
        if (!!this.previewState) {
          this.layoutProps.md = undefined
          this.layoutProps.sm = undefined
          this.layoutProps.xs = undefined

          let lyType = this.formConfig.layoutType
          if (lyType === 'H5') {
            this.layoutProps.span = this.widget.options.xs || 12
          } else if (lyType === 'Pad') {
            this.layoutProps.span = this.widget.options.sm || 12
          } else {
            this.layoutProps.span = this.widget.options.md || 12
          }
        } else {
          this.layoutProps.span = undefined
        }
      } else {
        this.layoutProps.md = undefined
        this.layoutProps.sm = undefined
        this.layoutProps.xs = undefined
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.blank-cell {
  font-style: italic;
  color: #cccccc;

  span.invisible-content {
    opacity: 0;
  }
}
</style>
../../../utils/i18ns
