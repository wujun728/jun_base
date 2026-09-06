import { crudModuleDashboardChartDataApi, crudModuleDashboardListDataApi } from "@/api/crud";

/**
 * 获取数组中的最大值
 * @param list number[]
 * @returns number
 */
export const getMaxValue = (list: number[]): number => {
  return list.reduce((a, b) => {
    a = Number(a);
    b = Number(b);
    return a > b ? a : b;
  });
}

/**
 * 给 series 数组中的每个对象增加 legendShape
 * @param arr object[]
 * @param legendShape string
 */
export const addLegendShapeToArrObjs = (arr: object[], legendShape: string = "roundedRect") => {
  arr.forEach(obj => {
    Object.assign(obj, {
      legendShape
    });
  });
}

/**
 * 根据分割线条数和当前数据最大值计算一个能够整除分割线条数的最大值，使 x 轴上每个刻度都是整数
 * @param maxValue 给出的初始最大值
 * @param splitNumber x 轴上分割线的条数
 * @returns int
 */
export const calcXAxisIntMaxValue = (maxValue: number, splitNumber: number = 7) => {
  maxValue = Math.max(maxValue, 10) + 1;
  while (1) {
    if (maxValue % splitNumber === 0 && maxValue % 10 === 0) {
      return maxValue;
    }
    maxValue++;
  }
}

/**
 * 
 * App 平台没有 requestAnimationFrame API，用 setTimeout 模拟
 */
const animationFramePolyfill = () => {
  if (globalThis.requestAnimationFrame) return;

  let lastTime = 0;
  const requestAnimationFrame = (callback: FrameRequestCallback) => {
    const currTime = new Date().getTime()
    // 为了使setTimteout的尽可能的接近每秒60帧的效果
    const timeToCall = Math.max(0, 16 - (currTime - lastTime))
    const id = setTimeout(() => {
      callback(currTime + timeToCall)
    }, timeToCall);
    lastTime = currTime + timeToCall;
    return id
  }

  const cancelAnimationFrame = (id: number) => {
    clearTimeout(id)
  }

  type RequestAnimationFrameFunc = (callback: FrameRequestCallback) => number;
  globalThis.requestAnimationFrame = requestAnimationFrame as unknown as RequestAnimationFrameFunc;
  globalThis.cancelAnimationFrame = cancelAnimationFrame;
}

animationFramePolyfill();


// 根据数值大小调整动画时间
export const calcCountAnimationTime = (inputValue: number, maxValue: number, minDuration: number, maxDuration: number) => {
  const normalizedValue = Math.min(Math.max(inputValue / maxValue, 0), 1);
  // 根据归一化的数值，调整动画时长

  const duration = minDuration + (maxDuration - minDuration) * Math.pow(normalizedValue, 0.5);
  return duration;
}


/**
 * 生成实时数值滚动
 * @param startValue int
 * @param endValue int
 * @param duration int
 * @returns 
 */
export const useCountTo = (startValue: number, endValue: Ref<number>, duration: number = 0) => {
  const currentCount = ref(startValue);
  let startTime = 0;
  let animationId = 0;

  const handleCount = (timestamp: number) => {
    if (!startTime) startTime = timestamp;
    const progress = timestamp - startTime;
    let value = Math.ceil(startValue + (endValue.value - startValue) * (progress / duration));
    currentCount.value = value > endValue.value ? endValue.value : value;

    if (progress < duration) {
      animationId = requestAnimationFrame(handleCount);
    } else {
      cancelAnimationFrame(animationId);
    }
  }

  // 检查动画时长，如果传入了动画时长，则不进行重新计算
  const handleCheckDuration = () => {
    if (duration) return;
    const maxValue = 99999;
    const minDuration = 400;
    const maxDuration = 2000;
    duration = calcCountAnimationTime(endValue.value, maxValue, minDuration, maxDuration);
  }

  const cancelWatch = watch(
    () => endValue.value,
    () => {
      handleCheckDuration();
      animationId = requestAnimationFrame(handleCount);
      nextTick(() => {
        cancelWatch();
      });
    }
  );

  return [
    currentCount
  ];
}

interface ScrollEventDetail {
  scrollLeft: number,
  scrollTop: number
}

