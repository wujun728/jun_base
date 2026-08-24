<template>
  <container-item-wrapper :widget="widget">
    <el-card
      :key="widget.id"
      class="card-container"
      :class="[!!widget.options.folded ? 'folded' : '', customClass]"
      :shadow="widget.options.shadow"
      :style="{
        width: widget.options.cardWidth + '!important' || '',
        marginTop: widget.options.topMargin + 20 + 'px !important' || '',
        marginBottom: widget.options.bottomMargin + 'px !important' || ''
      }"
      :ref="widget.id"
      v-show="!widget.options.hidden"
    >
      <div slot="header" class="clear-fix" v-if="widget.options.cardHeader">
        <span class="title">{{ widget.options.label }}</span>
        <i
          v-if="widget.options.showFold"
          class="float-right"
          :class="[!widget.options.folded ? 'el-icon-arrow-down' : 'el-icon-arrow-up']"
          @click="toggleCard"
        ></i>
      </div>
      <template v-if="!!widget.widgetList && widget.widgetList.length > 0">
        <template v-for="(subWidget, swIdx) in widget.widgetList">
          <template v-if="'container' === subWidget.category">
            <component
              :is="getComponentByContainer(subWidget)"
              :widget="subWidget"
              :developData="developData"
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
          <template v-else>
            <component
              :is="subWidget.type + '-widget'"
              :field="subWidget"
              :designer="null"
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
        </template>
      </template>
    </el-card>
  </container-item-wrapper>
</template>

<script>
import emitter from '@/utils/emitter'
import i18n from '@/utils/i18n'
import refMixin from '@/components/form-render/refMixin'
import ContainerItemWrapper from '@/components/form-render/container-item/container-item-wrapper'
import containerItemMixin from '@/components/form-render/container-item/containerItemMixin'
import FieldComponents from '@/components/form-designer/form-widget/field-widget/index'

export default {
  name: 'card-item',
  componentName: 'ContainerItem',
  mixins: [emitter, i18n, refMixin, containerItemMixin],
  components: {
    ContainerItemWrapper,
    ...FieldComponents
  },
  props: {
    widget: Object,
    developData: Object
  },
  inject: ['refList', 'sfRefList', 'globalModel'],
  computed: {
    customClass() {
      return this.widget.options.customClass || ''
    }
  },
  created() {
    this.initRefList()
  },
  beforeDestroy() {
    this.unregisterFromRefList()
  },
  watch: {
    '$store.state.user.activeField': {
      deep: true,
      handler: function (newVal, oldVal) {
        this.fieldShowFn(newVal)
      }
    }
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
    toggleCard() {
      this.widget.options.folded = !this.widget.options.folded
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-card__header {
  padding: 10px 12px;
  border: none;
}
::v-deep .el-card {
  border: none;
}
.title {
  position: relative;
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.85);
}
.title:before {
  content: '';
  background-color: #1890ff;
  width: 3px;
  height: 14px;
  position: absolute;
  left: -10px;
  top: 50%;
  margin-top: -7px;
}

.folded ::v-deep .el-card__body {
  display: none;
}

.clear-fix:before,
.clear-fix:after {
  display: table;
  content: '';
}

.clear-fix:after {
  clear: both;
}

.float-right {
  float: right;
}
</style>
