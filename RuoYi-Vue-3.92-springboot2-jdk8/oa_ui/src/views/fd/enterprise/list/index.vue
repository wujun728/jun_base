<!-- 财务-账目记录-收支记账页面 -->
<template>
  <div class="divBox">
    <el-card class="normal-page">
      <formBox
        ref="formBox"
        :total="total"
        @addFinance="addFinance"
        @confirmData="confirmData"
        @exportTemplate="exportTemplate"
        @importExcelDFn="importExcelDFn"
        @getExportData="getExportData"
      />
      <div v-loading="loading" class="table-box mt10">
        <el-table
          ref="table"
          :data="tableData"
          :height="tableHeight"
          :span-method="arraySpanMethod"
          :tree-props="{ children: 'children' }"
          row-key="id"
          style="width: 100%"
          @sort-change="sortChange"
        >
          <el-table-column :label="$t('finance.entrytime')" min-width="160" prop="edit_time" sortable />
          <el-table-column :label="$t('finance.accounttype')" min-width="100" prop="types">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.types == 0" plain size="small" type="warning">支出</el-tag>
              <el-tag v-else plain size="small" type="success">收入</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="收支金额(元)" min-width="120" prop="num" show-overflow-tooltip>
            <template slot-scope="scope">
              <div v-if="scope.row.types !== 10">
                {{ scope.row.num }}
              </div>
            </template>
          </el-table-column>
          <el-table-column :label="$t('finance.mode')" min-width="120" prop="pay_type" />
          <el-table-column :label="$t('finance.accounttabtype')" min-width="120" prop="cate.name" />
          <el-table-column label="操作时间" min-width="180" prop="updated_at" sortable />
          <el-table-column label="备注" min-width="180" prop="mark" show-overflow-tooltip>
            <template slot-scope="scope">{{
              scope.row.client_bill ? scope.row.client_bill.mark : scope.row.mark
            }}</template>
          </el-table-column>
          <el-table-column :label="$t('public.operation')" prop="types" width="200">
            <template slot-scope="scope">
              <div v-if="scope.row.types !== 10">
                <el-button v-hasPermi="['fd:enterprise:list:check']" type="text" @click="handleCheck(scope.row)"
                  >查看</el-button
                >
                <el-button v-hasPermi="['fd:enterprise:list:edit']" type="text" @click="handleEdit(scope.row)">{{
                  $t('public.edit')
                }}</el-button>
                <el-button
                  v-hasPermi="['fd:enterprise:list:delete']"
                  type="text"
                  @click="handleDelete(scope.row, scope.$index)"
                  >{{ $t('public.delete') }}</el-button
                >
              </div>
            </template>
          </el-table-column>
        </el-table>
        <div class="footer flex">
          <div v-if="where.types === ''" class="expend ml14">
            <span
              >流入: <span class="income">{{ totalIncome }} </span>
            </span>

            <span
              >流出: <span class="expend-color">{{ totalExpend }} </span>
            </span>

            <span>
              净额: <span :class="getNum() > 0 ? 'positive' : 'positiveF'">{{ getNum() }}</span></span
            >
          </div>
          <div v-else class="expend ml14">
            <span v-if="where.types == 1"
              >{{ $t('finance.totalrevenue') }}: <span class="income">{{ totalIncome }}</span></span
            >
            <span v-if="where.types == 0"
              >{{ $t('finance.totalexpenditure') }}: <span class="expend-color">{{ totalExpend }}</span></span
            >
          </div>
          <div class="page-fixed">
            <el-pagination
              :current-page="where.page"
              :page-size="where.limit"
              :page-sizes="[15, 20, 30]"
              :total="total"
              layout="total, sizes,prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="pageChange"
            />
          </div>
        </div>
      </div>
    </el-card>

    <!-- 通用弹窗表单 -->
    <dialogForm ref="dialogForm" :form-data="formBoxConfig" :roles-config="rolesConfig" @isOk="getTableData()" />
    <!-- 导入组件 -->
    <import-excel ref="importExcel" v-show="false" @importExcelData="importExcelData"></import-excel>
    <export-excel ref="exportExcel" :export-data="exportData" :save-name="saveName" :template="false" />
    <viewDetails ref="viewDetails" :form-data="detailsData"></viewDetails>
  </div>
