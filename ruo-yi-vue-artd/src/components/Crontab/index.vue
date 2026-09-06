<template>
  <div>
    <el-tabs type="border-card">
      <el-tab-pane v-if="shouldHide('second')" label="秒">
        <CrontabSecond
          :check="checkNumber"
          :cron="crontabValueObj"
          @update="updateCrontabValue" />
      </el-tab-pane>
      <el-tab-pane v-if="shouldHide('min')" label="分钟">
        <CrontabMin
          :check="checkNumber"
          :cron="crontabValueObj"
          @update="updateCrontabValue" />
      </el-tab-pane>
      <el-tab-pane v-if="shouldHide('hour')" label="小时">
        <CrontabHour
          :check="checkNumber"
          :cron="crontabValueObj"
          @update="updateCrontabValue" />
      </el-tab-pane>
      <el-tab-pane v-if="shouldHide('day')" label="日">
        <CrontabDay
          :check="checkNumber"
          :cron="crontabValueObj"
          @update="updateCrontabValue" />
      </el-tab-pane>
      <el-tab-pane v-if="shouldHide('month')" label="月">
        <CrontabMonth
          :check="checkNumber"
          :cron="crontabValueObj"
          @update="updateCrontabValue" />
      </el-tab-pane>
      <el-tab-pane v-if="shouldHide('week')" label="周">
        <CrontabWeek
          :check="checkNumber"
          :cron="crontabValueObj"
          @update="updateCrontabValue" />
      </el-tab-pane>
      <el-tab-pane v-if="shouldHide('year')" label="年">
        <CrontabYear
          :check="checkNumber"
          :cron="crontabValueObj"
          @update="updateCrontabValue" />
      </el-tab-pane>
    </el-tabs>

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
              <td v-for="key in cronKeys" :key="key">
                <span v-if="crontabValueObj[key].length < 10">
                  {{ crontabValueObj[key] }}
                </span>
                <el-tooltip
                  v-else
                  :content="crontabValueObj[key]"
                  placement="top">
                  <span>{{ crontabValueObj[key] }}</span>
                </el-tooltip>
              </td>
              <td class="result">
                <span v-if="crontabValueString.length < 90">
                  {{ crontabValueString }}
                </span>
                <el-tooltip
                  v-else
                  :content="crontabValueString"
                  placement="top">
                  <span>{{ crontabValueString }}</span>
                </el-tooltip>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="pop-btn">
        <el-button type="primary" @click="submitFill">确定</el-button>
        <el-button type="warning" @click="clearCron">重置</el-button>
        <el-button @click="hidePopup">取消</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { CronValue } from './types'

import CrontabDay from './day.vue'
import CrontabHour from './hour.vue'
import CrontabMin from './min.vue'
import CrontabMonth from './month.vue'
import CrontabSecond from './second.vue'
import CrontabWeek from './week.vue'
import CrontabYear from './year.vue'

defineOptions({ name: 'Crontab' })

const emit = defineEmits<{
  (e: 'hide'): void
  (e: 'fill', value: string): void
}>()
const props = withDefaults(
  defineProps<{
    hideComponent?: string[]
    expression?: string
  }>(),
  {
    hideComponent: () => [],
    expression: ''
  }
)

const tabTitles = ['秒', '分钟', '小时', '日', '月', '周', '年']
const cronKeys: (keyof CronValue)[] = [
  'second',
  'min',
  'hour',
  'day',
  'month',
  'week',
  'year'
]
const crontabValueObj = ref<CronValue>(createDefaultCron())
const crontabValueString = computed(() => {
  const obj = crontabValueObj.value
  return `${obj.second} ${obj.min} ${obj.hour} ${obj.day} ${obj.month} ${obj.week}${obj.year === '' ? '' : ` ${obj.year}`}`
})

watch(() => props.expression, resolveExp, { immediate: true })

function createDefaultCron(): CronValue {
  return {
    second: '*',
    min: '*',
    hour: '*',
    day: '*',
    month: '*',
    week: '?',
    year: ''
  }
}

function shouldHide(key: string) {
  return !props.hideComponent.includes(key)
}

function resolveExp() {
  if (!props.expression) {
    clearCron()
    return
  }
  const arr = props.expression.split(/\s+/)
  if (arr.length >= 6) {
    crontabValueObj.value = {
      second: arr[0],
      min: arr[1],
      hour: arr[2],
      day: arr[3],
      month: arr[4],
      week: arr[5],
      year: arr[6] || ''
    }
  }
}

function updateCrontabValue(name: keyof CronValue, value: string) {
  crontabValueObj.value[name] = value
}

function checkNumber(value: number, minLimit: number, maxLimit: number) {
  const intValue = Math.floor(value)
  if (intValue < minLimit) return minLimit
  if (intValue > maxLimit) return maxLimit
  return intValue
}

function hidePopup() {
  emit('hide')
}

function submitFill() {
  emit('fill', crontabValueString.value)
  hidePopup()
}

function clearCron() {
  crontabValueObj.value = createDefaultCron()
}
</script>

<style scoped lang="scss">
.pop-btn {
  margin-top: 20px;
  text-align: center;
}

.popup-main {
  position: relative;
  margin: 10px auto;
  overflow: hidden;
  font-size: 12px;
  border-radius: 5px;
}

.popup-result {
  position: relative;
  box-sizing: border-box;
  padding: 15px 10px 10px;
  margin: 25px auto;
  line-height: 24px;
  border: 1px solid #ccc;
}

.popup-result .title {
  position: absolute;
  top: -28px;
  left: 50%;
  width: 140px;
  margin-left: -70px;
  font-size: 14px;
  line-height: 30px;
  text-align: center;
  background: var(--el-bg-color);
}

.popup-result table {
  width: 100%;
  margin: 0 auto;
  text-align: center;
}

.popup-result table td:not(.result) {
  width: 3.5rem;
  min-width: 3.5rem;
  max-width: 3.5rem;
}

.popup-result table span {
  display: block;
  width: 100%;
  height: 30px;
  overflow: hidden;
  font-family: arial, sans-serif;
  line-height: 30px;
  white-space: nowrap;
  border: 1px solid #e8e8e8;
}
</style>
