<!-- 客户管理 -->
<template>
  <div class="divBox">
    <el-card :body-style="{ padding: '20px 20px 20px 20px' }" class="normal-page">
      <oaFromBox
        :dropdownList="dropdownList"
        :isAddBtn="activeName == '1'"
        :search="search"
        :ids="ids"
        :title="$route.meta.title"
        :total="total"
        :treeData="treeDataGroup"
        :treeDefault="defaultFrame"
        :viewSearch="viewSearch"
        btnText="添加客户"
        @addDataFn="addDataFn"
        @confirmData="confirmData"
        @dropdownFn="dropdownFn"
        @treeChange="handleNodeClick"
      ></oaFromBox>

      <div class="mt10" v-loading="loading">
        <el-table
          key="tab"
          ref="table"
          :data="tableData"
          :height="tableHeight"
          default-expand-all
          row-key="id"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column min-width="55" show-overflow-tooltip type="selection"> </el-table-column>
          <el-table-column
            v-for="header in tableHeaders"
            :key="header.field"
            :label="header.name"
            :min-width="header.field === 'customer_label' ? '270' : '160'"
            :prop="header.field"
            :show-overflow-tooltip="header.field === 'customer_label' ? false : true"
          >
            <template slot-scope="scope">
              <span
                v-if="(header.field === 'creator' && scope.row[header.field]) || header.field === 'before_salesman'"
              >
                {{ scope.row[header.field].name }}
              </span>
              <span
                v-else-if="(header.field === 'c45aa76a' && header.type == 'radio') || header.field === 'customer_way'"
                >{{ scope.row[header.field][0] || '--' }}</span
              >
              <div v-else-if="header.field === 'customer_status'">
                <el-tag>{{ getCustomerStatusLabel(header, scope.row) }}</el-tag>
              </div>
              <span
                v-else-if="
                  (header.field === 'c45aa76a' && header.type == 'checked') ||
                  header.field === 'area_cascade' ||
                  header.type == 'multiple' ||
                  header.type == 'single'
                "
              >
                <span v-for="(item, index) in scope.row[header.field]" :key="index">
                  {{ item || '--' }}
                  <span v-if="header.field === 'area_cascade'">{{
                    scope.row[header.field].length - 1 == index ? '' : '/'
                  }}</span>
                  <span v-else>{{ scope.row[header.field].length - 1 == index ? '' : '、' }}</span>
                </span>
                <span v-if="scope.row[header.field].length == 0">--</span>
              </span>
              <div v-else-if="header.field === 'customer_followed'" class="icon-star">
                <i
                  :class="
                    scope.row.customer_followed === 0
                      ? 'el-icon-star-off'
                      : 'el-icon-star-on color-collect icon-star-on'
                  "
                  class="pointer"
                  @click="focusEvt(scope.row)"
                ></i>
              </div>
              <div v-else-if="header.field === 'customer_label'" class="pointer customer-label">
                <!-- 大于两条浮窗 -->
                <el-popover v-if="scope.row[header.field].length > 2" placement="top-start" trigger="hover">
                  <template>
                    <div class="flex_box">
                      <div v-for="(item, index) in scope.row[header.field]" :key="index" class="tips">
                        <el-tag v-if="item.name.length <= 6" size="small">
                          {{ item.name }}
                        </el-tag>
                        <el-tag v-else size="small">
                          {{ item.name }}
                        </el-tag>
                      </div>
                    </div>
                  </template>
                  <div slot="reference">
                    <div class="flex_box">
                      <div v-for="(item, index) in scope.row[header.field]" :key="index" class="tips">
                        <el-tag v-if="index < 2" size="small">
                          {{ item.name }}
                        </el-tag>
                      </div>
                      <el-tag v-if="scope.row[header.field].length > 2" size="small">...</el-tag>
                    </div>
                  </div>
                </el-popover>
                <!-- 不需要浮窗 -->
                <template v-else>
                  <div class="flex_box">
                    <div v-for="(item, index) in scope.row[header.field]" :key="index" class="tips">
                      <el-tag v-if="index < 2" size="small">
                        {{ item.name }}
                      </el-tag>
                    </div>
                    <el-tag v-if="scope.row[header.field].length > 2" size="small">...</el-tag>
                  </div>
                </template>
              </div>
              <span v-else-if="header.field === 'liaison_tel'">
                {{
                  scope.row[header.field] && scope.row[header.field].liaison_name
                    ? scope.row[header.field].liaison_name
                    : '--'
                }}
                :
                {{ scope.row[header.field].liaison_tel ? scope.row[header.field].liaison_tel : '--' }}
              </span>
              <span v-else-if="header.field === 'salesman'">
                <img
                  :src="scope.row[header.field].avatar"
                  alt=""
                  style="width: 24px; height: 24px; border-radius: 50%; margin-right: 7px; vertical-align: bottom"
                />
                {{ scope.row[header.field].name }}
              </span>
              <div v-else-if="header.input_type === 'images'">
                <div v-for="item in scope.row[header.field]" :key="item.id" class="fileItem">
                  <el-image
                    v-if="item.att_dir"
                    :preview-src-list="[item.att_dir]"
                    :src="item.att_dir"
                    style="width: 100px; height: 100px"
                  >
                  </el-image>
                </div>
              </div>
              <span v-else-if="header.field === 'customer_name'" class="point" @click="handleCheck(scope.row)">{{
                scope.row[header.field] || '--'
              }}</span>
              <span v-else>{{ scope.row[header.field] || '--' }} </span>
            </template>
          </el-table-column>

          <!-- 合同列表操作 -->
          <el-table-column fixed="right" label="操作" prop="address" show-overflow-tooltip width="210">
            <template slot="header">
              操作
              <i class="el-icon-setting pointer" @click="customSearchEvt"></i>
            </template>
            <template slot-scope="scope">
              <el-button v-hasPermi="['customer:list:check']" type="text" @click="handleCheck(scope.row)"
                >查看</el-button
              >
              <el-button
                v-if="types == 2 || (types == 1 && userId == scope.row.salesman.id)"
                type="text"
                @click="handleFollowUp(scope.row)"
                >填写跟进</el-button
              >
              <el-button v-if="types == 3 && scope.row.customer_status != 2" type="text" @click="receive(0, scope.row)"
                >领取</el-button
              >
              <el-dropdown>
                <span class="el-dropdown-link el-button--text el-button more">
                  更多
                  <i class="el-icon-arrow-down" />
                </span>
                <el-dropdown-menu style="text-align: center">
                  <el-dropdown-item
                    v-if="types == 2 || (types == 1 && userId == scope.row.salesman.id)"
                    @click.native="addContract(scope.row)"
                  >
                    添加合同
                  </el-dropdown-item>
                  <el-dropdown-item @click.native="addFinance('edit', scope.row)"> 编辑资料 </el-dropdown-item>
                  <el-dropdown-item v-if="types !== 3" @click.native="handleTransfer(2, scope.row)">
                    移交同事
                  </el-dropdown-item>
                  <el-dropdown-item v-if="types !== 3" @click.native="handleReturn(0, scope.row)">
                    退回公海
                  </el-dropdown-item>
                  <el-dropdown-item v-if="types == 3" @click.native="markedLoss(scope.row, 1)">
                    {{ scope.row.customer_status == 2 ? '取消流失' : '标为流失' }}
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <select-label
                      ref="selectLabel"
                      :labelList="scope.row.customer_label || []"
                      :props="{ children: 'children', label: 'name' }"
                      @handleLabelConf="handleLabelConf($event, scope.row)"
                      :slotType="`customer`"
                    >
                    </select-label>
                  </el-dropdown-item>
                  <el-dropdown-item @click.native="handleDelete(scope.row)">删除 </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
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
    </el-card>

    <!-- 修改客户状态 -->
    <el-dialog
      :before-close="handleClose"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      title="修改客户状态"
      width="30%"
    >
      <el-form class="mt20" label-width="80px">
        <el-form-item label="客户状态：" prop="resource">
          <el-radio-group v-model="resource">
            <el-radio label="0">跟进中</el-radio>
            <el-radio label="1">已成交</el-radio>
            <el-radio label="2">已放弃</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="followFn">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 通用弹窗表单   -->
    <dialog-form ref="dialogForm" :form-data="formBoxConfig" @isOkEdit="getTableData()" />
    <edit-customer
      ref="editCustomer"
      :custom_type="types"
      :form-data="fromData"
      @isOkEdit="getTableData()"
    ></edit-customer>
    <add-contract ref="addContract" :form-data="contractFromData"></add-contract>
    <transfer-dialog ref="transferDialog" :from-data="transferData" @handleTransfer="getTable"></transfer-dialog>
    <!-- 跟进弹窗 -->
    <el-dialog :visible.sync="dialogShow" class="record" title="添加跟进记录" width="40%">
      <recordUpload :form-info="formInfo" @change="recordChange"></recordUpload>
    </el-dialog>
    <!-- 退回公海 -->
    <el-dialog :append-to-body="true" :visible.sync="returnShow" title="退回客户公海" width="40%">
      <el-form ref="returnForm" :model="returnForm" :rules="rule">
        <el-form-item label="说明原因：" label-width="90px" prop="reason">
          <el-input
            v-model="returnForm.reason"
            :autosize="{ minRows: 4, maxRows: 10 }"
            :maxlength="100"
            placeholder="请输入备注信息，最多可输入100字"
            type="textarea"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submit()">确 定</el-button>
      </div>
    </el-dialog>
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
    <image-viewer ref="imageViewer" :src-list="srcList"></image-viewer>
    <!-- 导出组件 -->
    <export-excel ref="exportExcel" :export-data="exportData" :save-name="saveName" :template="false" />
    <!-- 导入组件 -->
    <import-excel v-show="false" ref="importExcel" @importExcelData="importExcelData"></import-excel>
  </div>
