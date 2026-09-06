export type {
  InputLikeValue,
  NumericValue,
  TextValue,
  SelectValue,
} from '../../types/index.ts'

export const buildPlaceholder = (
  disabled: boolean,
  action: '请输入' | '请选择',
  label?: string,
  fallback?: string
): string => {
  if (disabled) return ''
  if (fallback) return fallback
  return `${action}${label ?? ''}`
}
