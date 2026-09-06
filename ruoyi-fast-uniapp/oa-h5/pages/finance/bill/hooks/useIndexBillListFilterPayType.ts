import { payTypeApi } from "@/api/customer";

export interface PayType {
  id: number,
  name: string
}

type FilterPayTypeHookReturnType = [
  Ref<number>,
  Ref<string>,
  Ref<PayType[]>,
  UniEventValueHandler,
  EventHandler<number>
];

export const DEFAULT_PAY_TYPE_ALL = 0;

export const useFilterPayType = (hasAll: boolean = true, defaultText: string = "支付方式"): FilterPayTypeHookReturnType => {
  const payType = ref<number>(DEFAULT_PAY_TYPE_ALL);

  const payTypeText = ref<string>(defaultText);

  const defaultConfig = hasAll ? [
    {
      id: DEFAULT_PAY_TYPE_ALL,
      name: "全部"
    }
  ] : [];

  const payTypeConfig = ref<PayType[]>(defaultConfig);

  onLoad(async () => {
    const result = await payTypeApi();

    const list: PayType[] = result.data.list;

    payTypeConfig.value = [...payTypeConfig.value, ...list];
  });

  const handleChange: UniEventValueHandler = (e) => {
    payType.value = payTypeConfig.value[e.detail.value].id;
    payTypeText.value = payTypeConfig.value[e.detail.value].name;
  }

  // 手动设置 pay type
  const handleUpdatePayType = (_id: number) => {

    const setValue = () => {
      const config = payTypeConfig.value.find(i => i.id === _id);
      if (!config) return;
      const { id, name } = config;
      payType.value = id;
      payTypeText.value = name;
    }

    if (payTypeConfig.value.length > 1) {
      setValue();
    } else {
      const stopWatch = watch(
        payTypeConfig,
        () => {
          setValue();
          payTypeConfig.value.length && stopWatch();
        },
        {
          immediate: true
        }
      )
    }
  }

  return [
    payType,
    payTypeText,
    payTypeConfig,
    handleChange,
    handleUpdatePayType
  ];
}