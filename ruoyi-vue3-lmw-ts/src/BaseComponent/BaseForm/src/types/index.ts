import type { ColProps, FormItemProps, FormRules } from 'element-plus'
import type { CSSProperties, Ref } from 'vue'

export type { CSSProperties } from 'vue'

/* -------------------------------------------------------------------------- */
/* 基础取值 / 选项（原 shared/option.ts、shared/form.ts 中的类型归并于此）      */
/* -------------------------------------------------------------------------- */

export type PrimitiveValue = string | number | boolean

export type ObjectValue = Record<string, unknown>

/** 与 Select / TreeSelect / SelectV2 等绑定的值 */
export type SelectLikeValue =
  | PrimitiveValue
  | ObjectValue
  | Array<PrimitiveValue | ObjectValue>
  | undefined

export type InputLikeValue = string | number | null | undefined

export type NumericValue = number | undefined

export type TextValue = string

export type SelectValue = SelectLikeValue

/** option.ts 内部辅助：选项行 */
export type OptionRecord = Record<string, unknown>

export type OptionItem = OptionRecord & {
  label?: string | number
  value?: unknown
  key?: string | number
  children?: OptionItem[]
  [key: string]: unknown
}

export type Permission = string | string[]

/** 透传到 `v-on` 的事件表（值可为 undefined，便于表单配置里按需注入监听） */
export type EventFunction = Record<string, Function | undefined>

/** `el-option` 等 option 级 `v-on` */
export type OptionFunction = Record<string, Function | undefined>

export type FormItemType =
  | 'input'
  | 'inputNumber'
  | 'textarea'
  | 'cascader'
  | 'custom'
  | 'select'
  | 'tree'
  | 'treeSelect'
  | 'datepicker'
  | 'checkBox'
  | 'radio'
  | 'selectV2'
  | 'inputTag'
  | 'autocomplete'

/** 各 type 对应的 v-model 取值（用于由 schema 推导表单 model） */
export type ValueForFormType<T extends FormItemType> = T extends 'input'
  ? InputLikeValue
  : T extends 'inputNumber'
    ? NumericValue
    : T extends 'textarea'
      ? TextValue
      : T extends 'autocomplete'
        ? TextValue
        : T extends 'inputTag'
          ? string[]
          : T extends 'select' | 'selectV2' | 'treeSelect'
            ? SelectValue
            : T extends 'datepicker'
              ? string | string[] | Date | Date[]
              : T extends 'checkBox'
                ? PrimitiveValue | PrimitiveValue[] | undefined
                : T extends 'radio'
                  ? PrimitiveValue | undefined
                  : T extends 'cascader' | 'tree' | 'custom'
                    ? unknown
                    : never

type FormItemBase = {
  field: string
  label?: string
  hideLabel?: boolean
  tip?: string
  tipConfig?: Record<string, unknown>
  formItemConfig?: Partial<FormItemProps>
  layout?: Partial<ColProps>
  isHidden?: boolean | Ref<boolean>
  slotNames?: string[]
  eventFunction?: EventFunction
  optionFunction?: OptionFunction
  permission?: Permission
  options?: OptionItem[] | Ref<OptionItem[]>
  setLabel?: string
  setValue?: string
  isGroup?: boolean
  optionConfig?: Record<string, unknown>
}

export type InputFormItem = FormItemBase & {
  type: 'input'
  config?: Record<string, unknown>
}

export type InputNumberFormItem = FormItemBase & {
  type: 'inputNumber'
  config?: Record<string, unknown>
}

export type TextareaFormItem = FormItemBase & {
  type: 'textarea'
  config?: Record<string, unknown>
}

export type CascaderFormItem = FormItemBase & {
  type: 'cascader'
  config?: Record<string, unknown>
}

export type CustomFormItem = FormItemBase & {
  type: 'custom'
  config?: Record<string, unknown>
}

export type SelectFormItem = FormItemBase & {
  type: 'select'
  config?: Record<string, unknown>
}

