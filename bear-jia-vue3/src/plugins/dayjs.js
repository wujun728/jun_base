/**
 * Day.js 配置
 * 配置 Day.js 所需的插件和本地化设置
 */

import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';

// 导入插件 - 使用 require 方式避免 ESM 导入问题
import weekday from 'dayjs/plugin/weekday';
import localeData from 'dayjs/plugin/localeData';
import weekOfYear from 'dayjs/plugin/weekOfYear';
import weekYear from 'dayjs/plugin/weekYear';
import advancedFormat from 'dayjs/plugin/advancedFormat';
import customParseFormat from 'dayjs/plugin/customParseFormat';
import quarterOfYear from 'dayjs/plugin/quarterOfYear';
import timezone from 'dayjs/plugin/timezone';
import utc from 'dayjs/plugin/utc';
import isLeapYear from 'dayjs/plugin/isLeapYear';
import duration from 'dayjs/plugin/duration';
import relativeTime from 'dayjs/plugin/relativeTime';
import isToday from 'dayjs/plugin/isToday';
import isYesterday from 'dayjs/plugin/isYesterday';
import isTomorrow from 'dayjs/plugin/isTomorrow';
import isBetween from 'dayjs/plugin/isBetween';

// 扩展插件
dayjs.extend(weekday);
dayjs.extend(localeData);
dayjs.extend(weekOfYear);
dayjs.extend(weekYear);
dayjs.extend(advancedFormat);
dayjs.extend(customParseFormat);
dayjs.extend(quarterOfYear);
dayjs.extend(timezone);
dayjs.extend(utc);
dayjs.extend(isLeapYear);
dayjs.extend(duration);
dayjs.extend(relativeTime);
dayjs.extend(isToday);
dayjs.extend(isYesterday);
dayjs.extend(isTomorrow);
dayjs.extend(isBetween);

// 设置默认语言
dayjs.locale('zh-cn');

// 导出配置后的 dayjs
export default dayjs;

/**
 * 初始化 Day.js
 * 可在应用启动时调用此函数
 */
export function initDayjs() {
  // 这里可以添加额外的初始化逻辑
  console.log('Day.js 已初始化，当前语言：', dayjs.locale());
}
