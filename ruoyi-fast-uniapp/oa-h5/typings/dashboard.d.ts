type ChartWidgetType = "statistic" | "progressbar" | "barChart" | "barXChart" | "lineChart" | "funnelChart" | "pieChart" | "radarChart" | "listTable";

type ChartWidgetDesignData = {
  type: ChartWidgetType;
  icon: string;
  formItemFlag: boolean;
  options: {
    name: string;
    value: number;
    label: string;
    dataEntity: number;
    setDimensional: {
      dimension: Array<any>;
      metrics: Array<{
        alias: string;
        showEdit: boolean;
        editAlias: string;
        type: string;
        sort: string;
        calcMode: string;
        thousandsSeparator: boolean;
        showDecimalPlaces: boolean;
        decimalPlaces: number;
        showNumericUnits: boolean;
        numericUnits: string;
        field_name: string;
        field_name_en: string;
        crud_id: number;
        id: number;
        association_crud_id: number;
        data_dict_id: number;
        prev_field: string;
        form_value: string;
        association: any;
        data_dict_list: Array<any>;
        is_user: boolean;
        is_frame: boolean;
      }>;
      targetValue: number;
      showFields: Array<any>;
      dimensionRow: Array<any>;
      dimensionCol: Array<any>;
    };
    chartStyle: number;
    setChartConf: {
      numShow: boolean;
      chartShow: boolean;
      useAllData: boolean;
    };
    setChartFilter: {
      equation: string;
      list: Array<any>;
      additional_search_boolean: string;
    };
    setChartStyle: {
      currencySymbol: string;
      currencySymbolSize: string;
      useTextColor: string;
    };
    showHeader: boolean;
    showFullscreen: boolean;
    showRefresh: boolean;
    showCollapse: boolean;
    dsEnabled: boolean;
    dsName: string;
    x: number;
    y: number;
    w: number;
    h: number;
  };
  id: string;
};

interface ListWidgetDesignData {
  type: string;
  icon: string;
  formItemFlag: boolean;
  options: {
    name: string;
    value: number;
    label: string;
    dataEntity: number;
    setDimensional: {
      dimension: Array<any>;
      metrics: Array<any>;
      targetValue: number;
      showFields: Array<{
        alias: string;
        showEdit: boolean;
        editAlias: string;
        type: string;
        sort: string;
        calcMode: string;
        thousandsSeparator: boolean;
        showDecimalPlaces: boolean;
        decimalPlaces: number;
        showNumericUnits: boolean;
        numericUnits: string;
        field_name: string;
        field_name_en: string;
        crud_id: number;
        id: number;
        association_crud_id: number;
        data_dict_id: number;
        prev_field: string;
        form_value: string;
        association?: {
          table_name: string;
          table_name_en: string;
          id: number;
          field: Array<{
            field_name: string;
            field_name_en: string;
            crud_id: number;
            prev_field: string;
            data_dict_id: number;
            form_value: string;
            id: number;
          }>;
        };
        data_dict_list: Array<any>;
        is_user: boolean;
        is_frame: boolean;
        prop: string;
      }>;
      dimensionRow: Array<any>;
      dimensionCol: Array<any>;
    };
    setChartConf: {
      pageSize: number;
      showSummary: boolean;
      showSumcol: boolean;
      useAllData: boolean;
    };
    setChartFilter: {
      equation: string;
      list: Array<any>;
      additional_search_boolean: string;
    };
    showHeader: boolean;
    showFullscreen: boolean;
    showRefresh: boolean;
    showCollapse: boolean;
    dsEnabled: boolean;
    dsName: string;
    x: number;
    y: number;
    w: number;
    h: number;
  };
  id: string;
}

interface CommonChartDataResponse {
  series: Array<{
    name: string;
    data: Array<number>;
  }>;
  xAxis: Array<string>;
  yAxis: Array<string>;
  other: Array<any>;
}

interface ColumnChartData extends Pick<CommonChartDataResponse, "series"> {
  categories: string[];
}

interface FunnelChartDataResponseItem {
  name: string;
  dim_value: string;
  other: Record<string, number>;
  value: number;
}

type FunnelChartDataResponse = FunnelChartDataResponseItem[];

interface FunnelChartDataSeriesDataItem {
  name: string;
  value: number;
  legendShape: string;
  labelTips: string;
  labelText: string;
}

interface FunnelChartDataSeries {
  data: FunnelChartDataSeriesDataItem[];
}

interface FunnelChartData {
  series: FunnelChartDataSeries[];
}

interface ListDataResponse {
  count: number;
  list: Record<string, any>[];
}
