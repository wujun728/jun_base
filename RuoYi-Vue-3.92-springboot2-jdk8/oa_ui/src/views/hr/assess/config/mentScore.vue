<template>
  <div class="divBox">
    <el-card class="card-box">
      <div class="form-wrapper">
        <!--考核评分-->
        <explain />
      </div>
    </el-card>
  </div>
</template>

<script>
import explain from './components/explain'
export default {
  name: 'Explain',
  components: {
    explain
  }
}
</script>

<style lang="scss" scoped>
.divBox {
  padding-bottom: 0;
  margin-bottom: 10px;
}
.card-box {
  height: calc(100vh - 130px);
  overflow-y: scroll;
}
</style>
