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
const cycle01 = ref(0)
const cycle02 = ref(1)
const average01 = ref(0)
const average02 = ref(1)
const checkboxList = ref([])
const checkCopy = ref([0])

const cycleTotal = computed(() => {
  cycle01.value = props.check(cycle01.value, 0, 22)
  cycle02.value = props.check(cycle02.value, cycle01.value + 1, 23)
  return cycle01.value + '-' + cycle02.value
})

const averageTotal = computed(() => {
  average01.value = props.check(average01.value, 0, 22)
  average02.value = props.check(average02.value, 1, 23 - average01.value)
  return average01.value + '/' + average02.value
})

const checkboxString = computed(() => checkboxList.value.join(','))

watch(
  () => props.cron.hour,
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
      emit('update', 'hour', '*', 'hour')
      break
    case 2:
      emit('update', 'hour', cycleTotal.value, 'hour')
      break
    case 3:
      emit('update', 'hour', averageTotal.value, 'hour')
      break
    case 4:
      if (checkboxList.value.length === 0) {
        checkboxList.value.push(checkCopy.value[0])
      } else {
        checkCopy.value = checkboxList.value
      }
      emit('update', 'hour', checkboxString.value, 'hour')
      break
  }
}
</script>

<template>
  <a-radio-group v-model:value="radioValue" class="crontab-radio-group">
    <div class="radio-row"><a-radio :value="1">小时，允许的通配符[, - * /]</a-radio></div>
    <div class="radio-row">
      <a-radio :value="2">
        周期从
        <a-input-number v-model:value="cycle01" :min="0" :max="22" size="small" />
        -
        <a-input-number v-model:value="cycle02" :min="cycle01 + 1" :max="23" size="small" />
        时
      </a-radio>
    </div>
    <div class="radio-row">
      <a-radio :value="3">
        从
        <a-input-number v-model:value="average01" :min="0" :max="22" size="small" />
        时开始，每
        <a-input-number v-model:value="average02" :min="1" :max="23 - average01" size="small" />
        小时执行一次
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
          :max-tag-count="10"
          class="field-select"
          @click.stop
        >
          <a-select-option v-for="item in 24" :key="item - 1" :value="item - 1">
            {{ item - 1 }}
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
