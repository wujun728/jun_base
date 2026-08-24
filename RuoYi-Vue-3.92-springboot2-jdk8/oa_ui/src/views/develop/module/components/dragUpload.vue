<template>
  <div class="oa-dialog">
    <el-dialog
      title="导入数据"
      :visible.sync="show"
      width="789px"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <div class="tips-box">
        按照模板中的格式编辑内容，完成后在下方上传<img src="@/assets/images/excel.png" alt="" class="img" />
        <span class="download" @click="download">下载空的模板表格</span>
      </div>
      <div class="upload-box">
        <el-upload
          class="upload-demo"
          drag
          action="##"
          :show-file-list="false"
          multiple
          :http-request="exportDataFn"
          :before-upload="changeUploadFn"
        >
          <!-- 导入 -->
          <template v-if="loading === 1">
            <img src="@/assets/images/upload.png" alt="" class="img" />
            <div class="el-upload__text">可直接将文件拖拽至此处上传，或 <em>点击添加</em></div>
            <div class="el-upload__type">支持格式.xls、.xlsx格式，单次最多可导入 1 万条</div>
          </template>
          <!-- 导入中 -->
          <template v-if="loading == 2">
            <img src="@/assets/images/loading.gif" alt="" class="img-gif" />
            <div class="el-upload__text">{{ file.name }}（{{ toSizeFile(file.size) }}）</div>
            <div class="el-upload__type">正在导入...</div>
          </template>
          <!-- 导入成功 -->
          <template v-if="loading == 3">
            <img src="@/assets/images/uploadOk.png" alt="" class="img-ok" />
            <div class="text-ok">导入成功！</div>
            <div class="el-upload__type">
              导入成功 {{ response_data.successCount }} 条，导入失败 {{ response_data.errorCount }} 条
            </div>
          </template>
          <!-- 导入失败 -->
          <template v-if="loading == 4">
            <i class="iconfont icontishi2"></i>
            <div class="text-ok">导入失败！</div>
            <div class="el-upload__text mb8 mt16">
              {{ file.name || '--' }}<span style="color: #909399">（{{ toSizeFile(file.size || 0) }}）</span>
            </div>
            <div class="el-upload__text"><em>重新选择</em></div>
          </template>
        </el-upload>
      </div>
      <div class="el-upload__tip">
        支持将导出的列表数据文件，批量修改后直接上传

        <div>被标记为唯一的字段必须确保每个值都是唯一的，否则重要数据不进行导入</div>
      </div>
    </el-dialog>
    <!-- 导出 -->
    <export-excel :template="true" :save-name="saveName" :merges="merges" :export-data="exportData" ref="exportExcel" />
  </div>
