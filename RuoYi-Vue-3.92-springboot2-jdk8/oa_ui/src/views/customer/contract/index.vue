<template>
  <div class="divBox">
    <el-card :body-style="{ padding: '20px 20px 20px 20px' }" class="employees-card">
      <oaFromBox
        :title="$route.meta.title"
        btnText="添加合同"
        :treeData="treeDataGroup"
        :search="search"
        :viewSearch="viewSearch"
        :dropdownList="dropdownList"
        :total="total"
        :isAddBtn="types == 1"
        class="from-box"
        @treeChange="handleNodeClick"
        @addDataFn="addContract"
        @dropdownFn="dropdownFn"
        @confirmData="confirmData"
      ></oaFromBox>

      <div v-loading="loading" class="mt10">
        <el-table
          ref="multipleTable"
          :data="tableData"
          :height="tableHeight"
          default-expand-all
          row-key="id"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          @sort-change="changeTimeSort"
        >
          <el-table-column min-width="55" type="selection"></el-table-column>
          <el-table-column
            v-for="header in tableHeaders"
            :key="header.field"
            :label="header.name"
            :min-width="header.input_type == 'file' ? '300' : '160'"
            :prop="header.field"
          >
            <template slot-scope="scope">
              <span v-if="header.field == 'creator' && scope.row[header.field]">{{
                scope.row[header.field].name
              }}</span>
              <span v-else-if="header.field == 'customer_way'">{{ scope.row[header.field][0] || '--' }}</span>
              <div v-else-if="header.field == 'contract_followed'" class="icon-star">
                <i
                  :class="
                    scope.row.contract_followed === 0
                      ? 'el-icon-star-off'
                      : 'el-icon-star-on color-collect icon-star-on'
                  "
                  class="pointer"
                  @click="focusEvt(scope.row)"
                ></i>
              </div>
              <div v-else-if="header.field == 'contract_label'" class="pointer">
                <el-tag v-for="item in scope.row[header.field]" :key="item.id" class="customer-tag" size="small">{{
                  item.name || '--'
                }}</el-tag>
              </div>
              <span v-else-if="header.field == 'liaison_tel'"
                >{{ scope.row[header.field] ? scope.row[header.field].liaison_name : ''
                }}{{ scope.row[header.field] ? '：' : '--'
                }}{{ scope.row[header.field] ? scope.row[header.field].liaison_tel : '' }}</span
              >
              <span v-else-if="header.field == 'salesman' && scope.row[header.field]">{{
                scope.row[header.field].name
              }}</span>
              <span v-else-if="header.field == 'area_cascade'">{{ scope.row[header.field][0] || '--' }}</span>
              <div v-else-if="header.field == 'contract_status'">
                <el-tag class="contract-status-tag" :type="getContractTag(scope.row)">{{
                  getContractTexts(scope.row)
                }}</el-tag>
              </div>
              <div v-else-if="header.field == 'signing_status'">
                <el-tag v-if="scope.row[header.field] == 0" class="customer-tag" size="small">未签约</el-tag>
                <el-tag v-if="scope.row[header.field] == 1" class="customer-tag" size="small" type="success"
                  >已签约</el-tag
                >
                <el-tag v-if="scope.row[header.field] == 2" class="customer-tag" size="small" type="danger"
                  >作废</el-tag
                >
              </div>
              <div v-else-if="header.field == 'bill_no' && scope.row[header.field]">
                <div v-for="(item, index) in scope.row[header.field]" :key="item.id">
                  <div class="line1">{{ item.bill_no }}</div>
                  <span v-if="item.bill_no && index < scope.row[header.field].length - 1">、</span>
                </div>
              </div>
              <span v-else-if="header.field == 'contract_category'">
                <span v-for="(item, index) in scope.row[header.field]" :key="index">{{ item || '--' }}</span>
              </span>
              <span v-else-if="header.field == 'contract_customer'" class="point" @click="handleClient(scope.row)">{{
                scope.row[header.field] ? scope.row[header.field].customer_name : '--'
              }}</span>
              <span v-else-if="header.field == 'contract_name'" class="point" @click="handleCheck(scope.row)">{{
                scope.row[header.field] || '--'
              }}</span>
              <span v-else-if="header.field == 'payment_status'">
                <span v-if="scope.row[header.field] == 1" style="color: #19be6b">已结清</span>
                <span v-if="scope.row[header.field] == 0" style="color: #ff9900">未结清</span>
              </span>
              <span v-else>{{ scope.row[header.field] || '--' }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('public.operation')" fixed="right" prop="address" width="200">
            <template slot="header">
              操作
              <i class="el-icon-setting pointer" @click="customSearchEvt"></i>
            </template>
            <template slot-scope="scope">
              <el-button v-hasPermi="['customer:contract:check']" type="text" @click="handleCheck(scope.row)">
                {{ $t('public.check') }}
              </el-button>
              <el-button type="text" @click="addContract(scope.row)"> 编辑合同 </el-button>
              <el-dropdown>
                <span class="el-dropdown-link el-button--text el-button more">
                  更多
                  <i class="el-icon-arrow-down" />
                </span>
                <el-dropdown-menu class="dropdown-menu-left" placement="top-start">
                  <el-dropdown-item
                    @click.native="handleBuild(scope.row, buildData.contract_refund_switch, 'contract_refund_switch')"
                  >
                    添加回款
                  </el-dropdown-item>
                  <el-dropdown-item
                    @click.native="handleBuild(scope.row, buildData.contract_renew_switch, 'contract_renew_switch')"
                  >
                    添加续费
                  </el-dropdown-item>
                  <el-dropdown-item
                    @click.native="
                      handleBuild(scope.row, buildData.contract_disburse_switch, 'contract_disburse_switch')
                    "
                  >
                    添加支出
                  </el-dropdown-item>
                  <el-dropdown-item
                    @click.native="handleBuild(scope.row, buildData.invoicing_switch, 'invoicing_switch')"
                  >
                    申请发票
                  </el-dropdown-item>
                  <el-dropdown-item @click.native="handleTransfer(2, scope.row)"> 移交同事 </el-dropdown-item>
                  <el-dropdown-item @click.native="markedAbnormal(scope.row)">
                    {{ scope.row.contract_status == 3 ? '标为正常合同' : '标为异常合同' }}
                  </el-dropdown-item>
                  <el-dropdown-item @click.native="handleDelete(scope.row)"> 删除 </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
      </div>
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
    </el-card>

    <add-contract ref="addContract" :form-data="contractFromData" @getTableData="getTableData()"></add-contract>
    <edit-contract ref="editContract" :form-data="fromData" @isOk="getTableData(true)"></edit-contract>
    <edit-customer ref="editCustomer" :form-data="fromData" @isOkEdit="getTableData(true)"></edit-customer>
    <transfer-dialog ref="transferDialog" :from-data="transferData" @handleTransfer="getTableData"></transfer-dialog>
    <visible-dialog
      v-if="tableItemDialogVisible"
      :default-table-item-list="defaultTableItemList"
      :table-item-dialog-visible="tableItemDialogVisible"
      :transfer-data-list="transferDataList"
      :transfer-props="{ key: 'field', label: 'name' }"
      :visible-table-item-list="visibleTableItemList"
      @handleCloseTableItem="handleCloseTableItem"
      @handleConfirmVisible="handleConfirmVisible"
    ></visible-dialog>
    <edit-examine
      ref="editExamine"
      :ids="formInfo.id"
      :parameterData="parameterData"
      @isOk="getTableData()"
    ></edit-examine>
    <!-- 导出组件 -->
    <export-excel ref="exportExcel" :export-data="exportData" :save-name="saveName" :template="false" />
    <!-- 导入组件 -->
    <import-excel ref="importExcel" v-show="false" @importExcelData="importExcelData"></import-excel>
  </div>
</template>

<script>
import { getContractTagType, getContractText } from '@/libs/customer'
import {
  clientContractDeleteApi,
  salesmanCustomApi,
  saveSalesmanCustomApi,
  contractViewApi,
  contractSubscribeApi,
  contractAbnormalApi,
  contractImport
} from '@/api/enterprise'
import { configRuleApproveApi } from '@/api/config'
import ExportExcel from '@/components/common/exportExcel'
import importExcel from '@/components/common/importExcel'

export default {
  name: 'FinanceList',
  components: {
    ExportExcel,
    importExcel,
    oaFromBox: () => import('@/components/common/oaFromBox'),
    editContract: () => import('./components/editContract'),
    addContract: () => import('./components/addContract'),
    transferDialog: () => import('@/views/customer/list/components/transferDialog'),
    editCustomer: () => import('@/views/customer/list/components/editCustomer'),
    VisibleDialog: () => import('@/components/form-common/dialog-transfer'),
    editExamine: () => import('@/views/user/examine/components/editExamine')
  },
  props: {
    activeName: {
      type: String,
      default: '1'
    },
    types: {
      type: Number,
      default: 1
    },
    custom_type: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      salesmanList: [],
      tableItemDialogVisible: false,
      defaultTableItemList: [],
      transferDataList: [],
      visibleTableItemList: [],
      getSalesmanCustom: [],
      tableData: [], // 表格的数据
      tableHeaders: [], // 表格的表头
      total_price: 0,
      drawer: false,
      fromData: {},
      labelText: '',
      contractFromData: {},
      where: {
        page: 1,
        limit: 15,
        types: this.custom_type
      },
      formInfo: {
        id: ''
      },
      parameterData: {
        contract_id: '',
        customer_id: '',
        invoice_id: '',
        bill_id: ''
      },
      total: 0,
      // expiredShow: true,
      transferData: {},
      ids: [],
      loading: false,
      searchSelect: [],
      searchForm: {
        scope_frame: 'all'
      },
      treeData: [
        {
          label: '全部',
          value: '',
          group: 1
        },
        {
          label: '我关注的',
          value: 'concern',
          group: 1
        },

        {
          label: '已签约',
          value: 'signed',
          group: 2
        },
        {
          label: '未签约',
          value: 'not_signed',
          group: 2
        },

        {
          label: '签约作废',
          value: 'void_signed',
          group: 2
        },

        {
          label: '过期合同',
          value: 'expired',
          group: 3
        },
        {
          label: '急需续费',
          value: 'urgent_renewal',
          group: 3
        },
        {
          label: '费用过期',
          value: 'cost_expired',
          group: 3
        }
      ],
      buildData: [],
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
      search: [],
      viewSearch: [],
      dropdownList: [
        { label: '移交同事', value: 1 },
        { label: '导出', value: 2 },
        { label: '导入', value: 3 }
      ]
    }
  },
  computed: {
    treeDataGroup() {
      let treeData = []
      for (let i = 0; i < this.treeData.length; i++) {
        if (!i || this.treeData[i].group != this.treeData[i - 1].group) {
          treeData.push({
            id: this.treeData[i].group,
            options: []
          })
        }
        treeData[treeData.length - 1].options.push(this.treeData[i])
      }
      return treeData
    }
  },

  created() {
    // this.where.types = this.types
    const query = this.$route.query
    if (query.id && query.name) {
      this.where.name = query.name
    }
    this.getTableData('', { scope_frame: 'all' })
    this.salesmanCustom()
    this.getConfigApprove()
  },

  mounted() {
    this.where.types = this.custom_type
    if (this.custom_type == 5) {
      this.where.scope_frame = 'all'
    } else {
      this.where.scope_frame = 'self'
    }
  },

  methods: {
    // 导出列表数据
    async exportContract() {
      if (this.total > 1000) {
        return this.$message.error('超出限制，最大支持导出1000条数据')
      }
      let listField = this.transferDataList.filter((item) => {
        return item.type && !['images', 'file', 'oaWangeditor'].includes(item.type)
      })
      // 添加ID项
      listField.unshift({
        name: 'ID',
        field: 'id'
      })
      this.saveName = '导出合同(' + this.$moment(new Date()).format('MMDDHHmmss') + ').xlsx'
      let title = []
      listField.map((item) => {
        title.push(item.name)
      })
      let aoaData = [title]
      if (this.loading) return
      this.loading = true
      let obj = {
        types: this.custom_type,
        statistics_type: this.labelText,
        is_export: 1
      }
      for (let key in this.where) {
        if (this.where[key]) {
          obj[key] = this.where[key]
        }
      }
      obj.page = 0
      obj.limit = 0

      if (this.custom_type === 6) {
        obj.scope_frame = 'self'
      }
      const listData = await contractViewApi(obj)
      if (listData.data.list.length > 0) {
        listData.data.list.forEach((value) => {
          let arr = []
          for (let i = 0; i < listField.length; i++) {
            arr.push(this.getFieldValue(listField[i], value[listField[i].field]))
          }
          aoaData.push(arr)
        })
        this.exportData.data = aoaData
        this.$refs.exportExcel.exportExcel()
      } else {
        this.$message.error('暂无数据')
      }
      this.loading = false
    },
    getFieldValue(field, val) {
      if (field.type === 'checked') {
        return val
          .map((item) => {
            return item.name
          })
          .join(',')
      } else if (field.field === 'contract_customer') {
        return val ? val.customer_name : ''
      } else if (field.type === 'single') {
        if (Array.isArray(val)) {
          return val.join('/')
        }
        return val || ''
      } else if (field.type === 'radio') {
        for (const key in field.dict) {
          if (parseInt(field.dict[key].value) === parseInt(val)) {
            return field.dict[key].label || ''
          }
        }
      } else if (field.type === 'multiple') {
        let arr = val.map((item) => item.join('/'))
        if (field.input_type === 'select') {
          if (arr.length === 1) {
            return arr[0] + '#'
          } else {
            return arr.join('#')
          }
        } else {
          return arr.join(',')
        }
      } else if (field.type === 'salesman') {
        return val ? val.name : ''
      } else {
        if (field.field === 'id') {
          return val + ''
        } else {
          return val || ''
        }
      }
    },
    // 标为异常
    markedAbnormal(row) {
      let text, status
      text = row.contract_status == 3 ? '正常' : '异常'
      status = row.contract_status == 3 ? 0 : 1
      this.$modalSure(this.$t(`您确定要将此合同标为${text}吗`)).then(() => {
        contractAbnormalApi(row.id, status)
        this.getTableData()
        this.salesmanCustom()
      })
    },
    handleBuild(item, val, type) {
      this.parameterData.customer_id = item.eid
      this.parameterData.contract_id = item.id
      this.$refs.editExamine.openBox(val, item.id, type)
    },
    getContractTag(row) {
      return getContractTagType(row)
    },
    getContractTexts(row) {
      return getContractText(row)
    },

    isOk() {
      this.where.types = ''
      this.getTableData()
    },
    async getConfigApprove() {
      const result = await configRuleApproveApi(0)
      this.buildData = result.data
    },
    focusEvt(item) {
      let status = item.contract_followed == 0 ? 1 : 0
      contractSubscribeApi(item.id, status).then((res) => {
        this.getTableData()
      })
    },
    // 关闭显示列弹框
    handleCloseTableItem() {
      this.tableItemDialogVisible = false
    },
    handleConfirmVisible(array) {
      let data = {
        select_type: 'list_select',
        data: array
      }
      saveSalesmanCustomApi(this.custom_type, data).then((res) => {
        this.tableItemDialogVisible = false
        this.tableHeaders = []
        this.salesmanCustom()
        this.getTableData()
      })
    },
    mapDict(dict) {
      for (let i = 0; i < dict.length; i++) {
        dict[i].name = dict[i].label
        if (dict[i].children) {
          this.mapDict(dict[i].children)
        }
      }
    },
    salesmanCustom() {
      salesmanCustomApi(this.custom_type).then((res) => {
        let search_select_list = res.data.search_select
        let search_list = res.data.search
        let search = []
        let viewSearch = []
        for (let i = 0; i < search_list.length; i++) {
          if (search_list[i].input_type == 'date') {
            search_list[i].input_type = 'date_picker'
          }
          if (search_list[i].field == 'customer_label') {
            search_list[i].input_type = 'tag'
          }
          if (search_list[i].input_type == 'select') {
            if (search_list[i].field == 'area_cascade') {
              search_list[i].input_type = 'cascader_address'
            } else if (search_list[i].type == 'single') {
              search_list[i].input_type = 'cascader_radio'
            } else if (search_list[i].type == 'multiple') {
              search_list[i].input_type = 'cascader'
            }
          }
          if (search_list[i].dict) {
            this.mapDict(search_list[i].dict)
          }
          search_list[i].form_value = search_list[i].input_type
          search_list[i].field_name_en = search_list[i].field
          search_list[i].field_name = search_list[i].name

          search_list[i].types = search_list[i].type
          search_list[i].type = search_list[i].input_type
          search_list[i].title = search_list[i].name
          search_list[i].options = search_list[i].dict || []
          search_list[i].data_dict = search_list[i].dict || []
          search_list[i].is_city_show = ''

          if (search_select_list.includes(search_list[i].field)) {
            search.push(search_list[i])
          } else {
            viewSearch.push(search_list[i])
          }
        }
        this.search = search
        this.viewSearch = viewSearch
        this.transferDataList = res.data.list
        this.defaultTableItemList = res.data.list_select
        const fieldMap = res.data.list.reduce((map, item) => {
          map[item.field] = item
          return map
        }, {})
        const searchMap = res.data.search.reduce((map, item) => {
          map[item.field] = item
          return map
        }, {})
        // 按 list_select 顺序提取数据
        this.searchSelect = res.data.search_select.map((field) => searchMap[field]).filter((item) => item)
        this.tableHeaders = res.data.list_select.map((field) => fieldMap[field]).filter((item) => item)
        this.getSalesmanCustom = res.data
      })
    },
    customSearchEvt() {
      this.tableItemDialogVisible = true
    },

    // 添加合同
    addContract(row) {
      this.contractFromData = {
        title: row ? '编辑合同' : '添加合同',
        edit: false,
        width: '570px'
      }
      this.$refs.addContract.openBox(row)
    },

    handleNodeClick(data) {
      this.where.page = 1
      this.where.statistics_type = data.value
      this.getTableData()
    },

    changeTimeSort(e) {
      switch (e.order) {
        case 'ascending':
          this.where.sort = e.prop + ' asc'
          break
        case 'descending':
          this.where.sort = e.prop + ' desc'
          break
        default:
          this.where.sort = ''
      }
      this.confirmData(this.where)
    },

    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },

    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },

    getTableData() {
      if (this.loading) return
      this.loading = true
      if (this.custom_type == 6) {
        this.where.scope_frame = 'self'
      }
      contractViewApi(this.where)
        .then((res) => {
          this.tableData = res.data.list
          this.total = res.data.count
          this.total_price = res.data.total_price || 0
          setTimeout(() => {
            this.loading = false
            this.$refs.multipleTable?.doLayout()
          }, 300)
        })
        .catch((error) => {
          this.loading = false
        })
    },

    // 转移
    handleTransfer(type, row = []) {
      if (this.ids.length <= 0 && type === 1) {
        this.$message.error(this.$t('customer.placeholder22'))
      } else {
        var ids = []
        if (type === 1) {
          // 批量
          this.ids.map((value) => {
            ids.push(value.id)
          })
        } else {
          if (this.ids.length > 0) {
            this.$nextTick(() => {
              for (var i = 0; i < this.$refs.multipleTable.length; i++) {
                this.$refs.multipleTable[i].clearSelection()
              }
            })
          }
          ids.push(row.id)
        }
        this.transferData = {
          title: type === 1 ? '移交其他同事' : this.$t('customer.transfersettings'),
          width: '520px',
          type: 2,
          ids
        }
        this.$refs.transferDialog.handleOpen()
      }
    },

    // 删除
    handleDelete(item) {
      this.$modalSure(this.$t('customer.placeholder27')).then(() => {
        clientContractDeleteApi(item.id).then((res) => {
          if (this.where.page > 1 && this.tableData.length <= 1) {
            this.where.page--
          }
          this.getTableData()
        })
      })
    },

    // 打开客户
    handleClient(item) {
      item.eid = item.eid
      item.cid = item.id
      this.fromData = {
        title: this.$t('customer.editcustomer'),
        width: '1000px',
        data: item
      }

      this.$refs.editCustomer.tabIndex = '1'
      this.$refs.editCustomer.tabNumber = 1
      this.$refs.editCustomer.openBox(item.eid)
    },

    // 查看
    async handleCheck(item) {
      item.cid = item.id
      this.fromData = {
        title: '查看合同',
        width: '1000px',
        data: item,
        isClient: false,
        name: item.client ? item.client.name : '',
        id: item.client ? item.client.id : '',
        edit: true
      }

      this.$refs.editContract.tabIndex = '1'
      this.$refs.editContract.tabNumber = 1
      this.$refs.editContract.openBox(item)
    },

    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: 15,
          types: this.where.types,
          scope_frame: this.where.types == 5 ? 'all' : 'self'
        }
      } else {
        if (this.custom_type == 5 && data.scope_frame && data.scope_frame.length > 0) {
          data.scope_frame = data.scope_frame[0]
        }

        this.where = { ...this.where, ...data }
      }

      this.searchForm = data
      this.where.page = 1
      this.getTableData()
    },

    handleSelectionChange(val) {
      this.ids = val
    },
    dropdownFn(item) {
      switch (item.value) {
        case 1:
          this.handleTransfer(1)
          break
        case 2:
          this.exportContract()
          break
        case 3:
          this.$refs.importExcel.btnClick()
          break
      }
    },
    importExcelData(arrRes) {
      let thead = arrRes[0]
      let data = []
      for (let i = 1; i < arrRes.length; i++) {
        data.push({})
        for (let j = 0; j < arrRes[i].length; j++) {
          if (!j) {
            arrRes[i][j] = Number(arrRes[i][j])
          }
          data[data.length - 1][thead[j]] = arrRes[i][j]
        }
      }
      contractImport(this.types, data).then((res) => {
        this.getTableData(this.labelText, this.searchForm)
      })
    }
  }
}
</script>

