<!-- @FileDescription: 公共-xlsx导入组件 -->
<template>
  <div class="import-excel">
    <input
      id="referenceUpload"
      class="input-file"
      type="file"
      accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
      @change="exportData"
    />
    <el-button v-show="false" size="small" @click="btnClick">{{
      distinguish == 2 ? '导入' : $t('finance.batchupload')
    }}</el-button>
  </div>
</template>

<script>
import XLSX from 'xlsx'
export default {
  name: 'ImportExcel',
  props: {
    columnNumber: {
      type: Number,
      default: 0
    },
    type: {
      type: String,
      default: ''
    },
    distinguish: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      rowNumber: 1
    }
  },
  methods: {
    btnClick() {
      // 点击事件
      document.querySelector('.input-file').click()
    },
    exportData(event) {
      if (!event.currentTarget.files.length) {
        return
      }
      const that = this
      // 拿取文件对象
      var f = event.currentTarget.files[0]
      // 用FileReader来读取
      var reader = new FileReader()
      // 重写FileReader上的readAsBinaryString方法
      FileReader.prototype.readAsBinaryString = function (f) {
        var binary = ''
        var wb // 读取完成的数据
        var outdata // 你需要的数据
        var reader = new FileReader()
        reader.onload = function () {
          // 读取成Uint8Array，再转换为Unicode编码（Unicode占两个字节）
          var bytes = new Uint8Array(reader.result)
          var length = bytes.byteLength
          for (var i = 0; i < length; i++) {
            binary += String.fromCharCode(bytes[i])
          }
          // 接下来就是xlsx了，具体可看api
          wb = XLSX.read(binary, {
            type: 'binary',
            cellDates: true
          })
          outdata = XLSX.utils.sheet_to_csv(wb.Sheets[wb.SheetNames[0]])
          const arrData = outdata.split('\n')
          const arrRes = []
          for (let i = that.columnNumber; i < arrData.length; i++) {
            if (arrData[i] === '') {
              continue
            } else {
              arrRes.push(arrData[i].split(','))
            }
          }
          document.getElementById('referenceUpload').value = null
          // 自定义方法向父组件传递数据
          that.$emit('importExcelData', arrRes)
        }
        reader.readAsArrayBuffer(f)
      }
      reader.readAsBinaryString(f)
    }
  }
}
</script>

<style lang="scss" scoped>
.import-excel {
  position: fixed;
  top: -300px;
  left: 0;

  .input-file {
    position: fixed;
    top: -300px;
    left: 0;
    width: 100%;
    height: 100%;
    display: none;
  }
}
</style>
