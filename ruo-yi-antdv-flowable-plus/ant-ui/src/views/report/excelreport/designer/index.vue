<!--
 * @Author: lide1202@hotmail.com
 * @Date: 2021-3-13 11:04:24
 * @Last Modified by:   lide1202@hotmail.com
 * @Last Modified time: 2021-3-13 11:04:24
 !-->
<template>
  <div class="layout">
    <div class="layout-left">
      <div class="add-collection">
        数据集管理
        <el-button
          type="text"
          icon="el-icon-circle-plus-outline"
          @click="queryAllDataSet()"
        />
      </div>
      <div>
        <el-collapse
          v-for="(item, indexs) in dataSet"
          :key="indexs"
          v-model="activeNames"
          @change="handleChange"
        >
          <el-collapse-item :title="item.setName" :name="item.id">
            <el-popconfirm
              :title="'确定删除' + item.setName + '吗？'"
              @onConfirm="del(item)"
            >
              <el-button
                slot="reference"
                type="text"
                icon="el-icon-close"
                class="delect-all"
              />
            </el-popconfirm>
            <draggable
              v-model="item.setParamList"
              :sort="false"
              group="people"
              style="margin-left: 10px"
              @start="onStart(item.setCode, $event)"
            >
              <!-- <transition-group> -->
              <div
                v-for="(i, index) in item.setParamList"
                :key="index"
                class="field-container"
              >
                <div class="aRow">
                  <span>{{ i }}</span>
                </div>
              </div>
              <!-- </transition-group> -->
            </draggable>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>
    <div class="layout-middle">
      <div class="push_btn">
        <el-tooltip
          class="item"
          effect="dark"
          content="预览"
          placement="bottom-start"
        >
          <el-button type="text" @click="preview()">
            <i class="iconfont iconfuzhi"></i>
          </el-button>
        </el-tooltip>
        <el-tooltip
          class="item"
          effect="dark"
          content="保存"
          placement="bottom-start"
        >
          <el-button type="text" @click="save()">
            <i class="iconfont iconsave"></i>
          </el-button>
        </el-tooltip>
      </div>
      <div
        id="luckysheet"
        style="margin:0px;padding:0px;position:absolute;width:100%;height:95vh;left: 0px;top: 30px;bottom:0px;"
      />
      <div id="qrCode" ref="qrCodeDiv" />
      <img id="barCode" />
    </div>

    <div class="layout-right">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="基础配置" name="first">
          <el-form ref="rightForm" :model="rightForm" label-width="60px">
            <el-form-item label="坐标">
              <el-input v-model="rightForm.coordinate" />
            </el-form-item>
            <el-form-item label="值">
              <el-input v-model="rightForm.value" />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog title="数据集管理" :visible.sync="outerVisible">
      <el-table
        ref="multipleTable"
        :data="dataSetData"
        tooltip-effect="dark"
        style="width: 100%;height: 60vh;overflow-y: scroll;"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column label="数据集名称" width="120" prop="setName" />
        <el-table-column prop="setDesc" label="数据集描述" width="180" />
        <el-table-column
          prop="setCode"
          label="数据集编码"
          show-overflow-tooltip
        />
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="outerVisible = false">取 消</el-button>
        <el-button type="primary" @click="checkDataSet()">确定 </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import { queryAllDataSet, detail, detailByReportCode } from '@/api/report/GaeaReport'
import { addReportExcel, editReportExcel } from '@/api/report/report'
import ColorPicker from '../../bigscreen/designer/designerComponents/colorPicker.vue'

