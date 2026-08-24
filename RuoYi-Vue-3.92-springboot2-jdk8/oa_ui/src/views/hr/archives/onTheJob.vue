<!-- 人事-员工档案-在职员工 -->
<template>
  <div>
    <index ref="index" component-name="jobEmployees"></index>
  </div>
</template>

<script>
import index from './index'
export default {
  name: 'OnTheJob',
  data() {
    return {}
  },
  components: {
    index
  },
  mounted() {
    this.$refs.index.setTabTypes(1)
  }
}
</script>
