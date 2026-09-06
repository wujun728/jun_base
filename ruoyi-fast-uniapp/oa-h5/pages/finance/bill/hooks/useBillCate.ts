import { financeBillCateApi } from "@/api/finance";
import { BillInComeAndExpendTypes } from "./useBillFilterIncomeAndExpend";

interface BillCate {
  id: number,
  name: string,
  path: string[],
  types: number,
  children?: BillCate[]
}

type BillCateIds = string[] | number[][];
type ChangeEventHandler = (ids: number[], names: string) => void;

type BillFilterCateHookReturnType = [
  Ref<BillCate[]>,
  Ref<BillCateIds>,
  Ref<string>,
  ChangeEventHandler
];

type CateIdToCateInfoMap = Map<number, BillCate>;

export const useBillCate = (multiple: boolean = true, defaultCatenames?: string, incomeType?: Ref<BillInComeAndExpendTypes>): BillFilterCateHookReturnType => {
  const billCateIds = ref<BillCateIds>([]);
  const billCateNames = ref<string>(defaultCatenames);
  const billCateFullList = ref<BillCate[]>([]);

  const billCateList = computed<BillCate[]>(() => {
    if (!incomeType || incomeType.value === BillInComeAndExpendTypes.ALL) return billCateFullList.value;

    const newBillCateList = billCateFullList.value.filter(i => (i.types + "") == incomeType.value);

    return newBillCateList;
  });

  // 将分类 ID 映射到分类详情
  const cateIdToCateInfoMap = computed<CateIdToCateInfoMap>(() => {
    const map: CateIdToCateInfoMap = new Map();

    const generateMap = (list: BillCate[]) => {
      for (let item of list) {
        map.set(item.id, item);
        if (item.children?.length) {
          generateMap(item.children);
        }
      }
    }

    generateMap(billCateList.value);
    return map;
  });

  // 选中的 ID 列表
  const handleBillCateChange: ChangeEventHandler = (selectIds: number[], names: string) => {
    if (!multiple) {
      // 单选时不做其他处理
      billCateIds.value = selectIds.map(i => i + "");
      billCateNames.value = names;
      return;
    }

    const cateIds: BillCateIds = [] as number[][];
    const cateNames: string[] = [];

    const generateCateIds = (cateList: BillCate[]) => {

      for (let cateInfo of cateList) {
        if (cateInfo.children?.length) {
          // 拥有 children，需要递归生成 ids 数组
          generateCateIds(cateInfo.children);
        } else {
          // 没有 children，则直接生成ids数组
          // path 是父级分类的 ID 路径
          cateIds.push(
            [...cateInfo.path.map(i => Number(i)), cateInfo.id]
          );
          cateNames.push(cateInfo.name);
        }
      }
    }

    const selectCateList = selectIds
      .filter(cateId => cateIdToCateInfoMap.value.has(cateId))
      .map(cateId => cateIdToCateInfoMap.value.get(cateId));

    selectCateList?.length && generateCateIds(selectCateList);

    billCateIds.value = cateIds;
    billCateNames.value = cateNames.join(",");
  }



  // 需要根据 income 类型来切换账目分类
  incomeType && watch(
    incomeType,
    (newIncomeType, oldIncomeType) => {
      // 当编辑账目详情时今日页面后会自动加载数据，无需切换账目分类
      if (oldIncomeType !== BillInComeAndExpendTypes.ALL) {
        handleBillCateChange([], defaultCatenames);
      }
    }
  )

  onLoad(async () => {
    const result = await financeBillCateApi({});
    billCateFullList.value.push(...result.data);
  });

  return [
    billCateList,
    billCateIds,
    billCateNames,
    handleBillCateChange
  ];
}