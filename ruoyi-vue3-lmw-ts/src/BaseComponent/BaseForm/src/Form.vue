<template>
  <div class="baseForm">
    <el-form
      ref="elFormRef"
      :rules="rules"
      :model="data"
      @submit.prevent
      v-bind="mergedFormConfig"
    >
      <el-row v-bind="rowConfig">
        <template v-for="schema in renderSchemas" :key="schema.field">
          <el-col :span="24" v-if="hasSlot(`${schema.field}Header`)">
            <slot
              :name="`${schema.field}Header`"
              :backData="{ item: schema, data: data[schema.field] }"
            ></slot>
          </el-col>

          <el-col
            v-bind="resolveItemLayout(schema)"
            :class="`${schema.field}Col`"
          >
            <el-form-item
              class="form-item"
              :class="`${schema.field}Class`"
              :label="schema.hideLabel ? '' : schema.label"
              :style="itemStyle"
              :prop="schema.field"
              :for="
                normalizeType(schema.type) === 'custom'
                  ? undefined
                  : getFieldId(schema.field)
              "
              v-bind="schema.formItemConfig"
            >
              <template #label="{ label }" v-if="!schema.hideLabel">
                <slot :name="schema.field + 'CustomLabel'" :backData="schema">
                  <el-tooltip
                    v-if="schema.tip"
                    :content="schema.tip"
                    v-bind="schema.tipConfig"
                  >
                    <el-icon><QuestionFilled /></el-icon>
                  </el-tooltip>
                  <span>{{ label }}</span>
                </slot>
              </template>

              <slot
                :name="`${schema.field}Before`"
                :backData="{ item: schema, data: data[schema.field] }"
              ></slot>

              <component
                :ref="
                  (target: Element | ComponentPublicInstance | null) =>
                    assignItemRef(target, schema.field)
                "
                :is="resolveFieldComponent(schema.type)"
                :item="schema"
                :allDisabled="allDisabled"
                :input-id="getFieldId(schema.field)"
                v-model:value="data[schema.field]"
                @keyUpEnter="emitEnter($event, schema)"
              >
                <template
                  v-for="slotName in schema.slotNames"
                  #[slotName]="slotData"
                >
                  <slot
                    :name="`${schema.field}${upperFirst(slotName)}`"
                    :backData="{
                      ...slotData,
                      item: schema,
                      dataValue: data[schema.field],
                      formData: data,
                    }"
                  ></slot>
                </template>

                <template
                  v-if="normalizeType(schema.type) === 'custom'"
                  #custom
                >
                  <slot
                    :name="`${schema.field}Custom`"
                    :backData="{
                      item: schema,
                      formData: data,
                      data: data[schema.field],
                    }"
                  >
                    {{ data[schema.field] }}
                  </slot>
                </template>
              </component>

              <slot
                :name="`${schema.field}After`"
                :backData="{ item: schema, data: data[schema.field] }"
              ></slot>
            </el-form-item>
          </el-col>
        </template>

        <el-col v-bind="footerLayout" v-if="$slots.footer" :style="itemStyle">
          <div class="footer">
            <slot name="footer"></slot>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import {
  computed,
  isRef,
  ref,
  type Component,
  type ComponentPublicInstance,
} from 'vue'
import type { FormInstance } from 'element-plus'
import { QuestionFilled } from '@element-plus/icons-vue'

import getLayout from './config/layout.ts'
import type {
  AllRefs,
  FormComponentInstance,
  FormItem,
  KeyUpEnterEvent,
  Props,
} from './types/index.ts'
import {
  Cascader,
  CheckBox,
  Custom,
  Datepicker,
  Input,
  InputNumber,
  Radio,
  Select,
  Textarea,
  Tree,
  TreeSelect,
  SelectV2,
  InputTag,
  Autocomplete,
} from './cpn/index.ts'

const {
  elFormConfig = {},
  allDisabled = false,
  formItems = [],
  data = {},
  itemStyle = { padding: '0px 20px 0px 0px' },
  colLayout,
  footerLayout = {
    xl: 3,
    lg: 4,
    md: 6,
    sm: 12,
    xs: 24,
  },
  rules = {},
  rowConfig = {},
  hideItems = [],
} = defineProps<Props>()

const emits = defineEmits<{
  keyUpEnter: [event: KeyUpEnterEvent]
}>()

