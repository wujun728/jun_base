<!-- 客户-联系人页面组件 -->
<template>
  <div class="station">
    <div class="btn-box1 mb10">
      <div class="title-16">联系人列表</div>
      <el-button @click="addLiaison()" size="small" type="primary">{{ $t('customer.addliaison') }}</el-button>
    </div>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column v-for="header in tableHeaders" :key="header.field" :prop="header.field" :label="header.name">
        <template slot-scope="scope">
          <span>{{ scope.row[header.field] || '--' }}</span>
          <span v-if="header.field == 'liaison_name'">
            <i v-if="scope.row['e06d7153'] == '0'"> </i>
            <i v-if="scope.row['e06d7153'] == '2'" class="el-icon-female"></i>
            <i v-if="scope.row['e06d7153'] == '1'" class="el-icon-male"></i>
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="address" width="110" :label="$t('public.operation')">
        <template slot="header">
          操作
          <i class="el-icon-setting pointer" @click="customSearchEvt"></i>
        </template>
        <template slot-scope="scope">
          <el-button @click="addLiaison('edit', scope.row)" type="text">{{ $t('public.edit') }}</el-button>
          <el-button @click="deleteLiaison(scope.row, scope.$index)" type="text">{{ $t('public.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        :page-size="where.limit"
        :current-page="where.page"
        layout="total, prev, pager, next, jumper"
        :total="liaisonTotal"
        @current-change="pageChange"
      />
    </div>

    <liaison-dialog ref="liaisonDialog" :formData="liaisonConfig" @isLiaison="getTableData"></liaison-dialog>
    <visible-dialog
      :default-table-item-list="defaultTableItemList"
      :table-item-dialog-visible="tableItemDialogVisible"
      v-if="tableItemDialogVisible"
      :visible-table-item-list="visibleTableItemList"
      :transfer-data-list="transferDataList"
      :transfer-props="{ key: 'field', label: 'name' }"
      @handleCloseTableItem="handleCloseTableItem"
      @handleConfirmVisible="handleConfirmVisible"
    ></visible-dialog>
  </div>
</template>

<script>
import liaisonDialog from '@/views/customer/list/components/liaisonDialog'
import { clientLiaisonDeleteApi, salesmanCustomApi, saveSalesmanCustomApi, liaisonViewApi } from '@/api/enterprise'
export default {
  name: 'Liaison',
  props: {
    formInfo: {
      type: Object,
      default: () => {
        return {}
      }
    },
    custom_type: {
      type: Number,
      default: 1
    },
    customInfo: {
      // 客户信息 客户id 和类型
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  components: {
    liaisonDialog,
    VisibleDialog: () => import('@/components/form-common/dialog-transfer')
  },
  data() {
    return {
      liaisonTotal: 0,
      liaisonData: [],
      liaisonConfig: {},
      tableItemDialogVisible: false,
      defaultTableItemList: [],
      transferDataList: [],
      visibleTableItemList: [],
      getSalesmanCustom: [],
      where: {
        page: 1,
        limit: 15,
        types: 1,
        eid: 0
      },
      tableData: [], // 表格的数据
      tableHeaders: [], // 表格的表头
      localCustomType: this.custom_type
    }
  },
  created() {
    this.salesmanCustom()
    this.getTableData()
  },
  methods: {
    getTableData(condition = false) {
      if (!condition) {
        if (this.loading) return
        this.loading = true
      }
      const typeMapping = {
        1: 117,
        2: 127,
        3: 137,
        4: 147
      }
      this.localCustomType = typeMapping[this.localCustomType] || this.localCustomType
      this.where.types = this.localCustomType
      this.where.eid = this.customInfo.id
      liaisonViewApi(this.where)
        .then((res) => {
          this.tableData = res.data.list
          this.liaisonTotal = res.data.count
          this.total_price = res.data.total_price || 0
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
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
      const typeMapping = {
        1: 117,
        2: 127,
        3: 137,
        4: 147
      }
      this.localCustomType = typeMapping[this.localCustomType] || this.localCustomType
      saveSalesmanCustomApi(this.localCustomType, data).then((res) => {
        this.tableItemDialogVisible = false
        this.salesmanCustom()
        this.getTableData()
      })
    },
    salesmanCustom() {
      const typeMapping = {
        1: 117,
        2: 127,
        3: 137,
        4: 147
      }
      this.localCustomType = typeMapping[this.localCustomType] || this.localCustomType
      salesmanCustomApi(this.localCustomType).then((res) => {
        const { list, list_select } = res.data
        this.transferDataList = list
        this.defaultTableItemList = list_select
        const fieldMap = res.data.list.reduce((map, item) => {
          map[item.field] = item
          return map
        }, {})
        // 按 list_select 顺序提取数据
        this.tableHeaders = res.data.list_select.map((field) => fieldMap[field]).filter((item) => item)
        this.tableHeaders = this.tableHeaders.filter((obj) => obj.name !== '性别')
        this.getSalesmanCustom = res.data
      })
    },
    customSearchEvt() {
      this.tableItemDialogVisible = true
    },
    // 获取联系人

    pageChange(val) {
      this.where.page = val
      this.getTableData()
    },
    // 添加编辑联系人
    addLiaison(edit, row) {
      this.liaisonConfig = {
        title: edit !== 'edit' ? this.$t('customer.addliaison') : this.$t('customer.editliaison'),
        width: '570px'
      }
      this.$refs.liaisonDialog.openBox(row, this.customInfo, edit)
    },
    getGender(id) {
      var str = ''
      if (id === 1) {
        str = this.$t('hr.male')
      } else if (id === 2) {
        str = this.$t('hr.female')
      } else {
        str = this.$t('hr.unknown')
      }
      return str
    },
    // 删除联系人
    async deleteLiaison(row, index) {
      await this.$modalSure(this.$t('customer.message07'))
      await clientLiaisonDeleteApi(row.id)
      this.tableData.splice(index, 1)
      this.liaisonTotal = this.liaisonData.length
      if (this.where.page > 1 && this.tableData.length <= 0) {
        this.where.page--
        this.getTableData()
      }
    }
  }
}
</script>
<style></style>

<style lang="scss" scoped>
.station {
  height: 100%;
}
.btn-box1 {
  height: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.el-icon-male {
  color: #1890ff;
  font-size: 13px;
}
.el-icon-female {
  color: #f95c96;
  font-size: 13px;
}
.hand {
  cursor: pointer;
}
::v-deep .el-input__inner {
  text-align: left;
}
.from-item-title {
  border-left: 5px solid #1890ff;
  span {
    padding-left: 10px;
    font-weight: bold;
    font-size: 14px;
  }
}
</style>