// import QRCode from 'qrcodejs2'
// import JsBarcode from 'jsbarcode'
export default {
  name: 'Excels',
  components: {
    draggable,
    ColorPicker
  },
  data() {
    return {
      activeName: 'first',
      activeNames: ['1'],
      reportId: null,
      accessKey: null,
      reportCode: '',
      options: [],
      sheet: {},
      sheetData: [],
      dataSetData: [],
      reportExcelDto: {
        id: null,
        jsonStr: '',
        setCodes: '',
        setParam: '',
        reportCode: ''
      },
      multipleSelection: [],
      rightForm: {
        coordinate: '',
        value: ''
      },
      qrCodeForm: {
        type: 'QRCode',
        content: 'https://www.baidu.com',
        width: 125,
        height: 125,
        colorDark: '#333333', // 二维码颜色
        colorLight: '#ffffff', // 二维码背景色
        // correctLevel: QRCode.CorrectLevel.L, // 容错率，L/M/H
        ri: 0,
        ci: 0,
        currentSrc: ''
      },
      barCodeForm: {
        type: 'BarCode',
        format: 'CODE39',
        displayValue: true,
        content: '99999999999',
        lineColor: '#000',
        background: '#fff',
        width: 100,
        height: 40,
        ri: 0,
        ci: 0,
        currentSrc: ''
      },
      formPrintSetting: {
        size: '',
        pixel1: '',
        pixel2: ''
      },
      pixelList: [
        { paper: 'A4', width: 210, height: 297 },
        { paper: 'A3', width: 297, height: 420 },
        { paper: 'Letter', width: 216, height: 279 },
        { paper: 'Legal', width: 216, height: 355 },
        { paper: 'Executive', width: 184, height: 266 }
      ],
      dataSet: [],
      outerVisible: false,
      printVisible: false,
      pop: false,
      setCode: null,
      draggableFieldLabel: null,
      dialogVisible: false,
      dialogBarCode: false,
      paperList: [],
      // 纸张标识
      paper: '',
      // 纸张宽高
      width: '',
      height: '',
      // 清晰度
      definition: '',
      // 是否套打
      isBackend: '',
      dpi: '',
      pxWidth: '',
      pxHeight: '',
      show: false,
      config: '',
      settings: {},
      qrCodeList: [],
      moveDataelse: {
        x: null,
        y: null
      }
    }
  },
  mounted() {},
  created() {
    this.reportCode = this.$route.query.reportCode
    this.accessKey = this.$route.query.accessKey
    this.loadDataSet()
    this.design()
  },
  methods: {
    handleClose() {
      this.printVisible = false
    },
    handleChange(val) {},
    // 右侧信息配置tabs
    handleClick(tab, event) {},
    async design() {
      // 根据reportCode获取单条报表
      const { code, data } = await detailByReportCode(this.reportCode)
      if (data != null) {
        this.reportId = data.id
      }
      this.sheetData = data == null ? [{}] : JSON.parse(data.jsonStr)
      console.log(this.sheetData)
      this.createSheet()
      if (data != null) {
        if (data.setCodes != null && data.setCodes !== '') {
          var dataSetList = data.setCodes.split('|')
          dataSetList.forEach(code => {
            this.dataSetData.forEach(setData => {
              if (code === setData.setCode) {
                this.detail(setData.id)
              }
            })
          })
        }
      }
    },
    //初始化表格
    createSheet() {
      //将vue对象传入
      const that = this
      const options = {
        container: 'luckysheet', // 设定DOM容器的id
        title: 'Luckysheet Demo', // 设定表格名称
        lang: 'zh', // 设定表格语言
        plugins: ['chart'],
        hook: {
          cellDragStop: function(cell, postion, sheetFile, ctx, event) {
            // console.info("cellDragStop-cell",cell);
            // console.info("cellDragStop-postion", postion);
            // console.info("cellDragStop-sheetFile", sheetFile);
            // console.info("cellDragStop-ctx",ctx);
            // console.info("cellDragStop-event", event);
            // console.log("cellDragStop-draggableFieldLabel", that);
            luckysheet.setCellValue(
              postion.r,
              postion.c,
              that.draggableFieldLabel
            )
          },
          cellMousedown: function(cell, postion, sheetFile, ctx) {
            that.rightForm.coordinate = postion.r + ',' + postion.c
            that.rightForm.value = cell == null ? '' : cell.v
          }
        },
        data: [
          {
            name: 'report', //工作表名称
            color: '', //工作表颜色
            index: 0, //工作表索引
            status: 1, //激活状态
            order: 0, //工作表的下标
            hide: 0, //是否隐藏
            row: 36, //行数
            column: 18, //列数
            defaultRowHeight: 19, //自定义行高
            defaultColWidth: 73, //自定义列宽
            celldata: [], //初始化使用的单元格数据
            config: {
              merge: {}, //合并单元格
              rowlen: {}, //表格行高
              columnlen: {}, //表格列宽
              rowhidden: {}, //隐藏行
              colhidden: {}, //隐藏列
              borderInfo: {}, //边框
              authority: {} //工作表保护
            },
            scrollLeft: 0, //左右滚动条位置
            scrollTop: 315, //上下滚动条位置
            luckysheet_select_save: [], //选中的区域
            calcChain: [], //公式链
            isPivotTable: false, //是否数据透视表
            pivotTable: {}, //数据透视表设置
            filter_select: {}, //筛选范围
            filter: null, //筛选配置
            luckysheet_alternateformat_save: [], //交替颜色
            luckysheet_alternateformat_save_modelCustom: [], //自定义交替颜色
            luckysheet_conditionformat_save: {}, //条件格式
            frozen: {}, //冻结行列配置
            chart: [], //图表配置
            zoomRatio: 1, // 缩放比例
            image: [], //图片
            showGridLines: 1, //是否显示网格线
            dataVerification: {} //数据验证配置
          }
        ]
      }
      options.data = this.sheetData

      $(function() {
        luckysheet.create(options)
      })
    },
    onStart(setCode, evt) {
      this.setCode = setCode
      var fieldLabel = evt.item.innerText // 列名称
      this.draggableFieldLabel = '#{' + this.setCode + '.' + fieldLabel + '}'
      console.log('evt', evt)
      console.log('draggableFieldLabel', this.draggableFieldLabel)
    },
    async loadDataSet() {
      const { code, data } = await queryAllDataSet()
      this.dataSetData = data
      if (code != '200') return
    },
    doCancel() {
      this.pop = false
    },
    async save() {
      // console.log(luckysheet.toJson())
      // console.log(luckysheet.getAllSheets())
      // console.log(luckysheet.getSheetData(0))
      const jsonData = luckysheet.getAllSheets()
      for (let i = 0; i < jsonData.length; i++) {
        //清空data数据，以celldata数据为主
        jsonData[i]['data'] = []
      }

      this.reportExcelDto.jsonStr = JSON.stringify(luckysheet.getAllSheets())
      var setCodeList = []
      var setParams = {}
      this.dataSet.forEach(code => {
        setCodeList.push(code.setCode)
        if (
          code.dataSetParamDtoList != null &&
          code.dataSetParamDtoList.length > 0
        ) {
          var dataSetParam = {}
          code.dataSetParamDtoList.forEach(value => {
            dataSetParam[value.paramName] = value.sampleItem
          })
          setParams[code.setCode] = dataSetParam
        }
      })

      this.reportExcelDto.setParam = JSON.stringify(setParams)
      this.reportExcelDto.setCodes = setCodeList.join('|')
      this.reportExcelDto.reportCode = this.reportCode
      if (this.reportId == null) {
        const { code } = await addReportExcel(this.reportExcelDto)
        if (code != '200') return
        this.$message.success('保存成功')
      } else {
        this.reportExcelDto.id = this.reportId
        const { code } = await editReportExcel(this.reportExcelDto)
        if (code != '200') return
        this.$message.success('更新成功')
      }
    },

    async detailByReportCode(reportCode) {
      const { code, data } = await detailByReportCode(reportCode)
      if (data != null) {
        this.reportId = data.id
      }
    },
    async preview() {
      var routeUrl = this.$router.resolve({
        path: '/excelreport/viewer',
        query: { reportCode: this.reportCode }
      })
      window.open(routeUrl.href, '_blank')
    },
    async queryAllDataSet() {
      this.outerVisible = true
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    checkDataSet() {
      this.outerVisible = false
      if (this.multipleSelection.length > 1) {
        this.$message({
          message: '一次最多勾选一个数据集',
          type: 'warning'
        })
        this.outerVisible = true
      } else {
        this.detail(this.multipleSelection[0].id)
      }
    },
    async detail(id) {
      const { code, data } = await detail(id)
      if (code != 200) return
      var flag = true
      this.dataSet.forEach(value => {
        if (value.setCode === data.setCode) {
          flag = false
        }
      })
      if (flag) {
        this.dataSet.push(data)
      }
    },
    del(val) {
      for (let i = 0; i < this.dataSet.length; i++) {
        if (this.dataSet[i].setCode === val.setCode) {
          this.dataSet.splice(i, 1)
        }
      }
    }
  }
}
</script>

<style src="../../../../../static/luckysheet/assets/iconfont/iconfont.css" />
<style scoped lang="scss">
.position {
  position: absolute;
  z-index: 100;
}

#qrCode,
#barCode {
  display: none;
}

