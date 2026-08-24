<!-- 消耗物资-物资管理 -->
<template>
  <div>
    <el-row :gutter="20">
      <el-col v-bind="gridl">
        <tree :parent-tree="treeData" :frame-id="frame_id" :types="types" @frameId="getFrameId" @getList="getList" />
      </el-col>
      <el-col v-bind="gridr">
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
        <div class="v-height-flag">
          <el-table
            class="mt10"
            :data="tableData"
            :height="tableHeight"
            style="width: 100%"
            row-key="id"
            default-expand-all
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55"> </el-table-column>
            <el-table-column prop="name" label="物资名称" min-width="100" />
            <el-table-column prop="units" label="规格型号" min-width="100" />
            <el-table-column prop="cate.cate_name" label="物资分类" min-width="100" />
            <el-table-column prop="specs" label="计量单位" min-width="80" />
            <el-table-column prop="stock" label="库存数量" min-width="80" />
            <el-table-column prop="used" label="领用数量" min-width="80" />
            <el-table-column prop="describe" :label="$t('public.operation')" fixed="right" width="200">
              <template slot-scope="scope">
                <el-button type="text" @click="handleRecord(scope.row)">记录</el-button>

                <el-button type="text" @click="handleStock(scope.row)">补货</el-button>

                <el-dropdown class="ml10">
                  <span class="el-dropdown-link el-button--text el-button">
                    {{ $t('hr.more') }}
                    <i class="el-icon-arrow-down el-icon--right" />
                  </span>
                  <el-dropdown-menu style="text-align: center">
                    <el-dropdown-item @click.native="handleCollection(scope.row)"> 领用 </el-dropdown-item>
                    <el-dropdown-item @click.native="handleEdit(scope.row)">
                      {{ $t('public.edit') }}
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="handleDelete(scope.row.id)">删除</el-dropdown-item>
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
    <material-dialog ref="materialDialog" :from-data="stockData" @isOk="handleSearch()"></material-dialog>
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
import tree from './tree'
import addMaterial from './addMaterial'
import receive from './receive'
import record from './record'
import materialDialog from './materialDialog'
import oaFromBox from '@/components/common/oaFromBox'
import { storageCateApi, storageDeleteApi, storageListApi, storageListCateApi } from '@/api/administration'
export default {
  name: 'Consume',
  components: {
    tree,
    addMaterial,
    receive,
    record,
    materialDialog,
    oaFromBox,
    oaDialog: () => import('@/components/form-common/dialog-form')
  },
  data() {
    return {
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
      formConfig: [],
      multipleSelection: [],
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
      alertText: '消耗物资经过领取之后，可供领取人使用耗尽，不需要归还，例如：卫生纸、签字笔等。',
      treeData: [],
      frame_id: 0,
      search: [
        {
          field_name: '物资名称',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '入库时间',
          field_name_en: 'time',
          form_value: 'date_picker'
        }
      ],
      dropdownList: [
        {
          label: '领用',
          value: 1
        },
        {
          label: '批量移动',
          value: 2
        }
      ],
      where: {
        page: 1,
        limit: 15,
        cid: '',
        types: 0
      },

      total: 0,
      tableData: [],
      fromData: {},
      receiveData: {},
      recordData: {},
      selectData: [],
      selectReceive: [],
      stockData: {},
      types: 0
    }
  },
  mounted() {
    this.getList()
    this.getSelectTableData('')
    this.getSelectTableData(1)
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = []
      val.map((item) => {
        this.multipleSelection.push(item.id)
      })
    },
    handleSearch() {
      this.getTableData()
    },
    // 获取权限列表
    getList() {
      storageCateApi({ type: this.types }).then(async (res) => {
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
    },
    dropdownFn(data) {
      if (data.value == '1') {
        this.handleReceive()
      } else {
        if (this.multipleSelection.length == 0) return this.$message.error('请选择要操作的物资')
        this.$refs.oaDialog.openBox()
      }
    },

    // 领用
    handleCollection(item) {
      this.stockData = {
        title: '物资领用（消耗物资）',
        width: '620px',
        label: '备 注',
        placeholder: '请输入备注',
        data: item,
        type: 7
      }
      this.$refs.materialDialog.handleOpen()
    },
    submit(data) {
      data.ids = this.multipleSelection
      data.cate_id = data.cate_id[data.cate_id.length - 1]
      storageListCateApi(data).then((res) => {
        if (res.status == 200) {
          this.$refs.oaDialog.handleClose()
          setTimeout(() => {
            this.getTableData()
          }, 300)
        }
      })
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },

    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: 15,
          cid: '',
          types: 0
        }
        this.getTableData()
      } else {
        this.where = { ...this.where, ...data }
        this.where.page = 1
        this.getTableData()
      }
    },
    // 获取表格数据
    async getTableData() {
      const result = await storageListApi(this.where)
      this.tableData = result.data.list || []
      this.total = result.data.count
    },
    getSelectTableData(stock) {
      storageListApi({ types: this.types, page: 0, limit: 0, stock: stock })
        .then((res) => {
          if (stock === '') {
            this.selectData = res.data.list || []
          } else if (stock === 1) {
            // 有库存
            this.selectReceive = res.data.list || []
          }
        })
        .catch((error) => {})
    },

    async handleDelete(id) {
      await this.$modalSure('你确定要删除这条内容吗')
      await storageDeleteApi(id)
      this.getTableData()
    },
    getFrameId(data) {
      this.where.cid = data ? data : ''
      this.where.page = 1
      this.getTableData()
    },
    handleManage() {
      this.fromData = {
        title: '新增入库(消耗物资)',
        width: 720,
        treeData: this.treeData,
        selectData: this.selectData,
        edit: false,
        type: this.types
      }
      this.$refs.addMaterial.openBox()
    },
    handleEdit(row) {
      if (row.cate && row.cate.path.length <= 0) {
        row.cate.path.unshift(0) // 添加总分类
      }
      if (row.cate && !row.cate.path.includes(row.cid)) {
        row.cate.path.push(row.cid) // 添加当前分类
      }
      this.fromData = {
        title: '编辑入库(消耗物资)',
        width: 720,
        treeData: this.treeData,
        edit: true,
        data: row,
        type: this.types
      }

      this.$refs.addMaterial.openBox()
    },
    handleReceive() {
      this.receiveData = {
        title: '领用(消耗物资)',
        width: 820,
        selectData: this.selectReceive,
        type: this.types
      }
      this.$refs.receive.openBox()
    },
    handleRecord(row) {
      this.recordData = {
        title: '记录详情(消耗物资)',
        width: 820,
        data: row,
        type: this.types
      }
      this.$refs.record.where.storage_id = row.id
      this.$refs.record.usersWhere.storage_id = row.id
      this.$refs.record.openBox()
    },
    handleStock(row) {
      this.stockData = {
        title: '补货',
        width: '520px',
        data: row,
        label: '入库说明',
        placeholder: '请填写入库说明',
        type: 4
      }
      this.$refs.materialDialog.handleOpen()
    }
  }
}
</script>

<style lang="scss" scoped>
.table-box {
  ::v-deep .el-table th {
    background-color: rgba(247, 251, 255, 1);
  }
}
</style>
