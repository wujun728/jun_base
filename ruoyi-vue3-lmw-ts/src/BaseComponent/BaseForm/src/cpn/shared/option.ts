import type {
  OptionRecord,
  PrimitiveValue,
  SelectLikeValue,
} from '../../types/index.ts'

export type { ObjectValue, PrimitiveValue, SelectLikeValue } from '../../types/index.ts'

export const toOptionKey = (option: OptionRecord): string | number =>
  (option.key ?? option.value) as string | number

export const toOptionPrimitiveValue = (option: OptionRecord): PrimitiveValue =>
  option.value as PrimitiveValue

export const toOptionGroupValue = (option: OptionRecord): string | number =>
  option.value as string | number

export const toOptionSelectValue = (option: OptionRecord): SelectLikeValue =>
  option.value as SelectLikeValue

export const toOptionLabel = (
  option: OptionRecord,
  setLabel?: string
): string | number =>
  (setLabel ? option[setLabel] : option.label) as string | number

export const toOptionValueByField = (
  option: OptionRecord,
  setValue?: string
): SelectLikeValue =>
  (setValue ? option[setValue] : option.value) as SelectLikeValue
