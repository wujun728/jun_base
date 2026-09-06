<script setup>
import CrontabSecond from './second.vue'
import CrontabMin from './min.vue'
import CrontabHour from './hour.vue'
import CrontabDay from './day.vue'
import CrontabMonth from './month.vue'
import CrontabWeek from './week.vue'
import CrontabYear from './year.vue'
import CrontabResult from './result.vue'

const emit = defineEmits(['hide', 'fill'])
const props = defineProps({
  hideComponent: {
    type: Array,
    default: () => [],
  },
  expression: {
    type: String,
    default: '',
  },
})

const tabTitles = ['秒', '分钟', '小时', '日', '月', '周', '年']
const hideComponent = ref([])
const expression = ref('')
const crontabValueObj = ref({
  second: '*',
  min: '*',
  hour: '*',
  day: '*',
  month: '*',
  week: '?',
  year: '',
})

const crontabValueString = computed(() => {
  const obj = crontabValueObj.value
  return (
    obj.second +
    ' ' +
    obj.min +
    ' ' +
    obj.hour +
    ' ' +
    obj.day +
    ' ' +
    obj.month +
    ' ' +
    obj.week +
    (obj.year === '' ? '' : ' ' + obj.year)
  )
})

const resultCells = computed(() => [
  { key: 'second', label: crontabValueObj.value.second },
  { key: 'min', label: crontabValueObj.value.min },
  { key: 'hour', label: crontabValueObj.value.hour },
  { key: 'day', label: crontabValueObj.value.day },
  { key: 'month', label: crontabValueObj.value.month },
  { key: 'week', label: crontabValueObj.value.week },
  { key: 'year', label: crontabValueObj.value.year },
])

watch(expression, () => resolveExp())

function shouldHide(key) {
  return !(hideComponent.value && hideComponent.value.includes(key))
}

function resolveExp() {
  if (expression.value) {
    const arr = expression.value.split(/\s+/)
    if (arr.length >= 6) {
      crontabValueObj.value = {
        second: arr[0],
        min: arr[1],
        hour: arr[2],
        day: arr[3],
        month: arr[4],
        week: arr[5],
        year: arr[6] ? arr[6] : '',
      }
    }
  } else {
    clearCron()
  }
}

function updateCrontabValue(name, value) {
  crontabValueObj.value[name] = value
}

function checkNumber(value, minLimit, maxLimit) {
  value = Math.floor(value)
  if (value < minLimit) {
    value = minLimit
  } else if (value > maxLimit) {
    value = maxLimit
  }
  return value
}

function hidePopup() {
  emit('hide')
}

function submitFill() {
  emit('fill', crontabValueString.value)
  hidePopup()
}

function clearCron() {
  crontabValueObj.value = {
    second: '*',
    min: '*',
    hour: '*',
    day: '*',
    month: '*',
    week: '?',
    year: '',
  }
}

onMounted(() => {
  expression.value = props.expression
  hideComponent.value = props.hideComponent
})

defineExpose({
  submitFill,
  clearCron,
  hidePopup,
})
</script>

<template>
  <div class="crontab">
    <a-tabs type="card">
      <a-tab-pane v-if="shouldHide('second')" key="second" tab="秒">
        <div class="tab-scroll">
          <CrontabSecond
            :check="checkNumber"
            :cron="crontabValueObj"
            @update="updateCrontabValue"
          />
        </div>
      </a-tab-pane>
      <a-tab-pane v-if="shouldHide('min')" key="min" tab="分钟">
        <div class="tab-scroll">
          <CrontabMin :check="checkNumber" :cron="crontabValueObj" @update="updateCrontabValue" />
        </div>
      </a-tab-pane>
      <a-tab-pane v-if="shouldHide('hour')" key="hour" tab="小时">
        <div class="tab-scroll">
          <CrontabHour :check="checkNumber" :cron="crontabValueObj" @update="updateCrontabValue" />
        </div>
      </a-tab-pane>
      <a-tab-pane v-if="shouldHide('day')" key="day" tab="日">
        <div class="tab-scroll">
          <CrontabDay :check="checkNumber" :cron="crontabValueObj" @update="updateCrontabValue" />
        </div>
      </a-tab-pane>
      <a-tab-pane v-if="shouldHide('month')" key="month" tab="月">
        <div class="tab-scroll">
          <CrontabMonth :check="checkNumber" :cron="crontabValueObj" @update="updateCrontabValue" />
        </div>
      </a-tab-pane>
      <a-tab-pane v-if="shouldHide('week')" key="week" tab="周">
        <div class="tab-scroll">
          <CrontabWeek :check="checkNumber" :cron="crontabValueObj" @update="updateCrontabValue" />
        </div>
      </a-tab-pane>
      <a-tab-pane v-if="shouldHide('year')" key="year" tab="年">
        <div class="tab-scroll">
          <CrontabYear :check="checkNumber" :cron="crontabValueObj" @update="updateCrontabValue" />
        </div>
      </a-tab-pane>
    </a-tabs>

    <div class="popup-main">
      <div class="popup-result">
        <p class="title">时间表达式</p>
        <table>
          <thead>
            <tr>
              <th v-for="item in tabTitles" :key="item">{{ item }}</th>
              <th>Cron 表达式</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td v-for="cell in resultCells" :key="cell.key">
                <a-tooltip v-if="String(cell.label).length >= 10" :title="cell.label">
                  <span>{{ cell.label }}</span>
                </a-tooltip>
                <span v-else>{{ cell.label }}</span>
              </td>
              <td class="result">
                <a-tooltip
                  v-if="crontabValueString.length >= 90"
                  :title="crontabValueString"
                >
                  <span>{{ crontabValueString }}</span>
                </a-tooltip>
                <span v-else>{{ crontabValueString }}</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <CrontabResult :ex="crontabValueString" />
    </div>
  </div>
</template>

<style scoped lang="scss">
.tab-scroll {
  max-height: 280px;
  overflow-y: auto;
  padding-right: 4px;
}

.popup-main {
  position: relative;
  margin: 10px auto 0;
  background: var(--ba-bg-color-overlay, #fff);
  border-radius: 5px;
  font-size: 12px;
  overflow: hidden;
}

.popup-result {
  box-sizing: border-box;
  line-height: 24px;
  margin: 25px auto 0;
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

.popup-result table {
  text-align: center;
  width: 100%;
  margin: 0 auto;
  border-collapse: collapse;
}

.popup-result table th,
.popup-result table td {
  padding: 0 4px;
}

.popup-result table span {
  display: block;
  width: 100%;
  font-family: arial;
  line-height: 30px;
  height: 30px;
  white-space: nowrap;
  overflow: hidden;
  border: 1px solid var(--ba-border-color);
}
</style>
