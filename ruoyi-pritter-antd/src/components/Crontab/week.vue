<script setup>
const emit = defineEmits(['update'])
const props = defineProps({
  cron: {
    type: Object,
    default: () => ({
      second: '*',
      min: '*',
      hour: '*',
      day: '*',
      month: '*',
      week: '?',
      year: '',
    }),
  },
  check: {
    type: Function,
    default: () => {},
  },
})

const radioValue = ref(2)
const cycle01 = ref(2)
const cycle02 = ref(3)
const average01 = ref(1)
const average02 = ref(2)
const weekday = ref(2)
const checkboxList = ref([])
const checkCopy = ref([2])

const weekList = [
  { key: 1, value: '星期日' },
  { key: 2, value: '星期一' },
  { key: 3, value: '星期二' },
  { key: 4, value: '星期三' },
  { key: 5, value: '星期四' },
  { key: 6, value: '星期五' },
  { key: 7, value: '星期六' },
]

const cycleTotal = computed(() => {
  cycle01.value = props.check(cycle01.value, 1, 6)
  cycle02.value = props.check(cycle02.value, cycle01.value + 1, 7)
  return cycle01.value + '-' + cycle02.value
})

const averageTotal = computed(() => {
  average01.value = props.check(average01.value, 1, 4)
  average02.value = props.check(average02.value, 1, 7)
  return average02.value + '#' + average01.value
})

const weekdayTotal = computed(() => {
  weekday.value = props.check(weekday.value, 1, 7)
  return weekday.value + 'L'
})

const checkboxString = computed(() => checkboxList.value.join(','))

watch(
  () => props.cron.week,
  (value) => changeRadioValue(value)
)

watch([radioValue, cycleTotal, averageTotal, weekdayTotal, checkboxString], () =>
  onRadioChange()
)

function changeRadioValue(value) {
  if (value === '*') {
    radioValue.value = 1
  } else if (value === '?') {
    radioValue.value = 2
  } else if (value.indexOf('-') > -1) {
    const indexArr = value.split('-')
    cycle01.value = Number(indexArr[0])
    cycle02.value = Number(indexArr[1])
    radioValue.value = 3
  } else if (value.indexOf('#') > -1) {
    const indexArr = value.split('#')
    average01.value = Number(indexArr[1])
    average02.value = Number(indexArr[0])
    radioValue.value = 4
  } else if (value.indexOf('L') > -1) {
    const indexArr = value.split('L')
    weekday.value = Number(indexArr[0])
    radioValue.value = 5
  } else {
    checkboxList.value = [...new Set(value.split(',').map((item) => Number(item)))]
    radioValue.value = 6
  }
}

function onRadioChange() {
  if (radioValue.value === 2 && props.cron.day === '?') {
    emit('update', 'day', '*', 'week')
  }
  if (radioValue.value !== 2 && props.cron.day !== '?') {
    emit('update', 'day', '?', 'week')
  }
  switch (radioValue.value) {
    case 1:
      emit('update', 'week', '*', 'week')
      break
    case 2:
      emit('update', 'week', '?', 'week')
      break
    case 3:
      emit('update', 'week', cycleTotal.value, 'week')
      break
    case 4:
      emit('update', 'week', averageTotal.value, 'week')
      break
    case 5:
      emit('update', 'week', weekdayTotal.value, 'week')
      break
    case 6:
      if (checkboxList.value.length === 0) {
        checkboxList.value.push(checkCopy.value[0])
      } else {
        checkCopy.value = checkboxList.value
      }
      emit('update', 'week', checkboxString.value, 'week')
      break
  }
}
</script>

<template>
  <a-radio-group v-model:value="radioValue" class="crontab-radio-group">
    <div class="radio-row"><a-radio :value="1">周，允许的通配符[, - * ? / L #]</a-radio></div>
    <div class="radio-row"><a-radio :value="2">不指定</a-radio></div>
    <div class="radio-row">
      <a-radio :value="3">
        周期从
        <a-select v-model:value="cycle01" allow-clear class="field-select-sm" @click.stop>
          <a-select-option
            v-for="item in weekList"
            :key="item.key"
            :value="item.key"
            :disabled="item.key === 7"
          >
            {{ item.value }}
          </a-select-option>
        </a-select>
        -
        <a-select v-model:value="cycle02" allow-clear class="field-select-sm" @click.stop>
          <a-select-option
            v-for="item in weekList"
            :key="item.key"
            :value="item.key"
            :disabled="item.key <= cycle01"
          >
            {{ item.value }}
          </a-select-option>
        </a-select>
      </a-radio>
    </div>
    <div class="radio-row">
      <a-radio :value="4">
        第
        <a-input-number v-model:value="average01" :min="1" :max="4" size="small" />
        周的
        <a-select v-model:value="average02" allow-clear class="field-select-sm" @click.stop>
          <a-select-option v-for="item in weekList" :key="item.key" :value="item.key">
            {{ item.value }}
          </a-select-option>
        </a-select>
      </a-radio>
    </div>
    <div class="radio-row">
      <a-radio :value="5">
        本月最后一个
        <a-select v-model:value="weekday" allow-clear class="field-select-sm" @click.stop>
          <a-select-option v-for="item in weekList" :key="item.key" :value="item.key">
            {{ item.value }}
          </a-select-option>
        </a-select>
      </a-radio>
    </div>
    <div class="radio-row">
      <a-radio :value="6">
        指定
        <a-select
          v-model:value="checkboxList"
          mode="multiple"
          allow-clear
          placeholder="可多选"
          :max-tag-count="6"
          class="field-select-lg"
          @click.stop
        >
          <a-select-option v-for="item in weekList" :key="item.key" :value="item.key">
            {{ item.value }}
          </a-select-option>
        </a-select>
      </a-radio>
    </div>
  </a-radio-group>
</template>

<style scoped lang="scss">
.crontab-radio-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 100%;
}

.radio-row {
  line-height: 32px;
}

.ant-input-number,
.field-select-sm,
.field-select-lg {
  margin: 0 0.5rem;
}

.field-select-sm {
  width: 8rem;
}

.field-select-lg {
  width: 17.8rem;
}
</style>
