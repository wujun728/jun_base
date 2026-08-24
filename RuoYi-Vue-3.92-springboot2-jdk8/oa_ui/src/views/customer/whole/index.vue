<!-- 客户-合同收支 -->
<template>
  <div class="divBox">
    <el-card class="mb14 normal-page" :body-style="{ padding: '20px 20px 20px 20px' }">
      <oaFromBox
        ref="oaFromBox"
        :search="search"
        :isViewSearch="false"
        :sortSearch="false"
        :total="total"
        :timeVal="timeValue"
        :title="`合同收支`"
        btnText="导出"
        :btnIcon="false"
        @addDataFn="getExportData"
        @confirmData="confirmData"
      ></oaFromBox>
      <div class="mt10" v-loading="loading">
        <el-table
          :data="tableData"
          :height="tableHeight"
          style="width: 100%"
          row-key="id"
          default-expand-all
          @sort-change="sortChange"
        >
          <el-table-column prop="date" min-width="150" sortable label="付款时间" />
          <el-table-column prop="bill_types" label="记录类型" min-width="90">
            <template v-slot:default="scope">
              <el-tag class="bill-types-tag" :type="scope.row.types === 2 ? 'warning' : 'success'">
                {{ scope.row.types === 2 ? '支出' : '收入' }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column prop="num" min-width="100" label="付款金额(元)" />
          <el-table-column prop="pay_type" min-width="100" label="支付方式">
            <template v-slot:default="scope">
              <span>{{ scope.row.pay_type !== '' ? scope.row.pay_type : '--' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="client.name" label="客户名称" min-width="120">
            <template v-slot:default="scope">
              <span class="pointer default-color" @click="clientCheck(scope.row)">
                {{ scope.row.client ? scope.row.client.customer_name : '--' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="contract.contract_name" label="合同名称" min-width="120">
            <template v-slot:default="scope">
              <span class="pointer default-color" @click="treatyCheck(scope.row)">
                {{ scope.row.contract ? scope.row.contract.contract_name : '--' }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="status" label="审核状态" min-width="140">
            <template v-slot:default="scope">
              <el-tag class="status-tag" type="success" v-if="scope.row.status === 1">已通过</el-tag>
              <el-tag class="status-tag" type="info" v-else-if="scope.row.status === -1">已撤销</el-tag>
              <el-tag class="status-tag" type="warning" v-else-if="scope.row.status === 0">待审核</el-tag>
              <el-tag class="status-tag" type="danger" v-else>未通过</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="业务员" min-width="140">
            <template slot-scope="scope">
              <img
                :src="scope.row.card.avatar"
                alt=""
                style="width: 24px; height: 24px; border-radius: 50%; margin-right: 7px; vertical-align: bottom"
              />
              {{ scope.row.card.name }}
            </template>
          </el-table-column>
          <el-table-column prop="created_at" label="申请时间" min-width="140"> </el-table-column>

          <el-table-column prop="address" fixed="right" :label="$t('public.operation')" width="100">
            <template v-slot:default="scope">
              <el-button type="text" @click="handleCheck(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="page-fixed">
          <el-pagination
            :page-size="where.limit"
            :current-page="where.page"
            :page-sizes="[15, 20, 30]"
            layout="total, sizes,prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="pageChange"
          />
        </div>
        <div class="footer-expend">
          <div class="expend">
            <span class="mr14"
              >累计收款金额(元): <span class="income">{{ census.income || '0' }} </span>
            </span>

            <span class="mr14">
              累计支出金额(元): <span class="expend-color">{{ census.expend || '0' }} </span>
            </span>

            <span>
              审核中金额(元): <span class="income">{{ census.review_income || '0' }}（收入）</span>
              <span class="expend-color">{{ census.review_expend || '0' }}（支出）</span></span
            >
          </div>
        </div>
      </div>
    </el-card>
    <!-- 查看客户详情侧滑 -->
    <edit-customer ref="editCustomer" :form-data="clientFromData"></edit-customer>
    <!-- 查看合同详情侧滑 -->
    <edit-contract ref="editContract" :form-data="contractFromData"></edit-contract>
    <!-- 查看详情 -->
    <detail-examine ref="detailExamine" @getList="getTableData" />
    <!-- 导出组件 -->
    <export-excel :template="false" :save-name="saveName" :export-data="exportData" ref="exportExcel" />
  </div>
</template>

<script>
import { clientBillListApi, getbillCate } from '@/api/enterprise'

export default {
  name: 'Index',
  components: {
    detailExamine: () => import('@/views/user/examine/components/detailExamine'),
    editCustomer: () => import('@/views/customer/list/components/editCustomer'),
    editContract: () => import('@/views/customer/contract/components/editContract'),
    exportExcel: () => import('@/components/common/exportExcel'),
    applyForPayment: () => import('@/views/customer/list/components/applyForPayment'),
    expendDialog: () => import('@/views/customer/list/components/expendDialog'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  props: {
    activeName: {
      type: String,
      default: '1'
    },
    types: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      tableData: [],
      saveName: '',
      exportData: {
        data: [],
        cols: [
          { wpx: 130 },
          { wpx: 70 },
          { wpx: 120 },
          { wpx: 120 },
          { wpx: 130 },
          { wpx: 130 },
          { wpx: 110 },
          { wpx: 110 }
        ]
      },
      census: {},
      catePath: [],
      timeValue: [
        this.$moment().startOf('month').format('YYYY/MM/DD'),
        this.$moment().endOf('month').format('YYYY/MM/DD')
      ],
      where: {
        types: '',
        page: 1,
        status: '',
        time: '',
        name: '',
        time_field: 'date',
        limit: 15,
        no_withdraw: 1,
        sort: 'created_at desc',
        scope_frame: 'all'
      },
      total: 0,
      contractFromData: {},
      clientFromData: {},
      search: [
        {
          field_name: '业务类型',
          field_name_en: 'types',
          form_value: 'select',
          data_dict: [
            { name: '全部', id: '' },
            { name: '合同回款', id: 0 },
            { name: '合同续费', id: 1 },
            { name: '合同支出', id: 2 }
          ]
        },
        {
          form_value: 'manage'
        },
        {
          field_name: '审核状态',
          field_name_en: 'status',
          form_value: 'select',
          data_dict: [
            {
              id: '',
              name: this.$t('toptable.all')
            },
            {
              id: 0,
              name: this.$t('customer.audit')
            },
            {
              id: 1,
              name: this.$t('customer.passed')
            },
            {
              id: 2,
              name: this.$t('customer.fail')
            }
          ]
        },
        {
          field_name: '时间类型',
          field_name_en: 'time_field',
          form_value: 'select',
          data_dict: [
            {
              value: 'date',
              name: '付款日期'
            },
            {
              value: 'time',
              name: '申请日期'
            }
          ]
        },
        {
          field_name: '开始时间',
          field_name_end: '结束时间',
          field_name_en: 'time',
          form_value: 'date_picker',
          data_dict: [
            this.$moment().startOf('month').format('YYYY/MM/DD'),
            this.$moment().endOf('month').format('YYYY/MM/DD')
          ]
        }
      ],
      dropdownList: [
        {
          value: 1,
          label: '导出'
        }
      ]
    }
  },
  created() {
    this.where.time = this.timeValue[0] + '-' + this.timeValue[1]
    this.getTableData()
  },
  methods: {
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    // 查看客户详情
    async clientCheck(val) {
      val.eid = val.eid
      val.cid = val.cid
      if (val) {
        this.clientFromData = {
          title: this.$t('customer.editcustomer'),
          width: '1000px',
          data: val,
          types: this.types
        }

        this.$refs.editCustomer.tabIndex = '1'
        this.$refs.editCustomer.tabNumber = 1
        this.$refs.editCustomer.openBox(val.eid, this.types)
      }
    },
    // 查看合同详情
    async treatyCheck(item) {
      item.cid = item.contract.id
      item.contract_name = item.contract.contract_name
      item.eid = item.eid
      this.contractFromData = {
        title: '查看合同',
        width: '1000px',
        data: item,
        isClient: false,
        name: item ? item.contract_name : '',
        id: item ? item.id : '',
        edit: true
      }

      this.$refs.editContract.tabIndex = '1'
      this.$refs.editContract.tabNumber = 1
      this.$refs.editContract.openBox(item)
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

    // 查看
    async handleCheck(item) {
      item.id = item.apply_id
      this.$refs.detailExamine.openBox(item)
    },

    // 导出
    getExportData() {
      this.$refs.oaFromBox.loading = true
      this.saveName = '导出付款审核_' + this.$moment(new Date()).format('MM_DD_HH_mm_ss') + '.xlsx'

      let where = {
        types: this.where.types,
        page: 0,
        status: this.where.status,
        time: this.where.time,
        name: this.where.name,
        time_field: this.where.time_field,
        limit: 0,
        no_withdraw: this.where.no_withdraw
      }
      clientBillListApi(where).then((res) => {
        let data = res.data.list
        let aoaData = [
          ['付款时间', '付款金额', '支付方式', '备注', '业务类型', '客户名称', '合同名称', '合同编号', '业务员']
        ]
        if (data.length > 0) {
          data.forEach((value) => {
            if (value.types == 0) {
              value.types = '回款记录'
            } else {
              if (value.renew && value.renew.title) {
                value.types = '续费记录' + '-' + value.renew.title
              } else {
                value.types = '续费记录'
              }
            }

            aoaData.push([
              value.date,
              value.num,
              value.pay_type,
              value.mark,
              value.types,
              value.client ? value.client.customer_name : '',
              value.contract ? value.contract.contract_name : '',
              value.treaty ? value.treaty.contract_no : '',
              value.card ? value.card.name : ''
            ])
          })

          this.exportData.data = aoaData
          this.$refs.exportExcel.exportExcel()
          this.$refs.oaFromBox.loading = false
        }
      })

      // this.getTableData()
    },

    async getbillCate(id) {
      getbillCate(id).then((res) => {
        this.catePath = res.data.bill_cate_path
      })
    },
    // 获取表格数据
    async getTableData() {
      this.loading = true
      const result = await clientBillListApi(this.where)
      this.tableData = result.data.list
      this.census = result.data.census
      this.total = result.data.count
      this.loading = false
    },
    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          types: '',
          page: 1,
          status: '',
          time: '',
          name: '',
          time_field: 'date',
          limit: 15,
          no_withdraw: 1,
          sort: 'created_at desc',
          scope_frame: 'all'
        }

        this.search[4].data_dict = this.timeValue
        this.where.time = this.timeValue[0] + '-' + this.timeValue[1]
      } else {
        this.where = { ...this.where, ...data }
      }
      this.where.page = 1
      this.getTableData()
    },
    isOk() {
      this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
.expend {
  font-size: 14px;
  color: #909399;

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
  }

  .income {
    color: #13ce66;
  }

  .positive {
    color: #1890ff;
  }
}

.btn-type {
  padding: 4px 10px;
  font-size: 13px;
}

.invoice-info {
  color: #909399;
}

.invoice-info > div > span {
  color: #606266;
}

.line {
  width: 100%;
  height: 4px;
  border-bottom: 1px solid #f2f6fc;
  margin-bottom: 30px;
}

.img {
  width: 40px;
  height: 40px;
}

.head-box {
  display: flex;
  align-items: center;

  .input {
    width: 240px;
    margin: 0 20px 0 10px;
  }
}

.detail-box {
  padding: 20px;
  color: #333;

  .item-box {
    display: flex;
    margin-bottom: 20px;
    font-size: 14px;

    span {
      /*width: 80px;*/
    }

    div {
      margin-left: 20px;
    }
  }

  .content-box {
    span {
      font-size: 14px;
    }
  }
}

::v-deep .el-drawer__body {
  height: 100%;
  overflow-y: auto;
}

.footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.from {
  margin: 0 24px;
}

.dialog {
  ::v-deep .el-dialog {
    border-radius: 6px;
    height: 249px;
  }

  ::v-deep .el-dialog__body {
    padding: 0;
  }

  ::v-deep .el-textarea__inner {
    width: 400px;
    height: 90px;
    font-size: 13px;
  }
}
</style>

<style lang="scss">
.content-box {
  font-size: 13px;
}

.table-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 22px;
  border-radius: 3px;
  font-size: 13px;
  cursor: pointer;

  &.blue {
    background: rgba(24, 144, 255, 0.05);
    border: 1px solid #1890ff;
    color: #1890ff;
  }

  &.yellow {
    background: rgba(255, 153, 0, 0.05);
    border: 1px solid #ff9900;
    color: #ff9900;
  }

  &.red {
    background: rgba(255, 153, 0, 0.05);
    border: 1px solid #ed4014;
    color: #ed4014;
  }

  &.green {
    background: rgba(0, 192, 80, 0.05);
    border: 1px solid #00c050;
    color: #00c050;
  }

  &.gray {
    background: rgba(153, 153, 153, 0.05);
    border: 1px solid #999999;
    color: #999999;
  }
}

.bill-types-tag {
  border: 0;
}

.status-tag {
  background-color: transparent !important;
}

.footer-expend {
  display: flex;
  justify-content: start;
  margin-top: 34px;
}
</style>
