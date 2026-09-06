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
const checkboxList = ref([])
const checkCopy = ref([1])

const monthList = [
  { key: 1, value: '一月' },
  { key: 2, value: '二月' },
  { key: 3, value: '三月' },
  { key: 4, value: '四月' },
  { key: 5, value: '五月' },
  { key: 6, value: '六月' },
  { key: 7, value: '七月' },
  { key: 8, value: '八月' },
  { key: 9, value: '九月' },
  { key: 10, value: '十月' },
  { key: 11, value: '十一月' },
  { key: 12, value: '十二月' },
]

const cycleTotal = computed(() => {
  cycle01.value = props.check(cycle01.value, 1, 11)
  cycle02.value = props.check(cycle02.value, cycle01.value + 1, 12)
  return cycle01.value + '-' + cycle02.value
})

const averageTotal = computed(() => {
  average01.value = props.check(average01.value, 1, 11)
  average02.value = props.check(average02.value, 1, 12 - average01.value)
  return average01.value + '/' + average02.value
})

const checkboxString = computed(() => checkboxList.value.join(','))

watch(
  () => props.cron.month,
  (value) => changeRadioValue(value)
)

watch([radioValue, cycleTotal, averageTotal, checkboxString], () => onRadioChange())

function changeRadioValue(value) {
  if (value === '*') {
    radioValue.value = 1
  } else if (value.indexOf('-') > -1) {
    const indexArr = value.split('-')
    cycle01.value = Number(indexArr[0])
    cycle02.value = Number(indexArr[1])
    radioValue.value = 2
  } else if (value.indexOf('/') > -1) {
    const indexArr = value.split('/')
    average01.value = Number(indexArr[0])
    average02.value = Number(indexArr[1])
    radioValue.value = 3
  } else {
    checkboxList.value = [...new Set(value.split(',').map((item) => Number(item)))]
    radioValue.value = 4
  }
}

function onRadioChange() {
  switch (radioValue.value) {
    case 1:
      emit('update', 'month', '*', 'month')
      break
    case 2:
      emit('update', 'month', cycleTotal.value, 'month')
      break
    case 3:
      emit('update', 'month', averageTotal.value, 'month')
      break
    case 4:
      if (checkboxList.value.length === 0) {
        checkboxList.value.push(checkCopy.value[0])
      } else {
        checkCopy.value = checkboxList.value
      }
      emit('update', 'month', checkboxString.value, 'month')
      break
  }
}
</script>

<template>
  <a-radio-group v-model:value="radioValue" class="crontab-radio-group">
    <div class="radio-row"><a-radio :value="1">月，允许的通配符[, - * /]</a-radio></div>
    <div class="radio-row">
      <a-radio :value="2">
        周期从
        <a-input-number v-model:value="cycle01" :min="1" :max="11" size="small" />
        -
        <a-input-number v-model:value="cycle02" :min="cycle01 + 1" :max="12" size="small" />
        月
      </a-radio>
    </div>
    <div class="radio-row">
      <a-radio :value="3">
        从
        <a-input-number v-model:value="average01" :min="1" :max="11" size="small" />
        月开始，每
        <a-input-number v-model:value="average02" :min="1" :max="12 - average01" size="small" />
        月月执行一次
      </a-radio>
    </div>
    <div class="radio-row">
      <a-radio :value="4">
        指定
        <a-select
          v-model:value="checkboxList"
          mode="multiple"
          allow-clear
          placeholder="可多选"
          :max-tag-count="8"
          class="field-select"
          @click.stop
        >
          <a-select-option v-for="item in monthList" :key="item.key" :value="item.key">
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
.field-select {
  margin: 0 0.2rem;
}

.field-select {
  width: 18.8rem;
}
</style>
