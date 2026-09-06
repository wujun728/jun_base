import { BillInComeAndExpendTypes } from '../hooks/useBillFilterIncomeAndExpend';

export interface FilterContent {
  type: BillInComeAndExpendTypes,
  cateId: number[][],
  time: string
}
