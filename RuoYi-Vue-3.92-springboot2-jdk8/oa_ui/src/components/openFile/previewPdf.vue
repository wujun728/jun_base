<template>
  <div class="pdf-wrapper">
    <canvas v-for="page in pages" :id="'the-canvas' + page" :key="page"></canvas>
  </div>
</template>

<script>
import PDFJS from 'pdfjs-dist'
PDFJS.GlobalWorkerOptions.workerSrc = '../../node_modules/pdfjs-dist/build/pdf.worker.js'
export default {
  props: {
    url: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      pdfDoc: null,
      pages: 0
    }
  },
  mounted() {
    this.loadFile()
  },
  methods: {
    showPdf() {
      this.loadFile(this.url)
    },
    loadFile(url) {
      let that = this
      PDFJS.getDocument(that.$processResourceUrl(that.url)).then(function (pdf) {
        that.pdfDoc = pdf
        that.pages = that.pdfDoc.numPages
        that.$nextTick(() => {
          that.renderPage(1)
        })
      })
    },
    renderPage(num) {
      let _this = this
      _this.pdfDoc.getPage(num).then(function (page) {
        let canvas = document.getElementById('the-canvas' + num)
        let ctx = canvas.getContext('2d')
        let dpr = window.devicePixelRatio || 1.0
        let bsr =
          ctx.webkitBackingStorePixelRatio ||
          ctx.mozBackingStorePixelRatio ||
          ctx.msBackingStorePixelRatio ||
          ctx.oBackingStorePixelRatio ||
          ctx.backingStorePixelRatio ||
          1.0
        let ratio = dpr / bsr
        let viewport = page.getViewport(window.screen.availWidth / page.getViewport(1).width)
        canvas.width = viewport.width * ratio
        canvas.height = viewport.height * ratio
        canvas.style.width = viewport.width + 'px'
        // canvas.style.height = viewport.height + 'px'
        ctx.setTransform(ratio, 0, 0, ratio, 0, 0)
        var renderContext = {
          canvasContext: ctx,
          viewport: viewport
        }
        page.render(renderContext)
        if (_this.pages > num) {
          _this.renderPage(num + 1)
        }
      })
    }
  }
}
</script>
<style scoped lang="scss">
.pdf-wrapper {
  width: 100%;
  padding-top: 1px;
}
canvas {
  max-width: 1036px;
  margin: 20px auto;
  display: block;
  box-shadow: 1px 2px 5px 2px #dcdfe6;
}
</style>
