<template>
  <div class="text-link">
    <template v-for="item in textArrs" :key="item.title">
      <div class="item">
        <span class="title">{{ item.title }} : </span>
        <el-link type="primary">{{ item.desp }}</el-link>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
type Props = {
  textArrs: { title: string; desp: string }[]
}
const { textArrs } = defineProps<Props>()
</script>

<style scoped lang="scss">
.text-link {
  padding: 0 15px;
  margin-bottom: 40px;
  text-align: left;

  .title {
    position: relative;
    display: inline-block;
    margin-right: 20px;
  }

  .item {
    margin: 5px;
  }

  .title::before {
    position: absolute;
    top: 40%;
    left: -15px;
    width: 5px;
    height: 5px;
    background: black;
    border-radius: 100%;
    content: '';
  }
}
</style>
