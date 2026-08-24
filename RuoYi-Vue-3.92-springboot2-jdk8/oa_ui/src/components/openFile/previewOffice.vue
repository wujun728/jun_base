<template>
  <div class="box">
    <iframe :src="officeUrl" width="100%" height="100%" frameBorder="0"></iframe>
  </div>
</template>

<script>
// .https://view.xdocin.com/view?src=
export default {
  name: '',
  props: {
    url: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      officeUrl: ''
    }
  },

  mounted() {
    this.officeUrl = `https://view.officeapps.live.com/op/embed.aspx?src=${encodeURIComponent(this.url)}`
    // this.officeUrl = `https://view.officeapps.live.com/op/view.aspx?src=${this.url}`
  }
}
</script>
<style scoped lang="scss">
.box {
  height: calc(100vh - 85px);
}
.Embed.NewEmbed .WACContainer {
  background: red;
}
</style>
