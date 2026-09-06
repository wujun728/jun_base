<template>
  <div class="popup-result">
    <p class="title">最近5次运行时间</p>
    <ul class="popup-result-scroll">
      <template v-if="isShow">
        <li v-for="item in resultList" :key="item">{{ item }}</li>
      </template>
      <li v-else>计算结果中...</li>
    </ul>
  </div>
</template>

<script setup>
const props = defineProps({
  ex: {
    type: String,
    default: '',
  },
})

const dayRule = ref('')
const dayRuleSup = ref('')
const dateArr = ref([])
const resultList = ref([])
const isShow = ref(false)

watch(
  () => props.ex,
  () => expressionChange()
)

function expressionChange() {
  isShow.value = false
  const ruleArr = props.ex.split(' ')
  let nums = 0
  const resultArr = []
  const nTime = new Date()
  let nYear = nTime.getFullYear()
  let nMonth = nTime.getMonth() + 1
  let nDay = nTime.getDate()
  let nHour = nTime.getHours()
  let nMin = nTime.getMinutes()
  let nSecond = nTime.getSeconds()

  getSecondArr(ruleArr[0])
  getMinArr(ruleArr[1])
  getHourArr(ruleArr[2])
  getDayArr(ruleArr[3])
  getMonthArr(ruleArr[4])
  getWeekArr(ruleArr[5])
  getYearArr(ruleArr[6], nYear)

  const sDate = dateArr.value[0]
  const mDate = dateArr.value[1]
  const hDate = dateArr.value[2]
  const DDate = dateArr.value[3]
  const MDate = dateArr.value[4]
  const YDate = dateArr.value[5]

  let sIdx = getIndex(sDate, nSecond)
  let mIdx = getIndex(mDate, nMin)
  let hIdx = getIndex(hDate, nHour)
  let DIdx = getIndex(DDate, nDay)
  let MIdx = getIndex(MDate, nMonth)
  let YIdx = getIndex(YDate, nYear)

  const resetSecond = () => {
    sIdx = 0
    nSecond = sDate[sIdx]
  }
  const resetMin = () => {
    mIdx = 0
    nMin = mDate[mIdx]
    resetSecond()
  }
  const resetHour = () => {
    hIdx = 0
    nHour = hDate[hIdx]
    resetMin()
  }
  const resetDay = () => {
    DIdx = 0
    nDay = DDate[DIdx]
    resetHour()
  }
  const resetMonth = () => {
    MIdx = 0
    nMonth = MDate[MIdx]
    resetDay()
  }

  if (nYear !== YDate[YIdx]) resetMonth()
  if (nMonth !== MDate[MIdx]) resetDay()
  if (nDay !== DDate[DIdx]) resetHour()
  if (nHour !== hDate[hIdx]) resetMin()
  if (nMin !== mDate[mIdx]) resetSecond()

  goYear: for (let Yi = YIdx; Yi < YDate.length; Yi++) {
    const YY = YDate[Yi]
    if (nMonth > MDate[MDate.length - 1]) {
      resetMonth()
      continue
    }
    goMonth: for (let Mi = MIdx; Mi < MDate.length; Mi++) {
      let MM = MDate[Mi]
      MM = MM < 10 ? '0' + MM : MM
      if (nDay > DDate[DDate.length - 1]) {
        resetDay()
        if (Mi === MDate.length - 1) {
          resetMonth()
          continue goYear
        }
        continue
      }
      goDay: for (let Di = DIdx; Di < DDate.length; Di++) {
        let DD = DDate[Di]
        let thisDD = DD < 10 ? '0' + DD : DD
        if (nHour > hDate[hDate.length - 1]) {
          resetHour()
          if (Di === DDate.length - 1) {
            resetDay()
            if (Mi === MDate.length - 1) {
              resetMonth()
              continue goYear
            }
            continue goMonth
          }
          continue
        }
        if (
          checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true &&
          dayRule.value !== 'workDay' &&
          dayRule.value !== 'lastWeek' &&
          dayRule.value !== 'lastDay'
        ) {
          resetDay()
          continue goMonth
        }
        if (dayRule.value === 'lastDay') {
          if (checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true) {
            while (
              DD > 0 &&
              checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true
            ) {
              DD--
              thisDD = DD < 10 ? '0' + DD : DD
            }
          }
        } else if (dayRule.value === 'workDay') {
          if (checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true) {
            while (
              DD > 0 &&
              checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true
            ) {
              DD--
              thisDD = DD < 10 ? '0' + DD : DD
            }
          }
          const thisWeek = formatDate(
            new Date(YY + '-' + MM + '-' + thisDD + ' 00:00:00'),
            'week'
          )
          if (thisWeek === 1) {
            DD++
            thisDD = DD < 10 ? '0' + DD : DD
            if (
              checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true
            ) {
              DD -= 3
            }
          } else if (thisWeek === 7) {
            if (dayRuleSup.value !== 1) {
              DD--
            } else {
              DD += 2
            }
          }
        } else if (dayRule.value === 'weekDay') {
          const thisWeek = formatDate(
            new Date(YY + '-' + MM + '-' + DD + ' 00:00:00'),
            'week'
          )
          if (dayRuleSup.value.indexOf(thisWeek) < 0) {
            if (Di === DDate.length - 1) {
              resetDay()
              if (Mi === MDate.length - 1) {
                resetMonth()
                continue goYear
              }
              continue goMonth
            }
            continue
          }
        } else if (dayRule.value === 'assWeek') {
          const thisWeek = formatDate(
            new Date(YY + '-' + MM + '-' + DD + ' 00:00:00'),
            'week'
          )
          if (dayRuleSup.value[1] >= thisWeek) {
            DD =
              (dayRuleSup.value[0] - 1) * 7 + dayRuleSup.value[1] - thisWeek + 1
          } else {
            DD = dayRuleSup.value[0] * 7 + dayRuleSup.value[1] - thisWeek + 1
          }
        } else if (dayRule.value === 'lastWeek') {
          if (checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true) {
            while (
              DD > 0 &&
              checkDate(YY + '-' + MM + '-' + thisDD + ' 00:00:00') !== true
            ) {
              DD--
              thisDD = DD < 10 ? '0' + DD : DD
            }
          }
          const thisWeek = formatDate(
            new Date(YY + '-' + MM + '-' + thisDD + ' 00:00:00'),
            'week'
          )
          if (dayRuleSup.value < thisWeek) {
            DD -= thisWeek - dayRuleSup.value
          } else if (dayRuleSup.value > thisWeek) {
            DD -= 7 - (dayRuleSup.value - thisWeek)
          }
        }
        DD = DD < 10 ? '0' + DD : DD
        goHour: for (let hi = hIdx; hi < hDate.length; hi++) {
          const hh = hDate[hi] < 10 ? '0' + hDate[hi] : hDate[hi]
          if (nMin > mDate[mDate.length - 1]) {
            resetMin()
            if (hi === hDate.length - 1) {
              resetHour()
              if (Di === DDate.length - 1) {
                resetDay()
                if (Mi === MDate.length - 1) {
                  resetMonth()
                  continue goYear
                }
                continue goMonth
              }
              continue goDay
            }
            continue
          }
          goMin: for (let mi = mIdx; mi < mDate.length; mi++) {
            const mm = mDate[mi] < 10 ? '0' + mDate[mi] : mDate[mi]
            if (nSecond > sDate[sDate.length - 1]) {
              resetSecond()
              if (mi === mDate.length - 1) {
                resetMin()
                if (hi === hDate.length - 1) {
                  resetHour()
                  if (Di === DDate.length - 1) {
                    resetDay()
                    if (Mi === MDate.length - 1) {
                      resetMonth()
                      continue goYear
                    }
                    continue goMonth
                  }
                  continue goDay
                }
                continue goHour
              }
              continue
            }
            goSecond: for (let si = sIdx; si <= sDate.length - 1; si++) {
              const ss = sDate[si] < 10 ? '0' + sDate[si] : sDate[si]
              if (MM !== '00' && DD !== '00') {
                resultArr.push(
                  YY + '-' + MM + '-' + DD + ' ' + hh + ':' + mm + ':' + ss
                )
                nums++
              }
              if (nums === 5) break goYear
              if (si === sDate.length - 1) {
                resetSecond()
                if (mi === mDate.length - 1) {
                  resetMin()
                  if (hi === hDate.length - 1) {
                    resetHour()
                    if (Di === DDate.length - 1) {
                      resetDay()
                      if (Mi === MDate.length - 1) {
                        resetMonth()
                        continue goYear
                      }
                      continue goMonth
                    }
                    continue goDay
                  }
                  continue goHour
                }
                continue goMin
              }
            }
          }
        }
      }
    }
  }

  if (resultArr.length === 0) {
    resultList.value = ['没有达到条件的结果！']
  } else {
    resultList.value = resultArr
    if (resultArr.length !== 5) {
      resultList.value.push(
        '最近100年内只有上面' + resultArr.length + '条结果！'
      )
    }
  }
  isShow.value = true
}

