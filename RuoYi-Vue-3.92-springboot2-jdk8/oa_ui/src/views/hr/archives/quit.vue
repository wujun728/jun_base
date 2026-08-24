<!-- 人事-员工档案-离职员工 -->
<template>
  <div>
    <index ref="index" component-name="resignedEmployee"></index>
  </div>
</template>

<script>
import index from './index'
export default {
  name: 'Quit',
  data() {
    return {}
  },
  components: {
    index
  },
  mounted() {
    this.$refs.index.setTabTypes(2)
  }
}
</script>