type FormType = FormItem['type']

const DEFAULT_FORM_CONFIG = {
  scrollToError: true,
  validateOnRuleChange: false,
} as const

const componentMap: Record<FormType, Component> = {
  input: Input,
  inputNumber: InputNumber,
  textarea: Textarea,
  cascader: Cascader,
  custom: Custom,
  select: Select,
  tree: Tree,
  treeSelect: TreeSelect,
  datepicker: Datepicker,
  checkBox: CheckBox,
  radio: Radio,
  selectV2: SelectV2,
  inputTag: InputTag,
  autocomplete: Autocomplete,
}

/** 运行时 type 别名（字符串大小写不一致时归一）；避免每次 normalize 重建对象 */
const TYPE_ALIAS_MAP: Record<string, FormType> = {
  input: 'input',
  inputnumber: 'inputNumber',
  textarea: 'textarea',
  cascader: 'cascader',
  custom: 'custom',
  select: 'select',
  tree: 'tree',
  treeselect: 'treeSelect',
  datepicker: 'datepicker',
  checkbox: 'checkBox',
  radio: 'radio',
  selectv2: 'selectV2',
  inputtag: 'inputTag',
  autocomplete: 'autocomplete',
}

const normalizeType = (type: string): FormType =>
  TYPE_ALIAS_MAP[type.trim().toLowerCase()] ?? 'custom'

const resolveFieldComponent = (type: FormType | string): Component =>
  componentMap[normalizeType(type)] ?? Custom

const mergedFormConfig = computed(() => ({
  ...DEFAULT_FORM_CONFIG,
  ...(elFormConfig && typeof elFormConfig === 'object' ? elFormConfig : {}),
}))

const hiddenFields = computed<Set<string>>(() => {
  if (isRef(hideItems)) return new Set(hideItems.value)
  if (Array.isArray(hideItems)) return new Set(hideItems)
  return new Set<string>()
})

const isHiddenItem = (item: FormItem): boolean =>
  Boolean(item.isHidden) || hiddenFields.value.has(item.field)

const renderSchemas = computed(() =>
  formItems.filter((item) => !isHiddenItem(item))
)

const slots = defineSlots()
const hasSlot = (slotName: string): boolean => Boolean(slots[slotName])

const upperFirst = (str: string): string =>
  str.charAt(0).toUpperCase() + str.slice(1)
const getFieldId = (field: string): string => `base-form-${field}`

const elFormRef = ref<FormInstance>()
const allRefs = ref<AllRefs>({})

const isFormComponentInstance = (
  target: unknown
): target is FormComponentInstance => {
  return Boolean(
    target &&
      typeof target === 'object' &&
      'getRef' in target &&
      typeof (target as FormComponentInstance).getRef === 'function'
  )
}

const assignItemRef = (
  target: Element | ComponentPublicInstance | null,
  field: string
) => {
  if (isFormComponentInstance(target)) {
    allRefs.value[field] = target.getRef?.() ?? null
    return
  }
  if (target === null) {
    delete allRefs.value[field]
  }
}

const emitEnter = (event: KeyboardEvent, current: FormItem) => {
  emits('keyUpEnter', { event, current })
}

const resolveItemLayout = (item: FormItem) =>
  getLayout(
    {
      ...item,
      type: normalizeType(item.type),
    } as FormItem,
    colLayout
  )

const getFormValidate = async (): Promise<boolean> => {
  if (!elFormRef.value) return false
  try {
    await elFormRef.value.validate()
    return true
  } catch {
    return false
  }
}

defineExpose({
  getFormValidate,
  allRefs,
  elFormRef,
})
</script>

<style scoped lang="scss">
.baseForm {
  :deep(.el-form-item__label) {
    margin: 0 !important;
    font-weight: 500;
    color: var(--el-text-color-primary);
  }
  :deep(.el-cascader) {
    width: 100%;
  }
  :deep(.el-select) {
    width: 100%;
  }
  :deep(.el-form-item__content) {
    width: 100%;
  }
  :deep(.el-date-editor) {
    width: 100%;
  }
  :deep(.el-input__clear) {
    position: absolute;
    right: 10px;
    top: 50%;
    transform: translateY(-50%);
  }
  :deep(.el-form-item) {
    margin: 10px 0;
  }
}

.footer {
  margin: 10px 0;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
</style>
