<template>
  <div class="baseFrom">
    <el-form
      ref="elFormRef"
      :rules="rules"
      :model="data"
      :scroll-to-error="true"
      :validate-on-rule-change="false"
      v-bind="elFormConfig"
    >
      <el-row v-bind="rowConfig">
        <template v-for="item in formItems">
          <el-col
            :span="24"
            v-if="hasSlot(`${item.field}Header`) && !isHiddenItem(item)"
          >
            <slot
              :name="`${item.field}Header`"
              :backData="{ item, data: data[`${item.field}`] }"
            ></slot>
          </el-col>
          <el-col
            :key="item.field"
            v-bind="getLayout(item, colLayout)"
            :class="`${item.field}Col`"
            v-if="!isHiddenItem(item)"
          >
            <el-form-item
              class="form-item"
              :class="`${item.field}Class`"
              v-show="!item.isHidden"
              :label="item.hideLabel ? '' : item.label"
              :style="itemStyle"
              :prop="item.field"
              v-bind="item.formItemConfig"
            >
              <template #label="{ label }" v-if="!item.hideLabel">
                <slot :name="item.field + 'CustomLabel'" :backData="item">
                  <el-tooltip
                    v-if="item.tip"
                    :content="item.tip"
                    v-bind="item.tipConfig"
                  >
                    <el-icon><question-filled /></el-icon>
                  </el-tooltip>
                  <span>{{ label }}</span>
                </slot>
              </template>
              <slot
                :name="`${item.field}Before`"
                :backData="{ item, data: data[`${item.field}`] }"
              ></slot>
              <template v-if="item.type">
                <component
                  :ref="(el) => setItemRef(el, item.field)"
                  :is="item.type.toUpperCase()"
                  :item="item"
                  :allDisabled="allDisabled"
                  :value="data[`${item.field}`]"
                  @update:value="handleValueChange($event, item.field)"
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
                    >
                    </slot>
                  </template>
                  <template v-if="item.type.toUpperCase() === 'CUSTOM'" #custom>
                    <slot
                      :name="`${item.field}Custom`"
                      :backData="{
                        item: item,
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
              ></slot>
            </el-form-item>
          </el-col>
        </template>
        <el-col
          v-bind="footerLayout"
          v-if="$slots['footer']"
          :style="itemStyle"
        >
          <div class="footer">
            <slot name="footer"></slot>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script setup>
import { useSlots, ref } from 'vue'
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
} from './cpn/index'
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
  },
})
const props = defineProps({
  // el-from的配置
  elFormConfig: {
    type: Object,
    default: () => {},
  },
  // 是否全部禁用
  allDisabled: {
    type: Boolean,
    default: false,
  },
  // 是一个数组对象，里面是el-form-item配置
  formItems: {
    type: Array,
    default: () => [],
  },
  // 数据
  data: {
    type: Object,
    required: true,
  },
  // el-form-item的style
  itemStyle: {
    type: Object,
    default: () => ({
      padding: '0px 20px 0px 0px',
    }),
  },
  // 布局适配
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
  // 表单正则校验
  rules: {
    type: Object,
    default: () => ({}),
  },
  rowConfig: {
    type: Object,
    default: () => {
      return {}
    },
  },
  hideItems: {
    type: [Array, Object],
    default: () => [],
  },
})
const emits = defineEmits(['update:data', 'keyUpEnter'])
const slots = useSlots()
let elFormRef = ref(null)
const allRefs = ref({})
const setItemRef = (el, type) => {
  if (el && el.getRef) {
    allRefs.value[type] = el.getRef()
  }
}
const handleValueChange = (value, field) => {
  emits('update:data', { ...props.data, [field]: value })
}
let getFormValidate = async () => {
  return elFormRef.value.validate((valid) => {
    return valid
  })
}

const capitalizeFirstLetter = (str) => {
  return str.charAt(0).toUpperCase() + str.slice(1)
}

const isHiddenItem = (item) => {
  let flag = false
  if (isRef(props.hideItems)) {
    if (props.hideItems.value.includes(item.field)) {
      flag = true
    }
  }
  return flag
}
const keyUpEnter = ($event, current) => {
  emits('keyUpEnter', {
    event: $event,
    current,
  })
}
const hasSlot = (slotName) => {
  return slots[slotName] ? true : false
}

defineExpose({
  getFormValidate,
  allRefs,
  elFormRef,
})
</script>

<style scoped lang="scss">
.baseFrom {
  :deep(.el-form-item__label) {
    margin: 0px !important;
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
    margin: 9px 0;
  }
}
.footer {
  margin: 9px 0;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
</style>