</template>
<script>
import { fileLinkDownLoad, getFileType } from '@/libs/public'
import SettingMer from '@/libs/settingMer'
import imageViewer from '@/components/common/imageViewer'
import {
  clientDataDeleteApi,
  clientDataLabelApi,
  clientDataStatusApi,
  getSalesman,
  salesmanCustomApi,
  saveSalesmanCustomApi,
  customerViewApi,
  customerSubscribeApi,
  customerReturnApi,
  customerLostApi,
  customerCancelLostApi,
  customerClaimApi,
  customerImport
} from '@/api/enterprise'
import ExportExcel from '@/components/common/exportExcel'
import importExcel from '@/components/common/importExcel'
export default {
  name: 'FinanceList',
  components: {
    ExportExcel,
    importExcel,
    imageViewer,
    recordUpload: () => import('@/views/customer/list/components/recordUpload'),
    dialogForm: () => import('./components/index'),
    editCustomer: () => import('./components/editCustomer'),
    selectLabel: () => import('@/components/form-common/select-label'),
    addContract: () => import('@/views/customer/contract/components/addContract'),
    transferDialog: () => import('@/views/customer/list/components/transferDialog'),
    VisibleDialog: () => import('@/components/form-common/dialog-transfer'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  props: {
    activeName: {
      type: String,
      default: '1'
    },
    types: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      returnForm: {
        reason: ''
      },
      dialogVisible: false,
      dialogShow: false,
      returnShow: false,
      resource: '',
      fromData: {},
      formBoxConfig: {},
      gettime: '',
      formInfo: {
        avatar: '',
        type: 'add',
        show: 1,
        data: {},
        follow_id: 0
      },
      userId: JSON.parse(localStorage.getItem('userInfo')).id,
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
          label: '未成交',
          value: 'unsettled',
          group: 2
        },
        {
          label: '已成交',
          value: 'traded',
          group: 2
        },

        {
          label: '急需跟进',
          value: 'urgent_follow_up',
          group: 1
        }
      ],
      fileLinkDownLoadUrl: '',
      tableData: [],
      tab: '',
      labelText: '',
      salesmanList: [],
      defaultFrame: '',
      where: {
        page: 1,
        limit: 15,
        types: this.types,
        scope_frame: 'all'
      },
      total: 0,
      type: '1',
      labelData: {},
      id: null,
      ids: [],
      eid: null,
      contractFromData: {},
      transferData: {},
      loading: false,
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      tableItemDialogVisible: false,
      defaultTableItemList: [],
      transferDataList: [],
      visibleTableItemList: [],
      getSalesmanCustom: [],
      tableHeaders: [], // 表格的表头
      srcList: [],
      rule: {
        reason: [{ required: true, message: '请输入备注信息', trigger: 'blur' }]
      },
      checkedId: [],
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
      dropdownList: [
        { label: '设置标签', value: 1 },
        { label: '移交同事', value: 2 },
        { label: '分配', value: 5 },
        { label: '领取', value: 6 },
        { label: '标为流失', value: 7 },
        { label: '退回公海', value: 3 },
        { label: '导出', value: 4 },
        { label: '导入', value: 8 }
      ],
      viewSearch: []
    }
  },

  created() {
    let dropdownValueList = [1, 2, 3, 4, 8]
    if (this.types == 3) {
      dropdownValueList = [1, 5, 6, 7, 4, 8]
    }
    for (let i = 0; i < this.dropdownList.length; i++) {
      if (!dropdownValueList.includes(this.dropdownList[i].value)) {
        this.dropdownList.splice(i, 1)
        i--
      }
    }
    this.where.types = this.types
    const query = this.$route.query
    if (query.id && query.name) {
      this.where.name = query.name
    }

    this.defaultFrame = ''

    this.salesmanCustom()
  },
  mounted() {
    if (this.types == 2) {
      this.where.scope_frame = 'self'
    } else {
      this.where.scope_frame = 'all'
    }
    this.getTableData()
  },
  computed: {
    fileUrl() {
      return SettingMer.https + `/client/import`
    },

    // treeData分组
    treeDataGroup() {
      let treeData = []
      if (this.types != 3) {
        for (let i = 0; i < this.treeData.length; i++) {
          if (!i || this.treeData[i].group != this.treeData[i - 1].group) {
            treeData.push({
              options: []
            })
          }
          treeData[treeData.length - 1].options.push(this.treeData[i])
        }
      }
      return treeData
    }
  },
  methods: {
    getCustomerStatusLabel(header, row) {
      if (header.dict_ident === "customer_status") {
        if (!this._customerStatusLabelMap) {
          this._customerStatusLabelMap = header.dict.reduce((acc, { value, label }) => {
            acc[value] = label;
            return acc;
          }, {});
        }

        if (row.customer_status) {
          return this._customerStatusLabelMap[row.customer_status];
        }
      }

      return "--";
    },
    focusEvt(item) {
      let status = item.customer_followed ? 0 : 1
      customerSubscribeApi(item.id, status).then(() => {
        this.getTableData()
      })
    },
    toSrcIcon(name) {
      return getFileType(name)
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
      saveSalesmanCustomApi(this.types, data).then((res) => {
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
      salesmanCustomApi(this.types).then((res) => {
        let search_select_list = res.data.search_select
        let search_list = res.data.search
        let search = []
        let searchs = []
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

          search_list[i].title = search_list[i].name
          search_list[i].options = search_list[i].dict
          search_list[i].type = search_list[i].input_type
          search_list[i].is_city_show = ''
          search_list[i].is_city_show = ''

          if (search_select_list.includes(search_list[i].field)) {
            search.push(search_list[i])
          } else {
            viewSearch.push(search_list[i])
          }
        }
        searchs = [search[1], search[2], search[0]]
        this.search = searchs
        this.viewSearch = viewSearch

        const { list, list_select } = res.data
        this.transferDataList = list
        this.defaultTableItemList = list_select
        const fieldMap = res.data.list.reduce((map, item) => {
          map[item.field] = item
          return map
        }, {})
        const searchMap = res.data.search.reduce((map, item) => {
          map[item.field] = item
          return map
        }, {})
        // 按 list_select 顺序提取数据
        this.tableHeaders = res.data.list_select.map((field) => fieldMap[field]).filter((item) => item)
        this.$nextTick(() => {
          this.$refs.table?.doLayout()
        })
        this.getSalesmanCustom = res.data
      })
    },
    customSearchEvt() {
      this.tableItemDialogVisible = true
    },
    getTable() {
      this.getTableData()
    },
    // 添加跟进记录
    handleFollowUp(item) {
      this.formInfo.data.eid = item.id
      this.dialogShow = true
    },
    recordChange() {
      this.dialogShow = false
    },

    async getTableData() {
      if (this.loading) return
      this.loading = true
      this.where.statistics_type = this.labelText
      if (this.types == 2) {
        this.where.scope_frame = 'self'
      }
      const res = await customerViewApi(this.where)
      this.tableData = res.data.list
      this.total = res.data.count
      this.loading = false
      this.$nextTick(() => {
        this.$refs.table.doLayout()
      })
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.where.page = 1
      this.getTableData()
    },
  
    handleNodeClick(data) {
      this.labelText = data.value
      this.where.page = 1
      if (data) {
        this.where.follows = data.value
      } else {
        this.where.follows = ''
      }
      this.getTableData(this.labelText)
    },

    
    // 导出列表数据
    async exportList() {
      if (this.total > 1000) {
        return this.$message.error('超出限制，最大支持导出1000条数据')
      }
      let listField = this.transferDataList.filter((item) => {
        return item.type && !['images', 'file', 'oaWangeditor'].includes(item.type)
      })
      this.saveName = '导出客户(' + this.$moment(new Date()).format('MMDDHHmmss') + ').xlsx'
      let title = []
      listField.map((item) => {
        title.push(item.name)
      })
      let aoaData = [title]
      if (this.loading) return
      this.loading = true

      let obj = {
        types: this.types,
        scope_frame: this.where.scope_frame,
        statistics_type: this.labelText,
        page: 0,
        limit: 0,
        is_export: 1
      }
      for (let key in this.where) {
        if (this.where[key]) {
          obj[key] = this.where[key]
        }
      }
      obj.page = 0
      obj.limit = 0
      const listData = await customerViewApi(obj)
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
            return typeof item == 'string' ? item : item.name || ''
          })
          .join('/')
      } else if (field.type === 'single') {
        if (Array.isArray(val)) {
          return val.join('/')
        } else if (field.field === 'customer_status') {
          return val > 1 ? '已流失' : val == 1 ? '已成交' : '未成交'
        }
        return val || ''
      } else if (field.type === 'radio') {
        for (const key in field.dict) {
          if (parseInt(field.dict[key].value) === parseInt(val)) {
            return field.dict[key].label || ''
          }
        }
      } else if (field.type === 'multiple') {
        return val.join(',')
      } else if (field.type === 'salesman') {
        return val ? val.name : ''
      } else {
        return val || ''
      }
    },
    // 添加客户
    async addFinance(str, row) {
      this.formBoxConfig = {
        title: str === 'edit' ? '编辑客户' : '新增客户',
        width: '570px'
      }
      this.$refs.dialogForm.openBox(this.types, row, str)
    },

    // 查看
    async handleCheck(item) {
      item.eid = item.id
      item.cid = 0
      this.fromData = {
        title: this.$t('customer.editcustomer'),
        width: '1000px',
        data: item,
        types: this.types
      }

      this.$refs.editCustomer.tabIndex = '1'
      this.$refs.editCustomer.tabNumber = 1
      this.$refs.editCustomer.openBox(item.id, this.types)
    },

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
            this.$refs.table.clearSelection()
          }
          ids.push(row.id)
        }
        this.transferData = {
          title: type === 1 ? '移交其他同事' : this.$t('customer.transfersettings'),
          width: '520px',
          type: 1,
          ids
        }
        this.$refs.transferDialog.handleOpen()
      }
    },
    
    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: 15,
          types: this.types,
          scope_frame: this.types == 2 ? 'self' : 'all',
          cid: '',
          name: '',
          label: [],
          follows: '',
          time: '',
          salesman_id: ''
        }
        this.labelText = ''

        this.getTableData('')
      } else {
        if (this.types !== 2 && data.scope_frame && data.scope_frame.length > 0) {
          data.scope_frame = data.scope_frame[0]
        }
        this.where.page = 1
        for (let key in data) {
          this.where[key] = data[key]
        }

        this.getTableData()
      }
    },
    // 删除
    async handleDelete(item) {
      await this.$modalSure(this.$t('customer.message06'))
      await clientDataDeleteApi(item.id)
      if (this.where.page > 1 && this.tableData.length <= 1) {
        this.where.page--
      }
      await this.getTableData()
    },

    followFn() {
      const data = {
        status: this.resource,
        types: 0
      }
      this.handleStatus(this.eid, data)
      this.dialogVisible = false
    },

    handleClose() {
      this.dialogVisible = false
    },

    // 关注
    handleFollow(row, type) {
      // 直接根据条件计算状态值，避免使用中间变量
      const status = type === 1 ? (row.follow === 0 ? 1 : 0) : (row.up_follow === 0 ? 1 : 0);
      const data = {
        status,
        types: 1
      };
      this.handleStatus(row.id, data);
    },

    // 修改状态与关注
    async handleStatus(id, data) {
      await clientDataStatusApi(id, data)
      await this.getTableData()
    },
    
    handleLabelConf(res, id) {
      // 若选中的标签列表为空，提示错误信息
      if (res.list.length === 0) {
        return this.$message.error(this.$t('customer.placeholder58'));
      }
      // 收集客户ID
      const data = id ? [id.id] : this.ids.map(value => value.id);
      // 收集标签ID
      const label = res.list.map(value => value.id);
      // 调用批量设置标签的方法
      this.batchSetLabel({ data, label });
    },
    handleLabel(row) {
      this.$refs.selectLabel.handlePopoverShow()
    },
    // 退回公海
    handleReturn(type, row) {
      if (this.ids.length <= 0 && type === 1) {
        this.$message.error(this.$t('customer.placeholder22'))
      } else {
        let checkedId = Array.from(this.checkedId)
        if (row) {
          this.id = [row.id]
        }
        this.returnShow = true
      }
    },
    cancel() {
      this.returnForm.reason = ''
      this.returnShow = false
    },
    // 确定退回公海
    submit() {
      let checkedId = Array.from(this.checkedId)
      let data = {
        data: checkedId.length ? checkedId : this.id,
        reason: this.returnForm.reason
      }
      this.$refs.returnForm.validate((valid) => {
        if (valid) {
          customerReturnApi(data).then((res) => {
            this.cancel()
            this.getTableData()
          })
        }
      })
    },
    // 标为流失
    markedLoss(row, val) {
      if (this.checkedId.length == 0 && val !== 1) {
        return this.$message.error('至少选择一项')
      }

      let checkedId = Array.from(this.checkedId)
      let id = checkedId.length && val !== 1 ? checkedId : [row.id]
      if (row && row.customer_status == 2) {
        this.$modalSure(this.$t('您确定要将此客户取消流失吗')).then(() => {
          customerCancelLostApi(row.id)
          setTimeout(() => {
            this.getTableData()
          }, 300)
          this.salesmanCustom()
        })
      } else {
        this.$modalSure(this.$t('您确定要将此客户标为流失吗')).then(() => {
          customerLostApi({ data: id })
          setTimeout(() => {
            this.getTableData()
          }, 300)
          this.salesmanCustom()
        })
      }
    },
    //领取客户
    receive(type, row) {
      if (this.ids.length <= 0 && type === 1) {
        this.$message.error(this.$t('customer.placeholder22'))
      } else {
        let checkedId = Array.from(this.checkedId)
        let id = checkedId.length ? checkedId : [row.id]
        this.$modalSure(this.$t('您确定要领取此客户吗')).then(async () => {
          await customerClaimApi({ data: id })
          await this.getTableData()
          await this.salesmanCustom()
        })
      }
    },
    // 批量设置标签
    async batchSetLabel(data) {
      await clientDataLabelApi(data)
      this.id = null
      this.ids = []
      this.getTableData()
      this.tab = 1
    },
    labelGroup(val) {
      let data = []
      let label = []

      this.ids.map((value) => {
        data.push(value.id)
      })
      label = val.ids
      this.batchSetLabel({ data, label })
    },
    handleSelectionChange(val) {
      this.checkedId = val.map((item) => item.id)
      this.ids = val
    },
    // 添加合同
    addContract(row) {
      this.contractFromData = {
        title: this.$t('customer.addcontract'),
        id: row.id,
        name: row.name,
        edit: false,
        width: '570px'
      }
      this.$refs.addContract.openBox()
    },

    addDataFn() {
      this.addFinance()
    },
    dropdownFn(item, val) {
      switch (item.value) {
        case 1:
          this.labelGroup(val)
          break
        case 2:
        case 5:
          this.handleTransfer(1)
          break
        case 3:
          this.handleReturn(1)
          break
        case 4:
          this.exportList()
          break
        case 6:
          this.receive(1)
          break
        case 7:
          this.markedLoss()
          break
        case 8:
          this.$refs.importExcel.btnClick()
          break
      }
    },
    importExcelData(arrRes) {
      // 提取表头
      const [thead, ...rows] = arrRes;
      const data = [];
      // 过滤掉全为空字符串的行，并转换为对象格式
      rows.forEach((row) => {
        const isAllEmpty = row.every((cell) => cell.trim() === '');
        if (!isAllEmpty) {
          const rowData = {};
          row.forEach((cell, index) => {
            rowData[thead[index]] = cell;
          });
          data.push(rowData);
        }
      });

      // 调用导入 API 并更新表格数据
      customerImport(this.types, data).then(() => {
        this.getTableData('');
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.m14 {
  padding: 14px;
}

.el-icon-info {
  margin-top: 4px;
  color: #1890ff;
  position: absolute;

  right: 15px;
}

.tooltip-wrap {
  max-width: 250px !important;
}

.right {
  border-left: 1px solid #eeeeee;
  padding-top: 14px;
}

.left {
  padding: 0;

  .title {
    padding-left: 25px;
    font-size: 14px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #303133;
  }
}

.tree .smallHand {
  cursor: pointer;
}

.boder {
  border: none;
}

::v-deep .el-card__body {
  padding: 0;
}

.upload {
  display: inline-block;
  margin-left: 10px;
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

.customer-tag {
  margin-right: 6px;
  background-color: transparent;
}

.fileItem {
  margin: 0;
  cursor: pointer;
  width: var(--width);
  display: inline-block;
  height: 48px;
  line-height: 1;
  align-items: center;
  position: relative;

  .file-close {
    font-size: 18px;
    position: absolute;
    top: 14px;
    right: 10px;
    color: #c0c4cc;
    cursor: pointer;
  }
  .el-image {
    margin-right: 10px;
  }
}

.left .title {
  padding-left: 25px;
  padding-top: 20px;
  font-size: 14px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #303133;
}

.el-table .cell {
  height: 24px;
}

.ml14 {
  margin-left: 14px !important;
}

.mr14 {
  margin-right: 14px !important;
}

.el-tooltip.pointer.line1.item {
  display: flex;
}

.more {
  margin-left: 10px;
}

.flex_box {
  display: flex;

  .tips {
    span {
      margin-right: 10px;
    }
  }
}
::v-deep .divBox .el-tag {
  max-width: 91px;
  overflow: hidden;
  text-overflow: ellipsis;
}
.customer-label .el-tag {
  border: 0;
}
.point {
  cursor: pointer;
  color: #1890ff;
}
</style>
