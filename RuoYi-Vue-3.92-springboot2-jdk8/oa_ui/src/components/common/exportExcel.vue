<!-- 
  @FileDescription: Excel导出组件
  功能：封装Excel导出功能，支持自定义模板、单元格合并和样式设置
-->
<template>
  <!-- 无UI展示，仅提供功能 -->
  <span />
</template>

<script>
import XLSX from 'xlsx'

export default {
  name: 'ExportExcel',
  props: {
    // 是否使用模板模式
    template: {
      type: Boolean,
      default: false
    },
    // 导出文件名
    saveName: {
      type: String,
      default: '导出.xlsx'
    },
    // 单元格合并配置
    merges: {
      type: Array,
      default() {
        return []
      }
    },
    // 导出数据配置
    exportData: {
      type: Object,
      default() {
        return {
          data: [],    // 表格数据
          cols: [     // 列宽配置
            { wpx: 70 }, 
            { wpx: 70 }, 
            { wpx: 70 }, 
            { wpx: 70 }
          ],
          rows: []    // 行高配置
        }
      }
    }
  },
  methods: {
    /**
     * 执行Excel导出
     */
    exportExcel() {
      if (this.exportData.data.length <= 0) {
        this.$message.error('内容为空')
        return
      }
      
      // 创建工作表
      const sheet = XLSX.utils.aoa_to_sheet(this.exportData.data)
      
      // 模板模式处理
      if (this.template) {
        // 自定义合并配置
        if (this.merges.length) {
          sheet['!merges'] = this.merges
          sheet['!rows'] = [{ hpx: 80 }]
          // 设置标题样式
          sheet['A1'].s = {
            font: {
              sz: 20,        // 字体大小
              bold: true,    // 加粗
              color: { rgb: '666666' } // 字体颜色
            },
            alignment: { 
              horizontal: 'center', // 水平居中
              vertical: 'center',   // 垂直居中
              wrapText: true        // 自动换行
            },
            fill: {
              fgColor: { rgb: 'ebebeb' } // 背景色
            }
          }
        } else {
          // 默认合并配置
          sheet['!merges'] = [
            { s: { r: 1, c: 0 }, e: { r: 1, c: 10 } },
            // ...其他合并配置
          ]
          sheet['!rows'] = [{ hpt: 30 }]
        }
      } else {
        // 非模板模式使用传入的行配置
        sheet['!rows'] = this.exportData.rows
      }
      
      // 设置列宽
      sheet['!cols'] = this.exportData.cols
      
      // 触发下载
      this.openDownloadDialog(this.sheet2blob(sheet), this.saveName)
    },

    /**
     * 打开下载对话框
     * @param {String|Blob} url - 下载地址或Blob对象
     * @param {String} saveName - 保存文件名
     */
    openDownloadDialog(url, saveName) {
      if (url instanceof Blob) {
        url = URL.createObjectURL(url) // 创建Blob URL
      }
      
      const aLink = document.createElement('a')
      aLink.href = url
      aLink.download = saveName || ''
      
      // 创建并触发点击事件
      const event = window.MouseEvent 
        ? new MouseEvent('click') 
        : document.createEvent('MouseEvents').initMouseEvent(
            'click', true, false, window, 0, 0, 0, 0, 0, 
            false, false, false, false, 0, null
          )
      
      aLink.dispatchEvent(event)
    },

    /**
     * 工作表转Blob对象
     * @param {Object} sheet - 工作表对象
     * @param {String} sheetName - 工作表名称
     * @returns {Blob} - 返回Blob对象
     */
    sheet2blob(sheet, sheetName = 'sheet1') {
      const workbook = {
        SheetNames: [sheetName],
        Sheets: { [sheetName]: sheet }
      }
      
      // Excel生成配置
      const wopts = {
        bookType: 'xlsx',  // 文件类型
        bookSST: false,    // 不生成Shared String Table
        type: 'binary'     // 输出类型
      }
      
      // 生成Excel二进制数据
      const wbout = XLSX.write(workbook, wopts)
      
      // 字符串转ArrayBuffer
      function s2ab(s) {
        const buf = new ArrayBuffer(s.length)
        const view = new Uint8Array(buf)
        for (let i = 0; i < s.length; i++) {
          view[i] = s.charCodeAt(i) & 0xff
        }
        return buf
      }
      
      return new Blob([s2ab(wbout)], { 
        type: 'application/octet-stream' 
      })
    }
  }
}
</script>

<style lang="scss" scoped>
/* 组件样式 */
.import-excel {
  margin-left: 10px;
  display: inline-block;
}
</style>
