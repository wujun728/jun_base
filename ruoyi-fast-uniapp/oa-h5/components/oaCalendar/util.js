const now_date = new Date()
class Util {

  _old_first_day = null
  _old_last_day = null
  _first_day = null // 日历第一位
  _last_day = null // 日历最后
  _calendar_data = [] // 保存当前的日历信息

  cur_year = now_date.getFullYear()
  cur_month = now_date.getMonth() + 1
  select_day = now_date.getDate()

  pack_up = true // 是否收起
  fixNumber = 7 // 总共要获取多少天数

  /**
   * @desc 设置基准时间
   * @param year 年
   * @param month 月
   * @param select_day  选中的日期
   */
  setBaseDate({ year, month, select_day }) {
    year && (this.cur_year = year)
    month && (this.cur_month = month)
    select_day && (this.select_day = select_day)
  }

  /**
   * @desc 获取日历的数据，外部调用
   * @param type next 往后 pre 往前 没有则是当月
   * @return {*[]}
   */
  getData(type, value) {
    this.fixNumber = 7
    let start_index = null
    if (type === 'pre' || type === 'next') {
      const cur_date = this.getNextMonthOrYear(value.year, value.month, value.day, type)
      start_index = cur_date.day
      this.cur_month = cur_date.month
      this.cur_year = cur_date.year
    }


    const data = this.generateCalendarData(start_index)

    // this._calendar_data = data this._first_day = data[0] this._last_day = data[data.length - 1]

    return data
  }


  /**
   * 生成日历的数据,
   * @param start_index 开始生成
   * @return {*[]}
   */
  generateCalendarData(start_index) {
    let year = this.cur_year
    let month = this.cur_month
    let fixNumbers = this.fixNumber
    const count_days = this.getCurMonthDays(year, month)

    let select_day = start_index ? start_index : this.pack_up ? this.select_day : 1

    let result = []
    // 指定时间往前找到周日的日期
    result.push(...this.getLatestWeekDays(this.formatterTime(year, month, select_day)).reverse())

    fixNumbers -= result.length

    let for_cur_day = select_day
    for (let i = 1; i <= fixNumbers; i++) {
      for_cur_day += 1

      if (for_cur_day > count_days) {

        if (month === 12) {
          year += 1
          month = 1
        } else {
          month += 1
        }

        for_cur_day = 1
      }
      result.push(this.generateData(this.formatterTime(year, month, for_cur_day)))
    }

    return result
  }

  // 向前找到距离当前指定时间最近的周日
  getLatestWeekDays(curDate) {
    const date = new Date(curDate)
    const result = []

    if (date.getDay() === 0) {
      result.push(this.generateData(curDate))
      return result
    }

    const getPreTimeText = () => {
      let year = date.getFullYear()
      let month = date.getMonth() + 1

      if (month === 1) {
        year -= 1
        month = 12
      } else {
        month -= 1
      }

      let day = this.getCurMonthDays(year, month)
      return this.formatterTime(year, month, day)
    }

    for (let i = date.getDate(); i > 0; i--) {
      const cur_date_text = this.formatterTime(date.getFullYear(), date.getMonth() + 1, i)
      const cur_date = new Date(cur_date_text)
      result.push(this.generateData(cur_date_text))

      if (cur_date.getDay() === 0) break;

      if (i === 1) {
        result.push(...this.getLatestWeekDays(getPreTimeText()))
      }
    }

    return result
  }

  // 获取当前月的总天数
  getCurMonthDays(year, month) {
    return new Date(year, month, 0).getDate()
  }

  /**
   * @desc 生成约定好的数据格式的对象
   * @param date yyyy-mm-dd / yyyy/mm/dd
   * @return {{date: Date, week: number, month: number, isCurMonth: boolean, year: number, isNextMonth: boolean, isPreMonth: boolean, day: number}}
   */
  generateData(date) {
    const _date = new Date(date)
    const _date_month = _date.getMonth() + 1

    const isPreMonth = () => {
      if (this.cur_month === 1) {
        return _date_month === 12
      }
      return _date_month === this.cur_month - 1
    }

    const isNextMonth = () => {
      if (this.cur_month === 12) {
        return _date_month === 1
      }
      return _date_month === this.cur_month + 1
    }

    const isCurMonth = () => {
      return this.cur_month === _date_month
    }

    return {
      date: _date,
      year: _date.getFullYear(),
      month: _date.getMonth() + 1,
      day: _date.getDate(),
      week: _date.getDay(),
      isPreMonth: isPreMonth(),
      isNextMonth: isNextMonth(),
      isCurMonth: isCurMonth()
    }
  }

  /**
   * @desc 根据类型返回确切可使用的日期数据
   * @param year 年份
   * @param month 月份
   * @param day 第几号
   * @param type 'pre 获取上一个月' 'next 获取下一个月'
   */
  getNextMonthOrYear(year, month, day, type) {
    const result = { year, month, day }
    let count_day = this.getCurMonthDays(year, month)
    switch (type) {
      case 'next':
        result.day += 1

        if (result.day > count_day) {
          result.day = 1

          if (month === 12) {
            result.year += 1
            result.month = 1
          } else {
            result.month += 1
          }

        }

        break;

      case 'pre':
        result.day -= 1

        if (result.day <= 0) {

          if (month === 1) {
            result.year -= 1
            result.month = 12
          } else {
            result.month -= 1
          }

          result.day = this.getCurMonthDays(result.year, result.month)

        }
        break;

      default:
        break;
    }

    return result
  }

  formatterTime(year, month, day) {
    const system_info = uni.getSystemInfoSync()
    const f = system_info.platform === 'ios' ? '/' : '-'
    return `${year}${f}${month}${f}${day}`
  }

}


export default Util