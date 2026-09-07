/**
 * Independent time operation tool to facilitate subsequent switch to dayjs
 */
import dayjs from 'dayjs';
import duration from 'dayjs/plugin/duration';
import relativeTime from 'dayjs/plugin/relativeTime';

/** 插件 */
dayjs.extend(duration);
dayjs.extend(relativeTime);

const DATE_TIME_FORMAT = 'YYYY-MM-DD HH:mm:ss';
const DATE_FORMAT = 'YYYY-MM-DD';

export function formatToDateTime(date?: dayjs.ConfigType, format = DATE_TIME_FORMAT): string {
  return dayjs(date).format(format);
}

export function formatToDate(date?: dayjs.ConfigType, format = DATE_FORMAT): string {
  return dayjs(date).format(format);
}

export const dateUtil = dayjs;
