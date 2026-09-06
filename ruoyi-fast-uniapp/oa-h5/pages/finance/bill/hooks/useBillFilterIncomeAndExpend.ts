export enum BillInComeAndExpendTypes {
  ALL = "",
  INCOME = "1",
  EXPEND = "0"
}

export enum BillInComeAndExpendTypesText {
  ALL = "全部",
  INCOME = "收入",
  EXPEND = "支出"
}

const config = [
  {
    name: BillInComeAndExpendTypesText.ALL,
    value: BillInComeAndExpendTypes.ALL
  },
  {
    name: BillInComeAndExpendTypesText.INCOME,
    value: BillInComeAndExpendTypes.INCOME
  },
  {
    name: BillInComeAndExpendTypesText.EXPEND,
    value: BillInComeAndExpendTypes.EXPEND
  }
];

type BillFilterIncomeAndExpendHookReturnType = [
  Ref<BillInComeAndExpendTypes>,
  Ref<string>,
  Record<string, string>[],
  UniEventValueHandler
];

export const useBillFilterIncomeAndExpend = (hasAll: boolean = true, defaultText: string = BillInComeAndExpendTypesText.ALL): BillFilterIncomeAndExpendHookReturnType => {
  const incomeAndExpendType = ref<BillInComeAndExpendTypes>(BillInComeAndExpendTypes.ALL);

  const incomeAndExpendTypeText = ref<string>(defaultText);

  const _config = hasAll ? config : config.slice(1);

  const handleChange: UniEventValueHandler = (e) => {
    incomeAndExpendType.value = _config[e.detail.value].value;
    incomeAndExpendTypeText.value = _config[e.detail.value].name;
  }

  return [
    incomeAndExpendType,
    incomeAndExpendTypeText,
    _config,
    handleChange
  ];
}