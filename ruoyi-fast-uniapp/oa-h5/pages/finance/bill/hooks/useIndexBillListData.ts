import { financeBillList } from "@/api/finance";
import { BILL_DETAIL_FLUSH_EVENT } from "@/utils/constants";
import { getFormatHourAndMinute, getFormatMonthAndDate, getWeekText } from "@/utils/date";

const PAGE_LIMIT = 7;

// 对返回的数据进行预处理
const handleProcessBillList = (list: any[]) => {
  return list.map(item => {
    const {
      days,
      income,
      expend,
      data
    } = item;


    return {
      ...item,
      date: getFormatMonthAndDate(days),
      day: getWeekText(days),
      income: income.toFixed(2),
      expend: expend.toFixed(2),
      data: (data as any[]).map(item => {
        const { edit_time } = item;
        return {
          ...item,
          time: getFormatHourAndMinute(edit_time),
        }
      })
    };
  });
}

export const useIndexBillListData = (filterContent: Ref<BillListFilterContent>) => {

  // 是否进行了搜索，要对之前的数据进行清理
  let cleanFlag = false;

  let loading = false;
  let loaded = false;

  const billList = ref<any[]>([]);

  const queryParams = reactive({
    page: 1,
    limit: PAGE_LIMIT,
    types: "",
    time: "",
    type_id: "",
    cate_id: []
  });

  onReachBottom(() => {
    if (loading || loaded) return;
    queryParams.page++;
  });

  const fetchData = async () => {
    if (loading || loaded) return;
    loading = true;

    const result = await financeBillList(queryParams);

    const nextList = handleProcessBillList(result.data);

    // 搜索条件变化后需要清除原有数据
    if (cleanFlag) {
      billList.value = nextList;
      cleanFlag = false;
    } else {
      billList.value.push(...nextList);
    }


    loaded = result.data.length < PAGE_LIMIT;
    loading = false;
  }

  const handleBuildQueryParams = () => {
    if (loading) return;
    const {
      searchText: name,
      inComeAndExpendType: types,
      dateRange: time,
      payType,
      billCateIds
    } = filterContent.value;

    loaded = false;
    cleanFlag = true;

    Object.assign(queryParams, {
      page: 1,
      name,
      types,
      time,
      type_id: payType || "",
      cate_id: [...billCateIds]
    });
  }

  onLoad(() => {
    uni.$on(BILL_DETAIL_FLUSH_EVENT, handleBuildQueryParams);
  });

  onUnload(() => {
    uni.$off(BILL_DETAIL_FLUSH_EVENT, handleBuildQueryParams);
  });

  watch(
    filterContent,
    handleBuildQueryParams,
  )

  watch(
    queryParams,
    fetchData,
    {
      immediate: true
    }
  );

  return [
    billList
  ];
}