<template>
  <div class="box">
    <iframe :src="url" class="iframe"></iframe>
  </div>
</template>

<script>
import { getAccount } from '@/utils/format'
export default {
  name: 'ArticleContent',
  data() {
    return {
      url: ``
    }
  },
  mounted() {
    let uniquedId = JSON.parse(localStorage.getItem('enterprise')).uniqued
    let accountNum = getAccount(JSON.parse(localStorage.getItem('userInfo')).phone, uniquedId)
    this.url = `https://doc.tuoluojiang.com/forum?account=${accountNum}&uniqued=${uniquedId}`
  }
}
</script>

<style lang="scss" scoped>
.box {
  margin: 14px;
  padding-top: 14px;
}
.iframe {
  width: 100%;
  height: calc(100vh - 85px);
  border: none;
}
.box::-webkit-scrollbar {
  height: 0;
  width: 0;
}
</style>
