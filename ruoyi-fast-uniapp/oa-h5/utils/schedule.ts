import moment from "moment";
// 获取星期
enum WeekText {
  周日,
  周一,
  周二,
  周三,
  周四,
  周五,
  周六
}
export const getWeekText = (date: string): string => {
  const week = moment(date).day();
  const startDay = moment(date).format("YYYY/MM/DD");
  const time = moment(new Date()).format("YYYY/MM/DD");
  if (startDay === time) {
    return "今天";
  } else {
    return WeekText[week];
  }
};

// 获取日程中开始时间与结束时间
export const getScheduleTime = (startTime: string, endTime: string): string => {
  let dateStr = "";
  const startDay = moment(startTime).format("YYYY/MM/DD");
  const endDay = moment(endTime).format("YYYY/MM/DD");
  const startYear = moment(startTime).format("YYYY");
  const endYear = moment(endTime).format("YYYY");
  if (startDay === endDay) {
    dateStr = `${moment(startTime).format("MM/DD")} (${getWeekText(startTime)}) ${moment(startTime).format("HH:mm")} — ${moment(endTime).format("HH:mm")}`;
  } else if (startYear == endYear) {
    dateStr = `${moment(startTime).format("MM/DD")} (${getWeekText(startTime)}) ${moment(startTime).format("HH:mm:ss")} — ${moment(endTime).format("MM/DD")} (${getWeekText(endTime)}) ${moment(endTime).format("HH:mm:ss")}`;
  } else {
    dateStr = `${moment(startTime).format("YYY/MM/DD HH:mm:ss")}  — ${moment(endTime).format("YYY/MM/DD HH:mm:ss")}`;
  }
  return dateStr;
};

/**
 * 格式化日期，返回字符串或 Date 对象
 * @param {string | Date} date -字符串或 Date 对象
 * @param {boolean} isDisplayTime 时间大于3天时是否显示时分
 * @returns {string} - 字符串或 Date 对象
 */
export const formatDate = (date: string | Date, isDisplayTime: boolean = false): string | Date => {
  const original = moment(date);
  // 判断时间大小
  const isTime = moment().isBefore(original);
  const isBeforeText = isTime ? "后" : "前";
  const secondsAgo = Math.abs(moment().diff(original, "seconds"));
  if (secondsAgo < 60) {
    return `${secondsAgo}秒${isBeforeText}`;
  }
  const minutesAgo = Math.abs(moment().diff(original, "minutes"));
  if (minutesAgo < 60) {
    return `${minutesAgo}分钟${isBeforeText}`;
  }
  const hoursAgo = Math.abs(moment().diff(original, "hours"));
  if (hoursAgo < 24) {
    return `${hoursAgo}小时${isBeforeText}`;
  }
  const daysAgo = Math.abs(moment().diff(original, "days"));
  // 判断是否在今年内
  const isYears = moment(date).isSame(new Date(), "year");
  if (daysAgo <= 3) {
    return `${daysAgo}天${isBeforeText}`;
  } else if (daysAgo > 3 && isYears) {
    return isDisplayTime ? original.format("MM/DD HH:mm") : original.format("MM/DD");
  } else {
    return original.format("YYYY/MM/DD");
  }
};

/**
 * 检查的日期是否在指定的时间范围内
 * @param {string} start - 开始日期
 * @param {string} end - 结束日期
 * @param {string} [check=moment()] - 指定时间，默认为当前时间
 * @returns {boolean} - 返回布尔值
 */
export const isTimeBetween = (start: string, end: string, check: string = moment().format("YYYY-MM-DD HH:mm:ss")): boolean => {
  const [momentCheck, momentStart, momentEnd] = [check, start, end].map(date => moment(date, "YYYY-MM-DD HH:mm:ss"));
  return momentCheck.isBetween(momentStart, momentEnd);
};

/**
 * 根据指定的日期和相差的天/秒/分/小时/周/月/年数获取另外一个日期
 * @param {string} [dateStr=moment()] - 指定的日期字符串，默认为当前时间
 * @param {number} diffValue - 相差的天/秒/分/小时/周/月/年数
 * @param {string} unit - 相差的单位（days, seconds, minutes, hours, weeks, months, years）
 * @param {string} format - 返回日期的格式，默认为 "YYYY-MM-DD"
 * @returns {string} - 格式化的新日期字符串
 */
export const getDateByDiff = (
  dateStr: string = moment().format(),
  diffValue: number,
  unit: "days" | "seconds" | "minutes" | "hours" | "weeks" | "months" | "years",
  format: string = "YYYY-MM-DD"
): string => {
  const momentDate = moment(dateStr, "YYYY-MM-DD HH:mm:ss");
  const newMomentDate = momentDate.add(diffValue, unit);
  return unit === "seconds"
    || unit === "minutes"
    || unit === "hours"
    ? newMomentDate.format("YYYY-MM-DD HH:mm:ss")
    : newMomentDate.format(format);
};

/**
 * 计算两个日期之间的时间差
 * @param {string} startDateStr - 开始日期
 * @param {string} endDateStr - 结束日期
 * @param {moment.unitOfTime.Diff} unit - 时间单位（days, seconds, minutes, hours, weeks, months, years）
 * @returns {number} - 两日期相差的时间数
 */
export const getTimeDiffBetweenDates = (startDateStr: string, endDateStr: string, unit: moment.unitOfTime.Diff): number => {
  const startDate = moment(startDateStr);
  const endDate = moment(endDateStr);
  const diff = endDate.diff(startDate, unit);
  return diff;
};
