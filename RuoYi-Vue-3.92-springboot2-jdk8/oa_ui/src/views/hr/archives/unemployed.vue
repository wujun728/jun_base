<!-- 人事-员工档案-未入职员工 -->
<template>
  <div>
    <index ref="index" component-name="notNewEmployees"></index>
  </div>
</template>

<script>
import index from './index'
export default {
  name: 'Unemployed',
  data() {
    return {}
  },
  components: {
    index
  },
  mounted() {
    this.$refs.index.setTabTypes(0)
  }
}
</script>