function getIndex(arr, value) {
  if (value <= arr[0] || value > arr[arr.length - 1]) {
    return 0
  }
  for (let i = 0; i < arr.length - 1; i++) {
    if (value > arr[i] && value <= arr[i + 1]) {
      return i + 1
    }
  }
}

function getYearArr(rule, year) {
  dateArr.value[5] = getOrderArr(year, year + 100)
  if (rule !== undefined) {
    if (rule.indexOf('-') >= 0) {
      dateArr.value[5] = getCycleArr(rule, year + 100, false)
    } else if (rule.indexOf('/') >= 0) {
      dateArr.value[5] = getAverageArr(rule, year + 100)
    } else if (rule !== '*') {
      dateArr.value[5] = getAssignArr(rule)
    }
  }
}

function getMonthArr(rule) {
  dateArr.value[4] = getOrderArr(1, 12)
  if (rule.indexOf('-') >= 0) {
    dateArr.value[4] = getCycleArr(rule, 12, false)
  } else if (rule.indexOf('/') >= 0) {
    dateArr.value[4] = getAverageArr(rule, 12)
  } else if (rule !== '*') {
    dateArr.value[4] = getAssignArr(rule)
  }
}

function getWeekArr(rule) {
  if (dayRule.value === '' && dayRuleSup.value === '') {
    if (rule.indexOf('-') >= 0) {
      dayRule.value = 'weekDay'
      dayRuleSup.value = getCycleArr(rule, 7, false)
    } else if (rule.indexOf('#') >= 0) {
      dayRule.value = 'assWeek'
      const matchRule = rule.match(/[0-9]{1}/g)
      dayRuleSup.value = [Number(matchRule[1]), Number(matchRule[0])]
      dateArr.value[3] = [1]
      if (dayRuleSup.value[1] === 7) {
        dayRuleSup.value[1] = 0
      }
    } else if (rule.indexOf('L') >= 0) {
      dayRule.value = 'lastWeek'
      dayRuleSup.value = Number(rule.match(/[0-9]{1,2}/g)[0])
      dateArr.value[3] = [31]
      if (dayRuleSup.value === 7) {
        dayRuleSup.value = 0
      }
    } else if (rule !== '*' && rule !== '?') {
      dayRule.value = 'weekDay'
      dayRuleSup.value = getAssignArr(rule)
    }
  }
}