interface ScrollEvent extends Event {
  detail: ScrollEventDetail
}

type EventHandler<T> = (e: T) => void;

type ScrollViewDetailTuple = [ScrollEventDetail, EventHandler<ScrollEvent>];
/**
 * 
 * scrollView scroll 滚动处理
 */
export const useScrollViewScroll = (callback?: EventHandler<ScrollEventDetail>): ScrollViewDetailTuple => {


  const scrollDetail = reactive<ScrollEventDetail>({
    scrollLeft: 0,
    scrollTop: 0
  });

  const handleScroll = (e: ScrollEvent) => {
    const { scrollLeft, scrollTop } = e.detail;
    scrollDetail.scrollLeft = scrollLeft;
    scrollDetail.scrollTop = scrollTop;
    callback && callback(e.detail);
  }


  return [
    scrollDetail,
    handleScroll
  ];
}


const ChartTypes = {
  // 统计数值
  statistic: 'statistic_numeric',
  // 进度条
  progressbar: 'progress_bar',
  // 柱状图
  barChart: 'bar_chart',
  // 条形图
  barXChart: 'column_chart',
  // 折线图
  lineChart: 'line_chart',
  // 漏斗图
  funnelChart: 'funnel_plot',
  // 漏斗图
  pieChart: 'pie_chart',
  // 雷达图
  radarChart: 'radar_chart',
  // 数据列表
  listTable: 'table',
  // 透视表
  pivotTable: 'table'
}


const formatItem = (list: any[], target: "longitude" | "latitude") => {
  let items = list.map((el) => {
    let newItem = {
      fieldNameEn: el.field_name_en,
      fieldName: el.alias,
      sort: el.sort,
      value: el.value
    }
    if (target == 'longitude') {
      Object.assign(newItem, {
        operator: el.calcMode
      });
    }
    return { ...newItem }
  })
  return items
}

export const useChartListData = <T>(listDesignData: ListWidgetDesignData) => {
  const chartData = ref<T>();

  const {
    id: uniqued,
    options
  } = listDesignData;
  const {
    dataEntity,
    setChartConf,
    setChartFilter
  } = options;

  const showField: string[] = listDesignData.options.setDimensional.showFields.map(i => i.field_name_en);

  let param = {
    limit: setChartConf.pageSize,
    page: 1,
    additionalSearch: setChartFilter.list || [],
    additionalSearchBoolean: setChartFilter.additional_search_boolean || 0,
    showField,
    sortFields: <any>[],
    crudId: dataEntity,
    uniqued, // 唯一标识
  };

  onLoad(async () => {
    const { data } = await crudModuleDashboardListDataApi(param);
    chartData.value = data;
  });

  return [
    chartData
  ];
}

export const useChartData = <T>(chartDesignData: ChartWidgetDesignData) => {
  const chartData = ref<T>();

  const {
    id: uniqued,
    type,
    options
  } = chartDesignData;
  const {
    dataEntity,
    setDimensional,
    setChartFilter
  } = options;

  let dimensionList = formatItem((setDimensional && setDimensional.dimension) || [], 'latitude');
  // 指标
  let indicatorList = formatItem((setDimensional && setDimensional.metrics) || [], 'longitude');


  let param = {
    uniqued, // 唯一标识
    type: ChartTypes[type], // 图表类型
    additionalSearch: setChartFilter.list || [],
    additionalSearchBoolean: setChartFilter.additional_search_boolean || 0,
    dimensionList, // 维度
    indicatorList, // 指标
    noPrivileges: true,
    crudId: dataEntity
  };

  onLoad(async () => {
    const { data } = await crudModuleDashboardChartDataApi(param);
    chartData.value = data;
  });

  return [
    chartData
  ];
}

/**
 * 将数字转换为货币格式
 * @param value number
 * @returns string
 */
export const numberToCurrencyNo = (value: number) => {
  if (!value) return '';
  const intPart = Math.trunc(value)
  const intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
  let floatPart = ''
  const valueArray = value.toString().split('.')
  if (valueArray.length === 2) {
    floatPart = valueArray[1].toString()
    return intPartFormat + '.' + floatPart
  }
  return intPartFormat + floatPart
}