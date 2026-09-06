import dayjs from 'dayjs'
import utc from 'dayjs/plugin/utc'
dayjs.extend(utc)
const HOUR_MINUTE_SECOND = 'YYYY-MM-DD HH:mm:ss'
export function parseTime(utc: dayjs.ConfigType, format = HOUR_MINUTE_SECOND) {
  return dayjs.utc(utc).utcOffset(8).format(format)
}
// 获取最近一个月到今天，返回一个数组
export function getLastMonth(format = HOUR_MINUTE_SECOND) {
  const now = dayjs()
  const lastMonth = now.subtract(1, 'month')
  return [lastMonth.format(format), now.format(format)]
}