function getDayArr(rule) {
  dateArr.value[3] = getOrderArr(1, 31)
  dayRule.value = ''
  dayRuleSup.value = ''
  if (rule.indexOf('-') >= 0) {
    dateArr.value[3] = getCycleArr(rule, 31, false)
    dayRuleSup.value = 'null'
  } else if (rule.indexOf('/') >= 0) {
    dateArr.value[3] = getAverageArr(rule, 31)
    dayRuleSup.value = 'null'
  } else if (rule.indexOf('W') >= 0) {
    dayRule.value = 'workDay'
    dayRuleSup.value = Number(rule.match(/[0-9]{1,2}/g)[0])
    dateArr.value[3] = [dayRuleSup.value]
  } else if (rule.indexOf('L') >= 0) {
    dayRule.value = 'lastDay'
    dayRuleSup.value = 'null'
    dateArr.value[3] = [31]
  } else if (rule !== '*' && rule !== '?') {
    dateArr.value[3] = getAssignArr(rule)
    dayRuleSup.value = 'null'
  } else if (rule === '*') {
    dayRuleSup.value = 'null'
  }
}

function getHourArr(rule) {
  dateArr.value[2] = getOrderArr(0, 23)
  if (rule.indexOf('-') >= 0) {
    dateArr.value[2] = getCycleArr(rule, 24, true)
  } else if (rule.indexOf('/') >= 0) {
    dateArr.value[2] = getAverageArr(rule, 23)
  } else if (rule !== '*') {
    dateArr.value[2] = getAssignArr(rule)
  }
}

