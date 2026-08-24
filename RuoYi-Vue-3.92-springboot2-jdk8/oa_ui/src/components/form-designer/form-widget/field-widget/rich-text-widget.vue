<template>
  <form-item-wrapper
    :designer="designer"
    :field="field"
    :rules="rules"
    :design-state="designState"
    :parent-widget="parentWidget"
    :parent-list="parentList"
    :index-of-parent-list="indexOfParentList"
    :sub-form-row-index="subFormRowIndex"
    :sub-form-col-index="subFormColIndex"
    :sub-form-row-id="subFormRowId"
  >
    <div v-if="getReadMode() && !isShow" class="iconfont iconbianji3" @click="isShow = true"></div>
    <ueditorFrom
      v-if="!getReadMode() || isShow"
      ref="ueditorFrom"
      :height="`400px`"
      :type="`simple`"
      :content="fieldModel"
      @input="ueditorEdit"
      :disabled="field.options.disabled"
      :placeholder="field.options.placeholder"
    >
    </ueditorFrom>
    <div v-if="getReadMode() && isShow" class="mt14">
      <el-button size="small" @click="isShow = false">取消</el-button>
      <el-button size="small" type="primary" @click="handlePopoverHideFn">保存</el-button>
    </div>

    <template v-if="getReadMode() && !isShow">
      <div class="readonly-mode-field h-400" @click="isShow = true" v-html="contentForReadMode"></div>
    </template>
  </form-item-wrapper>
</template>

<script>
import FormItemWrapper from './form-item-wrapper'
import emitter from '@/utils/emitter'
import i18n, { translate } from '@/utils/i18n'
import fieldMixin from '@/components/form-designer/form-widget/field-widget/fieldMixin'
import ueditorFrom from '@/components/form-common/oa-wangeditor'

export default {
  name: 'rich-text-widget',
  componentName: 'FieldWidget', //必须固定为FieldWidget，用于接收父级组件的broadcast事件
  mixins: [emitter, fieldMixin, i18n],
  props: {
    field: Object,
    parentWidget: Object,
    parentList: Array,
    indexOfParentList: Number,
    designer: Object,

    designState: {
      type: Boolean,
      default: false
    },

    subFormRowIndex: {
      /* 子表单组件行索引，从0开始计数 */ type: Number,
      default: -1
    },
    subFormColIndex: {
      /* 子表单组件列索引，从0开始计数 */ type: Number,
      default: -1
    },
    subFormRowId: {
      /* 子表单组件行Id，唯一id且不可变 */ type: String,
      default: ''
    }
  },
  components: {
    FormItemWrapper,
    ueditorFrom
  },
  inject: ['refList', 'formConfig', 'globalOptionData', 'globalModel'],
  data() {
    return {
      oldFieldValue: null, //field组件change之前的值
      fieldModel: null,
      isShow: false,
      rules: []
    }
  },
  computed: {
    contentForReadMode() {
      return this.fieldModel ? this.fieldModel : '--'
    }
  },
  beforeCreate() {
    /* 这里不能访问方法和属性！！ */
  },

  created() {
    /* 注意：子组件mounted在父组件created之后、父组件mounted之前触发，故子组件mounted需要用到的prop
           需要在父组件created中初始化！！ */
    this.initFieldModel()
    this.registerToRefList()
    this.initEventHandler()
    this.buildFieldRules()
    this.handleOnCreated()
  },

  mounted() {
    this.handleOnMounted()
  },

  methods: {
    ueditorEdit(val) {
      this.fieldModel = val
      this.handleChangeEvent(this.fieldModel)
    },
    handlePopoverHideFn() {
      if (this.getReadMode() && this.isShow) {
        this.isShow = false
        this.putUpdateField()
      }
    }
  },

  beforeDestroy() {
    this.unregisterFromRefList()
  }
}
</script>

<style lang="scss" scoped>
@import '../../../../styles/global.scss'; //* form-item-wrapper已引入，还需要重复引入吗？ *//
img {
  width: 80% !important;
}
.h-400 {
  height: 400px;
  overflow-y: auto;
}
.readonly-mode-field {
  padding-top: 10px;
  ::v-deep p {
    img {
      width: 80% !important;
    }
  }
}
</style>
@/utils/i18ns