.yulancopy {
  font-size: 16px;
}

.baocun {
  font-size: 17px;
}

.el-collapse {
  position: relative;
  border-top: 0 solid #e6ebf5;
  border-bottom: 0 solid #e6ebf5;
}

.el-collapse-item__header {
  border-bottom: 0 solid #e6ebf5 !important;
}

.delect-all {
  position: absolute;
  top: 10px;
  right: 30px;
  color: #333;
}

.push_btn {
  position: absolute;
  z-index: 100;
  top: 5px;
  left: 3px;

  .iconfont {
    color: black;
  }
}

.add-collection {
  line-height: 30px;

  .el-button {
    font-size: 16px !important;
    vertical-align: middle;
    margin-left: 20px;
  }
}

.layout {
  display: block;
  position: relative;
  margin: 0;
  padding: 0;
  border: 1px solid #d7dde4;
}

.layout-left {
  display: block;
  position: absolute;
  padding: 20px;
  width: 230px;
  min-height: 99.98vh;
  border-right: 1px solid #eee;
  border-color: #e8eaec;
  background: #fff;
  border-radius: 4px;
  font-size: 14px;
  -webkit-transition: all 0.2s ease-in-out;
  transition: all 0.2s ease-in-out;
  top: 0;
  left: 0;

  .field-container {
    display: block;
    position: relative;
    width: 100%;
    margin: 0;
    cursor: pointer;
    line-height: 30px;

    .aRow {
      padding: 0 10px;

      .operation {
        float: right;
        display: none;

        .el-button--text {
          color: #333 !important;
        }
      }

      &:hover {
        .operation {
          display: block;
        }

        background: #f7fafd;
      }
    }
  }
}

.layout-middle {
  display: block;
  position: absolute;
  left: 230px;
  right: 230px;
  width: calc(100% - 460px);
  margin: 0;
  padding: 0;

  .excel-designer {
    display: block;
    position: relative;
    margin: 0;
    padding: 0;
  }
}

.layout-right {
  display: block;
  position: absolute;
  top: 0;
  right: 0;
  padding: 20px;
  width: 230px;
  min-height: 99.98vh;
  border-left: 1px solid #dcdee2;
  border-color: #e8eaec;
  background: #fff;
  border-radius: 4px;
  font-size: 14px;
  -webkit-transition: all 0.2s ease-in-out;
  transition: all 0.2s ease-in-out;
}
</style>
