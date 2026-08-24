<template>
  <div class="main-page">
    <div
      id="luckysheet"
      style="margin: 0px; padding: 0px; position: absolute; width: 100%; height: 100%; left: 0px; top: 0px"
    ></div>
    <!-- <div class="export-box"> -->
    <!-- <el-button class="export" size="small" type="primary" @click.stop="exportData(1)">保存</el-button> -->
    <!-- <el-button class="export" size="small" @click="exportData(0)">导出</el-button> -->
    <!-- </div> -->
  </div>
</template>
<script>
import { roterPre } from '@/settings'
import LuckyExcel from 'luckyexcel'
import { exportExcel } from './export'
import { fileUpload } from '@/api/public'
export default {
  name: 'excelEdit',
  props: {
    url: {
      type: String,
      default: ''
    },
    fid: {
      type: String,
      default: ''
    },
    file: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {}
  },
  mounted() {
    this.loadExcel(this.$processResourceUrl(this.url))
  },
  methods: {
    normalizeFormula(formula) {
      return formula.replace(/^=+/, '=')
    },
    exportData(type) {
      let index = this.url.lastIndexOf('/')
      let filename = this.file.name + '.' + this.file.file_ext
      const sheetData = luckysheet.getAllSheets().map((sheet) => {
        const cells = sheet.data.map((row) => {
          return row.map((cell) => {
            if (cell && cell.f) {
              cell.f = this.normalizeFormula(cell.f)
            }
            return cell
          })
        })
        return { ...sheet, data: cells }
      })

      if (type) {
        exportExcel(sheetData, filename, 1).then((res) => {
          const newFile = new File([res], filename, {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
          })
          const formData = new FormData()
          formData.append('content', newFile)
          fileUpload(this.fid, this.file.id, formData).then((res) => {
            if (res.status === 200) {
              this.$emit('closeLoading')
            }
          })
        })
      } else {
        exportExcel(sheetData, filename)
      }
    },
    loadExcel(file) {
      //获取文件名
      let name = this.file.name + '.' + this.file.file_ext
      //转换数据
      LuckyExcel.transformExcelToLuckyByUrl(file, name, (exportJson, luckysheetfile) => {
        if (exportJson.sheets == null || exportJson.sheets.length == 0) {
          alert('无法读取excel文件的内容，当前不支持xls文件!')
          return
        }

        exportJson.sheets.forEach((sheet) => {
          sheet.celldata.forEach((cell) => {
            if (cell.v && cell.v.f) {
              cell.v.f = cell.v.f.replace(/^=+/, '=')
            }
          })
        })

        luckysheet.destroy()
        luckysheet.create({
          container: 'luckysheet', //luckysheet is the container id
          showinfobar: false,
          data: exportJson.sheets,

          title: name,
          cellFormula: {
            autoFormat: false
          },
          lang: 'zh', // 设定表格语言
          showtoolbarConfig: {
            undoRedo: true, //撤销重做，注意撤消重做是两个按钮，由这一个配置决定显示还是隐藏
            paintFormat: true, //格式刷
            currencyFormat: true, //货币格式
            percentageFormat: true, //百分比格式
            numberDecrease: true, // '减少小数位数'
            numberIncrease: true, // '增加小数位数
            moreFormats: true, // '更多格式'
            font: true, // '字体'
            fontSize: true, // '字号大小'
            bold: true, // '粗体 (Ctrl+B)'
            italic: true, // '斜体 (Ctrl+I)'
            strikethrough: true, // '删除线 (Alt+Shift+5)'
            underline: true, // '下划线 (Alt+Shift+6)'
            textColor: true, // '文本颜色'
            fillColor: true, // '单元格颜色'
            border: true, // '边框'
            mergeCell: true, // '合并单元格'
            horizontalAlignMode: true, // '水平对齐方式'
            verticalAlignMode: true, // '垂直对齐方式'
            textWrapMode: true, // '换行方式'
            textRotateMode: true, // '文本旋转方式'
            image: false, // '插入图片'
            link: true, // '插入链接'
            chart: false, // '图表'（图标隐藏，但是如果配置了chart插件，右击仍然可以新建图表）
            postil: true, //'批注'
            pivotTable: true, //'数据透视表'
            function: true, // '公式'
            frozenMode: true, // '冻结方式'
            sortAndFilter: true, // '排序和筛选'
            conditionalFormat: true, // '条件格式'
            dataVerification: true, // '数据验证'
            splitColumn: true, // '分列'
            screenshot: false, // '截图'
            findAndReplace: true, // '查找替换'
            protection: true, // '工作表保护'
            print: true // '打印'
          },
          hook: {
            sheetMouseup: (tabl, nowData) => {
              var text = $('.textmove').html()
              if (text) {
                luckysheet.setCellValue(nowData.r, nowData.c, `#{'${text}','${server_fun}'}`)
                $('.textmove').html('')
              }
            }
          }
          // userInfo: exportJson.info.name.creator
        })
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.main-page {
  position: relative;
  width: 100vw;
  height: calc(100vh - 65px);
  ::v-deep .luckysheet-icon-img-container.luckyiconfont {
    font-size: 24px;
  }
  ::v-deep.luckysheet-toolbar-menu-button {
    font-size: 14px;
  }
  .export-box {
    position: absolute;
    top: 4px;
    right: 10px;
    cursor: pointer;
    display: flex;
    .export {
      margin-left: 10px;
    }
    background-color: #fafafc;
  }
}
</style>
