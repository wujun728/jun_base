<!-- 固定物资管理 -->
<template>
  <div ref="bodyRef">
    <el-row :gutter="20">
      <el-col v-bind="gridl">
        <tree :parent-tree="treeData" :frame-id="frame_id" :types="types" @frameId="getFrameId" @getList="getList" />
      </el-col>
      <el-col v-bind="gridr">
        <div class="v-height-flag">
          <oaFromBox
            v-if="search.length > 0"
            :search="search"
            :alert="alertText"
            :dropdownList="dropdownList"
            :isViewSearch="false"
            :total="total"
            :title="`物资管理`"
            :isAddBtn="true"
            @addDataFn="handleManage"
            @dropdownFn="dropdownFn"
            @confirmData="confirmData"
          ></oaFromBox>

          <el-table
            class="mt10"
            :height="tableHeight"
            :data="tableData"
            style="width: 100%"
            row-key="id"
            default-expand-all
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55"> </el-table-column>
            <el-table-column prop="number" label="物资编号" min-width="150" show-overflow-tooltip />
            <el-table-column prop="name" label="物资名称" min-width="100" show-overflow-tooltip />
            <el-table-column prop="units" label="规格型号" min-width="100" show-overflow-tooltip />
            <el-table-column prop="cate.cate_name" label="物资分类" min-width="100" show-overflow-tooltip />
            <el-table-column prop="record[0].price" label="物资单价(元)" min-width="100" show-overflow-tooltip />
            <el-table-column prop="specs" label="计量单位" min-width="80" show-overflow-tooltip />
            <el-table-column pstatus label="状态" min-width="80" show-overflow-tooltip>
              <template slot-scope="scope">
                <el-tag v-if="scope.row.status === 0" type="success" size="mini">未使用</el-tag>
                <el-tag v-if="scope.row.status === 1" size="mini">已领用</el-tag>
                <el-tag v-if="scope.row.status === 2" type="success" size="mini">已归还</el-tag>
                <el-tag v-if="scope.row.status === 3" type="warning" size="mini">维修中</el-tag>
                <el-tag v-if="scope.row.status === 4" type="danger" size="mini">已报废</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="重要信息" min-width="180" show-overflow-tooltip>
              <template slot-scope="scope">
                <div v-if="scope.row.status === 1">
                  领用对象：{{
                    scope.row.receive_frame ? scope.row.receive_frame.name : scope.row.receive_user.name
                  }}
                  领用时间：{{
                    scope.row.receive_frame ? scope.row.receive_frame.created_at : scope.row.receive_user.created_at
                  }}
                </div>
                <div v-else>{{ scope.row.remark }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="describe" :label="$t('public.operation')" fixed="right" width="200">
              <template slot-scope="scope">
                <el-button type="text" @click="handleRecord(scope.row)" v-hasPermi="['material:fixed:manage:record']"
                  >记录</el-button
                >

                <el-button type="text" @click="handleEdit(scope.row)" v-hasPermi="['material:fixed:manage:edit']">{{
                  $t('public.edit')
                }}</el-button>

                <el-dropdown class="ml10">
                  <span class="el-dropdown-link el-button--text el-button">
                    {{ $t('hr.more') }}
                    <i class="el-icon-arrow-down el-icon--right" />
                  </span>
                  <el-dropdown-menu style="text-align: center">
                    <el-dropdown-item v-if="scope.row.status === 0" @click.native="handleMaterialData(5, scope.row)"
                      >领用</el-dropdown-item
                    >
                    <el-dropdown-item v-if="scope.row.status === 1" @click.native="handleMaterialData(6, scope.row)"
                      >归还</el-dropdown-item
                    >
                    <el-dropdown-item v-if="scope.row.status <= 2" @click.native="handleMaterialData(2, scope.row)"
                      >维修</el-dropdown-item
                    >
                    <el-dropdown-item v-if="scope.row.status === 3" @click.native="handleMaterialData(3, scope.row)"
                      >处理</el-dropdown-item
                    >
                    <el-dropdown-item v-if="scope.row.status !== 4" @click.native="handleMaterialData(1, scope.row)"
                      >报废</el-dropdown-item
                    >
                    <el-dropdown-item
                      v-if="scope.row.status === 0 || scope.row.status === 4"
                      @click.native="handleDelete(scope.row.id)"
                      >删除</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    <div class="page-fixed">
      <el-pagination
        :page-size="where.limit"
        :current-page="where.page"
        :page-sizes="[15, 20, 30]"
        layout="total,sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="pageChange"
      />
    </div>

    <add-material ref="addMaterial" :form-data="fromData" @isOk="handleSearch()"></add-material>
    <receive ref="receive" :form-data="receiveData" @isOk="handleSearch()"></receive>
    <record ref="record" :form-data="recordData"></record>
    <return-material ref="returnMaterial" :form-data="returnData" @isOk="handleSearch()"></return-material>
    <material-dialog ref="materialDialog" :from-data="materialData" @isOk="handleSearch()"></material-dialog>
    <export-excel :template="false" :save-name="saveName" :export-data="exportData" ref="exportExcel" />
    <!-- 批量移动物资 -->
    <oa-dialog
      ref="oaDialog"
      :fromData="fromData1"
      :formConfig="formConfig"
      :formRules="formRules"
      :formDataInit="formDataInit"
      @submit="submit"
    ></oa-dialog>
  </div>
</template>

<script>
import { storageCateApi, storageDeleteApi, storageListApi, storageListCateApi } from '@/api/administration'
export default {
  name: 'Consume',
  components: {
    tree: () => import('./tree'),
    addMaterial: () => import('./addMaterial'),
    receive: () => import('./receive'),
    record: () => import('./record'),
    returnMaterial: () => import('./return'),
    materialDialog: () => import('./materialDialog'),
    exportExcel: () => import('@/components/common/exportExcel'),
    oaFromBox: () => import('@/components/common/oaFromBox'),
    oaDialog: () => import('@/components/form-common/dialog-form')
  },
  data() {
    return {
      pickerOptions: this.$pickerOptionsTimeEle,
      alertText: '固定物资经过领取之后，领取人只为借用，最终需要归还给公司，例如：电脑、显示器等。',
      gridl: {
        xl: 3,
        lg: 4,
        md: 5,
        sm: 6,
        xs: 24
      },
      gridr: {
        xl: 21,
        lg: 20,
        md: 19,
        sm: 18,
        xs: 24
      },
      fromData1: {
        width: '500px',
        title: '批量移动物资分类',
        btnText: '确定',
        labelWidth: '80px',
        type: ''
      },
      formDataInit: { cate_id: '', ids: [] },
      formRules: {
        cate_id: [
          {
            required: true,
            message: '请选择物资类别',
            trigger: 'change'
          }
        ]
      },

      treeData: [],
      formConfig: [],
      frame_id: 0,
      where: {
        page: 1,
        limit: 15,
        types: 1,
        cid: ''
      },
      search: [
        {
          field_name: '物资名称/物资编号',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '物资状态',
          field_name_en: 'status',
          form_value: 'select',
          data_dict: [
            { value: '', name: '全部' },
            { value: '0', name: '未使用' },
            { value: 1, name: '已领用' },
            { value: 3, name: '维修中' },
            { value: 4, name: '已报废' }
          ]
        },
        {
          field_name: '入库时间',
          field_name_en: 'time',
          form_value: 'date_picker'
        }
      ],
      dropdownList: [
        { label: '批量移动', value: '4' },
        { label: '导出', value: '1' },
        {
          label: '领用',
          value: '2'
        },
        {
          label: '归还',
          value: '3'
        }
      ],

      total: 0,
      tableData: [],
      fromData: {},
      receiveData: {},
      recordData: {},
      returnData: {},
      materialData: {},
      options: [],
      selectData: [],
      multipleSelection: [],
      types: 1,
      saveName: '',
      exportData: {
        data: [],
        cols: [
          { wpx: 120 },
          { wpx: 120 },
          { wpx: 120 },
          { wpx: 80 },
          { wpx: 80 },
          { wpx: 80 },
          { wpx: 80 },
          { wpx: 180 },
          { wpx: 180 }
        ]
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: 15,
          types: 1,
          cid: ''
        }
      } else {
        this.where = { ...this.where, ...data }
        this.where.page = 1
      }

      this.getTableData()
    },
    handleSelectionChange(val) {
      this.multipleSelection = []
      val.map((item) => {
        this.multipleSelection.push(item.id)
      })
    },
    // 获取权限列表
    getList() {
      storageCateApi({ type: this.types })
        .then(async (res) => {
          this.treeData = res.data

          this.formConfig = [
            {
              type: 'cascaderNew',
              label: '物资类别:',
              placeholder: '请搜索选择物资类别',
              key: 'cate_id',

              options: this.treeData
            }
          ]
          this.getTableData()
        })
        .catch((error) => {})
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    dropdownFn(item) {
      if (item.value == 1) {
        this.handleExport()
      } else if (item.value == 2) {
        this.handleReceive()
      } else if (item.value == 3) {
        this.handleReturn()
      } else if (item.value == 4) {
        if (this.multipleSelection.length == 0) return this.$message.error('请选择要操作的物资')
        this.$refs.oaDialog.openBox()
      }
    },

    submit(data) {
      data.ids = this.multipleSelection
      data.cate_id = data.cate_id[data.cate_id.length - 1]
      storageListCateApi(data).then((res) => {
        if (res.status == 200) {
          this.$refs.oaDialog.handleClose()
          this.getTableData()
        }
      })
    },
    handleExport() {
      // 先将对象变为字符串，然后再变为json对象，防止对象的指针指向问题，为深拷贝
      let where = JSON.parse(JSON.stringify(this.where))
      where.limit = 0
      this.saveName = '导出固定物资_' + this.$moment(new Date()).format('MM_DD_HH_mm_ss') + '.xlsx'
      storageListApi(where)
        .then((res) => {
          let data = res.data.list
          if (data.length <= 0) {
            this.$message.error(this.$t('access.placeholder24'))
          } else {
            const aoaData = [
              [
                '物资编号',
                '物资名称',
                '规格型号',
                '物资分类',
                '物资单价',
                '计量单位',
                '物资状态',
                '重要信息',
                '创建时间'
              ]
            ]
            data.forEach((value) => {
              aoaData.push([
                value.number,
                value.name,
                value.units,
                value.cate ? value.cate.cate_name : '',
                value.record[0].price,
                value.specs,
                this.getMaterialStatus(value.status),
                value.remark,
                value.created_at
              ])
            })
            this.exportData.data = aoaData
            this.$refs.exportExcel.exportExcel()
          }
        })
        .catch((error) => {})
    },
    // 获取表格数据
    getTableData() {
      storageListApi(this.where)
        .then((res) => {
          this.tableData = res.data.list || []
          this.total = res.data.count
        })
        .catch((error) => {})
    },
    getSelectTableData(status) {
      const distinct = status === '' ? 'name' : ''
      storageListApi({ types: this.types, page: 0, distinct: distinct, limit: 0, status: status }).then((res) => {
        this.selectData = res.data.list || []
        if (status === 0) {
          // 领用物资列表
          this.receiveData.selectData = this.selectData
        } else if (status === '') {
          // 领用新增物资列表
          this.fromData.selectData = this.selectData
        }
      })
    },
    handleSearch() {
      this.where.page = 1
      this.getTableData()
    },

    getFrameId(data) {
      this.where.cid = data ? data : ''
      this.where.page = 1
      this.getTableData()
    },
    handleManage() {
      this.getSelectTableData('')
      this.fromData = {
        title: '新增入库(固定物资)',
        width: 720,
        treeData: this.treeData,
        selectData: this.selectData,
        edit: false,
        type: this.types
      }
      this.$refs.addMaterial.openBox()
    },
    handleReceive() {
      this.getSelectTableData(0)
      this.receiveData = {
        title: '领用(固定物资)',
        width: 820,
        selectData: this.selectData,
        type: this.types
      }
      this.$refs.receive.openBox()
    },
    handleReturn() {
      this.returnData = {
        title: '归还(固定物资)',
        width: 820,
        type: this.types
      }
      this.$refs.returnMaterial.openBox()
    },
    handleRecord(row) {
      this.recordData = {
        title: '记录详情(固定物资)',
        width: 820,
        data: row,
        type: this.types
      }
      this.$refs.record.where.storage_id = row.id
      this.$refs.record.usersWhere.storage_id = row.id
      this.$refs.record.openBox()
    },
    getMaterialStatus(value) {
      let str = ''
      if (value === 0) {
        str = '未使用'
      } else if (value === 1) {
        str = '已领用'
      } else if (value === 2) {
        str = '已归还'
      } else if (value === 3) {
        str = '维修中'
      } else if (value === 4) {
        str = '已报废'
      }
      return str
    },
    handleEdit(row) {
      if (row.cate.path.length <= 0) {
        row.cate.path.unshift(0) // 添加总分类
      }
      if (!row.cate.path.includes(row.cid)) {
        row.cate.path.push(row.cid) // 添加当前分类
      }
      this.fromData = {
        title: '编辑入库(固定物资)',
        width: 720,
        treeData: this.treeData,
        edit: true,
        data: row,
        type: this.types
      }
      this.$refs.addMaterial.openBox()
    },
    async handleDelete(id) {
      await this.$modalSure('你确定要删除这条内容吗')
      await storageDeleteApi(id)
      let totalPage = Math.ceil((this.total - 1) / this.where.limit)
      let currentPage = this.where.page > totalPage ? totalPage : this.where.page
      this.where.page = currentPage < 1 ? 1 : currentPage
      await this.getTableData()
    },
    handleMaterialData(type, row) {
      let title = ''
      let label = ''
      let placeholder = ''
      if (type === 1) {
        title = '报废'
        label = '报废原因'
        placeholder = '请输入报废原因'
      } else if (type === 2) {
        title = '维修'
        label = '维修原因'
        placeholder = '请输入维修原因'
      } else if (type === 3) {
        title = '维修处理'
        label = '备 注'
        placeholder = '请输入备注'
      } else if (type === 5) {
        title = '物资领用（固定物资）'
        label = '备 注'
        placeholder = '请输入备注'
      } else if (type === 6) {
        title = '物资归还（固定物资）'
        label = '备 注'
        placeholder = '请输入备注'
      }
      this.materialData = {
        title: title,
        width: '620px',
        label: label,
        placeholder: placeholder,
        data: row,
        type: type
      }
      this.$refs.materialDialog.handleOpen()
    }
  }
}
</script>

<style lang="scss" scoped>
.list-header {
  padding: 0;
  border-bottom: none;
}
.circular {
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  margin-top: -2px;
  margin-right: 5px;
}
::v-deep .el-form-item {
  margin-right: 10px;
}

.table-box {
  ::v-deep .el-table th {
    background-color: rgba(247, 251, 255, 1);
  }
}
</style>