function getMinArr(rule) {
  dateArr.value[1] = getOrderArr(0, 59)
  if (rule.indexOf('-') >= 0) {
    dateArr.value[1] = getCycleArr(rule, 60, true)
  } else if (rule.indexOf('/') >= 0) {
    dateArr.value[1] = getAverageArr(rule, 59)
  } else if (rule !== '*') {
    dateArr.value[1] = getAssignArr(rule)
  }
}

function getSecondArr(rule) {
  dateArr.value[0] = getOrderArr(0, 59)
  if (rule.indexOf('-') >= 0) {
    dateArr.value[0] = getCycleArr(rule, 60, true)
  } else if (rule.indexOf('/') >= 0) {
    dateArr.value[0] = getAverageArr(rule, 59)
  } else if (rule !== '*') {
    dateArr.value[0] = getAssignArr(rule)
  }
}

function getOrderArr(min, max) {
  const arr = []
  for (let i = min; i <= max; i++) {
    arr.push(i)
  }
  return arr
}

function getAssignArr(rule) {
  const arr = []
  const assiginArr = rule.split(',')
  for (let i = 0; i < assiginArr.length; i++) {
    arr[i] = Number(assiginArr[i])
  }
  arr.sort(compare)
  return arr
}

function getAverageArr(rule, limit) {
  const arr = []
  const agArr = rule.split('/')
  let min = Number(agArr[0])
  const step = Number(agArr[1])
  while (min <= limit) {
    arr.push(min)
    min += step
  }
  return arr
}

function getCycleArr(rule, limit, status) {
  const arr = []
  const cycleArr = rule.split('-')
  let min = Number(cycleArr[0])
  let max = Number(cycleArr[1])
  if (min > max) {
    max += limit
  }
  for (let i = min; i <= max; i++) {
    let add = 0
    if (status === false && i % limit === 0) {
      add = limit
    }
    arr.push(Math.round((i % limit) + add))
  }
  arr.sort(compare)
  return arr
}

function compare(value1, value2) {
  return value2 - value1 > 0 ? -1 : 1
}

function formatDate(value, type) {
  const time = typeof value == 'number' ? new Date(value) : value
  const Y = time.getFullYear()
  const M = time.getMonth() + 1
  const D = time.getDate()
  const h = time.getHours()
  const m = time.getMinutes()
  const s = time.getSeconds()
  const week = time.getDay()
  if (type === undefined) {
    return (
      Y +
      '-' +
      (M < 10 ? '0' + M : M) +
      '-' +
      (D < 10 ? '0' + D : D) +
      ' ' +
      (h < 10 ? '0' + h : h) +
      ':' +
      (m < 10 ? '0' + m : m) +
      ':' +
      (s < 10 ? '0' + s : s)
    )
  }
  if (type === 'week') {
    return week + 1
  }
}

function checkDate(value) {
  const time = new Date(value)
  const format = formatDate(time)
  return value === format
}

onMounted(() => {
  expressionChange()
})
</script>

<style scoped lang="scss">
.popup-result {
  box-sizing: border-box;
  line-height: 24px;
  margin: 25px auto 10px;
  padding: 15px 10px 10px;
  border: 1px solid var(--ba-border-color);
  position: relative;
}

.popup-result .title {
  position: absolute;
  top: -28px;
  left: 50%;
  width: 140px;
  font-size: 14px;
  margin-left: -70px;
  text-align: center;
  line-height: 30px;
  background: var(--ba-bg-color-overlay, #fff);
}

.popup-result-scroll {
  font-size: 12px;
  line-height: 24px;
  height: 10em;
  overflow-y: auto;
  margin: 0;
  padding: 0;
  list-style: none;
}
</style>
