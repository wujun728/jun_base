<template>
  <div class="divBox">
    <el-card class="card-box">
      <div class="form-wrapper">
        <!--考核计划-->
        <plan />
      </div>
    </el-card>
  </div>
</template>

<script>
import plan from './components/plan'
export default {
  name: 'Plan',
  components: {
    plan
  }
}
</script>

<style lang="scss" scoped>
.card-box {
  height: calc(100vh - 130px);
}
</style>
