<template>
  <div>
    <el-popover placement="bottom" trigger="hover" popper-class="popover-padding" :width="width">
      <span class="tips"> {{ tips }}。</span>
      <span slot="reference" class="iconfont icona-zu11438"></span>
    </el-popover>
  </div>
</template>
<script>
export default {
  name: '',
  components: {},
  props: {
    tips: { type: String, default: '' },
    width: { type: Number, default: 315 }
  }
}
</script>
<style scoped lang="scss">
.icona-zu11438 {
  font-size: 13px;
  cursor: pointer;
  margin-left: 4px;
  color: rgba(0, 0, 0, 0.45);
}
.tips {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: #606266;
  line-height: 18px;
}
</style>
<style>
.popover-padding {
  padding: 12px !important;
}
</style>