export type TreeFormItem = FormItemBase & {
  type: 'tree'
  config?: Record<string, unknown>
}

export type TreeSelectFormItem = FormItemBase & {
  type: 'treeSelect'
  config?: Record<string, unknown>
}

export type DatepickerFormItem = FormItemBase & {
  type: 'datepicker'
  config?: Record<string, unknown>
}

export type CheckBoxFormItem = FormItemBase & {
  type: 'checkBox'
  config?: Record<string, unknown>
}

export type RadioFormItem = FormItemBase & {
  type: 'radio'
  config?: Record<string, unknown>
}

export type SelectV2FormItem = FormItemBase & {
  type: 'selectV2'
  config?: Record<string, unknown>
}

export type InputTagFormItem = FormItemBase & {
  type: 'inputTag'
  config?: Record<string, unknown>
}

export type AutocompleteFormItem = FormItemBase & {
  type: 'autocomplete'
  config?: Record<string, unknown>
}

/** 表单项联合（按 type 区分） */
export type FormItem =
  | InputFormItem
  | InputNumberFormItem
  | TextareaFormItem
  | CascaderFormItem
  | CustomFormItem
  | SelectFormItem
  | TreeFormItem
  | TreeSelectFormItem
  | DatepickerFormItem
  | CheckBoxFormItem
  | RadioFormItem
  | SelectV2FormItem
  | InputTagFormItem
  | AutocompleteFormItem

export type FormItemTool<T extends FormItemType> = Extract<FormItem, { type: T }>

export type FormItems<T extends FormItem = FormItem> = T[] | readonly T[]

/**
 * 由 `as const satisfies readonly FormItem[]` 这类字面量 schema 推导表单 data 类型。
 * @example
 * const items = [{ field: 'name', type: 'input' }, ...] as const satisfies FormItems
 * type Model = InferFormModel<typeof items>
 */
export type InferFormModel<T extends FormItems | readonly FormItem[]> = {
  [K in T[number] as T[number]['field']]?: ValueForFormType<
    Extract<T[number], { field: K }>['type']
  >
}

/**
 * 由表单项数组推导表单 `data`；不显式传入泛参时为宽松字典（兼容任意业务 model）。
 */
export type FormData<T extends FormItems | readonly FormItem[] | undefined = undefined> =
  undefined extends T
    ? Record<string, unknown>
    : T extends FormItems | readonly FormItem[]
      ? InferFormModel<T>
      : Record<string, unknown>

/** 子插槽 / 自定义插槽回传的常用骨架（具体字段依插槽而定） */
export type SlotData = Record<string, unknown> & {
  item?: FormItem
  data?: unknown
  dataValue?: unknown
  formData?: Record<string, unknown>
  slotData?: unknown
}

export interface KeyUpEnterEvent {
  event: KeyboardEvent
  current: FormItem
}

export interface FormComponentInstance {
  getRef?: () => unknown
}

export type AllRefs = Record<string, unknown>

export type ElFormConfig = Record<string, unknown>

export interface Props<
  TModel extends Record<string, unknown> = Record<string, unknown>,
> {
  elFormConfig?: ElFormConfig
  allDisabled?: boolean
  formItems?: FormItem[]
  data?: TModel
  itemStyle?: CSSProperties
  colLayout?: Partial<ColProps>
  footerLayout?: Partial<ColProps>
  rules?: FormRules
  rowConfig?: Record<string, unknown>
  hideItems?: string[] | Ref<string[]>
}

/** 供需要同时约束「配置 + model」时使用 */
export type PropsWithModel<
  TItems extends FormItems,
  TModel extends InferFormModel<TItems> & Record<string, unknown> = InferFormModel<TItems> &
    Record<string, unknown>,
> = Omit<Props<TModel>, 'formItems' | 'data'> & {
  formItems?: [...TItems]
  data?: TModel
}

export type FormSlots = Record<string, unknown>

export interface FormEmits {
  keyUpEnter: [payload: KeyUpEnterEvent]
}
