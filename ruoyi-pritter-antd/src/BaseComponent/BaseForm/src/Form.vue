<template>
  <div class="baseForm">
    <a-form
      ref="formRef"
      :model="data"
      :rules="rules"
      v-bind="mergedFormConfig"
      @submit.prevent
    >
      <a-row v-bind="rowConfig">
        <template v-for="item in formItems" :key="item.field">
          <a-col
            v-if="hasSlot(`${item.field}Header`) && !isHiddenItem(item)"
            :span="24"
          >
            <slot
              :name="`${item.field}Header`"
              :backData="{ item, data: data[`${item.field}`] }"
            />
          </a-col>
          <a-col
            v-if="!isHiddenItem(item)"
            v-bind="getLayout(item, colLayout)"
            :class="`${item.field}Col`"
          >
            <a-form-item
              class="form-item"
              :class="`${item.field}Class`"
              :name="item.field"
              :style="itemStyle"
              v-bind="item.formItemConfig"
            >
              <template v-if="!item.hideLabel" #label>
                <slot :name="item.field + 'CustomLabel'" :backData="item">
                  <a-tooltip
                    v-if="item.tip"
                    :title="item.tip"
                    v-bind="item.tipConfig"
                  >
                    <QuestionCircleOutlined class="base-form-tip-icon" />
                  </a-tooltip>
                  <span>{{ item.label }}</span>
                </slot>
              </template>
              <slot
                :name="`${item.field}Before`"
                :backData="{ item, data: data[`${item.field}`] }"
              />
              <template v-if="item.type">
                <component
                  :is="item.type.toUpperCase()"
                  :ref="(el) => setItemRef(el, item.field)"
                  :item="item"
                  :all-disabled="allDisabled"
                  v-model:value="data[`${item.field}`]"
                  @keyUpEnter="keyUpEnter($event, item)"
                >
                  <template
                    v-for="slotName in item.slotNames"
                    #[slotName]="slotData"
                  >
                    <slot
                      :name="`${item.field}` + capitalizeFirstLetter(slotName)"
                      :backData="{
                        ...slotData,
                        item,
                        dataValue: data[`${item.field}`],
                        formData: data,
                      }"
                    />
                  </template>
                  <template
                    v-for="slotName in item.optionSlots"
                    #[`${item.field}${capitalizeFirstLetter(slotName)}Option`]="slotData"
                  >
                    <slot
                      :name="`${item.field}${capitalizeFirstLetter(slotName)}Option`"
                      :backData="{
                        ...slotData,
                        item,
                        dataValue: data[`${item.field}`],
                        formData: data,
                      }"
                    />
                  </template>
                  <template v-if="item.type.toUpperCase() === 'CUSTOM'" #custom>
                    <slot
                      :name="`${item.field}Custom`"
                      :backData="{
                        item,
                        formData: data,
                        data: data[`${item.field}`],
                      }"
                    >
                      {{ data[`${item.field}`] }}
                    </slot>
                  </template>
                </component>
              </template>
              <slot
                :name="`${item.field}After`"
                :backData="{ item, data: data[`${item.field}`] }"
              />
            </a-form-item>
          </a-col>
        </template>
        <a-col v-if="$slots.footer" v-bind="footerLayout" :style="itemStyle">
          <div class="footer">
            <slot name="footer" />
          </div>
        </a-col>
      </a-row>
    </a-form>
  </div>
</template>

<script setup>
import { QuestionCircleOutlined } from '@ant-design/icons-vue'
import getLayout from './config/layout.js'
import {
  Input as INPUT,
  InputNumber as INPUTNUMBER,
  Textarea as TEXTAREA,
  Cascader as CASCADER,
  Custom as CUSTOM,
  Select as SELECT,
  Tree as TREE,
  TreeSelect as TREESELECT,
  Datepicker as DATEPICKER,
  CheckBox as CHECKBOX,
  Radio as RADIO,
  Switch as SWITCH,
  InputTag as INPUTTAG,
  Autocomplete as AUTOCOMPLETE,
} from './cpn/index'
import { capitalizeFirstLetter } from './utils/index.js'

defineOptions({
  components: {
    INPUT,
    INPUTNUMBER,
    TEXTAREA,
    CASCADER,
    CUSTOM,
    SELECT,
    TREE,
    TREESELECT,
    DATEPICKER,
    CHECKBOX,
    RADIO,
    SWITCH,
    INPUTTAG,
    AUTOCOMPLETE,
    SELECTV2: SELECT,
  },
})

const props = defineProps({
  formConfig: {
    type: Object,
    default: () => ({}),
  },
  allDisabled: {
    type: Boolean,
    default: false,
  },
  formItems: {
    type: Array,
    default: () => [],
  },
  data: {
    type: Object,
    required: true,
  },
  itemStyle: {
    type: Object,
    default: () => ({
      padding: '0px 20px 0px 0px',
    }),
  },
  colLayout: {
    type: Object,
  },
  footerLayout: {
    type: Object,
    default: () => ({
      xl: 3,
      lg: 4,
      md: 6,
      sm: 12,
      xs: 24,
    }),
  },
  rules: {
    type: Object,
    default: () => ({}),
  },
  rowConfig: {
    type: Object,
    default: () => ({}),
  },
  hideItems: {
    type: [Array, Object],
    default: () => [],
  },
})

const emits = defineEmits(['keyUpEnter'])
const slots = useSlots()
const formRef = useTemplateRef('formRef')
const allRefs = ref({})

const mergedFormConfig = computed(() => ({
  scrollToFirstError: true,
  ...props.formConfig,
}))

const setItemRef = (el, type) => {
  if (el?.getRef) {
    allRefs.value[type] = el.getRef()
  }
}

const getFormValidate = () => formRef.value?.validate()

const isHiddenItem = (item) => {
  if (item.isHidden) return true
  if (isRef(props.hideItems)) {
    return props.hideItems.value.includes(item.field)
  }
  if (Array.isArray(props.hideItems)) {
    return props.hideItems.includes(item.field)
  }
  return false
}

const keyUpEnter = ($event, current) => {
  emits('keyUpEnter', { event: $event, current })
}

const hasSlot = (slotName) => Boolean(slots[slotName])

defineExpose({
  getFormValidate,
  allRefs,
  formRef,
})
</script>

<style scoped lang="scss">
.baseForm {
  width: 100%;

  :deep(.ant-form-item-label > label) {
    margin: 0 !important;
    font-weight: 500;
  }

  :deep(.ant-select),
  :deep(.ant-cascader),
  :deep(.ant-tree-select),
  :deep(.ant-picker),
  :deep(.ant-input-number),
  :deep(.ant-auto-complete) {
    width: 100%;
  }

  :deep(.ant-form-item) {
    margin: 10px 0;
  }
}

.base-form-tip-icon {
  margin-right: 4px;
}

.footer {
  margin: 10px 0;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
</style>