<style>
.el-tooltip__popper {
  max-width: 300px;
}
</style>
<style lang="scss" scoped>
.icon-star {
  i {
    font-size: 18px;
  }

  .icon-star-on {
    font-size: 24px;
    margin-left: -3px;
  }
}
.el-icon-info {
  margin-top: 4px;
  color: #1890ff;
  position: absolute;
  right: 15px;
}

.p14 {
  padding: 0 14px;
}
.right {
  border-left: 1px solid #eeeeee;
}
.left {
  padding: 0;
  .title {
    padding-left: 25px;
    font-size: 14px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 500;
    color: #303133;
  }
}
::v-deep .el-card__body {
  padding: 0;
}

.color1 {
  color: #ff9900;
}
.color2 {
  color: #19be6b;
}
.color3 {
  color: #ed4014;
}
.mark {
  display: inline-block;
  width: 80px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

::v-deep .el-button--primary.is-plain:hover {
  color: #1890ff;
  background: #e8f4ff;
  border-color: #a3d3ff;
}
.icon-star {
  i {
    font-size: 18px;
  }
  .icon-star-on {
    font-size: 24px;
    margin-left: -3px;
  }
}
.line1 {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.point {
  cursor: pointer;
  color: #1890ff;
}
.left .title {
  padding-left: 25px;
  padding-top: 20px;
  font-size: 14px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #303133;
}
.dropdown-menu-left {
  position: relative;
}
.dropdown-menu-right {
  width: 100px;
  position: absolute;
  top: -50px;
  right: 0;
}
.more {
  margin-left: 10px;
}
.contract-status-tag {
  background-color: transparent;
}
</style>
