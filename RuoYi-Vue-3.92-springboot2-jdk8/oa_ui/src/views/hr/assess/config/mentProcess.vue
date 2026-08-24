<template>
  <div class="divBox">
    <el-card class="employees-card-bottom">
      <div class="form-wrapper">
        <!--考核流程-->
        <process />
      </div>
    </el-card>
  </div>
</template>

<script>
import process from './components/process'
export default {
  name: 'Process',
  components: {
    process
  }
}
</script>

<style lang="scss" scoped>
.divBox {
  padding-bottom: 0;
  margin-bottom: 10px;
}
</style>
