<template>
  <div class="ivu-global-footer i-copyright">
    <div class="footers acea-row">
      <div class="title">{{ copyright }}</div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'i-copyright',
  data() {
    return {
      copyright: 'Copyright © 2018-2026 RuoYi. All Rights Reserved.',
    };
  },
};
</script>
<style lang="scss" scoped>
.ivu-global-footer {
  text-align: center;
  box-sizing: border-box;
}
.i-copyright {
  flex: 0 0 auto;
}
.ivu-global-footer-links {
  margin-bottom: 8px;
}
.ivu-global-footer-links a:not(:last-child) {
  margin-right: 40px;
}
.ivu-global-footer-links a {
  font-size: 14px;
  color: #808695;
  transition: all 0.2s ease-in-out;
}
.ivu-global-footer-copyright {
  color: #808695;
  font-size: 14px;
}
</style>
