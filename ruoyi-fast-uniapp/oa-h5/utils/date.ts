import moment from "moment";

/**
 * 获取当天时间
 * @param separater 日期连接符
 * @returns example: 2020-12-12
 */
export const getCurrentDate = (separater: string = "-") => {
  const timeInstance = new Date();

  return [
    timeInstance.getFullYear(),
    timeInstance.getMonth() + 1,
    timeInstance.getDate()
  ].join(separater);
};

enum WeekText {
  星期日,
  星期一,
  星期二,
  星期三,
  星期四,
  星期五,
  星期六
}

export const getWeekText = (dateStr: string) => {
  const momentInstance = moment(dateStr);
  const currentMomentInstance = moment(new Date());

  const week = momentInstance.day();
  const date = momentInstance.format("YYYY/MM/DD");
  const currentDate = currentMomentInstance.format("YYYY/MM/DD");
  const lastDate = currentMomentInstance.subtract(1, "day").format("YYYY/MM/DD");

  if (date === currentDate) {
    return "今天";
  } else if (date === lastDate) {
    return "昨天";
  } else {
    return WeekText[week];
  }
};

/**
 * 将接收到的日期字符串格式化为月日时间
 * @param dateStr 日期字符串
 * @returns example: 12月12日
 */
export const getFormatMonthAndDate = (dateStr: string) => {
  const momentInstance = moment(dateStr);

  const month = momentInstance.get("month");
  const date = momentInstance.get("date");

  return `${month + 1}月${date}日`;
};

/**
 * 将接收到的日期字符串格式化为时分
 * @param dateStr 日期字符串
 * @returns example: 12:24
 */
export const getFormatHourAndMinute = (dateStr: string) => {
  const momentInstance = moment(dateStr);

  const hour = momentInstance.get("hour");
  const minute = momentInstance.get("minute");

  return `${(hour + "").padStart(2, "0")}:${(minute + "").padStart(2, "0")}`;
};

/**
 * 获取当前月的日期范围
 * @returns string
 */
export const getCurrentMonthRange = () => {
  const start = moment().startOf("month").format("YYYY/MM/DD");
  const end = moment().endOf("month").format("YYYY/MM/DD");

  return [start, end];
};