</template>
<script>
import XLSX from 'xlsx'
import { crudImportApi, crudModuleInfoApi } from '@/api/develop'
import { formatBytes } from '@/libs/public'
import exportExcel from '@/components/common/exportExcel'
export default {
  name: '',
  components: { exportExcel },
  props: {},
  data() {
    return {
      show: false,
      loading: 1,
      columnNumber: 4,
      file: {},
      info: {},
      response_data: { successCount: 0, errorCount: 0 },
      saveName: '',
      exportData: {
        data: [],
        cols: [{ wpx: 80 }, { wpx: 80 }, { wpx: 80 }, { wpx: 80 }, { wpx: 80 }, { wpx: 80 }]
      },
      merges: [{ s: { r: 0, c: 0 }, e: { r: 0, c: 10 } }]
    }
  },

  methods: {
    openBox(keyName, info) {
      this.info = info
      this.keyName = keyName
      this.show = true
      this.saveName = '导出' + this.info.crudInfo.table_name + '.xlsx'
    },
    toSizeFile(size) {
      return formatBytes(size)
    },
    changeUploadFn(file, fileLis) {
      const fileTypeName = file.name.substr(file.name.lastIndexOf('.') + 1)
      let types = ['xlsx', 'xls']
      if (!types.includes(fileTypeName)) {
        this.$message.error('仅支持 ' + types.join(',') + ' 格式')
        return false
      }

      this.file = file
      this.loading = 2
    },

    // 下载模板
    download() {
      let data = {
        is_field_all: 1
      }
      let obj = {}
      crudModuleInfoApi(this.keyName, 0, data).then((res) => {
        obj = res.data
        this.exportData.rows = [{ hpt: 30 }, { hpx: 30 }]
        let aoaData = []
        let arr = []
        let headerArr = []
        obj.showField.map((item) => {
          arr.push(item.field_name)
          headerArr.push(item.field_name_en)
        })

        aoaData[0] = [
          `注意：富文本及图片不支持导入，省市区这种级联使用/隔开;
例如：陕西省/西安市/西咸新区；复选字段，多个选项之间用英文逗号隔开;
例如：重点客户,A级;
日期之间使用连接线隔开，时间之间用冒号隔开，例如：2024-08-10 13:14:16`
        ]
        aoaData[1] = headerArr
        aoaData[2] = arr
        this.exportData.data = aoaData
        this.$refs.exportExcel.exportExcel()
      })
    },

    // 导入
    exportDataFn(file) {
      if (!file.file) {
        return
      }
      const that = this
      // 拿取文件对象
      var f = file.file
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
              const result = arrData[i].split(',');
              if (result.every(i => i === "")) continue
              arrRes.push(result)
            }
          }
          that.importExcelData(arrRes)
        }
        reader.readAsArrayBuffer(f)
      }
      reader.readAsBinaryString(f)
    },

    importExcelData(arrRes) {
      let thead = arrRes[0]
      let data = []
      for (let i = 2; i < arrRes.length; i++) {
        data.push({})
        for (let j = 0; j < arrRes[i].length; j++) {
          data[data.length - 1][thead[j]] = arrRes[i][j]
        }
      }

      crudImportApi(this.keyName, { import: data })
        .then((res) => {
          if (res.status == 200) {
            this.response_data = res.data
            this.loading = 3
            this.$emit('getList')
            setTimeout(() => {
              this.handleClose()
            }, 2000)
          } else {
            this.loading = 4
          }
        })
        .catch((err) => {
          this.loading = 4
        })
    },
    handleClose() {
      this.show = false
      this.loading = 1
      this.file = {}
    }
  }
}
</script>
<style scoped lang="scss">
.tips-box {
  width: 100%;
  height: 38px;
  display: flex;
  align-items: center;
  background: #f4f6fa;
  border-radius: 4px;
  font-weight: 400;
  font-size: 14px;
  padding: 0 15px;
  color: #303133;
  .img {
    display: block;
    width: 15px;
    height: 18px;
    margin: 0 10px;
  }
  .download {
    cursor: pointer;
    color: #1890ff !important;
  }
}
.mt16 {
  margin-top: 16px;
}
.mb8 {
  margin-bottom: 8px;
}
.upload-box {
  margin: 20px 0;
  .upload-demo {
    width: 100%;
    height: 312px;
    ::v-deep .el-upload {
      width: 100%;
      height: 100%;
      border-color: #dddddd;
    }
    ::v-deep .el-upload .el-upload-dragger {
      width: 100%;
      height: 100%;
      border-color: #dddddd;
    }
  }
  .img {
    // display: block;
    width: 64px;
    height: 73px;
    margin: 90px 0 18px 0;
  }
  .img-ok {
    width: 39px;
    height: 39px;
    margin: 102px 0 14px 0;
  }
  .text-ok {
    font-weight: 600;
    font-size: 15px;
    color: #303133;
  }
  .img-gif {
    width: 75px;
    height: 75px;
    margin: 85px 0 21px 0;
  }
  .icontishi2 {
    display: inline-block;
    font-size: 39px;
    color: red;
    margin: 91px 0 14px 0;
  }
}
.el-upload__tip {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #606266;
  line-height: 18px;
  margin-bottom: 10px;
}
.el-upload__text {
  font-weight: 400;
  font-size: 13px;
  color: #303133;
  line-height: 18px;
}
.el-upload__type {
  margin-top: 4px;
  font-weight: 400;
  font-size: 13px;
  color: #909399;
  line-height: 18px;
}
</style>
