import request from './request'

/**
 *  图表列表
 * @param {*} data
 * @returns
 */
export function dashboardList(data) {
  return request.get('crud/dashboard', data)
}
/**
 * 新增
 * @param {*} data
 * @returns
 */
export function saveDashboard(data) {
  return request.post('crud/dashboard', data)
}
/**
 *  修改
 * @param {*} data
 * @param {*} id
 * @returns
 */
export function changeDashboard(id, data) {
  return request.put(`crud/dashboard/${id}`, data)
}
/**
 *  保存看板数据
 * @param {*} data
 * @param {*} id
 * @returns
 */
export function changeDashboardDesign(id, data) {
  return request.put(`crud/dashboard/design/${id}`, data)
}
/**
 *  获取看板数据
 * @param {*} data
 * @param {*} id
 * @returns
 */
export function getDashboardDesign(id, data) {
  return request.get(`crud/dashboard/design/${id}`, data)
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

const formatItem = (list, target) => {
  let items = list.map((el) => {
    let newItem = {
      fieldNameEn: el.field_name_en,
      fieldName: el.alias,
      sort: el.sort,
      value: el.value
    }
    if (target == 'longitude') {
      newItem.operator = el.calcMode
      // newItem.axisFormat = {
      //   thousandsSeparator: el.thousandsSeparator,
      //   decimalPlaces: el.showDecimalPlaces ? el.decimalPlaces : 0,
      //   numericUnits: el.showNumericUnits && el.numericUnits != '无' ? el.numericUnits : ''
      // }
    }
    return { ...newItem }
  })
  return items
}

/**
 *
 * 通用查询接口
 */
export function getDataList(data) {
  return request.post('crud/dashboard/list', data)
}

// 图表数据获取接口
export async function queryChartData(formModel, type) {

  const allEntityName = JSON.parse(localStorage.getItem('allEntityName'))
  let latitude = formatItem((formModel.setDimensional && formModel.setDimensional.dimension) || [], 'latitude')
  // 指标
  let longitude = formatItem((formModel.setDimensional && formModel.setDimensional.metrics) || [], 'longitude')

  if (type == 'pivotTable') {
    latitude = formatItem((formModel.setDimensional && formModel.setDimensional.dimensionRow) || [], 'latitude')
    let dimensionCol = formatItem((formModel.setDimensional && formModel.setDimensional.dimensionCol) || [], 'latitude')
    dimensionCol.forEach((el) => {
      el.columns = true
      latitude.push(el)
    })
  }

  let listArr = []
  if (formModel.setChartFilter.list && formModel.setChartFilter.list.length > 0&&!formModel.setChartFilter.list[0].obj) {

    formModel.setChartFilter.list.map((item) => {
      let obj = {
        operator: item.value,
        form_field: item.field,
        value: item.option,
        obj: item
      }
      if (!item.option && item.category === 2) {
        obj.value = []
        item.options.userList.map((i) => {
          obj.value.push(i.value)
        })
      } else if (!item.option && item.category === 1) {
        obj.value = []
        item.options.depList.map((i) => {
          obj.value.push(i.id)
        })
      }
      if (obj.operator === 'between') {
        if (item.type == 'date_time_picker' || item.type == 'date_picker') {
          let data = item.option[0] + '-' + item.option[1]
          obj.value = data
        } else {
          let data = {
            min: item.min,
            max: item.max
          }
          obj.value = data
        }
      
      }
      listArr.push(obj)
    })
    formModel.setChartFilter.list = listArr
  }
  let param = {
    uniqued: formModel.name, // 唯一标识
    type: ChartTypes[type], // 图表类型
    tableNameEn: allEntityName[formModel.dataEntity], // 表名
    dimensionList: latitude, // 维度
    indicatorList: longitude, // 指标
    noPrivileges: formModel.setDimensional && formModel.setChartConf.useAllData, // 是否使用全部数据
    additionalSearch: formModel.setChartFilter.list || [], // 过滤条件
    additionalSearchBoolean: formModel.setChartFilter.additional_search_boolean || '' // 布尔过滤条件
  }

  return request.post('crud/dashboard/chart', param)
}

// 修改默认视图
export function updateDefault(id, defaultChart) {
  return request.post('crud/dashboard/chart', param)
}
