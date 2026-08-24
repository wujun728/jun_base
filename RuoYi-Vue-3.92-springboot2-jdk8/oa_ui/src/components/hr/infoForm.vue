<!-- @FileDescription: 人事-审批设置-请假控件组对应的配置组件 -->
<template>
  <div class="composite-selector">
    <div class="label">
      <span>{{ title }}</span>
      <span v-if="title == '假期类型'" class="cursor" @click="goUrl()" style="color: #1890ff">修改</span>
    </div>
    <div class="info" v-html="value"></div>
  </div>
</template>

<script>
import { roterPre } from '@/settings'

export default {
  name: 'InfoForm',
  props: {
    value: {
      default: undefined,
      type: String
    },
    title: {
      default: undefined,
      type: String
    }
  },
  data() {
    return {}
  },
  methods: {
    goUrl() {
      this.$router.push({
        path: `${roterPre}/business/holidayType/index`,
        query: {}
      })
    }
  }
}
</script>

<style scoped lang="scss">
.label {
  display: flex;
  justify-content: space-between;
}
.info {
  font-size: 13px;
  color: #909399;
}
.cursor {
  cursor: pointer;
}
</style>