</template>
<script>
import {
  billListApi,
  billListEditApi,
  billListDeleteApi,
  billListCreateApi,
  enterprisePayTypeApi,
  billImportApi
} from '@/api/enterprise'
export default {
  name: 'FinanceList',
  components: {
    formBox: () => import('./components/formBox'),
    dialogForm: () => import('./components/index'),
    viewDetails: () => import('./components/viewDetails'),
    exportExcel: () => import('@/components/common/exportExcel'),
    importExcel: () => import('@/components/common/importExcel')
  },
  data() {
    return {
      drawer: false,
      input: '',
      detailsData: {},
      fromData: {},
      formBoxConfig: {},

      timeVal: [
        this.$moment().startOf('months').format('YYYY/MM/DD'),
        this.$moment().endOf('months').format('YYYY/MM/DD')
      ],
      exportData: {
        data: [],
        cols: [{ wpx: 70 }, { wpx: 70 }, { wpx: 120 }, { wpx: 140 }, { wpx: 120 }]
      },
      saveName: '导出收支记账模板.xlsx',
      where: {
        page: 1,
        limit: 15,
        sort: 'id',
        types: ''
      },
      loading: false,
      itemOptions: [],
      rolesConfig: [],
      tableData: [],
      detailData: null,
      treeData: null,
      companyData: null,

      gettime: '',
      totalExpend: 0,
      totalIncome: 0,
      total: 0
    }
  },

  created() {
    this.where.time = `${this.timeVal[0]}-${this.timeVal[1]}`
    this.getTableData()
    this.getPayType()
  },
  methods: {
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },

    getNum() {
      let num = 0
      num = this.totalIncome - this.totalExpend
      return num.toFixed(2)
    },
    sortChange(column) {
      if (column.order === 'ascending') {
        this.where.sort = column.prop + ' asc'
      } else if (column.order === 'descending') {
        this.where.sort = column.prop + ' desc'
      } else {
        this.where.sort = ''
      }

      this.getTableData()
    },

    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    // 获取表格数据
    getTableData() {
      this.loading = true
      billListApi(this.where).then((res) => {
        this.tableData = res.data.list
        this.total = res.data.count
        this.loading = false
        this.totalExpend = res.data.expend == 0 ? '0.00' : res.data.expend
        this.totalIncome = res.data.income == 0 ? '0.00' : res.data.income
      })
    },
    importExcelDFn() {
      this.$refs.importExcel.btnClick()
    },
    // 导入
    importExcelData(data) {
      var res = []
      if (data.length <= 0) {
        this.$message.error('批量导入内容为空')
      } else {
        for (let i = 0; i <= data.length - 1; i++) {
          if (data[i][0] === '') {
            continue
          } else {
            res.push({
              types: data[i][0],
              cate_id: data[i][1],
              mark: data[i][5],
              edit_time: this.$moment(data[i][4], 'YYYY-MM-DD HH:mm:ss').format('YYYY-MM-DD HH:mm:ss'),
              pay_type: data[i][3],
              num: data[i][2]
            })
          }
        }
        var data_s = {
          data: res
        }
        billImportApi(data_s).then(() => {
          if (res.status == 200) {
            this.$refs.formBox.tableFrom.search = 0
            this.getTableData()
          }
        })
      }
    },

    async getPayType() {
      var data = {
        page: 1,
        limit: 0
      }
      const result = await enterprisePayTypeApi(data)
      this.exportFileUrl = result.data.import_temp
    },
    arraySpanMethod({ row }) {
      //table合计行合并单元格
      if (row.types === 10) {
        return { rowspan: 7, colspan: this.tableData.length - 1 }
      }
    },

    // 导出模板
    exportTemplate() {
      this.saveName = '导出账目模板.xlsx'
      let aoaData = [
        [
          this.$t('finance.accounttabtitle'),
          this.$t('finance.accounttabtype'),
          this.$t('finance.accounttabmoney'),
          this.$t('finance.entrytime'),
          this.$t('finance.accountpay'),
          this.$t('finance.accounttabremark')
        ]
      ]

      this.exportData.data = aoaData
      this.$refs.exportExcel.exportExcel()
      this.$refs.exportExcel.exportExcel()
      // this.gettime = this.$moment(new Date()).format('YY-MM-dd hh:mm::ss')
      // if (!this.exportFileUrl) {
      //   this.$message.error('暂无导入模板,请联系管理员')
      // } else {
      //   this.fileLinkDownLoad(this.exportFileUrl, '收支记账导入模板' + this.gettime + '.xlsx')
      // }
    },
    // 导出
    getExportData() {
      this.saveName = '导出账目_' + this.$moment(new Date()).format('MM_DD_HH_mm_ss') + '.xlsx'
      let aoaData = [
        [
          this.$t('finance.accounttabtitle'),
          this.$t('finance.accounttabtype'),
          this.$t('finance.accounttabmoney'),
          this.$t('finance.entrytime'),
          this.$t('finance.accountpay'),
          this.$t('finance.accounttabremark')
        ]
      ]

      let obj = {}
      for (let key in this.where) {
        obj[key] = this.where[key]
      }

      obj.page = 0
      obj.limit = 0
      billListApi(obj).then((res) => {
        let data = res.data.list
        if (data.length > 0) {
          data.forEach((value) => {
            aoaData.push([
              value.types == 0 ? this.$t('finance.pay') : this.$t('finance.income'),
              value.cate ? value.cate.name : '',
              value.num,
              value.edit_time,
              value.pay_type,
              value.mark
            ])
          })

          this.exportData.data = aoaData
          this.$refs.exportExcel.exportExcel()
        }
      })
    },
    // 查看详情
    async handleCheck(item) {
      this.detailsData = {
        data: item
      }

      setTimeout(() => {
        this.$refs.viewDetails.handelOpen()
      }, 300)
    },
    // 添加科目
    async addFinance() {
      billListCreateApi()
        .then((res) => {
          this.formBoxConfig = {
            title: res.data.title,
            width: '500px',
            method: res.data.method,
            action: res.data.action.substr(4)
          }

          this.rolesConfig = res.data.rule
          this.$refs.dialogForm.openBox()
        })
        .catch((error) => {})
    },
    // 编辑
    async handleEdit(item) {
      billListEditApi(item.id).then((res) => {
        this.formBoxConfig = {
          title: res.data.title,
          width: '500px',
          method: res.data.method,
          action: res.data.action.substr(4)
        }
        this.rolesConfig = res.data.rule
        this.$refs.dialogForm.openBox()
      })
    },
    getItemArray(arr, type) {
      var arrs = []
      arr.map((value) => {
        if (value.types === type) {
          arrs.push(value)
        }
      })
      return arrs
    },
    // 删除
    async handleDelete(item, index) {
      await this.$modalSure(this.$t('finance.message4'))
      await billListDeleteApi(item.id)
      this.getTableData()
    },
    confirmData(data) {
      this.where.page = 1
      this.where = { ...this.where, ...data }
      if (data.search == 1) {
        this.$refs.formBox.tableFrom.search = 0
        this.getTableData()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.inTotal {
  margin-top: 0px;
  margin-bottom: 0px;
}

::v-deep .el-drawer__body {
  height: 100%;
  overflow-y: auto;
}
.hand {
  cursor: pointer;
}
.positiveF {
  color: #ed4014;
}
.footer {
  line-height: 42px;
  .expend {
    font-size: 14px;
    padding: 10px 0;
    color: #909399 !important;
    div {
      > span {
        padding-right: 15px;
      }
      > span:last-of-type {
        padding-right: 0;
      }
    }
    .expend-color {
      color: #ffba00;
      margin-right: 6px;
    }
    .income {
      color: #13ce66;
      margin-right: 6px;
    }
    .positive {
      color: #1890ff;
      margin-right: 6px;
    }
  }
  .page-fixed {
    bottom: auto;
  }
}
</style>
