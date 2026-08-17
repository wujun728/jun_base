function stubData(data) {
  return Promise.resolve({ status: 200, data: data || {} });
}

export function viewModelApi() {
  return stubData({ todayOrderNum: 0, todaySales: 0, todayUserNum: 0, todayVisits: 0 });
}

export function chartUserApi() {
  return stubData({ series: [], xAxis: [] });
}

export function chartBuyApi() {
  return stubData({ series: [], xAxis: [] });
}

export function chartOrder30Api() {
  return stubData({ series: [], xAxis: [] });
}

export function chartOrderMonthApi() {
  return stubData({ series: [], xAxis: [] });
}

export function chartOrderWeekApi() {
  return stubData({ series: [], xAxis: [] });
}

export function chartOrderYearApi() {
  return stubData({ series: [], xAxis: [] });
}

export function businessData() {
  return stubData({});
}