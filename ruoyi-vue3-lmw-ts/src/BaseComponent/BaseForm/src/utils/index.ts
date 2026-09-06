import type { FormItem, OptionItem } from '../types/index.ts'
import { isRef } from 'vue'

export function getOptions(item: FormItem): OptionItem[] {
  const { options } = item
  if (!options) return []
  if (isRef(options)) return (options.value ?? []) as OptionItem[]
  return options
}
