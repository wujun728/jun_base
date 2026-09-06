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

const radioValue = ref(1)
const cycle01 = ref(1)
const cycle02 = ref(2)
const average01 = ref(1)
const average02 = ref(1)
const workday = ref(1)
const checkboxList = ref([])
const checkCopy = ref([1])

const cycleTotal = computed(() => {
  cycle01.value = props.check(cycle01.value, 1, 30)
  cycle02.value = props.check(cycle02.value, cycle01.value + 1, 31)
  return cycle01.value + '-' + cycle02.value
})

const averageTotal = computed(() => {
  average01.value = props.check(average01.value, 1, 30)
  average02.value = props.check(average02.value, 1, 31 - average01.value)
  return average01.value + '/' + average02.value
})

const workdayTotal = computed(() => {
  workday.value = props.check(workday.value, 1, 31)
  return workday.value + 'W'
})

const checkboxString = computed(() => checkboxList.value.join(','))

watch(
  () => props.cron.day,
  (value) => changeRadioValue(value)
)

watch([radioValue, cycleTotal, averageTotal, workdayTotal, checkboxString], () =>
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
  } else if (value.indexOf('/') > -1) {
    const indexArr = value.split('/')
    average01.value = Number(indexArr[0])
    average02.value = Number(indexArr[1])
    radioValue.value = 4
  } else if (value.indexOf('W') > -1) {
    const indexArr = value.split('W')
    workday.value = Number(indexArr[0])
    radioValue.value = 5
  } else if (value === 'L') {
    radioValue.value = 6
  } else {
    checkboxList.value = [...new Set(value.split(',').map((item) => Number(item)))]
    radioValue.value = 7
  }
}

function onRadioChange() {
  if (radioValue.value === 2 && props.cron.week === '?') {
    emit('update', 'week', '*', 'day')
  }
  if (radioValue.value !== 2 && props.cron.week !== '?') {
    emit('update', 'week', '?', 'day')
  }
  switch (radioValue.value) {
    case 1:
      emit('update', 'day', '*', 'day')
      break
    case 2:
      emit('update', 'day', '?', 'day')
      break
    case 3:
      emit('update', 'day', cycleTotal.value, 'day')
      break
    case 4:
      emit('update', 'day', averageTotal.value, 'day')
      break
    case 5:
      emit('update', 'day', workdayTotal.value, 'day')
      break
    case 6:
      emit('update', 'day', 'L', 'day')
      break
    case 7:
      if (checkboxList.value.length === 0) {
        checkboxList.value.push(checkCopy.value[0])
      } else {
        checkCopy.value = checkboxList.value
      }
      emit('update', 'day', checkboxString.value, 'day')
      break
  }
}
</script>

<template>
  <a-radio-group v-model:value="radioValue" class="crontab-radio-group">
    <div class="radio-row"><a-radio :value="1">日，允许的通配符[, - * ? / L W]</a-radio></div>
    <div class="radio-row"><a-radio :value="2">不指定</a-radio></div>
    <div class="radio-row">
      <a-radio :value="3">
        周期从
        <a-input-number v-model:value="cycle01" :min="1" :max="30" size="small" />
        -
        <a-input-number v-model:value="cycle02" :min="cycle01 + 1" :max="31" size="small" />
        日
      </a-radio>
    </div>
    <div class="radio-row">
      <a-radio :value="4">
        从
        <a-input-number v-model:value="average01" :min="1" :max="30" size="small" />
        号开始，每
        <a-input-number v-model:value="average02" :min="1" :max="31 - average01" size="small" />
        日执行一次
      </a-radio>
    </div>
    <div class="radio-row">
      <a-radio :value="5">
        每月
        <a-input-number v-model:value="workday" :min="1" :max="31" size="small" />
        号最近的那个工作日
      </a-radio>
    </div>
    <div class="radio-row"><a-radio :value="6">本月最后一天</a-radio></div>
    <div class="radio-row">
      <a-radio :value="7">
        指定
        <a-select
          v-model:value="checkboxList"
          mode="multiple"
          allow-clear
          placeholder="可多选"
          :max-tag-count="10"
          class="field-select"
          @click.stop
        >
          <a-select-option v-for="item in 31" :key="item" :value="item">
            {{ item }}
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
.field-select {
  margin: 0 0.2rem;
}

.field-select {
  width: 18.8rem;
}
</style>
