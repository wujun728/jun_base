// @ts-nocheck
import {isNumeric} from '../isNumeric'
import {isDef} from '../isDef'
/** 加单位 */
export function addUnit(value?: string | number): string | undefined {
  if (!isDef(value)) {
    return undefined;
  }

  value = String(value);
  return isNumeric(value) ? `${value}px` : value;
}