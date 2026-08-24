<!-- @FileDescription: 人事-审批设置-金额组件 -->
<template>
  <div>
    <el-input-number
      v-model="num"
      :min="minNum"
      :max="maxNum"
      :precision="precisionNum"
      :step="Math.pow(10, precisionNum ? -precisionNum : 0)"
    ></el-input-number>
    <div class="daxie">大写：{{ numFrom }}</div>
  </div>
</template>

<script>
import { intToChinese } from '@/utils'
export default {
  name: 'MoneyFrom',
  props: {
    value: {
      default: undefined,
      type: [Number, String]
    },
    min: {
      default: undefined,
      type: Number
    },
    max: {
      default: undefined,
      type: Number
    },
    precision: {
      default: undefined,
      type: Number
    }
  },
  data() {
    return {
      num: Number(this.value),
      minNum: this.min,
      maxNum: this.max,
      precisionNum: this.precision
    }
  },
  computed: {
    numFrom: function () {
      return intToChinese(this.num)
    }
  },
  watch: {
    value(n) {
      this.num = Number(n)
    },
    min(n) {
      this.minNum = n
    },
    max(n) {
      this.maxNum = n
    },
    precision(n) {
      this.precisionNum = n
    },
    num() {
      this.onChange()
    }
  },
  methods: {
    onChange() {
      this.$emit('input', this.num)
    }
  }
}
</script>

<style scoped lang="scss">
.daxie {
  color: #999;
  font-size: 13px;
}
</style>
