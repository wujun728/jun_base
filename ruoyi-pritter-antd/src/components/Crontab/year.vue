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

const now = Number(new Date().getFullYear())
const fullYear = ref(now)
const maxFullYear = ref(now + 20)
const radioValue = ref(1)
const cycle01 = ref(0)
const cycle02 = ref(0)
const average01 = ref(0)
const average02 = ref(1)
const checkboxList = ref([])
const checkCopy = ref([])

const cycleTotal = computed(() => {
  cycle01.value = props.check(cycle01.value, fullYear.value, maxFullYear.value - 1)
  cycle02.value = props.check(cycle02.value, cycle01.value + 1, maxFullYear.value)
  return cycle01.value + '-' + cycle02.value
})

const averageTotal = computed(() => {
  average01.value = props.check(average01.value, fullYear.value, maxFullYear.value - 1)
  average02.value = props.check(average02.value, 1, 10)
  return average01.value + '/' + average02.value
})

const checkboxString = computed(() => checkboxList.value.join(','))

watch(
  () => props.cron.year,
  (value) => changeRadioValue(value)
)

watch([radioValue, cycleTotal, averageTotal, checkboxString], () => onRadioChange())

function changeRadioValue(value) {
  if (value === '') {
    radioValue.value = 1
  } else if (value === '*') {
    radioValue.value = 2
  } else if (value.indexOf('-') > -1) {
    const indexArr = value.split('-')
    cycle01.value = Number(indexArr[0])
    cycle02.value = Number(indexArr[1])
    radioValue.value = 3
  } else if (value.indexOf('/') > -1) {
    const indexArr = value.split('#')
    if (Number(indexArr[1])) {
      average01.value = Number(indexArr[1])
    }
    if (Number(indexArr[0])) {
      average02.value = Number(indexArr[0])
    }
    radioValue.value = 4
  } else {
    checkboxList.value = [...new Set(value.split(',').map((item) => Number(item)))]
    radioValue.value = 5
  }
}

function onRadioChange() {
  switch (radioValue.value) {
    case 1:
      emit('update', 'year', '', 'year')
      break
    case 2:
      emit('update', 'year', '*', 'year')
      break
    case 3:
      emit('update', 'year', cycleTotal.value, 'year')
      break
    case 4:
      emit('update', 'year', averageTotal.value, 'year')
      break
    case 5:
      if (checkboxList.value.length === 0) {
        checkboxList.value.push(checkCopy.value[0])
      } else {
        checkCopy.value = checkboxList.value
      }
      emit('update', 'year', checkboxString.value, 'year')
      break
  }
}

onMounted(() => {
  cycle01.value = fullYear.value
  cycle02.value = cycle01.value + 1
  average01.value = fullYear.value
  checkCopy.value = [fullYear.value]
})
</script>

<template>
  <a-radio-group v-model:value="radioValue" class="crontab-radio-group">
    <div class="radio-row"><a-radio :value="1">不填，允许的通配符[, - * /]</a-radio></div>
    <div class="radio-row"><a-radio :value="2">每年</a-radio></div>
    <div class="radio-row">
      <a-radio :value="3">
        周期从
        <a-input-number v-model:value="cycle01" :min="fullYear" :max="maxFullYear - 1" size="small" />
        -
        <a-input-number
          v-model:value="cycle02"
          :min="cycle01 + 1"
          :max="maxFullYear"
          size="small"
        />
      </a-radio>
    </div>
    <div class="radio-row">
      <a-radio :value="4">
        从
        <a-input-number
          v-model:value="average01"
          :min="fullYear"
          :max="maxFullYear - 1"
          size="small"
        />
        年开始，每
        <a-input-number v-model:value="average02" :min="1" :max="10" size="small" />
        年执行一次
      </a-radio>
    </div>
    <div class="radio-row">
      <a-radio :value="5">
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
          <a-select-option
            v-for="item in 9"
            :key="item - 1 + fullYear"
            :value="item - 1 + fullYear"
          >
            {{ item - 1 + fullYear }}
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
