<template>
  <div class="pdf-wrapper">
    <!-- <textarea style="width: 100%; height: 600px" id="txtContent" readonly v-model="htmlMD"></textarea> -->
    <div ref="previewContainer"></div>
    <vue-office-docx :src="urlDocx" />
    <!-- <vue-office-excel :src="urlExcel" @rendered="rendered" /> -->
  </div>
</template>
预览

<script>
//引入相关样式
import '@vue-office/docx/lib/index.css'
// import '@vue-office/excel/lib/index.css'

//引入VueOffice组件
import VueOfficeDocx from '@vue-office/docx'
// import VueOfficeExcel from '@vue-office/excel'
export default {
  name: '',
  props: {
    url: {
      type: String,
      default: ''
    }
  },
  components: {
    VueOfficeDocx,
    VueOfficeExcel
  },

  data() {
    return {
      urlDocx: `https://resources-demo.oss-cn-beijing.aliyuncs.com/1720746767675.docx`,
      urlExcel: `https://resources-demo.oss-cn-beijing.aliyuncs.com/1720749413235.xlsx`
    }
  },
  // watch() {
  //   u
  // },
  mounted() {},
  methods: {}
}
</script>
<style scoped lang="scss">
.pdf-wrapper {
  // padding: 30px;
}
</style>
